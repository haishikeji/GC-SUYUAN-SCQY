package com.px.modules.message.entity;

import com.px.common.system.base.entity.JeecgEntity;
import org.jeecgframework.poi.excel.annotation.Excel;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 消息模板
 * @@author 品讯科技
 * @Date:  2019-04-09
 * @Version: V1.0
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_sms_template")
public class SysMessageTemplate extends JeecgEntity{
	/**模板CODE*/
	@Excel(name = "模板CODE", width = 15)
	private String templateCode;
	/**模板标题*/
	@Excel(name = "模板标题", width = 30)
	private String templateName;
	/**模板内容*/
	@Excel(name = "模板内容", width = 50)
	private String templateContent;
	/**模板测试json*/
	@Excel(name = "模板测试json", width = 15)
	private String templateTestJson;
	/**模板类型*/
	@Excel(name = "模板类型", width = 15)
	private String templateType;
}
