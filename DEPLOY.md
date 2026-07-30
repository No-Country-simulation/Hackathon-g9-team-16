# Deploy da API - Hackathon TechMind

Este documento descreve o processo de implantação (deploy) da API Spring Boot em uma máquina virtual Oracle Cloud Infrastructure (OCI).

---

## Ambiente

### Infraestrutura
- Oracle Cloud Infrastructure (OCI)
- Oracle Linux 8 / Ubuntu 22.04
- Máquina Virtual Compute Instance

### Tecnologias
- Java 17 (OpenJDK)
- Spring Boot 3.5
- Maven
- PostgreSQL 10+
- systemd

---

## Banco de Dados

Banco utilizado: **PostgreSQL**
Banco criado: `hackathon_db`

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

O artefato gerado fica em: `target/techmind-api-0.0.1-SNAPSHOT.jar`

---

## Execução manual

```bash
java -jar target/techmind-api-0.0.1-SNAPSHOT.jar
```

---

## Serviço systemd

Arquivo: `/etc/systemd/system/hackathon-api.service`
Responsável por iniciar automaticamente a aplicação após reinicialização da máquina.

**Principais comandos:**
- Iniciar: `sudo systemctl start hackathon-api`
- Parar: `sudo systemctl stop hackathon-api`
- Reiniciar: `sudo systemctl restart hackathon-api`
- Status: `sudo systemctl status hackathon-api`
- Logs: `sudo journalctl -u hackathon-api -f`

---

## Health Check

Endpoint utilizado para validar a aplicação: `GET /health`
Exemplo: `http://IP_DA_VM:8080/health`

---

## Endpoints

- **Health:** `GET /health`
- **Times:** `GET /api/teams` | `POST /api/teams`
- **Classificação:** `POST /conteudo` (Integrado com FastAPI)

---

## Status do Deploy
- [x] Java instalado
- [x] PostgreSQL configurado
- [x] Banco criado
- [x] API compilada
- [x] Serviço systemd configurado
- [x] Inicialização automática habilitada
- [x] API operacional
