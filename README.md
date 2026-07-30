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
| **Gabriel** | Back-End, Front-End & Infra/OCI |
| **Giovana** | Back-End & Endpoints/Validações |
| **Diego** | Back-End, Banco de Dados & Documentação |
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
Abra um **segundo terminal**:
```bash
./mvnw spring-boot:run
```

### 4. Acessar a interface
1. Abra `http://localhost:8080`
2. Vá em **Configurações** → ative **"Conexão de API em Tempo Real"** → salve
3. Vá em **Análise e Cadastro** → escolha um exemplo → clique em **Analisar**

> **Modo Mock:** Com o toggle desativado, o Front-End usa um motor heurístico local (sem Back-End).

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

**Validações:** `titulo` (5–150 chars, obrigatório) · `texto` (20–5000 chars, obrigatório)

**Response `200 OK`:**
```json
{
  "categoria": "Backend",
  "probabilidade": 0.89,
  "palavrasChave": ["spring", "boot", "api", "rest", "java"],
  "resumo": "Introdução ao Spring Boot"
}
```

**Response `400 Bad Request`:**
```json
{ "titulo": "O título é obrigatório", "texto": "O texto técnico é obrigatório" }
```

### Outros Endpoints
| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/` | Health check |
| `GET` | `http://127.0.0.1:8000/saude` | Status do modelo e áreas |
| `GET` | `http://127.0.0.1:8000/taxonomia` | Lista áreas e subáreas |
| `GET` | `http://localhost:8080/swagger-ui.html` | Documentação Swagger |

---

## Modelo de Ciência de Dados

**Abordagem:** Dois classificadores encadeados de Regressão Logística (área + subárea).

**Pipeline NLP:**
1. Limpeza: lowercasing, remoção de pontuação e dígitos
2. Stopwords em português (NLTK)
3. Vetorização TF-IDF (n-grams 1–2, `max_features=1000`)
4. Treinamento: `LogisticRegression(C=1.0, solver='lbfgs')`
5. Palavras-chave: top-N termos por peso TF-IDF
6. Recomendação: `cosine_similarity` contra a matriz base

**Dataset:** 295 exemplos · 7 áreas · 30+ subáreas em `knowledgehub/dados/dataset.csv`

**Métricas:**
| Nível | Accuracy | F1 (macro) |
|---|---|---|
| Área | 93.75% | 93.78% |
| Subárea | ~75% | ~75% |

> Detalhes em `knowledgehub/modelo/metricas.json`.

**Serialização:** `joblib` → `modelo_knowledgehub.joblib` (344 KB) contendo pipelines, vetorizador, matriz base e metadados.

**Notebook de EDA:** `knowledgehub/notebook/knowledgehub_analise.ipynb`

---

## Exemplos de Uso (3 Cenários)

### 1. Back-End
```json
// Request
{ "titulo": "Introdução ao Spring Boot", "texto": "Conceitos básicos para criação de APIs REST com Java e Spring Boot." }
// Response
{ "categoria": "Backend", "probabilidade": 0.89, "palavrasChave": ["spring", "boot", "api", "rest", "java"] }
```

### 2. Data Science
```json
// Request
{ "titulo": "O que é machine learning?", "texto": "Os robôs possuem softwares que possibilitam o aprendizado a partir das interações." }
// Response
{ "categoria": "Machine Learning", "probabilidade": 0.76, "palavrasChave": ["machine", "learning", "aprendizado"] }
```

### 3. DevOps/Cloud
```json
// Request
{ "titulo": "Deploy de containers na nuvem", "texto": "Guia prático de containerização com Docker e publicação usando OCI." }
// Response
{ "categoria": "Cloud", "probabilidade": 0.92, "palavrasChave": ["deploy", "containers", "docker", "cloud", "oci"] }
```

---

## Integração com OCI

| Serviço | Uso |
|---|---|
| **OCI Object Storage** | Armazenamento do modelo `.joblib` em bucket |
| **OCI Compute** | Hospedagem da aplicação (VM Ubuntu 22.04) |

### Gerenciar o modelo na nuvem
```bash
pip install oci  # uma vez
python knowledgehub/oci_storage.py enviar   # upload
python knowledgehub/oci_storage.py baixar   # download
python knowledgehub/oci_storage.py listar   # listar
```
**Bucket:** `knowledgehub-modelos` · Pré-requisito: configurar `~/.oci/config` no console OCI.

---

## Testes Automatizados

```bash
./mvnw test
```

| Teste | Camada |
|---|---|
| `TechmindApiApplicationTests` | Contexto Spring |
| `SecurityConfigTest` | CORS e permissões |
| `ConteudoControllerTest` | `POST /conteudo` |
| `HealthControllerTest` | `GET /` |
| `ConteudoServiceTest` | Mapeamento e fallback |

---

## Deploy em Produção (OCI Compute)

1. **Provisionar VM:** Ubuntu 22.04 LTS, Always Free. Liberar portas 8080 (TCP) e 22 (SSH) na Security List.
2. **Instalar dependências:**
   ```bash
   sudo apt install -y openjdk-17-jdk python3-pip postgresql
   pip3 install -r knowledgehub/requirements.txt
   ```
3. **Build e transferência:**
   ```bash
   ./mvnw clean package -DskipTests
   scp -i "chave.key" target/techmind-api-0.0.1-SNAPSHOT.jar ubuntu@<IP>:~/
   scp -i "chave.key" -r knowledgehub/ ubuntu@<IP>:~/
   ```
4. **Executar na VM:**
   ```bash
   nohup python3 -m uvicorn knowledgehub.api.main:app --host 0.0.0.0 --port 8000 > fastapi.log 2>&1 &
   nohup java -jar techmind-api-0.0.1-SNAPSHOT.jar > app.log 2>&1 &
   ```
5. **Acessar:** `http://<IP_PUBLICO>:8080`

**Docker (alternativa):**
```bash
docker build -t techmind-api .
docker run -p 8080:8080 techmind-api
```

---

## Tecnologias

**Back-End:** Java 17 · Spring Boot 3.5.16 · Spring Security 6.x · SpringDoc OpenAPI 2.8.5 · Lombok · Maven
**Data Science:** Python 3.10+ · FastAPI 0.110+ · Scikit-Learn 1.3+ · Pandas 2.0+ · NumPy 1.24+ · Joblib 1.3+ · OCI SDK
**Front-End:** HTML5 · CSS3 (Glassmorphism) · JavaScript ES6+ (Fetch API) · Google Fonts
**Infra:** OCI Object Storage · OCI Compute · Docker · GitHub Actions (CI/CD)

---

## Branches

| Branch | Responsável | Descrição |
|---|---|---|
| `main` | — | Produção |
| `feature/front-end` | Gabriel | SPA Vanilla JS, integração fetch ↔ Spring Boot |
| `feature/backend-alissonls` | Alisson | Security, Swagger, testes, Docker, CI/CD |
| `feature/backend-giovana` | Giovana | PythonApiClient, DTOs tipados, persistência |
| `feature/backend-diego` | Diego | Entidades JPA, CRUD, DEPLOY.md |
| `data-science` | Alice, Julia | Dataset, NLP, TF-IDF, modelo, notebook, OCI |

---

Projeto desenvolvido para o **Hackathon ONE — Alura + Oracle** (Equipe 16).
