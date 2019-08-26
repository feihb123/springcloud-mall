package cn.datacharm.mallproductservice.mapper;

import cn.datacharm.pojo.Product;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * description:
 *
 * @author Purity
 * @date 2019/08/19
 */
@Mapper
@Repository
public interface ProductMapper {
    @Select("select count(product_id) from t_product") //相当于mapper.xml实现的sql
    int queryTotal();
    @Select("select * from t_product limit #{start},#{rows}")
    List<Product> queryPage(@Param("start") int start, @Param("rows") Integer rows);
    @Select("select * from t_product where product_id=#{productId}")
    Product queryById(String productId);
    @Insert("insert into t_product (product_id,product_price,product_name,product_category" +
            ",product_description,product_num,product_imgurl) values (#{productId}," +
            "#{productPrice},#{productName},#{productCategory},#{productDescription}," +
            "#{productNum},#{productImgurl})")
    void saveProduct(Product product);
    @Update("update t_product set product_name= #{productName}" +
            ",product_price= #{productPrice}" +
            ",product_num= #{productNum}" +
            ",product_description= #{productDescription}" +
            ",product_category= #{productCategory}" +
            ",product_imgurl=  #{productImgurl}" +
            " where product_id=#{productId}" )
    void updateProduct(Product product);
}
