package cn.com.jchen.blog.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class JwtToken implements AuthenticationToken
{
    public String token;

    public JwtToken(String token)
    {
        this.token = token;
    }

    @Override
    public String getPrincipal()
    {
        return token;
    }

    @Override
    public String getCredentials()
    {
        return token;
    }
}
