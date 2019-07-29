package com.xiabuxiabu.storemanage.repository.store;

import com.xiabuxiabu.storemanage.entity.store.Store;
import com.xiabuxiabu.storemanage.entity.store.StoreRemarks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface StoreRemarksRepository extends JpaRepository<StoreRemarks,Integer> {
    @Modifying
    @Transactional
    @Query("delete from StoreRemarks where itemId=:itemsId")
    int deleteByItemsId(@Param("itemsId") int itemsId);
    @Query("from StoreRemarks  where itemId=:itemsId")
    StoreRemarks findByItemsId(@Param("itemsId") int itemsId);

    List<StoreRemarks> findAll();
    Page<StoreRemarks> findAll(Pageable pageable);
    Page<StoreRemarks> findAll(Specification<StoreRemarks> specification);
    Page<StoreRemarks> findAll(Specification<StoreRemarks> specification,Pageable pageable);


}
