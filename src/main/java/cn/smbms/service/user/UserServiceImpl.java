package cn.smbms.service.user;

import cn.smbms.dao.UserMapper;
import cn.smbms.pojo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Mr.Zhou
 * @Date 2020/4/5
 * @Explain:
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User login(String userCode, String password) {
        User user = userMapper.login(userCode);
        if (null != user) {
            if (user.getUserPassword().equals(password)) {
               return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        currentPageNo=(currentPageNo-1)*pageSize;
        return userMapper.getUserList(queryUserName,queryUserRole,currentPageNo,pageSize);
    }

    @Override
    public int getUserCount(String queryUserName, int queryUserRole) {
        return userMapper.getUserCount(queryUserName,queryUserRole);
    }

    @Override
    public User getUserById(int id) {
        return userMapper.getUserById(id);
    }

    @Override
    public boolean userModifySave(User user) {
        return userMapper.userModifySave(user)>0?true:false;
    }

    @Override
    public boolean delUserName(int id) {
        return userMapper.delUserName(id)>0?true:false;
    }

    @Override
    public boolean addUser(User user) {
        return userMapper.addUser(user)>0?true:false;
    }

    @Override
    public User getUserByUserCode(String userCode) {
        return userMapper.getUserByUserCode(userCode);
    }

    @Override
    public boolean updatePassword(String password, int id) {
        return userMapper.updatePassword(password,id)>0?true:false;
    }
}
