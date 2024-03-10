package com.tianyongwei.repo;

import com.tianyongwei.entity.core.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepo extends JpaRepository<Article, Long> {

    List<Article> findBySubjectIdAndUserIdAndIsDrop(Long subjectId, Long id, boolean b);
}
