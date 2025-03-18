/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pullwise.prinfoservice.serviceImpl;

/**
 *
 * @author Mownesh
 */
import com.pullwise.prinfoservice.constants.PRInfoServiceConstant;
import com.pullwise.prinfoservice.dto.GitHubWebhookPayload;
import com.pullwise.prinfoservice.requests.GithubAccessTokenRequest;
import com.pullwise.prinfoservice.resolver.PRInfoServiceResolver;
import com.pullwise.prinfoservice.response.GithubAccessTokenResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.FileReader;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class GithubAuthServiceImpl {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PRInfoServiceResolver prInfoServiceResolver;

    private static final long EXPIRATION_TIME_MS = 10 * 60 * 1000;  // 10 minutes

    public static String generateGitHubJWT(String appId, String keyPath) throws Exception {
        PrivateKey privateKey = getPrivateKey(keyPath);
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setIssuer(appId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRATION_TIME_MS))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

     private static PrivateKey getPrivateKey(String keyPath) throws Exception {
         try (PEMParser pemParser = new PEMParser(new FileReader(keyPath))) {
             Object object = pemParser.readObject();
             if (object instanceof PEMKeyPair) {
                 PEMKeyPair keyPair = (PEMKeyPair) object;
               return new JcaPEMKeyConverter().getKeyPair(keyPair).getPrivate();
             }
             else {
                throw new IllegalArgumentException("Invalid private key format");
            }
         }
     }

    private String getGitHubAPIKey(String instalationId, String repoId) throws Exception{

        try{
            String apiUrl = String.format("https://api.github.com/app/installations/%s/access_tokens", instalationId);
            GithubAccessTokenRequest accessTokenRequest = new GithubAccessTokenRequest();
            accessTokenRequest.setRepos(List.of(repoId));
            HttpHeaders headers = new HttpHeaders();

            String jwt = GithubAuthServiceImpl.generateGitHubJWT(prInfoServiceResolver.getAppId(), prInfoServiceResolver.getPrivateKeyPath());
            headers.setBearerAuth(jwt);

            HttpEntity<GithubAccessTokenRequest> entity = new HttpEntity<>(accessTokenRequest,headers);
            ResponseEntity<GithubAccessTokenResponse> accessTokenResponse = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, GithubAccessTokenResponse.class);

            if(accessTokenResponse.getStatusCode() == HttpStatus.CREATED){
                GithubAccessTokenResponse accessToken = accessTokenResponse.getBody();
                assert accessToken != null;
                return accessToken.getAccessToken();
            }else{
                log.error("GitHub API Error: Status Code = {}, Response Body = {}", accessTokenResponse.getStatusCode(), accessTokenResponse.getBody());
                throw new Exception("GitHub API returned an error: " + accessTokenResponse.getStatusCode());
            }
        } catch (Exception e){
            log.error("Exception occurred at PRApiServiceImpl :: getGitHubAPIKey ->  {}", e.getMessage());
            throw e;
        }
    }

    public String getAccessToken(String instalationId, String repoId){
        try{
            return this.getGitHubAPIKey(instalationId,repoId);
        }catch(Exception e){
            log.error("Exception occured in PRApiServiceImpl :: getAccessToken -> " + e.getMessage());
            return "";
        }
    }

    public Integer getInstallationId(GitHubWebhookPayload payload) throws Exception {
        String jwtToken = GithubAuthServiceImpl.generateGitHubJWT(prInfoServiceResolver.getAppId(),prInfoServiceResolver.getPrivateKeyPath());
        return this.fetchInstallationIdFromGithub(payload,jwtToken);
    }

    private Integer fetchInstallationIdFromGithub(GitHubWebhookPayload payload, String appToken) throws RuntimeException {

        String url = String.format(
                PRInfoServiceConstant.GITHUB_INSTALLATION_ID_URL,
                payload.getRepository().getOwner().getLogin(),
                payload.getRepository().getName()
        );

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github+json");
        headers.setBearerAuth(appToken);
        headers.set("X-GitHub-Api-Version", "2022-11-28");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonResponse = new JSONObject(response.getBody());
            return jsonResponse.getInt("id");
        } else {
            throw new RuntimeException("Failed to fetch installation ID: " + response.getStatusCode());
        }
    }
}
