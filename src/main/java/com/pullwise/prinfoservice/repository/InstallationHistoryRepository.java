package com.pullwise.prinfoservice.repository;

import com.pullwise.prinfoservice.entity.InstallationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallationHistoryRepository extends JpaRepository<InstallationHistory, Long> {
}