package cn.com.jchen.blog.utils;

import cn.com.jchen.blog.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

public class ShiroUtil
{
    /**
     * 获取当前的鉴权用户信息
     * @return 用户信息
     */
    public static AccountProfile getProfile()
    {
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }
}
