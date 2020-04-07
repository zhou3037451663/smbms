package cn.smbms.service.bill;


import cn.smbms.dao.BillMapper;
import cn.smbms.pojo.Bill;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Mr.Zhou
 * @Date 2020/3/30
 * @Explain:
 */
@Service("billService")
public class BillServiceImpl implements BillService {
    @Resource
    private BillMapper billMapper;
    @Override
    public List<Bill> getBillList(String queryProductName, int queryProviderId, int queryIsPayment) {
        return billMapper.getBillList(queryProductName,queryProviderId,queryIsPayment);
    }

    @Override
    public Bill getBill(int id) {
        return billMapper.getBill(id);
    }

    @Override
    public Bill getBillByCodeName(String billCode) {
        return billMapper.getBillByCodeName(billCode);
    }

    @Override
    public boolean addBill(Bill bill) {
        return billMapper.addBill(bill)>0?true:false;
    }

    @Override
    public boolean delBill(int id) {
        return billMapper.delBill(id)>0?true:false;
    }

    @Override
    public Bill getBillById(int id) {
        return billMapper.getBillById(id);
    }

    @Override
    public boolean updateBill(Bill bill) {
        return billMapper.updateBill(bill)>0?true:false;
    }

}
