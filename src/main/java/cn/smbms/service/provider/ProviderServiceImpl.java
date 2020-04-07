package cn.smbms.service.provider;

import cn.smbms.dao.ProviderMapper;
import cn.smbms.pojo.Provider;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: Mr.Zhou
 * @Date 2020/4/6
 * @Explain:
 */
@Service("providerService")
public class ProviderServiceImpl implements ProviderService {

    @Resource
    private ProviderMapper providerMapper;
    @Override
    public int getProviderCount(String proCode, String proName) {
        return providerMapper.getProviderCount(proCode,proName);
    }

    @Override
    public List<Provider> getProviderList(String queryProCode, String queryProName, int currentPageNo, int pageSize) {
        currentPageNo=(currentPageNo-1)*pageSize;
        return providerMapper.getProviderList(queryProCode,queryProName,currentPageNo,pageSize);
    }

    @Override
    public Provider getProvider(int id) {
        return providerMapper.getProvider(id);
    }

    @Override
    public boolean updateProvider(Provider provider) {
        return providerMapper.updateProvider(provider)>0?true:false;
    }

    @Override
    public int getProviderBill(int id) {
        return providerMapper.getProviderBill(id);
    }

    @Override
    public boolean delProvider(int id) {
        return providerMapper.delProvider(id)>0?true:false;
    }

    @Override
    public boolean providerAdd(Provider provider) {
        return providerMapper.providerAdd(provider)>0?true:false;
    }

    @Override
    public List<Provider> getProviderProName() {
        return providerMapper.getProviderProName();
    }
}
