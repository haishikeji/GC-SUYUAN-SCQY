package com.px.modules.aa.admincasehistory.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import com.px.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 采购单
 * @@author 品讯科技
 * @Date:   2023-02-13
 * @Version: V1.0
 */
@ApiModel(value="admin_godown_entry对象", description="采购单")
@Data
@TableName("admin_godown_entry")
public class AdminGodownEntry implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private String id;
	/**采购订单号*/
	@Excel(name = "采购订单号", width = 15)
    @ApiModelProperty(value = "采购订单号")
    private String purchaseOrderNumber;
	/**供应商*/
	@Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    private String supplier;
	/**仓库名称*/
	@Excel(name = "仓库名称", width = 15)
    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;
	/**订单类型*/
	@Excel(name = "订单类型", width = 15)
    @ApiModelProperty(value = "订单类型")
    private String orderType;
	/**采购单条码*/
	@Excel(name = "采购单条码", width = 15)
    @ApiModelProperty(value = "采购单条码")
    private String barCode;
	/**操作员*/
    @ApiModelProperty(value = "操作员")
    private String operator;
	/**操作员*/
    @Excel(name = "操作员", width = 15)
    @ApiModelProperty(value = "操作员")
    private String createBy;
	/**创建时间*/
    @Excel(name = "创建时间", width = 15,format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	/**修改人*/
    @ApiModelProperty(value = "修改人")
    private String updateBy;
	/**修改时间*/
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}
