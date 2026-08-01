package br.com.techmind.service;

import br.com.techmind.config.oci.OciProperties;
import br.com.techmind.dto.ConteudoResponse;
import com.oracle.bmc.generativeaiinference.GenerativeAiInferenceClient;
import com.oracle.bmc.generativeaiinference.model.*;
import com.oracle.bmc.generativeaiinference.requests.GenerateTextRequest;
import com.oracle.bmc.generativeaiinference.responses.GenerateTextResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class OciGenerativeAiService {

    private static final Logger log = LoggerFactory.getLogger(OciGenerativeAiService.class);

    private final Optional<GenerativeAiInferenceClient> ociClient;
    private final OciProperties ociProperties;

    public OciGenerativeAiService(Optional<GenerativeAiInferenceClient> ociClient, OciProperties ociProperties) {
        this.ociClient = ociClient;
        this.ociProperties = ociProperties;
    }

    public boolean isOciAvailable() {
        return ociClient.isPresent()
                && ociProperties.getCompartmentId() != null
                && !ociProperties.getCompartmentId().isBlank();
    }

    public Optional<ConteudoResponse> processarComOci(String titulo, String texto) {
        if (!isOciAvailable()) {
            log.info("Integração OCI indisponível ou compartimento não configurado. Retornando resposta de teste/fallback.");
            return Optional.empty();
        }

        try {
            String prompt = String.format(
                    "Analise o seguinte conteúdo:\nTítulo: %s\nTexto: %s\n\n" +
                    "Forneça uma resposta curta e objetiva com:\n" +
                    "1. Resumo em até 2 frases.\n" +
                    "2. 3 Palavras-chave principais separadas por vírgula.\n" +
                    "3. Categoria principal.",
                    titulo, texto
            );

            CohereLlmInferenceRequest llmInferenceRequest = CohereLlmInferenceRequest.builder()
                    .prompt(prompt)
                    .maxTokens(300)
                    .temperature(0.3)
                    .build();

            GenerateTextDetails generateTextDetails = GenerateTextDetails.builder()
                    .servingMode(OnDemandServingMode.builder()
                            .modelId(ociProperties.getGenerativeai().getModelId())
                            .build())
                    .compartmentId(ociProperties.getCompartmentId())
                    .inferenceRequest(llmInferenceRequest)
                    .build();

            GenerateTextRequest generateTextRequest = GenerateTextRequest.builder()
                    .generateTextDetails(generateTextDetails)
                    .build();

            log.info("Enviando requisição para OCI Generative AI (Modelo: {})...", ociProperties.getGenerativeai().getModelId());
            GenerateTextResponse response = ociClient.get().generateText(generateTextRequest);

            if (response != null && response.getGenerateTextResult() != null) {
                GenerateTextResult result = response.getGenerateTextResult();
                if (result.getInferenceResponse() instanceof CohereLlmInferenceResponse cohereResponse) {
                    List<GeneratedText> generatedTexts = cohereResponse.getGeneratedTexts();
                    if (generatedTexts != null && !generatedTexts.isEmpty()) {
                        String generatedContent = generatedTexts.get(0).getText();

                        ConteudoResponse res = new ConteudoResponse();
                        res.setCategoria("Processado por OCI AI");
                        res.setProbabilidade(0.98);
                        res.setPalavrasChave(List.of("OCI", "Generative AI", "TechMind"));
                        res.setResumo(generatedContent != null ? generatedContent.trim() : "Resumo indisponível.");
                        return Optional.of(res);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Erro ao chamar o OCI Generative AI: {}", e.getMessage(), e);
        }

        return Optional.empty();
    }
}
