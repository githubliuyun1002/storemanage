package com.xiabuxiabu.storemanage.service.equip;

import com.xiabuxiabu.storemanage.entity.equip.Classification;
import com.xiabuxiabu.storemanage.entity.equip.EquipName;
import com.xiabuxiabu.storemanage.entity.equip.Item;
import com.xiabuxiabu.storemanage.repository.equip.EquipNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class EquipNameService {
    @Autowired
    private EquipNameRepository equipNameRepository;
    public List<EquipName> findAll(){
        return  equipNameRepository.findAll();
    }
    public EquipName findById(int id){
        return equipNameRepository.findById(id).get();
    }
    public EquipName save(EquipName equipName){
        return  equipNameRepository.save(equipName);
    }
    public Page<EquipName> findAllList(int page,int size,String value) {
        Sort sort = new Sort(Sort.Direction.ASC, "equipId");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        /*if(value!=null){
            return equipNameRepository.findAll((Root<EquipName> root,CriteriaQuery<?> criteriaQuery,CriteriaBuilder criteriaBuilder)->{
                List<Predicate> predicates = new ArrayList<Predicate>();
                predicates.add(criteriaBuilder.like(root.get("name").as(String.class),"%"+value+"%"));
                predicates.add(criteriaBuilder.isNotNull(root.get("itemSet").as(Item.class)));
                return criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            },pageable);
        }else{
            return equipNameRepository.findAll(new Specification<EquipName>() {
                @Override
                public Predicate toPredicate(Root<EquipName> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    criteriaQuery.where(criteriaBuilder.isNotNull(root.get("itemSet").as(Item.class)));
                    return null;
                }
            },pageable);
        }*/
        if (value != null) {
            return equipNameRepository.findAll(new Specification<EquipName>() {
                @Override
                public Predicate toPredicate(Root<EquipName> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> equipName = root.get("name");
                    criteriaQuery.where(criteriaBuilder.like(equipName, "%" + value + "%"));
                    return null;
                }
            }, pageable);
        }
        return equipNameRepository.findAll(pageable);

    }



}
