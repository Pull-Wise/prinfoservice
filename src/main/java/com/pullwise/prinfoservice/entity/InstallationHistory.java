package com.pullwise.prinfoservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PW002_INSTALLATION_HST")
public class InstallationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PW002_INSTALLATION_HST_K")
    private Long installationHistoryKey;

    @ManyToOne
    @JoinColumn(name = "PW001_INSTALLATION_ID",referencedColumnName = "PW001_INSTALLATION_ID",nullable = false)
    private InstallationData installation;

    @Column(name = "PW002_INSTALLATION_ACTION", nullable = false)
    private String action;

    @Column(name = "PW002_REPO_COUNT")
    private Integer repoCount;

    @Column(name = "PW002_CREATED_D")
    private LocalDateTime createdDate;

    @Column(name = "PW002_CREATED_S")
    private String createdBy;

    @Column(name = "PW002_LAST_UPDATED_D")
    private LocalDateTime lastUpdatedDate;

    @Column(name = "PW002_LAST_UPDATED_S")
    private String lastUpdatedBy;
}