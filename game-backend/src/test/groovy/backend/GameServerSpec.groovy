package backend;

import spock.lang.Specification
import spock.lang.Unroll;

public class GameServerSpec extends Specification {

    public static final String SERVER_URL = "http://localhost:8081"

    def setupSpec() {
        Main.main();
    }

    def "Scenario test"() {
        when:
        def sessionKeys = [4711, 5000, 9999].collectEntries() { userid ->
            def (code, body) = performRequest("GET", "http://localhost:8081/$userid/login", "")
            [userid, body]
        }

        [4711:[1500,1400], 5000:[1200,2100], 9999:[1700,1800]].each {
            userid,scores -> scores.each {
                score ->  performRequest("POST",SERVER_URL + "/2/score?sessionkey=${sessionKeys[userid]}",  String.valueOf(score))
            }
        }

        def (code, body) = performRequest("GET", "http://localhost:8081/2/highscorelist",  "")
        then:
        body == "5000=2100,9999=1800,4711=1500"
    }

    @Unroll
    def "Error cases: when #method:#requestPath with body '#requestBody', response code should be #responseCode"() {
        when:
        def (code, responseBody) = performRequest(method, SERVER_URL +requestPath, requestBody)
        then:
        code == expectedCode
        where:
        method | requestPath                      | requestBody | expectedCode
        "GET"  | "/something"                     | ""          | 404
        "GET"  | "/ghhh/login"                    | ""          | 400
        "GET"  | "/-2/login"                      | ""          | 400
        "POST" | "/555/score?sessionkey=kjdksjd"  | "444"       | 403
        "POST" | "/gfgf/score?sessionkey=kjdksjd" | "444"       | 400
        "POST" | "/-2/score?sessionkey=kjdksjd"   | "444"       | 400
        "POST" | "/-2/score?sessionkey=kjdksjd"   | "erer"      | 400
        "POST" | "/-2/score?sessionkey=kjdksjd"   | "-100"      | 400
        "POST" | "/-2/score"                      | "-100"      | 400
        "GET"  | "/ghhh/highscorelist"            | ""          | 400
        "GET"  | "/-1/highscorelist"              | ""          | 400
    }

    def performRequest(String method, String requestUrl, String requestBody) {
        def url = new URL(requestUrl)
        def connection = url.openConnection()
        connection.doOutput = true
        connection.requestMethod = method
        if (requestBody) {
            def writer = new OutputStreamWriter(connection.outputStream)
            writer.write(requestBody)
            writer.close()
        }
        connection.connect()
        def code = connection.responseCode
        def body;
        if (code == 200) {
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(connection.getInputStream()));
            body = reader.readLine()
        }
        [code, body]
    }
}
