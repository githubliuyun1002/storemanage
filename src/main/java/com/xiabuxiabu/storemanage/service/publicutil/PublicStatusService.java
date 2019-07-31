package com.xiabuxiabu.storemanage.service.publicutil;

import com.xiabuxiabu.storemanage.entity.publicutil.PublicStatus;
import com.xiabuxiabu.storemanage.repository.publicutil.PublicStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublicStatusService {
    @Autowired
    private PublicStatusRepository publicStatusRepository;

    /**
     * 保存公共状态
     * @param publicStatus
     */
    public void save(PublicStatus publicStatus){
         publicStatusRepository.save(publicStatus);
    }

    /**
     * 获取所有的公共状态值
     * @return
     */
    public List<PublicStatus> findAll(){
        return publicStatusRepository.findAll();
    }

    public PublicStatus findById(int id){
        return  publicStatusRepository.findById(id).get();
    }
}
