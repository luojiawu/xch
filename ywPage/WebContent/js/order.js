$(function(){
	
	
	var WxOpenID = getQueryVariable("WxOpenID");
	var UserID = getQueryVariable("UserID");
	var Token = getQueryVariable("Token");
	var BookID = getQueryVariable("BookID");
	var RootCount = getQueryVariable("RootCount");
	var CouID = getQueryVariable("CouID");
	var LIID = getQueryVariable("LIID");
	var ProID = getQueryVariable("ProID");
	var OrderPreview;
	getOrderPreview();
	
	function getOrderPreview(){	
		mui.ajax(orderPreview+'?UserID='+UserID+'&Token='+Token+'&BookID='+BookID+'&RootCount='+RootCount+"&ProID="+ProID,{
			dataType:'json',//服务器返回json格式数据
			type:'get',//HTTP请求类型
			timeout:10000,//超时时间设置为10秒；
			success:function(data){
				console.log(data)
				var mydata = data;
				if(mydata.code == 0){
					OrderPreview = mydata.data;
					
					console.log("OrderPreview",OrderPreview)
					setSite();
					setGoods();
					
					
				}
			},
			error:function(xhr,type,errorThrown){
				
			}
		});
	}
	
	function setGoods(){
		
		
		var goods = '<div class="goodsLeft" id="imgDiv"><img id="img_id" class="img_id" alt="" src="'+OrderPreview.PhotoCover+'"></div>'
		+ '<div class="goodsRight"><span class="goodsName">'+OrderPreview.ProName+'</span><br>'
		+ '<span>'+OrderPreview.Title+'</span><br>'
		+ '<span>页数：'+OrderPreview.PageCount+'</span><br>'
		+ '<span>尺寸：'+OrderPreview.ProSize+'</span></div>';
		$(".goodsDiv").append(goods);
		imgCenter();
		
		
		var money = '<div><span class="moneyLeft">基础价格&nbsp;<font>('+OrderPreview.MoneyBaseStr+')</font></span>'
		+ '<span class="moneyRight">￥'+OrderPreview.MoneyBase+'</span></div>'
		+ '<div><span class="moneyLeft">追加页数&nbsp;<font>('+OrderPreview.MoneyMoreStr+')</font></span>'	
		+ '<span class="moneyRight">￥'+OrderPreview.MoneyMore+'</span></div>';
		$(".moneyDiv").append(money);
		
		var printingNum = '<span class="NumLeft">打印本数</span><span class="NumRight">x<font>'+RootCount+'</font></span>';
		$(".printingNum").append(printingNum);
		
		var cou = '<span>优惠券折扣</span><span style="float: right;margin-right: 1.5vw;color: #9E9E9E;"><font>'+"选择"+'</font></span>';
		$(".couDiv").append(cou);	
			
		$(".moneyPay").text(OrderPreview.MoneyPay);	
		
	
		
		
	}
	
	
	function setSite(){
		
		console.log(OrderPreview.LinkInfo);
		if(OrderPreview.LinkInfo.length == 0){
			
			var site = '<input type="hidden" id="siteType" value="0"/><div class="addSite" id="addSite">添加收货地址</div>';
			
			$(".header").append(site);
			
		}else{
			
			mui.each(OrderPreview.LinkInfo,function(index,item){
				
				if(LIID == "" && item.IsDefault == 1){
					
					var site = '<input type="hidden" id="siteType" value="1"/>'
					+'<div id="siteList"><div class="userDiv" id="userDiv">'
		    		+'<span>收货人：'+item.Man+'</span><span class="phone">'+item.Phone+'</span></div>'	
		    		+'<div class="siteDiv"><span class="siteSpan">收货地址：<span style="color: #F44336;margin: 0vw 1vw;">[默认地址]</span>'+item.Province+item.City+item.District+item.Address+'</span>'		
		    		+'<img src="../img/choice.png"></div></div>';		
			    			
							
					$(".header").append(site);
					LIID = item.LIID;
					return false;
					
				}else if(item.LIID == LIID){
					
					var site = '<input type="hidden" id="siteType" value="1"/>'
					+'<div id="siteList"><div class="userDiv" id="userDiv">'
		    		+'<span>收货人：'+item.Man+'</span><span class="phone">'+item.Phone+'</span></div>'	
		    		+'<div class="siteDiv"><span class="siteSpan">收货地址：'+item.Province+item.City+item.District+item.Address+'</span>'		
		    		+'<img src="../img/choice.png"></div></div>';		
			    			
					$(".header").append(site);
					return false;
				}
				
				
				
				
				
				
				
			})
			
			
			
		}
		
	}
	
	
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
	
	
	
	$(".header").on("click",function(){
		
		window.location.href="siteList.html?UserID="+UserID+"&Token="+Token+"&BookID="+BookID+"&RootCount="+RootCount+"&CouID="+CouID;
	})
	
	$(".couDiv").on("click",function(){
		
		window.location.href="coupon.html?UserID="+UserID+"&Token="+Token+"&BookID="+BookID+"&RootCount="+RootCount+"&CouID="+CouID;
	
	})
	
	
	
	$("#payBtn").on("click",function(){
		var siteType = $("#siteType").val();
		//地址验证
		if(siteType == 0){
			alert("请先填入地址");
		}else{
			
			mui.ajax(ordersA+'?UserID='+UserID+'&Token='+Token+'&BookID='+BookID+'&RootCount='+RootCount+"&LIID="+LIID,{
				dataType:'json',//服务器返回json格式数据
				type:'get',//HTTP请求类型
				timeout:10000,//超时时间设置为10秒；
				success:function(data){
					console.log(data)
					var mydata = data;
					if(mydata.code == 0){
						
						mui.ajax(ypc+'?UserID='+UserID+'&Token='+Token+'&OrderID='+mydata.data.OrderID+'&TradeType=MWEB',{
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
				},
				error:function(xhr,type,errorThrown){
					
				}
			});
			
			
			
		}
		
		
	});
	
	


});