package cn.com.jchen.blog.service.impl;

import cn.com.jchen.blog.entity.User;
import cn.com.jchen.blog.mapper.UserMapper;
import cn.com.jchen.blog.service.IUserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
