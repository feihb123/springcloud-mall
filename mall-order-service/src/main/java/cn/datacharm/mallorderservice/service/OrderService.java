package cn.datacharm.mallorderservice.service;

import cn.datacharm.mallorderservice.mapper.OrderMapper;
import cn.datacharm.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
	@Autowired
	private OrderMapper orderMapper;
	public List<Order> queryMyOrders(String userId) {
		return orderMapper.queryOrders(userId);
	}
	public void saveOrder(Order order) {
		//补齐数据
		order.setOrderId(UUID.randomUUID().toString());
		order.setOrderTime(new Date());
		order.setOrderPaystate(0);
		orderMapper.addOrder(order);
	}
	public void deleteOrder(String orderId) {
		orderMapper.deleteOrder(orderId);
		
	}

}
