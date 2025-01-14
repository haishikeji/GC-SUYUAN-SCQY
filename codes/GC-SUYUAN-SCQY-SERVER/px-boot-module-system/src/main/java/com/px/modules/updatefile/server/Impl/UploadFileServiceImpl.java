package com.px.modules.updatefile.server.Impl;//package com.px.modules.updatefile.server.Impl;
//
//
//import lombok.extern.slf4j.Slf4j;
//import com.px.modules.updatefile.server.Global;
//import com.px.modules.updatefile.server.UploadFileServer;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//
//@Service
//@Slf4j
//public class UploadFileServiceImpl implements UploadFileServer {
//
//
//	@Override
//	/**
//	 * 上传附件示例
//	 * @return
//	 */
//	public Boolean uploadAuthAttachment(String customerId, MultipartFile[] files, HttpServletRequest request, HttpServletResponse response){
//		try{
//			/**
//			 * 省略附件上传前的业务处理逻辑
//			 * 比如结合传入的其他参数(例如当前的 customerId) 做一些数据有效性校验等
//			 */
//
//			//附件保存逻辑
//			String filename=null;
//			if(files==null||files.length<1){
//				throw new Exception("文件未上传");
//			}else{
//				String rootOriPath = Global.PARH;
//				//循环上传上来的附件数组，保存到服务器指定的路径下
//				for (int i = 0; i < files.length; i ++ ) {
//					MultipartFile myfile=files[i];
//					//服务器中存储的文件名
//					filename=new StringBuilder().append(System.nanoTime()).append(new Random().nextInt(100)).append(".").append(myfile.getContentType().substring(myfile.getContentType().indexOf("/") + 1)).toString();
//					File file = new File(rootOriPath);
//					if(!file.exists()){
//						file.mkdirs();
//					}
//					//文件保存至服务器指定路径下
//					org.apache.commons.io.FileUtils.copyInputStreamToFile(myfile.getInputStream(), new File(rootOriPath, filename));
//
//					/***
//					 * 省略上传后的逻辑，
//					 * 比如保存附件信息至附件表中，建立附件和我们业务的关联关系等
//					 *
//					 * MultipartFile 中可以获取到的一些常用的信息：
//					 * 文件大小：myfile.getSize()
//					 * 文件类型：myfile.getContentType()
//					 * 文件原始名称：myfile.getOriginalFilename()
//					 *
//					 * 其余信息的获取方法可以通过百度 MultipartFile 找到对应的Api
//					 */
//				}
//			}
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//	@Override
//	public String upload(MultipartFile[] multipartFiles, HttpServletRequest request) {
//		//设置当前日期
//		String uploaddate= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//		//设置文件上传保存文件路径：保存在项目运行目录下的uploadFile文件夹+当前日期
//		String savepath = request.getSession().getServletContext().getRealPath("/uploadFile/")+uploaddate;
////		String savepath = Global.PARH+"/"+uploaddate;
//		//创建文件夹,当文件夹不存在时，创建文件夹
//		File folder = new File(savepath);
//		if(!folder.isDirectory()){
//			folder.mkdir();
//		}
//		//建立一个listmap的返回参数
//		List<Map<String,Object>> listmap =new ArrayList<>();
//		//建立一个循环分别接收多文件
//		for(MultipartFile file:multipartFiles){
//			//重命名上传的文件,为避免重复,我们使用UUID对文件分别进行命名
//			String oldname=file.getOriginalFilename();//getOriginalFilename()获取文件名带后缀
//			//UUID去掉中间的"-",并将原文件后缀名加入新文件
//			String newname= UUID.randomUUID().toString().replace("-","")
//					+oldname.substring(oldname.lastIndexOf("."));
//			//建立每一个文件上传的返回参数
//			Map<String,Object> map=new HashMap<>();
//			//文件保存操作
//			try {
//				file.transferTo(new File(folder,newname));
//				//建立新文件路径,在前端可以直接访问如http://localhost:8080/uploadFile/2021-07-16/新文件名(带后缀)
//				String filepath=request.getScheme()+"://"+request.getServerName()+":"+
////						request.getServerPort()+"/"+uploaddate+"/"+newname;
//						request.getServerPort()+"/uploadFile/"+uploaddate+"/"+newname;
//				//写入返回参数
//				map.put("[oldname]",oldname);
//				map.put("[newname]",newname);
//				map.put("[filepath]",filepath);
//				map.put("[result]","成功!");
//				listmap.add(map);
//			}catch (IOException ex){
//				//操作失败报错并写入返回参数
//				ex.printStackTrace();
//				map.put("[oldname]",oldname);
//				map.put("[newname]",newname);
//				map.put("[filepath]","");
//				map.put("[result]","失败!");
//			}
//		}
//		//将执行结果返回
//		return listmap.toString();
//	}
//}
