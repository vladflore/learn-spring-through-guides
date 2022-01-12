```shell
docker --version
docker build -t springio/demo .
docker images
docker run -p 8080:8080 springio/demo
docker image inspect springio/demo:latest 
docker image history springio/demo:latest 
docker ps -a
docker run -e "SPRING_PROFILES_ACTIVE=prod" -p 8080:8080 -t springio/demo
docker run -e "JAVA_TOOL_OPTIONS=-agentlib:jdwp=transport=dt_socket,address=5005,server=y,suspend=n" -p 8080:8080 -p 5005:5005 -t springio/demo
```

