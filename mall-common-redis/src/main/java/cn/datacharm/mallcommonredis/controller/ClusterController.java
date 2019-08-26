package cn.datacharm.mallcommonredis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

/**
 * description:
 *
 * @author Herb
 * @date 2019/08/2019-08-26
 */
@RestController
public class ClusterController {
    @Autowired
    private JedisCluster cluster;
    @RequestMapping("cluster")
    public String setAndGet(String key,String value){
        cluster.set(key,value);
        return cluster.get(key);
    }

}