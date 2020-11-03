var WxOpenID = getQueryVariable("WxOpenID");
var UserID = getQueryVariable("UserID");
var Token = getQueryVariable("Token");

$(function(){
	
	mui.init({
		swipeBack: true ,//启用右滑关闭功能
		
	});
	var linkinfo;
	
	getLinkinfo();
	function getLinkinfo(){	
		mui.ajax(linkinfoUrl+'?UserID='+UserID+'&Token='+Token,{
			dataType:'json',//服务器返回json格式数据
			type:'get',//HTTP请求类型
			timeout:10000,//超时时间设置为10秒；
			success:function(data){
				console.log(data)
				var mydata = data;
				if(mydata.code == 0){
					linkinfo = mydata.data;
					console.log("linkinfo",linkinfo)
					setLinkinfo();
				}
			},
			error:function(xhr,type,errorThrown){
				
			}
		});
	}
	
	function setLinkinfo(){
		
		
		mui.each(linkinfo,function(index,item){
			
			var siteDiv = '<div class="siteDiv"><div class="siteContent"  onclick="pickOrderLIID('+item.LIID+')"><div class="checkboxDiv">'
				+'<input type="checkbox" id="checkbox_a1" class="chk_1"/><label for="checkbox_a1"></label></div>'
				+'<div class="siteRight"><div class="siteContentTop"><span>收货人：'+item.Man+'</span>'
				+'<span style="float: right;margin-right: 4vw;">'+item.Phone+'</span></div>'
				+'<div class="siteContentBottom">收货地址：'+item.Province+item.City+item.District+item.Address+'</div></div></div>'
				+'<span onclick="delSiteBySiteId('+item.LIID+')"><img alt="" style="width: 4vw" src="../img/laji.png"><font>删除</font></span>'
				+'<span onclick="toSite('+item.LIID+')"><img alt="" style="width: 4vw" src="../img/bianji.png"><font>编辑</font></span></div>';
				
				
			$(".content").append(siteDiv);	
		})
		
	}
	
	
	
	$("#addSite").on("click",function(){
		
		window.location.href="site.html"+location.search+"&LIID=0";
		
	});
	
	
	
})




function toSite(LIID){
		
	window.location.href="site.html"+location.search+"&LIID="+LIID;
	
}

function pickOrderLIID(LIID){
	
	window.location.href="order.html"+location.search+"&LIID="+LIID;
	
}

function delSiteBySiteId(LIID){
	
	
	$.ajax({
		type: "GET",			
        url: linkinfoD+"?UserID="+UserID+"&Token="+Token+"&LIID="+LIID,
        async: false,
        dataType:"text",
        success: function (data) {
        	  
        	if(jQuery.parseJSON(data).code == 0){
        		
        		location.replace("siteList.html"+location.search);
        		
        	}
        	
        }	
    });  
	
	
}
