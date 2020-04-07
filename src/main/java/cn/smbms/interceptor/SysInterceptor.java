package cn.smbms.interceptor;

import cn.smbms.pojo.User;
import cn.smbms.tools.Constants;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Author: Mr.Zhou
 * @Date 2020/4/4
 * @Explain:
 */
public class SysInterceptor extends HandlerInterceptorAdapter {
    private Logger log = Logger.getLogger(SysInterceptor.class);

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        log.debug("==========SysInterceptor preHandle!==========");
        HttpSession session = request.getSession();
        User user=(User) session.getAttribute(Constants.USER_SESSION);
        if (null==user){
            response.sendRedirect(request.getContextPath()+"/401.jsp");
            return false;
        }
        return true;
    }
}
