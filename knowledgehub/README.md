# Hackathon ONE — Team 16 — KnowledgeHub

**Organização Inteligente de Conhecimento Técnico**

Solução que recebe conteúdos técnicos (título + texto) e devolve, em JSON,
uma **classificação em dois níveis** (área principal + subárea), a **confiança**
de cada nível, as **palavras-chave** e os **conteúdos relacionados** da base.

Parte de **Ciência de Dados** do projeto: o modelo e a API que o back-end consome.

---

## O contrato (o que a API devolve)

Este é o formato que o back-end e o front-end podem esperar:

```json
{
  "titulo": "O que é machine learning e como ele funciona?",
  "classificacao": {
    "area_principal": "Inteligencia Artificial",
    "subarea": "Machine Learning",
    "confianca_area": 0.89,
    "confianca_subarea": 0.76
  },
  "palavras_chave": ["machine learning", "aprendizado", "robos"],
  "conteudos_relacionados": [
    {
      "titulo": "Entendendo o que e machine learning",
      "area": "Inteligencia Artificial",
      "subarea": "Machine Learning",
      "similaridade": 0.593
    }
  ]
}
```

---

## Como funciona

```
        titulo + texto
              │
              ▼
   ┌────────────────────┐
   │  API REST (FastAPI) │   valida entrada, trata erros, libera CORS
   └─────────┬──────────┘
             │ chama processar()
             ▼
   ┌────────────────────┐
   │  modelo/inferencia  │   2 classificadores (área e subárea)
   │                     │   + palavras-chave + recomendação
   └─────────┬──────────┘
             │ carrega
             ▼
   modelo_knowledgehub.joblib  ←──  OCI Object Storage
             │
             ▼
         resposta JSON
```

---

## Estrutura

```
techmind/
├── dados/
│   ├── gerar_dataset.py       gera a base (área + subárea, textos mistos)
│   └── dataset.csv
├── modelo/
│   ├── treinar.py             treina os dois níveis
│   ├── inferencia.py          consumido pela API
│   ├── modelo_knowledgehub.joblib
│   └── metricas.json
├── api/
│   └── main.py                API REST (FastAPI)
├── notebook/
│   └── techmind_analise.ipynb EDA, treino e avaliação
├── oci_storage.py             integração OCI Object Storage
├── exemplos.http              exemplos de requisição
└── requirements.txt
```

---

## Como executar

```bash
pip install -r requirements.txt
python dados/gerar_dataset.py
python dados/artigos_reais.py
python modelo/treinar.py
uvicorn api.main:app --reload
```

Documentação interativa: **http://127.0.0.1:8000/docs**

---

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| GET | `/` | Status do serviço |
| GET | `/saude` | Modelo carregado + áreas e subáreas |
| POST | `/conteudo` | Processa um conteúdo |
| POST | `/conteudo/lote` | Processa até 100 conteúdos |
| GET | `/taxonomia` | Lista áreas e as subáreas de cada uma |

### Validações e erros

| Situação | Código |
|---|---|
| Título < 3 caracteres ou texto < 20 | 422 |
| Campo só com espaços | 422 |
| Modelo não treinado | 503 |
| Erro inesperado | 500 |

---

## Sobre o modelo

- **Dois níveis:** dois classificadores independentes (área e subárea), ambos
  **TF-IDF + Regressão Logística**.
- **Base própria:** ~290 conteúdos sintéticos **mais 24 trechos reais extraídos de
  4 artigos científicos** (segmentados por seção), totalizando ~318 registros em
  **7 áreas** e **21 subáreas**, misturando documentação técnica, textos
  explicativos e conteúdo acadêmico real.
- **Métrica:** F1-score macro. Resultado — **F1 ≈ 0,97 na área** e **≈ 0,85 na
  subárea**. A subárea acerta um pouco menos por ter mais classes (21 vs 7) e
  menos exemplos por classe, o que é esperado.
- **Onde erra:** conteúdos de fronteira, que citam dois assuntos ao mesmo tempo.
  A base foi construída de propósito com esses casos, para não gerar um
  resultado artificialmente perfeito.

### Áreas

Backend · Frontend · Inteligência Artificial · Ciência de Dados ·
Banco de Dados · DevOps e Cloud · Mobile

(cada uma com 2–3 subáreas — veja `GET /taxonomia`)

---

## Integração entre as equipes

- **Data Science (esta pasta):** entrega o `.joblib` e a função `processar()`,
  além da API que expõe o modelo em localhost.
- **Back-end:** consome a API (ou a função, se também for Python). Recebe o JSON
  do contrato acima e cuida do "caderno" do usuário, da confirmação e do resto.
- A API já libera **CORS**, então o front-end consegue chamá-la direto durante o
  desenvolvimento.

---

## Integração com OCI

Serviço: **OCI Object Storage**, para armazenar o modelo serializado.
O modelo é treinado localmente, enviado ao bucket com
`python oci_storage.py enviar`, e a aplicação baixa dele ao iniciar.

---

## Dependências

Python 3.12, scikit-learn 1.8, pandas 3.0, FastAPI 0.139. Ver `requirements.txt`.
