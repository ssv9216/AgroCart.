package ssv.com.agrocart;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public class MyPostsGetterSetter implements Parcelable {
    String weight;
    String name;
    String price;
    String uid;
    String mKey;
    String description;

    protected MyPostsGetterSetter(Parcel in) {
        weight = in.readString();
        name = in.readString();
        price = in.readString();
        uid = in.readString();
        mKey = in.readString();
        description = in.readString();
    }

    public static final Creator<MyPostsGetterSetter> CREATOR = new Creator<MyPostsGetterSetter>() {
        @Override
        public MyPostsGetterSetter createFromParcel(Parcel in) {
            return new MyPostsGetterSetter(in);
        }

        @Override
        public MyPostsGetterSetter[] newArray(int size) {
            return new MyPostsGetterSetter[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Exclude
    public String getmKey() {
        return mKey;
    }

    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public MyPostsGetterSetter() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(weight);
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(uid);
        parcel.writeString(description);
    }
}
