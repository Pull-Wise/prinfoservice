package com.pullwise.prinfoservice.repository;

import com.pullwise.prinfoservice.entity.HookData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HookDataRepository extends JpaRepository<HookData, Long> {
}