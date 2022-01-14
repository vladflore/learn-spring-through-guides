```
curl https://start.spring.io/starter.tgz -d dependencies=webflux,actuator | tar -xzvf -
```

```
$ curl localhost:8080/actuator | jq .
```

```
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=vladflore/springguides_sb_k8s
docker push vladflore/springguides_sb_k8s
```

## Docker related commands

`docker images | awk '{print $3}' | tail -n +2`

`docker images | awk '{print $3}' | tail -n +2 | xargs docker image rm -f`

`docker container ls -a | awk '{print $1}' | tail -n +2 | xargs docker container rm`

`docker login`

`docker tag <image_id> <tag>`

`docker push <image_name>[:tag]`
