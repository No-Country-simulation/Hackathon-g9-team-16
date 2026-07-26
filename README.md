# TechMind

![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-purple?style=flat)
![Java](https://img.shields.io/badge/-Java-E76F00?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/-Spring%20Boot-6DB33F?style=flat&logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/-PostgreSQL-4169E1?style=flat&logo=postgresql&logoColor=white)
![Python](https://img.shields.io/badge/-Python-3776AB?style=flat&logo=python&logoColor=white)
![FastAPI](https://img.shields.io/badge/-FastAPI-009688?style=flat&logo=fastapi&logoColor=white)

Sistema inteligente para organização e classificação de conhecimento técnico utilizando Inteligência Artificial.

O TechMind recebe conteúdos técnicos, realiza a classificação automática utilizando Inteligência Artificial e retorna informações estruturadas como área, subárea, nível de confiança, palavras-chave e conteúdos semanticamente relacionados.

Projeto desenvolvido durante o Hackathon Oracle + Alura, integrando uma API backend em Java Spring Boot com um serviço de Inteligência Artificial desenvolvido em Python.

---

## Sobre o Projeto

Com o crescimento constante da quantidade de informações técnicas disponíveis, organizar e encontrar conteúdos relevantes tornou-se um desafio.

O TechMind tem como objetivo facilitar a organização do conhecimento técnico, permitindo que conteúdos sejam cadastrados, processados e classificados automaticamente utilizando Inteligência Artificial.

A solução é composta por dois serviços principais:

### Backend Java Spring Boot

Responsável por:

- Gerenciamento dos conteúdos;
- Validação das informações recebidas;
- Persistência dos dados;
- Comunicação com o serviço de Inteligência Artificial;
- Disponibilização da API REST.

### Serviço de Inteligência Artificial Python

Responsável por:

- Classificação dos conteúdos por área e subárea;
- Extração de palavras-chave;
- Análise de similaridade entre conteúdos;
- Identificação de conteúdos relacionados.

---

## Funcionalidades

- Cadastro e gerenciamento de conteúdos técnicos;
- Classificação automática utilizando IA;
- Identificação de palavras-chave relevantes;
- Recomendação de conteúdos semanticamente relacionados;
- Integração entre serviços Java e Python;
- Tratamento global de erros da API.

---

## Arquitetura da Solução

```text
Cliente
   |
   v
API REST Spring Boot
   |
   | HTTP REST
   |
   v
Serviço Python IA
   |
   v
Processamento NLP
(Classificação + Similaridade)
   |
   v
Resposta enriquecida
```

---

## Fluxo da Aplicação

1. O usuário envia um conteúdo técnico através da API.

2. O backend Java recebe e valida os dados.

3. O conteúdo é enviado para o serviço Python.

4. A Inteligência Artificial realiza:
    - classificação por área e subárea;
    - extração de palavras-chave;
    - análise de similaridade.

5. O backend recebe a resposta processada e retorna as informações ao usuário.

---


## API REST

### Criar conteúdo

POST

```
/v1/conteudos
```

Exemplo de requisição:

```json
{
  "titulo": "Introdução ao Spring Security",
  "texto": "Spring Security é um framework utilizado para adicionar autenticação e autorização em aplicações Java."
}
```

Exemplo de resposta:

```json
{
  "titulo": "Introdução ao Spring Security",
  "areaPrincipal": "Backend",
  "subarea": "APIs e Serviços",
  "confiancaArea": 0.7484,
  "confiancaSubarea": 0.242,
  "palavrasChave": [
    "spring",
    "java",
    "jwt"
  ],
  "conteudosRelacionados": [
    {
      "titulo": "O que é APIs REST com Spring Boot",
      "similaridade": 0.742,
      "nivelSimilaridade": "ALTA"
    }
  ]
}
```

Outros endpoints:

```
GET    /v1/conteudos
GET    /v1/conteudos/{id}
PUT    /v1/conteudos/{id}
DELETE /v1/conteudos/{id}
```

---

## Tratamento de Erros

A API possui tratamento global de exceções, retornando respostas padronizadas para facilitar a identificação dos problemas.

Exemplo:

```json
{
  "timestamp": "2026-07-25T23:35:58",
  "status": 404,
  "mensagem": "Conteúdo não encontrado.",
  "detalhes": [
    "Conteúdo não encontrado com o ID informado."
  ]
}
```

---

## Como Executar

### Pré-requisitos

- Java 17+
- Maven
- PostgreSQL
- Python 3.x

---

## Backend Java

Clone o projeto:

```bash
git clone https://github.com/No-Country-simulation/Hackathon-g9-team-16.git
```

Configure as variáveis do banco:

```
DATABASE_USERNAME
DATABASE_PASSWORD
```

Execute a aplicação:

```bash
./mvnw spring-boot:run
```

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