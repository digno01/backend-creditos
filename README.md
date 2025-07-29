# API de Créditos Tributários 💰

API REST para gerenciamento de créditos tributários do ISSQN, desenvolvida com Spring Boot 3 e Java 17.

## Tecnologias Utilizadas

- **Java 17** - Linguagem de programação
- **Spring Boot 3.2.1** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados principal
- **H2 Database** - Banco de dados para testes
- **Apache Kafka** - Mensageria assíncrona
- **MapStruct** - Mapeamento de objetos
- **Lombok** - Redução de código boilerplate
- **SpringDoc OpenAPI** - Documentação da API
- **JaCoCo** - Cobertura de testes
- **Docker & Docker Compose** - Containerização

## Pré-requisitos

### Para desenvolvimento local:
- Java 17 (recomendado via Scoop no Windows)
- Maven 3.6+
- Docker e Docker Compose
- Git

## Executando com Docker

### 1. Clone o repositório
```bash
git clone https://github.com/digno01/backend-creditos.git
cd backend-creditos
```

### 2. Subir toda a infraestrutura
```bash
docker-compose up -d
```

Este comando irá subir:
- **PostgreSQL** (porta 5432)
- **Zookeeper** (porta 2181)
- **Kafka** (porta 9092)
- **Kafka UI** (porta 8090)
- **API Backend** (porta 8080)

### 3. Verificar se os serviços estão rodando
```bash
docker-compose ps
```

### 4. Acessar a aplicação
- **API:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **Kafka UI:** http://localhost:8090
- **Health Check:** http://localhost:8080/actuator/health

### 5. Parar os serviços
```bash
docker-compose down
```

### 6. Parar e remover volumes (dados)
```bash
docker-compose down -v
```

## Desenvolvimento Local

### 1. Subir apenas a infraestrutura (sem a API)
```bash
docker-compose up -d postgres kafka zookeeper kafka-ui
```

### 2. Configurar variáveis de ambiente
```bash
# Windows PowerShell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/db_creditos"
$env:SPRING_DATASOURCE_USERNAME="postgres"
$env:SPRING_DATASOURCE_PASSWORD="postgres"
$env:SPRING_KAFKA_BOOTSTRAP_SERVERS="localhost:9092"
```

### 3. Executar a aplicação
```bash
mvn spring-boot:run
```

## Testes

### Executar todos os testes
```bash
mvn test
```

### Executar testes com relatório de cobertura
```bash
mvn clean test jacoco:report
```

### Visualizar relatório de cobertura
Após executar os testes, abra o arquivo:
```
target/site/jacoco/index.html
```

## Documentação da API

### Swagger UI
Acesse: http://localhost:8080/swagger-ui.html

### Endpoints principais

#### Consultar crédito por NFSE
```http
GET /api/creditos/nfse/{numeroNfse}
```

#### Consultar crédito por número do crédito
```http
GET /api/creditos/credito/{numeroCredito}
```

### Exemplo de resposta
```json
{
  "numeroCredito": "CR001",
  "numeroNfse": "NF001",
  "dataConstituicao": "2024-01-15",
  "valorIssqn": 100.00,
  "tipoCredito": "ISSQN",
  "simplesNacional": "Sim",
  "aliquota": 5.00,
  "valorFaturado": 2000.00,
  "valorDeducao": 0.00,
  "baseCalculo": 2000.00
}
```

## Banco de Dados

### Estrutura da tabela `creditos`
```sql
CREATE TABLE creditos (
    id BIGSERIAL PRIMARY KEY,
    numero_credito VARCHAR(50) UNIQUE NOT NULL,
    numero_nfse VARCHAR(50) UNIQUE NOT NULL,
    data_constituicao DATE NOT NULL,
    valor_issqn DECIMAL(15,2) NOT NULL,
    tipo_credito VARCHAR(20) NOT NULL,
    simples_nacional BOOLEAN NOT NULL,
    aliquota DECIMAL(5,2) NOT NULL,
    valor_faturado DECIMAL(15,2) NOT NULL,
    valor_deducao DECIMAL(15,2) NOT NULL,
    base_calculo DECIMAL(15,2) NOT NULL
);
```

### Dados de exemplo
O script `scripts/init-db.sql` contém dados de exemplo para testes.

## Kafka

### Tópicos criados automaticamente
- **creditos-consultas** - Eventos de consulta
- **creditos-eventos** - Eventos gerais
- **creditos-auditoria** - Logs de auditoria

### Monitoramento
Acesse o Kafka UI em: http://localhost:8090

## Configuração

### Profiles disponíveis
- **dev** - Desenvolvimento local
- **test** - Testes automatizados
- **docker** - Execução em container
- **prod** - Produção

### Variáveis de ambiente importantes
```bash
SPRING_PROFILES_ACTIVE=dev
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/db_creditos
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
SERVER_PORT=8080
```

## Monitoramento

### Actuator endpoints
- **Health:** `/actuator/health`
- **Metrics:** `/actuator/metrics`
- **Prometheus:** `/actuator/prometheus`
- **Kafka:** `/actuator/kafka`

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/api/credit/
│   │   ├── config/          # Configurações
│   │   ├── controller/      # Controllers REST
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # Entidades JPA
│   │   ├── exception/      # Tratamento de exceções
│   │   ├── mapper/         # Mapeadores MapStruct
│   │   ├── repository/     # Repositórios JPA
│   │   └── service/        # Serviços de negócio
│   └── resources/
│       ├── application.yml # Configurações da aplicação
│       └── data.sql       # Dados iniciais
└── test/
    └── java/com/api/credit/ # Testes unitários
```

## Deploy

### Build da aplicação
```bash
mvn clean package -DskipTests
```

### Executar JAR
```bash
java -jar target/api-credito-1.0.0.jar
```

### Build da imagem Docker
```bash
docker build -f devops/Dockerfile -t api-creditos .
```

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.


** DEV Fabricio Fernandes **