package cn.datacharm.mallproductservice.service;


import cn.datacharm.mallproductservice.mapper.ProductMapper;
import cn.datacharm.pojo.Product;
import cn.datacharm.utils.MapperUtil;
import cn.datacharm.vo.EasyUIResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

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

    public EasyUIResult pageQuery(Integer page, Integer rows) {
        EasyUIResult result = new EasyUIResult();
        int total = productMapper.queryTotal();
        result.setTotal(total);
        int start = (page - 1) * rows;
        List<Product> pList = productMapper.queryPage(start, rows);
        result.setRows(pList);
        return result;
    }


    @Autowired
    private JedisCluster cluster;

    public Product queryById(String productId) {
        //引入缓存逻辑 redis jedisCluster
        //准备一个操作缓存的key值:业务逻辑+参数相关
        String productQueryKey = "product_query_" + productId;
        //判断redis缓存中是否存在商品数据
        try {
            if (cluster.exists(productQueryKey)) {
                //缓存命中,直接使用
                //不知道缓存的数据结构 String pJson
                String pJson = cluster.get(productQueryKey);
                return MapperUtil.MP.readValue(
                        pJson, Product.class);
            } else {//缓存未命中,数据库压力
                //读取数据库数据
                Product p = productMapper.queryById(productId);
                //将数据库读取的数据放到缓存一份
                String pJson = MapperUtil.MP.writeValueAsString(p);
                cluster.setex(productQueryKey, 60 * 60 * 24 * 2,
                        pJson);
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
        //缓存锁解决高并发的资源争抢
        //1 生成锁
        String productUpdateLock
                ="product_update_"+product.getProductId()
                +".lock";
        //2 删除缓存
        String productQueryKey="product_query_"
                +product.getProductId();
        cluster.del(productQueryKey);
        //3 更新数据库
        productMapper.updateProduct(product);
        //4 释放锁
        cluster.del(productUpdateLock);
    }
}
