# Guia de Integração Frontend - TechMind API

Este documento destina-se a desenvolvedores Frontend (React, Vue, Angular, Next.js, React Native, etc.) para integração simples, rápida e segura com a **TechMind API**.

---

## 1. Endpoints e Documentação Interativa

- **URL Base Local**: `http://localhost:8080`
- **Documentação Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **ESPECIFICAÇÃO OpenAPI 3.0 (JSON)**: `http://localhost:8080/v3/api-docs`

---

## 2. Configurações de CORS & Segurança

A API está configurada para aceitar requisições de Frontends hospedados em:
- `http://localhost:3000` (React CRA / Next.js)
- `http://localhost:5173` (Vite / Vue / React Vite)
- `http://localhost:8080`

> **Nota para Produção**: Defina a variável de ambiente `ALLOWED_ORIGINS` no servidor backend com o domínio de produção do frontend (ex: `https://meu-app.com`).

---

## 3. Endpoints Principais

### POST `/conteudo`
Processa e classifica um texto de tecnologia.

#### Request Body (`application/json`)
```json
{
  "titulo": "Introdução ao Spring Boot e APIs RESTful",
  "texto": "Spring Boot facilita a criação de aplicações Spring baseadas em microsserviços prontas para produção com Java e Spring Security."
}
```

**Validações**:
- `titulo`: Obrigatório, mínimo de 3 caracteres e máximo de 200.
- `texto`: Obrigatório, mínimo de 10 caracteres e máximo de 50.000.

#### Response Body Status `200 OK`
```json
{
  "categoria": "Backend",
  "probabilidade": 0.98,
  "palavrasChave": [
    "Java",
    "Spring",
    "Boot",
    "Security",
    "Api"
  ],
  "resumo": "Spring Boot facilita a criação de aplicações Spring baseadas em microsserviços prontas para produção com Java e Spring Security."
}
```

---

## 4. Exemplos de Código para Conexão

### A. Exemplo com JavaScript Native `fetch`

```javascript
async function analisarConteudo(titulo, texto) {
  try {
    const response = await fetch('http://localhost:8080/conteudo', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ titulo, texto }),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Erro no processamento');
    }

    const data = await response.json();
    console.log('Resultado da Análise:', data);
    return data;
  } catch (error) {
    console.error('Erro na chamada da API:', error);
    throw error;
  }
}
```

### B. Exemplo com `axios`

```typescript
import axios from 'axios';

interface ConteudoRequest {
  titulo: string;
  texto: string;
}

interface ConteudoResponse {
  categoria: string;
  probabilidade: number;
  palavrasChave: string[];
  resumo: string;
}

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const processarTexto = async (payload: ConteudoRequest): Promise<ConteudoResponse> => {
  const { data } = await api.post<ConteudoResponse>('/conteudo', payload);
  return data;
};
```

### C. Exemplo de Custom Hook React (`useConteudo.ts`)

```typescript
import { useState } from 'react';

export function useConteudo() {
  const [loading, setLoading] = useState(false);
  const [resultado, setResultado] = useState<any>(null);
  const [erro, setErro] = useState<string | null>(null);

  const analisar = async (titulo: string, texto: string) => {
    setLoading(true);
    setErro(null);
    try {
      const res = await fetch('http://localhost:8080/conteudo', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ titulo, texto }),
      });
      const data = await res.json();
      if (!res.ok) {
        throw new Error(data.message || 'Falha ao analisar texto');
      }
      setResultado(data);
    } catch (e: any) {
      setErro(e.message);
    } finally {
      setLoading(false);
    }
  };

  return { analisar, resultado, loading, erro };
}
```

---

## 5. Estrutura de Erros da API (`400 Bad Request` / `500 Internal Error`)

Quando ocorre um erro de validação (ex: campos vazios), a API retorna HTTP `400` com a seguinte estrutura:

```json
{
  "timestamp": "2026-07-24T00:39:00Z",
  "status": 400,
  "error": "Requisição Inválida",
  "message": "Um ou mais campos contêm erros de validação.",
  "path": "/conteudo",
  "errors": {
    "titulo": "O título é obrigatório",
    "texto": "O texto é obrigatório"
  }
}
```
