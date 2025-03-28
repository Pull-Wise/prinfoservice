package com.pullwise.prinfoservice.controllers;

import com.pullwise.prinfoservice.serviceImpl.PRApiServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Mownesh
 */
@Slf4j
@RestController
@RequestMapping("/github-api/v1/")
public class GithubApiController {

    @Autowired
    PRApiServiceImpl prApiService;

    @GetMapping("pr-list")
    public void getPrList(
        @RequestParam("installationId")
        Long installationId
    )
    {
        this.prApiService.getListOfPRsForInstallaionId(installationId);
    }

}
