package cn.smbms.controller;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;
import com.alibaba.fastjson.JSONArray;
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
 * @Date 2020/4/6
 * @Explain:
 */
@Controller
public class ProviderController {

    @Resource
    private ProviderService providerService;
    private Logger log = Logger.getLogger(ProviderController.class);

    @RequestMapping(value = "/provider/list.html")
    public String providerList(@RequestParam(value = "queryProCode", required = false) String queryProCode,
                               @RequestParam(value = "queryProName", required = false) String queryProName,
                               @RequestParam(value = "pageIndex", required = false) String pageIndex, Model model) {
        log.info("queryProCode========================" + queryProCode);
        log.info("queryProName========================" + queryProName);
        log.info("pageIndex========================" + pageIndex);
        List<Provider> list = null;
        //设置页面容量
        int pageSize = Constants.pageSize;
        //设置页码
        int currentPageNo = 1;
        if (pageIndex != null) {
            currentPageNo = Integer.parseInt(pageIndex);
        }
        if (queryProCode == null) {
            queryProCode = "";
        }
        if (queryProName == null) {
            queryProName = "";
        }
        //获取总数据量
        int totalCount = providerService.getProviderCount(queryProCode, queryProName);
        PageSupport pageSupport = new PageSupport();
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);
        pageSupport.setCurrentPageNo(currentPageNo);
        //获取总页数
        int totalPageCount = pageSupport.getTotalPageCount();
        //控制首页和尾页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        list = providerService.getProviderList(queryProCode, queryProName, currentPageNo, pageSize);
        model.addAttribute("queryProCode", queryProCode);
        model.addAttribute("queryProName", queryProName);
        model.addAttribute("providerList", list);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        return "providerlist";
    }

    @RequestMapping(value = "/provider/view/{proid}")
    public String providerView(@PathVariable(value = "proid") String proid, Model model) {
        log.info("proid==========================" + proid);
        log.info("proid=============================" + proid);
        Provider provider = null;
        if (proid != null) {
            provider = providerService.getProvider(Integer.parseInt(proid));
            if (provider != null) {
                String a = "\\" + "\\";
                String b = a.substring(0, 1);
                if (provider.getCompanyLicPicPath() != null && !"".equals(provider.getCompanyLicPicPath())) {
                    String[] paths = provider.getCompanyLicPicPath().split(b + File.separator);
                    log.info("--------------------view companyLicPicPath paths[paths.length-1]--------------------" + paths[paths.length - 1]);
                    provider.setCompanyLicPicPath("E:\\Y2\\smbms\\src\\main\\webapp\\statics\\providerUpload\\" + paths[paths.length - 1]);
                }
                if (provider.getOrgCodePicPath() != null && !"".equals(provider.getOrgCodePicPath())) {
                    String[] paths = provider.getOrgCodePicPath().split(b + File.separator);
                    log.info("--------------------view companyLicPicPath paths[paths.length-1]--------------------" + paths[paths.length - 1]);
                    provider.setOrgCodePicPath("E:\\Y2\\smbms\\src\\main\\webapp\\statics\\providerUpload\\" + paths[paths.length - 1]);
                }
                model.addAttribute("provider", provider);
            }
        }
        return "providerview";
    }

    @RequestMapping(value = "/sys/provider/modify/{proid}")
    public String providerModify(@PathVariable(value = "proid") String proid, Model model) {
        if (proid != null) {
            Provider provider = providerService.getProvider(Integer.parseInt(proid));
            if (provider != null) {
                model.addAttribute("provider", provider);
            }
        }
        return "providermodify";
    }

    @RequestMapping(value = "/sys/provider/modifysave.html", method = RequestMethod.POST)
    public String modifySave(HttpSession session, Provider provider, @RequestParam(value = "attachs", required = false) MultipartFile[] attach,
                             HttpServletRequest request) {
        log.info("provider============================" + provider);
        File file = null;
        //定义文件路径
        String companyLicPicPath = null;
        String orgCodePicPath = null;
        String errorInfo = null;
        boolean flag = true;
        if (provider != null) {
            if (attach != null) {
                String path = "E:\\Y2\\smbms_Test\\src\\main\\webapp\\statics\\providerUpload";
                for (int i = 0; i < attach.length; i++) {
                    MultipartFile multipartFiles = attach[i];
                    if (!multipartFiles.isEmpty()) {
                        if (i == 0) {
                            errorInfo = "uploadFileError";
                        } else if (i == 1) {
                            errorInfo = "uploadFileError";
                        }
                        String oldFileName = multipartFiles.getOriginalFilename();
                        log.info("oldFileName============" + oldFileName);
                        String prefix = FilenameUtils.getExtension(oldFileName);
                        log.info("prefix=======================" + prefix);
                        int fileSize = 500000;
                        if (multipartFiles.getSize() > fileSize) {
                            request.setAttribute(errorInfo, "文件不能大于500kb");
                            flag = false;
                        } else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                                || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")) {
                            String fileName = null;
                            if (i == 0) {
                                fileName = System.currentTimeMillis() + RandomUtils.nextInt(10000) + "companyLicPicPath.jpg";
                            } else if (i == 1) {
                                fileName = System.currentTimeMillis() + RandomUtils.nextInt(10000) + "orgCodePicPath.jpg";
                            }
                            log.info("fileName======================" + fileName);
                            File targetFile = new File(path, fileName);
                            if (!targetFile.getParentFile().exists()) {
                                targetFile.getParentFile().mkdirs();
                            }
                            try {
                                multipartFiles.transferTo(new File(path + File.separator + fileName));
                            } catch (IOException e) {
                                e.printStackTrace();
                                request.setAttribute(errorInfo, "上传失败!");
                                flag = false;
                            }
                            if (i == 0) {
                                companyLicPicPath = path + File.separator + fileName;
                            } else if (i == 1) {
                                orgCodePicPath = path + File.separator + fileName;
                            }
                        } else {
                            request.setAttribute(errorInfo, "文件格式错误!");
                            flag = false;
                        }
                    }
                }
            }

            if (flag) {
                provider.setCompanyLicPicPath(companyLicPicPath);
                provider.setOrgCodePicPath(orgCodePicPath);
                provider.setCreatedBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
                if (providerService.updateProvider(provider)) {
                    Provider provider1 = providerService.getProvider(provider.getId());
                    if (provider1.getCompanyLicPicPath() != null) {
                        String file1 = provider1.getCompanyLicPicPath();
                        file = new File(file1);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    if (provider1.getOrgCodePicPath() != null) {
                        String file1 = provider1.getOrgCodePicPath();
                        file = new File(file1);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    return "redirect:/provider/list.html";
                }
            }
        }

        return "providermodify";
    }

    @RequestMapping(value = "/jsp/provider.json", method = RequestMethod.GET)
    @ResponseBody
    public Object providerDel(@RequestParam String method, @RequestParam String proid) {
        log.info("proid==========================" + proid);
        Map<String, String> map = new HashMap<>();
        String delResult = "delResult";
        if (proid != null) {
            if (providerService.getProvider(Integer.parseInt(proid)) != null) {
                if (providerService.getProviderBill(Integer.parseInt(proid)) < 1) {
                    if (providerService.delProvider(Integer.parseInt(proid))) {
                        map.put(delResult, "true");
                    } else {
                        map.put(delResult, "false");
                    }
                }
            } else {
                map.put(delResult, "notexist");
            }
        }
        return JSONArray.toJSONString(map);
    }

    @RequestMapping(value = "/provider/add.html")
    public String providerAdd(@ModelAttribute("provider") Provider provider) {
        return "provideradd";
    }

    @RequestMapping(value = "/provider/addsave.html", method = RequestMethod.POST)
    public String providerAddSave(Provider provider, HttpServletRequest request,
                                  HttpSession session,
                                  @RequestParam(value = "attachs", required = false) MultipartFile[] attach) {
        log.info("provider==================" + provider);
        String companyLicPicPath=null;//企业营业执照
        String orgCodePicPath=null;//组织机构代码证
        String errorInfo=null;
        boolean flag=true;
        //文件保存地址
        String path="E:\\Y2\\smbms_Test\\src\\main\\webapp\\statics\\providerUpload";
        for (int i = 0; i <attach.length ; i++) {
            MultipartFile multipartFile=attach[i];
            if (!multipartFile.isEmpty()){
                if (i==0){
                    errorInfo="uploadFileError";
                }else if(i==1){
                    errorInfo="uploadOcError";
                }
                //获取原文件名
                String oldFileName=multipartFile.getOriginalFilename();
                log.info("oldFileName===================="+oldFileName);
                //获取文件后缀
                String prefix=FilenameUtils.getExtension(oldFileName);
                log.info("perfix===================="+prefix);
                //设置文件上传最大值
                int fileSize=500000;
                if (multipartFile.getSize()>fileSize){
                    request.setAttribute(errorInfo,"上传文件不能大于500kb");
                    flag=false;
                }else if (prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                        || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){
                    //生成新的文件名
                    String fileName=null;
                    if (i==0){
                        fileName=System.currentTimeMillis()+RandomUtils.nextInt(10000)+"_companyLicPicPath.jpg";
                    }else if (i==1){
                        fileName=System.currentTimeMillis()+RandomUtils.nextInt(10000)+"_orgCodePicPath.jpg";
                    }
                    log.info("fileName==================="+fileName);
                    File targetFile=new File(path,fileName);
                    if (!targetFile.exists()){
                        targetFile.mkdirs();
                    }
                    //保存
                    try {
                        multipartFile.transferTo(new File(path+File.separator+fileName));
                    } catch (IOException e) {
                        e.printStackTrace();
                        request.setAttribute(errorInfo,"上传失败!");
                        flag=false;
                    }
                    if (i==0){
                        companyLicPicPath=path+File.separator+fileName;
                    }else if (i==1){
                        orgCodePicPath=path+File.separator+fileName;
                    }
                }else{
                    request.setAttribute(errorInfo,"文件格式错误!");
                    flag=false;
                }
            }
        }
        if (flag){
            provider.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
            provider.setCreationDate(new Date());
            provider.setCompanyLicPicPath(companyLicPicPath);
            provider.setOrgCodePicPath(orgCodePicPath);
            if (providerService.providerAdd(provider)){
                return "redirect:/provider/list.html";
            }
        }
        return "provideradd";
    }
}
