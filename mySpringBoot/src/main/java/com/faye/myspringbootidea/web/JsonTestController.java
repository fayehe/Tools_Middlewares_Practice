package com.faye.myspringbootidea.web;

import com.faye.myspringbootidea.dao.CategoryDAO;
import com.faye.myspringbootidea.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //是spring4里的新注解，是@ResponseBody和@Controller的缩写。
//@Responsebody 注解表示该方法的返回的结果直接写入 HTTP 响应正文（ResponseBody）中，一般在异步获取数据时使用，通常是在使用 @RequestMapping 后，返回值通常解析为跳转路径，加上 @Responsebody 后返回结果不会被解析为跳转路径，而是直接写入HTTP 响应正文中。
//该注解用于将Controller的方法返回的对象，通过适当的HttpMessageConverter转换为指定格式后，写入到Response对象的body数据区。
//使用时机：返回的数据不是html标签的页面，而是其他某种格式的数据时（如json、xml等）使用；
public class JsonTestController {

    /**
     * test for JSON
     */
    @Autowired
    CategoryDAO categoryDAO;

    @GetMapping("/categoryJson")
    public List<Category> listCategory(@RequestParam(value = "start", defaultValue = "0") int start, @RequestParam(value = "size", defaultValue = "5") int size) throws Exception {
        start = start<0?0:start;
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start, size, sort);
        Page<Category> page =categoryDAO.findAll(pageable);
        System.out.println(page);
        System.out.println(page.getContent());
        return page.getContent();
    }

    @GetMapping("/categoryJson/{id}")
    public Category getCategory(@PathVariable("id") int id) throws Exception {
        Category c= categoryDAO.getOne(id);
        System.out.println(c);
        return c;
    }
    @PutMapping("/categoryJson")
    public void addCategory(@RequestBody Category category) throws Exception {
        System.out.println("springboot接受到浏览器以JSON格式提交的数据："+category);
    }
}
