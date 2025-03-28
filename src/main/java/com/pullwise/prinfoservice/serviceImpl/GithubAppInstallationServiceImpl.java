package com.pullwise.prinfoservice.serviceImpl;

import com.pullwise.prinfoservice.constants.PRInfoServiceConstant;
import com.pullwise.prinfoservice.dto.Repository;
import com.pullwise.prinfoservice.entity.InstallationData;
import com.pullwise.prinfoservice.entity.InstallationHistory;
import com.pullwise.prinfoservice.entity.RepositoryData;
import com.pullwise.prinfoservice.repository.InstallationHistoryRepository;
import com.pullwise.prinfoservice.repository.InstallationRepository;
import com.pullwise.prinfoservice.repository.RepositoryDataRepository;
import com.pullwise.prinfoservice.requests.GitHubInstallationRequest;
import com.pullwise.prinfoservice.requests.GithubRepoChangeRequest;
import com.pullwise.prinfoservice.utils.PRInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class GithubAppInstallationServiceImpl {

    @Autowired
    InstallationRepository installationRepository;
    @Autowired
    InstallationHistoryRepository installationHistoryRepository;
    @Autowired
    RepositoryDataRepository repoRepository;

    public void processGithubInstallationHook(GitHubInstallationRequest payload){
        try{
            if(PRInfoUtils.compareStringEquality(payload.getAction(), PRInfoServiceConstant.GITHUB_INSTALLATION_ACTION_CREATED)){
                InstallationData newInstallation =
                        InstallationData.builder()
                            .installationId(payload.getInstallation().getId())
                            .activeFlag(true)
                            .createdDate(LocalDateTime.now())
                            .createdBy(payload.getSender().getLogin())
                            .lastUpdatedDate(LocalDateTime.now())
                            .lastUpdatedBy(payload.getSender().getLogin())
                            .build();
                InstallationData installation = installationRepository.save(newInstallation);
                this.insertInstallationHistoryData(payload,installation);
                this.insertRepositoryData(payload,installation,PRInfoServiceConstant.ACTIVE_F_TRUE);
            }
            if(PRInfoUtils.compareStringEquality(payload.getAction(), PRInfoServiceConstant.GITHUB_INSTALLATION_ACTION_DELETED)){
                InstallationData updateInstallation = installationRepository.findByInstallationId(payload.getInstallation().getId());
                Optional.ofNullable(updateInstallation)
                        .ifPresentOrElse(
                            (installation) -> {
                                installation.setActiveFlag(false);
                                installation.setLastUpdatedDate(LocalDateTime.now());
                                installation.setLastUpdatedBy(payload.getSender().getLogin());
                                InstallationData installationData = installationRepository.save(installation);
                                this.insertInstallationHistoryData(payload, installationData);
                                this.insertRepositoryData(payload,installationData, PRInfoServiceConstant.ACTIVE_F_FALSE);
                            },
                            () -> {
                                log.error("Installation not found -> {}", payload);
                                InstallationData newInstallation =
                                        InstallationData.builder()
                                                .installationId(payload.getInstallation().getId())
                                                .activeFlag(true)
                                                .createdDate(LocalDateTime.now())
                                                .createdBy(payload.getSender().getLogin())
                                                .lastUpdatedDate(LocalDateTime.now())
                                                .lastUpdatedBy(payload.getSender().getLogin())
                                                .build();
                                InstallationData installationData = installationRepository.save(newInstallation);
                                this.insertInstallationHistoryData(payload, installationData);
                                this.insertRepositoryData(payload,installationData, PRInfoServiceConstant.ACTIVE_F_FALSE);
                            }
                        );
            }
        }catch (Exception e){
            log.error("Error in GithubAppInstallationServiceImpl :: processGithubInstallationHook with message -> {} and payload {}",e.getMessage(),payload);
        }
    }

    private void insertInstallationHistoryData(GitHubInstallationRequest payload, InstallationData installation){
        try {
            InstallationHistory history = InstallationHistory.builder()
                    .installation(installation)
                    .action(payload.getAction())
                    .repoCount(payload.getRepositories().size())
                    .lastUpdatedBy(payload.getSender().getLogin())
                    .lastUpdatedDate(LocalDateTime.now())
                    .createdBy(payload.getSender().getLogin())
                    .createdDate(LocalDateTime.now())
                    .build();
            this.installationHistoryRepository.save(history);
        }catch (Exception e){
            log.error("Error occurred in GithubAppInstallationServiceImpl ::" +
                    "insertInstallationHistoryData with message -> {} , payload {}",e.getMessage(),payload);
        }
    }

    private void insertRepositoryData(GitHubInstallationRequest payload, InstallationData installationData, boolean activeFlag){
        try{
            List<RepositoryData> repositories = payload.getRepositories()
                    .stream()
                    .map((repo) -> {
                        RepositoryData repository = repoRepository.findByRepositoryId(repo.getId());
                        return RepositoryData.builder()
                                .id(Optional.ofNullable(repository).map(RepositoryData::getId).orElse(null))
                                .repositoryId(repo.getId())
                                .repositoryName(repo.getName())
                                .repositoryFullname(repo.getFullName())
                                .installation(installationData)
                                .privateFlag(repo.isPrivate())
                                .activeFlag(activeFlag)
                                .createdDate(Optional.ofNullable(repository).map(RepositoryData::getCreatedDate).orElse(LocalDateTime.now()))
                                .createdBy(Optional.ofNullable(repository).map(RepositoryData::getCreatedBy).orElse(payload.getSender().getLogin()))
                                .lastUpdatedDate(LocalDateTime.now())
                                .lastUpdatedBy(payload.getSender().getLogin())
                                .build();
                        }
                    )
                    .toList();
            this.repoRepository.saveAll(repositories);
        }catch (Exception e){
            log.error("Error occurred in GithubAppInstallationServiceImpl :: insertRepositoryData," +
                    "with error message {}, and payload {}", e.getMessage(),payload);
        }
    }

    public void updateRepoChange(List<Repository> repos, String updatedBy,InstallationData installationData, boolean activeFlag){
        try{
            List<RepositoryData> repositories = repos
                    .stream()
                    .map((repo) -> {
                                RepositoryData repository = repoRepository.findByRepositoryId(repo.getId());
                                return RepositoryData.builder()
                                        .id(Optional.ofNullable(repository).map(RepositoryData::getId).orElse(null))
                                        .repositoryId(repo.getId())
                                        .repositoryName(repo.getName())
                                        .repositoryFullname(repo.getFullName())
                                        .installation(installationData)
                                        .privateFlag(repo.isPrivate())
                                        .activeFlag(activeFlag)
                                        .createdDate(Optional.ofNullable(repository).map(RepositoryData::getCreatedDate).orElse(LocalDateTime.now()))
                                        .createdBy(Optional.ofNullable(repository).map(RepositoryData::getCreatedBy).orElse(updatedBy))
                                        .lastUpdatedDate(LocalDateTime.now())
                                        .lastUpdatedBy(updatedBy)
                                        .build();
                            }
                    )
                    .toList();
            this.repoRepository.saveAll(repositories);
        }catch (Exception e){
            log.error("Error occurred in GithubAppInstallationServiceImpl :: updateRepoChange," +
                    "with error message {}, and payload {}", e.getMessage(),repos);
        }
    }

    public void processRepoChangeRequest(GithubRepoChangeRequest payload){
        try{
            InstallationData installation = installationRepository.findByInstallationId(payload.getInstallation().getId());
            Optional.ofNullable(installation).ifPresentOrElse(
                    (installationData) -> {
                        if(PRInfoUtils.compareStringEquality(PRInfoServiceConstant.GITHUB_INSTALLATION_ACTION_REPO_ADDED,
                                payload.getAction())){
                            this.updateRepoChange(payload.getRepositoriesAdded(),payload.getSender().getLogin(),installationData,true);
                        }

                        if(PRInfoUtils.compareStringEquality(PRInfoServiceConstant.GITHUB_INSTALLATION_ACTION_REPO_REMOVED,
                                payload.getAction())){
                            this.updateRepoChange(payload.getRepositoriesRemoved(), payload.getSender().getLogin(),installationData, false);
                        }
                    },
                    () -> {
                        log.error("Installation not found in Repo change request with payload -> {}",payload);
                    }
            );
        }catch (Exception e){
            log.error("Error occurred in GithubAppInstallationServiceImpl :: processRepoChangeRequest," +
                    "with error message {}, and payload {}",e.getMessage(),payload);
        }
    }


}
