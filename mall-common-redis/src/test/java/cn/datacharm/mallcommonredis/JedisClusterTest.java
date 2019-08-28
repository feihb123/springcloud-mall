package cn.datacharm.mallcommonredis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * description:
 *
 * @author Herb
 * @date 2019/08/2019-08-28
 */
public class JedisClusterTest {
    /*
     * 测试使用jediscluster操作redis集群
     */
    @Test
    public void test(){
        //收集节点信息,由于两两互联,只需要提供其中几个节点即可
        Set<HostAndPort> set=new HashSet<HostAndPort>();
        set.add(new HostAndPort("39.106.81.102", 10001));
        //由于jedisCluster创建了连接池 JedisPool
        GenericObjectPoolConfig config=
                new GenericObjectPoolConfig();
        config.setMaxIdle(8);
        config.setMaxTotal(200);
        //利用set和config对象构造一个jedisCluster
        JedisCluster cluster=new JedisCluster(set, config);
        //调用api测试访问使用redis集群
        cluster.set("name", "王老师");//key-value
        System.out.println(cluster.get("name"));
    }
}
