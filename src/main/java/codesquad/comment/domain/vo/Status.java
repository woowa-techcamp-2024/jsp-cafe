package codesquad.comment.domain.vo;

public enum Status {
    COMMENTED, DELETED;

    public static Status of(String status) {
        for (Status value : Status.values()) {
            if (value.name().equalsIgnoreCase(status)) {
                return value;
            }
        }
        return null;
    }
}
