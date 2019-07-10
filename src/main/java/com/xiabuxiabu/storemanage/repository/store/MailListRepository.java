package com.xiabuxiabu.storemanage.repository.store;

import com.xiabuxiabu.storemanage.entity.store.MailList;
import com.xiabuxiabu.storemanage.entity.store.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MailListRepository extends JpaRepository<MailList,Integer> {
    Page<MailList> findAll(Pageable pageable);
    Page<MailList> findAll(Specification<MailList> specification);
    Page<MailList> findAll(Specification<MailList> specification,Pageable pageable);
    @Query("from MailList where  storeCode=:storeCode")
    MailList findByStoreCode(@Param("storeCode") String storeCode);
    @Query("select distinct(marketName) from MailList")
    List<String> marketList();
    @Query("from MailList where marketName=:marketName")
    List<MailList> findByMarketName(@Param("marketName") String marketName);

}
