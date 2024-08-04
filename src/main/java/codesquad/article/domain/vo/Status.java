package codesquad.article.domain.vo;

public enum Status {
    PUBLISHED, DELETED;

    public static Status of(String status) {
        for (Status value : Status.values()) {
            if (value.name().equalsIgnoreCase(status)) {
                return value;
            }
        }
        return null;
    }
}
