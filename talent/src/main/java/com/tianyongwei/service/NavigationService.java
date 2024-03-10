package com.tianyongwei.service;

import com.tianyongwei.config.MyWebUtil;
import com.tianyongwei.entity.core.Navigation;
import com.tianyongwei.repo.NavigationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NavigationService {

    @Autowired
    private NavigationRepo navigationRepo;

    public void del(Long id) {
        Navigation navigation = navigationRepo.findOne(id);
        navigation.setDrop(true);
        navigationRepo.save(navigation);
    }

    public Navigation add(Long subjectId, String title, String url) {
        Navigation navigation = new Navigation();
        navigation.setSubjectId(subjectId);
        navigation.setTitle(title);
        navigation.setUrl(url);
        navigation.setUserId(MyWebUtil.getCurrentUser().getId());
        navigation = navigationRepo.saveAndFlush(navigation);
        return navigation;
    }

    public List<Navigation> getNavListBySubjectId(Long subjectId) {
        return navigationRepo.findByUserIdAndSubjectIdAndIsDrop(MyWebUtil.getCurrentUser().getId(),subjectId, false);
    }

    public Navigation saveEdit(Long id, String title, String url) {
        Navigation navigation = navigationRepo.findOne(id);
        navigation.setTitle(title);
        navigation.setUrl(url);
        navigation = navigationRepo.saveAndFlush(navigation);
        return navigation;
    }
}
