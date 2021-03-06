package cn.com.winning.ssgj.domain;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

import java.util.Date;

import cn.com.winning.ssgj.domain.BaseDomain;

/**
 *
 *
 * @author SSGJ
 * @date 2018-01-18 10:11:47
 */

@Alias("sysProductDataInfo")
public class SysProductDataInfo extends BaseDomain implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * @val 产品ID/系统ID
     */
    private Long pdId;

    /**
     * @val 基础数据ID
     */
    private Long bdId;

    /**
     * @val 生效日期
     */
    private Date effectiveDate;

    /**
     * @val 失效日期
     */
    private Date expireDate;

    /**
     * @val 维护人员
     */
    private Long lastUpdator;

    /**
     * @val 维护时间
     */
    private java.sql.Timestamp lastUpdateTime;

    private String pdName;
    private String pdCode;
    private String dbName;
    private String bdName;
    private String bdCnName;
    private String lastUpdate;

    public SysProductDataInfo() {

    }

    /**
     * @val 产品ID/系统ID
     */
    public Long getPdId() {
        return pdId;
    }

    /**
     * @val 产品ID/系统ID
     */
    public void setPdId(Long pdId) {
        this.pdId = pdId;
    }

    /**
     * @val 基础数据ID
     */
    public Long getBdId() {
        return bdId;
    }

    /**
     * @val 基础数据ID
     */
    public void setBdId(Long bdId) {
        this.bdId = bdId;
    }

    /**
     * @val 生效日期
     */
    public Date getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * @val 生效日期
     */
    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    /**
     * @val 失效日期
     */
    public Date getExpireDate() {
        return expireDate;
    }

    /**
     * @val 失效日期
     */
    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    /**
     * @val 维护人员
     */
    public Long getLastUpdator() {
        return lastUpdator;
    }

    /**
     * @val 维护人员
     */
    public void setLastUpdator(Long lastUpdator) {
        this.lastUpdator = lastUpdator;
    }

    /**
     * @val 维护时间
     */
    public java.sql.Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * @val 维护时间
     */
    public void setLastUpdateTime(java.sql.Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getPdName() {
        return pdName;
    }

    public void setPdName(String pdName) {
        this.pdName = pdName;
    }

    public String getPdCode() {
        return pdCode;
    }

    public void setPdCode(String pdCode) {
        this.pdCode = pdCode;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getBdName() {
        return bdName;
    }

    public void setBdName(String bdName) {
        this.bdName = bdName;
    }

    public String getBdCnName() {
        return bdCnName;
    }

    public void setBdCnName(String bdCnName) {
        this.bdCnName = bdCnName;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}