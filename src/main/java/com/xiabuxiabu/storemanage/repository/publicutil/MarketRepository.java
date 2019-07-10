package com.xiabuxiabu.storemanage.repository.publicutil;

import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MarketRepository extends JpaRepository<MarketEntity,Integer> {
    @Query("from MarketEntity where name=:marketName")
    MarketEntity findMarketByMarketName(@Param("marketName")String marketName);
    @Query("from MarketEntity where marketId=:marketCode")
    MarketEntity findByMarketCode(@Param("marketCode") int marketCode);

}
