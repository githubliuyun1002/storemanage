package com.xiabuxiabu.storemanage.service.publicutil;

import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;
import com.xiabuxiabu.storemanage.repository.publicutil.MarketRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketService {
    @Autowired
    private MarketRepository marketRepository;
    public void save(MarketEntity marketEntity){
        marketRepository.save(marketEntity);
    }
    public List<MarketEntity> findAll(){
        return marketRepository.findAll();
    }
}
