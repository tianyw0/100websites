package com.tianyongwei.repo;

import com.tianyongwei.entity.core.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepo extends JpaRepository<Subject, Long> {
    Page<Subject> findByUserIdAndIsDrop(Long id, boolean b, Pageable pageable);
}
