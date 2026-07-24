package br.com.techmind.service;

import br.com.techmind.dto.ConteudoRequest;
import br.com.techmind.dto.ConteudoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ConteudoService {

    private static final Logger log = LoggerFactory.getLogger(ConteudoService.class);

    private static final Map<String, List<String>> CATEGORY_KEYWORDS = Map.of(
            "Backend", List.of("java", "spring", "boot", "api", "rest", "node", "express", "python", "django", "sql", "postgres", "mysql", "microservices", "hibernate", "jpa", "c#", ".net"),
            "Frontend", List.of("react", "vue", "angular", "javascript", "typescript", "css", "html", "tailwind", "next", "redux", "frontend", "ui", "ux", "browser", "dom", "vite"),
            "DevOps", List.of("kubernetes", "docker", "terraform", "aws", "azure", "gcp", "ci/cd", "pipeline", "actions", "devops", "ansible", "cloud", "linux", "helm", "prometheus"),
            "Data & AI", List.of("python", "pandas", "numpy", "tensorflow", "pytorch", "ai", "ia", "inteligencia", "data", "machine", "learning", "nlp", "llm", "spark", "bigdata"),
            "Cybersecurity", List.of("segurança", "seguranca", "security", "jwt", "oauth", "ssl", "tls", "criptografia", "firewall", "vulnerabilidade", "pentest", "auth", "token", "xss", "csrf")
    );

    public ConteudoResponse processar(ConteudoRequest request) {
        log.info("Processando solicitação de análise de conteúdo com título: '{}'", request.getTitulo());

        String fullText = (request.getTitulo() + " " + request.getTexto()).toLowerCase();

        Map<String, Integer> categoryScores = new HashMap<>();
        Map<String, Set<String>> categoryMatchedWords = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : CATEGORY_KEYWORDS.entrySet()) {
            String category = entry.getKey();
            int score = 0;
            Set<String> matchedWords = new LinkedHashSet<>();

            for (String keyword : entry.getValue()) {
                Pattern pattern = Pattern.compile("\\b" + Pattern.quote(keyword) + "\\b", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(fullText);
                while (matcher.find()) {
                    score++;
                    matchedWords.add(capitalize(keyword));
                }
            }

            categoryScores.put(category, score);
            categoryMatchedWords.put(category, matchedWords);
        }

        String bestCategory = "Tecnologia Geral";
        int maxScore = 0;

        for (Map.Entry<String, Integer> entry : categoryScores.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                bestCategory = entry.getKey();
            }
        }

        double probabilidade;
        if (maxScore == 0) {
            probabilidade = 0.70;
            bestCategory = "Tecnologia Geral";
        } else if (maxScore <= 2) {
            probabilidade = 0.85;
        } else if (maxScore <= 5) {
            probabilidade = 0.92;
        } else {
            probabilidade = 0.98;
        }

        Set<String> extractedKeywords = new LinkedHashSet<>();
        if (categoryMatchedWords.containsKey(bestCategory) && !categoryMatchedWords.get(bestCategory).isEmpty()) {
            extractedKeywords.addAll(categoryMatchedWords.get(bestCategory));
        }

        for (Set<String> matched : categoryMatchedWords.values()) {
            extractedKeywords.addAll(matched);
            if (extractedKeywords.size() >= 5) break;
        }

        if (extractedKeywords.isEmpty()) {
            extractedKeywords.add("Tecnologia");
            extractedKeywords.add("Análise");
        }

        List<String> finalKeywordsList = extractedKeywords.stream().limit(5).collect(Collectors.toList());

        String resumo = gerarResumo(request.getTitulo(), request.getTexto());

        log.info("Conteúdo analisado com sucesso. Categoria: '{}', Probabilidade: {}, Palavras-chave: {}",
                bestCategory, probabilidade, finalKeywordsList);

        return new ConteudoResponse(bestCategory, probabilidade, finalKeywordsList, resumo);
    }

    private String gerarResumo(String titulo, String texto) {
        String limpo = texto.trim().replaceAll("\\s+", " ");
        if (limpo.length() <= 150) {
            return limpo;
        }
        int pontoFinalIndex = limpo.indexOf(".", 80);
        if (pontoFinalIndex > 0 && pontoFinalIndex <= 200) {
            return limpo.substring(0, pontoFinalIndex + 1);
        }
        return limpo.substring(0, 150) + "...";
    }

    private String capitalize(String word) {
        if (word == null || word.isEmpty()) return word;
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }
}