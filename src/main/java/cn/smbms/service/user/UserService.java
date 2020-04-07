package cn.smbms.service.user;

import cn.smbms.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Mr.Zhou
 * @Date 2020/4/5
 * @Explain:
 */
public interface UserService {
    /**
     * 用户登录查询
     *
     * @param userCode
     * @param password
     * @return
     */
    public User login(String userCode, String password);

    /**
     * 查询所有用户和分页
     *
     * @param queryUserName
     * @param queryUserRole
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    public List<User> getUserList(String queryUserName, int queryUserRole,
                                  int currentPageNo,
                                  int pageSize);

    /**
     * 查询总数
     *
     * @param queryUserName
     * @param queryUserRole
     * @return
     */
    public int getUserCount(String queryUserName, int queryUserRole);

    public User getUserById(int id);

    public boolean userModifySave(User user);

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    public boolean delUserName(int id);

    /*
       添加用户
     */
    public boolean addUser(User user);
    public User getUserByUserCode(String userCode);

    public boolean updatePassword(String password,int id);
}
