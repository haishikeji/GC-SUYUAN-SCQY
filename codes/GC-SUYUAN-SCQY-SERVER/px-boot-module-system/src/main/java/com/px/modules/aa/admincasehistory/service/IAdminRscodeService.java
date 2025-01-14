package com.px.modules.aa.admincasehistory.service;

import com.px.modules.aa.admincasehistory.entity.AdminRscode;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 溯源码
 * @@author 品讯科技
 * @Date:   2023-02-13
 * @Version: V1.0
 */
public interface IAdminRscodeService extends IService<AdminRscode> {

	public List<AdminRscode> selectByMainId(String mainId);
}
