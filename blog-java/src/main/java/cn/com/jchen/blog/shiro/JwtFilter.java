package cn.com.jchen.blog.shiro;

import cn.com.jchen.blog.common.lang.Result;
import cn.com.jchen.blog.utils.JwtUtil;
import cn.hutool.json.JSONUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends AuthenticatingFilter
{
    @Autowired
    JwtUtil jwtUtil;

    /**
     * 获取token对象
     * @param servletRequest 请求
     * @param servletResponse 响应
     * @return token对象
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception
    {
        // 将servletRequest转为HttpServletRequest对象
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 获取请求头信息
        String jwt = request.getHeader("Authorization");
        // 如果jwt是空的，则返回空
        if (StringUtils.isEmpty(jwt))
        {
            return null;
        }

        // 如果不是空，则返回Token对象
        return new JwtToken(jwt);
    }

    /**
     * 针对jwt进行相关校验
     * @param servletRequest 请求对象
     * @param servletResponse 响应对象
     * @return 是否校验通过；true-校验通过
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception
    {
        // 将servletRequest转为HttpServletRequest对象
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 获取请求头信息
        String jwt = request.getHeader("Authorization");
        // 如果jwt是空的，则直接放心，让controller通过注解进行权限控制
        if (StringUtils.isEmpty(jwt))
        {
            return true;
        }

        // 校验jwt
        Claims claim = jwtUtil.getClaimByToken(jwt);
        if (null == claim || jwtUtil.isTokenExpired(claim.getExpiration()))
        {
            // 抛出异常
            throw new ExpiredCredentialsException("token已失效，请重新登录！");
        }

        // 登录处理
        // 此处调用的ubject.login(token);会代理到AccountRealm.doGetAuthenticationInfo的方法实现中
        // 需要重写onLoginFailure方法来保证返回到前端的内容
        return executeLogin(servletRequest, servletResponse);
    }

    /**
     * 执行登录失败的处理，需要将错误信息以全局统一的消息格式返回给前端
     * 获取异常信息，并写回前端
     * @param token token对象
     * @param e 异常信息
     * @param request 请求对象
     * @param response 响应对象
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
                                     ServletResponse response)
    {
        // 获取异常信息
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        Result result = Result.fail(throwable.getMessage());
        // 错误信息
        String errMsg = JSONUtil.toJsonStr(result);

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        try
        {
            httpServletResponse.getWriter().print(errMsg);
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }

        return false;
    }

    /**
     * 增加跨域处理
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception
    {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
    }
}
