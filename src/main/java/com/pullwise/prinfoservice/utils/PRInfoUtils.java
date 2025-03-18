package com.pullwise.prinfoservice.utils;

import org.springframework.stereotype.Service;

@Service
public class PRInfoUtils {

    public boolean isStringNullOrEmpty(String text){
        return text != null && !text.isEmpty();
    }

}
