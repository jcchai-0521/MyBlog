package cn.com.jchen.blog.shiro;

import cn.com.jchen.blog.entity.User;
import cn.com.jchen.blog.service.IUserService;
import cn.com.jchen.blog.utils.JwtUtil;
import cn.hutool.core.bean.BeanUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRealm extends AuthorizingRealm
{
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    IUserService userService;


    /**
     * 判断是否是支持自定义的JwtToken
     * @param token token对象
     * @return true 表示当前token对象是自定义的JwtToken对象
     */
    @Override
    public boolean supports(AuthenticationToken token)
    {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection)
    {
        return null;
    }

    /**
     * 获取token信息
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException
    {
        // 将token转为自定义的JwtToken
        JwtToken jwtToken = (JwtToken) token;
        // 用户编码
        String userId = jwtUtil.getClaimByToken(jwtToken.getPrincipal()).getSubject();
        // 根据userId获取用户信息
        User currUser = userService.getById(Long.parseLong(userId));
        // 判断用户是否存在
        if (null == currUser)
        {
            throw new UnknownAccountException("账户不存在！");
        }

        // 判断用户状态
        if (-1 == currUser.getStatus())
        {
            throw new LockedAccountException("账户已被锁定！");
        }

        // 创建简单账户信息
        AccountProfile profile = new AccountProfile();
        // 使用工具类copy相应字段信息
        BeanUtil.copyProperties(currUser, profile);

        // 返回简单的鉴权信息，比如：用户名、邮箱等
        return new SimpleAuthenticationInfo(profile, jwtToken.getCredentials(), getName());
    }
}
