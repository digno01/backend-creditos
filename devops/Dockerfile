FROM openjdk:17-jdk-slim AS builder

# Instalar Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copiar arquivos necessários para o build
COPY ../pom.xml .
COPY ../src ./src

# Build da aplicação
RUN mvn clean package -DskipTests

# Imagem final para runtime
FROM openjdk:17-jre-slim

# Criar diretório para logs
RUN mkdir -p /app/logs

WORKDIR /app

# Copiar o JAR da etapa de build
COPY --from=builder /app/target/*.jar app.jar

# Expor a porta da aplicação
EXPOSE 8080

# Configurações de JVM otimizadas para Java 17
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:G1HeapRegionSize=16m -XX:+UseStringDeduplication"

# Comando para executar a aplicação
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]