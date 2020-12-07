package com.majesty.letswatch.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private String id;
    private String photo;
    private String name;
    private String description;
    private String release;
    private String runtime;

    protected Movie(Parcel in) {
        id = in.readString();
        photo = in.readString();
        name = in.readString();
        description = in.readString();
        release = in.readString();
        runtime = in.readString();
    }

    public Movie(){

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
    public String getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getRelease() {
        return release;
    }

    public String getRuntime() {
        return runtime;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String setPhoto(String photo) {
        this.photo = photo;
        return photo;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public void setRelease(String release) {
        this.release = release;
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
