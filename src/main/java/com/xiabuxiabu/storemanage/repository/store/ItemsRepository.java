package com.xiabuxiabu.storemanage.repository.store;

import com.xiabuxiabu.storemanage.entity.equip.Items;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemsRepository extends JpaRepository<Items,Integer> {
    @Query("select distinct(className) from Items")
    public List<Items> findByClassName(@Param("classname") String classname);

}
