var artList;
var UserID = getQueryVariable("UserID");
var Token = getQueryVariable("Token");
var artid = getQueryVariable("artid");
var commentList;
var data;
var attentionGap;
var nickname;
var headimgurl;
//是否用户自己
var isUser = 0;
//评论数
var CommentCount;
//点赞次数
var LikeCount;


var action = "hide";
//判断设备
var os = function() {  
     var ua = navigator.userAgent,  
     isWindowsPhone = /(?:Windows Phone)/.test(ua),  
     isSymbian = /(?:SymbianOS)/.test(ua) || isWindowsPhone,   
     isAndroid = /(?:Android)/.test(ua),   
     isFireFox = /(?:Firefox)/.test(ua),   
     isChrome = /(?:Chrome|CriOS)/.test(ua),  
     isTablet = /(?:iPad|PlayBook)/.test(ua) || (isAndroid && !/(?:Mobile)/.test(ua)) || (isFireFox && /(?:Tablet)/.test(ua)),  
     isPhone = /(?:iPhone)/.test(ua) && !isTablet,  
     isPc = !isPhone && !isAndroid && !isSymbian;  
     return {  
          isTablet: isTablet,  
          isPhone: isPhone,  
          isAndroid : isAndroid,  
          isPc : isPc  
     };  
}();  

var photoList = new Array();

function copy(text){
	
	var clipboard = new Clipboard(text); 
     clipboard.on('success', function(e) { 
           // console.info('Action:', e.action); 
           // console.info('Text:', e.text);
           // console.info('Trigger:', e.trigger);
           // alert("复制成功");
           
           e.clearSelection(); 
       });
    
	
}





function toMenu(ACID){
	
	$(".menu").hide();
	$("#menu_"+ACID).show();
	event.stopPropagation();
	
}

$(document).bind("click", function (e) {
	$(".menu").hide();
})

function respHandle(type,data){
	
	if(type == "ArtLike"){
		
		$('#zan').toggleClass('whiteButton');
		$('#zan').toggleClass('zan');
		
		if(data == 1){
			console.log("点赞了")
			//alert("data.code-->"+data.code);
			var count = artList.LikeCount + 1;
			$('#zan').html('<img alt="" src="../img/like.png">'+count);
			
			var ArtLike = {"ArtID":artList.ArtID,"IsType":1};
		
		}else if(data == 2){
			console.log("取消点赞了")
			//alert("data.code-->"+data.code);
			var count = artList.LikeCount;
			$('#zan').html('<img alt="" src="../img/like.png">'+count);
			var ArtLike = {"ArtID":artList.ArtID,"IsType":0};
		}
		
		  
	}else if(type == "Comment"){
		
		getComment();
		
		
	}else if(type == "ReplyMore"){
		
		getComment();
		
	}else if(type == "attention"){
		
		if(data == 1){
			$(".attention").css({"background-color":"#78d5ff","color":"#2a516d"});
			$(".attention").text("已关注");
		}else if(data == 2){
			$(".attention").css({"background-color":"#ffc300","color":"#044c7e"});
			$(".attention").text("关注");
		}
		
		
		
	}
	
	
}


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

function photoAmplify(photoNum){
	
	var photoAmplify = {"photoList":photoList,"photoNum":photoNum};
	
	if(os.isPhone){ 
		window.webkit.messageHandlers.photoAmplify.postMessage(photoAmplify);
	}else if(os.isAndroid){
		window.WebViewJavascriptBridge.callHandler("photoAmplify", photoAmplify
				, function(responseData) {
			console.log(responseData);
		});
	}
	
}

function attention(){
	var isType;
	if($(".attention").text() == "关注"){
		isType = 1;
		
		
		
	}else if($(".attention").text() == "已关注"){
		isType = 2;
		$(".attention").css({"background-color":"#ffc300","color":"#044c7e"});
		$(".attention").text("关注");
	}
	
	
	mui.ajax(followA,{
		data:{"Token":Token,"UserID":UserID,"IsType":isType,"FUserID":artList.UserID},
		dataType:'json',//服务器返回json格式数据
		type:'get',//HTTP请求类型
		timeout:10000,//超时时间设置为10秒；
		success:function(data){
			console.log("关注的结果",data);
			if(data.code == 1){
				if(isType == 1){
					$(".attention").css({"background-color":"#78d5ff","color":"#2a516d"});
					$(".attention").text("已关注");
					
				}else if(isType == 2){
					$(".attention").css({"background-color":"#ffc300","color":"#044c7e"});
					$(".attention").text("关注");
					
				}
				
				
				if(os.isPhone){ 
					window.webkit.messageHandlers.attention.postMessage({"IsType":isType});
				}else if(os.isAndroid){
					window.WebViewJavascriptBridge.callHandler("attention", {"IsType":isType}
							, function(responseData) {
						alert("安卓返回参数-->"+responseData);
					});
				}
			}
			
		},
		error:function(xhr,type,errorThrown){
			
		}
	});
	
	
	
	
}

function toReplyMore(ACID,UserID,UserName){
	
	var ReplyMore = {"ACID":ACID,"UserID":UserID,"UserName":UserName};
	if(os.isPhone){ 
		window.webkit.messageHandlers.ReplyMore.postMessage(ReplyMore);
	}else if(os.isAndroid){
		window.WebViewJavascriptBridge.callHandler("ReplyMore", ReplyMore
				, function(responseData) {
			console.log(responseData);
		});
	}
	
	
}

function deleteComment(ACID){

	mui.ajax(artCommentD,{
		data:{"Token":Token,"UserID":UserID,"ArtID":artid,"ACID":ACID},
		dataType:'json',//服务器返回json格式数据
		type:'get',//HTTP请求类型
		timeout:10000,//超时时间设置为10秒；
		success:function(data){
			console.log("关注的结果",data);
			if(data.code == 1){
				getComment();
			}
			
		},
		error:function(xhr,type,errorThrown){
			
		}
	});
	
	
	
}

function headImg(){
	
	if(os.isPhone){ 
		
		window.webkit.messageHandlers.headImg.postMessage("");
	}else if(os.isAndroid){
		window.WebViewJavascriptBridge.callHandler("headImg", {}, function(responseData) {
			console.log(responseData);
		});
		
		
	}
	
}

function recommend(artid){
	
	if(os.isPhone){ 
		
		window.webkit.messageHandlers.recommend.postMessage({"ArtID":artid});
	}else if(os.isAndroid){
		window.WebViewJavascriptBridge.callHandler("recommend", {"ArtID":artid}, function(responseData) {
			console.log(responseData);
		});
		
		
	}
	
	
}

function toCommentZan(e,ACID){
	//点赞类型，默认是没有点。
	var IsType;
	var i = e.outerHTML.indexOf("png\">");
	var j = e.outerHTML.indexOf("</button>");
	var text = e.outerHTML.substring(i+5, j);
	var lc;
	//alert(text);
	if(e.outerHTML.indexOf("like.png") >= 0){
		IsType = 1;
		if(text == "点赞"){
			lc = 1;
		}
		
		           
		mui.ajax(artCommentLike,{
			data:{
				
				UserID:UserID,
				Token:Token,
				ACID: ACID,
				IsType: IsType
			},
			dataType:'json',//服务器返回json格式数据
			type:'get',//HTTP请求类型
			timeout:10000,//超时时间设置为10秒；
			success:function(data){
				console.log("评论点赞",data)
				if(data.code != 1){
					
						$(e).html('<img class="likeFont" src="../img/like_1.png">'+lc+'');
					
					
				}
			},
			error:function(xhr,type,errorThrown){
				
			} 
		});
		
	}else{
		IsType = 2;
		if(text == 1){
			lc = "点赞";
		}else{
			lc--;
		}
		
		mui.ajax(artCommentLike,{
			data:{
				
				UserID:UserID,
				Token:Token,
				ACID: ACID,
				IsType: IsType
			},
			dataType:'json',//服务器返回json格式数据
			type:'get',//HTTP请求类型
			timeout:10000,//超时时间设置为10秒；
			success:function(data){
				console.log("评论点赞",data)
				if(data.code != 1){
					//如果不成功，则将点赞状态改为原来
					
					$(e).html('<img class="likeFont" src="../img/like.png">'+lc+'');
					
					
				}
			},
			error:function(xhr,type,errorThrown){
				
			} 
		});
		
		
		
	}
	
	
	
	
	
	
}

function getComment(){
	mui.ajax(artComment,{
		data:{
			//WxOpenID: openid,
			UserID:UserID,
			Token:Token,
			ArtID: artid,
			PageNo: 1,
			PageSize: 5
		},
		dataType:'json',//服务器返回json格式数据
		type:'get',//HTTP请求类型
		timeout:10000,//超时时间设置为10秒；
		success:function(data){
			console.log("获取评论",data)
			if(data.code == 1){
				commentList = data.data.CommentList;
				
				console.log("commentList",commentList);
				setComment();
				
			}
		},
		error:function(xhr,type,errorThrown){
			
		} 
	});
}

function setComment(){
	$(".comment-2-comment").remove();
	//用于加底部距离
	var bottom = "0";
	mui.each(commentList,function(index,item){
		var lc = item.LikeCount;
		if(lc == 0){
			lc = "点赞";
		}
		var il = item.IsLike;
		if(il == 0){
			il = "../img/like.png";
		}else{
			il = "../img/like_1.png";
		}
		
		var comment;
		if(index+1 == commentList.length){
			bottom = "10vw";
		}
		if(item.UserID == UserID){
			
			comment = '<div class="comment-2-comment"><div class="comment-2-comment-left"><img src="'+ item.HeadImg +'" style="border-radius: 98px;" ></div>'
			+'<div style="margin-bottom:'+bottom+';" class="comment-2-comment-right"><div><div style="width: 66vw;color: #7bbbff;">'+ item.UserName +'<span style="margin-left: 1vw;color: #FFEB3B;font-size: 3vw;border: 1px solid #FFEB3B;padding: 0 1vw;">自己</span></div></div>'
			+'<div onclick="toMenu('+item.ACID+')" style="word-break: break-all;word-wrap: break-word;position: relative;">'+ item.ContentStr +'<div class="menu" id="menu_'+item.ACID+'"><span  onclick="toReplyMore('+item.ACID+','+item.UserID+',\''+item.UserName+'\')">回复</span><i>|</i><span onclick="copy(\''+item.ContentStr+'\')">复制</span><i>|</i><span onclick="deleteComment('+item.ACID+')">删除</span><img src="../img/xsj.png"></div></div>'
			+'<div style="font-size: 3vw;color: #9b9b9b;border-bottom: solid 1px #f1f1f12b;padding-bottom: 2vw;margin-bottom: 4vw;">'
			+item.CreateTimeStr
			+'<button onclick="toCommentZan(this,'+item.ACID+')"><img class="likeFont" src="'+il+'">'+lc+'</button>'
			+'<button onclick="toReplyMore('+item.ACID+','+item.UserID+',\''+item.UserName+'\')"><img class="infoFont" src="../img/info_2.png">回复</button>'
			+'<div class="comment-2-'+item.ACID+'" style="font-size: 4vw; color: white; background-color: #ffffff21; border-radius: 2vw;"></div></div></div></div>';
			
		}else{
			comment = '<div class="comment-2-comment"><div class="comment-2-comment-left"><img src="'+ item.HeadImg +'" style="border-radius: 98px;" ></div>'
			+'<div style="'+bottom+'" class="comment-2-comment-right"><div><div style="width: 66vw;color: #7bbbff;">'+ item.UserName +'</div></div>'
			+'<div style="word-break: break-all;word-wrap: break-word;">'+ item.ContentStr +'</div>'
			+'<div style="font-size: 3vw;color: #9b9b9b;border-bottom: solid 1px #f1f1f12b;padding-bottom: 2vw;margin-bottom: 4vw;">'
			+item.CreateTimeStr
			+'<button onclick="toCommentZan(this,'+item.ACID+')"><img class="likeFont" src="'+il+'">'+lc+'</button>'
			+'<button onclick="toReplyMore('+item.ACID+','+item.UserID+',\''+item.UserName+'\')"><img class="infoFont" src="../img/info_2.png">回复</button>'
			+'<div class="comment-2-'+item.ACID+'" style="font-size: 4vw; color: white; background-color: #ffffff21; border-radius: 2vw;"></div></div></div></div>';
			
		}
		
		
		
		
		
		$(".comment-2").append(comment);
		var ACID = item.ACID;
		mui.each(item.ReplyList,function(index,item){
			var reply;
			
			if(item.UserType == 1){
				reply = '<div style="word-break: break-all;word-wrap: break-word;"><span style="color: #7bbbff;">'+item.UserName+'</span><span style="margin-left: 1vw;color: #FFEB3B;font-size: 3vw;border: 1px solid #FFEB3B;padding: 0 1vw;">作者</span>：'+item.Content+'</div>';
			}else{
				reply = '<div style="word-break: break-all;word-wrap: break-word;"><span style="color: #7bbbff;">'+item.UserName+'</span>：'+item.Content+'</div>';
			}
			
			$(".comment-2-"+ACID).append(reply);
			$(".comment-2-"+ACID).css("padding","2vw");
		})
		
		
	})
}



$("#TimelineShare").on("click",function(){
	
	if(os.isPhone){ 
		window.webkit.messageHandlers.TimelineShare.postMessage("");
	}else if(os.isAndroid){
		window.WebViewJavascriptBridge.callHandler("TimelineShare", {}
				, function(responseData) {
			console.log(responseData);
		});
	}
	
})

$("#AppMessageShare").on("click",function(){
	
	if(os.isPhone){ 
		window.webkit.messageHandlers.AppMessageShare.postMessage("");
	}else if(os.isAndroid){
		window.WebViewJavascriptBridge.callHandler("AppMessageShare", {}
				, function(responseData) {
			console.log(responseData);
		});
	}
	
	
})



$(function(){
	
	mui.init({
		swipeBack: true ,//启用右滑关闭功能
		
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
	
	function sendArt(){
		
		var shareUrl = "http://move.ehai.xyz/move/pages/artdetails.html?UserID="+UserID+"&Token="+Token+"&artid="+artid;
		
		var content = "";
		
		if(artList.UserID == UserID){
			
			isUser = 1;
		}
		
		mui.each(artList.Content,function(index,item){
			if(item.text != ""){
				content = item.text;
				return false;
			}
		 })
		
		data = {"nickname":artList.UserName,"headimgurl":artList.WxHeadImgUrl,"CommentCount":artList.CommentCount,"LikeCount":artList.LikeCount,"Title":artList.Title,
			"PhotoCover":artList.PhotoCover,"isUser":isUser,"content":content,"shareUrl":shareUrl,"FUserID":artList.UserID};
		if(os.isPhone){ 
			
			window.webkit.messageHandlers.sendArt.postMessage(data);
			
		}else if(os.isAndroid){
			window.WebViewJavascriptBridge.callHandler("sendArt", data
					, function(responseData) {
				console.log(responseData);
			});
			
			
		}
		
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
					console.log("artList",artList)
					
					setArtTitle();
					setArtContent();
					setArtBottom();	
					getArticleTemp(artList.ATID);
					attentionGap = $(".attention").offset().top;
					sendArt();
					
					
				}
			},
			error:function(xhr,type,errorThrown){
				
			}
		});
	}
	
	function setArtTitle(){
		console.log(artList.TimeCreate)
		// mui.each(artList.Content)
		/*nickname = artList.UserName;
		headimgurl = artList.WxHeadImgUrl;
		CommentCount = artList.CommentCount;
		LikeCount = artList.LikeCount;*/
		
		var html = '<img onclick="headImg()" src="'+ artList.WxHeadImgUrl +'" >'
					+'<div>'+ artList.UserName +'</div>'
					+'<div class="attention" onclick="attention()">关注</div>'
					+'<div class="title">'+ artList.Title +'</div>'
					+'<div><span>'+ artList.TimeCreate +'</span><span>阅读'+ artList.ReadCount +'</span></div>';
		$(".userInfo").append(html);
	}
	
	
	function setArtContent(){
		var photoNum = 0;
		 mui.each(artList.Content,function(index,item){
			 var contentList;
			 if(item.photo != ""){
				 photoNum++;
				 photoList.push(item.photo);
				 //处理文字图片位置
				 if(artList.FontType == 0){
					 contentList = '<div><p>'+item.text+'</p>'
						+'<img onclick="photoAmplify('+photoNum+')" src="'+ item.photo +'"/></div>';
					 
				 }else if(artList.FontType == 1){
					 contentList = '<div><img onclick="photoAmplify('+photoNum+')" src="'+ item.photo +'"/>'
						+'<p>'+item.text+'</p></div>';
				 }
				 
			 }else{
				//处理文字图片位置
				 if(artList.FontType == 0){
					 contentList = '<div><p>'+item.text+'</p>'
						+'<img src="'+ item.photo +'"/></div>';
				 }else if(artList.FontType == 1){
					 contentList = '<div><img src="'+ item.photo +'"/>'
						+'<p>'+item.text+'</p></div>';
				 }
				 
				 
				 
			 }
			 
			 
			$(".content").append(contentList);
		 })
	}
	
	function setArtBottom(){
		var html = '<button class="whiteButton" id="zan"><img alt="" src="../img/like.png">'+ artList.LikeCount +'</button>'
		$("#threeBtn").prepend(html)
	}
	
	
	
	function openCommentArea(){
		mui('.comment-2-write').on('tap','.comment-2-write-right',function(){
			//唤起评论框的，改为调用APP，先注释
			//mui('#popover').popover('toggle',document.getElementById("writeComment"));
			var Comment = {"ArtID":artid};
			if(os.isPhone){ 
				window.webkit.messageHandlers.Comment.postMessage(Comment);
			}else if(os.isAndroid){
				window.WebViewJavascriptBridge.callHandler("Comment", Comment
						, function(responseData) {
					console.log(responseData);
					getComment();
					
				});
			}
			
		})
	}
	
	function upLoadComment(){
		mui('.input-div').on('tap','button',function(event){
			event.detail.gesture.preventDefault();//阻止默认事件
			mui.focus($('#theComment'));
			// e.preventDefault();
			var value =  $('#theComment').val();
			$('#theComment').val("");
			mui('#popover').popover('toggle',document.getElementById("writeComment"));
			console.log(value);
			
			mui.ajax(articleCommentA,{
				data:{
					//WxOpenID: openid,
					UserID:UserID,
					Token:Token,
					ACID: 0,
					ArtID: artid,
					Content: value,
				},
				dataType:'json',//服务器返回json格式数据
				type:'post',//HTTP请求类型
				timeout:10000,//超时时间设置为10秒；
				success:function(data){
					console.log("提交评论的结果",data);
					getComment();
					 var target_top = $(".comment").offset().top;
					 //$("html,body").animate({scrollTop: target_top}, 1000);  //带滑动效果的跳转
					 $("html,body").scrollTop(target_top);
				},
				error:function(xhr,type,errorThrown){
					
				}
			});
		})
	}
	
		
	function clickZan(){
		mui('#threeBtn').on('tap','#zan',function(){
			// $('#zan').removeClass('whiteButton');
			$('#zan').toggleClass('whiteButton');
			$('#zan').toggleClass('zan');
			var IsType;
			if($('#zan').hasClass('zan')){
				console.log("点赞了")
				var count = artList.LikeCount + 1;
				$('#zan').html('<img alt="" src="../img/like.png">'+count);
				IsType = 1;
				//ArtLike = {"ArtID":artList.ArtID,"IsType":1};
			
			}else if($('#zan').hasClass('whiteButton')){
				console.log("取消点赞了")
				var count = artList.LikeCount;
				$('#zan').html('<img alt="" src="../img/like.png">'+count);
				IsType = 2;
			}
			
			mui.ajax(artLikeA,{
				data:{"Token":Token,"UserID":UserID,"IsType":IsType,"ArtID":artList.ArtID},
				dataType:'json',//服务器返回json格式数据
				type:'get',//HTTP请求类型
				timeout:10000,//超时时间设置为10秒；
				success:function(data){
					console.log("文章点赞的结果",data);
					/*alert("Token-->"+Token+",UserID-->"+UserID);
					alert("安卓返回参数-->"+data.code+",msg-->"+data.msg);*/
					if(data.code == 1){
						if(os.isPhone){ 
							window.webkit.messageHandlers.ArtLike.postMessage({"IsType":IsType,"ArtID":artList.ArtID});
						}else if(os.isAndroid){
							window.WebViewJavascriptBridge.callHandler("ArtLike", {"IsType":IsType,"ArtID":artList.ArtID}
									, function(responseData) {
							});
						}
					}else{
						
						$('#zan').toggleClass('whiteButton');
						$('#zan').toggleClass('zan');
						
						if($('#zan').hasClass('whiteButton')){
							console.log("取消点赞了")
							var count = artList.LikeCount;
							$('#zan').html('<img alt="" src="../img/like.png">'+count);
							var ArtLike = {"ArtID":artList.ArtID,"IsType":2};
						}
						
					}
					
				},
				error:function(xhr,type,errorThrown){
					
				}
			});
			
			
			
			
			
			
			
			
		})
	}
	
	function clickCommentZan(){
		mui('#threeBtn').on('tap','#zan',function(){
			// $('#zan').removeClass('whiteButton');
			$('#zan').toggleClass('whiteButton');
			$('#zan').toggleClass('zan');
			
			if($('#zan').hasClass('zan')){
				console.log("点赞了")
				var count = artList.ReadCount + 1;
				$('#zan').html('<img alt="" src="../img/like.png">'+count);
				
				var ArtLike = {"ArtID":artList.ArtID,"IsType":1};
				
			}else if($('#zan').hasClass('whiteButton')){
				console.log("取消点赞了")
				var count = artList.ReadCount;
				$('#zan').html('<img alt="" src="../img/like.png">'+count);
				var ArtLike = {"ArtID":artList.ArtID,"IsType":2};
			}
			
			if(os.isPhone){ 
				window.webkit.messageHandlers.ArtLike.postMessage(ArtLike);
			}else if(os.isAndroid){
				window.WebViewJavascriptBridge.callHandler("ArtLike", ArtLike
						, function(responseData) {
					console.log(responseData);
				});
			}
			
			
		})
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
		$(".content,.comment").css("color",contentColor);
		$(".header,.content").css("background-image", "url(\'"+tempBackground+"\')");
		$("body").css("background-color", tempColorTwo);
		
		//$("body").css("background-image", "url(\'"+tempBackground+"\')");
		//$(".buttonDiv,.artList,.comment").css("background-color",tempColorTwo);
		
	}
	
	
	
	getArt();
	getComment();
	clickZan();
	openCommentArea();
	upLoadComment();
	
	
	
	
})

