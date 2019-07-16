package com.xiabuxiabu.storemanage.service.equip;

import com.xiabuxiabu.storemanage.entity.equip.Classification;
import com.xiabuxiabu.storemanage.entity.equip.EquipName;
import com.xiabuxiabu.storemanage.entity.publicutil.ResultByClassAndEquipName;
import com.xiabuxiabu.storemanage.repository.equip.ClassificationRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.List;


@Service
public class ClassificationService {
    @Autowired
    private ClassificationRepository classificationRepository;
    @PersistenceContext  //持久化管理对象
    private EntityManager entityManager;
    public Page<Classification> findAllList(int page, int size, String values){
        Sort sort = new Sort(Sort.Direction.ASC,"classId");
        Pageable pageable = PageRequest.of(page-1,size,sort);
        if(values!=null){
            return classificationRepository.findAll(new Specification<Classification>() {
                @Override
                public Predicate toPredicate(Root<Classification> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Path<String> name = root.get("name");
                    Join  join = root.join(root.getModel().getSet("equipNames",EquipName.class),JoinType.LEFT);
                    Predicate p1 = criteriaBuilder.like(name, "%" + values + "%");
                    Predicate p2 = criteriaBuilder.like(join.get("name"),"%"+values+"%");
                    Predicate p = criteriaBuilder.or(p1,p2);
                    criteriaQuery.where(p);
                    criteriaQuery.distinct(true);
                    return null;
                }
            },pageable);
        }
        return classificationRepository.findAll(pageable);

    }
    public List<Classification> findAll(){
        return  classificationRepository.findAll();
    }
    public Classification findById(int id){
        return classificationRepository.findById(id).get();
    }
    public Classification save(Classification classification){
        return classificationRepository.save(classification);
    }

    /**
     * 使用原生sql进行查询
     * @param page
     * @param size
     * @param searchClass
     * @param searchEquipName
     * @return
     */
    public List<ResultByClassAndEquipName> findSearch(int page, int size, String searchClass, String searchEquipName){
        String sql = " select a.class_id, a. NAME, a.status, c.equip_id, c.equip_name " +
                " from classification a, class_equipname b,equip_name c " +
                " where a.class_id = b.class_id AND c.equip_id = b.equipname_id ";
        if(searchClass!=null&&searchClass!=""){
            sql +=" and a.name LIKE "+"'%"+searchClass+"%'";
        }
        if(searchEquipName!=null&&searchEquipName!=""){
            sql+= " and c.equip_name LIKE "+"'%"+searchEquipName+"%'";
        }
        System.out.println("sql----->"+sql);
        Query nativeQuery = entityManager.createNativeQuery(sql);
        //设置分页的页数
        nativeQuery.setFirstResult((page-1)*size);
        //设置每页展示多少条记录
        nativeQuery.setMaxResults(size);
        //最后展示的分页的查询结果
        return nativeQuery.getResultList();
    }

    public Classification findByName(String name){
        return  classificationRepository.findByName(name);
    }

}
