package com.xiabuxiabu.storemanage.repository.ccstore;

import com.xiabuxiabu.storemanage.entity.ccstore.CCStore;

import com.xiabuxiabu.storemanage.entity.store.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CCStoreRepository extends JpaRepository<CCStore,Integer> {
    List<CCStore> findAll();
    Page<CCStore> findAll(Pageable pageable);
    Page<CCStore> findAll(Specification<CCStore> specification);
    Page<CCStore> findAll(Specification<CCStore> specification,Pageable pageable);
    @Query("from CCStore where storeCode=:storeCode")
    CCStore findByStoreCode(@Param("storeCode") String storeCode);

    @Query("from CCStore where marketName =:marketName")
    List<CCStore> findByMarketName(@Param("marketName")String marketName);


}
