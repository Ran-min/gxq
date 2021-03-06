package com.hengyunsoft.platform.exchange.api.flow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * com.hengyunsoft.platform.exchange.api.flow.dto
 * gxqpt
 * 版权：中科恒运软件科技股份有限公司贵阳分公司
 * 描述:api需求申请查询条件Dto
 * 修改人：zhaopengfei
 * 修改时间：2018/4/25
 * 修改内容：
 */
@Data
public class ShareFLowDemandConditionDTO {
    @ApiModelProperty(value = "申请单位id")
    private String unitName;
    @ApiModelProperty(value = "申请开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date startTime;
    @ApiModelProperty(value = "申请结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date endTime;
    @ApiModelProperty(value = "审批状态(0：全部，1:通过，2：驳回)")
    private Integer handleStatus;


}
