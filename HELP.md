# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.2/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.2/gradle-plugin/reference/html/#build-image)
* [Spring Integration AMQP Module Reference Guide](https://docs.spring.io/spring-integration/reference/html/amqp.html)
* [Spring Integration JPA Module Reference Guide](https://docs.spring.io/spring-integration/reference/html/jpa.html)
* [Spring Integration Test Module Reference Guide](https://docs.spring.io/spring-integration/reference/html/testing.html)
* [Spring Integration HTTP Module Reference Guide](https://docs.spring.io/spring-integration/reference/html/http.html)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#web)
* [Thymeleaf](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#web.servlet.spring-mvc.template-engines)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#using.devtools)
* [Docker Compose Support](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#features.docker-compose)
* [Spring for RabbitMQ](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#messaging.amqp)
* [Spring Integration](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#messaging.spring-integration)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Spring Batch](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#howto.batch)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)
* [Messaging with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq/)
* [Integrating Data](https://spring.io/guides/gs/integration/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Creating a Batch Service](https://spring.io/guides/gs/batch-processing/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

### Docker Compose support

This project contains a Docker Compose file named `compose.yaml`.
In this file, the following services have been defined:

* postgres: [`postgres:latest`](https://hub.docker.com/_/postgres)
* rabbitmq: [`rabbitmq:latest`](https://hub.docker.com/_/rabbitmq)

Please review the tags of the used images and set them to the same as you're running in production.

