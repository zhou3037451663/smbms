package cn.smbms.dao;

import cn.smbms.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Mr.Zhou
 * @Date 2020/4/5
 * @Explain:
 */
public interface UserMapper {
    /**
     * 登录查询
     *
     * @param userCode
     * @return
     */
    public User login(@Param("userCode") String userCode);

    /**
     * 查询所有用户和分页
     *
     * @param queryUserName
     * @param queryUserRole
     * @param currentPageNo
     * @param pageSize
     * @return
     */
    public List<User> getUserList(@Param("queryUserName") String queryUserName,
                                  @Param("queryUserRole") Integer queryUserRole,
                                  @Param("currentPageNo") Integer currentPageNo,
                                  @Param("pageSize") Integer pageSize);

    public int getUserCount(@Param("queryUserName") String queryUserName, @Param("queryUserRole") Integer queryUserRole);

    public User getUserById(@Param("id") Integer id);

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    public int userModifySave(User user);

    /**
     * 删除用户
     * @param id
     * @return
     */
    public int delUserName(@Param("id") Integer id);
    /*
    添加用户
     */
    public int addUser(User user);

    public User getUserByUserCode(@Param("userCode")String userCode);

    public int updatePassword(@Param("password")String password,
                              @Param("id")Integer id);
}
