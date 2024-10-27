# Docker Commands

Use Spring Boot Maven Plugin to build a Docker image:
```shell
./mvnw clean package spring-boot:build-image
```

Create Docker image skipping the tests:
```shell
./mvnw clean package spring-boot:build-image -DskipTests=true
```

Inspect the Docker image:
```shell
docker image inspect docker.io/library/spring6gateway:0.0.1-SNAPSHOT
```

Run the Docker image:
```shell
docker run spring6gateway:0.0.1-SNAPSHOT
```

Run the Docker image with port mapping:
```shell
docker run -p 8080:8080 spring6gateway:0.0.1-SNAPSHOT
```

Run the Docker image in background:
```shell
docker run -d -p 8080:8080 spring6gateway:0.0.1-SNAPSHOT
```

List running Docker containers:
```shell
docker ps
```

Stop a running Docker container:
```shell
docker stop <container-id>
```

Name and Run the Docker image in background:
```shell
docker run --name gateway -d -p 8080:8080 spring6gateway:0.0.1-SNAPSHOT
```

Stop a running Docker container by name:
```shell
docker stop gateway
```

Restart a stopped Docker container by name. This will restore using the previous settings.
```shell
docker start gateway
```

# Images
spring6gateway:0.0.1-SNAPSHOT
spring-6-auth-server:0.0.1-SNAPSHOT
restmvc:0.0.1-SNAPSHOT
spring-6-reactive:0.0.1-SNAPSHOT
reactive-mongo:0.0.1-SNAPSHOT











