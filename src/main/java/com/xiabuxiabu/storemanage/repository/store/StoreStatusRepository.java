package com.xiabuxiabu.storemanage.repository.store;
import com.xiabuxiabu.storemanage.entity.store.StoreStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreStatusRepository extends JpaRepository<StoreStatus,Integer> {

}
