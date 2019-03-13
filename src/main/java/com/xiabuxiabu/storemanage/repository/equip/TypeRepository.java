package com.xiabuxiabu.storemanage.repository.equip;

import com.xiabuxiabu.storemanage.entity.equip.TypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TypeRepository extends JpaRepository<TypeEntity,Integer> {
    @Query("from TypeEntity where typeName = :typeName")
    TypeEntity findByName(@Param("typeName") String typeName);

}
