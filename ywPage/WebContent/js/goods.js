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


$(function(){
	
	
	
	mui.init({
		swipeBack: true ,//启用右滑关闭功能
		
	});
	var proList;
	var goodsType;
	var ProId = getQueryVariable("ProID");
	
	var UserID = getQueryVariable("UserID");
	var Token = getQueryVariable("Token");
	
	
	
	getProList();
	
	function getProList(){
		mui.ajax(product+'?ProID='+ProId,{
			dataType:'json',//服务器返回json格式数据
			type:'get',//HTTP请求类型
			timeout:10000,//超时时间设置为10秒；
			success:function(data){
				console.log(data)
				var mydata = data;
				if(mydata.code == 0){
					proList = mydata.data;
					console.log("proList",proList)
					if(proList.ProID == 28){
						goodsType = "calendar";
					}else{
						goodsType = "photo";
					}
					setBanner();
					setPro();
					setPhotoDetail();
				}
			},
			error:function(xhr,type,errorThrown){
				
			}
		});
	}
	
	function setBanner(){
		
		console.log(proList.PhotoAd);
		mui.each(proList.PhotoAd,function(index,item){
			
			var PhotoAd = '<div class="swiper-slide"><div class="swiper-zoom-container"><img src="'+ item.url +'"></div></div>';
		
			$("#banner1 .swiper-wrapper").append(PhotoAd);
			
		
		
		})
		
		/*
		创建Swiper对象，以及因为Swiper的特性，
		需要隐藏的标签代码需要在创建对象后再执行
		*/
		//轮播图滑动对象				  
		var swiper1 = new Swiper('#banner1', {
		    zoom: true,
		    autoplay:3000,
		    autoplayDisableOnInteraction:false,
		    pagination: '.swiper-pagination'
		});	
		
	}
	
	function setPro(){
		
		
		var PhotoAd = '<span style="color: #ff1e1e">￥<font style="font-size: 5vw">'+proList.PriceSale+'</font></span>&nbsp;起<font style="font-size: 3vw;text-decoration:line-through;margin-left: 2vw;color: #9E9E9E;">'+proList.PriceDiscount+'</font><br>'
			+'<span style="font-size: 4vw;font-weight: bold;">'+proList.ProName+'</span><br>'
			+'<span style="font-size: 4vw;font-weight: bold;">'+proList.DiscountStr+'</span>';
		
		$(".goodsIntro").append(PhotoAd);
		
		PhotoAd = '<span style="color: #ff1e1e">￥<font style="font-size: 5vw">'+proList.PriceSale+'</font></span>&nbsp;起<font style="font-size: 3vw;text-decoration:line-through;margin-left: 2vw;color: #9E9E9E;">'+proList.PriceDiscount+'</font><br>'
		
		$(".typeHeaderRight").append(PhotoAd);
		
		
		mui.each(proList.PropertyList,function(index,item){
			
			var PPName = '<div class="swiper-slide" id="typeName">'+item.PPName+'</div>';
			
			$("#banner2 .swiper-wrapper").append(PPName);
			if(item.IsDefault == 1){
				
				$("#materials span").text(item.PPName);
				$(".typeHeaderLeft img").attr("src",item.PhotoStr);
				
				$("#typeName").css({"background-color":"#157ed2","color":"white"});
				
				
			}
			
		})
		
		//商品类型滑动对象				  
		var swiper2 = new Swiper('#banner2', {
		    slidesPerView: 2,
		    paginationClickable: true,
		    spaceBetween: 0,
		    freeMode: true
		});
		$(".typeDiv .swiper-slide").css({"width":"auto"});
		
		
		
	}
	
	function setPhotoDetail(){
		
		
		console.log(proList.PhotoDetail);
		mui.each(proList.PhotoDetail,function(index,item){
			
			var img = $("<img/>").attr("src",item.url);
			
			$(".goodsDetails").append(img);
			
		
		})
		
	}
	
	$("#materials").on("click",function(){     
		
		$("#typeBackground").css("display","block");
		
		
		
		
	})
	
	$("#toPhotoPage").on("click",function(){     
	
		$("#typeBackground").css("display","block");
		
		/*if(proList.CID == 2){
			window.location.href="artList.html?WxOpenID="+WxOpenID+"&RootCount="+RootCount;
		}else if(proList.CID == 3){
			var data = {"PPID":proList.PropertyList[0].PPID,"ProID":ProId,"PageMin":proList.PageMin};
			if(os.isPhone){ 
				window.webkit.messageHandlers.toPhoto.postMessage(data);
			}else if(os.isAndroid){
				window.WebViewJavascriptBridge.callHandler("toPhoto", data
						, function(responseData) {
					console.log(responseData);
				});
			}
		}*/
	
	})
	
	$("#blankDiv").on("click",function(){     
	
		$("#typeBackground").css("display","none");
	})
	
	$("#typeBtn").on("click",function(){     
		
		var RootCount = $("#numSpan").text();
		var data = {"UserID":UserID,"Token":Token,"RootCount":RootCount,"PPID":proList.PropertyList[0].PPID,"ProID":ProId,"PageMin":proList.PageMin,"type":goodsType};
		if(proList.CID == 2){
			window.location.href="artList.html?UserID="+UserID+"&Token="+Token+"&RootCount="+RootCount;
		}else if(proList.CID == 3){
			
			if(os.isPhone){ 
				window.webkit.messageHandlers.toPhoto.postMessage(data);
			}else if(os.isAndroid){
				window.WebViewJavascriptBridge.callHandler("toPhoto", data
						, function(responseData) {
					console.log(responseData);
				});
			}
		}
		
		
	})
	
	$("#cutSpan").on("click",function(){     
		
		var num = $("#numSpan").text();
		
		if(num > 1){
			num--;
			$("#numSpan").text(num);
		}
		
		
		
	})
	
	$("#addSpan").on("click",function(){     
		
		var num = $("#numSpan").text();
		
		num++;
		$("#numSpan").text(num);
		
		
	})
	
	
	
	
	
})

  
