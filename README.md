# TechMind — Repositório Inteligente de Conhecimento Técnico

> **Hackathon ONE — Alura + Oracle | Equipe 16**
> Organização inteligente de conteúdo técnico com Machine Learning, API REST e Oracle Cloud Infrastructure (OCI).

O **TechMind** recebe textos técnicos (artigos, tutoriais, documentação), utiliza **ML Clássico** (Regressão Logística + TF-IDF) para classificar em dois níveis (área principal e subárea), extrair palavras-chave e recomendar conteúdos relacionados — devolvendo tudo em **JSON**.

---

## Arquitetura

```
Front-End (SPA Vanilla JS)  →  POST /conteudo  →  Spring Boot (Java 17)
                                                         ↓ HTTP POST
                                              FastAPI (Python, porta 8000)
                                                         ↓ carrega .joblib
                                              Modelo ML (Scikit-Learn)
                                                         ↓
                                              Oracle Cloud (OCI)
```

**Fluxo:** O usuário envia um texto → o Spring Boot valida e repassa à API Python → o modelo vetoriza com TF-IDF, classifica e extrai palavras-chave → o Java mapeia em JSON e devolve ao Front-End.

---

## Equipe

| Membro | Função |
|---|---|
| **Gabriel** |  Front-End |
| **Giovana** | Back-End & Endpoints/Validações |
| **Diego** | Back-End, Banco de Dados & Documentação & Infra/OCI|
| **Alice** | Data Science — NLP & Modelagem |
| **Julia** | Data Analytics & Tratamento/EDA |
| **Alisson** | Back-End — Security & DevOps |

---

## Estrutura do Repositório

```
├── knowledgehub/               # Data Science (Alice, Julia)
│   ├── api/main.py             # API FastAPI (porta 8000)
│   ├── dados/dataset.csv       # 295 exemplos rotulados
│   ├── modelo/
│   │   ├── treinar.py          # Pipeline de treino (2 níveis)
│   │   ├── inferencia.py       # Módulo de inferência
│   │   ├── modelo_knowledgehub.joblib  # Modelo serializado (344 KB)
│   │   └── metricas.json       # Métricas de avaliação
│   ├── notebook/               # Notebook de EDA
│   └── oci_storage.py          # Upload/download do modelo (OCI)
├── src/main/java/.../techmind/ # Back-End Spring Boot
│   ├── config/                 # Security, CORS, Swagger
│   ├── controller/             # POST /conteudo, GET /
│   ├── dto/                    # ConteudoRequest, ConteudoResponse
│   ├── exception/              # GlobalExceptionHandler
│   └── service/                # ConteudoService (chama FastAPI)
├── src/main/resources/static/  # Front-End (HTML/CSS/JS)
├── src/test/java/              # Testes automatizados
├── Dockerfile                  # Containerização
└── pom.xml                     # Maven (Spring Boot 3.5.16)
```

---

## Como Executar Localmente

**Pré-requisitos:** Java JDK 17, Python 3.10+, Git

### 1. Clonar
```bash
git clone https://github.com/No-Country-simulation/Hackathon-g9-team-16.git
cd Hackathon-g9-team-16
```

### 2. Subir a API de IA (FastAPI — porta 8000)
```bash
pip install -r knowledgehub/requirements.txt
python -m uvicorn knowledgehub.api.main:app --reload
```

### 3. Subir o Back-End (Spring Boot — porta 8080)
```bash
./mvnw spring-boot:run
```

---

## API REST

### `POST /conteudo`

**Request:**
```json
{
  "titulo": "Introdução ao Spring Boot",
  "texto": "Conceitos básicos para criação de APIs REST com Java e Spring Boot."
}
```

**Response `200 OK`:**
```json
{
  "categoria": "Backend",
  "probabilidade": 0.89,
  "palavrasChave": ["spring", "boot", "api", "rest", "java"],
  "resumo": "Introdução ao Spring Boot"
}
```

---

## Deploy em Produção (OCI Compute)

Consulte o guia detalhado em [DEPLOY.md](file:///c:/Users/Gabriel/Documents/projetos/Hackathon-g9-team-16/DEPLOY.md).

---

Projeto desenvolvido para o **Hackathon ONE — Alura + Oracle** (Equipe 16).RUD, DEPLOY.md |
| `data-science` | Alice, Julia | Dataset, NLP, TF-IDF, modelo, notebook, OCI |

---

Projeto desenvolvido para o **Hackathon ONE — Alura + Oracle** (Equipe 16).
=======
---

## Serviço Python

Instale as dependências:

```bash
pip install -r requirements.txt
```

Execute o serviço:

```bash
uvicorn api.main:app --reload
```

Documentação interativa:

```
http://127.0.0.1:8000/docs
```

---
>>>>>>> origin/feature/backend-giovana
