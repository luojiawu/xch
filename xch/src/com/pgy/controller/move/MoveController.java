package com.pgy.controller.move;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pgy.service.MoveService;
import com.xch.dto.M_Order;
import com.xch.dto.M_TemplateBook;

/**
 * h5
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/move")
public class MoveController {
	
	private static Logger logger = LogManager.getLogger(MoveController.class);
	
	MoveService moveService = null;
	
	@RequestMapping(value="/toMOrder")
	public String toMOrder() {
		return "m_order";
	}
	
	@ResponseBody
	@RequestMapping(value="/affirmMOrder")
	public String affirmMOrder(String orderId) {
		
		moveService = new MoveService();
		
		M_Order m_order = moveService.getMOrder(orderId);
		
		String code = "200";
		
		if(m_order == null) {
			code = "201";
		}
		
		return code;
	}
	
	@RequestMapping(value="/toTemplateBookList")
	public String toTemplateBookList(String orderId,Model data) {
		
		moveService = new MoveService();
		
		List<M_TemplateBook> m_TemplateBookList = moveService.getMTemplateBookList();
		
		M_Order m_Order = moveService.getMOrder(orderId);
		
		data.addAttribute("m_Order", m_Order);
		data.addAttribute("m_TemplateBookList", m_TemplateBookList);
		
		return "m_templateBookList";
	}
	
	@RequestMapping(value="/toTemplateBook")
	public String toTemplateBook(String orderId,int templateBookId) {
		
		
		
		
		
		
		return "m_templateBook";
	}
	
	@RequestMapping(value="/toUploadPhoto")
	public String toUploadPhoto() {
		
		
		
		
		return "uploadPhoto";
	}
	
	@ResponseBody
	@RequestMapping(value="/uploadPhoto")
	public String uploadPhoto(@RequestParam("file")MultipartFile [] files) {
		
		MultipartFile file = null;
		for(int i=0;i<files.length;i++) {
			file = files[i];
			String sourceName = file.getOriginalFilename(); 
			String path = "D:/upload/"+sourceName;
			/*String suffix = sourceName.substring(sourceName.lastIndexOf("."));
			
			String path = "D:/upload/"+Util.getStringRandom(16)+suffix;*/
			
			File upload = new File(path);
	        try {
	        	file.transferTo(upload);
	        } catch (IOException e) {
	        	logger.error("文件上传失败");
	        }
		}
		
		return "1";
	}
	
}
