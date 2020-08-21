package ssv.com.agrocart;

public class CropRatesSample {
    private String City;
    private String Variety;
    private String Arrivals;
    private String ModelPrice;
    private String MinMaxPrice;

    public CropRatesSample(String city, String variety, String arrivals, String modelPrice, String minMaxPrice) {
        City = city;
        Variety = variety;
        Arrivals = arrivals;
        ModelPrice = modelPrice;
        MinMaxPrice = minMaxPrice;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getVariety() {
        return Variety;
    }

    public void setVariety(String variety) {
        Variety = variety;
    }

    public String getArrivals() {
        return Arrivals;
    }

    public void setArrivals(String arrivals) {
        Arrivals = arrivals;
    }

    public String getModelPrice() {
        return ModelPrice;
    }

    public void setModelPrice(String modelPrice) {
        ModelPrice = modelPrice;
    }

    public String getMinMaxPrice() {
        return MinMaxPrice;
    }

    public void setMinMaxPrice(String minMaxPrice) {
        MinMaxPrice = minMaxPrice;
    }
}
