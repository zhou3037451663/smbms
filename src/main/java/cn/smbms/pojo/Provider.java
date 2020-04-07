package cn.smbms.pojo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

/**
 * @Author: Mr.Zhou
 * @Date 2020/3/11
 * @Explain:
 */
public class Provider {
    private Integer id;//id
    private String proCode;//供应商编码
    private String proDesc;//供应商详细描述
    private String proContact;//供应商联系人
    private String proPhone;//联系电话
    private String proName;//供应商名称
    private String proAddress;//地址
    private String proFax;//传真
    private Integer createdBy;//创建者（userId）
    private Date creationDate;//创建时间
    private Date modifyDate;//更新时间
    private Integer modifyBy;//更新者（userId）
    private String companyLicPicPath;
    private String orgCodePicPath;

    public String getOrgCodePicPath() {
        return orgCodePicPath;
    }

    public void setOrgCodePicPath(String orgCodePicPath) {
        this.orgCodePicPath = orgCodePicPath;
    }

    public String getCompanyLicPicPath() {
        return companyLicPicPath;
    }

    public void setCompanyLicPicPath(String companyLicPicPath) {
        this.companyLicPicPath = companyLicPicPath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProCode() {
        return proCode;
    }

    public void setProCode(String proCode) {
        this.proCode = proCode;
    }

    public String getProDesc() {
        return proDesc;
    }

    public void setProDesc(String proDesc) {
        this.proDesc = proDesc;
    }

    public String getProContact() {
        return proContact;
    }

    public void setProContact(String proContact) {
        this.proContact = proContact;
    }

    public String getProPhone() {
        return proPhone;
    }

    public void setProPhone(String proPhone) {
        this.proPhone = proPhone;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProAddress() {
        return proAddress;
    }

    public void setProAddress(String proAddress) {
        this.proAddress = proAddress;
    }

    public String getProFax() {
        return proFax;
    }

    public void setProFax(String proFax) {
        this.proFax = proFax;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(Integer modifyBy) {
        this.modifyBy = modifyBy;
    }
}
