package cn.datacharm.mallcartservice.service;

import java.util.List;

import cn.datacharm.mallcartservice.mapper.CartMapper;
import cn.datacharm.pojo.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CartService {
	@Autowired
	private CartMapper cartMapper;
	public List<Cart> queryMyCart(String userId) {
		return cartMapper.queryMyCart(userId);
	}

}
