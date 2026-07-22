"""
Gera a base de conteudos tecnicos do KnowledgeHub — versao com DOIS NIVEIS.

Cada conteudo recebe:
  - area   (nivel 1, ex: "Inteligencia Artificial")
  - subarea(nivel 2, ex: "Machine Learning")

E os textos vem em DOIS ESTILOS misturados:
  - "tecnico": estilo documentacao/tutorial
  - "explicativo": estilo linguagem natural, como alguem explicando o conceito

Saida: dados/dataset.csv com colunas titulo, texto, area, subarea
"""

import random
import csv
from pathlib import Path

random.seed(42)

TAXONOMIA = {
    "Backend": {
        "APIs e Servicos": [
            ("APIs REST com Spring Boot", ["Java", "Spring Boot", "API REST"]),
            ("criacao de servicos com FastAPI", ["Python", "FastAPI", "API REST"]),
            ("endpoints em Node.js", ["Node.js", "Express", "API REST"]),
            ("versionamento de APIs", ["API REST", "boas praticas", "documentacao"]),
        ],
        "Autenticacao e Seguranca": [
            ("autenticacao com JWT", ["JWT", "seguranca", "autenticacao"]),
            ("controle de acesso e permissoes", ["seguranca", "autorizacao", "roles"]),
            ("protecao contra ataques comuns", ["seguranca", "OWASP", "vulnerabilidades"]),
        ],
        "Arquitetura": [
            ("arquitetura de microsservicos", ["microsservicos", "arquitetura", "escalabilidade"]),
            ("injecao de dependencias", ["Spring", "arquitetura", "boas praticas"]),
            ("filas de mensagens com RabbitMQ", ["RabbitMQ", "mensageria", "assincrono"]),
        ],
    },
    "Frontend": {
        "Componentes e Estado": [
            ("componentes reutilizaveis em React", ["React", "JavaScript", "componentes"]),
            ("gerenciamento de estado", ["React", "Redux", "estado"]),
            ("tipagem de componentes com TypeScript", ["TypeScript", "React", "tipagem"]),
        ],
        "Estilizacao e Layout": [
            ("estilizacao com CSS moderno", ["CSS", "Flexbox", "Grid"]),
            ("responsividade e mobile first", ["CSS", "responsivo", "UX"]),
            ("acessibilidade em interfaces web", ["acessibilidade", "HTML", "UX"]),
        ],
        "Integracao": [
            ("consumo de APIs no navegador", ["JavaScript", "fetch", "API REST"]),
            ("roteamento em aplicacoes SPA", ["React Router", "SPA", "navegacao"]),
            ("formularios e validacao no cliente", ["JavaScript", "formularios", "validacao"]),
        ],
    },
    "Inteligencia Artificial": {
        "Machine Learning": [
            ("o que e machine learning e como funciona", ["machine learning", "aprendizado", "modelos"]),
            ("aprendizado supervisionado e nao supervisionado", ["supervisionado", "nao supervisionado", "aprendizado"]),
            ("modelos de classificacao supervisionada", ["Scikit-Learn", "classificacao", "machine learning"]),
            ("agrupamento com KMeans", ["KMeans", "clusterizacao", "nao supervisionado"]),
        ],
        "Deep Learning": [
            ("introducao a redes neurais", ["redes neurais", "deep learning", "neuronios"]),
            ("o que e deep learning", ["deep learning", "redes neurais", "camadas"]),
            ("visao computacional com CNNs", ["CNN", "visao computacional", "imagens"]),
        ],
        "Processamento de Linguagem Natural": [
            ("processamento de linguagem natural", ["NLP", "texto", "linguagem"]),
            ("vetorizacao de textos com TF-IDF", ["TF-IDF", "NLP", "vetorizacao"]),
            ("similaridade textual entre documentos", ["similaridade", "cosseno", "NLP"]),
        ],
    },
    "Ciencia de Dados": {
        "Analise de Dados": [
            ("analise exploratoria de dados", ["Python", "Pandas", "EDA"]),
            ("limpeza e tratamento de dados", ["Pandas", "pre-processamento", "qualidade de dados"]),
            ("engenharia de atributos", ["feature engineering", "Pandas", "modelagem"]),
        ],
        "Visualizacao": [
            ("visualizacao de dados", ["Matplotlib", "Seaborn", "graficos"]),
            ("construcao de dashboards", ["dashboard", "visualizacao", "BI"]),
        ],
        "Avaliacao de Modelos": [
            ("avaliacao de modelos preditivos", ["metricas", "F1-score", "validacao"]),
            ("validacao cruzada e overfitting", ["validacao cruzada", "overfitting", "generalizacao"]),
        ],
    },
    "Banco de Dados": {
        "SQL": [
            ("consultas SQL com JOIN", ["SQL", "JOIN", "consultas"]),
            ("funcoes de agregacao e agrupamento", ["SQL", "GROUP BY", "agregacao"]),
            ("indices e performance de consultas", ["indices", "performance", "SQL"]),
        ],
        "Modelagem": [
            ("modelagem relacional", ["modelagem", "normalizacao", "SQL"]),
            ("transacoes e integridade de dados", ["ACID", "transacoes", "SQL"]),
            ("modelagem dimensional para BI", ["data warehouse", "BI", "modelagem"]),
        ],
        "NoSQL": [
            ("bancos NoSQL orientados a documentos", ["MongoDB", "NoSQL", "documentos"]),
            ("quando usar NoSQL em vez de SQL", ["NoSQL", "escalabilidade", "flexibilidade"]),
        ],
    },
    "DevOps e Cloud": {
        "Containers": [
            ("containerizacao de aplicacoes com Docker", ["Docker", "containers", "deploy"]),
            ("orquestracao de containers", ["Kubernetes", "orquestracao", "escalabilidade"]),
        ],
        "Cloud": [
            ("armazenamento de objetos na nuvem", ["OCI", "Object Storage", "cloud"]),
            ("provisionamento de maquinas virtuais", ["OCI Compute", "infraestrutura", "cloud"]),
            ("funcoes serverless", ["OCI Functions", "serverless", "cloud"]),
        ],
        "Automacao": [
            ("pipelines de integracao continua", ["CI/CD", "automacao", "GitHub Actions"]),
            ("deploy automatizado de APIs", ["deploy", "CI/CD", "API REST"]),
            ("versionamento de codigo com Git", ["Git", "versionamento", "colaboracao"]),
        ],
    },
    "Mobile": {
        "Android": [
            ("desenvolvimento de apps Android", ["Android", "Kotlin", "mobile"]),
            ("ciclo de vida de uma activity", ["Android", "Kotlin", "ciclo de vida"]),
            ("notificacoes push", ["Firebase", "notificacoes", "mobile"]),
        ],
        "Multiplataforma": [
            ("interfaces multiplataforma com Flutter", ["Flutter", "Dart", "multiplataforma"]),
            ("navegacao entre telas no React Native", ["React Native", "navegacao", "mobile"]),
        ],
        "Boas Praticas Mobile": [
            ("armazenamento local no dispositivo", ["SQLite", "mobile", "persistencia"]),
            ("testes em aplicacoes mobile", ["testes", "mobile", "qualidade"]),
        ],
    },
}

TECNICO = [
    "Neste conteudo sao apresentados os conceitos basicos de {assunto}. "
    "Sao abordados temas como {tec}. {compl}",
    "Este material aprofunda o tema de {assunto}, cobrindo {tec}. {compl}",
    "Um guia passo a passo sobre {assunto}, com foco em {tec}. {compl}",
    "Documentacao sobre {assunto}. Principais topicos: {tec}. {compl}",
]

EXPLICATIVO = [
    "Voce ja parou para pensar em {assunto}? De forma simples, e um tema que "
    "envolve {tec}. {compl}",
    "Para entender {assunto}, imagine o seguinte: trata-se de algo relacionado a "
    "{tec}. {compl}",
    "Muita gente tem duvida sobre {assunto}. Na pratica, tudo gira em torno de "
    "{tec}. {compl}",
    "Vamos falar sobre {assunto} de um jeito facil. A ideia central passa por "
    "{tec}. {compl}",
]

FORMATOS = ["Guia", "Tutorial", "Introducao a", "O que e", "Entendendo",
            "Anotacoes sobre", "Resumo:", "Como funciona"]

COMPLEMENTOS = [
    "O conteudo traz exemplos praticos.",
    "Ao final ha exercicios propostos.",
    "O material e voltado para iniciantes.",
    "Sao discutidos erros comuns e como evita-los.",
    "Inclui referencias para aprofundamento.",
    "Traz um exemplo do inicio ao fim.",
]

def aplicar_ruido(titulo, texto):
    if random.random() < 0.25:
        texto = texto.split(". ")[0] + "."
    if random.random() < 0.15:
        titulo = random.choice(
            ["Anotacoes de estudo", "Material de apoio", "Resumo da aula",
             "Conteudo complementar", "Notas rapidas"]
        )
    if random.random() < 0.20:
        palavras = [p for p in texto.split() if random.random() > 0.15]
        texto = " ".join(palavras)
    return titulo, texto

def gerar(por_subarea: int = 14):
    linhas = []
    for area, subareas in TAXONOMIA.items():
        for subarea, assuntos in subareas.items():
            for _ in range(por_subarea):
                assunto, tecnologias = random.choice(assuntos)
                formato = random.choice(FORMATOS)
                compl = random.choice(COMPLEMENTOS)
                modelo = random.choice(TECNICO if random.random() < 0.5 else EXPLICATIVO)
                texto = modelo.format(
                    assunto=assunto, tec=", ".join(tecnologias), compl=compl)
                titulo = f"{formato} {assunto}"
                titulo, texto = aplicar_ruido(titulo, texto)

                area_final, subarea_final = area, subarea
                if random.random() < 0.12:
                    outra_area = random.choice([a for a in TAXONOMIA if a != area])
                    sub2 = random.choice(list(TAXONOMIA[outra_area]))
                    assunto2, tec2 = random.choice(TAXONOMIA[outra_area][sub2])
                    texto += (f" O conteudo tambem aborda {assunto2}, "
                              f"mencionando {', '.join(tec2)}.")
                    if random.random() < 0.5:
                        area_final, subarea_final = outra_area, sub2

                linhas.append({
                    "titulo": titulo, "texto": texto,
                    "area": area_final, "subarea": subarea_final,
                })
    random.shuffle(linhas)
    return linhas

if __name__ == "__main__":
    linhas = gerar()
    destino = Path(__file__).parent / "dataset.csv"
    with open(destino, "w", newline="", encoding="utf-8") as f:
        escritor = csv.DictWriter(f, fieldnames=["titulo", "texto", "area", "subarea"])
        escritor.writeheader()
        escritor.writerows(linhas)
    areas = {}
    for l in linhas:
        areas[l["area"]] = areas.get(l["area"], 0) + 1
    n_sub = sum(len(s) for s in TAXONOMIA.values())
    print(f"Base gerada: {len(linhas)} registros")
    print(f"{len(TAXONOMIA)} areas, {n_sub} subareas")
    print(f"Arquivo: {destino}")
