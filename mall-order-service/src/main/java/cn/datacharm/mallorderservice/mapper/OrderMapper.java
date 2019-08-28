package cn.datacharm.mallorderservice.mapper;

import cn.datacharm.pojo.Order;

import java.util.List;

public interface OrderMapper {

	List<Order> queryOrders(String userId);

	void addOrder(Order order);

	void deleteOrder(String orderId);

}
