package com.px.modules.aa.admincasehistory.service;

import com.px.modules.aa.admincasehistory.entity.AdminRscode;
import com.px.modules.aa.admincasehistory.entity.AdminGodownEntry;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 采购单
 * @@author 品讯科技
 * @Date:   2023-02-13
 * @Version: V1.0
 */
public interface IAdminGodownEntryService extends IService<AdminGodownEntry> {

	/**
	 * 添加一对多
	 * 
	 */
	public void saveMain(AdminGodownEntry adminGodownEntry, List<AdminRscode> adminRscodeList) ;
	
	/**
	 * 修改一对多
	 * 
	 */
	public void updateMain(AdminGodownEntry adminGodownEntry, List<AdminRscode> adminRscodeList);
	
	/**
	 * 删除一对多
	 */
	public void delMain(String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain(Collection<? extends Serializable> idList);
	
}
