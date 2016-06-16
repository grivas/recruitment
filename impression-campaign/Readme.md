#Build
./gradlew clean fatjar

#Execution
java -jar build/libs/impression-campain-all-1.0-SNAPSHOT.jar server campaign.yml

#Test
Unit test can be ran using ./gradlew clean test