package com.springboot.example.talentlens.Repositories;

import com.springboot.example.talentlens.Evidence.EvidenceModule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvidenceModuleRepository extends JpaRepository<EvidenceModule, Long> {
}