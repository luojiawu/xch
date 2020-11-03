var serverUrl = "https://api2.sppxw.cn";
//var serverUrl = "https://api1.sppxw.com";
//h5支付返回的页面
var redirect_url = "http%3A%2F%2Fmove.ehai.xyz%2Fmove%2Fpages%2FpayCallback.html";


var homeUrl = serverUrl+'/ywapp/home/';
var orders = serverUrl+'/YwApp/Orders/';
var ypc = serverUrl+'/YwApi/WxPay/YPC/';
var ordersD = serverUrl+"/YwApp/Orders/d/";
var orderDetails = serverUrl+'/YwApp/Orders/OrderDetails/';
var coupon = serverUrl+'/ywapp/Coupon/';
var category = serverUrl+'/ywapp/category/';
var product = serverUrl+'/ywapp/product/';
var picPreviewUrl = serverUrl+'/ywapp/orders/PicPreview/';				
var imgDo = serverUrl+'/ImgDo/';
var ordersA = serverUrl+'/YwApp/Orders/a/';
var orderPreview = serverUrl+'/YwApp/Orders/OrderPreview/';
var linkinfoUrl = serverUrl+'/cli/linkinfo/';
var linkinfoD = serverUrl+"/cli/linkinfo/d/";
var linkinfoA = serverUrl+"/cli/linkinfo/a/";
var bookPreview = serverUrl+'/ywapp/orders/BookPreview/';
var bookTemps = serverUrl+'/ywapp/orders/BookTemps/';
var books = serverUrl+'/ywapp/orders/Books/';
var articles = serverUrl+'/ywapp/orders/Articles/';
var articleTemp = serverUrl+'/art/ArticleTemp/';
var artUrl = serverUrl+'/Public/Art/';

var followA = serverUrl+'/App/Art/Follow/A/';
var artCommentD = serverUrl+'/App/Art/ArtComment/D/';
var artCommentLike = serverUrl+'/App/Art/ArtComment/Like/';
var artComment = serverUrl+'/App/Art/ArtComment/';
var articleCommentA = serverUrl+'/art/ArticleComment/a/';
var artLikeA = serverUrl+'/App/Art/ArtLike/A/';

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

/* 获取url上的参数 */
function getQueryVariable(variable){
   var query = window.location.search.substring(1);
   var vars = query.split("&");
   for (var i=0;i<vars.length;i++) {
           var pair = vars[i].split("=");
           if(pair[0].toUpperCase() == variable.toUpperCase()){return pair[1];}
   }
   return(false);
}