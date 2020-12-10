package com.pgy.common;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传公共操作类
 * @author Administrator
 *
 */
public class FileUpload {
	
	
	
	
	/**
	 * 文件上传公共方法
	 * @param file
	 * @param session
	 * @return
	 * @throws IOException
	 */                                                             //对应上面的文件存储路径
	public static String imgsUpload(MultipartFile file, HttpSession session,String savePath)
			throws IOException {
		//获取一个文件夹的路径用于存储获取的图片，该路径需提交创建，否则需使用判断代码创建
		String path = session.getServletContext().getRealPath(savePath);
		//通过file获取上传文件名称
		String fileName = file.getOriginalFilename();
		File f = new File(path,fileName);
		if(!f.exists()){
		    f.mkdirs();
		}
		//进行文件存储
		file.transferTo(f);
		return savePath+fileName;
	}
	
	
	
	
}
