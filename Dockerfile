FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw

RUN ./mvnw -DskipTests -Dcheckstyle.skip=true dependency:go-offline

COPY src ./src

RUN ./mvnw -DskipTests -Dcheckstyle.skip=true clean package

EXPOSE 8080

CMD ["sh", "-c", "java -jar target/*.jar"]