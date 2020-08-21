package ssv.com.agrocart;

// News Item for News Fragment
public class NewsItem {
    private String mImageUrl;
    private String mTitle;
    private String mDesc;
    private String mUrl;

    public NewsItem(String imageUrl, String creator, String likes, String url) {
        mImageUrl = imageUrl;
        mTitle = creator;
        mDesc = likes;
        mUrl = url;

    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDesc() {
        return mDesc;
    }

    public String getUrl(){ return mUrl;}

}

