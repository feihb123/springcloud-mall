package cn.datacharm.mallcartservice.mapper;

import cn.datacharm.pojo.Cart;

import java.util.List;

public interface CartMapper {

	List<Cart> queryMyCart(String userId);

	Cart queryExist(Cart cart);

	void saveCart(Cart cart);

	void updateNum(Cart cart);

	void deleteCart(Cart cart);

}
