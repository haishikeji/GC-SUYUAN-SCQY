package com.px.modules.aa.adminqrcode.controller;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.px.common.api.vo.Result;
import com.px.common.system.query.QueryGenerator;
import com.px.common.util.oConvertUtils;
import com.px.modules.aa.admincasehistory.entity.AdminGodownEntry;
import com.px.modules.aa.admincasehistory.entity.AdminRscode;
import com.px.modules.aa.admincasehistory.service.IAdminGodownEntryService;
import com.px.modules.aa.admincasehistory.service.IAdminRscodeService;
import com.px.modules.aa.adminpurchasenote.entity.AdminPurchaseNote;
import com.px.modules.aa.adminpurchasenote.entity.AdminRscode2;
import com.px.modules.aa.adminpurchasenote.service.IAdminPurchaseNoteService;
import com.px.modules.aa.adminpurchasenote.service.IAdminRscode2Service;
import com.px.modules.aa.adminqrcode.entity.AdminQrcode;
import com.px.modules.aa.adminqrcode.entity.AdminQrcodeVO;
import com.px.modules.aa.adminqrcode.service.IAdminQrcodeService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import com.px.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.px.common.aspect.annotation.AutoLog;

/**
 * @Description: 二维码管理
 * @@author 品讯科技
 * @Date: 2023-02-16
 * @Version: V1.0
 */
@Api(tags = "二维码管理")
@RestController
@RequestMapping("/qrcode/adminQrcode")
@Slf4j
public class AdminQrcodeController extends JeecgController<AdminQrcode, IAdminQrcodeService> {
    @Autowired
    private IAdminQrcodeService adminQrcodeService;
    @Autowired
    private IAdminRscode2Service adminRscode2Service;
    @Autowired
    private IAdminRscodeService adminRscodeService;
    @Autowired
    private IAdminGodownEntryService adminGodownEntryService;
    @Autowired
    private IAdminPurchaseNoteService adminPurchaseNoteService;

    /**
     * 分页列表查询
     *
     * @param adminQrcode
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "二维码管理-分页列表查询")
//	@ApiOperation(value="二维码管理-分页列表查询", notes="二维码管理-分页列表查询")
//	@GetMapping(value = "/list")
//	public Result<IPage<AdminQrcode>> queryPageList(AdminQrcode adminQrcode,
//								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
//								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
//								   HttpServletRequest req,String ssretrospectiveSourceCode) {
//		QueryWrapper<AdminQrcode> queryWrapper = QueryGenerator.initQueryWrapper(adminQrcode, req.getParameterMap());
//		if (ssretrospectiveSourceCode != null) {
//			queryWrapper.like( "retrospective_source_code", ssretrospectiveSourceCode );
//		}
//		queryWrapper.eq( "isbottle_and_tetr", "1" );
//		Page<AdminQrcode> page = new Page<AdminQrcode>(pageNo, pageSize);
//		IPage<AdminQrcode> pageList = adminQrcodeService.page(page, queryWrapper);
//		return Result.OK(pageList);
//	}
    @ApiOperation(value = "二维码管理-分页列表查询", notes = "二维码管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<AdminQrcodeVO>> queryPageList(AdminQrcodeVO adminQrcode,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                      HttpServletRequest req, String ssretrospectiveSourceCode,
                                                      String a, String b,
                                                      String c, String d) {
        QueryWrapper<AdminQrcodeVO> queryWrapper = QueryGenerator.initQueryWrapper( adminQrcode, req.getParameterMap() );
        if (ssretrospectiveSourceCode != null) {
            queryWrapper.like( "admin_qrcode.retrospective_source_code", ssretrospectiveSourceCode );
        }
        if (a != null) {
            queryWrapper.like( "admin_godown_entry.purchase_order_number", a );
        }
        if (b != null) {
            queryWrapper.like( "admin_purchase_note.market_order_number", b );
        }
        if (c != null) {
            queryWrapper.like( "admin_rscode.name", c );
        }
        if (d != null) {
            queryWrapper.like( "admin_rscode.code", d );
        }
        queryWrapper.eq( "admin_qrcode.isbottle_and_tetr", "1" );
        Page<AdminQrcodeVO> page = new Page<AdminQrcodeVO>( pageNo, pageSize );
        IPage<AdminQrcodeVO> pageList = adminQrcodeService.All( page, queryWrapper );
        List<AdminQrcodeVO> records = pageList.getRecords();
        for (int i = 0; i < records.size(); i++) {
            //获取采购单（入库）
            QueryWrapper<AdminRscode> queryWrapperrs = new QueryWrapper<>();
            queryWrapperrs.eq( "retrospective_source_code", records.get( i ).getRetrospectiveSourceCode() );
            List<AdminRscode> listrs = adminRscodeService.list( queryWrapperrs );
            if (listrs.size() > 0) {
                List<String> listId = listrs.stream().map( AdminRscode::getPurchaseNoteId ).collect( Collectors.toList() );

                QueryWrapper<AdminGodownEntry> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.in( "id", listId );
                List<AdminGodownEntry> list = adminGodownEntryService.list( queryWrapper2 );

                pageList.getRecords().get( i ).setAdminGodownEntryList( list );
            }


            //获取销售单（出库）
            QueryWrapper<AdminRscode2> queryWrapperrs2 = new QueryWrapper<>();
            queryWrapperrs2.eq( "retrospective_source_code",
                    records.get( i ).getRetrospectiveSourceCode() +"-"+ records.get( i ).getBottleCode() );
            List<AdminRscode2> listrs2 = adminRscode2Service.list( queryWrapperrs2 );
            if (listrs2.size() > 0) {
                List<String> listId2 =
                        listrs2.stream().map( AdminRscode2::getSalesTicketId ).collect( Collectors.toList() );

                QueryWrapper<AdminPurchaseNote> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.in( "id", listId2 );
                List<AdminPurchaseNote> list1 = adminPurchaseNoteService.list( queryWrapper3 );

                pageList.getRecords().get( i ).setAdminPurchaseNoteList( list1 );
            }
        }
        return Result.OK( pageList );
    }
    @GetMapping(value = "/listByIds")
    public Result<List<AdminQrcode>> listByIds(@RequestParam String ids) {
        List<String> stringList = Arrays.asList( ids.split( "," ) );
        QueryWrapper<AdminQrcode> queryWrapper = new QueryWrapper<>(  );
        queryWrapper.in( "id", stringList );
        List<AdminQrcode> list = adminQrcodeService.list( queryWrapper );
        return Result.OK( list );
    }

    @GetMapping(value = "/list2")
    public Result<IPage<AdminQrcode>> queryPageList2(AdminQrcode adminQrcode,
                                                     @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                     HttpServletRequest req, String ssretrospectiveSourceCode) {
        QueryWrapper<AdminQrcode> queryWrapper = QueryGenerator.initQueryWrapper( adminQrcode, req.getParameterMap() );
        if (ssretrospectiveSourceCode != null) {
            queryWrapper.like( "retrospective_source_code", ssretrospectiveSourceCode );
        }
        queryWrapper.eq( "isbottle_and_tetr", "2" );
        Page<AdminQrcode> page = new Page<AdminQrcode>( pageNo, pageSize );
        IPage<AdminQrcode> pageList = adminQrcodeService.page( page, queryWrapper );
        return Result.OK( pageList );
    }


//    private static final String QR_CODE_IMAGE_PATH = "D:\\AJeecgBootFiles\\";
//	 private static final String QR_CODE_IMAGE_PATH = "/usr/java/AJeecgBootFiles/";
	 private static final String QR_CODE_IMAGE_PATH = "C:\\AJeecgBootFiles\\";

    private static void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        BitMatrix bitMatrix = qrCodeWriter.encode( text, BarcodeFormat.QR_CODE, width, height );

        Path path = FileSystems.getDefault().getPath( filePath );

        MatrixToImageWriter.writeToPath( bitMatrix, "PNG", path );

    }

    /**
     * 添加
     *
     * @param adminQrcode
     * @return
     */
    @AutoLog(value = "二维码管理-添加")
    @ApiOperation(value = "二维码管理-添加", notes = "二维码管理-添加")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody AdminQrcode adminQrcode) {

        List<String> result = Arrays.asList( adminQrcode.getBottleCode().split( "," ) );
        try {
            for (int i = 0; i < result.size(); i++) {
                AdminQrcode adminQrcode1 = new AdminQrcode();
                String fileName = adminQrcode.getRetrospectiveSourceCode() + "-" + result.get( i ) + "-" + System.currentTimeMillis() + ".png";
                String url = QR_CODE_IMAGE_PATH + fileName;

                generateQRCodeImage( adminQrcode.getRetrospectiveSourceCode() + "-" + result.get( i ), 400, 400,
                        url );
                adminQrcode1.setRetrospectiveSourceCode( adminQrcode.getRetrospectiveSourceCode() );
                adminQrcode1.setBottleCode( result.get( i ) );
                adminQrcode1.setQrcodePic( fileName );
                adminQrcode1.setShengchengTime( new Date() );
                adminQrcode1.setIsbottleAndTetr( "1" );
                adminQrcodeService.save( adminQrcode1 );
            }
        } catch (WriterException e) {
            System.out.println( "Could not generate QR Code, WriterException :: " + e.getMessage() );
        } catch (IOException e) {
            System.out.println( "Could not generate QR Code, IOException :: " + e.getMessage() );
        }
        return Result.OK( "添加成功！" );
    }

//	public Result<String> add(@RequestBody AdminQrcode adminQrcode) {
//
//		List<String> result = Arrays.asList(adminQrcode.getBottleCode().split(","));
//		String fileName ="";
//		try {
//		for (int i = 0;i<result.size();i++){
//			if (i == result.size()-1){
//				fileName =
//						fileName + adminQrcode.getRetrospectiveSourceCode()+"-"+result.get( i )+"-"+System.currentTimeMillis()+ ".png";
//			}else {
//				fileName =
//						fileName + adminQrcode.getRetrospectiveSourceCode()+"-"+result.get( i )+"-"+System.currentTimeMillis()+ ".png,";
//			}
//			String fileName2 = adminQrcode.getRetrospectiveSourceCode()+"-"+result.get( i )+"-"+System.currentTimeMillis()+ ".png";
//			String url = QR_CODE_IMAGE_PATH+fileName2;
//
//			generateQRCodeImage(adminQrcode.getRetrospectiveSourceCode()+"-"+result.get( i ), 350, 350,
//					url);
//		}
//			adminQrcode.setIsbottleAndTetr( "1" );
//			adminQrcode.setQrcodePic( fileName );
//			adminQrcode.setShengchengTime( new Date(  ) );
//			adminQrcodeService.save(adminQrcode);
//		} catch (WriterException e) {
//			System.out.println("Could not generate QR Code, WriterException :: " + e.getMessage());
//		} catch (IOException e) {
//			System.out.println("Could not generate QR Code, IOException :: " + e.getMessage());
//		}
//		return Result.OK("添加成功！");
//	}

    @PostMapping(value = "/add2")
    public Result<String> add2(@RequestBody AdminQrcode adminQrcode) {
        String fileName =
                adminQrcode.getRetrospectiveSourceCode() + "-" + System.currentTimeMillis() + ".png";
        String url = QR_CODE_IMAGE_PATH + fileName;
        try {
            generateQRCodeImage( adminQrcode.getRetrospectiveSourceCode(), 400, 400,
                    url );
            adminQrcode.setQrcodePic( fileName );
            adminQrcode.setShengchengTime( new Date() );
        } catch (WriterException e) {
            System.out.println( "Could not generate QR Code, WriterException :: " + e.getMessage() );
        } catch (IOException e) {
            System.out.println( "Could not generate QR Code, IOException :: " + e.getMessage() );
        }
        adminQrcode.setIsbottleAndTetr( "2" );
        adminQrcodeService.save( adminQrcode );
        return Result.OK( "添加成功！" );
    }

    /**
     * 编辑
     *
     * @param adminQrcode
     * @return
     */
    @AutoLog(value = "二维码管理-编辑")
    @ApiOperation(value = "二维码管理-编辑", notes = "二维码管理-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody AdminQrcode adminQrcode) {

        String fileName =
                adminQrcode.getRetrospectiveSourceCode() + "-" + adminQrcode.getBottleCode() + "-" + System.currentTimeMillis() + ".png";
        String url = QR_CODE_IMAGE_PATH + fileName;
        try {
            generateQRCodeImage( adminQrcode.getRetrospectiveSourceCode() + "-" + adminQrcode.getBottleCode(), 400, 400,
                    url );
            adminQrcode.setQrcodePic( fileName );
            adminQrcode.setShengchengTime( new Date() );
        } catch (WriterException e) {
            System.out.println( "Could not generate QR Code, WriterException :: " + e.getMessage() );
        } catch (IOException e) {
            System.out.println( "Could not generate QR Code, IOException :: " + e.getMessage() );
        }

        adminQrcodeService.updateById( adminQrcode );
        return Result.OK( "编辑成功!" );
    }

    @RequestMapping(value = "/edit2", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit2(@RequestBody AdminQrcode adminQrcode) {

        String fileName =
                adminQrcode.getRetrospectiveSourceCode() + System.currentTimeMillis() + ".png";
        String url = QR_CODE_IMAGE_PATH + fileName;
        try {
            generateQRCodeImage( adminQrcode.getRetrospectiveSourceCode(), 400, 400,
                    url );
            adminQrcode.setQrcodePic( fileName );
            adminQrcode.setShengchengTime( new Date() );
        } catch (WriterException e) {
            System.out.println( "Could not generate QR Code, WriterException :: " + e.getMessage() );
        } catch (IOException e) {
            System.out.println( "Could not generate QR Code, IOException :: " + e.getMessage() );
        }
        adminQrcodeService.updateById( adminQrcode );
        return Result.OK( "编辑成功!" );
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "二维码管理-通过id删除")
    @ApiOperation(value = "二维码管理-通过id删除", notes = "二维码管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        adminQrcodeService.removeById( id );
        return Result.OK( "删除成功!" );
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "二维码管理-批量删除")
    @ApiOperation(value = "二维码管理-批量删除", notes = "二维码管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.adminQrcodeService.removeByIds( Arrays.asList( ids.split( "," ) ) );
        return Result.OK( "批量删除成功!" );
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "二维码管理-通过id查询")
    @ApiOperation(value = "二维码管理-通过id查询", notes = "二维码管理-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<AdminQrcode> queryById(@RequestParam(name = "id", required = true) String id) {
        AdminQrcode adminQrcode = adminQrcodeService.getById( id );
        if (adminQrcode == null) {
            return Result.error( "未找到对应数据", null );
        }
        return Result.OK( adminQrcode );
    }

    /**
     * 导出excel
     *
     * @param request
     * @param adminQrcode
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, AdminQrcode adminQrcode) {
        return super.exportXls( request, adminQrcode, AdminQrcode.class, "二维码管理" );
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel( request, response, AdminQrcode.class );
    }

}
