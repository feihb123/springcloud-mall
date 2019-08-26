package cn.datacharm.malluserservice.controller;


import cn.datacharm.malluserservice.service.UserService;
import cn.datacharm.pojo.User;
import cn.datacharm.utils.CookieUtils;
import cn.datacharm.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * description:
 *
 * @author Purity
 * @date 2019/08/19
 */
@RestController
@RequestMapping("/user/manage")
public class UserController {

    @Autowired
    private UserService userService;

    //校验用户名是否存在
    @RequestMapping("checkUserName")
    public SysResult checkUserName(String userName) {
        int exist = userService.checkUserName(userName);
        if (exist == 0) {
            return SysResult.ok();
        } else {
            return SysResult.build(201, "", null);
        }
    }

    //注册表单提交
    @RequestMapping("save")
    public SysResult saveUser(User user) {
        try {
            userService.saveUser(user);
            return SysResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return SysResult.build(201, "", null);
        }
    }

    //根据接口文件实现登录
    //用户存储在redis中
    @RequestMapping("login")
    public SysResult login(User user,
                           HttpServletRequest req,
                           HttpServletResponse res) {
        String ticket = userService.login(user);
        if ("".equals(ticket)) {
            //没有正确生成Redis的key说明登录失败
            return SysResult.build(201, "", null);
        } else {
            //TODO cookie存放一个值，EM_TICKET
            CookieUtils.setCookie(req, res, "EM_TICKET", ticket);
            return SysResult.ok();
        }
    }

    //用户登录状态的获取
    @RequestMapping("query/{ticket}")
    public SysResult queryUserJson(@PathVariable String ticket) {
        String userJson = userService.queryUserJson(ticket);
        //如果redis数据不存在,返回时null还是""
        if (userJson == null) {
            //登录状态不存在,不是ticket出错了,就是超时
            return SysResult.build(201, "", null);
        } else {
            //登录状态合法
            return SysResult.build(200, "ok", userJson);
        }
    }

}
