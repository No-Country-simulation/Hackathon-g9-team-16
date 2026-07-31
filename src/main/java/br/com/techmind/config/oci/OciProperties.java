package br.com.techmind.config.oci;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "oci")
public class OciProperties {

    private String configFileLocation;
    private String profile = "DEFAULT";
    private String tenantId;
    private String userId;
    private String fingerprint;
    private String privateKeyPath;
    private String region = "us-chicago-1";
    private String compartmentId;
    private GenerativeAi generativeai = new GenerativeAi();

    public static class GenerativeAi {
        private String modelId = "cohere.command-r-plus";

        public String getModelId() {
            return modelId;
        }

        public void setModelId(String modelId) {
            this.modelId = modelId;
        }
    }

    public String getConfigFileLocation() {
        return configFileLocation;
    }

    public void setConfigFileLocation(String configFileLocation) {
        this.configFileLocation = configFileLocation;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCompartmentId() {
        return compartmentId;
    }

    public void setCompartmentId(String compartmentId) {
        this.compartmentId = compartmentId;
    }

    public GenerativeAi getGenerativeai() {
        return generativeai;
    }

    public void setGenerativeai(GenerativeAi generativeai) {
        this.generativeai = generativeai;
    }

    public boolean isConfigured() {
        boolean hasConfigFile = configFileLocation != null && !configFileLocation.isBlank();
        boolean hasDirectConfig = tenantId != null && !tenantId.isBlank()
                && userId != null && !userId.isBlank()
                && fingerprint != null && !fingerprint.isBlank()
                && privateKeyPath != null && !privateKeyPath.isBlank();
        return hasConfigFile || hasDirectConfig;
    }
}
