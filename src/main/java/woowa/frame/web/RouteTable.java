package woowa.frame.web;

import jakarta.servlet.http.HttpServletRequest;
import woowa.frame.core.annotation.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RouteTable {

    private List<RouteTableRow> routeTable = new ArrayList<>();

    public void addRouteTableRow(RouteTableRow... row) {
        for (RouteTableRow target : row) {
            routeTable.add(target);
        }
    }

    public Optional<RouteTableRow> findTable(HttpServletRequest request) {
        return routeTable.stream()
                .filter(row -> row.isMatch(request))
                .findAny();
    }
}
