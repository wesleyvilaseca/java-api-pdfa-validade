FROM openjdk

WORKDIR /app

COPY target/pdfa-rest-api-validate-0.0.1-SNAPSHOT.jar /app/pdfa-rest-api-validate.jar

ENTRYPOINT ["java", "-jar", "pdfa-rest-api-validate.jar"]