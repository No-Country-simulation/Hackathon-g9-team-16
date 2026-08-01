/**
 * TechMind MVP - Banco de Dados Local Simulado (Mock Data)
 * Contém dados técnicos estruturados com base em artigos científicos reais para testes do repositório.
 */

window.TechMindData = {
  // Lista de documentos cadastrados (Baseada em artigos científicos reais fornecidos)
  documentos: [
    {
      id: "doc-001",
      titulo: "Aplicação da Linguagem Python no Search Engine Optimization: Explorando sua Contribuição para a Análise de Dados",
      tipo: "Artigo",
      texto: "Este estudo investiga a contribuição da linguagem Python na análise e otimização de dados para Search Engine Optimization (SEO), com foco na organização e recuperação da informação. Adota-se uma abordagem teórico-exploratória, fundamentada em levantamento bibliográfico em bases reconhecidas, como Scopus e Web of Science, além da análise de ferramentas especializadas em SEO. O estudo identificou que Python, por meio de bibliotecas como Pandas e NumPy, permite a automação de processos essenciais, como extração de dados, análise de palavras-chave e modelagem preditiva de padrões de indexação. Os resultados apontam que a aplicação dessas ferramentas melhora a eficiência das estratégias de Search Engine Optimization, tornando-as mais precisas e baseadas em dados. Além disso, destaca-se a interseção entre Ciência da Informação e Ciência da Computação, evidenciando como a programação pode contribuir para a estruturação semântica e organização dos conteúdos digitais. Conclui-se que Python oferece soluções eficazes para a automação e análise preditiva no Search Engine Optimization, potencializando a visibilidade e recuperação da informação nos motores de busca.",
      categoria: "Data Science",
      probabilidade: 0.94,
      informacoes_adicionais: ["Python", "Search Engine Optimization", "Análise de dados", "Automação", "Pandas", "NumPy", "NLP"],
      data_criacao: "2026-07-10T14:30:00Z",
      oci_storage_path: "oci://techmind-kb-bucket/raw/datascience/doc-001.json"
    },
    {
      id: "doc-002",
      titulo: "O Nudging Baseado em Inteligência Artificial Pode Promover a Adoção de Produtos Sustentáveis em Plataformas de E-Commerce?",
      tipo: "Artigo",
      texto: "Este estudo visa identificar intervenções comportamentais eficazes e modelos de aprendizado de máquina que promovem compras ambientalmente sustentáveis. O estudo está fundamentado na economia comportamental e na arquitetura de escolhas, apoiando-se na Nudge Theory (ou Teoria do Nudge) de Thaler e Sunstein, de forma a explicar como elementos sutis de design digital podem servir de orientação para escolhas sem comprometer a autonomia do consumidor. Utiliza uma revisão sistemática baseada no protocolo PRISMA em múltiplas bases acadêmicas. Constatou-se que técnicas de nudging baseadas em IA, tais como personalização, default green options (opções verdes predefinidas), prova social e gamificação, aumentam significativamente o engajamento em comportamentos de consumo sustentável. Modelos de aprendizado de máquina como Random Forests, Deep Neural Networks e Reinforcement Learning têm um papel crucial na otimização do nudging por IA, equilibrando desempenho preditivo e capacidade explicativa do modelo (XAI).",
      categoria: "Data Science",
      probabilidade: 0.92,
      informacoes_adicionais: ["Inteligência Artificial", "Nudging Digital", "E-Commerce", "Consumo Sustentável", "Random Forests", "Explainable AI"],
      data_criacao: "2026-07-12T09:15:00Z",
      oci_storage_path: "oci://techmind-kb-bucket/raw/datascience/doc-002.json"
    },
    {
      id: "doc-003",
      titulo: "Aplicações da Inteligência Artificial no Transplante Renal: Uma Revisão de Escopo ao Longo do Continuum do Cuidado",
      tipo: "Artigo",
      texto: "Mapeamento da literatura científica sobre as aplicações de inteligência artificial (IA) no processo de transplante renal, identificando seus principais usos, etapas do cuidado e desfechos clínicos influenciados. Realizou-se uma revisão de escopo baseada no checklist PRISMA-ScR na base de dados Scopus cobrindo o período de 2019 a 2023. As aplicações de IA mapeadas foram organizadas em sete categorias principais: previsão de comportamentos do paciente (como adesão a imunossupressores); avaliação radiológica e patológica; previsão de progressão da doença renal pré-transplante; previsão de compatibilidade doador-receptor; otimização da administração de medicamentos; diagnóstico de complicações pós-transplante; e previsão de sobrevivência do enxerto. Observou-se a predominância de aplicações no pós-transplante (65,7%), indicando maturidade em tarefas de diagnóstico de rejeição e monitoramento clínico, e lacunas em dimensões comportamentais e na fase pré-transplante.",
      categoria: "Backend",
      probabilidade: 0.89,
      informacoes_adicionais: ["Transplante Renal", "Inteligência Artificial", "Aprendizado de Máquina", "Nefrologia", "Modelos Preditivos", "Saúde Digital"],
      data_criacao: "2026-07-14T18:45:00Z",
      oci_storage_path: "oci://techmind-kb-bucket/raw/backend/doc-003.json"
    },
    {
      id: "doc-004",
      titulo: "What Triggers Environmental Sustainability in the Organization? Empirical Evidence from the Cause-and-Effect Relationship",
      tipo: "Artigo",
      texto: "O estudo examina o mecanismo de impacto da liderança transformacional ambiental (ETL) sobre os comportamentos pró-ambientais dos funcionários (PEBs) e a motivação ambiental autônoma (AEM). Os dados foram coletados de funcionários por meio de questionários auto-administrados, e 216 respostas válidas foram analisadas usando o Modelo Macro PROCESS 4. O estudo descobriu que o aspecto inspirador da liderança transformacional ambiental é benéfico para a motivação ambiental autônoma e para os comportamentos pró-ambientais dos colaboradores nas organizações. Do ponto de vista prático, para se tornarem mais ecologicamente sustentáveis, as organizações e os departamentos de recursos humanos devem priorizar a identificação e contratação de líderes que demonstrem forte motivação ambiental em seus critérios de seleção gerencial.",
      categoria: "DevOps",
      probabilidade: 0.91,
      informacoes_adicionais: ["Sustentabilidade Ambiental", "Liderança", "Comportamento Humano", "PROCESS Macro 4", "Relação Causa-Efeito", "Gestão Verde"],
      data_criacao: "2026-07-15T11:00:00Z",
      oci_storage_path: "oci://techmind-kb-bucket/raw/devops/doc-004.json"
    }
  ],

  // Exemplos rápidos para preenchimento automático no formulário (agora apontando para tópicos dos artigos)
  exemplosRapidos: [
    {
      titulo: "Automação de Auditorias de SEO técnico usando scripts em Python e Pandas",
      tipo: "Artigo",
      texto: "Esta pesquisa aborda a implementação prática de scripts em Python utilizando a biblioteca Pandas para extração e processamento em lote de dados estruturados de URLs web. O script realiza a análise semântica de heading tags (H1-H6) e a densidade de palavras-chave do site para verificar a conformidade com as regras de indexação orgânica dos motores de busca (SEO On-Page), simulando pipelines de recuperação de informação.",
      categoria: "Data Science",
      tags: ["Python", "Pandas", "SEO On-Page", "Automação", "Linguagem Natural"]
    },
    {
      titulo: "Previsão de Compatibilidade Doador-Receptor com Algoritmos de Machine Learning no Transplante Renal",
      tipo: "Artigo",
      texto: "O projeto descreve o desenvolvimento de modelos de aprendizado de máquina supervisionados para a análise de incompatibilidades de aminoácidos em antígenos leucocitários humanos (HLAs). O algoritmo é capaz de estratificar pares doador-receptor de rim em grupos de baixo ou alto risco de rejeição aguda, auxiliando na alocação de órgãos e otimização do continuum de cuidado no pós-transplante.",
      categoria: "Backend",
      tags: ["Machine Learning", "HLA", "Transplante Renal", "Modelos Preditivos", "Medicina Computacional"]
    },
    {
      titulo: "Otimização de Nudging em E-Commerce baseada em Modelos de Random Forest",
      tipo: "Artigo",
      texto: "Estudo empírico focado na aplicação do algoritmo de Random Forest no e-commerce para classificar intenções de compra sustentáveis. A partir do histórico de navegação e preferências, o modelo otimiza a exibição de nudges digitais personalizados (como alertas de estoque de produtos ecológicos e opções de entrega de baixo carbono) para influenciar o engajamento do consumidor em práticas de consumo verde.",
      categoria: "Data Science",
      tags: ["Random Forest", "E-Commerce", "Nudging", "Comportamento do Consumidor", "Explainable AI"]
    }
  ]
};
