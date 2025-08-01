FROM eclipse-temurin:17-jdk-alpine AS builder

# Instalar Maven
RUN apk add --no-cache maven

WORKDIR /app

# Copiar arquivos necessários para o build
COPY ../pom.xml .
COPY ../src ./src

# Build da aplicação
RUN mvn clean package -DskipTests

# Imagem final para runtime
FROM eclipse-temurin:17-jre-alpine

# Criar diretório para logs
RUN mkdir -p /app/logs

WORKDIR /app

# Copiar o JAR da etapa de build
COPY --from=builder /app/target/*.jar app.jar

# Expor a porta da aplicação
EXPOSE 8080

# Configurações de JVM otimizadas para Java 17
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:G1HeapRegionSize=16m -XX:+UseStringDeduplication --add-opens=java.base/java.nio=ALL-UNNAMED --add-opens=java.base/sun.nio.ch=ALL-UNNAMED"

# Comando para executar a aplicação
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]