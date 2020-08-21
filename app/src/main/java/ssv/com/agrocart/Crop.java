package ssv.com.agrocart;

import android.os.Parcel;
import android.os.Parcelable;

public class Crop implements Parcelable {
    String weight;
    String name;
    String price;
    String uid;
    String description;

    public Crop() {
    }

    public Crop(String weight, String name, String price, String uid, String description) {
        this.weight = weight;
        this.name = name;
        this.price = price;
        this.uid = uid;
        this.description = description;
    }

    //parcel
    protected Crop(Parcel in) {
        weight = in.readString();
        name = in.readString();
        price = in.readString();
        uid = in.readString();
        description = in.readString();
    }

    public static final Creator<Crop> CREATOR = new Creator<Crop>() {
        @Override
        public Crop createFromParcel(Parcel in) {
            return new Crop(in);
        }

        @Override
        public Crop[] newArray(int size) {
            return new Crop[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    //parcel
    @Override
    public int describeContents() {
        return 0;
    }

    //parcel
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(weight);
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(uid);
        parcel.writeString(description);
    }
}
