package backend.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

/**
 * Created by germanrivas on 5/5/16.
 */
public class ParamUtils {
    public static int asUnsignedInt(Map<String, String> params, String paramName) {
        return asUnsignedInt(params.get(paramName), paramName);
    }

    public static int asUnsignedInt(String value, String paramName) {
        try {
            return Integer.parseUnsignedInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Value of %s is not a number", paramName));
        }
    }

    public static String notNull(Map<String, String> params, String paramName) {
        String value = params.get(paramName);
        if (value != null) {
            return value;
        }
        throw new IllegalArgumentException(String.format("Value of %s must be specified", paramName));
    }

    public static Map<String, String> pathParamsOf(URI requestURI, Pattern pathPathern, List<String> pathParamNames) {
        Matcher matcher = pathPathern.matcher(requestURI.getPath());
        if (matcher.matches()) {
            return pathParamNames.stream().collect(toMap(parameterName -> parameterName, matcher::group));
        }
        throw new RuntimeException(String.format("Provided URI does not match: %s", pathPathern));
    }

    public static Map<String, String> queryParamsOf(URI requestURI) {
        if (requestURI.getQuery() == null) {
            return new HashMap<>();
        }
        return stream(requestURI.getQuery().split("&"))
                .map(pair -> pair.split("="))
                .collect(toMap(entry -> entry[0], entry -> entry[1]));
    }

    public static String asString(InputStream requestBody) throws RuntimeException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(requestBody))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read request's body", e);
        }
    }

}
