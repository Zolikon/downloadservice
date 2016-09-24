package zolikon.downloadservice.model;


public class AdditionalInformation {

    private String saveFolder;
    private boolean absolutePath;

    public AdditionalInformation(String saveFolder, boolean absolutePath) {
        this.saveFolder = saveFolder;
        this.absolutePath = absolutePath;
    }

    public AdditionalInformation(String saveFolder) {
        this(saveFolder,false);
    }

    public String getSaveFolder() {
        return saveFolder;
    }

    public boolean isAbsolutePath() {
        return absolutePath;
    }
}
