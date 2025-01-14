package com.px.modules.aa.adminpurchasenote.service;

import com.px.modules.aa.adminpurchasenote.entity.AdminRscode2;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 出库溯源码
 * @@author 品讯科技
 * @Date:   2023-02-16
 * @Version: V1.0
 */
public interface IAdminRscode2Service extends IService<AdminRscode2> {

	public List<AdminRscode2> selectByMainId(String mainId);
}
