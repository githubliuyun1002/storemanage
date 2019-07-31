package com.xiabuxiabu.storemanage.service.equip;

import com.xiabuxiabu.storemanage.entity.equip.Classification;
import com.xiabuxiabu.storemanage.entity.equip.EquipName;
import com.xiabuxiabu.storemanage.entity.equip.Item;
import com.xiabuxiabu.storemanage.entity.publicutil.PublicStatus;
import com.xiabuxiabu.storemanage.repository.equip.EquipNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
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
        if (value != null) {
            return equipNameRepository.findAll(new Specification<EquipName>() {
                @Override
                public Predicate toPredicate(Root<EquipName> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> equipName = root.get("name");
                    Join join = root.join( root.getModel().getSet("itemSet",Item.class),JoinType.LEFT);
                    Predicate p1 = criteriaBuilder.like(join.get("name"),"%"+value+"%");
                    Predicate p2 = criteriaBuilder.like(equipName, "%" + value + "%");
                    Predicate p = criteriaBuilder.or(p1,p2);
                    criteriaQuery.where(p);
                    //对实体中有set集合的查询时，会使得记录的条数增多set集合中的元素，以主键进行区分。
                    //需要对查询的结果进行去重 distinct(true)
                    criteriaQuery.distinct(true);
                    return null;
                }
            }, pageable);
        }
        return equipNameRepository.findAll(pageable);
    }

    public Page<EquipName> findAllEquip(int page,int size,String value){
        Sort sort = new Sort(Sort.Direction.ASC, "equipId");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        if(value!=null){
            return equipNameRepository.findAll(new Specification<EquipName>() {
                @Override
                public Predicate toPredicate(Root<EquipName> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> equipName = root.get("name");
                    Path<String> className = root.get("className");
                    Path<PublicStatus> publicStatus =root.get("publicStatus");
                 //   Join join = root.join( root.getModel().getSet("itemSet",Item.class),JoinType.LEFT);
                    Predicate p1 = criteriaBuilder.like(equipName, "%" + value + "%");
                    Predicate p2 = criteriaBuilder.like(className, "%" + value + "%");
                    Predicate p = criteriaBuilder.or(p1,p2);
                    //######################
                   // Predicate equal = criteriaBuilder.equal(publicStatus.get("name"),"启用");
                    criteriaQuery.where(p);
                    criteriaQuery.distinct(true);
                    return null;
                }
            },pageable);

        }
        return equipNameRepository.findAll(pageable);
    }
    public List<EquipName> findByClassId(int classId){
        return equipNameRepository.findByClassId(classId);
    }

    public EquipName findByName( String name,String className){
        return  equipNameRepository.findByName(name,className);
    }

}
