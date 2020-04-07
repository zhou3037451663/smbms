package cn.smbms.service.role;

import cn.smbms.dao.RoleMapper;
import cn.smbms.pojo.Role;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Mr.Zhou
 * @Date 2020/4/5
 * @Explain:
 */
@Service("roleService")
public class RoleServiceImpl implements RoleService {
@Resource
private RoleMapper roleMapper;
    @Override
    public List<Role> getRoleList() {
        return roleMapper.getRoleList();
    }
}
