package com.px.modules.aa.adminqrcode.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.modules.aa.adminqrcode.entity.AdminQrcode;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.modules.aa.adminqrcode.entity.AdminQrcodeVO;

/**
 * @Description: 二维码管理
 * @@author 品讯科技
 * @Date:   2023-02-16
 * @Version: V1.0
 */
public interface IAdminQrcodeService extends IService<AdminQrcode> {

    IPage<AdminQrcodeVO> All(Page<AdminQrcodeVO> page, QueryWrapper<AdminQrcodeVO> queryWrapper);

}
