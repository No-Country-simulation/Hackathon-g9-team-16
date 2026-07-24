"""
Modulo de inferencia do KnowledgeHub — DOIS NIVEIS.

    processar(titulo, texto) -> dict

Devolve area, subarea, probabilidades, palavras-chave e conteudos relacionados,
no formato do contrato acordado com a equipe.
"""

from pathlib import Path

import joblib
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity

CAMINHO_MODELO = Path(__file__).resolve().parent / "modelo_knowledgehub.joblib"
_artefato = None

def carregar_modelo(caminho: Path = CAMINHO_MODELO):
    """Carrega o .joblib UMA vez (na primeira chamada)."""
    global _artefato
    if _artefato is None:
        if not caminho.exists():
            raise FileNotFoundError(
                f"Modelo nao encontrado em {caminho}. "
                "Rode 'python modelo/treinar.py' antes de subir a API."
            )
        _artefato = joblib.load(caminho)
    return _artefato

def extrair_palavras_chave(texto_completo, quantidade=5):
    art = carregar_modelo()
    vet = art["vetorizador"]
    vetor = vet.transform([texto_completo])
    if vetor.nnz == 0:
        return []
    termos = np.array(vet.get_feature_names_out())
    pesos = vetor.toarray()[0]
    melhores = pesos.argsort()[::-1][:quantidade]
    return [termos[i] for i in melhores if pesos[i] > 0]

def buscar_relacionados(texto_completo, quantidade=3):
    art = carregar_modelo()
    vetor = art["vetorizador"].transform([texto_completo])
    sims = cosine_similarity(vetor, art["matriz_base"])[0]

    resultados, vistos = [], set()
    for i in sims.argsort()[::-1]:
        if len(resultados) >= quantidade or sims[i] <= 0.05:
            break
        titulo = art["titulos"][i]
        if titulo in vistos:
            continue
        vistos.add(titulo)
        resultados.append({
            "titulo": titulo,
            "area": art["areas"][i],
            "subarea": art["subareas"][i],
            "similaridade": round(float(sims[i]), 3),
        })
    return resultados

def _prever_com_prob(pipeline, texto):
    """Devolve (classe_prevista, probabilidade_da_classe)."""
    classe = pipeline.predict([texto])[0]
    probs = pipeline.predict_proba([texto])[0]
    prob = float(probs.max())
    return str(classe), round(prob, 4)

def processar(titulo, texto):
    """
    Funcao principal consumida pela API.

    Exemplo de retorno:
    {
      "titulo": "...",
      "classificacao": {
        "area_principal": "Inteligencia Artificial",
        "subarea": "Machine Learning",
        "confianca_area": 0.91,
        "confianca_subarea": 0.78
      },
      "palavras_chave": [...],
      "conteudos_relacionados": [...]
    }
    """
    art = carregar_modelo()
    texto_completo = f"{titulo or ''} {texto or ''}".strip()

    area, conf_area = _prever_com_prob(art["pipeline_area"], texto_completo)
    subarea, conf_sub = _prever_com_prob(art["pipeline_subarea"], texto_completo)

    return {
        "titulo": titulo,
        "classificacao": {
            "area_principal": area,
            "subarea": subarea,
            "confianca_area": conf_area,
            "confianca_subarea": conf_sub,
        },
        "palavras_chave": extrair_palavras_chave(texto_completo),
        "conteudos_relacionados": buscar_relacionados(texto_completo),
    }

if __name__ == "__main__":
    import json
    exemplos = [
        ("O que e machine learning e como ele funciona?",
         "Os robos, como sao chamados, sao equipamentos que possuem softwares "
         "que possibilitam o aprendizado e o desenvolvimento de novas "
         "habilidades a partir das interacoes que realizam."),
        ("Introducao ao Spring Boot",
         "Conceitos basicos para criacao de APIs REST utilizando Java e Spring Boot."),
        ("Deploy de containers na nuvem",
         "Guia pratico de containerizacao com Docker e publicacao usando OCI."),
    ]
    for titulo, texto in exemplos:
        print(json.dumps(processar(titulo, texto), ensure_ascii=False, indent=2))
        print("-" * 70)
