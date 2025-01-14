package com.px.modules.aa.adminpurchasenote.service.impl;

import com.px.modules.aa.adminpurchasenote.entity.AdminRscode2;
import com.px.modules.aa.adminpurchasenote.mapper.AdminRscode2Mapper;
import com.px.modules.aa.adminpurchasenote.service.IAdminRscode2Service;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 出库溯源码
 * @@author 品讯科技
 * @Date:   2023-02-16
 * @Version: V1.0
 */
@Service
public class AdminRscode2ServiceImpl extends ServiceImpl<AdminRscode2Mapper, AdminRscode2> implements IAdminRscode2Service {
	
	@Autowired
	private AdminRscode2Mapper adminRscode2Mapper;
	
	@Override
	public List<AdminRscode2> selectByMainId(String mainId) {
		return adminRscode2Mapper.selectByMainId(mainId);
	}
}
