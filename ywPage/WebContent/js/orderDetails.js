
var OrderID = getQueryVariable("OrderID");
var UserID = getQueryVariable("UserID");
var Token = getQueryVariable("Token");



$(function(){
	
	var OrdersDetails;
	
	
	
	getOrdersDetails();
	
	
	
	
})

function imgCenter(){
	
	//调整照片取中间部分
	var img = document.getElementById("img_id");
   	var image = new Image();
		image.src = $("#img_id").attr("src");
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
            img.width = (h/realHeight)*realWidth;//等比缩放宽度
            img.height = h;//跟div高度一致
            img.style.left = '-' + ((h/realHeight)*realWidth-w)/2 + 'px';//设置图片相对自己位置偏移为img标签的宽度-高度的一半
        }else if (realWidth < realHeight){
            img.width =w ;//跟div高度一致
            img.height = (w/realWidth)*realHeight;//等比缩放高度
            img.style.top = '-' + ((w/realWidth)*realHeight-h)/2 + 'px';//设置图片相对自己位置偏移为img标签的高度-宽度的一半
        }else {
            img.width =w ;
            img.height = h;
        }

    };
	
	
}

function pay(orderId){
		
	mui.ajax(ypc+'?UserID='+UserID+'&Token='+Token+'&OrderID='+OrderID+'&TradeType=MWEB',{
		dataType:'json',//服务器返回json格式数据
		type:'get',//HTTP请求类型
		timeout:10000,//超时时间设置为10秒；
		success:function(data){
			console.log(data)
			var mydata = data;
			if(mydata.code == 0){
				
				window.location.href=mydata.data.mwebUrl+"&"+redirect_url;
				
			}
		},
		error:function(xhr,type,errorThrown){
			
		}
	});
	
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
    						
    						if(OrderStatus=="-1"){
    							window.location.href="orderList.html?UserID="+UserID+"&Token="+Token;
        					}else{
        						window.location.reload();
    	    				}
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








function getOrdersDetails(){
		mui.ajax(orderDetails+'?UserID='+UserID+"&Token="+Token+"&OrderID="+OrderID,{
			dataType:'text',//服务器返回json格式数据
			type:'get',//HTTP请求类型
			timeout:10000,//超时时间设置为10秒；
			success:function(data){
				console.log(data)
				var mydata = JSON.parse(data.replace(/[\r\n]/g,""));
				if(mydata.code == 0){
					
					OrdersDetails = mydata.data;
					
					console.log("OrdersDetails",OrdersDetails);
					setOrderStatusTop();
					setOrderStatusBottom();
					setOrderInfoTop();
					setOrderInfoMiddle();
					setOrderTime();
					setFooter();
				}
			},
			error:function(xhr,type,errorThrown){
				
			}
		});
	}
	
	function setOrderStatusTop(){
		
		
		
		var orderStatusTop = '<span style="font-size: 5vw">'+OrdersDetails.OrderStatusCS+'</span>'
		+'<span style="float: right;display: inline-block;width: 18vw;border: 1px solid white;text-align: center;height: 6vw;'
	    +'font-size: 3vw;line-height: 6vw;border-radius: 5vw;"><img style="width: 4vw;vertical-align: text-bottom;margin-right: 1vw;" src="../img/service2.png">客服</span>';
		
		$(".orderStatusTop").append(orderStatusTop);
		
	}

	function setOrderStatusBottom(){
		
		var orderStatusBottom = '<div class="orderStatusMan"><span>收货人：'+OrdersDetails.LinkMan+'</span><span style="float: right;margin-right: 4vw;">'
		+OrdersDetails.LinkPhone+'</span></div><div class="orderStatusSite">收货地址：'+OrdersDetails.LinkAddress+'</div>';
		$(".orderStatusBottom").append(orderStatusBottom);
		
	}
	
	function setOrderInfoTop(){
		var orderInfoTop = '<div class="imgDiv" id="imgDiv"><img id="img_id" class="img_id" alt="" src="'+OrdersDetails.PhotoCover+'"></div>'
		+'<div class="orderInfoPrice"><span>￥'+OrdersDetails.MoneyPay+'</span><br><span style="display: inline-block;margin-top: 16vw;">'
		+'X '+OrdersDetails.RootCount+'</span></div><div class="orderInfoGoods"><span>'
		+OrdersDetails.ProName+'</span><br><span style="font-size: 3vw;color: #b1afb2;">《'+OrdersDetails.Title+'》</span><br>'
		+'<span style="font-size: 3vw;color: #b1afb2;">数量:'+OrdersDetails.PageCount+'</span></div>';
		
		
		$(".orderInfoTop").append(orderInfoTop);
			
		imgCenter();
		
	}
	
	function setOrderInfoMiddle(){
		
		var orderInfoMiddle = '<div><span>商品总价</span><span style="float: right;margin-right: 4vw;">￥'+OrdersDetails.MoneyOrder+'</span></div>';
		/* +'<div><span>运费</span><span style="float: right;margin-right: 4vw;">￥'+OrdersDetails.MoneyPay+'</span></div>';
		 */
		
		$(".orderInfoMiddle").append(orderInfoMiddle);
		$("#MoneyPay").html("￥"+OrdersDetails.MoneyPay);
		
	}
	
	function setOrderTime(){
		
		$("#OrderNum").html("订单编号："+OrdersDetails.OrderNum);
		$("#TimeCreate").html("下单时间："+OrdersDetails.TimeCreate);
		 
	}
	
	function setFooter(){
		var footer;
		if(OrdersDetails.OrderStatus == 0){
			
			footer = '<span class="toPay" onclick="pay('+OrdersDetails.OrderID+')">去付款</span>'
			+'<span class="delOrder" onclick="delOrder('+OrdersDetails.OrderID+',6)">取消订单</span>';
			
		}else{
			
			footer = '<span class="delOrder" onclick="delOrder('+OrdersDetails.OrderID+',-1)">删除订单</span>';
			
		}
		$(".footer").append(footer);
		
	}

	