package ssv.com.agrocart;

public class Policies {
    private String title;
    private String Description;
    private String url;

    public Policies(String title, String description, String url) {
        this.title = title;
        Description = description;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return Description;
    }

    public String getUrl() {
        return url;
    }
}
