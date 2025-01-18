package com.consultingfirm.benchprofiles.repository;

import com.consultingfirm.benchprofiles.model.BenchProfilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenchProfilesRepository extends JpaRepository<BenchProfilesEntity, Long> {
}
