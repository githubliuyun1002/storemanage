package com.xiabuxiabu.storemanage.service.equip;

import com.xiabuxiabu.storemanage.entity.equip.TypeEntity;
import com.xiabuxiabu.storemanage.repository.equip.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TypeService {
    @Autowired
    private TypeRepository typeRepository;
    public TypeEntity save(TypeEntity typeEntity){
        return typeRepository.save(typeEntity);
    }
    public TypeEntity findByName(String typeName){
        return typeRepository.findByName(typeName);
    }
    public TypeEntity findById(int id){
        return typeRepository.findById(id).get();
    }
}
