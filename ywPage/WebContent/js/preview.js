
var artList;
var artid = getQueryVariable("artid");
var tempData;

var action = "hide";



function respHandle(type,data){
	
	if(type == "Temp"){
		
		getArticleTemp(data);
		
	}else if(type == "setArtContent"){
		setArtContent(data);
		
	}
	
	
	
	
	
}

function getArticleTemp(ATID){
	if(ATID == 0){
		
		$(".header .userInfo .title").css("color","rgb(0, 0, 0)");
		$(".author").css("color","rgb(0, 0, 0)");
		$(".content,.comment").css("color","rgb(0, 0, 0)");
		$(".header,.content").css("background-image", "none");
		$("body").css("background-color", "white");
		
	}else{
		
		mui.ajax(articleTemp,{
			data:{
				ATID: ATID
			},
			dataType:'json',//服务器返回json格式数据
			type:'get',//HTTP请求类型
			timeout:10000,//超时时间设置为10秒；
			success:function(data){
				console.log(data);
				var mydata = data;
				if(mydata.code == 0){
					tempData = mydata.data[0];
					console.log(tempData);
					setArticleTemp();
				}
			},
			error:function(xhr,type,errorThrown){
				
			}
		});
		
	}
	
	
}


function setArticleTemp(){
	var titleColor = "#"+tempData.TitleColor;
	var authorColor = "#"+tempData.AuthorColor;
	var contentColor = "#"+tempData.ContentColor;
	var tempBackground = tempData.TempBackground;
	var tempColorTwo = "#"+tempData.TempColorTwo;
	$(".header .userInfo .title").css("color",titleColor);
	$(".author").css("color",authorColor);
	$(".content").css("color",contentColor);
	$(".header,.content").css("background-image", "url(\'"+tempBackground+"\')");
	$("body").css("background-color", tempColorTwo);
}

function setFontType(fontType){
	
	mui.ajax(articleTemp,{
		data:{
			ATID: ATID
		},
		dataType:'json',//服务器返回json格式数据
		type:'get',//HTTP请求类型
		timeout:10000,//超时时间设置为10秒；
		success:function(data){
			console.log(data);
			var mydata = data;
			if(mydata.code == 0){
				tempData = mydata.data[0];
				console.log(tempData);
				setArticleTemp();
			}
		},
		error:function(xhr,type,errorThrown){
			
		}
	});
	
	
	
}

function getArt(){
	mui.ajax(artUrl,{
		data:{
			ArtID: artid
		},
		dataType:'json',//服务器返回json格式数据
		type:'get',//HTTP请求类型
		timeout:10000,//超时时间设置为10秒；
		success:function(data){
			console.log(data)
			var mydata = data;
			if(mydata.code == 0){
				artList = mydata.data[0];
				console.log("artList",artList);
				
				setArtTitle();
				setArtContent(-1);
				getArticleTemp(artList.ATID);
				attentionGap = $(".attention").offset().top;
				
			}
		},
		error:function(xhr,type,errorThrown){
			
		}
	});
}

function setArtTitle(){
	$(".userInfo").empty();
	console.log(artList.TimeCreate)
	// mui.each(artList.Content)
	/*nickname = artList.UserName;
	headimgurl = artList.WxHeadImgUrl;
	CommentCount = artList.CommentCount;
	LikeCount = artList.LikeCount;*/
	
	var html = '<img onclick="headImg()" src="'+ artList.WxHeadImgUrl +'" >'
				+'<div class="author">'+ artList.UserName +'</div>'
				+'<div class="attention" onclick="attention()">关注</div>'
				+'<div class="title">'+ artList.Title +'</div>'
				+'<div class="author"><span>'+ artList.TimeCreate +'</span><span>阅读'+ artList.ReadCount +'</span></div>';
	$(".userInfo").append(html);
}


function setArtContent(fontType){
	$(".content").empty();
	if(fontType == -1){
		fontType = artList.FontType;
	}
	 mui.each(artList.Content,function(index,item){
		 var contentList;
		 
		 if(fontType == 0){
			 contentList = '<div><p>'+item.text+'</p>'
				+'<img src="'+ item.photo +'"/></div>';
		 }else if(fontType == 1){
			 contentList = '<div><img src="'+ item.photo +'"/>'
				+'<p>'+item.text+'</p></div>';
		 }
		 
		 
		$(".content").append(contentList);
	 })
}



function attention(){
	
	respHandle("getArt",1);
}


$(function(){
	
	
	

	
	
	
	
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
	
	
	
	
	
	$(window).scroll(function(event){
		   var gap = $(window).scrollTop()+30;
		   if(gap>attentionGap && action == "hide"){
			   
			   action = "show";
			   console.log("显示");
			   
			   if(os.isPhone){
					 window.webkit.messageHandlers.topControl.postMessage({"action":action});
				}else if(os.isAndroid){
					window.WebViewJavascriptBridge.callHandler("topControl", {"action":action}
							, function(responseData) {
						console.log(responseData);
					});
				}else if(os.isPc){
					
				}
			   
			  
			   
		   }else if(gap<attentionGap && action == "show"){
			  
			   action = "hide";
			   console.log("隐藏");
			   if(os.isPhone){
					 window.webkit.messageHandlers.topControl.postMessage({"action":action});
				}else if(os.isAndroid){
					window.WebViewJavascriptBridge.callHandler("topControl", {"action":action}
							, function(responseData) {
						console.log(responseData);
					});
				}else if(os.isPc){
					
				}
			   
		   }
	});
	
	
	
	
	
	
	
	
	
	
	
	
	
		
	
	
	
	getArt();
	
	
	
	
	
})

