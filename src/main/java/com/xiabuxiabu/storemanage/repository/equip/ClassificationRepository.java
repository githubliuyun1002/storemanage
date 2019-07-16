package com.xiabuxiabu.storemanage.repository.equip;


import com.xiabuxiabu.storemanage.entity.equip.Classification;
import com.xiabuxiabu.storemanage.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClassificationRepository extends JpaRepository<Classification,Integer> {
    Page<Classification> findAll(Specification<Classification> specification, Pageable pageable);
    Page<Classification> findAll(Pageable pageable);
    Page<Classification> findAll(Specification<User> specification);
    @Query("from  Classification  where  name=:name")
    Classification findByName(@Param("name") String name);


}
