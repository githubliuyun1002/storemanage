package com.xiabuxiabu.storemanage.repository.ccequip;

import com.xiabuxiabu.storemanage.entity.ccequip.ccItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CCItemsRepository extends JpaRepository<ccItems,Integer> {
    Page<ccItems> findAll(Pageable pageable);
    Page<ccItems> findAll(Specification<ccItems> specification,Pageable pageable);
    Page<ccItems> findAll(Specification<ccItems> specification);



}
