package com.noza.zplitter.app.android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.noza.zplitter.app.android.exception.IncompleteModelException;

/**
 * Created by omiwrench on 2016-02-17.
 */

public class Profile implements Parcelable{

    private final String firstName;
    private final String lastName;
    private final Gender gender;
    private final int age;

    private Profile(Builder b){
        this.firstName = b.firstName;
        this.lastName = b.lastName;
        this.gender = b.gender;
        this.age = b.age;
    }
    public Profile(Parcel in){
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.gender = Gender.fromString(in.readString());
        this.age = in.readInt();
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public Gender getGender() {
        return gender;
    }
    public int getAge() {
        return age;
    }

    public static class Builder{
        private String firstName;
        private String lastName;
        private Gender gender;
        private int age = -1;

        public Builder firstName(String firstName){
            this.firstName = firstName;
            return this;
        }
        public Builder lastName(String lastName){
            this.lastName = lastName;
            return this;
        }
        public Builder gender(Gender gender){
            this.gender = gender;
            return this;
        }
        public Builder gender(String genderString){
            this.gender = gender.fromString(genderString);
            return this;
        }
        public Builder age(int age){
            this.age = age;
            return this;
        }

        public Profile build(){
            validate();
            return new Profile(this);
        }
        private void validate(){
            IncompleteModelException incompleteModelException = new IncompleteModelException(Profile.class.getName());
            if(this.firstName == null){
                incompleteModelException.addArgument("firstName");
            }
            if(this.lastName == null){
                incompleteModelException.addArgument("lastName");
            }
            if(this.gender == null){
                incompleteModelException.addArgument("gender");
            }
            if(this.age == -1){
                incompleteModelException.addArgument("age");
            }
            if(incompleteModelException.getNumArguments() > 0){
                throw incompleteModelException;
            }
        }
    }
    @Override
    public int describeContents(){
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(gender.getAsString());
        dest.writeInt(age);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>(){
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }
        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };
}
