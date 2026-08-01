package br.com.techmind.config.oci;

import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.Region;
import com.oracle.bmc.generativeaiinference.GenerativeAiInferenceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Optional;

@Configuration
public class OciConfig {

    private static final Logger log = LoggerFactory.getLogger(OciConfig.class);

    private final OciProperties ociProperties;

    public OciConfig(OciProperties ociProperties) {
        this.ociProperties = ociProperties;
    }

    @Bean
    public Optional<AuthenticationDetailsProvider> ociAuthenticationDetailsProvider() {
        try {
            // 1. Tentar via arquivo de configuração (ex: ~/.oci/config ou oci.config-file-location)
            String configPath = ociProperties.getConfigFileLocation();
            if (configPath == null || configPath.isBlank()) {
                String userHome = System.getProperty("user.home");
                File defaultConfigFile = Paths.get(userHome, ".oci", "config").toFile();
                if (defaultConfigFile.exists()) {
                    configPath = defaultConfigFile.getAbsolutePath();
                }
            }

            if (configPath != null && new File(configPath).exists()) {
                log.info("Carregando autenticação OCI via arquivo de configuração: {}", configPath);
                AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(
                        configPath,
                        ociProperties.getProfile()
                );
                return Optional.of(provider);
            }

            // 2. Tentar via propriedades diretas (ex: OCI_TENANT_ID, OCI_USER_ID, etc.)
            if (ociProperties.getTenantId() != null && !ociProperties.getTenantId().isBlank()
                    && ociProperties.getUserId() != null && !ociProperties.getUserId().isBlank()
                    && ociProperties.getFingerprint() != null && !ociProperties.getFingerprint().isBlank()
                    && ociProperties.getPrivateKeyPath() != null && !ociProperties.getPrivateKeyPath().isBlank()) {

                log.info("Carregando autenticação OCI via propriedades diretas.");
                File keyFile = new File(ociProperties.getPrivateKeyPath());

                SimpleAuthenticationDetailsProvider provider = SimpleAuthenticationDetailsProvider.builder()
                        .tenantId(ociProperties.getTenantId())
                        .userId(ociProperties.getUserId())
                        .fingerprint(ociProperties.getFingerprint())
                        .privateKeySupplier(() -> {
                            try {
                                return new FileInputStream(keyFile);
                            } catch (Exception e) {
                                throw new RuntimeException("Falha ao ler chave privada OCI no caminho: " + keyFile.getAbsolutePath(), e);
                            }
                        })
                        .build();

                return Optional.of(provider);
            }

            log.warn("Nenhuma credencial OCI válida foi identificada. O modo fallback local será utilizado.");
            return Optional.empty();

        } catch (Exception e) {
            log.error("Erro ao inicializar o provedor de autenticação OCI: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Bean
    public Optional<GenerativeAiInferenceClient> generativeAiInferenceClient(
            Optional<AuthenticationDetailsProvider> authProvider) {

        if (authProvider.isEmpty()) {
            log.warn("GenerativeAiInferenceClient não será inicializado pois a autenticação OCI não foi configurada.");
            return Optional.empty();
        }

        try {
            GenerativeAiInferenceClient client = GenerativeAiInferenceClient.builder()
                    .build(authProvider.get());

            if (ociProperties.getRegion() != null && !ociProperties.getRegion().isBlank()) {
                client.setRegion(Region.fromRegionId(ociProperties.getRegion()));
            }

            log.info("GenerativeAiInferenceClient inicializado com sucesso para a região: {}", ociProperties.getRegion());
            return Optional.of(client);
        } catch (Exception e) {
            log.error("Erro ao criar o cliente GenerativeAiInferenceClient da OCI: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }
}
