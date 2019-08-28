package cn.datacharm.mallcartservice.controller;

import java.util.List;

import cn.datacharm.mallcartservice.service.CartService;
import cn.datacharm.pojo.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("cart/manage")
public class CartController {
	@Autowired
	private CartService cartService;
	//根据userId查询我的购物车商品数据
	@RequestMapping("query")
	public List<Cart> queryMyCart(String userId){
		return cartService.queryMyCart(userId);
	}
}
