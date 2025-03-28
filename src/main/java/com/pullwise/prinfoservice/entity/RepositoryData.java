package com.pullwise.prinfoservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "PW003_REPOSITORIES")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PW003_REPOSITORIES_K")
    private Long id;

    @Column(name = "PW003_REPOSITORY_ID",nullable = false)
    private Long repositoryId;

    @ManyToOne
    @JoinColumn(name = "PW001_INSTALLATION_ID", referencedColumnName = "PW001_INSTALLATION_ID", nullable = false)
    private InstallationData installation;

    @Column(name = "PW003_REPOSITORY_NAME")
    private String repositoryName;

    @Column(name = "PW003_REPOSITORY_FULLNAME")
    private String repositoryFullname;

    @Column(name = "PW003_PRIVATE_F")
    private Boolean privateFlag;

    @Column(name = "PW003_ACTIVE_F")
    private Boolean activeFlag;

    @Column(name = "PW003_CREATED_D")
    private LocalDateTime createdDate;

    @Column(name = "PW003_CREATED_S")
    private String createdBy;

    @Column(name = "PW003_LAST_UPDATED_D")
    private LocalDateTime lastUpdatedDate;

    @Column(name = "PW003_LAST_UPDATED_S")
    private String lastUpdatedBy;
}
