package cn.smbms.controller;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;
import com.alibaba.fastjson.JSONArray;
import com.sun.org.glassfish.external.probe.provider.annotations.ProbeProvider;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Mr.Zhou
 * @Date 2020/4/5
 * @Explain:
 */
@Controller
public class UserController {
    //log4j日志
    private Logger log = Logger.getLogger(UserController.class);

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;

    @RequestMapping(value = "/login.html")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/dologin.html", method = RequestMethod.POST)
    public String doLogin(@RequestParam String userCode,
                          @RequestParam String userPassword,
                          HttpServletRequest request,
                          HttpSession session) {
        log.info("userCode=====================" + userCode);
        log.info("userPassword=====================" + userPassword);

        //调用service方法，进行用户匹配
        User user = userService.login(userCode, userPassword);
        if (user != null) {//登录成功
            session.setAttribute(Constants.USER_SESSION, user);
            return "redirect:/sys/main.html";
        } else {
            request.setAttribute("error", "用户名或密码输入错误");
            return "login";
        }
    }

    @RequestMapping(value = "/logout.html")
    public String logout(HttpSession session) {
        session.removeAttribute(Constants.USER_SESSION);
        return "login";
    }

    @RequestMapping(value = "/sys/main.html")
    public String main(HttpSession session) {
        if (session.getAttribute(Constants.USER_SESSION) == null) {
            log.info("==========================================session NULL==========================================");
            return "redirect:/login.html";
        }
        return "frame";
    }

    @RequestMapping(value = "/user/list.html")
    public String userList(Model model, @RequestParam(value = "queryname", required = false) String queryname,
                           @RequestParam(value = "queryUserRole", required = false) String queryUserRole,
                           @RequestParam(value = "pageIndex", required = false) String pageIndex) {
        log.info("queryname=============================" + queryname);
        log.info("queryUserRole=============================" + queryUserRole);
        log.info("pageIndex=============================" + pageIndex);
        int _queryUserRole = 0;
        List<User> users = null;
        //设置页面容量
        int pageSize = Constants.pageSize;
        //设置当前页码
        int currentPageNo = 1;
        if (queryname == null) {
            queryname = "";
        }
        if (queryUserRole != null && !queryUserRole.equals("")) {
            _queryUserRole = Integer.parseInt(queryUserRole);
        }
        if (pageIndex != null) {
            currentPageNo = Integer.parseInt(pageIndex);
        }
        //总数据量
        int totalCount = userService.getUserCount(queryname, _queryUserRole);
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);
        int totalPageCount = pageSupport.getTotalPageCount();
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        users = userService.getUserList(queryname, _queryUserRole, currentPageNo, pageSize);
        List<Role> roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);
        model.addAttribute("userList", users);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        model.addAttribute("queryUserName", queryname);
        model.addAttribute("queryUserRole", queryUserRole);
        return "userlist";
    }

    @RequestMapping(value = "/user/view.json", method = RequestMethod.GET)
    @ResponseBody
    public Object view(@RequestParam(value = "id") String id) {
        log.info("id===========================" + id);
        String str = "";
        User user = null;
        if (id == null || "".equals(id)) {
            return "nodata";
        } else {
            try {
                user = userService.getUserById(Integer.parseInt(id));
                if (user != null) {
                    str = JSONArray.toJSONString(user);
                    log.info("str===============" + str);
                } else {
                    return "nodata";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
        }
        return str;
    }

    /**
     * 修改用户信息
     */
    @RequestMapping(value = "/user/usermodify.html/{userid}")
    public String userModify(@PathVariable String userid, Model model) {
        log.info("userid=========================" + userid);
        if (userid != null && !userid.equals("")) {
            User user = null;
            try {
                user = userService.getUserById(Integer.parseInt(userid));
                if (user != null) {
                    model.addAttribute("user", user);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "异常";
            }

        }
        return "usermodify";
    }

    @RequestMapping(value = "/user/usermodifysave.html", method = RequestMethod.POST)
    public String userModifySave(User user) {
        if (user != null) {
            if (userService.userModifySave(user)) {
                return "redirect:/user/list.html";
            }
        }
        return "/user/usermodifysave.html";
    }

    /**
     * 删除用户
     *
     * @param method
     * @param uid
     * @return
     */
    @RequestMapping(value = "/deluser.json", method = RequestMethod.GET)
    @ResponseBody
    public Object delUser(@RequestParam String method,
                          @RequestParam String uid) {
        log.info("methon===========================" + method);
        log.info("uid===========================" + uid);
        User user = null;
        String file1 = null;
        String file2 = null;
        File file = null;
        String result = "delResult";
        Map<String, String> map = new HashMap<>();
        if (uid == null) {
            map.put(result, "notexist");
        } else {
            user = userService.getUserById(Integer.parseInt(uid));
            if (user == null) {
                map.put(result, "notexist");
            } else {
                if (user.getIdPicPath() != null) {//判断数据库中证件照是否为空
                    file1 = user.getIdPicPath();//如果不为空赋值给file1
                }
                if (user.getWorkPicPath() != null) {//判断数据库中工作证照片是否为空
                    file2 = user.getWorkPicPath();//如果不为空赋值给file2
                }
                if (userService.delUserName(Integer.parseInt(uid))) {
                    if (file1 != null) {//如果file1不为空
                        file = new File(file1);//创建一个File对象
                        log.info("file1=======================" + file);
                        if (file.exists()) {//如果file对象是文件 则调用delete方法删除
                            file.delete();
                        }
                    }
                    if (file2 != null) {//如果file2不为空
                        file = new File(file2);//创建一个File对象
                        log.info("file2=======================" + file);
                        if (file.exists()) {//如果file对象是文件 则调用delete方法删除
                            file.delete();
                        }
                    }
                    map.put(result, "true");
                } else {
                    map.put(result, "false");
                }
            }
        }
        return JSONArray.toJSONString(map);
    }

    /**
     * 添加用户
     */
    @RequestMapping(value = "/add.html")
    public String userAdd(@ModelAttribute("user") User user) {
        return "useradd";
    }

    @RequestMapping(value = "/user/add.html", method = RequestMethod.POST)
    public String userAddSave(User user, HttpSession session,
                              HttpServletRequest request,
                              @RequestParam(value = "attachs", required = false) MultipartFile[] attcah) {
        log.info("attcahs======================" + attcah);
        //定义文件路径
        String idPicPath = null;
        String wordPicPath = null;
        String errorInfo = null;
        boolean flag = true;
        String path = "E:\\Y2\\smbms\\src\\main\\webapp\\statics\\upload";
        for (int i = 0; i < attcah.length; i++) {
            MultipartFile multipartFile = attcah[i];
            if (!multipartFile.isEmpty()) {
                if (i == 0) {
                    errorInfo = "uploadFileError";
                } else if (i == 1) {
                    errorInfo = "uploadWpError";
                }
                //获取原文件名
                String oldFileName = multipartFile.getOriginalFilename();
                log.info("oldFileName===============" + oldFileName);
                //获取源文件后缀
                String prefix = FilenameUtils.getExtension(oldFileName);
                log.info("prefix==================" + prefix);
                //文件上传最大值
                int fileSize = 500000;
                log.info("fileSize====================" + fileSize);
                if (multipartFile.getSize() > fileSize) {
                    request.setAttribute(errorInfo, "上传文件不能大于500kb");
                    flag = false;
                } else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                        || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")) {
                    //生成新的文件名
                    String fileName = null;
                    if (i == 0) {
                        fileName = System.currentTimeMillis() + RandomUtils.nextInt(10000) + "_Personal.jpg";
                    } else if (i == 1) {
                        fileName = System.currentTimeMillis() + RandomUtils.nextInt(10000) + "_Work.jpg";
                    }
                    log.info("fileName=========================" + fileName);
                    File targetFile = new File(path, fileName);//放入路径和文件名
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }
                    //保存
                    try {
                        multipartFile.transferTo(new File(path + File.separator + fileName));
                    } catch (IOException e) {
                        e.printStackTrace();
                        request.setAttribute(errorInfo, "上传失败");
                        flag = false;
                    }
                    if (i == 0) {
                        idPicPath = path + File.separator + fileName;
                    } else if (i == 1) {
                        wordPicPath = path + File.separator + fileName;
                    }
                } else {
                    request.setAttribute(errorInfo, "图片格式错误!");
                    flag = false;
                }
            }
        }
        if (flag) {
            user.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
            user.setCreationDate(new Date());
            user.setIdPicPath(idPicPath);
            user.setWorkPicPath(wordPicPath);
            if (userService.addUser(user)) {
                return "redirect:/user/list.html";
            }
        }

        return "useradd";
    }

    @RequestMapping(value = "/user/ucexist.json", method = RequestMethod.GET)
    @ResponseBody
    public Object ucexist(@RequestParam(value = "userCode") String userCode) {
        Map<String, String> map = new HashMap<>();
        if (userCode != null && !"".equals(userCode)) {
            User user = userService.getUserByUserCode(userCode);
            if (user != null) {
                map.put("userCode", "exist");
            } else {
                map.put("userCode", "noexist");
            }
        }
        return JSONArray.toJSONString(map);
    }

    /**
     * 修改用户密码
     *
     * @return
     */
    @RequestMapping(value = "/sys/pwdmodify.html")
    public String pwdModify() {
        return "pwdmodify";
    }

    @RequestMapping(value = "/user/pwdsave.html", method = RequestMethod.POST)
    public String pwdSave(Model model,HttpSession session,
                          @RequestParam String oldpassword,
                          @RequestParam String newpassword,
                          @RequestParam String rnewpassword) {
        log.info("oldpassword=================="+oldpassword);
        log.info("newpassword=================="+newpassword);
        log.info("rnewpassword=================="+rnewpassword);
        String message="message";
        if (oldpassword != null &&((User)session.getAttribute(Constants.USER_SESSION)).getUserPassword().equals(oldpassword)) {
            if (newpassword != null && newpassword.length() >= 6 && newpassword.length() <= 12 && newpassword.equals(rnewpassword)) {
                if (userService.updatePassword(newpassword,((User)session.getAttribute(Constants.USER_SESSION)).getId())){
                    model.addAttribute(message,"修改成功，请从新登录");
                    return "redirect:/login.html";
                }else{
                    model.addAttribute(message,"修改失败");
                }
            }else{
                model.addAttribute(message,"新密码输入异常");
            }
        }else{
            model.addAttribute(message,"原密码输入为空，或原密码输入错误!");
        }
        return "pwdmodify";
    }

    @RequestMapping(value = "/user/pwdmodify.json", method = RequestMethod.POST)
    @ResponseBody
    public Object pwdUpdate(@RequestParam String oldpassword, HttpSession session) {
        String result = "result";
        Map<String, String> map = new HashMap<>();
        if (session.getAttribute(Constants.USER_SESSION) == null) {
            map.put(result, "sessionerror");
        } else if (oldpassword == null || "".equals(oldpassword)) {
            map.put(result, "error");
        } else if (((User) session.getAttribute(Constants.USER_SESSION)).getUserPassword().equals(oldpassword)) {
            map.put(result, "true");
        } else {
            map.put(result, "false");
        }
        return JSONArray.toJSONString(map);
    }
}
