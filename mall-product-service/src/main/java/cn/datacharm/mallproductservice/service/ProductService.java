package cn.datacharm.mallproductservice.service;


import cn.datacharm.mallcommonredis.config.JedisClusterConfig;
import cn.datacharm.mallproductservice.mapper.ProductMapper;
import cn.datacharm.pojo.Product;
import cn.datacharm.vo.EasyUIResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * description:
 *
 * @author Purity
 * @date 2019/08/19
 */
@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    public EasyUIResult pageQuery(Integer page, Integer rows){
        EasyUIResult result = new EasyUIResult();
        int total = productMapper.queryTotal();
        result.setTotal(total);
        int start = (page-1)*rows;
        List<Product> pList = productMapper.queryPage(start,rows);
        result.setRows(pList);
        return result;
    }



    public Product queryById(String productId) {
        //准备缓存逻辑 redis jedisCluster
        //准备一个操作缓存的key值 业务逻辑+参数相关
        String productQueryKey = "" + productId;

        try {


        } catch (Exception e) {
            e.printStackTrace();
        }
        return productMapper.queryById(productId);
    }
    public void saveProduct(Product product) {
        //product缺少productID补齐数据
        String productId = UUID.randomUUID().toString();
        product.setProductId(productId);
        //TODO 引入缓存的逻辑
        productMapper.saveProduct(product);
        //insert into t_product values(#{}#)

    }

    public void updateProduct(Product product) {
        //TODO 缓存锁解决高并发的资源争抢
        productMapper.updateProduct(product);
    }
}
