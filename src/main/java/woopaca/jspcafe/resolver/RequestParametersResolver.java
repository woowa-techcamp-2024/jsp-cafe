package woopaca.jspcafe.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestParametersResolver {

    private static final Logger log = LoggerFactory.getLogger(RequestParametersResolver.class);

    public static <T> T resolve(HttpServletRequest request, Class<T> targetRecord) {
        try {
            RecordComponent[] recordComponents = targetRecord.getRecordComponents();
            List<Object> arguments = new ArrayList<>();
            for (RecordComponent recordComponent : recordComponents) {
                Object argument = findArgument(recordComponent, request);
                arguments.add(argument);
            }

            Constructor<T> constructor = targetRecord.getDeclaredConstructor(
                    Arrays.stream(recordComponents)
                            .map(RecordComponent::getType)
                            .toArray(Class[]::new)
            );
            return constructor.newInstance(arguments.toArray());
        } catch (ReflectiveOperationException e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException("[ERROR] 파라미터 resolving 중 오류가 발생했습니다.");
        }
    }

    private static Object findArgument(RecordComponent recordComponent, HttpServletRequest request) {
        String componentName = recordComponent.getName();
        Class<?> componentType = recordComponent.getType();

        String value = request.getParameter(componentName);
        if (value == null) {
            throw new IllegalArgumentException("[ERROR] 파라미터에 `" + componentName + "` 값이 없습니다.");
        }
        return convert(value, componentType);
    }

    private static Object convert(String value, Class<?> type) {
        if (type.isAssignableFrom(String.class)) {
            return value;
        }
        if (type.isAssignableFrom(Integer.class)) {
            return Integer.parseInt(value);
        }
        if (type.isAssignableFrom(Long.class)) {
            return Long.parseLong(value);
        }
        if (type.isAssignableFrom(Double.class)) {
            return Double.parseDouble(value);
        }
        if (type.isAssignableFrom(Boolean.class)) {
            return Boolean.parseBoolean(value);
        }
        return null;
    }
}
