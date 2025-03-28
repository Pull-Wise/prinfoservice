package com.pullwise.prinfoservice.repository;

import com.pullwise.prinfoservice.entity.RepositoryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryDataRepository extends JpaRepository<RepositoryData, Long> {
    RepositoryData findByRepositoryId(Long repositoryId);

}