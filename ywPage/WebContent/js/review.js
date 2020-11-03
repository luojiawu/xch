$(function(){
	
	/*var myopenid = 'ojgin5DhC_CU5V4HpRPbjG0bArZ4';
	var artIDList = '3196,3198,3200';*/
	
	var UserID = getQueryVariable("UserID");
	var Token = getQueryVariable("Token");
	var artIDList = getQueryVariable("artIDList");
	var RootCount = getQueryVariable("RootCount");
	var theBookInfo;
	var thePageInfo;
	var theModuleList;
	var nowMould;
	var obj = [
		// {
		// 	img:'./image/test.jpg',
		// 	bg:'https://img1.sppxw.com/YW/BookTemp/32K01/320104.jpg',
		// 	left: 7.8,
		// 	top: 5.3,
		// 	width: 80,
		// 	height: 28.0,
		// },
		// {
		// 	img:'./image/test.jpg',
		// 	bg:'https://img1.sppxw.com/YW/BookTemp/32K01/320105.jpg',
		// 	left: 7.3,
		// 	top: 6.7,
		// 	width: 41.0,
		// 	height: 44.8,
		// },
		// {
		// 	img:'./image/test.jpg',
		// 	bg:'https://img1.sppxw.com/YW/BookTemp/32K01/320103.jpg',
		// 	left: 51.6,
		// 	top: 48.6,
		// 	width: 41.0,
		// 	height: 44.8,
		// },
		// {
		// 	img:'./image/test.jpg',
		// 	bg:'https://img1.sppxw.com/YW/BookTemp/32K01/320101.jpg',
		// 	left: 7.8,
		// 	top: 5.3,
		// 	width: 56.3,
		// 	height: 28.0,
		// },
		// {
		// 	img:'./image/test.jpg',
		// 	bg:'https://img1.sppxw.com/YW/BookTemp/32K01/320102.jpg',
		// 	left: 36.0,
		// 	top: 36.0,
		// 	width: 56.3,
		// 	height: 28.0,
		// },
		// {
		// 	img:'./image/test.jpg',
		// 	bg:'https://img1.sppxw.com/YW/BookTemp/32K01/320104.jpg',
		// 	left: 9.8,
		// 	top: 66.8,
		// 	width: 56.3,
		// 	height: 28.0,
		// },
		// {
		// 	img:'./image/test.jpg',
		// 	bg:'https://img1.sppxw.com/YW/BookTemp/32K01/320105.jpg',
		// 	left: 8.4,
		// 	top: 5.3,
		// 	width: 39.8,
		// 	height: 31.9,
		// },
		// {
		// 	img:'./image/test.jpg',
		// 	bg:'https://img1.sppxw.com/YW/BookTemp/32K01/320103.jpg',
		// 	left: 8.4,
		// 	top: 51.2,
		// 	width: 39.8,
		// 	height: 31.9,
		// },
	];
	
	$(".checkBtn").on("click",function(){
		
		window.location.href="order.html?UserID="+UserID+"&Token="+Token+"&RootCount="+RootCount+"&BookID="+theBookInfo.BookID;
		
	})
	
	//B页面onload从服务器获取列表数据；
	window.onload = function(){
	  //从服务器获取数据

	  //业务数据获取完毕，并已插入当前页面DOM；
	  //注意：若为ajax请求，则需将如下代码放在处理完ajax响应数据之后；
	  mui.plusReady(function(){
	    //关闭等待框
	    plus.nativeUI.closeWaiting();
	    //显示当前页面
	    mui.currentWebview.show();
		var currView = plus.webview.currentWebview();
		var parentview = currView.opener();
		console.log(currView.artIDList);	//123
	  });
	}
	
	function getArtList(){
		mui.ajax(bookPreview,{
			data:{
				"UserID": UserID,
				"Token": Token,
				  "ArtID": artIDList,
				  "ProID": "29",
				  "PPID": "33",
				  "BookTempID": 0,
				  "IsImg": 0
			},
			dataType:'json',//服务器返回json格式数据
			type:'post',//HTTP请求类型
			timeout:10000,//超时时间设置为10秒；
			success:function(data){
				console.log("结果",data)
				if(data.code == 0){
					theBookInfo = data.data;
					thePageInfo = data.data.Content;
					setPageInfoList();
					setBookInfo();
					getModule();
				}
			},
			error:function(xhr,type,errorThrown){
				
			}
		});
	}
	
	function setPageInfoList(){
		var reg = /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/;
		mui.each(thePageInfo,function(index,item){
			var itemp ={
				bg:item.ImgBackUrl,
			}
			if(item.PageStr == "目录页"){
				itemp.PageStr = "目录页";
				itemp.left = item.PageContent[0].XValue;
				itemp.top = item.PageContent[0].YValue;
				itemp.width = item.PageContent[0].WValue;
				itemp.height =  item.PageContent[0].HValue;
				var o = item.PageContent[0].ContentInfo.split('\n');
				itemp.ContentInfo = o;
				
			}
			for(var i = 0;i<item.PageContent.length;i++){
				if(reg.test(item.PageContent[i].ContentInfo)){
					itemp.img = item.PageContent[i].ContentInfo;	
					itemp.left = item.PageContent[i].XValue;
					itemp.top =  item.PageContent[i].YValue;
					itemp.width = item.PageContent[i].WValue;
					itemp.height =  item.PageContent[i].HValue;
				}
			}
			obj.push(itemp)
		})
		
		console.log("obj",obj);
		setImg();
	}
	
	function setImg(){
		mui.each(obj,function(index,item){
			if(item.img){
				var html = 	'<div class="swiper-slide">'
							+'<div class="thePage" style="background: url('+ item.bg +');background-size:100% 100%;">'
							+'<img src="'+ item.img +'" style="position: absolute;left:'+ item.left +'%;top:'+ item.top +'%;width:'+ item.width +'%;height:'+ item.height +'%;">'
							+'</div>'
							+'</div>';
			}else if(item.PageStr == "目录页"){
				var html = 	'<div class="swiper-slide">'
							+'<div class="thePage" style="background-color:#ffffff;">'
							+'<div style="position: absolute;left:'+ item.left +'%;top:'+ item.top +'%;width:'+ item.width +'%;height:'+ item.height +'%;" id="catalogue">'
							
							+'</div>'
							// +'<img src="'+ item.img +'" style="position: absolute;left:'+ item.left +'%;top:'+ item.top +'%;width:'+ item.width +'%;height:'+ item.height +'%;">'
							+'</div>'
							+'</div>';
			}else{
				var html = 	'<div class="swiper-slide">'
							+'<div class="thePage" style="background: url('+ item.bg +');background-size:100% 100%;">'
							// +'<img src="'+ item.img +'" style="position: absolute;left:'+ item.left +'%;top:'+ item.top +'%;width:'+ item.width +'%;height:'+ item.height +'%;">'
							+'</div>'
							+'</div>';
			}
			
			$('.swiper-wrapper').append(html);
			
		})
		mui.each(obj,function(index,item){
			if(item.PageStr == "目录页"){
				mui.each(item.ContentInfo,function(ondex,otem){
					
					var m = '<p>'+ otem +'</p>';
					
					$('#catalogue').append(m)
				})
			}
		})
		
		
		
		// $(".swiper-container").append('<div class="swiper-pagination"></div>');
		
		if(!mySwiper){
			var mySwiper;
		}else{
			mySwiper = null;
		}
		mySwiper = new Swiper ('.swiper-container', {
		    direction: 'horizontal', // 垂直切换选项
		    loop: false, // 循环模式选项
		    // 如果需要分页器
		    pagination: {
		      el: '.swiper-pagination',
			  dynamicBullets: true,
			  dynamicMainBullets: 2
		    },
		  })  
	}
	
	function setBookInfo(){
		console.log('ceshi',$('#booktitle').val())
		$('#booktitle').val(theBookInfo.Title);
		console.log('ceshi',$('#booktitle').val())
		$('#authorName').val(theBookInfo.Author);
		
	}
	
	function getModule(){
		mui.ajax(bookTemps,{
			data:{
				ProID: 29
			},
			dataType:'json',//服务器返回json格式数据
			type:'get',//HTTP请求类型
			timeout:10000,//超时时间设置为10秒；
			success:function(data){
				console.log("data",data)
				if(data.code == 0){
					theModuleList = data.data;
					
					setModule();
				}
			},
			error:function(xhr,type,errorThrown){
				
			}
		});
	}
	
	function setModule(){
		console.log("theBookInfo",theBookInfo)
		mui.each(theModuleList,function(index,item){
			if(item.BookTempID == theBookInfo.BookTempID){
				var html = '<div class="mui-active mymould chooseMould" name="mouldDiv" dataindex="'+ item.BookTempID +'">'
							+'<img src="'+ item.PhotoSmall +'" style="width: 100px;height: 135px;border: #DDDDDD 1px solid;">'
							+'<p>'+ item.Title +'</p>'
							+'</div>';
			}else{
				var html = '<div class="mui-active mymould" name="mouldDiv" dataindex="'+ item.BookTempID +'">'
							+'<img src="'+ item.PhotoSmall +'" style="width: 100px;height: 135px;border: #DDDDDD 1px solid;">'
							+'<p>'+ item.Title +'</p>'
							+'</div>';
			}
			
			
			$('#mouldScroll').append(html);
		})
		
		
	}
	
	function changeMould(){

		mui('#mouldScroll').on('click','.mymould',function(event){
			var target = $(event.target);
			console.log("target",target);
			$(this).toggleClass('chooseMould');
			$(this).siblings("div[name='mouldDiv']").removeClass('chooseMould')
			nowMould = $(this).attr('dataindex');
			console.log("nowMould",nowMould);
			
			// $("div[name='mouldDiv']").siblings("div").removeClass('chooseMould');
			// target.removeClass('chooseMould');
			
			// target.addClass('chooseMould');
			// event.stopPropagation();
		})
	}
	
	function getNewMould(aa){
		console.log("输入的是",aa);
		mui.ajax(books,{
			data:{
				"ProID": 29,
				"UserID": UserID,
				"Token": Token,
				"BookTempID": aa,
				
			},
			dataType:'json',//服务器返回json格式数据
			type:'post',//HTTP请求类型
			timeout:10000,//超时时间设置为10秒；
			success:function(data){
				console.log("改变",data)
			},
			error:function(xhr,type,errorThrown){
				
			}
		});
	}
	
	function clearPage(){
		$('#mouldScroll').empty();
		$('.swiper-wrapper').empty();
		// $('.swiper-pagination').remove();	
	}
	
	// function setSwiperPagination(){
	// 	console.log("jinrule")
	// 	// var html = '<div class="swiper-pagination"></div>';
	// 	$(".swiper-container").append('<div class="swiper-pagination"></div>');
	// }
	
	function clickChange(){
		console.log("进入");
		mui('.Changeit').on('click','.rightbtn',function(){
			console.log("再次进入")
			mui.ajax(books,{
				data:{
					"BookID": theBookInfo.BookID,
					"UserID": UserID,
					"Token": Token,
					"BookTempID": nowMould,
					
				},
				dataType:'json',//服务器返回json格式数据
				type:'post',//HTTP请求类型
				timeout:10000,//超时时间设置为10秒；
				success:function(data){
					console.log("改变",data)
					if(data.code == 0){
						mui('#popover2').popover('toggle');
						theBookInfo = data.data;
						thePageInfo = data.data.Content;
						obj = [];
						setTimeout(clearPage,200);
						// clearPage();
						setTimeout(setPageInfoList,400);
						setTimeout(setModule,400);
						// setTimeout(setSwiperPagination,1500);
						// setSwiperPagination();
						// setPageInfoList();
						// setModule();
						
						
					}
				},
				error:function(xhr,type,errorThrown){
					
				}
			});
		})
	}
	

	
	
	getArtList();
	changeMould();
	clickChange();
	
	mui.init({
		// gestureConfig:{
		// 	tap: true, //默认为true
		//    doubletap: true, //默认为false
		//    longtap: true, //默认为false
		//    swipe: true, //默认为true
		//    drag: true, //默认为true
		//    hold:false,//默认为false，不监听
		//    release:false//默认为false，不监听
		// },
		
	});
	// mui('.mui-scroll-wrapper').scroll({
	// 	// deceleration: 0.0005 //flick 减速系数，系数越大，滚动速度越慢，滚动距离越小，默认值0.0006
	// 	 scrollY: false, //是否竖向滚动
	// 	 scrollX: true, //是否横向滚动
	// });
	
	function addd(){
		document.getElementsByClassName('contentBox')[0].addEventListener("swipeleft",function(){
		     console.log("你正在向左滑动");
			 $('.thePage').addClass('moved');
		});
		document.getElementsByClassName('contentBox')[0].addEventListener("swiperight",function(){
		     console.log("你正在向右滑动");
			 $('.thePage').removeClass('moved');
		});
	}
	
	// addd();
	/* 获取url上的参数 */
	
	
})