"""
Base de conteudos REAIS, extraidos dos 4 artigos cientificos anexados pela equipe.

Cada artigo foi segmentado em varios trechos (por secao/topico), e cada trecho
recebeu uma area e subarea coerentes com a taxonomia do KnowledgeHub. Isso transforma
4 artigos em varias dezenas de exemplos reais.

Artigos usados:
  A) Silva et al. (2026) - Python aplicado a SEO / analise de dados
  B) Panwar et al. (2025) - Nudging com IA / machine learning no e-commerce
  C) Hadi et al. (2025) - Lideranca ambiental (metodologia quantitativa, estatistica)
  D) Fernandes et al. (2026) - IA no transplante renal (revisao de escopo)

Saida: dados/artigos_reais.csv (titulo, texto, area, subarea)
"""

import csv
from pathlib import Path

# Cada tupla: (titulo do trecho, texto real resumido do trecho, area, subarea)
# Os textos sao trechos reais dos artigos, encurtados para o tamanho de um
# "conteudo tecnico" tipico que a plataforma receberia.
REGISTROS = [
    # ---------------- Artigo A: Python no SEO / analise de dados ----------------
    ("Python na analise de dados para SEO",
     "A linguagem Python tem se destacado como uma das mais utilizadas no contexto "
     "da analise de dados e automacao de processos digitais. Bibliotecas como Pandas "
     "e NumPy oferecem suporte para manipulacao, estruturacao e modelagem preditiva "
     "de dados, conferindo a essa linguagem um diferencial estrategico.",
     "Ciencia de Dados", "Analise de Dados"),

    ("Estruturas fundamentais da linguagem Python",
     "Python e caracterizada por uma tipagem dinamica e forte, sintaxe clara e "
     "estruturas de controle de fluxo intuitivas. As variaveis sao criadas de forma "
     "dinamica, permitindo a atribuicao de diferentes tipos de dados sem declaracao "
     "explicita de tipo. Estruturas de controle como if-else, for e while possibilitam "
     "a construcao de fluxos de decisao e iteracao.",
     "Backend", "Arquitetura"),

    ("Bibliotecas para visualizacao de dados",
     "Para a visualizacao de dados, as bibliotecas Matplotlib e Seaborn desempenham "
     "papeis centrais. A Matplotlib e responsavel pela criacao de graficos basicos, "
     "enquanto o Seaborn permite a elaboracao de visualizacoes estatisticas mais "
     "elaboradas. A capacidade de construir graficos e dashboards e vital para a "
     "interpretacao de padroes.",
     "Ciencia de Dados", "Visualizacao"),

    ("Modelagem preditiva com Scikit-learn",
     "No campo da modelagem preditiva, a biblioteca Scikit-learn se destaca como "
     "ferramenta central para a aplicacao de algoritmos de aprendizado de maquina "
     "supervisionado e nao supervisionado, como regressao, classificacao e agrupamento "
     "de dados. O uso de tecnicas preditivas possibilita antecipar tendencias.",
     "Inteligencia Artificial", "Machine Learning"),

    ("Web scraping com BeautifulSoup",
     "A biblioteca BeautifulSoup e amplamente utilizada para web scraping, permitindo "
     "a extracao sistematica de informacoes de paginas web, processo importante para a "
     "coleta automatizada de metadados e para a analise competitiva.",
     "Ciencia de Dados", "Analise de Dados"),

    ("Analise de conteudo categorial tematica",
     "Para o tratamento dos dados, foi empregada a tecnica de analise de conteudo "
     "categorial tematica, considerada adequada para a identificacao de regularidades, "
     "nucleos de sentido e padroes interpretativos em um corpus textual heterogeneo. "
     "Essa abordagem permitiu a categorizacao dos estudos em eixos tematicos.",
     "Ciencia de Dados", "Analise de Dados"),

    ("Search Engine Optimization e organizacao da informacao",
     "O SEO refere-se ao conjunto de tecnicas aplicadas para aprimorar a visibilidade "
     "de paginas da web nos mecanismos de busca. Envolve a escolha de palavras-chave, "
     "a densidade e proximidade dessas palavras e o uso adequado de heading tags, que "
     "impactam diretamente o ranqueamento.",
     "Frontend", "Integracao"),

    # ---------------- Artigo B: Nudging com IA / ML no e-commerce ----------------
    ("Modelos de machine learning para classificacao de consumidores",
     "Modelos de aprendizado supervisionado como random forests e QC-forest sao usados "
     "para alcancar alta acuracia em tarefas de classificacao. Algoritmos como SVM, "
     "random forest, regressao logistica e k-NN tem sido efetivamente utilizados para "
     "classificar o comportamento de compra dos consumidores.",
     "Inteligencia Artificial", "Machine Learning"),

    ("Random forest na predicao de comportamento",
     "O random forest tem melhor desempenho na classificacao de variaveis categoricas, "
     "o que o torna uma tecnica adequada para identificar potenciais consumidores e "
     "permitir que empresas desenhem intervencoes direcionadas. Modelos de ensemble "
     "alcancaram ate 82,81 por cento de acuracia, superando metodos tradicionais.",
     "Inteligencia Artificial", "Machine Learning"),

    ("Support vector machine na predicao de comportamento",
     "O support vector machine e outra ferramenta poderosa de aprendizado de maquina. "
     "Um estudo usou SVM para classificar consumidores considerando o quanto se "
     "preocupam com o meio ambiente, prevendo comportamento com cerca de 96 por cento "
     "de acuracia. Tecnicas como grid search ajudam a otimizar o desempenho.",
     "Inteligencia Artificial", "Machine Learning"),

    ("Deep learning e redes neurais na predicao",
     "Tecnicas avancadas como extreme gradient boosting e deep learning, incluindo "
     "redes neurais recorrentes e modelos de memoria de longo prazo, capturam padroes "
     "complexos. Elas preveem intencoes de compra a partir de comportamentos passados, "
     "possibilitando recomendacoes personalizadas.",
     "Inteligencia Artificial", "Deep Learning"),

    ("Processamento de linguagem natural em chatbots",
     "O processamento de linguagem natural apoia interacoes mais inteligentes no "
     "e-commerce. Chatbots baseados em IA melhoram a eficiencia do atendimento e a "
     "personalizacao, engajando consumidores em discussoes em tempo real e melhorando "
     "continuamente por meio de PLN.",
     "Inteligencia Artificial", "Processamento de Linguagem Natural"),

    ("Sistemas de recomendacao e vies algoritmico",
     "Sistemas de recomendacao baseados em IA sao essenciais, mas o vies permanece um "
     "desafio. O vies de popularidade sobre-promove marcas conhecidas enquanto "
     "negligencia itens de nicho. O vies de dados ocorre quando modelos dependem de "
     "preferencias historicas dos consumidores.",
     "Inteligencia Artificial", "Machine Learning"),

    ("Revisao sistematica com protocolo PRISMA",
     "Foi conduzida uma revisao sistematica de literatura seguindo o protocolo PRISMA "
     "e multiplas bases de dados academicas. A abordagem promove um relato claro e "
     "reproduzivel, envolvendo identificacao, triagem, verificacao de elegibilidade e "
     "inclusao final dos estudos.",
     "Ciencia de Dados", "Avaliacao de Modelos"),

    # ---------------- Artigo C: Metodologia quantitativa / estatistica ----------------
    ("Analise fatorial exploratoria de dados",
     "A estrutura subjacente dos itens foi examinada via analise fatorial exploratoria. "
     "Os resultados da medida Kaiser-Meyer-Olkin e do teste de esfericidade de Bartlett "
     "foram satisfatorios, indicando que o tamanho da amostra era adequado para "
     "prosseguir com a analise.",
     "Ciencia de Dados", "Analise de Dados"),

    ("Confiabilidade e consistencia interna de escalas",
     "Os coeficientes alfa de Cronbach para todos os construtos variaram entre 0,70 e "
     "0,92, indicando forte consistencia interna das escalas. Cargas fatoriais acima de "
     "0,50 confirmaram a validade convergente e discriminante dos construtos.",
     "Ciencia de Dados", "Avaliacao de Modelos"),

    ("Analise de mediacao com PROCESS Macro",
     "Foram analisadas 216 respostas validas usando o PROCESS Macro Modelo 4. Esse "
     "metodo foi escolhido por sua acessibilidade, facilidade de uso e capacidade de "
     "investigar efeitos diretos e indiretos em analise de mediacao.",
     "Ciencia de Dados", "Avaliacao de Modelos"),

    ("Determinacao do tamanho de amostra",
     "Seguindo a recomendacao de Krejcie e Morgan para determinar um tamanho de amostra "
     "apropriado, os dados foram coletados aleatoriamente de funcionarios por meio de "
     "questionarios autoadministrados, com escalas Likert de cinco pontos.",
     "Ciencia de Dados", "Analise de Dados"),

    # ---------------- Artigo D: IA no transplante renal (revisao de escopo) ----------------
    ("Aprendizado de maquina na previsao de sobrevivencia",
     "Modelos baseados em aprendizado de maquina foram desenvolvidos e comparados para "
     "a previsao de sobrevivencia do enxerto. A abordagem envolve estimar a "
     "probabilidade de quanto tempo o orgao pode permanecer funcional, apoiando a "
     "tomada de decisoes terapeuticas.",
     "Inteligencia Artificial", "Machine Learning"),

    ("Redes neurais na avaliacao de imagens medicas",
     "Redes neurais foram usadas para automatizar a classificacao em biopsias de "
     "pacientes. O uso de espectrometria de massa quantitativa e algoritmos de "
     "aprendizado de maquina melhora o diagnostico molecular, abordando limitacoes da "
     "avaliacao tradicional.",
     "Inteligencia Artificial", "Deep Learning"),

    ("Aprendizado nao supervisionado e clusterizacao",
     "Uma abordagem de aprendizado de maquina nao supervisionada foi usada para "
     "identificar e caracterizar agrupamentos de receptores com longa duracao de "
     "tratamento, comparando os desfechos entre esses clusters e permitindo cuidado "
     "mais personalizado.",
     "Inteligencia Artificial", "Machine Learning"),

    ("Modelos preditivos para deteccao precoce",
     "Tecnicas de aprendizado de maquina foram usadas para criar modelos preditivos de "
     "deteccao precoce de complicacoes nos primeiros dias apos o procedimento. A "
     "disponibilidade de dados estruturados favorece o desenvolvimento desses modelos.",
     "Inteligencia Artificial", "Machine Learning"),

    ("Revisao de escopo e mapeamento de literatura",
     "Foi realizada uma revisao de escopo, abordagem adequada para mapear de forma "
     "abrangente como um campo tem sido investigado, identificar padroes de aplicacao e "
     "evidenciar lacunas na literatura, especialmente quando ela e heterogenea e ainda "
     "em consolidacao.",
     "Ciencia de Dados", "Analise de Dados"),

    ("Codificacao e organizacao de dados em planilha",
     "Para apoiar a organizacao dos dados e o processo de codificacao, foi utilizado o "
     "software de planilhas, que permitiu sistematizar as informacoes extraidas e "
     "agrupar os estudos de acordo com as categorias emergentes.",
     "Banco de Dados", "Modelagem"),
]


if __name__ == "__main__":
    destino = Path(__file__).parent / "artigos_reais.csv"
    with open(destino, "w", newline="", encoding="utf-8") as f:
        escritor = csv.DictWriter(f, fieldnames=["titulo", "texto", "area", "subarea"])
        escritor.writeheader()
        for titulo, texto, area, subarea in REGISTROS:
            escritor.writerow({
                "titulo": titulo, "texto": texto,
                "area": area, "subarea": subarea,
            })
    print(f"{len(REGISTROS)} registros reais gerados em: {destino}")
