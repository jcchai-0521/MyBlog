# SpringBoot+Vue前后端分离博客项目学习笔记

## 一、搭建SpringBoot工程

## 二、配置MyBatisPlus自动生成

## 三、测试功能的实现

## 四、统一结果封装

## 五、整合shiro+jwt，进行会话共享

### 流程

#### 一、添加引用

```xml
<dependency>
    <groupId>org.crazycake</groupId>
    <artifactId>shiro-redis-spring-boot-starter</artifactId>
    <version>3.2.1</version>
</dependency>
<!-- hutool工具类-->
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>5.3.3</version>
</dependency>
<!-- jwt -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
```

#### 二、开发ShiroConfig.java

```html
1.引入RedisSessionDAO和RedisCacheManager，为了解决shiro的权限数据和会话信息能保存到redis中，实现会话共享。
2.重写了SessionManager和DefaultWebSecurityManager，同时在DefaultWebSecurityManager中为了关闭shiro自带的session方式，我们需要设置为false，这样用户就不再能通过session方式登录shiro。后面将采用jwt凭证登录。
3.在ShiroFilterChainDefinition中，我们不再通过编码形式拦截Controller访问路径，而是所有的路由都需要经过JwtFilter这个过滤器，然后判断请求头中是否含有jwt的信息，有就登录，没有就跳过。跳过之后，有Controller中的shiro注解进行再次拦截，比如@RequiresAuthentication，这样控制权限访问。
```

#### 注意事项

```
如果使用了spring-boot-devtools包，需要在resources/META-INF/spring-devtools.properties
并增加内容：restart.include.shiro-redis=/shiro-[\\w-\\.]+jar
```

## 全局异常处理

