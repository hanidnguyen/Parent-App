package ca.cmpt276.iteration1.model;

/*
    Child class:
    -   Stores name of child to be added to a children manager in main menu.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class Child implements Parcelable {
    private String name;
    public Child(String name) {
        this.name = name;
    }

    protected Child(Parcel in) {
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Child> CREATOR = new Creator<Child>() {
        @Override
        public Child createFromParcel(Parcel in) {
            return new Child(in);
        }

        @Override
        public Child[] newArray(int size) {
            return new Child[size];
        }
    };

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
