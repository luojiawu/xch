var UserID = getQueryVariable("UserID");
var Token = getQueryVariable("Token");

$(function(){
	
	
	
	mui.init({
		swipeBack: true ,//启用右滑关闭功能
		 
	});
	
	var homeList;
	
	getHome();
	
	/*alert("UserID-->"+UserID+",Token-->"+Token);*/
	
	
	function getHome(){
		
		mui.ajax(homeUrl,{
			dataType:'json',//服务器返回json格式数据
			type:'get',//HTTP请求类型
			timeout:10000,//超时时间设置为10秒；
			success:function(data){
				console.log(data)
				var mydata = data;
				if(mydata.code == 0){
					homeList = mydata.data[0];
					console.log("homeList",homeList)
					setBanner();
					setCateList();
					setBottom();
				}
			},
			error:function(xhr,type,errorThrown){
				
			}
		});
	}

	function setCateList(){
		
		console.log(homeList.CateList);
		
		mui.each(homeList.CateList,function(index,item){
			
			var goodsDiv = $("<div/>").addClass("goodsDiv");
			
			var CName = '<div class="goodsType"><img alt="" src="../img/flower_l.png">'+item.CName+'<img alt="" src="../img/flower_r.png"></div>';
			
			var div = '<div onclick="toGoodsList('+item.CID+')">'+item.CName+'</div>';
			$(".classify").append(div);
				
			goodsDiv.append(CName);
			var entityDiv = $("<div/>").addClass("goodsEntityDiv");
			mui.each(item.ProList,function(index,ProList){
				
				var goodsEntity = '<div class="goodsEntity" onclick="toGoods('+ProList.ProID+","+ProList.CID+')">'
				+'<input type="hidden" value="'+ProList.ProID+'">'
				+'<img alt="商品展示图" src="'+ProList.PhotoHome+'">'
				+'<div class="goodsPrice">￥  <span>'+ProList.PriceSale+'</span></div>'
				+'<div class="goodsName">'+ProList.ProName+'</div>'
				+'<div class="goodsTitle">'+ProList.SubTitle+'</div></div>';
				entityDiv.append(goodsEntity);
			
			})
			goodsDiv.append(entityDiv);
			
			$(".cateList").append(goodsDiv);
		
		})
		
		
		
		
	}

	function setBottom(){
		
		console.log(homeList.HomeBottom);
		
		var bottomImg = '<img alt="" src="'+homeList.HomeBottom+'" style="width: 100vw;">';
		
		$(".explain").append(bottomImg);
		
		
	}

	function setBanner(){
		
		console.log(homeList.HomeAd);
		mui.each(homeList.HomeAd,function(index,item){
			
			var HomeAd = '<div class="swiper-slide"><div class="swiper-zoom-container"><img src="'+ item.url +'"></div></div>';
		
			$(".swiper-wrapper").append(HomeAd);
			
		
		
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
	
	
	
	$("#toGoodsList").on("click",function(){
		
		$(".classify").toggle();
		
	})
	
	$("#toCoupon").on("click",function(){
		
		window.location.href="coupon.html?UserID="+UserID+"&Token="+Token;
		
	})
	
	$("#toOrderList").on("click",function(){
		
		window.location.href="orderList.html?UserID="+UserID+"&Token="+Token;
		
	})
	
})


function toGoodsList(CID){
	
	window.location.href="goodsList.html?CID="+CID+"&UserID="+UserID+"&Token="+Token;
	
}

//因为上面的代码是拼接加进页面的，该用于点击事件的方法不能在$(funciton)里面。
function toGoods(ProID,CID){
	
	window.location.href="goods.html?ProID="+ProID+"&CID="+CID+"&UserID="+UserID+"&Token="+Token;
	
}

  
