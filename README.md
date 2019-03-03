# Template Service implementation

## Build
mvn clean install

## Run
mvn spring-boot:run

## Usage
Either use curl, or hit the local endpoint Swagger harness http://localhost:8080/swagger-ui.html

curl --header "Content-Type: application/json" --request POST http://localhost:8080/template

curl --header "Content-Type: application/json" --request GET http://localhost:8080/template/{id}

curl --header "Content-Type: application/json" --request PUT --data '{ "body": "Hello {{name}}", "channels": ["EMAIL","SMS"], "id": 1}' http://localhost:8080/template/{id}

curl --header "Content-Type: application/json" --request POST --data '{"data": {"name": "Chris"}, "destination": "email@address.com"}' http://localhost:8080/send/channel/{channel}/template/{id}
