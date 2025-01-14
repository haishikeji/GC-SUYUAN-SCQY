package com.px.modules.aa.adminrscode.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.px.modules.aa.admincasehistory.entity.AdminGodownEntry;
import com.px.modules.aa.adminpurchasenote.entity.AdminPurchaseNote;
import com.px.modules.aa.commodity.entity.AdminCommodity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value="溯源码对象", description="溯源码")
public class AdminRScodeVO {

    @ApiModelProperty(value = "id")
    private String id;

	@Excel(name = "溯源码", width = 15)
    @ApiModelProperty(value = "归属溯源码")
    private String retrospectiveSourceCode;

    @Excel(name = "供应商", width = 15)
    @ApiModelProperty(value = "供应商")
    private String supplier;



    @ApiModelProperty(value = "溯源码对应的商品信息")
    private AdminCommodity adminCommodity;

    @Excel(name = "商品名称", width = 15)
    @ApiModelProperty(value = "商品名称")
    private String cname;
    @Excel(name = "商品代码", width = 15)
    @ApiModelProperty(value = "商品代码")
    private String ccode;
    @Excel(name = "商品条码", width = 15)
    @ApiModelProperty(value = "商品条码")
    private String cbarcode;
    private String purchaseId;
    private String salesId;

    /**操作员*/
    @Excel(name = "操作员", width = 15)
    @ApiModelProperty(value = "操作员")
    private String createBy;

    /**创建时间*/
    @ApiModelProperty(value = "入库时间")
    private Date createTime;

    @ExcelCollection(name="销售单列表")
    @ApiModelProperty(value = "销售单列表")
    private List<AdminPurchaseNote> adminPurchaseNoteList;

    @ExcelCollection(name="采购单列表")
    @ApiModelProperty(value = "采购单列表")
    private List<AdminGodownEntry> adminGodownEntryList;

}
