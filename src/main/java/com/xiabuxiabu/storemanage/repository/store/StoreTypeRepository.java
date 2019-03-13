package com.xiabuxiabu.storemanage.repository.store;

import com.xiabuxiabu.storemanage.entity.store.StoreType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreTypeRepository extends JpaRepository<StoreType,Integer> {
    @Query("from StoreType  where storeId = :storeId")
    List<StoreType> findTypeByStoreId(@Param("storeId") int storeId);
}
