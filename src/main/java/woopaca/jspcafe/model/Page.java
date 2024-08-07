package woopaca.jspcafe.model;

import java.util.List;

public record Page<T>(List<T> data, int totalPage, int currentPage, int total) {
}
