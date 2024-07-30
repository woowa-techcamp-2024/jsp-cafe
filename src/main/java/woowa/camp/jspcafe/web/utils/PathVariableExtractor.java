package woowa.camp.jspcafe.web.utils;

import java.util.HashMap;
import java.util.Map;

public class PathVariableExtractor {

    public static Map<String, String> extractPathVariables(String urlPattern, String actualPath) {
        Map<String, String> pathVariables = new HashMap<>();
        String[] patternParts = urlPattern.split("/");
        String[] actualParts = actualPath.split("/");

        if (patternParts.length != actualParts.length) {
            return pathVariables;
        }

        for (int idx = 0; idx < patternParts.length; idx++) {
            if (!isPathVariablePattern(patternParts, idx)) {
                continue;
            }
            String keyCandidate = patternParts[idx].substring(1, patternParts[idx].length() - 1);

            if (keyCandidate.isEmpty() || keyCandidate.contains(" ")) {
                continue;
            }

            pathVariables.put(keyCandidate, actualParts[idx]);
        }

        return pathVariables;
    }

    private static boolean isPathVariablePattern(String[] patternParts, int idx) {
        return patternParts[idx].startsWith("{") && patternParts[idx].endsWith("}");
    }
}
