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
        //验证登录信息
        user.setUserPassword(MD5Util.md5(user.getUserPassword()));
        User exist=userMapper.queryExist(user);
        if(exist==null){
            //登录信息不对
            return "";
        }else{//登录成功
            //准备2个key值,userLoginLock ticket
            String userLoginLock=
                    "user_login_"+exist.getUserId()+".lock";
            String ticket=
                    "EM_TICKET"+System.currentTimeMillis()+exist.getUserId();
            //判断
            if(jedis.exists(userLoginLock)){
                //说明曾经有人登录过,而且没超时
                String oldTicket=jedis.get(userLoginLock);
                jedis.del(oldTicket);
            }//不存在顶替中的lock,说明是第一次登录
            jedis.setex(userLoginLock, 60*3, ticket);//超时应该
            //ticket userJson一致,并且开始存储这对key-value
            try{
                String userJson=MapperUtil.MP.writeValueAsString(exist);
                jedis.setex(ticket, 60*3, userJson);
                return ticket;
            }catch(Exception e){
                e.printStackTrace();
                return "";
            }
        }
    }

    public String queryUserJson(String ticket) {
        try{
            //判断超时时间剩余值
            Long leftTime=jedis.pttl(ticket);
            String userJson=jedis.get(ticket);
            User user=MapperUtil.MP.
                    readValue(userJson, User.class);
            Long leaseTime=1000*60*2l;
            if(leftTime<=leaseTime){//达到续租的条件
                //将剩余时间加上5分钟,做续租
                leftTime=leftTime+1000*60*5l;
                //做expire 对ticket和userLoginLock
                jedis.pexpire(ticket, leftTime);
                jedis.pexpire("user_login_"
                        +user.getUserId()+".lock", leftTime);
            }
            return userJson;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
