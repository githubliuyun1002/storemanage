package com.xiabuxiabu.storemanage.service.equip;

import com.xiabuxiabu.storemanage.entity.equip.EquipEntity;
import com.xiabuxiabu.storemanage.repository.equip.EquipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.List;

@Service
public class EquipService {
    @Autowired
    private EquipRepository equipRepository;
    public Page<EquipEntity> findAll(int page,int size,String values){
        Sort sort = new Sort(Sort.Direction.ASC,"id");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        if(values!=null){
            return equipRepository.findAll(new Specification<EquipEntity>() {
                @Override
                public Predicate toPredicate(Root<EquipEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> name= root.get("name");// 设备名称
                    criteriaQuery.where(criteriaBuilder.like(name,"%" + values + "%"));
                    return null;
                }
            },pageable);
        }
        return equipRepository.findAll(pageable);
    }
    public List<EquipEntity> findAll(){
        return  equipRepository.findAll();
    }
    /**
     * 保存新增设备数据
     */
    public EquipEntity addSave(EquipEntity equipEntity){
        return equipRepository.save(equipEntity);
    }
    public EquipEntity findById(int id){
        return equipRepository.findById(id).get();
    }
}
