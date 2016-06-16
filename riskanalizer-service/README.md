#Decision Service Spring Boot
#Prerequisites: Java 8, Gradle '2.3'
#Instructions:
1. gradle clean build run
2. curl -H "Content-Type: application/json" -d '{"email":"a@b.com","first_name":"a","last_name":"b","amount":"100"}' http://localhost:8080/decision