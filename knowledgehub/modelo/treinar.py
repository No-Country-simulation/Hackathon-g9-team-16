"""
Treina os modelos de classificacao do KnowledgeHub — DOIS NIVEIS.

Estrategia: dois classificadores independentes, ambos TF-IDF + Regressao Logistica.
  - modelo de AREA    -> prediz o nivel 1 (ex: "Inteligencia Artificial")
  - modelo de SUBAREA -> prediz o nivel 2 (ex: "Machine Learning")

Salva em modelo/modelo_knowledgehub.joblib um arquivo com:
  - pipeline_area, pipeline_subarea (os dois modelos treinados)
  - vetorizador compartilhado para palavras-chave e recomendacao
  - matriz TF-IDF da base + titulos/areas/subareas (para recomendar)

Rodar:  python modelo/treinar.py
"""

import json
from pathlib import Path

import joblib
import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import classification_report
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline

RAIZ = Path(__file__).resolve().parent.parent
CAMINHO_DADOS = RAIZ / "dados" / "dataset.csv"
CAMINHO_REAIS = RAIZ / "dados" / "artigos_reais.csv"
CAMINHO_MODELO = RAIZ / "modelo" / "modelo_knowledgehub.joblib"
CAMINHO_METRICAS = RAIZ / "modelo" / "metricas.json"

STOPWORDS_PT = [
    "a", "ao", "aos", "as", "com", "como", "da", "das", "de", "do", "dos", "e",
    "em", "essa", "esse", "esta", "este", "eu", "isso", "na", "nas", "no", "nos",
    "o", "os", "ou", "para", "pela", "pelo", "por", "que", "se", "sem", "ser",
    "sobre", "sua", "suas", "seu", "seus", "tem", "um", "uma", "the", "of",
    "neste", "sao", "conteudo", "material", "texto", "final",
    "abordados", "temas", "apresentados", "inclui", "pratico", "guia",
    "artigo", "tutorial", "explica", "forma", "reais", "exemplos",
    "voce", "ja", "parou", "pensar", "simples", "imagine", "seguinte",
    "trata", "muita", "gente", "duvida", "vamos", "falar", "jeito", "facil",
    "ideia", "central", "gira", "torno", "envolve", "relacionado",
]

def juntar_texto(df):
    return df["titulo"].fillna("") + " " + df["texto"].fillna("")

def novo_pipeline():
    return Pipeline([
        ("tfidf", TfidfVectorizer(
            stop_words=STOPWORDS_PT, ngram_range=(1, 2),
            min_df=2, sublinear_tf=True)),
        ("clf", LogisticRegression(max_iter=1000, C=5.0)),
    ])

def treinar_nivel(nome, X, y):
    print(f"\n=== Nivel: {nome} ===")
    X_tr, X_te, y_tr, y_te = train_test_split(
        X, y, test_size=0.2, random_state=42, stratify=y)
    pipe = novo_pipeline()
    pipe.fit(X_tr, y_tr)
    y_prev = pipe.predict(X_te)
    print(classification_report(y_te, y_prev, zero_division=0))
    relatorio = classification_report(y_te, y_prev, output_dict=True, zero_division=0)
    pipe.fit(X, y)
    return pipe, relatorio

def main():
    print("Carregando a base...")
    df = pd.read_csv(CAMINHO_DADOS)

    if CAMINHO_REAIS.exists():
        df_reais = pd.read_csv(CAMINHO_REAIS)
        df = pd.concat([df, df_reais], ignore_index=True)
        print(f"  + {len(df_reais)} conteudos reais (artigos cientificos)")
    print(f"{len(df)} registros | {df['area'].nunique()} areas | "
          f"{df['subarea'].nunique()} subareas")

    X = juntar_texto(df)

    pipe_area, met_area = treinar_nivel("AREA", X, df["area"])
    pipe_sub, met_sub = treinar_nivel("SUBAREA", X, df["subarea"])

    vetorizador = pipe_area.named_steps["tfidf"]
    matriz_base = vetorizador.transform(X)

    artefato = {
        "pipeline_area": pipe_area,
        "pipeline_subarea": pipe_sub,
        "vetorizador": vetorizador,
        "matriz_base": matriz_base,
        "titulos": df["titulo"].tolist(),
        "areas": df["area"].tolist(),
        "subareas": df["subarea"].tolist(),
    }

    CAMINHO_MODELO.parent.mkdir(exist_ok=True)
    joblib.dump(artefato, CAMINHO_MODELO)
    print(f"\nModelo salvo em: {CAMINHO_MODELO}")

    with open(CAMINHO_METRICAS, "w", encoding="utf-8") as f:
        json.dump({"area": met_area, "subarea": met_sub}, f,
                  ensure_ascii=False, indent=2)

    print(f"Acuracia area:    {met_area['accuracy']:.2%}")
    print(f"Acuracia subarea: {met_sub['accuracy']:.2%}")

if __name__ == "__main__":
    main()
