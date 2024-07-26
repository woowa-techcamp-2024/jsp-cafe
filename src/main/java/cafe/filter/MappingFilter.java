package cafe.filter;

import jakarta.servlet.Filter;

import java.util.List;

public interface MappingFilter extends Filter {

    List<String> mappings();
}
