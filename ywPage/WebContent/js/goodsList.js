var CID = getQueryVariable("CID");
var UserID = getQueryVariable("UserID");
var Token = getQueryVariable("Token");

$(function(){
	
	mui.init({
		swipeBack: true ,//启用右滑关闭功能
		
	});

	var allGood;
	var nowGoodList;






/**
 * 获取商品分类
 */
function getGoodsTitle(){
	mui.ajax(category,{
		data:{
			CID: CID
		},
		dataType:'json',//服务器返回json格式数据
		type:'get',//HTTP请求类型
		timeout:10000,//超时时间设置为10秒；
		// headers:{'Content-Type':'application/json'},	              
		success:function(data){
			console.log("getGoodsTitle",data)
				
			if(data.code == 0){
				allGood = data.data
				// console.log(allGood)
				setTitle();
				// console.log(allGood[0].CID)
				if(CID == 3){
					
					
					$("#segmentedControl a:eq(0)").removeClass("mui-active");
					$("#item2").removeClass("mui-active");
					$("#item3").addClass("mui-active");
					$("#segmentedControl a:eq(1)").addClass("mui-active");
					getGoodsList(1,allGood[1].CID);
				}else{
					getGoodsList(0,allGood[0].CID);
				}
				
			}
			
		},
		error:function(xhr,type,errorThrown){
			//异常处理；
			console.log(type);
		}
	});
}

/**
 * 设置分类tab
 */
function setTitle(){
	mui.each(allGood,function(index,item){
		console.log(index,item);
		if(index == 0){
			$('#segmentedControl').append('<a class="mui-control-item mui-active" href="#item'+ item.CID+'"'+'data-index='+ index +'>'+ item.CName +'</a>');
			
		}else{
			$('#segmentedControl').append('<a class="mui-control-item" href="#item'+ item.CID+'"'+ 'data-index='+ index +'><span id="i3">'+ item.CName +'</span></a>')
		}
		
		$('.mui-content').append('<div id="item'+ item.CID +'" class="mui-slider-item mui-control-content mui-active"></div>');
		
		
	})
	tapTab();
	
	
}


/**
 * 获取具体的分类内容
 */
function getGoodsList(i,cid){
	mui.ajax(category,{
		data:{
			CID: cid
		},
		dataType:'json',//服务器返回json格式数据
		type:'get',//HTTP请求类型
		timeout:10000,//超时时间设置为10秒；
		success:function(data){
			console.log("getGoodsList",cid,data)
			if(data.code == 0){
				nowGoodList = data.data[i]
				console.log(nowGoodList);
				setGoodsList();
				
			}
		},
		error:function(xhr,type,errorThrown){
			console.log(type);
		}
	});

	
}

/**
 * 设置分类内容
 */
function setGoodsList(){
	if(mui(("#scroll"+nowGoodList.CID)).length>0){
		console.log("#scroll"+nowGoodList.CID+"存在")
		return
	}else{
		console.log("#scroll"+nowGoodList.CID+"不存在")
		var html = '<div id="scroll'+ nowGoodList.CID +'" class="mui-scroll-wrapper">'
					+'<div class="mui-scroll">'
					+'<ul class="mui-table-view" id="ul'+ nowGoodList.CID +'">'
					+'</ul>'
					+'</div>'
					+'</div>';
		// console.log(html);
		$("#item"+nowGoodList.CID).append(html);
		$('#scroll'+nowGoodList.CID).scroll({
			indicators: true //是否显示滚动条
		});

		mui.each(nowGoodList.ProList,function(index,item){
			var li = '<li onClick="toGoods('+item.ProID+')" class="mui-table-view-cell mui-media">'
					+'<img class="mui-pull-left my-media-img" src="'+item.PhotoCategory+'">'
					+'<div class="my-media-content">'
					+'<div class="mui-media-body">'
					+item.ProName
					+'<p class="mui-ellipsis my-content-p">'+ item.SubTitle +'</p>'
					+'</div>'
					+'<div class="my-media-price">'
					+'&yen;'+item.PriceSale
					+'</div>'
					+'</div>'
					+'</li>';
			$('#ul'+nowGoodList.CID ).append(li);	

		})
	}
}

/**
 * tab的点击事件
 */
function tapTab(){
	mui('#segmentedControl').on('tap','a',function(){
		var index = this.getAttribute("data-index");
		console.log(index)
		getGoodsList(index,allGood[index].CID);
	}) 
}



function setCID(CID){
	
	if(CID == 3){
		
		getGoodsList(1,CID);
		$("#segmentedControl a:eq(0)").removeClass("mui-active");
		$("#segmentedControl a:eq(1)").addClass("mui-active");
	}
	
	
	
}



getGoodsTitle();

});


function toGoods(ProID){
	
	window.location.href="goods.html?ProID="+ProID+"&UserID="+UserID+"&Token="+Token;
	
}


