package cn.datacharm.mallproductservice.controller;

import cn.datacharm.mallproductservice.service.ProductService;
import cn.datacharm.pojo.Product;
import cn.datacharm.vo.EasyUIResult;
import cn.datacharm.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description:
 *
 * @author Purity
 * @date 2019/08/19
 */
@RestController
@RequestMapping("/product/manage")
public class ProductController {

    @Autowired
    private ProductService productservice;

    //商品的分页查询
    @RequestMapping("pageManage")
    public EasyUIResult productPageQuery(Integer page, Integer rows) {

        EasyUIResult result = productservice.pageQuery(page, rows);
        return result;
    }

    //商品的单个查询
    @RequestMapping("{haha}/{productId}")
    //在路径中如果某个阶段的字符串想要接收成为参数变量的值
    //需要在springmvc中使用变量名称
    public Product queryById(@PathVariable String haha, @PathVariable String productId) {
        //haha,productId在请求url中以?key=value
        //post请求体key=value
//        System.out.println("haha:"+haha);
//        System.out.println("productId:"+productId);
        Product product = productservice.queryById(productId);
        return product;
    }

    //商品的新增
    @RequestMapping("save")
    public SysResult saveProduct(Product product) {
        //成功与失败
        try {
            productservice.saveProduct(product);
            return SysResult.ok();//{"status":200,"msg"
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.build(201, e.getMessage(), null);
        }
    }

    //商品的更新
    @RequestMapping("update")
    public SysResult updateProduct(Product product) {
        //包含了所有的商品字段属性值
        try {
            productservice.updateProduct(product);
            return SysResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.build(201, e.getMessage(), null);
        }
    }

}
