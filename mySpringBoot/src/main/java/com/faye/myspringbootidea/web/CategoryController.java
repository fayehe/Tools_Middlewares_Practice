package com.faye.myspringbootidea.web;

import com.faye.myspringbootidea.dao.CategoryDAO;
import com.faye.myspringbootidea.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CategoryController {
    @Autowired
    CategoryDAO categoryDAO;

    @GetMapping("/category")  //@RequestMapping("/listCategory") //1. 接受listCategory映射
    public String listCategory(Model model,
                               //在参数里接受当前是第几页 start ，以及每页显示多少条数据 size。 默认值分别是0和5。
                               @RequestParam(value = "start", defaultValue = "0") int start,
                               @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {

        start = start < 0 ? 0 : start; // 如果 start 为负，那么修改为0. 这个事情会发生在当前是首页，并点击了上一页的时候
        Sort sort = new Sort(Sort.Direction.DESC, "id"); //设置倒排序
        Pageable pageable = new PageRequest(start, size, sort); //根据start,size和sort创建分页对象
        Page page = categoryDAO.findAll(pageable); //CategoryDAO根据这个分页对象获取结果page.
        model.addAttribute("page", page);
        return "listCategory";

//        List<Category> categoryList=categoryDAO.findAll(); //2. 然后获取所有的分类数据
//        model.addAttribute("categoryList", categoryList); //3. 接着放如Model中
//        return "listCategory"; //4. 跳转到listCategory.jsp中
    }

    // 为CategoryController添加： 增加、删除、获取、修改映射
    // 值得注意：JPA 新增和修改用的都是save. 它根据实体类的id是否为0来判断是进行增加还是修改
    @PutMapping("/category") //@RequestMapping("/addCategory")
    public String addCategory(Category c) throws Exception {
        categoryDAO.save(c);
        return "redirect:/category";
    }
    @DeleteMapping("/category/{id}") //@RequestMapping("/deleteCategory")
    public String deleteCategory(Category c) throws Exception {
        categoryDAO.delete(c);
        return "redirect:/category";
    }
    @PostMapping("/category/{id}") //@RequestMapping("/updateCategory")
    public String updateCategory(Category c) throws Exception {
        categoryDAO.save(c);
        return "redirect:/category";
    }
    @GetMapping("/category/{id}") //@RequestMapping("/editCategory")
    public String editCategory(@PathVariable("id") int id, Model m) throws Exception {
        Category c= categoryDAO.getOne(id);
        m.addAttribute("c", c);
        return "editCategory";
    }
}
