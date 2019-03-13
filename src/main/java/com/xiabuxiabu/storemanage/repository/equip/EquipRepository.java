package com.xiabuxiabu.storemanage.repository.equip;

import com.xiabuxiabu.storemanage.entity.equip.EquipEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipRepository extends JpaRepository<EquipEntity,Integer> {
    List<EquipEntity> findAll();
    Page<EquipEntity> findAll(Pageable pageable);
    Page<EquipEntity> findAll(Specification<EquipEntity> specification);
    Page<EquipEntity> findAll(Specification<EquipEntity> specification,Pageable pageable);

}
