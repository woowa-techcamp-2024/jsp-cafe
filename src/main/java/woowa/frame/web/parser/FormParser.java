package woowa.frame.web.parser;

import woowa.frame.core.annotation.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FormParser {

    public Map<String, String> parse(String form) {
        HashMap<String, String> map = new HashMap<>();

        String[] split = form.split("&");
        for (String keyValue : split) {
            String[] split1 = keyValue.split("=");
            if (split1.length == 1) {
                map.put(split1[0], null);
                continue;
            }
            map.put(split1[0], split1[1]);
        }

        return map;
    }
}
