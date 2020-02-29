package cn.how2j.springcloud.client;
 
import java.util.ArrayList;
import java.util.List;
 
import org.springframework.stereotype.Component;
 
import cn.how2j.springcloud.pojo.Product;
 
@Component
public class ProductClientFeignHystrix implements ProductClientFeign{
    public List<Product> listProdcuts(){
        // 例如：调用备用data service查询产品信息
        List<Product> result = new ArrayList<>();
        result.add(new Product(0,"产品数据微服务不可用 - 断路器",0));
        return result;
    }
}