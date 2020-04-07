package cn.smbms.dao;

import cn.smbms.pojo.Provider;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Mr.Zhou
 * @Date 2020/4/6
 * @Explain:
 */
public interface ProviderMapper {

    public int getProviderCount(@Param("proCode")String proCode,
                                @Param("proName")String proName);
    public List<Provider>getProviderList(@Param("queryProCode")String queryProCode,
                                         @Param("queryProName")String queryProName,
                                         @Param("currentPageNo")Integer currentPageNo,
                                         @Param("pageSize")Integer pageSize);

    public Provider getProvider(@Param("id")Integer id);

    public int updateProvider(Provider provider);

    public int getProviderBill(@Param("id")Integer id);

    public int delProvider(@Param("id")Integer id);

    public int providerAdd(Provider provider);
    /**
     * 商品表中的下拉框
     */
    public List<Provider> getProviderProName();
}
