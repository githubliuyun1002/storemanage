package com.xiabuxiabu.storemanage.repository.equip;


import com.xiabuxiabu.storemanage.entity.equip.Classification;
import com.xiabuxiabu.storemanage.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassificationRepository extends JpaRepository<Classification,Integer> {
    Page<Classification> findAll(Specification<Classification> specification, Pageable pageable);
    Page<Classification> findAll(Pageable pageable);
    Page<Classification> findAll(Specification<User> specification);
}
