# Deploy da API - Hackathon TechMind

## Visão Geral

Este documento descreve o processo de implantação (deploy) da API Spring Boot em uma máquina virtual Oracle Cloud Infrastructure (OCI).

---

## Ambiente

### Infraestrutura

- Oracle Cloud Infrastructure (OCI)
- Oracle Linux 8
- Máquina Virtual Compute Instance

### Tecnologias

- Java 17 (OpenJDK)
- Spring Boot 3.5
- Maven
- PostgreSQL 10
- systemd

---

## Banco de Dados

Banco utilizado:

PostgreSQL

Banco criado:

```
hackathon_db
```

As tabelas são criadas automaticamente pelo Hibernate utilizando:

```properties
spring.jpa.hibernate.ddl-auto=update
```

---

## Build da aplicação

Gerar o arquivo JAR:

```bash
./mvnw clean package -DskipTests
```

O artefato gerado fica em:

```
target/hackathon-api-0.0.1-SNAPSHOT.jar
```

---

## Execução manual

```bash
java -jar target/hackathon-api-0.0.1-SNAPSHOT.jar
```

---

## Serviço systemd

Arquivo:

```
/etc/systemd/system/hackathon-api.service
```

Responsável por iniciar automaticamente a aplicação após reinicialização da máquina.

Principais comandos:

Iniciar:

```bash
sudo systemctl start hackathon-api
```

Parar:

```bash
sudo systemctl stop hackathon-api
```

Reiniciar:

```bash
sudo systemctl restart hackathon-api
```

Verificar status:

```bash
sudo systemctl status hackathon-api
```

Logs:

```bash
sudo journalctl -u hackathon-api -f
```

---

## Health Check

Endpoint utilizado para validar a aplicação:

```
GET /health
```

Exemplo:

```
http://IP_DA_VM:8080/health
```

Resposta esperada:

```json
{
  "database": "OK",
  "service": "Hackathon API",
  "version": "1.0.0",
  "status": "UP"
}
```

---

## Endpoints

### Health

```
GET /health
```

### Times

```
GET /api/teams
```

```
GET /api/teams/{id}
```

```
POST /api/teams
```

### Classificação de Conteúdo

```
POST /api/conteudo/classificar
```

---

## Arquitetura

Cliente

↓

Spring Boot REST API

↓

Spring Data JPA

↓

PostgreSQL

---

## Status do Deploy

- Java instalado
- PostgreSQL configurado
- Banco criado
- API compilada
- Serviço systemd configurado
- Inicialização automática habilitada
- API operacional