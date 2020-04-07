package cn.smbms.service.provider;

import cn.smbms.pojo.Provider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Mr.Zhou
 * @Date 2020/4/6
 * @Explain:
 */
public interface ProviderService {
    public int getProviderCount(String proCode,String proName);
    public List<Provider> getProviderList(String queryProCode, String queryProName,
                                          int currentPageNo,
                                          int pageSize);

    public Provider getProvider(int id);

    public boolean updateProvider(Provider provider);

    public int getProviderBill(int id);
    public boolean delProvider(int id);
    public boolean providerAdd(Provider provider);
    public List<Provider> getProviderProName();
}
