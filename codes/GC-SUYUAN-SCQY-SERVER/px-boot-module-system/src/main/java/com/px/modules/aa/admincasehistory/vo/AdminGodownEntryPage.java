package com.px.modules.aa.admincasehistory.vo;

import java.util.List;
import com.px.modules.aa.admincasehistory.entity.AdminGodownEntry;
import com.px.modules.aa.admincasehistory.entity.AdminRscode;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import com.px.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 采购单
 * @@author 品讯科技
 * @Date:   2023-02-13
 * @Version: V1.0
 */
@Data
@ApiModel(value="admin_godown_entryPage对象", description="采购单")
public class AdminGodownEntryPage {

	/**id*/
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
	@ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**修改人*/
	@ApiModelProperty(value = "修改人")
    private String updateBy;
	/**修改时间*/
	@ApiModelProperty(value = "修改时间")
    private Date updateTime;
	
	@ExcelCollection(name="溯源码")
	@ApiModelProperty(value = "溯源码")
	private List<AdminRscode> adminRscodeList;
	
}
