package cn.com.jchen.blog.service.impl;

import cn.com.jchen.blog.entity.Blog;
import cn.com.jchen.blog.mapper.BlogMapper;
import cn.com.jchen.blog.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jchen
 * @since 2021-05-26
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

}
