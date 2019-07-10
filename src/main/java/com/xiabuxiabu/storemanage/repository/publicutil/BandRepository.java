package com.xiabuxiabu.storemanage.repository.publicutil;

import com.xiabuxiabu.storemanage.entity.publicutil.Band;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BandRepository extends JpaRepository<Band,Integer> {
    @Query("from Band where name=:name")
    Band findByName(@Param("name") String name);
}
