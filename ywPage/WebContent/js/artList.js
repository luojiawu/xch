$(function(){
	
	
	var UserID = getQueryVariable("UserID");
	var Token = getQueryVariable("Token");
	var RootCount = getQueryVariable("RootCount");
	mui.init()
	var arr = [];
	var articlesList = [];
	
	
	mui('.mui-scroll-wrapper').scroll({
		// deceleration: 0.0005 //flick 减速系数，系数越大，滚动速度越慢，滚动距离越小，默认值0.0006
	});
	
	getArtList();
	setSelectList();
	sendInfo();
	
	$('.mui-table-view').on('click','li',function(event){
		// console.log(event);
		var target = $(event.target);
		var that = target.find('.theIndexColor')
		
		// alert($(this).attr("dataid"));
		// var that = $('.mui-table-view').eq(0).find('.theIndexColor')
		
		if(that.hasClass('my-disabled')){
			var thatIndex = parseInt(that.attr('dataindex')) 
			var obj = {
				index: thatIndex,
				content: articlesList[thatIndex].Title,
				artID: articlesList[thatIndex].ArtID
			}
			
			arr.push(obj);
			that.removeClass('my-disabled');
			that.text(arr.length);
			console.log(arr)
	
		}else{
			
			var myindex = parseInt(that.attr('dataindex'));
			console.log(myindex)
			var theindex;
			for(var d = 0;d<arr.length;d++){
				if(arr[d].index == myindex){
					arr = $.grep(arr,function(n,i){
						return i != d
					})
				}
			}
			if(arr.length>0){
				console.log("shanchuhoudearr",arr)
				for(var b = 0;b<arr.length;b++){
					$('#artUl').children().eq(arr[b].index).find('.theIndexColor').text(b+1);
				}
			}
			that.addClass('my-disabled');
			console.log(arr)
		}
	})
	
	function getArtList(){
		mui.ajax(articles,{
			data:{
				UserID:UserID,
				Token:Token,
				PageNo: '1',
				PageSize: '20'
			},
			dataType:'json',//服务器返回json格式数据
			type:'get',//HTTP请求类型
			timeout:10000,//超时时间设置为10秒；
			success:function(data){
				console.log(data);
				articlesList = data.data
				setArtList();
			},
			error:function(xhr,type,errorThrown){
				// console.log(xhr)
			}
		});
	}
	
	function setArtList(){
		console.log('articlesList',articlesList);
		mui.each(articlesList,function(index,item){
			var html = '<li class="mui-table-view-cell mui-media" style="display: flex;">'
						+'<div style="display: flex;">'
						+'<div class="li-first">'
						+'<div class="theIndex">'
						+'<div class="theIndexColor my-disabled" dataindex="'+ index +'">'
						+'</div>'
						+'</div>'
						+'</div>'
						+'<img class="mui-pull-left my-media-img" src="'+ item.PhotoCover +'">'
						+'</div>'
						+'<div class="my-media-content">'
						+'<div class="mui-media-body">'+ item.Title+'</div>'
						+'<div class="my-media-price">约'+ item.PageCount +'页</div></div>'
						+'</li>';
			$('#artUl').append(html);
						
		})
		
	}
	
	mui('.yixuan').on('tap',function(){
		mui.each(arr,function(index,item){
			var html = '<li class="mui-table-view-cell selectLi">'
						+'<p>'+ item.content +'</p>'
						+'<p>移除</p></li>';
		})
	})
	
	
	
	function setSelectList(){
		mui('#selected').on('tap','#selected1',function(){
			$('#selectUl').empty();
			if(arr.length != 0){
				mui.each(arr,function(index,item){
					var html = '<li class="mui-table-view-cell selectLi">'
								+'<p>'+ item.content +'</p>'
								+'<p>移除</p></li>';
								
					$('#selectUl').append(html);
				})
			}
			
		})
	}
	
	function sendInfo(){
		mui('#selected').on('tap','.checkBtn',function(){
			console.log("数据",arr);
			var artIDList = '';
			mui.each(arr,function(index,item){
				if(index < arr.length-1){
					artIDList = artIDList + item.artID + ',';
				}else if(index == arr.length-1){
					artIDList = artIDList + item.artID
				}
			})
			console.log("要传递的数据",artIDList)
			
			
			getArtData(artIDList);
		})
	}
	
	function getArtData(artIDList){
		// mui.openWindow({
		// 					url: './index.html',  //跳转路径
		// 					id: 'notice.html',
		// 					extras: {
		// 						name: 123  //传递参数
		// 					},
		// 		        });
		
		mui.openWindow({
			
			url:'./review.html?UserID='+UserID+"&Token="+Token+"&artIDList="+artIDList+"&RootCount="+RootCount
		});
		console.log(webview.name);//输出mui字符串
		
		
		
		
		mui.ajax(bookPreview,{
			data:{	
				"UserID": UserID,
				"Token": Token,
				  "ArtID": artIDList,
				  "ProID": "29",
				  "PPID": "33",
				  "BookTempID": 0,
				  "IsImg": 1
			},
			dataType:'json',//服务器返回json格式数据
			type:'post',//HTTP请求类型
			timeout:10000,//超时时间设置为10秒；
			success:function(data){
				console.log("结果",data)
			},
			error:function(xhr,type,errorThrown){
				
			}
		});
	}
	
	
})