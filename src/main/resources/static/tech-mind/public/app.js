/**
 * TechMind MVP - Lógica da Aplicação (SPA & Motor de Mock)
 * Responsável pelo roteamento, gerenciamento de estado local, lógica de IA simulada,
 * mecanismo de busca por similaridade e logs de integração OCI.
 */

document.addEventListener("DOMContentLoaded", () => {
  // --- VERIFICAÇÃO DE SESSÃO ---
  const sessionData = sessionStorage.getItem("techmind_user_session");
  const path = window.location.pathname;
  
  if (!sessionData && !path.includes("login.html")) {
    window.location.href = "login.html";
    return;
  }

  // Vincula o nome do usuário logado na sidebar
  if (sessionData) {
    const sessionObj = JSON.parse(sessionData);
    const loggedUserElem = document.getElementById("logged-user-name");
    if (loggedUserElem) {
      loggedUserElem.innerText = sessionObj.name || "Usuário";
    }
  }

  // --- SAIR DA CONTA (LOGOUT) ---
  const btnLogout = document.getElementById("btn-logout-sidebar");
  if (btnLogout) {
    btnLogout.addEventListener("click", (e) => {
      e.preventDefault();
      if (confirm("Deseja realmente sair da sua conta?")) {
        sessionStorage.removeItem("techmind_user_session");
        window.location.href = "login.html";
      }
    });
  }

  // --- INICIALIZAÇÃO DE ESTADO ---
  let documentos = [];
  const logsOCI = [];
  let currentAnalysisResult = null;

  // Carrega do sessionStorage para persistência entre recargas ou usa os dados iniciais do mockData.js
  const loadState = () => {
    const savedDocs = sessionStorage.getItem("techmind_docs");
    let loadedDocs = null;
    if (savedDocs) {
      loadedDocs = JSON.parse(savedDocs);
      // Reseta proativamente o sessionStorage se contiver os dados do mock antigo
      if (loadedDocs.length > 0 && loadedDocs[0].titulo.includes("Construindo APIs REST Seguras")) {
        loadedDocs = null;
      }
    }

    if (loadedDocs) {
      documentos = loadedDocs;
    } else if (window.TechMindData && window.TechMindData.documentos) {
      documentos = [...window.TechMindData.documentos];
      saveState();
    }
    
    // Configurações de API
    const savedMode = sessionStorage.getItem("techmind_api_mode");
    const savedUrl = sessionStorage.getItem("techmind_api_url");
    
    const apiToggle = document.getElementById("api-mode-toggle");
    const apiUrlInput = document.getElementById("api-url-input");
    
    if (apiToggle && savedMode) {
      apiToggle.checked = savedMode === "live";
      if (apiUrlInput) apiUrlInput.disabled = savedMode !== "live";
    }
    if (apiUrlInput && savedUrl) {
      apiUrlInput.value = savedUrl;
    }
  };

  const saveState = () => {
    sessionStorage.setItem("techmind_docs", JSON.stringify(documentos));
  };

  // --- ROTEAMENTO SPA ---
  const initNavigation = () => {
    const navLinks = document.querySelectorAll(".nav-item a");
    const views = document.querySelectorAll(".view-container");

    navLinks.forEach(link => {
      link.addEventListener("click", (e) => {
        e.preventDefault();
        const targetViewId = link.getAttribute("data-target");

        // Remove classe ativa de todos os links e adiciona no clicado
        navLinks.forEach(l => l.parentElement.classList.remove("active"));
        link.parentElement.classList.add("active");

        // Oculta todas as views e exibe a selecionada
        views.forEach(view => {
          if (view.id === targetViewId) {
            view.classList.add("active-view");
            triggerViewLogs(targetViewId);
          } else {
            view.classList.remove("active-view");
          }
        });
        
        // Atualiza a visualização específica se necessário
        if (targetViewId === "view-dashboard") {
          updateDashboard();
        } else if (targetViewId === "view-library") {
          renderLibrary();
        } else if (targetViewId === "view-oci") {
          renderOCIMonitor();
        }
      });
    });
  };

  // --- GERADOR DE LOGS PARA O CONSOLE OCI ---
  const logOCI = (message, type = "info") => {
    const now = new Date();
    const timeStr = now.toTimeString().split(" ")[0];
    const log = { time: timeStr, message, type };
    logsOCI.push(log);
    
    // Limita em 50 logs no console
    if (logsOCI.length > 50) logsOCI.shift();
    
    // Se a view OCI estiver ativa, renderiza imediatamente
    const logsContainer = document.getElementById("oci-logs-container");
    if (logsContainer) {
      const entry = document.createElement("div");
      entry.className = `log-entry ${type}`;
      entry.innerHTML = `<span class="log-time">[${timeStr}]</span> ${message}`;
      logsContainer.appendChild(entry);
      logsContainer.scrollTop = logsContainer.scrollHeight;
    }
  };

  const triggerViewLogs = (viewId) => {
    switch (viewId) {
      case "view-dashboard":
        logOCI("Carregando Painel Administrativo. Buscando estatísticas da base.", "info");
        break;
      case "view-analyze":
        logOCI("Acessou módulo de análise. Pronto para receber novas requisições.", "info");
        break;
      case "view-library":
        logOCI("Carregando indexador de documentos. Total de registros: " + documentos.length, "info");
        break;
      case "view-semantic":
        logOCI("Módulo de similaridade ativo. Vetorizador pronto para NLP local.", "info");
        break;
      case "view-oci":
        logOCI("Painel de topologia de rede OCI carregado.", "info");
        break;
      case "view-settings":
        logOCI("Painel de configurações gerais acessado pelo administrador.", "warn");
        break;
    }
  };

  // --- DADOS DO DASHBOARD ---
  const updateDashboard = () => {
    // 1. KPI Values
    document.getElementById("kpi-total-docs").innerText = documentos.length;
    
    const categories = [...new Set(documentos.map(d => d.categoria))];
    document.getElementById("kpi-categories").innerText = categories.length;

    let totalTags = 0;
    documentos.forEach(d => totalTags += d.informacoes_adicionais.length);
    document.getElementById("kpi-tags").innerText = totalTags;

    // Tamanho estimado de armazenamento (ex: 2.5 KB por documento)
    const kbTotal = (documentos.length * 2.45).toFixed(2);
    document.getElementById("kpi-size").innerText = `${kbTotal} KB`;

    // 2. SVG/CSS Bars Chart
    const counts = { Backend: 0, Frontend: 0, DevOps: 0, "Data Science": 0 };
    documentos.forEach(d => {
      if (counts[d.categoria] !== undefined) {
        counts[d.categoria]++;
      }
    });

    const maxCount = Math.max(...Object.values(counts), 1);
    
    // Atualiza a altura das barras e as tooltips
    Object.keys(counts).forEach(cat => {
      const barId = `bar-${cat.toLowerCase().replace(" ", "")}`;
      const tooltipId = `tooltip-${cat.toLowerCase().replace(" ", "")}`;
      const barElement = document.getElementById(barId);
      const tooltipElement = document.getElementById(tooltipId);
      
      if (barElement) {
        const heightPercent = (counts[cat] / maxCount) * 85 + 10; // min 10% para visualização
        barElement.style.height = `${heightPercent}%`;
      }
      if (tooltipElement) {
        tooltipElement.innerText = `${counts[cat]} doc(s)`;
      }
    });

    // 3. Renderiza Atividades Recentes
    renderRecentTimeline();
  };

  const renderRecentTimeline = () => {
    const timeline = document.getElementById("timeline-recent");
    if (!timeline) return;

    timeline.innerHTML = "";
    
    // Pega os 3 documentos mais recentes com base no ID ou data
    const sortedDocs = [...documentos].sort((a, b) => new Date(b.data_criacao) - new Date(a.data_criacao));
    const recent = sortedDocs.slice(0, 3);

    if (recent.length === 0) {
      timeline.innerHTML = '<div style="font-size:12px; color:var(--text-muted); text-align:center; padding:10px 0;">Nenhuma atividade recente encontrada.</div>';
      return;
    }

    recent.forEach(doc => {
      const timeAgo = formatTimeAgo(new Date(doc.data_criacao));
      
      const item = document.createElement("div");
      item.className = "timeline-item success";
      item.innerHTML = `
        <div class="timeline-dot">
          <svg viewBox="0 0 24 24"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14M22 4L12 14.01l-3-3" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>
        </div>
        <div class="timeline-info">
          <div class="timeline-title">${doc.titulo}</div>
          <div class="timeline-desc">Classificado em <strong style="color:var(--accent-cyan);">${doc.categoria}</strong> (confiança: ${(doc.probabilidade * 100).toFixed(0)}%)</div>
          <div class="timeline-time">${timeAgo}</div>
        </div>
      `;
      timeline.appendChild(item);
    });
  };

  // Helper para tempo relativo simples
  const formatTimeAgo = (date) => {
    const seconds = Math.floor((new Date() - date) / 1000);
    let interval = Math.floor(seconds / 31536000);

    if (interval >= 1) return `Há ${interval} ano(s)`;
    interval = Math.floor(seconds / 2592000);
    if (interval >= 1) return `Há ${interval} mês(es)`;
    interval = Math.floor(seconds / 86400);
    if (interval >= 1) return `Há ${interval} dia(s)`;
    interval = Math.floor(seconds / 3600);
    if (interval >= 1) return `Há ${interval} hora(s)`;
    interval = Math.floor(seconds / 60);
    if (interval >= 1) return `Há ${interval} minuto(s)`;
    return "Agora mesmo";
  };

  // --- EXEMPLOS RÁPIDOS NO CADASTRO ---
  const initQuickExamples = () => {
    const container = document.getElementById("quick-examples-container");
    if (!container || !window.TechMindData || !window.TechMindData.exemplosRapidos) return;

    container.innerHTML = "";
    window.TechMindData.exemplosRapidos.forEach((ex, idx) => {
      const btn = document.createElement("button");
      btn.type = "button";
      btn.className = "quick-btn";
      btn.innerText = ex.titulo;
      btn.addEventListener("click", () => {
        document.getElementById("content-title").value = ex.titulo;
        document.getElementById("content-text").value = ex.texto;
        document.getElementById("content-type").value = ex.categoria === "Backend" ? "Artigo" : ex.categoria === "Frontend" ? "Tutorial" : "Notas de Estudo";
        logOCI(`Exemplo técnico carregado no formulário: "${ex.titulo}"`, "info");
      });
      container.appendChild(btn);
    });
  };

  // --- PROCESSO DE ANÁLISE COM IA (MOCK COM MOTOR DE REGEX) ---
  const initAnalyzer = () => {
    const form = document.getElementById("analyzer-form");
    const btnAnalyze = document.getElementById("btn-analyze-submit");
    const containerProcessing = document.getElementById("processing-container");
    const containerResult = document.getElementById("result-container");
    const processingText = document.getElementById("processing-step-text");

    if (!form) return;

    form.addEventListener("submit", (e) => {
      e.preventDefault();

      const titulo = document.getElementById("content-title").value.trim();
      const texto = document.getElementById("content-text").value.trim();
      const tipo = document.getElementById("content-type").value;

      if (titulo.length < 5) {
        alert("O título deve conter pelo menos 5 caracteres.");
        return;
      }
      if (texto.length < 20) {
        alert("O conteúdo textual deve conter pelo menos 20 caracteres para permitir uma análise contextual correta.");
        return;
      }

      // Prepara interface para carregamento
      btnAnalyze.disabled = true;
      containerResult.style.display = "none";
      containerProcessing.style.display = "flex";

      const apiMode = sessionStorage.getItem("techmind_api_mode") || "mock";
      const apiUrl = sessionStorage.getItem("techmind_api_url") || "/conteudo";

      // Logs de execução simulados no painel OCI
      logOCI(`Iniciando análise de texto técnico: "${titulo}"`, "info");

      if (apiMode === "live") {
        logOCI(`Efetuando chamada real para API: POST ${apiUrl}`, "info");
        processingText.innerText = "Conectando ao servidor Spring Boot...";

        fetch(apiUrl, {
          method: "POST",
          headers: {
            "Content-Type": "application/json"
          },
          body: JSON.stringify({ titulo, texto })
        })
        .then(response => {
          if (!response.ok) {
            throw new Error(`Erro na API (Status ${response.status})`);
          }
          return response.json();
        })
        .then(data => {
          // O Spring Boot retorna { categoria, probabilidade, palavrasChave, resumo }
          const docId = `doc-${String(documentos.length + 1).padStart(3, "0")}`;
          const cleanCategorySlug = (data.categoria || "generic").toLowerCase().replace(" ", "");

          const result = {
            id: docId,
            titulo: titulo,
            tipo: tipo,
            texto: texto,
            categoria: data.categoria || "Backend",
            probabilidade: data.probabilidade || 1.0,
            informacoes_adicionais: data.palavrasChave || ["Tech"],
            data_criacao: new Date().toISOString(),
            oci_storage_path: `oci://techmind-kb-bucket/raw/${cleanCategorySlug}/${docId}.json`
          };

          currentAnalysisResult = result;
          renderAnalysisResult(result);

          containerProcessing.style.display = "none";
          containerResult.style.display = "block";
          btnAnalyze.disabled = false;

          logOCI(`Retorno recebido do modelo via Spring Boot! Categoria: ${data.categoria}`, "success");
        })
        .catch(error => {
          console.error("Erro na requisição real:", error);
          alert(`Falha ao conectar com o backend real. Verifique se o servidor Spring Boot está de pé na porta 8080.\n\nDetalhe do erro: ${error.message}`);
          containerProcessing.style.display = "none";
          btnAnalyze.disabled = false;
          logOCI(`Falha na requisição real: ${error.message}`, "error");
        });

      } else {
        // Passos da animação simulada
        const steps = [
          "Lendo texto técnico inserido...",
          "Enviando payload para pipeline de Ciência de Dados...",
          "Executando vetorização do texto com TF-IDF...",
          "Invocando modelo de Regressão Logística na OCI...",
          "Computando tags mais relevantes do conteúdo...",
          "Simulando upload no OCI Object Storage...",
          "Estruturando arquivo de resposta JSON..."
        ];

        let currentStep = 0;
        processingText.innerText = steps[currentStep];
        logOCI("POST /api/conteudo - Iniciado processamento simulado", "info");

        const interval = setInterval(() => {
          currentStep++;
          if (currentStep < steps.length) {
            processingText.innerText = steps[currentStep];
            if (currentStep === 2) logOCI("Processando NLP: Removendo stopwords e gerando n-grams", "info");
            if (currentStep === 3) logOCI("Classificando via ML: LogisticRegression.predict()", "info");
            if (currentStep === 5) logOCI(`Gravando backup raw na OCI: oci://techmind-kb-bucket/raw/temp-${Date.now()}.json`, "success");
          } else {
            clearInterval(interval);
            
            const result = analyzeContent(titulo, texto, tipo);
            currentAnalysisResult = result;
            
            renderAnalysisResult(result);
            
            containerProcessing.style.display = "none";
            containerResult.style.display = "block";
            btnAnalyze.disabled = false;
            
            logOCI("POST /api/conteudo - Retornou Status 200 (Success) via Mock", "success");
          }
        }, 400); // 400ms por etapa = 2.8s no total
      }
    });

    // Evento de Salvar na base
    const btnSave = document.getElementById("btn-save-result");
    if (btnSave) {
      btnSave.addEventListener("click", () => {
        if (!currentAnalysisResult) return;

        // Adiciona à lista local
        documentos.push(currentAnalysisResult);
        saveState();

        logOCI(`Documento salvo com sucesso na base. ID gerado: ${currentAnalysisResult.id}`, "success");
        logOCI(`OCI Object Storage: Persistido em ${currentAnalysisResult.oci_storage_path}`, "success");
        
        // Reset formulário e estado do painel de resultado
        form.reset();
        currentAnalysisResult = null;
        containerResult.style.display = "none";
        
        // Animação de sucesso
        alert("Conteúdo catalogado e integrado na Base de Conhecimento com sucesso!");
        updateDashboard();
      });
    }
  };

  // Algoritmo de classificação heurística baseado em léxico técnico
  const analyzeContent = (title, text, type) => {
    const combined = (title + " " + text).toLowerCase();
    
    // Regras de detecção de tags e categorias baseadas em dicionários de palavras-chave
    const keywordsDb = {
      Backend: ["java", "spring", "boot", "controller", "endpoint", "api rest", "jpa", "hibernate", "sql", "postgres", "node", "express", "backend", "segurança", "jwt", "auth"],
      Frontend: ["react", "hooks", "context", "vue", "angular", "css", "html", "javascript", "typescript", "flexbox", "grid", "tailwind", "component", "frontend", "dom"],
      DevOps: ["docker", "kubernetes", "oke", "ci/cd", "pipeline", "github actions", "deploy", "nginx", "aws", "cloud", "oci", "terraform", "ansible", "container", "bucket", "object storage"],
      "Data Science": ["python", "pandas", "numpy", "scikit-learn", "regressão", "classificação", "machine learning", "nlp", "notebook", "jupyter", "eda", "modelagem", "algoritmo", "dados"]
    };

    const counts = { Backend: 0, Frontend: 0, DevOps: 0, "Data Science": 0 };
    const detectedTags = [];

    // Contabiliza ocorrências
    Object.keys(keywordsDb).forEach(cat => {
      keywordsDb[cat].forEach(word => {
        if (combined.includes(word)) {
          counts[cat] += 2; // peso para correspondência de categoria
          if (detectedTags.length < 6 && !detectedTags.includes(word)) {
            // Capitaliza tags para apresentação
            detectedTags.push(word.charAt(0).toUpperCase() + word.slice(1));
          }
        }
      });
    });

    // Se nenhuma tag detectada, adiciona genéricas baseadas no tipo
    if (detectedTags.length === 0) {
      detectedTags.push(type);
      detectedTags.push("Tech");
    }

    // Determina categoria vencedora
    let winner = "Backend"; // default
    let maxCount = -1;
    
    Object.keys(counts).forEach(cat => {
      if (counts[cat] > maxCount) {
        maxCount = counts[cat];
        winner = cat;
      }
    });

    // Se não houver correspondências, escolhe uma com base em caracteres ou aleatório parcial
    if (maxCount === 0) {
      const options = ["Backend", "Frontend", "DevOps", "Data Science"];
      winner = options[Math.floor(Math.random() * options.length)];
    }

    // Calcula confiança realista baseado no número de correspondências
    let confidence = 0.7 + (Math.min(maxCount, 8) / 8) * 0.28;
    confidence = Math.min(confidence, 0.99); // max 99%

    const docId = `doc-${String(documentos.length + 1).padStart(3, "0")}`;
    const cleanCategorySlug = winner.toLowerCase().replace(" ", "");

    return {
      id: docId,
      titulo: title,
      tipo: type,
      texto: text,
      categoria: winner,
      probabilidade: parseFloat(confidence.toFixed(2)),
      informacoes_adicionais: detectedTags,
      data_criacao: new Date().toISOString(),
      oci_storage_path: `oci://techmind-kb-bucket/raw/${cleanCategorySlug}/${docId}.json`
    };
  };

  const renderAnalysisResult = (result) => {
    // 1. Categoria e Barra
    const categoryElem = document.getElementById("res-category");
    categoryElem.innerText = result.categoria;
    
    const confidenceElem = document.getElementById("res-confidence");
    confidenceElem.innerText = `Confiança: ${(result.probabilidade * 100).toFixed(0)}%`;

    // 2. Tags
    const tagsContainer = document.getElementById("res-tags");
    tagsContainer.innerHTML = "";
    result.informacoes_adicionais.forEach(tag => {
      const badge = document.createElement("span");
      badge.className = "tag";
      badge.innerText = tag;
      tagsContainer.appendChild(badge);
    });

    // 3. OCI Path
    document.getElementById("res-oci-path").innerText = result.oci_storage_path;

    // 4. JSON de saída
    const jsonOutput = {
      categoria: result.categoria,
      probabilidade: result.probabilidade,
      informacoes_adicionais: result.informacoes_adicionais
    };
    
    document.getElementById("res-json-pre").innerText = JSON.stringify(jsonOutput, null, 2);
  };

  // --- BIBLIOTECA DE DOCUMENTOS (LISTAGEM & FILTRAGEM) ---
  let activeCategoryFilter = "Todos";
  let activeSearchQuery = "";

  const renderLibrary = () => {
    const grid = document.getElementById("library-grid");
    if (!grid) return;

    grid.innerHTML = "";

    // Filtra documentos
    const filtered = documentos.filter(doc => {
      const matchesCat = activeCategoryFilter === "Todos" || doc.categoria === activeCategoryFilter;
      
      const searchLower = activeSearchQuery.toLowerCase();
      const matchesSearch = activeSearchQuery === "" || 
        doc.titulo.toLowerCase().includes(searchLower) ||
        doc.texto.toLowerCase().includes(searchLower) ||
        doc.informacoes_adicionais.some(tag => tag.toLowerCase().includes(searchLower));
        
      return matchesCat && matchesSearch;
    });

    if (filtered.length === 0) {
      grid.innerHTML = `
        <div class="no-results">
          <h3>Nenhum conteúdo técnico encontrado</h3>
          <p style="margin-top:8px; color:var(--text-muted); font-size:13px;">Tente alterar os filtros ou cadastrar novos conteúdos na aba "Cadastrar Conteúdo".</p>
        </div>
      `;
      return;
    }

    filtered.forEach(doc => {
      const card = document.createElement("div");
      card.className = "doc-card";
      
      // Determina classe de categoria para cor do badge
      const catClass = `cat-${doc.categoria.toLowerCase().replace(" ", "")}`;
      const dateStr = new Date(doc.data_criacao).toLocaleDateString("pt-BR");

      card.innerHTML = `
        <div class="doc-card-header">
          <span class="doc-type-badge">${doc.tipo}</span>
          <span class="doc-card-category ${catClass}">${doc.categoria}</span>
        </div>
        <h3>${doc.titulo}</h3>
        <p class="doc-snippet">${doc.texto}</p>
        <div class="doc-card-footer">
          <span>${dateStr}</span>
          <span class="doc-confidence">
            <svg viewBox="0 0 24 24"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14M22 4L12 14.01l-3-3" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>
            ${(doc.probabilidade * 100).toFixed(0)}%
          </span>
        </div>
      `;
      
      // Evento de clique para abrir detalhes no modal
      card.addEventListener("click", () => openDocModal(doc));
      grid.appendChild(card);
    });
  };

  const initLibraryFilters = () => {
    const searchInput = document.getElementById("library-search");
    if (searchInput) {
      searchInput.addEventListener("input", (e) => {
        activeSearchQuery = e.target.value;
        renderLibrary();
      });
    }

    const filterButtons = document.querySelectorAll(".filter-btn");
    filterButtons.forEach(btn => {
      btn.addEventListener("click", () => {
        filterButtons.forEach(b => b.classList.remove("active"));
        btn.classList.add("active");
        
        activeCategoryFilter = btn.getAttribute("data-category");
        renderLibrary();
        logOCI(`Filtro da biblioteca alterado para: "${activeCategoryFilter}"`, "info");
      });
    });
  };

  // --- DETALHES EM MODAL ---
  const modalOverlay = document.getElementById("doc-modal");
  const modalTitle = document.getElementById("modal-doc-title");
  const modalType = document.getElementById("modal-doc-type");
  const modalCategory = document.getElementById("modal-doc-category");
  const modalConfidence = document.getElementById("modal-doc-confidence");
  const modalDate = document.getElementById("modal-doc-date");
  const modalOciPath = document.getElementById("modal-doc-oci-path");
  const modalText = document.getElementById("modal-doc-text");
  const modalJsonPre = document.getElementById("modal-doc-json-pre");

  const openDocModal = (doc) => {
    if (!modalOverlay) return;

    modalTitle.innerText = doc.titulo;
    modalType.innerText = `Tipo: ${doc.tipo}`;
    modalCategory.innerText = `Categoria: ${doc.categoria}`;
    modalConfidence.innerText = `Confiança: ${(doc.probabilidade * 100).toFixed(0)}%`;
    modalDate.innerText = `Data: ${new Date(doc.data_criacao).toLocaleDateString("pt-BR")}`;
    modalOciPath.innerText = doc.oci_storage_path;
    modalText.innerText = doc.texto;

    const fullJson = {
      id: doc.id,
      titulo: doc.titulo,
      tipo: doc.tipo,
      texto: doc.texto,
      analise_ia: {
        categoria: doc.categoria,
        probabilidade: doc.probabilidade,
        informacoes_adicionais: doc.informacoes_adicionais
      },
      oci_metadata: {
        bucket: "techmind-kb-bucket",
        storage_path: doc.oci_storage_path,
        encryption: "AES-256 (OCI KMS Managed)"
      }
    };
    
    modalJsonPre.innerText = JSON.stringify(fullJson, null, 2);
    modalOverlay.classList.add("active");
    
    logOCI(`Visualizando detalhes completos do documento ID: ${doc.id}`, "info");
  };

  const initModal = () => {
    const closeBtn = document.getElementById("modal-close");
    if (closeBtn) {
      closeBtn.addEventListener("click", () => {
        modalOverlay.classList.remove("active");
      });
    }
    
    if (modalOverlay) {
      modalOverlay.addEventListener("click", (e) => {
        if (e.target === modalOverlay) {
          modalOverlay.classList.remove("active");
        }
      });
    }

    // Funcionalidades de cópia de JSON
    const btnCopyAnalysis = document.getElementById("btn-copy-res-json");
    if (btnCopyAnalysis) {
      btnCopyAnalysis.addEventListener("click", () => {
        const text = document.getElementById("res-json-pre").innerText;
        navigator.clipboard.writeText(text).then(() => {
          alert("JSON de análise copiado para a área de transferência!");
          logOCI("JSON de análise copiado pelo usuário.", "info");
        });
      });
    }

    const btnCopyModalJson = document.getElementById("btn-copy-modal-json");
    if (btnCopyModalJson) {
      btnCopyModalJson.addEventListener("click", () => {
        const text = modalJsonPre.innerText;
        navigator.clipboard.writeText(text).then(() => {
          alert("JSON completo copiado!");
          logOCI("JSON estruturado de documento copiado pelo usuário.", "info");
        });
      });
    }
  };

  // --- MOTOR DE BUSCA SEMÂNTICA ---
  const initSemanticSearch = () => {
    const input = document.getElementById("semantic-search-input");
    const btn = document.getElementById("btn-semantic-search");
    const resultsContainer = document.getElementById("semantic-results");

    if (!btn || !input || !resultsContainer) return;

    const performSearch = () => {
      const query = input.value.trim().toLowerCase();
      if (query.length < 3) {
        alert("Digite um termo de pesquisa com pelo menos 3 caracteres.");
        return;
      }

      logOCI(`Iniciando busca semântica para consulta: "${query}"`, "info");

      // Stopwords comuns em português
      const stopwords = ["de", "a", "o", "que", "e", "do", "da", "em", "um", "para", "com", "na", "no", "uma", "os", "as", "como", "sobre", "mais", "web", "api"];
      
      const queryWords = query.split(/\s+/)
        .map(w => w.replace(/[.,\/#!$%\^&\*;:{}=\-_`~()]/g,""))
        .filter(w => w.length > 2 && !stopwords.includes(w));

      if (queryWords.length === 0) {
        queryWords.push(query); // cai de volta na query inteira
      }

      // Calcula similaridades para cada documento
      const rankedResults = documentos.map(doc => {
        const textToMatch = (doc.titulo + " " + doc.texto + " " + doc.informacoes_adicionais.join(" ")).toLowerCase();
        
        let score = 0;
        let matches = 0;

        queryWords.forEach(word => {
          if (textToMatch.includes(word)) {
            matches++;
            // Dá pesos diferentes para correspondências no título vs texto
            if (doc.titulo.toLowerCase().includes(word)) {
              score += 0.4;
            } else {
              score += 0.25;
            }
          }
        });

        // Adiciona um valor semântico simulado para sinônimos tecnológicos clássicos
        const synonyms = {
          backend: ["spring", "java", "sql", "api", "database", "security"],
          frontend: ["react", "js", "css", "html", "grid", "hooks"],
          cloud: ["oci", "deploy", "kubernetes", "oke", "storage", "bucket"],
          dados: ["python", "pandas", "ml", "machine", "scikit", "regressão"]
        };

        Object.keys(synonyms).forEach(key => {
          if (query.includes(key)) {
            synonyms[key].forEach(syn => {
              if (textToMatch.includes(syn)) {
                score += 0.15; // bônus de relação semântica
              }
            });
          }
        });

        // Normalização matemática do score para simular graus realistas de similaridade
        let similarityPercent = 0;
        if (matches > 0 || score > 0) {
          // Garante valor entre 40% e 98%
          similarityPercent = 40 + Math.min((score * 20), 58);
          
          // Adiciona ruído estocástico controlado para parecer processamento em tempo real
          similarityPercent += (Math.sin(doc.id.charCodeAt(4)) * 3);
          similarityPercent = Math.min(similarityPercent, 99);
        }

        return {
          document: doc,
          score: parseFloat(similarityPercent.toFixed(1))
        };
      });

      // Ordena por maior pontuação e filtra os que têm 0%
      const filteredResults = rankedResults
        .filter(r => r.score > 0)
        .sort((a, b) => b.score - a.score);

      // Renderiza resultados
      resultsContainer.innerHTML = "";

      if (filteredResults.length === 0) {
        resultsContainer.innerHTML = `
          <div class="no-results" style="grid-column: 1/-1;">
            <h3>Nenhum conteúdo com relevância similar encontrado</h3>
            <p style="margin-top:8px; color:var(--text-muted); font-size:13px;">Tente digitar outros termos técnicos como "Spring", "CSS Layout", "Object Storage" ou "Regressão".</p>
          </div>
        `;
        logOCI("Pesquisa semântica concluída sem correspondências.", "warn");
        return;
      }

      filteredResults.forEach(item => {
        const doc = item.document;
        const resItem = document.createElement("div");
        resItem.className = "semantic-result-item";
        
        const catClass = `cat-${doc.categoria.toLowerCase().replace(" ", "")}`;
        
        resItem.innerHTML = `
          <div class="semantic-result-main">
            <div class="semantic-result-header">
              <h3>${doc.titulo}</h3>
              <span class="doc-type-badge">${doc.tipo}</span>
              <span class="doc-card-category ${catClass}">${doc.categoria}</span>
            </div>
            <p class="semantic-result-text">${doc.texto}</p>
            <div class="semantic-result-tags">
              ${doc.informacoes_adicionais.map(tag => `<span class="tag tag-purple">${tag}</span>`).join("")}
            </div>
          </div>
          <div class="semantic-result-side">
            <span class="similarity-badge">${item.score}% Match</span>
            <span style="font-size:10px; color:var(--text-muted);">ID: ${doc.id}</span>
          </div>
        `;

        resItem.addEventListener("click", () => openDocModal(doc));
        resultsContainer.appendChild(resItem);
      });

      logOCI(`Pesquisa semântica concluída com ${filteredResults.length} resultados listados.`, "success");
    };

    btn.addEventListener("click", performSearch);
    input.addEventListener("keyup", (e) => {
      if (e.key === "Enter") performSearch();
    });
  };

  // --- MONITOR INTEGRADO E REDE OCI ---
  const renderOCIMonitor = () => {
    // Restaura e desenha fluxo
    const nodes = document.querySelectorAll(".diagram-node");
    
    // Animação de pulso nos nós de forma sequencial para demonstrar fluxo ativo
    nodes.forEach((node, i) => {
      node.style.opacity = "0.3";
      setTimeout(() => {
        node.style.opacity = "1";
        node.classList.add("active-node");
        setTimeout(() => {
          node.classList.remove("active-node");
        }, 1000);
      }, i * 300);
    });
  };

  // --- CONFIGURAÇÕES DO PAINEL ---
  const initSettings = () => {
    const apiToggle = document.getElementById("api-mode-toggle");
    const apiUrlInput = document.getElementById("api-url-input");
    const btnSaveSettings = document.getElementById("btn-save-settings");
    const btnTestConnection = document.getElementById("btn-test-connection");
    const presetsContainer = document.getElementById("backend-presets-container");
    const statusDot = document.getElementById("api-status-dot");
    const statusText = document.getElementById("api-status-text");

    if (!apiToggle || !apiUrlInput || !btnSaveSettings) return;

    // Presets de backend conhecidos
    const backendPresets = [
      { label: "Local (Spring Boot na porta 8080)", url: "/conteudo", health: "/actuator/health" },
      { label: "Backend OCI do Diego", url: "http://163.176.134.19:8080/conteudo", health: "http://163.176.134.19:8080/health" },
    ];

    if (presetsContainer) {
      presetsContainer.innerHTML = backendPresets.map(p => `
        <div class="credential-item" data-url="${p.url}" data-health="${p.health}">
          <div class="credential-info">
            <strong>${p.label}</strong><br/>
            <span style="font-family: var(--font-mono); font-size:11px;">${p.url}</span>
          </div>
          <span class="btn-fill-badge">Usar</span>
        </div>
      `).join("");

      presetsContainer.querySelectorAll(".credential-item").forEach(item => {
        item.addEventListener("click", () => {
          if (apiUrlInput.disabled) {
            apiToggle.checked = true;
            apiUrlInput.disabled = false;
          }
          apiUrlInput.value = item.getAttribute("data-url");
          logOCI(`Configuração: backend selecionado — ${item.getAttribute("data-url")}`, "warn");
        });
      });
    }

    const setStatus = (state, text) => {
      const colors = {
        idle: "var(--text-muted)",
        testing: "#fbbf24",
        ok: "#10b981",
        fail: "#ef4444",
      };
      if (statusDot) statusDot.style.background = colors[state] || colors.idle;
      if (statusText) statusText.innerText = text;
      if (statusDot && state === "testing") {
        statusDot.style.animation = "pulse 1s infinite";
      } else if (statusDot) {
        statusDot.style.animation = "none";
      }
    };

    apiToggle.addEventListener("change", () => {
      apiUrlInput.disabled = !apiToggle.checked;
      if (!apiToggle.checked) setStatus("idle", "Modo live desativado — usando motor simulado (mock).");
      logOCI(`Configuração: Conexão API integrada definida como ${apiToggle.checked ? "LIVE (backend)" : "MOCKED"}`, "warn");
    });

    if (btnTestConnection) {
      // Tenta vários endpoints; usa no-cors como último recurso para só checar se o servidor responde
      const tryHealth = (healthUrl) =>
        fetch(healthUrl, { method: "GET" })
          .then(resp => {
            if (resp.ok) return { ok: true, healthUrl, fallback: false };
            throw new Error(`HTTP ${resp.status}`);
          });

      const tryHealthNoCors = (healthUrl) =>
        fetch(healthUrl, { method: "GET", mode: "no-cors" })
          .then(resp => ({ ok: resp.type === "opaque", healthUrl, fallback: true }));

      btnTestConnection.addEventListener("click", () => {
        let url = apiUrlInput.value.trim();
        if (!url || apiUrlInput.disabled) {
          alert("Ative o modo live e informe a URL do backend antes de testar.");
          return;
        }

        // Se for URL relativa (backend local mesmo host), usa origin do dashboard
        let base = url.replace(/\/conteudo\/?$/, "");
        if (base === "" || base.startsWith("/")) {
          base = window.location.origin + base;
        }
        const candidates = [`${base}/health`, `${base}/actuator/health`];

        setStatus("testing", `Testando conexão com ${base}...`);
        logOCI(`Testando conexão com backend: ${base}`, "info");

        tryHealth(candidates[0])
          .catch(() => tryHealth(candidates[1]))
          .catch(() => tryHealthNoCors(candidates[0]))
          .catch(() => tryHealthNoCors(candidates[1]))
          .then(r => {
            if (r && r.ok) {
              setStatus("ok", r.fallback ? "Servidor alcançável (resposta opaca)." : "Servidor respondeu (conexão OK).");
              logOCI(`Teste de conexão: SUCESSO — servidor alcançável via ${r.healthUrl}`, "success");
            } else {
              setStatus("fail", "Servidor não respondeu em nenhum endpoint de saúde.");
              logOCI(`Teste de conexão: FALHA`, "error");
            }
          })
          .catch(err => {
            setStatus("fail", `Falha de rede ao conectar: ${err.message}`);
            logOCI(`Teste de conexão falhou: ${err.message}`, "error");
          });
      });
    }


    btnSaveSettings.addEventListener("click", () => {
      const mode = apiToggle.checked ? "live" : "mock";
      const url = apiUrlInput.value.trim();

      sessionStorage.setItem("techmind_api_mode", mode);
      sessionStorage.setItem("techmind_api_url", url);

      alert("Configurações do sistema aplicadas!\n\nModo: " + (mode === "live" ? "API em tempo real" : "Simulado (mock)") + "\nURL: " + url);
      logOCI(`Configurações de rede salvas. URL Base: ${url}`, "success");

      if (mode === "live") {
        btnSaveSettings.dispatchEvent(new Event("click-test"));
      }
    });
  };

  // --- INICIALIZAÇÃO GERAL ---
  loadState();
  initNavigation();
  updateDashboard();
  initQuickExamples();
  initAnalyzer();
  initLibraryFilters();
  initModal();
  initSemanticSearch();
  initSettings();

  // Logs iniciais do sistema
  logOCI("Sistema TechMind inicializado com sucesso.", "success");
  logOCI("Mock API de processamento NLP carregado localmente.", "success");
  logOCI("Persistência em SessionStorage ativa.", "info");
});
