import cn.smbms.pojo.User;
import cn.smbms.service.user.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

/**
 * @Author: Mr.Zhou
 * @Date 2020/4/5
 * @Explain:
 */
public class UserTest {
    public static void main(String[] args) {
        File file=new File("E:\\Y2\\smbms\\src\\main\\webapp\\statics\\upload","1586162509793_Personal.jpg");
        System.out.println(file);
        if (file.exists()){
            if (file.delete()) {
                System.out.println("删除成功");
            }
        }

    }


}
