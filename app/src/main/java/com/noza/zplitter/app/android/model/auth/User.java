package com.noza.zplitter.app.android.model.auth;

import android.os.Parcel;
import android.os.Parcelable;

import com.noza.zplitter.app.android.model.Gender;
import com.noza.zplitter.app.android.model.Profile;

/**
 * Created by omiwrench on 2016-01-30.
 */
public class User implements Parcelable{
    private final int id;
    private final String email;
    private Profile profile;

    private User(Builder b){
        this.id = b.id;
        this.email = b.email;
        this.profile = b.profile;
    }
    protected User(Parcel in){
        id = in.readInt();
        email = in.readString();
        profile = (Profile) in.readValue(Profile.class.getClassLoader());
    }

    public int getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getFirstName(){
        if(profile != null) return profile.getFirstName();
        return null;
    }
    public String getLastName(){
        if(profile != null) return profile.getLastName();
        return null;
    }
    public String getFullName(){
        if(profile != null) return profile.getFirstName() + " " + profile.getLastName();
        return null;
    }
    public Gender getGender(){
        if(profile != null) return profile.getGender();
        return null;
    }
    public String getGenderAsString(){
        if(profile != null) return profile.getGender().getAsString();
        return null;
    }
    public int getAge(){
        if(profile != null) return profile.getAge();
        return -1;
    }

    public boolean hasProfile(){ return profile != null;}
    public void setProfile(Profile profile){
        this.profile = profile;
    }

    public static class Builder{
        private int id;
        private String email;
        private Profile profile;

        public Builder id(int id){
            this.id = id;
            return this;
        }
        public Builder email(String email){
            this.email = email;
            return this;
        }
        public Builder profile(Profile profile){
            this.profile = profile;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(email);
        dest.writeValue(profile);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
