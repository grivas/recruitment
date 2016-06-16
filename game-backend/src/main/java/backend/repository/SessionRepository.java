package backend.repository;

import backend.entity.Session;

import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentMap;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class SessionRepository {
    private final Map<String, Session> sessionStore;
    private final long validityThreshold;

    /**
     * Set up session repository with a session cleaner to remove old sessions.
     * @param sessionStore to store sessions
     * @param validityThreshold in milliseconds. Specifies for how long a session can be consider valid.
     * @param cleanupFrequency in millisecends. Sets the frequency in which old sessions will be cleaned up.
     */
    public SessionRepository(ConcurrentMap<String, Session> sessionStore, long validityThreshold, long cleanupFrequency) {
        this.sessionStore = sessionStore;
        this.validityThreshold = validityThreshold;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                removeOldSessions();
            }
        }, this.validityThreshold+cleanupFrequency,cleanupFrequency);
    }

    /**
     * Adds a new session with the specified key.
     * @param key of the session
     * @param session object
     * @return The value of the new session.
     */
    public Session create(String key, Session session){
        return sessionStore.put(key, session);
    }

    /**
     * Retries and optional that might contain a session.
     * @param key of the session
     * @return Optional containing the session associated to the specified key or empty if not session is found
     */
    public Optional<Session> findBy(String key){
        if (sessionStore.containsKey(key)){
            Session session = sessionStore.get(key);
            return session.isValid(validityThreshold)?of(session):empty();
        }
        return empty();
    }

    /**
     * Iterates over all keys to identify and remove old sessions.
     */
    public void removeOldSessions(){
        for (String key : sessionStore.keySet()) {
            if(!sessionStore.get(key).isValid(validityThreshold)){
                sessionStore.remove(key);
            }
        }
    }
}
