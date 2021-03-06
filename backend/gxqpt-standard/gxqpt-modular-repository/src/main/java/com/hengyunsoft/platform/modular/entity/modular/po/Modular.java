package com.hengyunsoft.platform.modular.entity.modular.po;

import com.hengyunsoft.base.entity.BaseEntity;
import com.hengyunsoft.validator.ValidatorGroups;
import com.hengyunsoft.validator.annotation.FieldDesc;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class Modular extends BaseEntity<Long> implements Serializable {
    /**
     * id
     *
     * @mbggenerated
     */
    @FieldDesc("id")
    @Range(min=Long.MIN_VALUE,max=Long.MAX_VALUE)
    @NotNull(groups=ValidatorGroups.MustNoneNull.class)
    private Long id;

    /**
     * 仓库名称
     *
     * @mbggenerated
     */
    @FieldDesc("仓库名称")
    @Length(max=64)
    private String repName;

    /**
     * 模块名称
     *
     * @mbggenerated
     */
    @FieldDesc("模块名称")
    @Length(max=64)
    private String modularName;

    /**
     * groupId
     *
     * @mbggenerated
     */
    @FieldDesc("groupId")
    @Length(max=64)
    private String groupId;

    /**
     * artifactId
     *
     * @mbggenerated
     */
    @FieldDesc("artifactId")
    @Length(max=64)
    private String artifactId;

    /**
     * 模块描述
     *
     * @mbggenerated
     */
    @FieldDesc("模块描述")
    @Length(max=255)
    private String description;

    /**
     * 上传人
     *
     * @mbggenerated
     */
    @FieldDesc("上传人")
    @Range(min=Long.MIN_VALUE,max=Long.MAX_VALUE)
    private Long createUser;

    /**
     * 上传用户姓名
     *
     * @mbggenerated
     */
    @FieldDesc("上传用户姓名")
    @Length(max=64)
    private String createUserName;

    /**
     * 更新人
     *
     * @mbggenerated
     */
    @FieldDesc("更新人")
    @Range(min=Long.MIN_VALUE,max=Long.MAX_VALUE)
    private Long updateUser;

    /**
     * 更新用户姓名
     *
     * @mbggenerated
     */
    @FieldDesc("更新用户姓名")
    @Length(max=64)
    private String updateUserName;

    /**
     * 上传时间
     *
     * @mbggenerated
     */
    @FieldDesc("上传时间")
    private Date createTime;

    /**
     * 更新时间
     *
     * @mbggenerated
     */
    @FieldDesc("更新时间")
    private Date updateTime;

    /**
     * 上传单位Id
     *
     * @mbggenerated
     */
    @FieldDesc("上传单位Id")
    @Range(min=Long.MIN_VALUE,max=Long.MAX_VALUE)
    private Long createCompanyId;

    /**
     * 上传单位名称
     *
     * @mbggenerated
     */
    @FieldDesc("上传单位名称")
    @Length(max=64)
    private String createCompanyName;
    /**
     * 模块版本
     *
     * @mbggenerated
     */
    @FieldDesc("模块版本")
    @Length(max=30)
    private String version;

    private static final long serialVersionUID = 1L;

    /**
     * id
     *
     * @mbggenerated
     */
    public Long getId() {
        return id;
    }

    /**
     * id
     *
     * @mbggenerated
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 仓库名称
     *
     * @mbggenerated
     */
    public String getRepName() {
        return repName;
    }

    /**
     * 仓库名称
     *
     * @mbggenerated
     */
    public void setRepName(String repName) {
        this.repName = repName == null ? null : repName.trim();
    }

    /**
     * 模块名称
     *
     * @mbggenerated
     */
    public String getModularName() {
        return modularName;
    }

    /**
     * 模块名称
     *
     * @mbggenerated
     */
    public void setModularName(String modularName) {
        this.modularName = modularName == null ? null : modularName.trim();
    }

    /**
     * groupId
     *
     * @mbggenerated
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * groupId
     *
     * @mbggenerated
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }

    /**
     * artifactId
     *
     * @mbggenerated
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * artifactId
     *
     * @mbggenerated
     */
    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId == null ? null : artifactId.trim();
    }

    /**
     * 模块描述
     *
     * @mbggenerated
     */
    public String getDescription() {
        return description;
    }

    /**
     * 模块描述
     *
     * @mbggenerated
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 上传人
     *
     * @mbggenerated
     */
    public Long getCreateUser() {
        return createUser;
    }

    /**
     * 上传人
     *
     * @mbggenerated
     */
    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    /**
     * 上传用户姓名
     *
     * @mbggenerated
     */
    public String getCreateUserName() {
        return createUserName;
    }

    /**
     * 上传用户姓名
     *
     * @mbggenerated
     */
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName == null ? null : createUserName.trim();
    }

    /**
     * 更新人
     *
     * @mbggenerated
     */
    public Long getUpdateUser() {
        return updateUser;
    }

    /**
     * 更新人
     *
     * @mbggenerated
     */
    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    /**
     * 更新用户姓名
     *
     * @mbggenerated
     */
    public String getUpdateUserName() {
        return updateUserName;
    }

    /**
     * 更新用户姓名
     *
     * @mbggenerated
     */
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName == null ? null : updateUserName.trim();
    }

    /**
     * 上传时间
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 上传时间
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 更新时间
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 更新时间
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 上传单位Id
     *
     * @mbggenerated
     */
    public Long getCreateCompanyId() {
        return createCompanyId;
    }

    /**
     * 上传单位Id
     *
     * @mbggenerated
     */
    public void setCreateCompanyId(Long createCompanyId) {
        this.createCompanyId = createCompanyId;
    }

    /**
     * 上传单位名称
     *
     * @mbggenerated
     */
    public String getCreateCompanyName() {
        return createCompanyName;
    }

    /**
     * 上传单位名称
     *
     * @mbggenerated
     */
    public void setCreateCompanyName(String createCompanyName) {
        this.createCompanyName = createCompanyName == null ? null : createCompanyName.trim();
    }
    /**
     * 模块版本
     *
     * @mbggenerated
     */
    public String getVersion() {
        return version;
    }
    /**
     * 模块版本
     *
     * @mbggenerated
     */
    public void setVersion(String version) {
        this.version = version;
    }
}