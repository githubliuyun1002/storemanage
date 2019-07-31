package com.xiabuxiabu.storemanage.repository.equip;

import com.xiabuxiabu.storemanage.entity.equip.EquipName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EquipNameRepository extends JpaRepository<EquipName,Integer> {
    Page<EquipName> findAll(Specification<EquipName> specification, Pageable pageable);
    Page<EquipName> findAll(Pageable pageable);
    Page<EquipName> findAll(Specification<EquipName> specification);
    @Query("from EquipName where classId=:classId")
    List<EquipName> findByClassId(@Param("classId") int classId);
    @Query("from EquipName where name=:name and className=:className")
    EquipName findByName(@Param("name") String name,@Param("className") String className);

}
