package com.px.modules.aa.adminpurchasenote.service.impl;

import com.px.modules.aa.adminpurchasenote.entity.AdminPurchaseNote;
import com.px.modules.aa.adminpurchasenote.entity.AdminRscode2;
import com.px.modules.aa.adminpurchasenote.mapper.AdminRscode2Mapper;
import com.px.modules.aa.adminpurchasenote.mapper.AdminPurchaseNoteMapper;
import com.px.modules.aa.adminpurchasenote.service.IAdminPurchaseNoteService;
import com.px.modules.aa.adminpurchasenote.service.IAdminRscode2Service;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 销售单
 * @@author 品讯科技
 * @Date:   2023-02-16
 * @Version: V1.0
 */
@Service
public class AdminPurchaseNoteServiceImpl extends ServiceImpl<AdminPurchaseNoteMapper, AdminPurchaseNote> implements IAdminPurchaseNoteService {

	@Autowired
	private AdminPurchaseNoteMapper adminPurchaseNoteMapper;
	@Autowired
	private AdminRscode2Mapper adminRscode2Mapper;

	@Autowired
	private IAdminRscode2Service adminRscode2Service;

	@Override
	@Transactional
	public void saveMain(AdminPurchaseNote adminPurchaseNote, List<AdminRscode2> adminRscode2List) {
		adminPurchaseNoteMapper.insert(adminPurchaseNote);
		if(adminRscode2List!=null && adminRscode2List.size()>0) {
			for(AdminRscode2 entity:adminRscode2List) {
				//外键设置
				entity.setSalesTicketId(adminPurchaseNote.getId());
				adminRscode2Mapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional
	public void updateMain(AdminPurchaseNote adminPurchaseNote,List<AdminRscode2> adminRscode2List) {
		adminPurchaseNoteMapper.updateById(adminPurchaseNote);

		//1.先删除子表数据
		adminRscode2Mapper.deleteByMainId(adminPurchaseNote.getId());

		//2.子表数据重新插入
		if(adminRscode2List!=null && adminRscode2List.size()>0) {
			for(AdminRscode2 entity:adminRscode2List) {
				//外键设置
				entity.setSalesTicketId(adminPurchaseNote.getId());
			}
			adminRscode2Service.saveBatch(adminRscode2List,1000);
		}
	}

	@Override
	@Transactional
	public void delMain(String id) {
		adminRscode2Mapper.deleteByMainId(id);
		adminPurchaseNoteMapper.deleteById(id);
	}

	@Override
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			adminRscode2Mapper.deleteByMainId(id.toString());
			adminPurchaseNoteMapper.deleteById(id);
		}
	}

}
