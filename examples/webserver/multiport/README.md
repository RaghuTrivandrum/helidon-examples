# Helidon WebServer Multiple Port Example

It is common when deploying a microservice to run your service on
multiple ports so that you can control the visibility of your
service's endpoints. For example, you might want to use three ports:

- 8080: public REST endpoints of application
- 8081: private REST endpoints of application
- 8082: admin endpoints for health, metrics, etc.

This lets you expose only the public endpoints via your 
ingress controller or load balancer.

This example shows a Helidon application running on three ports
as described above.

The ports are configured in `application.yaml` by using named sockets.

Separate routing is defined for each named socket in `Main.java`

## Build and run

```shell
mvn package
java -jar target/helidon-examples-webserver-multiport.jar
```
## Exercise the application

```shell
curl -X GET http://localhost:8080/hello

curl -X GET http://localhost:8081/private/hello

curl -X GET http://localhost:8082/observe/health

curl -X GET http://localhost:8082/observe/metrics
```
