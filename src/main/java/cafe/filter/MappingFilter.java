package cafe.filter;

import jakarta.servlet.Filter;

import java.util.List;

public interface MappingFilter extends Filter {

    default List<String> mappings() {
        return List.of("/*");
    }
}
