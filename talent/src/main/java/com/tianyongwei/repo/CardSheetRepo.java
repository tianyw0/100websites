package com.tianyongwei.repo;

import com.tianyongwei.entity.core.CardSheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardSheetRepo extends JpaRepository<CardSheet, Long> {

    List<CardSheet> findByUserIdAndSubjectIdAndIsDrop(Long id, Long subjectId, boolean b);
}
