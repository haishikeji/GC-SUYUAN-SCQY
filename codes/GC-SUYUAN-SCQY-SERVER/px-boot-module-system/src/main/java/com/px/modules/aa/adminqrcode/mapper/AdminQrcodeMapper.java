package com.px.modules.aa.adminqrcode.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import com.px.modules.aa.adminqrcode.entity.AdminQrcode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.px.modules.aa.adminqrcode.entity.AdminQrcodeVO;

/**
 * @Description: 二维码管理
 * @@author 品讯科技
 * @Date:   2023-02-16
 * @Version: V1.0
 */
public interface AdminQrcodeMapper extends BaseMapper<AdminQrcode> {

    IPage<AdminQrcodeVO> All(IPage<AdminQrcodeVO> page,
                             @Param(Constants.WRAPPER) Wrapper<AdminQrcodeVO> wrapper);

}
