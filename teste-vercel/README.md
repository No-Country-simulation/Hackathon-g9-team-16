# TechMind - Teste de Deploy na Vercel

Este repositorio contem apenas o front-end para testar o deploy na Vercel.

## O que tem aqui

- **Landing page** (Next.js/React): `app/page.tsx` + `components/landing/`
- **App SPA** (HTML/CSS/JS vanilla): `public/login.html`, `public/app.html`, `public/app.js`
- **Config de API**: `public/api-config.js` (define para onde o front aponta)

## Como subir na Vercel

1. Crie um repositorio no GitHub e faça push deste conteudo:
   ```bash
   cd teste-vercel
   git init
   git add .
   git commit -m "Front-end para teste Vercel"
   git branch -M main
   git remote add origin https://github.com/SEU_USUARIO/techmind-test-deploy.git
   git push -u origin main
   ```

2. Acesse https://vercel.com e clique em "New Project"

3. Importe o repositorio `techmind-test-deploy`

4. A Vercel detecta o Next.js automaticamente. Nao mude nada, so clique em "Deploy"

5. Pronto! Voce tera uma URL como `https://techmind-test-deploy.vercel.app`

## Como conectar ao backend

### Opcao A: Arquivo api-config.js (simples)

Edite `public/api-config.js` e coloque a URL do backend:
```js
window.__NEXT_PUBLIC_API_URL__ = "http://163.176.134.19:8080";
```
Suba novamente (`git push`) e a Vercel redistribui.

### Opcao B: Environment Variable (recomendado)

No painel da Vercel: Settings > Environment Variables > adicione:
- Name: `NEXT_PUBLIC_API_URL`
- Value: `http://163.176.134.19:8080`

> **Atencao:** como `login.html` e `app.html` sao HTML estatico (nao React),
> a env do painel nao e injetada automaticamente nesses arquivos.
> Use a Opcao A para o app vanilla. A Opcao B funciona para componentes Next.js.

## Testando o fluxo

1. Acesse `https://sua-url.vercel.app` -> landing page
2. Clique em "Testar a API" -> vai para `/login.html`
3. Use as credenciais de teste: `admin@teste.com` / `senha123`
4. Va em **Configuracoes** -> ative **Conexao em Tempo Real**
5. Aponte para `http://163.176.134.19:8080/conteudo` (ou `http://localhost:8080/conteudo` se o Spring Boot estiver rodando local)
6. Va em **Analise e Cadastro** -> escolha um exemplo -> clique em **Analisar**

Se o backend responder, voce vera a categoria, probabilidade e palavras-chave na tela.
