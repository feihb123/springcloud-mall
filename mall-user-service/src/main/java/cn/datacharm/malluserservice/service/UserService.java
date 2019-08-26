package cn.datacharm.malluserservice.service;

import cn.datacharm.pojo.User;
import cn.datacharm.malluserservice.mapper.UserMapper;
import cn.datacharm.utils.MD5Util;
import cn.datacharm.utils.MapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.UUID;

/**
 * description:
 *
 * @author Purity
 * @date 2019/08/19
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    ShardedJedisPool pool;

    private Jedis jedis = new Jedis("39.106.81.102", 6379);

    public int checkUserName(String userName) {
        return userMapper.checkUserName(userName);
    }

    public void saveUser(User user) {
        //对用户密码进行加密
        String md5password = MD5Util.md5(user.getUserPassword());
        user.setUserPassword(md5password);
        //登录比对校验数据的userPassword也加密比对
        //补充userId
        user.setUserId(UUID.randomUUID().toString());
        userMapper.saveUser(user);
    }

    public String login(User user) {
        /**
         * 对User的password进行加密
         */
        ShardedJedis shardedJedis = pool.getResource();
        try {
            user.setUserPassword(MD5Util.md5(user.getUserPassword()));
            User existUser = userMapper.queryExist(user);
            if (existUser != null) {
                String userJson = MapperUtil.MP.writeValueAsString(existUser);
                String ticket = "EM_TICKET" + System.currentTimeMillis() + existUser.getUserId();
                jedis.setex(ticket, 60 * 60 * 2, userJson);
                return ticket;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        } finally {
            pool.returnResource(shardedJedis);
        }
        return "";
    }

    public String queryUserJson(String ticket) {

        try {
            Long leftTime = jedis.pttl(ticket);
            Long leaseTime = 1000 * 60 * 2L;
            if (leftTime <= leaseTime) {
                //达到续租条件
                leftTime = leftTime + 1000 * 60 * 5L;
                //做expire
                jedis.pexpire(ticket, leftTime);

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

}
