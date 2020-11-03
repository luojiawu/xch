$(function(){
	
	var blankImg;
	var allCoupon = [];
	var nowTab;
	mui.init({
		swipeBack: true ,//启用右滑关闭功能
		
	});
	
	var WxOpenID = getQueryVariable("WxOpenID");
	var UserID = getQueryVariable("UserID");
	var Token = getQueryVariable("Token");
	
	function getCoupon(){
		mui.ajax(coupon,{
			data:{
				UserID: UserID,
				Token: Token
			},
			dataType:'json',//服务器返回json格式数据
			type:'get',//HTTP请求类型
			timeout:10000,//超时时间设置为10秒；
			success:function(data){
				console.log(data)
				var mydata = data;
				if(mydata.code == 0){
					blankImg = mydata.data.CouponImg;
					allCoupon.push(mydata.data.ListNow);
					allCoupon.push(mydata.data.ListOld);
					allCoupon.push(mydata.data.ListUse);
					console.log("allCoupon",allCoupon);
					setTitle();
					tapTab();
					nowTab = 0;
					setCouponList();
				}
			},
			error:function(xhr,type,errorThrown){
				
			}
		});
	}
	
	function setTitle(){
		mui.each(allCoupon,function(index,item){
			console.log(index,item);
			if(index == 0){
				$('#segmentedControl').append('<a class="mui-control-item mui-active" href="#item'+ index+'"'+'data-index='+ index +'>可使用</a>');
				
			}else if(index == 1){
				$('#segmentedControl').append('<a class="mui-control-item" href="#item'+ index+'"'+ 'data-index='+ index +'>已使用</a>')
			}else if(index == 2){
				$('#segmentedControl').append('<a class="mui-control-item" href="#item'+ index+'"'+ 'data-index='+ index +'>已过期</a>')
			}
			
			$('.mui-content').append('<div id="item'+ index +'" class="mui-slider-item mui-control-content mui-active"></div>');
		})
	}
	
	/**
	 * tab的点击事件
	 */
	function tapTab(){
		mui('#segmentedControl').on('tap','a',function(){
			var index = this.getAttribute("data-index");
			// console.log(index)
			nowTab = parseInt(index)
			console.log(nowTab)
			setCouponList();
		}) 
	}
	
	function setCouponList(){
		if(mui(("#scroll"+nowTab)).length>0){
			console.log("#scroll"+nowTab+"存在")
			return
		}else{
			console.log("#scroll"+nowTab+"不存在")
			var html = '<div id="scroll'+ nowTab +'" class="mui-scroll-wrapper">'
						+'<div class="mui-scroll">'
						+'</div>'
						+'</div>';
			$("#item"+nowTab).append(html);
			$('#scroll'+nowTab).scroll({
				indicators: true //是否显示滚动条
			});
			
			mui.each(allCoupon[nowTab],function(index,item){
				if(nowTab == 0){
					var myCoupon = '<div class="coupon list-width" style="margin-top: 10px;">'
									+'<div class="coupon-left not-expired">'
									+'<p class="money-1"><span class="money-2">&yen;</span>'+ item.MoneyCou +'</p>'
									+'<p class="money-2">满'+ item.MoneyPay +'可用</p>'
									+'</div>'
									+'<div class="coupon-right">'
									+'<div class="coupon-right-info">'
									+'<div class="top">'
									+'<p>有效期</p>'
									+'<div class="button">去使用</div>'
									+'</div>'
									+'<div class="bottom">'+ item.TimeStart +' 至 '+ item.TimeEnd +'</div>'
									+'</div>'
									+'<div class="coupon-right-kind">'+ item.ProName +'</div>'
									+'</div>'
									+'<i class="coupon-circle cc-left"></i><i class="coupon-circle cc-right"></i>'
									+'</div>';
				}else if(nowTab == 1){
					var myCoupon = '<div class="coupon list-width" style="margin-top: 10px;">'
									+'<div class="coupon-left used">'
									+'<p class="money-1"><span class="money-2">&yen;</span>'+ item.MoneyCou +'</p>'
									+'<p class="money-2">满'+ item.MoneyPay +'可用</p>'
									+'</div>'
									+'<div class="coupon-right">'
									+'<div class="coupon-right-info">'
									+'<div class="top">'
									+'<p>有效期</p>'
									+'</div>'
									+'<div class="bottom">'+ item.TimeStart +' 至 '+ item.TimeEnd +'</div>'
									+'</div>'
									+'<div class="coupon-right-kind">'+ item.ProName +'</div>'
									+'</div>'
									+'<i class="coupon-circle cc-left"></i><i class="coupon-circle cc-right"></i>'
									+'</div>';
				}else if(nowTab == 2){
					var myCoupon = '<div class="coupon list-width" style="margin-top: 10px;">'
									+'<div class="coupon-left expire">'
									+'<p class="money-1"><span class="money-2">&yen;</span>'+ item.MoneyCou +'</p>'
									+'<p class="money-2">满'+ item.MoneyPay +'可用</p>'
									+'</div>'
									+'<div class="coupon-right">'
									+'<div class="coupon-right-info">'
									+'<div class="top">'
									+'<p>有效期</p>'
									+'</div>'
									+'<div class="bottom">'+ item.TimeStart +' 至 '+ item.TimeEnd +'</div>'
									+'</div>'
									+'<div class="coupon-right-kind">'+ item.ProName +'</div>'
									+'</div>'
									+'<i class="coupon-circle cc-left"></i><i class="coupon-circle cc-right"></i>'
									+'</div>';
				}
				$('#scroll'+nowTab ).children('.mui-scroll').append(myCoupon);
			})
			
		}
	}
	
	// <div class="coupon list-width" style="margin-top: 10px;">
	// 	<div class="coupon-left used">
	// 		<p class="money-1"><span class="money-2">&yen;</span>8</p>
	// 		<p class="money-2">满50可用</p>
	// 	</div>
	// 	<div class="coupon-right">
	// 		<div class="coupon-right-info">
	// 			<div class="top">
	// 				<p>有效期</p>
	// 			</div>
	// 			<div class="bottom">
	// 				2020.03.06-2020.03.31
	// 			</div>
	// 		</div>
	// 		<div class="coupon-right-kind">
	// 			不限商品
	// 		</div>
	// 	</div>
	// 	<i class="coupon-circle cc-left"></i>
	// 	<i class="coupon-circle cc-right"></i>
	// </div>
	
	// <div class="coupon list-width" style="margin-top: 10px;">
	// 	<div class="coupon-left not-expired">
	// 		<p class="money-1"><span class="money-2">&yen;</span>8</p>
	// 		<p class="money-2">满50可用</p>
	// 	</div>
	// 	<div class="coupon-right">
	// 		<div class="coupon-right-info">
	// 			<div class="top">
	// 				<p>有效期</p>
	// 				<div class="button">
	// 					去使用
	// 				</div>
	// 			</div>
	// 			<div class="bottom">
	// 				2020.03.06-2020.03.31
	// 			</div>
	// 		</div>
	// 		<div class="coupon-right-kind">
	// 			不限商品
	// 		</div>
	// 	</div>
	// 	<i class="coupon-circle cc-left"></i>
	// 	<i class="coupon-circle cc-right"></i>
	// </div>
	
	getCoupon();
	
	
});