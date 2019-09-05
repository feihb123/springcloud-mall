package cn.datacharm.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.Before;
import org.junit.Test;


/**
 * description:
 * 实现rabbitmq五种工作模式的第一种 一发一接
 * @author Herb
 * @date 2019/09/2019-09-05
 */
public class SimpleModelTest {
    //准备一个客户端连接对象 channel信道
    private Channel channel;
    @Before
    public void init() throws Exception{
        //获取长连接,构造一个连接工程
        ConnectionFactory factory=new ConnectionFactory();
        //需要将rabbitmq的连接信息提供给工程
        factory.setHost("10.9.104.184");
        factory.setPort(5672);//15672web控制台 5672代码客户端端口
        factory.setUsername("guest");
        factory.setPassword("guest");
        //从工厂获取连接
        Connection conn = factory.newConnection();
        //获取短连接
        channel=conn.createChannel();
    }
    @Test
    //生产端代码 创建生成一条消息,通过channel发送到消息队列
    public void sender() throws Exception{
        //准备一条消息
        String msg="hello world";
        //调用channel 将消息发送到交换机AMQP default
        //默认的AMQP default会绑定所有的后端队列，
        //以队列名称绑定
        //exchange :交换机名称 AMQP default的名称是""
        //routingKey: 路由key 后端接收消息的队列名称
        //props:消息的属性
        //body:消息体 byte[]类型的二进制数据
        for(int i=0;i<10;i++){
            channel.basicPublish("", "simple02", null, msg.getBytes());
        }
    }
    @Test
    public void con() throws Exception{
        //声明一个队列
        /*queue:队列名称
         *durable：持久化吗？true 持久化 false 不持久化-+
         *exclusive：是否专属于一个连接对象
         *argument:Map类型定义了队列的属性
         *autoDelete:是否自动删除 当最后一个channel连接完成当前队列后是否自动删除
         *	例如：可以容纳的消息长度
         *		  消息超时等
         */
        channel.queueDeclare("simple01",false, false, false, null);
        //如果rabbitmq存在队列,直接使用,不存在才创建
    }

}
