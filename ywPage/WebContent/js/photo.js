function connectWebViewJavascriptBridge(callback) {
    if (window.WebViewJavascriptBridge) {
        callback(WebViewJavascriptBridge)
    } else {
        document.addEventListener(
            'WebViewJavascriptBridgeReady'
            , function() {
                callback(WebViewJavascriptBridge)
            },
            false
        );
    }
}

connectWebViewJavascriptBridge(function(bridge) {
	//初始化
    bridge.init(function(message, responseCallback) {
        var data = {
            'Javascript Responds': 'HelloWorld'
        };
        responseCallback(data);
    });
});
 

var UserID = getQueryVariable("UserID");
var Token = getQueryVariable("Token");
var PPID = getQueryVariable("PPID"); 
var ProID = getQueryVariable("ProID");
var PageMin = getQueryVariable("PageMin");
var RootCount = getQueryVariable("RootCount");
var CouID = -1;
var scale_width = 402.6;
var scale_height = 600;
var s_width;
var s_height;
var s_padding;
var imageNum = 0;



function getWidth(){
	
  	//获取屏幕宽度的百分之50
  	s_width = $(window).width()/2.5;
  	s_height = scale_width*s_width/scale_height;
  	
  	s_padding = ($(window).width()-(s_width*2))/4;
  	
  	
  	
}


function respHandle(type,data,imageId,photoNum){
	
	
	if(type == "photo"){
		
		
		
		var url = data;
		var img = new Image();
		
		
		
		img.src = url;
	    // 如果图片被缓存，则直接返回缓存数据
	    if(img.complete){
	    	getImg(url,img.width,img.height,imageId,photoNum);
	    }else{
	        img.onload = function(){
	            getImg(url,img.width,img.height,imageId,photoNum);
	        }
	    }
			
		
	    
		
	}else if(type == "addPhoto"){
		
		var url = data;
		var img = new Image();
		img.src = url;
	    // 如果图片被缓存，则直接返回缓存数据
	    if(img.complete){
	    	getImg(url,img.width,img.height,imageId,photoNum);
	    }else{
	        img.onload = function(){
	            getImg(url,img.width,img.height,imageId,photoNum);
	        }
	    }
		
		
	}else if(type == "delPhoto"){
		
		$("#img-box-"+imageId).remove();
		
		
	}else if((type == "submitPhoto")){
		
		/* var url = data;
		var img = new Image();
		img.src = url;
	    // 如果图片被缓存，则直接返回缓存数据
	    if(img.complete){
	    	getImg(url,img.width,img.height,imageId);
	    }else{
	        img.onload = function(){
	            getImg(url,img.width,img.height,imageId);
	        }
	    } */
		
		var url = data;
		var img = new Image();
		img.src = url;
	    // 如果图片被缓存，则直接返回缓存数据
	    if(img.complete){
	    	updateImg(url,img.width,img.height,imageId,photoNum);
	    }else{
	        img.onload = function(){
	        	updateImg(url,img.width,img.height,imageId,photoNum);
	        }
	    }
		
	}
	
	
}

function updateImg(url,width,height,imageId,photoNum){
	
	var imgBox;
	if(width > height){			
		
		var i_height = height*s_width/width;
		var t = (s_height-i_height)/2;
		
		$("#img-box-"+imageId).find(".new-img").css({"width":s_width+"px","height":s_width+"px"});
		$("#img-box-"+imageId).find(".new-img").find("div").css({"width":s_width+"px","height":s_height+"px"});
		$("#img-box-"+imageId).find("img").attr("src",url);
		
		
	}else{
		//计算处理后的图片高度，用于调整图片位置
		var i_height = width*s_width/height;
		var t = (s_height-i_height)/2;
		
		$("#img-box-"+imageId).find(".new-img").css({"height":s_width+"px","transform":"rotate(90deg)"});
		$("#img-box-"+imageId).find(".new-img").find("div").css({"width":s_height+"px","height":s_width+"px"});
		$("#img-box-"+imageId).find("img").attr("src",url);
		
	}
	
	$("#img-box-"+imageId+" .numSpan").text(photoNum);
	
}

function getImg(url,width,height,imageId,photoNum){
	
	
	var imgBox;
	//图片横竖参数
	var hor;
	if(width > height){			
		
		var i_height = height*s_width/width;
		var t = (s_height-i_height)/2;
		hor = 0;
		var imgBox = '<div hor='+hor+' style="padding:0 '+s_padding+'px;width:'+s_width+'px;" class="img-box" id="img-box-'+imageId+'"><div class="new-img" style="width:'+s_width+'px;height:'+s_width+'px">'
			+'<div onclick="toPhotoEdit('+imageId+')" style="width:'+s_width+'px;height:'+s_height+'px;"><img alt="" style="height: 100%;" src="'+url+'"></div></div><span class="numSpan">'+photoNum+'</span><span onclick="delPhoto('+imageId+')">删除</span></div>';
	}else{
		//计算处理后的图片高度，用于调整图片位置
		hor = 1;
		var i_height = width*s_width/height;
		var t = (s_height-i_height)/2;
		var imgBox = '<div hor='+hor+' style="padding:0 '+s_padding+'px;width:'+s_width+'px;" class="img-box" id="img-box-'+imageId+'"><div class="new-img" style="height:'+s_width+'px;transform: rotate(90deg);">'
			+'<div onclick="toPhotoEdit('+imageId+')" style="width:'+s_height+'px;height:'+s_width+'px;" ><img style="height: 100%;" alt="" src="'+url+'"></div></div><span  class="numSpan">'+photoNum+'</span><span onclick="delPhoto('+imageId+')">删除</span></div>';
		
	}
	
	$(".photoList").append(imgBox);
	imageNum++;
    $("#imageNum").text("已选"+imageNum+"张");
 	
    
    
}




var HuiFang={
		m_tishi :null,//全局变量 判断是否存在div,
		//提示div 等待2秒自动关闭
		Funtishi: function (content,type) {
			if (HuiFang.m_tishi == null) {
				HuiFang.m_tishi = '<div class="xiaoxikuang none" id="app_tishi" style="z-index:9999;left: 15%;width:70%;position: fixed;background:none;bottom:40%;"> <p class="app_tishi" style="background: none repeat scroll 0 0 #000; text-shadow:none;border-radius: 2vw;color: #fff; margin: 0 auto;padding: 3vw;text-align: center;width: 70%;opacity: 0.8; font-family:Microsoft YaHei;letter-spacing: 1vw;font-size: 5vw;"></p></div>';
				$(document.body).append(HuiFang.m_tishi);
			}
			$("#app_tishi").show();
			$(".app_tishi").html(content);
			if (type == 1){
				setTimeout('$("#app_tishi").fadeOut()', 1500);
			}
		},
}



function addImg() {
	
	if(os.isPhone){ 
		window.webkit.messageHandlers.addImg.postMessage("");
	}else if(os.isAndroid){
		window.WebViewJavascriptBridge.callHandler("addImg", ""
				, function(responseData) {
			console.log(responseData);
		});
	}
    
}

function toPhotoEdit(imageId){
	var delType = 0;
	if($(".img-box").length == 1){
		delType = 1;
	}
	var photoNum = $("#img-box-"+imageId).find(".numSpan").text();
	var data = {"imageId":imageId,"delType":delType,"photoNum":photoNum};
	if(os.isPhone){ 
		window.webkit.messageHandlers.toPhotoEdit.postMessage(data);
	}else if(os.isAndroid){
		window.WebViewJavascriptBridge.callHandler("toPhotoEdit", data
				, function(responseData) {
			console.log(responseData);
		});
	}
	
}


function picPreview(PicUrl){
	
	var picData = {"UserID":UserID,"Token":Token,"ProID":ProID,"PPID":PPID,"PicUrl":PicUrl,"CouID":CouID};
	
	mui.ajax(picPreviewUrl,{
		type: "POST",
		data:picData,
		async:false,
		dataType:'json',//服务器返回json格式数据			type:'post',//HTTP请求类型
		timeout:10000,//超时时间设置为10秒；
		success:function(data){
			console.log(data)
			
			if(data.code == 0){
				var BookID = data.data.BookID;
				
				window.location.href="order.html?UserID="+UserID+"&Token="+Token+"&BookID="+BookID+"&RootCount="+RootCount+"&CouID="+CouID+"&ProID="+ProID;
			}
		},
		error:function(xhr,type,errorThrown){
			
		}
	});
	
}

function SetProgress(progress) { 
  progress = progress.toFixed(2);
  if (progress) { 
   $("#progress > span").css("width", String(progress) + "%"); 
   $("#progress > span").html(String(progress) + "%"); 
  } 
} 

var HuiFang={
		m_tishi :null,//全局变量 判断是否存在div,
		//提示div 等待2秒自动关闭
		Funtishi: function (content, url) {
		if (HuiFang.m_tishi == null) {
		HuiFang.m_tishi = '<div class="xiaoxikuang none" id="app_tishi" style="z-index:9999;left: 15%;width:70%;position: fixed;background:none;bottom:40%;"> <p class="app_tishi" style="background: none repeat scroll 0 0 #000; text-shadow:none;border-radius: 2vw;color: #fff; margin: 0 auto;padding: 3vw;text-align: center;width: 70%;opacity: 0.8; font-family:Microsoft YaHei;letter-spacing: 1vw;font-size: 5vw;"></p></div>';
		$(document.body).append(HuiFang.m_tishi);
		}
		$("#app_tishi").show();
		},
}

$(function(){
	
	getWidth();
	
	  var i = "data:image/png;base64,AAABAAEAMDAAAAEAIACoJQAAFgAAACgAAAAwAAAAYAAAAAEAIAAAAAAAACQAAAAAAAAAAAAAAAAAAAAAAAD+/v4C/v7+Av7+/gD///8A/v7+Av///wD+/v4A////AP7+/gL+/v4A////Av///wD+/v4C////AP///wD+/v4C////AP///wD+/v4C////AP///wD///8C/v7+AP///wD///8C/v7+AP///wD///8C/v7+AP///wD6y2WL+tF3fP78+AT///8C/v7+AP///wD///8C/v7+AP///wD///8C/v7+AP///wD+/v4A////AP///wL+/v4A////AP///wL+/v4C////AP7+/gL///8A/v7+Av///wD+/v4A////AP7+/gD+/v4A/v7+Av///wD///8A/v7+Av///wD///8A/v7+Av///wD///8A/v7+Av///wD///8A////Av7+/gD///8A////Av7+/gD///8A////Av3z3Rr3rAr7+cBGs/7+/gL++OwM/evDOP747A7///8A////Av7+/gD///8A////Av7+/gD+/v4A////Av7+/gD+/v4A////Av7+/gD+/v4C/v7+Av7+/gD///8A/v7+Av///wD+/v4A////AP7+/gL+/v4A////Av///wD+/v4C////AP///wD+/v4C////AP///wD+/v4C////AP///wD///8C/v7+AP///wD///8C/v7+AP///wD///8C/v7+APvakmT3rAn//OKqSv3tyyr3sBbp9qsI//ewFe397Mcw/v7+AP///wD///8C/v7+AP///wD+/v4A////AP///wL+/v4A////AP///wL+/v4C////AP7+/gL///8A/v7+Av///wD+/v4A////AP7+/gD+/v4A/v7+Av///wD///8A/v7+Av///wD///8A/v7+Av///wD///8A/v7+Av///wD///8A////Av7+/gD///8A////Av7+/gD///8A////AvrOb4f3rAn//fDTHvnHWqH3rAn/9qsI//esCf/5xFCn////Av7+/gD///8A////Av7+/gD+/v4A////Av7+/gD+/v4A////Av7+/gD+/v4C/v7+Av7+/gD///8A/v7+Av///wD+/v4A////AP7+/gL+/v4A////Av///wD+/v4C////AP///wD+/v4C////AP///wD+/v4C////AP///wD///8C/v7+AP///wD///8C/v7+AP///wD///8C/v7+APrQdoH3rAn//evEMvnIXp33rAn/9qsI//esCf/5xFKl/v7+APziqUr++e0M/v7+AP///wD+/v4A////AP///wL+/v4A////AP///wL+/v4C////AP7+/gL///8A/v7+Av///wD+/v4A////AP7+/gD+/v4A/v7+Av///wD///8A/v7+Av///wD///8A/v7+Av///wD///8A/v7+Av///wD///8A////Av7+/gD///8A////Av7+/gD///8A////AvzgpVD3rAn/+s1sif3w0yb3sh3j9qsI//exGuX97csq/ezHMPetDPn5wkyn////Av7+/gD+/v4A////Av7+/gD+/v4A////Av7+/gD+/v4C/v7+Av7+/gD///8A/v7+Av///wD96sMy/vnuDP7+/gL+/v4A////Av///wD+/v4C////AP///wD+/v4C////AP///wD+/v4C////AP///wD///8C/v7+AP///wD///8C/v7+AP///wD///8C/v7+AP768Qj3shzh96wL+/vcmFr++vEI/ezJKv768gj8465E97Ea5/etDvP97cwo/vnuDP3qwzL+/v4A////AP///wL+/v4A////AP///wL+/v4C////AP7+/gL///8A/v7+Av736BD3sBfp97Mg3f3sxy7+/v4A/v7+Av///wD///8A/v7+Av///wD///8A/v7+Av///wD///8A/v7+Av///wD///8A////Av7+/gD///8A////Av7+/gD///8A////Av7+/gD85rc+960O9/esCf/3tibV+cNNrfe2J9P3rAn/964R8fzls0T97Mcu97Mg3/ewF+n+9+gQ////Av7+/gD+/v4A////Av7+/gD+/v4C/v7+Av7+/gD///8A/v7+Av7+/AL73Jla964R8/ewFOv98dYg////Av///wD+/v4C////AP///wD+/v4C////AP///wD+/v4C////AP///wD///8C/v7+AP///wD///8C/v7+AP///wD///8C/v7+AP///wD98NUi960M+fetDvX3rAv59qsI//euEe35yFyb/fLYHv3x1iD3sBTr964R8/vcmVr+/vwC////AP///wL+/v4A////AP///wL+/v4C////AP7+/gL///8A/erDMvzgpU7++O0M/OSwRPesCf34wEax/v7+Av///wD///8A/v7+Av///wD///8A/v7+Av///wD///8A/v7+Av///wD///8A////Av7+/gD///8A////Av7+/gD///8A////Av7+/gD5w06p9qsI//vZj2b++vAG/vXhEP79+QL///8A////AvjARrH3rAn9/OSwQv747Qz84KVO/erDMv7+/gD+/v4A////Av7+/gD+/v4C/v7+Av7+/gD60nt29qsI//esCf/3tSTd/vfnEvnCSq/2qwj//fLYHP///wD+/v4C////AP///wD+/v4C////AP///wD+/v4C////AP///wD///8C/v7+AP///wD///8C/v7+AP///wD///8C/v7+APzoujr3rAn/+Lcq0/789wT///8C/v7+AP///wD///8C/fLYHPesCf/5wkqv/vfnEve1JN32rAj/96sJ//rSe3b+/v4A////AP///wL+/v4C////AP79+gL3rhHt9qsI//esCf/2rAj/+9uUZvvXiXD2rAj//OGmTv///wD///8A/v7+Av///wD///8A/v7+Av///wD///8A/v7+Av///wD///8A////Av7+/gD///8A////Av7+/gD///8A/v78Avi6Msf3rAn//OOuRv7+/gD///8A////Av7+/gD///8A/OGmTvesCf/714tw+9mRZvesCf/2rAj/96sJ//euEe3+/foC////Av7+/gD+/v4C/v7+Av7+/AL3sx7d9qsI//esCf/2qwj/+9+hVPrUgHj2qwj//OCkUP///wD+/v4C////AP///wD+/v4C////AP///wD+/v4C////AP///wD///8C/v7+AP///wD///8C/v7+AP///wD///8C+92cWPesCf/4v0G1/v7+Av///wD///8C/v7+AP///wD///8C/OCkUPesCf/61IB4+9+hVPesCf/2rAj/96sJ//ezHt3+/vwC////AP///wL+/v4C////AP7+/gL85bJE97AX6fesCf/5xFGj/vnvCPi3K9H2rAj//OWzPv///wD///8A/v7+Av///wD///8A/v7+Av///wD///8A/v7+Av///wD///8A////Av7+/gD///8A////Av7+/gD++vEI97Mf3/esCvv97coq////Av7+/gD///8A////Av7+/gD///8A/OWzPvesCf/4uCzR/vnvCPnEUaP2rAj/97AX6fzls0T+/v4A////Av7+/gD+/v4C/fHXIPrReXr++OwM/vz4BP757gz+9eIU+cRQq/arCP/2qwj/960N9/rPcoH++vAK////AP///wD+/v4C////AP///wD+/v4C////AP///wD///8C/v7+AP///wD///8C/v7+AP///wD85bJA96wL+fnJYJf///8C/v7+AP///wD///8C/v7+AP768Ar6z3KB960N9/esCf/3rAn/+cRRqf714hT++e4M/vz4BP747Az60Xl6/fHXIP///wL+/v4C+s1sh/erCP/3rQ71+Lozx/i6Msn3rAv396wJ//i5Msf714lq97AW6/erCf/3sx/f/OCjUP7+/AL///8A/v7+Av///wD///8A/v7+Av///wD///8A////Av7+/gD///8A////Av7+/gD///8A/fDTJP725BT///8A////Av7+/gD+/vwC/OCjUPezH9/3rAn/97AW6/vXiWr4uTLH9qsI//esC/f4ujLJ+Lozx/etDvX2rAj/+s1sh/7+/gD+/v4C/vz4Avvenlb5wUmx97Ia3/ewFeX4vDu/+9mPaP789gb+/v4A/vbnEvnJYZX3rAn996wI//i+P7n97s4o////AP///wD+/v4C////AP///wD///8C/v7+AP///wD///8C/v7+AP///wD///8C/v7+AP///wD///8C/v7+AP3uzij4vj+59qsI//esCf35yWGV/vbnEv///wD+/PYG+9mPaPi8O7/3sBXl97Ia3/nBSbH73p5W/vz4Av///wL9894a+9aFevvblWD979Ik/v7+Av///wD+/v4A////AP7+/gD+/v4A/v7+Av///wD968Yw+Ls3w/erCf/3rQz5+96fVP///wD///8A/v7+Av///wD///8A////Av7+/gD///8A////Av7+/gD///8A////Av7+/gD///8A+96fVPetDPn3rAn/+Ls3w/3rxjD///8A////Av7+/gD///8A////Av7+/gD+/v4A////Av3v0CT725Vg+9aFev3z3hr857o89qsJ//esCf/3rAn/+L5At/3x1iL+/v4A////AP7+/gL+/v4A////Av///wD+/v4C/v78AvvcmFr3tSTV/vXhFv///wD+/v4C////AP///wD///8C/v7+AP///wD///8C/v7+AP///wD///8C/v7+AP///wD///8C/vXhFve1JNX73Jha/v78Av///wD///8C/v7+AP///wD///8C/v7+AP///wD98dYi+L5At/asCP/2rAj/96sJ//znujz++vEK/e7NJPzlskT5xleh96wJ//ewFOv97cwq////AP7+/gD+/v4A/v7+Av///wD///8A/v7+Av///wD+/foC/v7+Av///wD///8A/v7+Av///wD+9eEY+sxpjfe1JdP3sBXj+L09u/vdm1j+/v0C////Av7+/gD///8A////Av79+gL///8A////Av7+/gD///8A////Av7+/gD///8A////Av3tyir3sBTr96wJ//nGV6H85bJE/e7NJP768Qr+/v4C/Oe5PPvdmlr+9eMU+96dVvesCf34uC3N/v36Av7+/gL+/v4A////Av///wD+/v4C////AP///wD+/v4C////AP///wD+/v4C////AP3tyi73sRjp9qsI//esCf/3rAn/9qsI//esCf/5xlah/v36BP///wD///8C/v7+AP///wD///8C/v7+AP///wD///8C/v7+AP///wD///8C/v36Avi4Lc/3rAn9+96dVv714xT73Zpa/Oe5PP7+/gL60nx496wJ//erCP/3sRrn/fTfGvnEUKf2rAj//OOtQv746wj++OsE/vjrAv757wL///8A/v7+Av///wD///8A/v7+Av///wD///8A/vv0Bve1JNn3rAn/9qsI//esCf/3rAn/9qsI//esCf/3rAn/+9eKav7+/gD///8A////Av7+/gD///8A////Av7+/gD++e8C/vjrAv746wT++OsI/OOsQvesCf/5xFCn/fTfGvexGuf2rAj/96sJ//rSfHj3rxLp9qsJ//esCf/3rAn/+9iLcPvbl2T2qwj/96wJ//arCP/2qwj/96wI//erCf/2qwn996wI9/etDOP+/v4C////AP///wD+/v4C/OGoTverCf/3rAn/9qsI//esCf/3rAn/9qsI//esCf/3rAn/97Qg2f7+/gL///8C/v7+AP///wD3rQzj9qsI9/esCf33rAn/9qsI//esCf/3rAn/9qsI//esCf/725di+9iLcPasCP/2rAj/96sJ//evEun4tijR96wJ//erCP/3rAn/+92bWPvWhXL2rAj/+Lgu0/i8OMP4uTDF+Lkwxfi5MMn4uTDN+Lkw0fi5MMf///8A/v7+Av///wD///8A+tN+fverCf/3rAn/9qsI//esCf/3rAn/9qsI//esCf/3rAn/9qsI//746wz///8A////Av7+/gD4uTDH+Lkw0fi5MM34uTDJ+Lkwxfi5MMX4vDjD+Lgu0/esCf/71oVy+92bWPasCf/2rAj/96sJ//i2KNH86sE497Mg3fesCvn5xVWd/vnuCvi3KdP3rAn9/fHXIP7+/gL+/v4A////Av///wD+/v4C////AP///wD+/v4C////AP///wD+/v4C+tWCdverCf/3rAn/9qsI//esCf/3rAn/9qsI//esCf/3rAn/9qsI/f757gj///8C/v7+AP///wD///8C/v7+AP///wD///8C/v7+AP///wD///8C/fHXIPesCf34tynT/vnuCvnFVZ33rAr597Mg3fzqwTj+/v4C/v37Av789wb+9+kQ+cFIrfesCf/5yF6X////AP7+/gD+/v4A/v7+Av///wD///8A/v7+Av///wD///8A/v7+Av///wD///8A/Oi7OPerCf/3rAn/9qsI//esCf/3rAn/9qsI//esCf/3rAn/+Lo0xf7+/gL///8A////Av7+/gD///8A////Av7+/gD///8A////Av7+/gD///8A////AvnIXpf2rAj/+cFIrf736RD+/PcG/v37Av7+/gD97coo+s9xh/nGVqP3rxLv9qsI//nCS63++/QI////AP7+/gL+/v4A////Av///wD+/v4C////AP///wD+/v4C////AP///wD+/v4C/v79Avi/Qbf3rAn/9qsI//esCf/3rAn/9qsI//esCf/3rAn//OOuRv///wD///8C/v7+AP///wD///8C/v7+AP///wD///8C/v7+AP///wD///8C/v7+AP779Aj5wkut96sJ//evEu/5xlaj+s9xh/3tyij84aZK96wJ//erCPv4uzbF+96eWP79+wL+/v4A////AP7+/gD+/v4A/v7+Av///wD///8A/v79AvzjrUb71oZs/v7+Av///wD///8A/v7+Av746hL4vDrB9qsI//esCf/3rAn/9qsI//etDPn72Y9k////Av7+/gD///8A////AvvWhmz8461G/v79Av7+/gD///8A////Av7+/gD///8A////Av7+/gD+/fsC+96eWPi7NsX2rAj796sJ//zhpkr++/UG/fHXFv768gb///8A/v7+Av///wD+/v4A////AP7+/gL+/v4A////Av///wD97Mks+L5Bt/erCf/3rAv3/fPdFv///wD+/v4C////AP///wD+/foC/OClTvnKYpf5xFKl+tJ7fP3w1CD///8C/v7+AP///wD///8C/fPdFvesC/f3rAn/+L9Ct/3sySz///8C/v7+AP///wD///8C/v7+AP///wD+/v4A////AP///wL++vIG/fHXFv779Qb+/v4C/vfnEPvWhnD4wEW397Ym0fi7NcX6zWyL/fDUIv7+/gD+/v4A/vTgGPnIXZn3rAn99qwI//i6NcX86Ls4/v7+Av///wD///8A/v7+Av///wD///8A////Av7+/gD///8A////Av7+/gD///8A////Av7+/gD///8A/v7+Avzouzj4ujXF9qsI//esCf35yF2Z/vTgGP7+/gD///8A/fDUIvrNbIv4uzXF97Ym0fjARbf71oZw/vfnEP7+/gD++vEI97EZ4/esCf/3rhHv97Um0/evE+/2qwj/960N9/valGD60np6964P8/erCf/3tCHd+96dVv7+/AL+/v4C////AP///wD+/v4C////AP///wD///8C/v7+AP///wD///8C/v7+AP///wD///8C/v7+AP///wD///8C/v7+AP///wD+/vwC+96dVve0Id33rAn/964P8/rSenr72pRg960N9/esCf/3rxPv97Um0/euEe/2rAj/97Ea4/768gj+/v4C+9WFbPzgpU7+/PYE/v7+Av78+AT73ZpY964Q8fasCP/2rAj/968T7/rUgHT++/MI/v7+Av///wD///8A/v7+Av7+/QL6y2eL/evEMP///wD///8A////Av7+/gD///8A////Av7+/gD///8A/evEMPrLZ4v+/v0C////Av7+/gD///8A////Av778wj61IB0968T7/esCf/3rAn/964Q8fvdmlj+/PgE////Av789gT84KVO+9WFbP7+/gD+/v4C/v7+Av7+/AL6zGiJ960N8/e0I9n86L06+9+gVParCP/3tifT/vXkFP///wD+/v4C////AP///wD+/v4C////APven1b2qwn/+L08u/7+/gL///8C/v7+AP///wD///8C/v7+AP///wD+/v4C+L08u/esCf/73p9W/v7+AP///wD///8C/v7+AP///wD///8C/vXkFPe2J9P3rAn/+9+gUvzovTr3tCPZ960N8/rMaYn+/vwC////AP///wL+/v4C////APzjrUb3rAn/9qsI//esCf/3syDb/vz2AvevE+v3sx7d/v7+Av///wD///8A/v7+Av///wD///8A/vntDPeyHOP3rAv7/e3KLP///wD///8A/v7+AvevEtn4uzbB////Av7+/gD///8A/ezJLPesCvv3shzj/vntDP7+/gD///8A////Av7+/gD///8A////AvezHt33rxPr/vz2AvezINv2rAj/96sJ//asCf/8461G////Av7+/gD+/v4C/v7+Avvcl2L3rAn/9qsI//esCf/3rQz1/vz2AveyHN/3shzf////Av///wD+/v4C////AP///wD+/v4C+s1th/erCf/6y2aP////AP///wD///8C/v7+AvesCfH3shzZ/v7+AP///wD///8C/v7+APrLZo/3rAn/+s1th////wD///8C/v7+AP///wD///8C/v7+APeyHN/3shzd/vz2AvetDPX2rAj/96sJ//asCP/73Jdi////AP///wL+/v4C////AP714xT3sh3l9qsI//esCf/6y2WP/fHXHPesCf35wkqv/v7+Av///wD///8A/v7+Av///wD979Am96wL+fexGef+9+kQ/v7+Av///wD///8A/v7+AvesCe/3shzZ////Av7+/gD///8A////Av736RD3sRnn960M+f3v0Cb///8A////Av7+/gD///8A////AvnCSq/3rAn9/fLYHPrLZY/2rAj/96sJ//eyHeX+9eMU////Av7+/gD+/v4C/v7+Av7+/gD+9eMU+9qSYvziqkr+/fkC+cVTo/arCP/84adM////Av///wD+/v4C////AP7+/QL4vkC596wI//vcmFz+/v4C////AP///wD///8C/v7+AvesCe33shzd/v7+AP///wD///8C/v7+AP///wD73Jhe9qsI//i+QLn+/v0C/v7+AP///wD///8C/v7+APzhp0z3rAn/+cVUo/79+QL84qpK+9qSYv714xT+/v4A////AP///wL+/v4C////AP7+/gL///8A/v7+Av747A75yF2Z96wJ//nDT6n+/v0C/vntDPvdnFr6zm6J+s9zh/rPcYH3qwn/+Ls1w/79+gL///8A/v7+Av///wD///8A/v7+AvewFev3shzf////Av7+/gD///8A////Av7+/gD+/foC+Ls1w/esCf/6z3GB+s9zh/rObon73p1Y/vntDP7+/QL5w02p9qsI//nIXZn++OwO////Av7+/gD+/v4A////Av7+/gD+/v4C/v7+Av7+/gD///8A/v7+AvrRdn72qwj/+cJMq/779Aj73JlY97AV6/erCf/2qwn/96wI//erCf/2qwn//OClTP///wD+/v4C////AP///wD///8C/v7+APeyHOf3shzl/v7+AP///wD///8C/v7+AP///wD///8C/OClTPesCf/3rAn/9qsI//esCf/3rAn/97AV6/vcmVj++/QK+cJMq/esCf/60XZ+////AP///wL+/v4A////AP///wL+/v4C////AP7+/gL///8A/v7+Av747Q7736FQ/v78AvvWhWz2rAj/97Ic4/vXiGz96sM2/OSvRvnCTKv3qwn/97Um1/768Qr///8A/v7+Av///wD///8A////AveyHOP3shzr////Av7+/gD///8A////Av7+/gD++vEK97Um1/esCf/5wkyr/OSvRv3qwzb714hu97Ic4/esCf/71oVs/v78AvvfoVD++O0O////Av7+/gD+/v4A////Av7+/gD+/v4C/v7+Av7+/gD///8A/v7+Av///wD+/v4A/evFMPesC/v4uTHN/vblEvzmtUL5x1uj+s5whf746hD5ymOV96wI//vXimr+/v4C////AP///wD+/v4C/e7PJvewFen3rAnz/evFLv7+/QL///8C/v7+AP///wD714pq9qsI//nKY5X++OoQ+s5whfnHW6P85rVC/vblEvi5MM33rAv7/evGMP///wD+/v4A////AP///wL+/v4A////AP///wL+/v4C////AP7+/gL///8A/v7+Av///wD+/v4A/evFMPi5Mcf98NIk/ezHLvesC/v3qwn/9qwI//jARrH+9eIS96wK+/i/Q7f///8A/v7+Av736BD5w02p9qsI//esCf/3rAn/9qsI//i+Prn99N4a////Av7+/gD4v0G396wK+/714hL4wEax9qsI//esCf/3rAv7/ezHLv3w0iT4uTHH/evFMP7+/gD+/v4A////Av7+/gD+/v4A////Av7+/gD+/v4C/v7+Av7+/gD///8A/v7+Av///wD+/v4A////AP7+/QL+/v4A+9aHbPerCf/2qwn/96wI//esC/f+/fsC97Qh2fi2KdH+/v4C/vryCPi6Msn3rAn/+Lo1x/vXiW772Y5q+L4/ufesCf/3tSTZ/vfnEv///wD4tinR97Qh2f79+wL3rAv39qsI//esCf/3rAn/+9eIbP///wD+/v0C/v7+AP///wD+/v4A////AP///wL+/v4A////AP///wL+/v4C////AP7+/gL///8A/v7+Av///wD+/v4A////AP7+/gD+/v4A/erBNPerCf33qwn/9qwI//i+P7v+/vwC97AX5fi7N8P///8A+tF5everCf/6y2WR/vnuCvvcmGD72pNm/vfnEPrSeXb3rAn/+cpil/7+/gD4uzXD97EY5f7+/AL4vj+79qsI//esCf/3rAn9/erBNP7+/gD///8A////Av7+/gD+/v4A////Av7+/gD+/v4A////Av7+/gD+/v4C/v7+Av7+/gD///8A/v7+Av///wD+/v4A////AP7+/gL+/v4A////AvvfoVL4v0K1+chemf725Bb968Qu96wI//rObon+/fkC97AX5/e1JNn++/QG+L08vfesCf/3rAn/+LYo0/768gj4vDrB96wL9/736Q76zWyJ9qsI//3rxC7+9uQW+chemfi/QrX736FS/v7+AP///wD///8C/v7+AP///wD+/v4A////AP///wL+/v4A////AP///wL+/v4C////AP7+/gL///8A/v7+Av///wD+/v4A////AP7+/gD+/v4A/v7+Av///wD///8A/v7+Av///wD5xVSd96wJ/f3uzij97s4m9qwI//rReIH979Ig9qsI//esCf/3rAn/9qsI//zovTz72pRi9qsI//zls0L97s4o96wJ/fnGVp3///8A////Av7+/gD///8A////Av7+/gD///8A////Av7+/gD+/v4A////Av7+/gD+/v4A////Av7+/gD+/v4C/v7+Av7+/gD///8A/v7+Av///wD+/v4A////AP7+/gL+/v4A////Av///wD+/v4C////AP///wD++vII/OKrRv///wD86cA+96wI//valGD++OoO960O8/esCf/3rAn/96wK/f3x1iD85rZE9qsI//vdmlj///8C/OOsRv768gj///8C/v7+AP///wD///8C/v7+AP///wD///8C/v7+AP///wD+/v4A////AP///wL+/v4A////AP///wL+/v4C////AP7+/gL///8A/v7+Av///wD+/v4A////AP7+/gD+/v4A/v7+Av///wD///8A/v7+Av///wD///8A/v7+Av///wD+9+gQ/OGmSv3y2B7///8A/OGmTvezHt33sRjj+9uUYP7+/gD+9uYS/Oe6PP714hT///8A////Av7+/gD///8A////Av7+/gD///8A////Av7+/gD///8A////Av7+/gD+/v4A////Av7+/gD+/v4A////Av7+/gD////9//8AAP////z//wAA/////Y//AAD////5B/8AAP////kH/wAA/////Iz/AAD////8+f8AAPz///4DPwAA/n///gZ/AAD/P//8/P8AAPE///z8jwAA4b//+f2HAADhv//5/YcAAPE///P8jwAA/g//8/B/AACAR///4gEAAOHh//+HhwAA//j//x//AACH/v//f+EAAOP//D//xwAA+f/4D/+fAACJ//AP/5EAAAwB8AeAMAAADAHwB4AwAACJ//AH/5EAAPH/8Af/jwAAg//wD//BAACP//gf//EAAP/4/n8f/wAA4eH//4eHAACAx///4wEAAP4f3/v4fwAA4z/P8/zHAADhP555/IcAAOE/Hnj8hwAA4T8+fPyHAAD+fn5+fn8AAPxwfn4OPwAA/MD+fwM/AAD/nH5+Of8AAP8yfn5M/wAA/2E4HIb/AAD/4TGMh/8AAP/hM+SH/wAA//MkJM//AAD//mQ2f/8AAP//7Df//wAA///+f///AAA=";
	var j = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAJAAAACQCAMAAADQmBKKAAAAP1BMVEX39/fd3d0yMjHT09M9PTzy8vLr6+v19fX7+/s5OThYWFdpaWjl5eV4eHjFxcVJSUiTk5O4uLigoKCGhoWsrKyRGP89AAAACXBIWXMAAAsTAAALEwEAmpwYAAAGymlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS42LWMxNDUgNzkuMTYzNDk5LCAyMDE4LzA4LzEzLTE2OjQwOjIyICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6cGhvdG9zaG9wPSJodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RFdnQ9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZUV2ZW50IyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoV2luZG93cykiIHhtcDpDcmVhdGVEYXRlPSIyMDE5LTA4LTEwVDE3OjU4OjA5KzA4OjAwIiB4bXA6TW9kaWZ5RGF0ZT0iMjAxOS0wOC0xMFQxODowMzoxMCswODowMCIgeG1wOk1ldGFkYXRhRGF0ZT0iMjAxOS0wOC0xMFQxODowMzoxMCswODowMCIgZGM6Zm9ybWF0PSJpbWFnZS9wbmciIHBob3Rvc2hvcDpDb2xvck1vZGU9IjIiIHBob3Rvc2hvcDpJQ0NQcm9maWxlPSJzUkdCIElFQzYxOTY2LTIuMSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo0ZTZhZGFhMi02NmU2LTdlNGEtOTk1Yy1hMDg1Y2QxY2U0OTUiIHhtcE1NOkRvY3VtZW50SUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDpkMzFjMDAxOC1iOWQ5LTJmNGEtYjQyZS1mODkyM2NhZWY2N2IiIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo5ZDNlN2EwNC04NzcyLTc2NDQtYTA3YS05ZTM5MjQyODk5OWUiPiA8eG1wTU06SGlzdG9yeT4gPHJkZjpTZXE+IDxyZGY6bGkgc3RFdnQ6YWN0aW9uPSJjcmVhdGVkIiBzdEV2dDppbnN0YW5jZUlEPSJ4bXAuaWlkOjlkM2U3YTA0LTg3NzItNzY0NC1hMDdhLTllMzkyNDI4OTk5ZSIgc3RFdnQ6d2hlbj0iMjAxOS0wOC0xMFQxNzo1ODowOSswODowMCIgc3RFdnQ6c29mdHdhcmVBZ2VudD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTkgKFdpbmRvd3MpIi8+IDxyZGY6bGkgc3RFdnQ6YWN0aW9uPSJzYXZlZCIgc3RFdnQ6aW5zdGFuY2VJRD0ieG1wLmlpZDo2MGQxNjNiYi0zZjRlLTMyNDEtOWZhMy05OWI5NGEyNzA1NTgiIHN0RXZ0OndoZW49IjIwMTktMDgtMTBUMTg6MDM6MTArMDg6MDAiIHN0RXZ0OnNvZnR3YXJlQWdlbnQ9IkFkb2JlIFBob3Rvc2hvcCBDQyAyMDE5IChXaW5kb3dzKSIgc3RFdnQ6Y2hhbmdlZD0iLyIvPiA8cmRmOmxpIHN0RXZ0OmFjdGlvbj0ic2F2ZWQiIHN0RXZ0Omluc3RhbmNlSUQ9InhtcC5paWQ6NGU2YWRhYTItNjZlNi03ZTRhLTk5NWMtYTA4NWNkMWNlNDk1IiBzdEV2dDp3aGVuPSIyMDE5LTA4LTEwVDE4OjAzOjEwKzA4OjAwIiBzdEV2dDpzb2Z0d2FyZUFnZW50PSJBZG9iZSBQaG90b3Nob3AgQ0MgMjAxOSAoV2luZG93cykiIHN0RXZ0OmNoYW5nZWQ9Ii8iLz4gPC9yZGY6U2VxPiA8L3ht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";
	respHandle("photo",j,"1",1);
	respHandle("photo",j,"2",2);
	respHandle("photo",j,"3",3);
	respHandle("photo",j,"4",4);  
	respHandle("photo",j,"5",5);
	respHandle("photo",j,"6",1);
	respHandle("photo",j,"7",2);
	respHandle("photo",j,"8",3);     
	respHandle("photo",j,"9",1);
	respHandle("photo",j,"10",2);
	respHandle("photo",j,"11",3);
	respHandle("photo",j,"12",4);  
	respHandle("photo",j,"13",5);
	respHandle("photo",j,"14",1);
	respHandle("photo",j,"15",2);
	respHandle("photo",j,"16",3);     
	respHandle("photo",j,"17",1);
	respHandle("photo",j,"18",2);
	respHandle("photo",j,"19",3);
	respHandle("photo",j,"20",4);  
	respHandle("photo",j,"21",5);
	respHandle("photo",j,"22",1);
	respHandle("photo",j,"23",2);
	respHandle("photo",j,"24",3);     
	var imgList = new Array();
	
	var photoList;
	

	 $("#submitBtn").on("click",function(){
		 HuiFang.Funtishi("推荐码不能为空");
		 /*
		 if(confirm('确定要删除吗')==true){
			 
			 dec2();

		    }else{
		       return false;

		    }*/
		 
		 
	})
	
	function dec2(){
		 
		 var imgNum = $(".img-box").length;
		 /* window.location.href="https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?prepay_id=wx071438322624427fca814441fb663f0000&package=2241788934"; */
		  if(imgNum<PageMin){
			 alert("至少上传"+PageMin+"张");
		 }else{
			$(".progressDiv").css("display","block");
			var eachcount = 0;
			
			$(".img-box").each(function(){
				
		      var imgInfo = $(this).find("img").attr("src");
		      var hor = $(this).attr("hor");
		      var num = $(this).find(".numSpan").text();
		      imgInfo = imgInfo.replace(/^data:image\/\w+;base64,/, '');
		      
		      var imgData = {"photo":imgInfo,"hor":hor,"num":num,"full": 1};
		      imgList.push(imgData);
		      
		      
		      
		    });
			console.log(imgList);
			uploadImg(imgList,imgNum,eachcount);
		 }
		 
	 }
	
	function uploadImg(imgList,imgNum,eachcount){
		
		mui.ajax(imgDo,{
			type: "POST",
			data:{"imgInfo":imgList[eachcount].photo,"UserID":UserID,"Token":Token},
			async:true,
			dataType:'json',//服务器返回json格式数据			type:'post',//HTTP请求类型
			timeout:10000,//超时时间设置为10秒；
			success:function(data){
				
				console.log(data)
				
				
				if(data.code == 0){
					
					var mydata = data.data;
					/*var newarray
		            newarray = {
		              "photo": mydata.photo,
		              "num": imgData.num,
		              "hor": imgData.hor,
		              "full": 1 //等比例裁剪，1等比例缩放
		            }*/
					imgList[eachcount].photo = mydata.photo;
					SetProgress((100/imgNum)*(eachcount+1));
					//如果是最后一条数据，则跳转到订单页面
					if(eachcount >= imgNum-1){
						SetProgress(100);
						var PicUrl = JSON.stringify(imgList);
						
						
						picPreview(PicUrl);
						
					}else{
						uploadImg(imgList,imgNum,++eachcount);
					}
				}
			},
			error:function(xhr,type,errorThrown){
				
			}
		});
			
	}
	
	function dec(){
		 
		 var imgNum = $(".img-box").length;
		 /* window.location.href="https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?prepay_id=wx071438322624427fca814441fb663f0000&package=2241788934"; */
		  if(imgNum<PageMin){
			 alert("至少上传"+PageMin+"张");
		 }else{
			 
			var eachcount = 1;
			
			$(".img-box").each(function(){
				
		      var imgInfo = $(this).find("img").attr("src");
		      var hor = $(this).attr("hor");
		      var num = $(this).find(".numSpan").text();
		      
		      
		      imgInfo = imgInfo.replace(/^data:image\/\w+;base64,/, '');
		      
		      
		      mui.ajax(imgDo,{
					type: "POST",
					data:{"imgInfo":imgInfo,"UserID":UserID,"Token":Token},
					async:false,
					dataType:'json',//服务器返回json格式数据			type:'post',//HTTP请求类型
					timeout:10000,//超时时间设置为10秒；
					success:function(data){
						console.log(eachcount);
						console.log(data)
						
						
						if(data.code == 0){
							
							var mydata = data.data;
							var newarray
				            newarray = {
				              "photo": mydata.photo,
				              "num": num,
				              "hor": hor,
				              "full": 1 //等比例裁剪，1等比例缩放
				            }
							imgList.push(newarray);
							SetProgress((100/imgNum)*eachcount);
							//如果是最后一条数据，则跳转到订单页面
							if(++eachcount > imgNum){
								SetProgress(100);
								var PicUrl = JSON.stringify(imgList);
								
								
								picPreview(PicUrl);
								
							}
						}
					},
					error:function(xhr,type,errorThrown){
						
					}
				});
		      
		    });
			 
		 }
		 
	 }
	
	
})
  
