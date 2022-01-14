```
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

```
docker build --build-arg JAR_FILE=target/*.jar -t myorg/myapp .
```

```
FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

```
docker build -t myorg/myapp .
docker run -p 8080:8080 myorg/myapp
docker run -ti --entrypoint /bin/sh myorg/myapp
docker run --name myapp -ti --entrypoint /bin/sh myorg/myapp
docker exec -ti myapp /bin/sh
```

```
FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY run.sh .
COPY target/*.jar app.jar
ENTRYPOINT ["run.sh"]
```

`run.sh`
```
#!/bin/sh
exec java -jar /app.jar
```

```
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","${JAVA_OPTS}","-jar","/app.jar"]
```

```
docker build -t myorg/myapp .
docker run -p 9000:9000 -e JAVA_OPTS=-Dserver.port=9000 myorg/myapp
```

```
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]
```

```
docker run -p 8080:8080 -e "JAVA_OPTS=-Ddebug -Xmx128m" myorg/myapp
```

```
docker run -p 9000:9000 myorg/myapp --server.port=9000
```

```
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${0} ${@}"]
```

```
#!/bin/sh
exec java ${JAVA_OPTS} -jar /app.jar ${@}
```

```
mkdir target/dependency
(cd target/dependency; jar -xf ../*.jar)
docker build -t myorg/myapp .
```

`Dockerfile`
```
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","hello.Application"]
```

```
mkdir target/extracted
java -Djarmode=layertools -jar target/*.jar extract --destination target/extracted
docker build -t myorg/myapp .
```

`Dockerfile
```
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG EXTRACTED=/workspace/app/target/extracted
COPY ${EXTRACTED}/dependencies/ ./
COPY ${EXTRACTED}/spring-boot-loader/ ./
COPY ${EXTRACTED}/snapshot-dependencies/ ./
COPY ${EXTRACTED}/application/ ./
ENTRYPOINT ["java","org.springframework.boot.loader.JarLauncher"]
```

`Multi-Stage Build`
```
FROM openjdk:8-jdk-alpine as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","hello.Application"]
```