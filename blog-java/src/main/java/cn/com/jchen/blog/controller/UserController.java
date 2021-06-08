package cn.com.jchen.blog.controller;


import cn.com.jchen.blog.common.lang.Result;
import cn.com.jchen.blog.entity.User;
import cn.com.jchen.blog.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jchen
 * @since 2021-05-26
 */
@RestController
@RequestMapping("/user")
public class UserController
{
    @Autowired
    IUserService userService;


    @RequiresAuthentication
    @GetMapping("/index")
    public Result query()
    {
        User user = userService.getById(1l);
        return Result.success(user);
    }

    /**
     * 保存一个用户信息
     * @return 保存结果
     */
    @PostMapping("/save")
    public Result save(@Validated @RequestBody User user)
    {
        // 先判断用户是否存在
        User userBean = userService.getById(user.getId());
        if (null != userBean)
        {
//            throw new
        }
        userService.save(user);
        return Result.success(user);
    }
}
