package com.majesty.letswatch.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Favorite implements Parcelable {
    private String id;
    private String photo;
    private String name;
    private String description;
    private String release;
    private String runtime;

    protected Favorite(Parcel in) {
        id = in.readString();
        photo = in.readString();
        name = in.readString();
        description = in.readString();
        release = in.readString();
        runtime = in.readString();
    }

    public Favorite(){

    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }
        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    //setter and getter hasil generate
    public String getId(String id) {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPhoto(String photo) {
        return photo;
    }
    public String setPhoto(String photo) {
        this.photo = photo;
        return photo;
    }
    public String getName(String name) {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription(String description) {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getRelease(String release) {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getRuntime(String runtime) {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(photo);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(release);
        dest.writeString(runtime);
    }
}
