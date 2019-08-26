package cn.datacharm.malluserservice.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author feihb
 */
@Configuration
@ConfigurationProperties(prefix = "mall.redis")
//@ConditionalOnClass({Jedis.class, ShardedJedis.class})
public class ShardeJedisPoolConfig {

    private List<String> nodes;

    private Integer maxTotal;

    private Integer maxIdle;

    private Integer minIdle;

    @Bean
    public ShardedJedisPool initShardedJedisPool() {
        //收集节点信息
        List<JedisShardInfo> info = new ArrayList<JedisShardInfo>();
        //for循环nodes, 增强型for循环
        for (String node : nodes) {
            String hostIP = node.split(":")[0];
            int port = Integer.parseInt(node.split(":")[1]);
            info.add(new JedisShardInfo(hostIP, port));
        }
        //准备构造的连接池对象需要配置对象config
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        //构造连接池对象
        ShardedJedisPool pool = new ShardedJedisPool(config, info);
        return pool;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public Integer getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(Integer maxTotal) {
        this.maxTotal = maxTotal;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }
}

