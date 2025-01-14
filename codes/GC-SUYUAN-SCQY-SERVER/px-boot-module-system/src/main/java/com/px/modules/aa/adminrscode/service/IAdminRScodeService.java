package com.px.modules.aa.adminrscode.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.px.modules.aa.adminqrcode.entity.AdminQrcode;
import com.px.modules.aa.adminqrcode.entity.AdminQrcodeVO;
import com.px.modules.aa.adminrscode.entity.AdminRScode;
import com.px.modules.aa.adminrscode.entity.AdminRScodeVO;

import java.util.List;


public interface IAdminRScodeService extends IService<AdminRScodeVO> {

    IPage<AdminRScodeVO> All(Page<AdminRScodeVO> page, QueryWrapper<AdminRScodeVO> queryWrapper);


    List<AdminRScodeVO> DYAll(QueryWrapper<AdminRScodeVO> queryWrapper);

    List<AdminRScode> DYAll2(QueryWrapper<AdminRScode> queryWrapper);


}
