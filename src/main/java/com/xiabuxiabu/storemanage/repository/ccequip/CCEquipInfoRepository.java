package com.xiabuxiabu.storemanage.repository.ccequip;

import com.xiabuxiabu.storemanage.entity.ccequip.CCEquipInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CCEquipInfoRepository extends JpaRepository<CCEquipInfo,Integer> {
    Page<CCEquipInfo> findAll(Pageable pageable);
    Page<CCEquipInfo> findAll(Specification<CCEquipInfo> specification);
    Page<CCEquipInfo> findAll(Specification<CCEquipInfo> specification,Pageable pageable);

}
