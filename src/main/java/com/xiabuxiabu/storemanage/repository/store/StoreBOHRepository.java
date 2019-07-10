package com.xiabuxiabu.storemanage.repository.store;

import com.xiabuxiabu.storemanage.entity.store.StoreBOH;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreBOHRepository extends JpaRepository<StoreBOH,String> {
    @Query("select distinct(marketname) from StoreBOH")
    List<String> marketNameList();

    @Query("select distinct(band) from StoreBOH")
    List<String> bandList();

    @Query("select distinct (marketname) from StoreBOH where band=:band")
    List<String> bandMarketList(@Param("band") String band);
}
