$(function(){
	
	var WxOpenID = getQueryVariable("WxOpenID");
	var UserID = getQueryVariable("UserID");
	var Token = getQueryVariable("Token");
	var BookID = getQueryVariable("BookID");
	var RootCount = getQueryVariable("RootCount");
	var CouID = getQueryVariable("CouID");
	var LIID = getQueryVariable("LIID");
	
	var Province; 
	var City; 
	var District;
	
	selCity();
	getLinkinfo();
	function getLinkinfo(){			
		mui.ajax(linkinfoUrl+'?UserID='+UserID+"&Token="+Token,{
			dataType:'json',//服务器返回json格式数据
			type:'get',//HTTP请求类型
			timeout:10000,//超时时间设置为10秒；
			success:function(data){
				console.log(data)
				var mydata = data;
				if(mydata.code == 0){
					linkinfo = mydata.data;
					console.log("linkinfo",linkinfo)
					if(LIID != 0){
						setLinkinfo();
					}
					
				}
			},
			error:function(xhr,type,errorThrown){
				
			}
		});
	}
	
	function setLinkinfo(){
		
		mui.each(linkinfo,function(index,item){
			
			if(LIID == item.LIID){
				
				$("#siteId").val(item.LIID);
				$("#name").val(item.Man);
				$("#phone").val(item.Phone);
				$("#sel_city").html(item.Province+"  "+item.City+"  "+item.District);
				$("#site").val(item.Address);
				
				//如果默认选择为1，则把默认打开的按钮设置为打开
				if(item.IsDefault == 1){
					mui("#mySwitch").switch().toggle();	
				} 
				Province = item.Province; 
				City = item.City; 
				District = item.District;
				
			}
			
			
		})
		
	}
	
	
	$("#addSite").on("click",function(){
		
		var siteId = $("#siteId").val();
		var name = $("#name").val();
		var phone = $("#phone").val();
		var site = $("#site").val();
		var IsDefault = document.getElementById("mySwitch").classList.contains("mui-active") == true?1:0;
		
		var pattern = /^1[34578]\d{9}$/;
        
		
		//提交前的验证
		if(name == ""){
			alert("请输入姓名");
		}else if(!pattern.test(phone)){
			alert("请输入正确的联系电话");
		}else if(Province == null || City == null || site == ""){
			alert("请输入地址");
		}else{
			
			District = District == ""?City:District;
			
			var data = "UserID="+UserID+"&Token="+Token+"&LIID="+siteId+"&Man="+name+"&Phone="+phone+"&Province="+Province+"&City="+City+"&District="+District+"&Address="+site+"&IsDefault="+IsDefault;
			
			$.ajax({
				type: "GET",
		        url: linkinfoA+"?"+data,
		        async: false,
		        dataType:"json",
		        success: function (config) {
		        	
		        	if(config.code == 0){
		        		
		        		window.location.href="siteList.html?UserID="+UserID+"&Token="+Token+"&BookID="+BookID+"&RootCount="+RootCount+"&CouID="+CouID;
						
		        	}
		        }	
					
			});  
			
			
			
			
			
		}
		
		
	});
	
	
	
	
	function selCity(){
		
		var nameEl = document.getElementById('sel_city');
		
		var first = []; /* 省，直辖市 */
		var second = []; /* 市 */
		var third = []; /* 镇 */
		
		var selectedIndex = [0, 0, 0]; /* 默认选中的地区 */
		
		var checked = [0, 0, 0]; /* 已选选项 */
		
		
		
		creatList(city, first);
		
		if (city[selectedIndex[0]].hasOwnProperty('sub')) {
		  creatList(city[selectedIndex[0]].sub, second);
		} else {
		  second = [{text: '', value: 0}];
		}
		
		if (city[selectedIndex[0]].sub[selectedIndex[1]].hasOwnProperty('sub')) {
		  creatList(city[selectedIndex[0]].sub[selectedIndex[1]].sub, third);
		} else {
		  third = [{text: '', value: 0}];
		}
		
		var picker = new Picker({
		    data: [first, second, third],
		  selectedIndex: selectedIndex,
		    title: '选择地区'
		});
		
		picker.on('picker.select', function (selectedVal, selectedIndex) {
		  var text1 = first[selectedIndex[0]].text;
		  var text2 = second[selectedIndex[1]].text;
		  var text3 = third[selectedIndex[2]] ? third[selectedIndex[2]].text : '';
			
		  Province = text1; 
		  City = text2; 
		  District = text3;
		  
		    nameEl.innerText = text1 + ' ' + text2 + ' ' + text3;
		});
		
		picker.on('picker.change', function (index, selectedIndex) {
		  if (index === 0){
		    firstChange();
		  } else if (index === 1) {
		    secondChange();
		  }
		
		  function firstChange() {
		    second = [];
		    third = [];
		    checked[0] = selectedIndex;
		    var firstCity = city[selectedIndex];
		    if (firstCity.hasOwnProperty('sub')) {
		      creatList(firstCity.sub, second);
		
		      var secondCity = city[selectedIndex].sub[0]
		      if (secondCity.hasOwnProperty('sub')) {
		        creatList(secondCity.sub, third);
		      } else {
		        third = [{text: '', value: 0}];
		        checked[2] = 0;
		      }
		    } else {
		      second = [{text: '', value: 0}];
		      third = [{text: '', value: 0}];
		      checked[1] = 0;
		      checked[2] = 0;
		    }
		
		    picker.refillColumn(1, second);
		    picker.refillColumn(2, third);
		    picker.scrollColumn(1, 0)
		    picker.scrollColumn(2, 0)
		  }
		
		  function secondChange() {
		    third = [];
		    checked[1] = selectedIndex;
		    var first_index = checked[0];
		    if (city[first_index].sub[selectedIndex].hasOwnProperty('sub')) {
		      var secondCity = city[first_index].sub[selectedIndex];
		      creatList(secondCity.sub, third);
		      picker.refillColumn(2, third);
		      picker.scrollColumn(2, 0)
		    } else {
		      third = [{text: '', value: 0}];
		      checked[2] = 0;
		      picker.refillColumn(2, third);
		      picker.scrollColumn(2, 0)
		    }
		  }
		
		});
		
		picker.on('picker.valuechange', function (selectedVal, selectedIndex) {
		  console.log(selectedVal);
		  console.log(selectedIndex);
		});
		
		nameEl.addEventListener('click', function () {
		    picker.show();
		});
		
	}
	
	function creatList(obj, list){
	  obj.forEach(function(item, index, arr){
	  var temp = new Object();
	  temp.text = item.name;
	  temp.value = index;
	  list.push(temp);
	  })
	}
	
	
	
	
	
	
	
})

  
