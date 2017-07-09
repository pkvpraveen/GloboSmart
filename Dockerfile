FROM openjdk:8
ADD target/product-catalog.jar product-catalog.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "product-catalog.jar"]
