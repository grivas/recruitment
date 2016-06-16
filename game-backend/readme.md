#Execution
java -jar build/libs/game-backend-1.0-SNAPSHOT.jar
The server runs on port 8081 by default, but it can be overridden -Dport=xxxx

#Test
Unit test can be ran using ./gradlew clean test

#Considerations
## Http server
The server uses regular expressions and request method to route requests to specific request controllers, which must 
implement backend.server.Controller interface.
## Concurrency:
The scores are saved in a map indexed by level. The list of scores is saved as a list sorted in descending order by score,
and with one and only one score by user. During score addition, the threads are synchronized based on score level, 
the list of high scores is converted into a map to find if the user score should be updated or added and then reconverted
to list and trimmed if needed. The overall complexity of add score operations is O(n), being n limited to 15. 
On the otherhand retrieving the list of highscores for a level is constant.
 
