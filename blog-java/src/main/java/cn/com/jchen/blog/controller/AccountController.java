package cn.com.jchen.blog.controller;

import cn.com.jchen.blog.common.dto.LoginDto;
import cn.com.jchen.blog.common.lang.Result;
import cn.com.jchen.blog.entity.User;
import cn.com.jchen.blog.service.IUserService;
import cn.com.jchen.blog.utils.JwtUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AccountController
{
    @Autowired
    IUserService userService;

    @Autowired
    JwtUtil jwtUtil;


    /**
     * 登录处理
     * @param loginDto
     * @return
     */
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response)
    {
        // 根据用户名查找用户信息
        User loginUser = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
        // 判断用户是否存在
        Assert.notNull(loginUser, "用户不存在");

        // 校验密码
        if (!loginUser.getPassword().equals(SecureUtil.md5(loginDto.getPassword())))
        {
            return Result.fail("密码不正确，请重新输入！");
        }

        // 通过JwtUtil成功token
        String token = jwtUtil.generateToken(loginUser.getId());
        // 将token设置到响应头
        response.setHeader("Authorization", token);
        response.setHeader("Access-control-Expose-Headers", "Authorization");

        // 将用户非敏感信息返回
        return Result.success(MapUtil.builder().put("id", loginUser.getId())
                .put("username", loginUser.getUsername())
                .put("email", loginUser.getEmail()).map());
    }

    /**
     * 退出
     * @return
     */
    @RequiresAuthentication
    @GetMapping("/logout")
    public Result logout()
    {
        SecurityUtils.getSubject().logout();
        return Result.success(null);
    }
}
