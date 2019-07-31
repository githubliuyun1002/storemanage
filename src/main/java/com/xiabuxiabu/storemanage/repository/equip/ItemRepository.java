package com.xiabuxiabu.storemanage.repository.equip;

import com.xiabuxiabu.storemanage.entity.equip.EquipName;
import com.xiabuxiabu.storemanage.entity.equip.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemRepository extends JpaRepository<Item,Integer> {
    Page<Item> findAll(Specification<Item> specification, Pageable pageable);
    Page<Item> findAll(Pageable pageable);
    Page<Item> findAll(Specification<Item> specification);
    @Query("from Item where name=:name and equipName=:equipName")
    Item findByName(@Param("name") String name,@Param("equipName") String equipName);


}
