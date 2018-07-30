package com.noza.zplitter.app.android.helper;

/**
 * Created by omiwrench on 2016-02-22.
 */
public class TextHelper {
    /***
     *
     * @param check integer to check, only works in range 0-100
     * @return true if integer starts with vowel, false if it does not
     */
    public static boolean startsWithVowel(int check){
        return (check == 8 || check == 11 || check == 18 || (check >= 80 && check <= 89));
    }
}
