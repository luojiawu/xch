function imgCenter(){
	
	$(".orderDiv").each(function () {
		var img = $(this).find(".img_id");
		
		var src = $(this).find(".img_id").attr("src");
		var image = new Image();
		image.src = src;
	    var realWidth = 0;//储存图片实际宽度
	    var realHeight = 0;//储存图片实际高度
	    var o = document.getElementById("imgDiv");
	    var h = o.offsetHeight; //高度
	    var w = o.offsetWidth; //宽度
	    image.onload = function () {
	        realWidth = image.width;//获取图片实际宽度
	        realHeight = image.height;//获取图片实际高度
	        //让img的宽高相当于图片实际宽高的等比缩放，然后再偏移
	        if (realWidth > realHeight){
	            img.css("width",(h/realHeight)*realWidth);//等比缩放宽度
	            img.css("height",h);//跟div高度一致
	            img.css("left",'-' + ((h/realHeight)*realWidth-w)/2 + 'px');//设置图片相对自己位置偏移为img标签的宽度-高度的一半
	        }else if (realWidth < realHeight){
	            img.css("width",w);//跟div高度一致
	            img.css("height",(w/realWidth)*realHeight);//等比缩放高度
	            img.css("top",'-' + ((w/realWidth)*realHeight-h)/2 + 'px');//设置图片相对自己位置偏移为img标签的高度-宽度的一半
	        }else {
	            img.width =w ;
	            img.height = h;
	        }
	
	    }; 
		
	}) 
	
}






var UserID = getQueryVariable("UserID");
var Token = getQueryVariable("Token");
$(document).ready(function(e) { 
    var counter = 0;
    if (window.history && window.history.pushState) {
      $(window).on('popstate', function () {
        window.history.pushState('forward', null, '#');
        window.history.forward(1);
        window.location.href="index.html?UserID="+UserID+"&Token="+Token;//执行操作
      });
    }
    window.history.pushState('forward', null, '#'); //在IE中必须得有这两行
    window.history.forward(1);
});
$(function(){
	
	
	getOrders();
	
	
	
})

function getOrders(){
	mui.ajax(orders+'?UserID='+UserID+'&Token='+Token,{
		dataType:'json',//服务器返回json格式数据
		type:'get',//HTTP请求类型
		timeout:10000,//超时时间设置为10秒；
		success:function(data){
			console.log(data)
			var mydata = data;
			if(mydata.code == 0){
				
				Orders = mydata.data;
				
				console.log("Orders",Orders)
				setOrderList();
				
				
			}
		},
		error:function(xhr,type,errorThrown){
			
		}
	});
}


function setOrderList(){
	
	mui.each(Orders,function(index,item){
		var orderDiv;
		if(item.OrderStatusCS == "已关闭"){
			orderDiv = '<div class="orderDiv"><div class="orderContent"><div class="dateDiv">'+item.TimeCreate+'<span>'+item.OrderStatusCS+'</span></div>'
			+'<div class="orderDetails" onclick="toOrderDetails('+item.OrderID+')"><div id="imgDiv" ><img id="img_id" class="img_id" alt="" src="'+item.PhotoCover+'"></div>'
			+'<div class="leftDetails"><span style="color: black;font-size: 4vw">'+item.ProName+'</span>'
			+'</div><div class="rightDetails">X '+item.RootCount+'</div></div>'
			+'<div class="moneyDiv"><font style="color: black;">合计：￥'+item.MoneyPay+'元</font></div></div>'
			+'<span onclick="delOrder('+item.OrderID+',-1)">删除订单</span></div>';	
		}else if(item.OrderStatusCS == "未付款"){
			orderDiv = '<div class="orderDiv"><div class="orderContent"><div class="dateDiv">'+item.TimeCreate+'<span>'+item.OrderStatusCS+'</span></div>'
			+'<div class="orderDetails" onclick="toOrderDetails('+item.OrderID+')"><div id="imgDiv" ><img id="img_id" class="img_id" alt="" src="'+item.PhotoCover+'"></div>'
			+'<div class="leftDetails"><span style="color: black;font-size: 4vw">'+item.ProName+'</span>'
			+'</div><div class="rightDetails">X '+item.RootCount+'</div></div>'
			+'<div class="moneyDiv"><font style="color: black;">合计：￥'+item.MoneyPay+'元</font></div></div>'
			+'<span style="background-color: #bbe3f5;color: #1994f7;" onclick="pay('+item.OrderID+')">去付款</span><span onclick="delOrder('+item.OrderID+',6)">取消订单</span></div>';
		}else{
			orderDiv = '<div class="orderDiv"><div class="orderContent"><div class="dateDiv">'+item.TimeCreate+'<span>'+item.OrderStatusCS+'</span></div>'
			+'<div class="orderDetails" onclick="toOrderDetails('+item.OrderID+')"><div id="imgDiv" ><img id="img_id" class="img_id" alt="" src="'+item.PhotoCover+'"></div>'
			+'<div class="leftDetails"><span style="color: black;font-size: 4vw">'+item.ProName+'</span>'
			+'</div><div class="rightDetails">X '+item.RootCount+'</div></div>'
			+'<div class="moneyDiv"><font style="color: black;">合计：￥'+item.MoneyPay+'元</font></div></div>'
			+'<span>订单完成</span></div>';
			
		}
		
		
		$(".content").append(orderDiv);
	})
	imgCenter();	
}


function pay(OrderID){
	mui.ajax(ypc+'?UserID='+UserID+'&Token='+Token+'&OrderID='+OrderID+'&TradeType=MWEB',{
		dataType:'json',//服务器返回json格式数据
		type:'get',//HTTP请求类型
		timeout:10000,//超时时间设置为10秒；
		success:function(data){
			console.log(data)
			var mydata = data;
			if(mydata.code == 0){
				/*alert(mydata.data.mwebUrl+"&"+redirect_url);*/
				window.location.href=mydata.data.mwebUrl+"&"+redirect_url;
				
			}
		},
		error:function(xhr,type,errorThrown){
			
		}
	});
}

function toOrderDetails(orderId){
	window.location.href="orderDetails.html?UserID="+UserID+"&Token="+Token+"&OrderID="+orderId;
}

function delOrder(OrderID,OrderStatus){
		
	if(confirm(OrderStatus=="-1"?"确定删除该订单吗？":"确定取消该订单吗？")){
		var data = "UserID="+UserID+"&Token="+Token+"&OrderID="+OrderID+"&OrderStatus="+OrderStatus;
		$.ajax({
	        type: "GET",			
	            url: ordersD+"?"+data,
	            async: false,
	            dataType:"json",
	            success: function (data) {
	              
	            	if(data.code == "0"){
	    				
	    				setTimeout(function(){
	    					window.location.reload();
	            		}, 1000);
	    				
	    			}else{		
	    				alert("操作失败");
	    			}
	            },
	          	//失败时的函数
	    		error:function(){
	    			alert("操作失败");
	    		}	
	      });
		
	}
	
}