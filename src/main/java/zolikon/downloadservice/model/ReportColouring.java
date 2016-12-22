package zolikon.downloadservice.model;

public enum ReportColouring {
    RED("\u001B[31m","%"),
    GREEN("\u001B[32m","ß"),
    YELLOW("\u001B[33m","÷"),
    BLUE("\u001B[34m","×"),
    WHITE("\u001B[37m","|"),
    RESET("\u001B[0m","$");

    private String colourCode;
    private String placeHolder;

    ReportColouring(String colourCode, String placeHolder) {
        this.colourCode = colourCode;
        this.placeHolder = placeHolder;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public String getColourCode() {
        return colourCode;
    }

}
