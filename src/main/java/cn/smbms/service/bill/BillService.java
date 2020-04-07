package cn.smbms.service.bill;


import cn.smbms.pojo.Bill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Mr.Zhou
 * @Date 2020/3/30
 * @Explain:
 */
public interface BillService {
    /**
     * @param queryProductName 商品名称
     * @param queryProviderId  供应商id
     * @param queryIsPayment   是否付款
     * @return
     */
    public List<Bill> getBillList(String queryProductName, int queryProviderId, int queryIsPayment);

    /**
     * 查找商品详细信息
     *
     * @param id 商品id
     * @return
     */
    public Bill getBill(int id);

    public Bill getBillByCodeName(String billCode);

    /**
     * 商品表的添加
     *
     * @param bill
     * @return
     */
    public boolean addBill(Bill bill);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public boolean delBill(int id);

    public Bill getBillById(int id);
    public boolean updateBill(Bill bill);
}
