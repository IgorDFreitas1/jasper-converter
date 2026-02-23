# 1. Estágio de Build (Compila o projeto)
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. Estágio de Execução (Roda o projeto de forma leve)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia o JAR compilado
COPY --from=build /app/target/*.jar app.jar

# Define a porta que o Render vai ler
ENV PORT=8080
EXPOSE 8080

# Comando mágico: Limita o Java a 350MB de RAM (-Xmx350m) para não travar o Render
ENTRYPOINT ["java", "-Xmx350m", "-jar", "app.jar"]