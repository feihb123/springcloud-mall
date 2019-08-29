package cn.datacharm.mallorderservice.mapper;

import cn.datacharm.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface OrderMapper {

	List<Order> queryOrders(String userId);

	void addOrder(Order order);

	void deleteOrder(String orderId);

}
