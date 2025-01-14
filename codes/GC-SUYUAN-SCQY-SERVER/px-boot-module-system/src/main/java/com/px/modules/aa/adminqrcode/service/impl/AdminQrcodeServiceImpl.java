package com.px.modules.aa.adminqrcode.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.px.modules.aa.adminqrcode.entity.AdminQrcode;
import com.px.modules.aa.adminqrcode.entity.AdminQrcodeVO;
import com.px.modules.aa.adminqrcode.mapper.AdminQrcodeMapper;
import com.px.modules.aa.adminqrcode.service.IAdminQrcodeService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;

/**
 * @Description: 二维码管理
 * @@author 品讯科技
 * @Date:   2023-02-16
 * @Version: V1.0
 */
@Service
public class AdminQrcodeServiceImpl extends ServiceImpl<AdminQrcodeMapper, AdminQrcode> implements IAdminQrcodeService {

    @Resource
    private AdminQrcodeMapper adminQrcodeMapper;


    public IPage<AdminQrcodeVO> All(Page<AdminQrcodeVO> page,
                                    QueryWrapper<AdminQrcodeVO> queryWrapper) {
        return adminQrcodeMapper.All(page,queryWrapper);
    }

}
