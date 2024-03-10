package com.tianyongwei.service;

import com.tianyongwei.config.MyWebUtil;
import com.tianyongwei.entity.core.Subject;
import com.tianyongwei.repo.SubjectRepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepo subjectRepo;

    public Long add(String name, String intro, Long id) {
        if(StringUtils.isEmpty(name) || StringUtils.isEmpty(intro)) {
            return null;
        }
        Subject subject = new Subject();
        subject.setUserId(MyWebUtil.getCurrentUser().getId());
        subject.setName(name);
        subject.setIntro(intro);
        subject = subjectRepo.save(subject);
        return subject.getId();
    }

    public Page<Subject> mySubjectList(Pageable pageable) {
        System.out.println(MyWebUtil.getCurrentUser().getId());
        return subjectRepo.findByUserIdAndIsDrop(MyWebUtil.getCurrentUser().getId(), false, pageable);
    }

    public void del(Long id) {
        Subject subject = subjectRepo.findOne(id);
        subject.setDrop(true);
        subjectRepo.save(subject);
    }

    public Page<Subject> findAll(Pageable pageable) {
        Page<Subject> page = subjectRepo.findAll(pageable);
        return page;
    }

    public void saveEdit(Long id, String name, String intro) {
        Subject subject = subjectRepo.findOne(id);
        subject.setName(name);
        subject.setIntro(intro);
        subjectRepo.saveAndFlush(subject);
    }

    public Subject subjectInfo(Long subjectId) {
        return subjectRepo.findOne(subjectId);
    }
}
