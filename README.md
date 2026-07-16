# TechMind - Repositório Inteligente de Conhecimento Técnico

O **TechMind** é uma solução inteligente desenvolvida para receber, classificar, catalogar e permitir buscas em conteúdos técnicos (como tutoriais, anotações de estudo, artigos e documentações). A plataforma automatiza a organização de bases de dados técnicas e integra-se conceitualmente e fisicamente aos serviços da **Oracle Cloud Infrastructure (OCI)**.

Este repositório contém o **MVP do Front-End** do projeto, desenvolvido como uma Single Page Application (SPA) responsiva e moderna de alta fidelidade visual, utilizando tecnologias web puras.

---

## 🚀 Como Executar o Projeto

Como o front-end foi construído utilizando HTML, CSS e JavaScript puros (Vanilla), ele é altamente portátil e não exige dependências de build como Node.js ou bundlers complexos. Há duas formas principais de executá-lo:

### Opção 1: Execução Direta (Sem Servidor)
1. Navegue até a pasta `src/main/resources/static/`.
2. Dê um clique duplo ou abra o arquivo `login.html` em qualquer navegador moderno (Chrome, Edge, Firefox, Safari).
3. Todas as funcionalidades (autenticação, cadastro, dashboard cognitivo, busca semântica e simulador de rede OCI) funcionarão de forma local e imediata, alimentadas por um motor de mock JavaScript embarcado.

### Opção 2: Integração com o Backend Spring Boot
Os arquivos estáticos do front-end já estão alocados na pasta padrão de recursos estáticos do Spring Boot (`src/main/resources/static/`). 
Quando o backend estiver ativo:
1. Abra o terminal na raiz do projeto.
2. Execute o comando Maven para iniciar o Spring Boot:
   ```bash
   ./mvnw spring-boot:run
   ```
3. Acesse no seu navegador: `http://localhost:8080/login.html` ou `http://localhost:8080/`

---

## 🛠️ Detalhes das Telas do MVP

0. **Autenticação Unificada (Login e Cadastro)**: Tela inicial com formulários para entrada e criação de contas. Exibe um card flutuante contendo credenciais de teste rápido (`admin@teste.com` / `senha123` e `user@teste.com` / `user123`) com botões de auto-preenchimento e redirecionamento dinâmico.
1. **Painel Geral (Dashboard)**: Exibe contadores em tempo real (Total de documentos, categorias cadastradas, palavras-chave e uso de dados), além de um gráfico de colunas de distribuição temática, linha do tempo de atividades e exibição do usuário ativo na sidebar.
2. **Análise e Cadastro**: Formulário para inserção de títulos e textos técnicos. Possui **botões de preenchimento rápido** com exemplos reais de códigos e guias. Mostra a animação do processo cognitivo da IA e retorna a categoria predita, tags de palavras-chave, OCI storage path e o JSON estruturado gerado.
3. **Base de Conhecimento**: Lista em grid no estilo glassmorphism. Permite filtrar dinamicamente por categoria ou por busca textual. Ao clicar em um card, um modal exibe o texto completo e a exportação do JSON de integração.
4. **Busca Semântica**: Barra de pesquisa conceitual que calcula a similaridade da pesquisa contra os textos da base usando pesos lexicais e sinônimos estruturados, ordenando os resultados por porcentagem de correspondência (ex: *94% Match*).
5. **Topologia OCI & Monitor**: Diagrama visual interativo que mostra como a arquitetura do projeto flui até a Oracle Cloud Infrastructure, acompanhado de um console de terminal simulando requisições e uploads na nuvem.
6. **Configurações**: Permite alternar a aplicação entre o *Modo de Demonstração (Mocks)* e o *Modo de API Conectada*, permitindo apontar a URL de requisições para o endpoint oficial do backend.

---

## 📊 Especificações da API (Contrato de Integração)

O front-end está preparado para consumir a API REST nos seguintes formatos de payload:

### Endpoint Principal de Análise
* **URL**: `POST /api/conteudo` (ou customizável na aba Configurações)
* **Headers**: `Content-Type: application/json`

#### Exemplo de Requisição (Request Payload)
```json
{
  "titulo": "Introdução ao Spring Boot",
  "texto": "Neste conteúdo são apresentados os conceitos básicos para criação de APIs REST utilizando Java e Spring Boot."
}
```

#### Exemplo de Resposta (Response Payload)
```json
{
  "categoria": "Backend",
  "probabilidade": 0.94,
  "informacoes_adicionais": [
    "Java",
    "Spring Boot",
    "API REST",
    "Segurança"
  ]
}
```

---

## ☁️ Integração com Serviços Oracle Cloud Infrastructure (OCI)

A arquitetura do TechMind foi desenhada para interagir de forma nativa com a nuvem OCI através dos seguintes recursos:

* **OCI Object Storage**: Utilizado como repositório frio de documentos estruturados. Cada texto processado gera um backup em formato JSON que é transferido para o bucket `techmind-kb-bucket` no compartimento do projeto.
* **OCI Data Science (Model Catalog)**: Hospeda o modelo de Machine Learning (como a Regressão Logística construída com Scikit-Learn e TF-IDF serializado em joblib/pickle) exposto por um endpoint Flask/FastAPI ou OCI Functions.
* **OCI Compute / Container Engine (OKE)**: Usado para implantar e gerenciar a aplicação backend Spring Boot de forma escalável.
* **OCI Autonomous Database**: Persiste os dados cadastrados, metadados de auditoria e logs de processamento.

---

## ⚙️ Tecnologias e Versões Utilizadas

* **HTML5**: Estruturação semântica e acessibilidade.
* **CSS3 (Vanilla)**: Layout Flexbox/Grid, Glassmorphism, animações customizadas e variáveis de ambiente CSS.
* **JavaScript (ES6+)**: Lógica SPA de alteração de estados, SessionStorage, motor de NLP heurístico local e tratamento de eventos.
* **Google Fonts**: Fontes *Inter* e *JetBrains Mono*.
* **Spring Boot (Backend - Estrutura Base)**: Versão 3.x configurada no Maven `pom.xml`.
