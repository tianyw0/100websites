package com.tianyongwei.repo;

import com.tianyongwei.entity.core.CheatSheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheatSheetRepo extends JpaRepository<CheatSheet, Long> {

  List<CheatSheet> findByUserIdAndSubjectIdAndIsDrop(Long id, Long subjectId, boolean b);
}
