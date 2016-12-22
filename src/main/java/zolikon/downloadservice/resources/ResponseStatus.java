package zolikon.downloadservice.resources;

public enum ResponseStatus {
    OK(200),
    MISSING_NAME(400),
    UNKNOWN_NAME(400),
    DUPLICATED_NAME(400);

    private int status;

    ResponseStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
