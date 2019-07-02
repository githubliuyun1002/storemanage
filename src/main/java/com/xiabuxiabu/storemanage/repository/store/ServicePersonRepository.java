package com.xiabuxiabu.storemanage.repository.store;

import com.xiabuxiabu.storemanage.entity.store.ServicePerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServicePersonRepository extends JpaRepository<ServicePerson,Integer> {
    @Query("from ServicePerson where serviceName =:serviceName")  //
    ServicePerson findByServiceName(@Param("serviceName") String serviceName);
}
