package cn.com.jchen.blog.controller;


import cn.com.jchen.blog.common.lang.Result;
import cn.com.jchen.blog.entity.Blog;
import cn.com.jchen.blog.service.IBlogService;
import cn.com.jchen.blog.utils.ShiroUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jchen
 * @since 2021-05-26
 */
@RestController
public class BlogController
{

    @Autowired
    IBlogService blogService;

    /**
     * 分页查询博客信息
     * @param currentPage 档期那页面
     * @return 分页数据
     */
    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage)
    {
        // 创建分页对象
        Page page = new Page(currentPage, 5);
        // 分页查询并过滤
        IPage<Blog> pageList = blogService.page(page, new QueryWrapper<Blog>().orderByDesc("created"));
        // 返回分页数据
        return Result.success(pageList);
    }

    /**
     * 查看某个博客信息
     * @param id 博客编码
     * @return 分页数据
     */
    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id") Integer id)
    {
        // 查询博客信息
        Blog blog = blogService.getById(id);
        // 判断博客是否存在
        Assert.notNull(blog, "该博客不存在！");
        // 返回博客信息
        return Result.success(blog);
    }

    /**
     * 编辑/添加博客信息
     * 必须登录后才有权限进行编辑或者添加
     * @param blog 修改后的博客信息
     * @return 编辑/添加结果
     */
    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog)
    {
        Blog temp = null;
        Long blogId = blog.getId();
        // 判断博客是否存在
        if (blog.getId() != null)
        {
            // 判断是否有权限编辑，用户只能编辑自己的博客信息
            temp = blogService.getById(blogId);
            Assert.isTrue(temp.getId() == ShiroUtil.getProfile().getId(), "没有权限编辑他人博客！");
        }
        else
        {
            temp = new Blog();
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setStatus(0);
            temp.setCreated(LocalDateTime.now());
        }

        // 复制相关参数，并忽略某些参数
        BeanUtils.copyProperties(blog, temp, "id", "userId", "status", "created");

        // 添加/保存博客信息
        blogService.save(temp);

        return Result.success(null);
    }
}
