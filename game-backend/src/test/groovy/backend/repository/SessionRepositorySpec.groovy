package backend.repository

import backend.entity.Session
import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.ConcurrentHashMap

import static backend.entity.Session.newBuilder
import static java.time.LocalDateTime.now
import static java.util.UUID.randomUUID

public class SessionRepositorySpec extends Specification {
    @Unroll
    def """When session was #createdString and validity is #validityThreshold if a session lookup is made #elapsedTime after creation,
           session should be #foundString"""(){
        setup:
        SessionRepository sessionRepository = new SessionRepository(new ConcurrentHashMap<String, Session>()
                ,validityThreshold,elapsedTime)
        String sessionKey = randomUUID().toString()
        when:
        Session original = (created)?sessionRepository.create(sessionKey, newBuilder().withCreated(now()).withUserId(1).build()):null
        sleep(elapsedTime)
        Optional<Session> lookup = sessionRepository.findBy(sessionKey)
        then:
        lookup.isPresent() == found
        lookup.ifPresent(){ session -> original == session }
        where:
        created |validityThreshold   |elapsedTime    |found
        true    |200                 |50             |true
        true    |200                 |200            |false
        true    |200                 |300            |false
        false   |200                 |1              |false
        foundString = found ? 'found' : 'not found'
        createdString = created ? 'created' : 'not created'
    }
}