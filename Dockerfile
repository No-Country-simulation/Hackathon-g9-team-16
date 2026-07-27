# Stage 1: Build da Aplicação com Maven
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copia dependências do Maven primeiro para otimizar o cache de camadas
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN ./mvnw dependency:go-offline -B

# Copia código-fonte e realiza o build sem executar testes (testes são executados no CI/CD)
COPY src ./src
RUN ./mvnw package -DskipTests -B

# Stage 2: Imagem leve de Execução em Produção
FROM eclipse-temurin:17-jre-alpine WORKDIR /app

# Cria usuário não-root por questões de segurança
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copia o artefato JAR compilado do builder
COPY --from=builder /app/target/*.jar app.jar

# Variáveis de ambiente configuráveis
ENV PORT=8080
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
