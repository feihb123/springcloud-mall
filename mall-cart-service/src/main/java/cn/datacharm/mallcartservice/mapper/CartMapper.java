package cn.datacharm.mallcartservice.mapper;

import java.util.List;

import cn.datacharm.pojo.Cart;
import org.apache.ibatis.annotations.Select;

public interface CartMapper {
	@Select("select * from t_cart where user_id=#{userId}")
	List<Cart> queryMyCart(String userId);

}
