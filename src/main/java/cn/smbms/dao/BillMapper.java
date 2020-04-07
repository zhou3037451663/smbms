package cn.smbms.dao;

import cn.smbms.pojo.Bill;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Mr.Zhou
 * @Date 2020/3/30
 * @Explain:
 */
public interface BillMapper {
    public List<Bill> getBillList(@Param("queryProductName") String queryProductName,
                                  @Param("queryProviderId") Integer _queryProviderId,
                                  @Param("queryIsPayment") Integer queryIsPayment);

    /**
     * 查找商品详细信息
     * @param id 商品id
     * @return
     */
    public Bill getBill(@Param("id") Integer id);

    public Bill getBillByCodeName(@Param("billCode") String billCode);

    /**
     * 商品表的添加
     * @param bill
     * @return
     */
    public int addBill(Bill bill);



    /**
     * 删除
     * @param id
     * @return
     */
    public int delBill(@Param("id") Integer id);

    public Bill getBillById(@Param("id")Integer id);

    public int updateBill(Bill bill);
}
