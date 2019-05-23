package com.xiabuxiabu.storemanage.repository.equip;

import com.xiabuxiabu.storemanage.entity.equip.EquipName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EquipNameRepository extends JpaRepository<EquipName,Integer> {
    Page<EquipName> findAll(Specification<EquipName> specification, Pageable pageable);
    Page<EquipName> findAll(Pageable pageable);
    Page<EquipName> findAll(Specification<EquipName> specification);

}
