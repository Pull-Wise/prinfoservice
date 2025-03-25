package com.pullwise.prinfoservice.utils;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PRInfoUtils {

    public boolean isStringNullOrEmpty(String text){
        return text != null && !text.isEmpty();
    }
    public static boolean compareStringEquality(String text1 , String text2){return (text1 != null && text2 != null) && Objects.equals(text1, text2);}

}
