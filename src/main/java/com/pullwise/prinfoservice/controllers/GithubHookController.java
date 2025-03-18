/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pullwise.prinfoservice.controllers;

import com.pullwise.prinfoservice.dto.GitHubWebhookPayload;
import com.pullwise.prinfoservice.serviceImpl.PRApiServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Mownesh
 */
@Slf4j
@RestController
@RequestMapping("/github-webhook")
public class GithubHookController {
    
    @Autowired
    PRApiServiceImpl prAPIServiceImpl;

    private static final Set<String> VALID_ACTIONS = Set.of("opened", "reopened");
    
    @PostMapping
    public void handleGithubWebhook(@RequestBody GitHubWebhookPayload payload) {
        if(Objects.nonNull(payload.action) && VALID_ACTIONS.contains(payload.action)){
            prAPIServiceImpl.processGithubWebHook(payload);
        }else{
            log.error("Github Action is not supported -> {} ",payload);
        }
    }

    @GetMapping("/hello")
    public ResponseEntity<String> getHello(){
        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }
    
}

