package com.pullwise.prinfoservice.resolver;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class PRInfoServiceResolver {

    @Value("${appId}")
    private String appId;

}
