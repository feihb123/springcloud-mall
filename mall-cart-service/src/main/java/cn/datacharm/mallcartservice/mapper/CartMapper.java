package cn.datacharm.mallcartservice.mapper;

import cn.datacharm.pojo.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface CartMapper {

	List<Cart> queryMyCart(String userId);

	Cart queryExist(Cart cart);

	void saveCart(Cart cart);

	void updateNum(Cart cart);

	void deleteCart(Cart cart);

}
