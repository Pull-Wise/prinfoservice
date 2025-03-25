package com.pullwise.prinfoservice.repository;

import com.pullwise.prinfoservice.entity.InstallationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallationRepository extends JpaRepository<InstallationData, Long> {
    InstallationData findByInstallationId(long installationId);

}