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
@Table(name = "PW001_INSTALLATION")
public class InstallationData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PW001_INSTALLATION_K")
    private Long installationKey;

    @Column(name = "PW001_INSTALLATION_ID", nullable = false)
    private Long installationId;

    @Column(name = "PW001_ACTIVE_F", nullable = false)
    private Boolean activeFlag;

    @Column(name = "PW001_CREATED_D")
    private LocalDateTime createdDate;

    @Column(name = "PW001_CREATED_S")
    private String createdBy;

    @Column(name = "PW001_LAST_UPDATED_D")
    private LocalDateTime lastUpdatedDate;

    @Column(name = "PW001_LAST_UPDATED_S")
    private String lastUpdatedBy;
}