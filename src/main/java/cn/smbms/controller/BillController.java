package cn.smbms.controller;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.bill.BillService;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.tools.Constants;
import com.alibaba.fastjson.JSONArray;

import com.mysql.jdbc.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Mr.Zhou
 * @Date 2020/3/30
 * @Explain:
 */
@Controller
public class BillController {
    @Resource
    private ProviderService providerService;
    @Resource
    private BillService billService;
    private Logger log = Logger.getLogger(BillController.class);

    @RequestMapping(value = "/bill.html", params = {"method=query"})
    public String billDo(Model model, @RequestParam(value = "method", required = false) String method,
                         @RequestParam(value = "queryProductName", required = false) String queryProductName,
                         @RequestParam(value = "queryProviderId", required = false) String queryProviderId,
                         @RequestParam(value = "queryIsPayment", required = false) String queryIsPayment) {
        int _queryProviderId = 0;
        int _queryIsPayment = 0;
        if (queryProductName == null) {
            queryProductName = "";
        }
        if (queryProviderId != null && !queryProviderId.equals("")) {
            _queryProviderId = Integer.parseInt(queryProviderId);
        }
        if (queryIsPayment != null && !queryIsPayment.equals("")) {
            _queryIsPayment = Integer.parseInt(queryIsPayment);
        }
        List<Provider> providerList = providerService.getProviderProName();
        List<Bill> billList = billService.getBillList(queryProductName, _queryProviderId, _queryIsPayment);
        model.addAttribute("queryProductName", queryProductName);
        model.addAttribute("queryProviderId", queryProviderId);
        model.addAttribute("queryIsPayment", queryIsPayment);
        model.addAttribute("providerList", providerList);
        model.addAttribute("billList", billList);
        model.addAttribute("method", method);
        return "billlist";
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/billadd.html")
    public String billadd() {
        return "billadd";
    }

    @RequestMapping(value = "/add.html", method = RequestMethod.POST)
    public String billAdd(Bill bill) {
        log.info("bill=============================" + bill);
        if (bill != null) {
            if (billService.addBill(bill)) {
                return "redirect:/bill.html?method=query";
            } else {
                return "billadd";
            }
        }
        return "billadd";
    }

    @RequestMapping(value = "/selectbill.json", method = RequestMethod.GET)
    @ResponseBody
    public Object selectBill(@RequestParam String billCode) {
        log.info("billCode-*-*-*-*-*-*-*-*-*-*-*" + billCode);
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isNullOrEmpty(billCode)) {
            map.put("billCode", "null");
        } else {
            Bill bill = billService.getBillByCodeName(billCode);
            if (bill != null) {
                map.put("billCode", "exist");
            } else {
                map.put("billCode", "noexist");
            }
        }
        return JSONArray.toJSONString(map);
    }

    @RequestMapping(value = "/billproname.json", method = RequestMethod.GET)
    @ResponseBody
    public Object billProName(@RequestParam String method) {
        log.info("method==========================" + method);
        List<Provider> list = providerService.getProviderProName();
        method = JSONArray.toJSONString(list);
        return method;
    }

    /*删除*/
    @RequestMapping(value = "/delbill.json", method = RequestMethod.GET)
    @ResponseBody
    public Object delBill(@RequestParam String method,
                          @RequestParam String billid) {
        log.info("method==========================" + method);
        log.info("billid==========================" + billid);
        String delResult = "delResult";
        Map<String, String> map = new HashMap<>();
        if (billid != null && !"".equals(billid)) {
            Bill bill = billService.getBill(Integer.parseInt(billid));
            if (bill != null) {
                if (billService.delBill(Integer.parseInt(billid))) {
                    map.put(delResult, "true");
                } else {
                    map.put(delResult, "false");
                }
            } else {
                map.put(delResult, "notexist");
            }
        } else {
            map.put(delResult, "notexist");
        }
        return JSONArray.toJSONString(map);
    }

    @RequestMapping(value = "/bill.html/{billid}")
    public String billView(@PathVariable(value = "billid") String billid, Model model) {
        log.info("billid================" + billid);
        if (billid != null) {
            Bill bill = billService.getBillById(Integer.parseInt(billid));
            if (bill != null) {
                model.addAttribute("bill", bill);
            } else {
                model.addAttribute("bill", null);
            }
        }
        return "billview";
    }

    @RequestMapping(value = "/updatebill.html/{bid}")
    public String updateBill(@PathVariable String bid, Model model) {
        log.info("bid==============================" + bid);
        Bill bill = null;
        if (bid != null) {
            bill = billService.getBillById(Integer.parseInt(bid));
            if (bill != null) {
                model.addAttribute("bill", bill);
            }
        }
        return "billmodify";
    }

    @RequestMapping(value = "/jsp/bill.json", method = RequestMethod.GET)
    @ResponseBody
    public Object bill(@RequestParam String method) {
        log.info("method=====================" + method);
        String str = "";
        List<Provider> providers = providerService.getProviderProName();
        if (providers != null) {
            str = JSONArray.toJSONString(providers);
            log.info("str=======================" + str);
        } else {
            return "nostr";
        }
        return str;
    }

    @RequestMapping(value = "/billsave.html", method = RequestMethod.POST)
    public String billSave(Bill bill, HttpSession session) {
        if (bill != null) {
            bill.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
            bill.setCreationDate(new Date());
            if (billService.updateBill(bill)) {
                return "redirect:/bill.html?method=query";
            }
        }
        return "billmodify";
    }
}
