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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PW_HOOK_HST")
public class HookData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PW_HOOK_ID")
    private Long hookId;

    @Column(name = "PW_HOOK_DATA", nullable = false)
    private String hookData;

    @Column(name = "PW_EVENT_TYPE")
    private String eventType;

    @Column(name = "PW_CREATED_D")
    private LocalDateTime createdDate;

    @Column(name = "PW_CREATED_S")
    private String createdBy;

    @Column(name = "PW_LAST_UPDATED_D")
    private LocalDateTime lastUpdatedDate;

    @Column(name = "PW_LAST_UPDATED_S")
    private String lastUpdatedBy;
}