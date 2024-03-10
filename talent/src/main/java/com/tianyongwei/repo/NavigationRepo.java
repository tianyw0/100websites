package com.tianyongwei.repo;

import com.tianyongwei.entity.core.Navigation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NavigationRepo extends JpaRepository<Navigation, Long> {

    List<Navigation> findByUserIdAndSubjectId(Long id, Long subjectId);

    List<Navigation> findByUserIdAndSubjectIdAndIsDrop(Long id, Long subjectId, boolean b);
}
