package ssv.com.agrocart;

public class PesticideSample {
    private String title;
    private String desc;
    private String price;
    private String rating;
    private int thumbnail;

    public PesticideSample(String title, String desc, String price, String rating, int thumbnail) {
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.rating = rating;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public int setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
        return thumbnail;
    }
}
