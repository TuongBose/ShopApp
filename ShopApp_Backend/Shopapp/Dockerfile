#Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

#multi-staging
FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/Shopapp-0.0.1-SNAPSHOT.jar app.jar
COPY --from=build /app/uploads uploads

EXPOSE 8080
CMD ["java","-jar","app.jar"]

# docker build -t shopapp-spring:1.0.0 .
# docker login
# docker tag shopapp-spring:1.0.0 tuongbose/shopapp-spring:1.0.0
# docker push tuongbose/shopapp-spring:1.0.0