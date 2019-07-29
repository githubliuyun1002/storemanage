package com.xiabuxiabu.storemanage.repository.store;
import com.xiabuxiabu.storemanage.entity.store.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import javax.transaction.Transactional;
import java.util.List;

public interface StoreRepository extends JpaRepository<Store,Integer>{
    List<Store> findAll();
    Page<Store> findAll(Pageable pageable);
    Page<Store> findAll(Specification<Store> specification);
    Page<Store> findAll(Specification<Store> specification,Pageable pageable);
    @Query("from Store where storeCode =:code")
    Store findByStoreCode(@Param("code") String code);
    @Query("from Store where storeStatus.statusName=:storeStatus")
    List<Store> findByStoreStatus(@Param("storeStatus") String storeStatus);

    @Query("select distinct(marketName) from  Store ")
    List<String> marketNameList();
    @Query("from Store where marketName=:marketName")
    List<Store> findByMarketName(@Param("marketName")String marketName);
//    @Modifying
//    @Transactional
//    @Query("delete from Store where itemsSet.id=:itemsId")
//    int deleteByStoreItmsAndStoreId(@Param("itemsId") int itemsId);




 }
