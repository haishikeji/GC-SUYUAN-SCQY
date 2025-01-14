package com.px.modules.aa.admincasehistory.service.impl;

import com.px.modules.aa.admincasehistory.entity.AdminGodownEntry;
import com.px.modules.aa.admincasehistory.entity.AdminRscode;
import com.px.modules.aa.admincasehistory.mapper.AdminRscodeMapper;
import com.px.modules.aa.admincasehistory.mapper.AdminGodownEntryMapper;
import com.px.modules.aa.admincasehistory.service.IAdminGodownEntryService;
import com.px.modules.aa.admincasehistory.service.IAdminRscodeService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 采购单
 * @@author 品讯科技
 * @Date:   2023-02-13
 * @Version: V1.0
 */
@Service
public class AdminGodownEntryServiceImpl extends ServiceImpl<AdminGodownEntryMapper, AdminGodownEntry> implements IAdminGodownEntryService {

	@Autowired
	private AdminGodownEntryMapper adminGodownEntryMapper;
	@Autowired
	private AdminRscodeMapper adminRscodeMapper;

	@Autowired
	private IAdminRscodeService adminRscodeService;

	@Override
	@Transactional
	public void saveMain(AdminGodownEntry adminGodownEntry, List<AdminRscode> adminRscodeList) {
		adminGodownEntryMapper.insert(adminGodownEntry);
		if(adminRscodeList!=null && adminRscodeList.size()>0) {
			for(AdminRscode entity:adminRscodeList) {
				//外键设置
				entity.setPurchaseNoteId(adminGodownEntry.getId());
				adminRscodeMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(AdminGodownEntry adminGodownEntry,List<AdminRscode> adminRscodeList) {
		adminGodownEntryMapper.updateById(adminGodownEntry);

		//1.先删除子表数据
		adminRscodeMapper.deleteByMainId(adminGodownEntry.getId());
		//2.子表数据重新插入
		if(adminRscodeList!=null && adminRscodeList.size()>0) {
			for(AdminRscode entity:adminRscodeList) {
				//外键设置
				entity.setPurchaseNoteId(adminGodownEntry.getId());
				//adminRscodeMapper.insert(entity);
			}
		}
		adminRscodeService.saveBatch(adminRscodeList);
	}

	@Override
	@Transactional
	public void delMain(String id) {
		adminRscodeMapper.deleteByMainId(id);
		adminGodownEntryMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			adminRscodeMapper.deleteByMainId(id.toString());
			adminGodownEntryMapper.deleteById(id);
		}
	}

}
