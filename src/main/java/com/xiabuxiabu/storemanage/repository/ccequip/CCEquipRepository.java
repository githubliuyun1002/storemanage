package com.xiabuxiabu.storemanage.repository.ccequip;

import com.xiabuxiabu.storemanage.entity.ccequip.ccEquip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CCEquipRepository extends JpaRepository<ccEquip,Integer> {
    Page<ccEquip> findAll(Pageable pageable);
    Page<ccEquip> findAll(Specification<ccEquip> specification);
    Page<ccEquip> findAll(Specification<ccEquip> specification,Pageable pageable);

    @Query("from ccEquip where name =:name")
    ccEquip findByName(@Param("name") String name);


}
