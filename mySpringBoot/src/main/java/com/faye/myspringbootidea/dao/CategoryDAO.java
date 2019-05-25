package com.faye.myspringbootidea.dao;

import com.faye.myspringbootidea.pojo.Category;
import org.springframework.data.jpa.repository.JpaRepository;

//- 继承JpaRepository，并且提供泛型<Category,Integer>，表示这个是针对Category类的DAO，Integer表示主键是Integer类型。
//- JpaRepository 这个父接口，就提供了CRUD, 分页等等一系列的查询了，直接拿来用，都不需要二次开发的了
public interface CategoryDAO extends JpaRepository<Category, Integer> {

}
