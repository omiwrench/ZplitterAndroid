package com.noza.zplitter.app.android.model;

/**
 * Created by omiwrench on 2016-02-17.
 */
public enum Gender{
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    private String genderString;
    Gender(String string){
        this.genderString = string;
    }
    public String getAsString(){
        return genderString;
    }

    public static Gender fromString(String genderString) throws IllegalArgumentException{
        if(genderString == null){
            return null;
        }
        if(genderString.equalsIgnoreCase("male")){
            return Gender.MALE;
        }
        else if(genderString.equalsIgnoreCase("female")){
            return Gender.FEMALE;
        }
        else if(genderString.equalsIgnoreCase("other")){
            return Gender.OTHER;
        }
        else{
            throw new IllegalArgumentException("No gender found for: " + genderString);
        }
    }
}
