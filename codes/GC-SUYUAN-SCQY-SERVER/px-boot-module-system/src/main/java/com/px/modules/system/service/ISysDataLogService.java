package com.px.modules.system.service;

import com.px.modules.system.entity.SysDataLog;

import com.baomidou.mybatisplus.extension.service.IService;

public interface ISysDataLogService extends IService<SysDataLog> {
	
	/**
	 * 添加数据日志
	 * @param tableName
	 * @param dataId
	 * @param dataContent
	 */
	public void addDataLog(String tableName, String dataId, String dataContent);

}
