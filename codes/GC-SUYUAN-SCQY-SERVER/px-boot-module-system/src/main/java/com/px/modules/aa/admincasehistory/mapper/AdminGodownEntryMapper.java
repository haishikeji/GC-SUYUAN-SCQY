package com.px.modules.aa.admincasehistory.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import com.px.modules.aa.admincasehistory.entity.AdminGodownEntry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.px.modules.aa.admincasehistory.vo.AdminGodownEntryPage;

/**
 * @Description: 采购单
 * @@author 品讯科技
 * @Date:   2023-02-13
 * @Version: V1.0
 */
public interface AdminGodownEntryMapper extends BaseMapper<AdminGodownEntry> {

//    IPage<AdminGodownEntryPage> findAll(IPage<AdminGodownEntryPage> page,
//                                        @Param(Constants.WRAPPER) Wrapper<AdminGodownEntryPage> wrapper);

}
