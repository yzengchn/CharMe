/*** 表单验证相关 ***/
"hssduc" in window || (window.hssduc = {}) , "validation" in hssduc || (hssduc.validation = {});
hssduc.validation = {
   "getLength":function(str){
	   var len = str.length;
	    var reLen = 0;
	    for (var i = 0; i < len; i++) {        
	        if (str.charCodeAt(i) < 27 || str.charCodeAt(i) > 126) {
	            // 全角    
	            reLen += 2;
	        } else {
	            reLen++;
	        }
	    }
	    return reLen;  
    }
}

"hssduc" in window || (window.hssduc = {}) , "page" in hssduc || (hssduc.page = {});
hssduc.page = {
   "addRefreshTab":function(menuName,dataUrl){
	   // 移除相同的tab
	   $(".J_menuTabs .J_menuTab").each(function(index){
		   if($(this).data('id')==dataUrl){
			   $(this).remove();
			   return false;
		   }
	   })
	   $('.J_mainContent .J_iframe').each(function () {
            if ($(this).data('id') == dataUrl) {
                $(this).remove();
                return false;
            }
	   });
	   //新增tab
	   var dataIndex = hssduc.math.getRandomInteger(1000,1100);
	   var str = '<a href="javascript:;" class="active J_menuTab" data-id="' + dataUrl + '">' + menuName + ' <i class="fa fa-refresh"></i>&nbsp;<i class="fa fa-times-circle"></i></a>';
       $('.J_menuTab').removeClass('active');

       // 添加选项卡对应的iframe
       var str1 = '<iframe class="J_iframe" name="iframe' + dataIndex + '" width="100%" height="100%" src="' + dataUrl + '" frameborder="0" data-id="' + dataUrl + '" seamless></iframe>';
       $('.J_mainContent').find('iframe.J_iframe').hide().parents('.J_mainContent').append(str1);
       // 添加选项卡
       $('.J_menuTabs .page-tabs-content').append(str);
    },
    "closeCurrentAddNew":function(menuName,dataUrl){
    	var $currentTab = $('.J_menuTabs .J_menuTab.active').eq(0);
    	var closeTabId = $currentTab.data('id');
    	$currentTab.remove();
        // 移除相应tab对应的内容区
        $('.J_mainContent .J_iframe').each(function () {
            if ($(this).data('id') == closeTabId) {
                $(this).remove();
                return false;
            }
        });
        hssduc.page.addRefreshTab(menuName,dataUrl);
    },
    "setLayerArea":function(width,height){//设置弹窗自适应
    	var winWidth = $(window).width();
    	if(!width){
    		if(winWidth>1600){
    			width = "50%";
    		}else 
    		if(winWidth>1300){
        		width = "60%";
        	}else 
    		if(winWidth>1000){
        		width = "70%";
        	}else 
        	if(winWidth>700){
        		width = "80%";
        	}else{
        		width = "90%";
        	}
    	}
    	if(!height){
    		height = "500px";
    	}
    	return [width,height];
    },
    "refleashListPage":function(){//刷新工序列表页面
    	$(top.document).contents().find(".J_iframe").each(function(){
			if($(this).data("id").indexOf("/roval/toList")!=-1||//审批
				$(this).data("id").indexOf("/approval/allocation")!=-1||//分单
				$(this).data("id").indexOf("/register/list")!=-1){//录单
				$(this)[0].contentWindow.location.reload();
			}
		});
    }
}

/**** 数据工具类 ****/
"hssduc" in window || (window.hssduc = {}) , "data" in hssduc || (hssduc.data = {});
hssduc.data = {
	"refleshTable":function(tableId){//刷新列表
		$("#"+tableId).bootstrapTable('refresh');
	},
	"procedureData":{//工序数据
		15:"录单中",16:"分单中",17:"初审中",18:"初审核实中",19:"审批中",20:"高级审批中",21:"联名审批中",22:"待签约",
		23:"待财务放款中",24:"待银行确认放款中",25:"完成",26:"取消",27:"拒绝",28:"撤销",29:"合约签署中",30:"自动取消",
		31:"车辆抵押信息核对及确认",32:"保险更新信息确认",33:"财务制表中",34:"财务放款审核中",35:"待平台确认放款中",40:"网签确认中",
		50:"公证/签约",60:"产权可抵押确认",70:"抵押确认中",110:"预录单",120:"初审核实中",130:"审批中",140:"签约中",150:"待放款"
	},
	"addressData":function(opts){//省市区三级联动
		var options = {
			contextPathUrl:"",
			field:"",
			provinceField:".addressProvince",
			cityField:".addressCity",
			districtField:".addressDistrict"
		};
	    options = $.extend(true,{},options,opts);
	    var $self = this;
	    var $addressProvince = $(options.provinceField);
		var $addressDistrict = $(options.districtField);
		var $addressCity = $(options.cityField);
		if(typeof(options.field)!="undefind"&&options.field!=""){
			$addressProvince = $(options.field).find(options.provinceField).eq(0);
			$addressDistrict = $(options.field).find(options.districtField).eq(0);
			$addressCity = $(options.field).find(options.cityField).eq(0);
		}
		this.initData = function(){
			$self.initProvinceData();
		}
		this.initProvinceData = function(){
			$self.getAjaxData("",1,function(data){
				$addressCity.empty();
				$addressDistrict.empty();
				$addressProvince.empty();
				$addressDistrict.append(
					"<option value=\"\">\----县/区---- \</option>");
				$addressCity.append(
					"<option value=\"\">\----市---- \</option>");
				$addressProvince.append(
					"<option value=\"\">\----省---- \</option>");
				$.each(data.data, function(i, item) {
					$addressProvince.append("<option value=\"" + item.id + "\">"+ item.name+ "</option>");
				});
				$addressProvince.off("change");
				$addressProvince.on("change",function(){
					$self.initCityData($(this).val());
				});
				if(!hssduc.util.stringUtils.isBlank($addressProvince.attr("data-val"))){
					$addressProvince.val($addressProvince.attr("data-val"));
					$self.initCityData($addressProvince.attr("data-val"));
					$addressProvince.removeAttr("data-val");
				}
			});
		}
		this.initCityData = function(parentId){
			$addressCity.empty();
			$addressDistrict.empty();
			$addressDistrict.append(
				"<option value=\"\">\----县/区---- \</option>");
			$addressCity.append(
				"<option value=\"\">\----市---- \</option>");
			if(hssduc.util.stringUtils.isBlank(parentId)){
				return;
			}
			$self.getAjaxData(parentId,2,function(data){
				
				$.each(data.data, function(i, item) {
					$addressCity.append("<option value=\"" + item.id + "\">"+ item.name+ "</option>");
				});
				$addressCity.off("change");
				$addressCity.on("change",function(){
					$self.initDistrictData($(this).val());
				});
				if(!hssduc.util.stringUtils.isBlank($addressCity.attr("data-val"))){
					$addressCity.val($addressCity.attr("data-val"));
					$self.initDistrictData($addressCity.attr("data-val"));
					$addressCity.removeAttr("data-val");
				}
			})
		}
		this.initDistrictData = function(parentId){
			$addressDistrict.empty();
			$addressDistrict.append(
				"<option value=\"\">\----县/区---- \</option>");
			if(hssduc.util.stringUtils.isBlank(parentId)){
				return;
			}
			$self.getAjaxData(parentId,3,function(data){
				$.each(data.data, function(i, item) {
					$addressDistrict.append("<option value=\"" + item.id + "\">"+ item.name+ "</option>");
				});
				if(!hssduc.util.stringUtils.isBlank($addressDistrict.attr("data-val"))){
					$addressDistrict.val($addressDistrict.attr("data-val"));
					$addressDistrict.removeAttr("data-val");
				}
			})
		}
		this.getAjaxData = function(parentId,level,callback){
			$.ajax({
				type : "POST",
				url : options.contextPathUrl+"/addressParam/getAddressParam",
				data : {
					"id":parentId,
					"addressLevel":level
				},
				dataType : 'json',
				success : function(data) {
					if(typeof(callback)=="function"){
						callback.call(this,data);
					}
				}
			});
		}
	},
	"dictionaryData":function(opts){//数据字典
		var options = {
			contextPathUrl:"",
				key:"",
				async:true,//默认异步
				callback:function(){}
			};
		    options = $.extend(true,{},options,opts);
		$.ajax({
	      	url : options.contextPathUrl+"/dictionary/getAllActiveDictionary",
	      	type : "post",
	      	data : {},
	      	async:options.async,
	      	dataType : "json",
	      	success : function(data){
				if(data.comboboxType&&data.comboboxType.length>0){
					var moduleName = "";
					var dictObj = {};
					$.each(data.comboboxType,function(index){
						if(options.key!=""&&typeof(options.key)!="undefined"){
							if(options.key!=this.module){
								return true;
							}
						}
						/*if(dictObj.length==0){
							moduleName = this.module;
							dictObj[moduleName] = [];
						}
						if(moduleName!=this.module){
							moduleName = this.module;
							dictObj[moduleName] = [];
						}
						dictObj[moduleName].push(this);
						console.info(dictObj["FILE_TYPE"]);*/
						if(!dictObj[this.module]){
							dictObj[this.module] = [];
						}
						dictObj[this.module].push(this);
					});
					if(typeof(options.callback)=="function"){
						options.callback.call(this,dictObj);
					}
	    		} 
			}
		});
	},
	"setTableDictionary":function(opts){//设置列表数据字典的值
		var options = {
			key:"",
			data:"",
			value:"",
			emptyText:""
		};
	    options = $.extend(true,{},options,opts);
	    var returnVal = options.emptyText;
	    if(options.key&&options.data&&options.data[options.key]&&options.data[options.key].length>0){
			$.each(options.data[options.key],function(){
				if(options.value&&options.value==this.key){
					returnVal = this.value;
					return false;
				}
			});
		}
	    return returnVal;
	},
	"setFormDictionary":function(opts){//设置表单数据字典的值
		var options = {
			field:"",
			data:"",
			callback:function(){},
			items:[],
		};
	    options = $.extend(true,{},options,opts);
	    var selectNum = 0;
	    var radioNum = 0;
	    if(options.items.length>0){
	    	$.each(options.items,function(){
	    		var $item = this;
	    		if(this["type"]=="select"){//下拉框
	    			selectNum++;
	    			if(options.data[this.key]&&options.data[this.key].length>0){
		    			var htmlStr = [];
		    			$.each(options.data[this.key],function(index){
		    				//console.info("--"+this["key"]);
		    				if($item["putOff"]&&$item["putOff"].length!=0){//判断是否有被删除的属性
		    	    			if($item["putOff"].indexOf(this["key"])>-1){
		    	    				return true;
		    	    			}
		    	    		}
		    				htmlStr[htmlStr.length] = '<option value="'+this["key"]+'">'+this["value"]+'</option>';
		    			});
		    			$(this.target).append(htmlStr.join(''));
		    		}
	    		}
	    		else if(this["type"]=="radio"){//单选
	    			radioNum++;
	    			if(options.data[this.key]&&options.data[this.key].length>0){
						var htmlStr = [];
						$.each(options.data[this.key],function(index){
							if(this["putOff"]&&this["putOff"].length!=0){//判断是否有被删除的属性
		    	    			if(this["putOff"].indexOf(this["key"])>-1){
		    	    				return true;
		    	    			}
		    	    		}
							htmlStr[htmlStr.length] = '<label class="checkbox-inline i-checks">';
							htmlStr[htmlStr.length] = '<input type="radio" name="'+$item["name"]+'" value="'+this["key"]+'"';
							if(index==0){
								htmlStr[htmlStr.length] = 'checked ';
							}
							htmlStr[htmlStr.length] = '>'+this["value"]+'</label>';
						});
						$(this.target).append(htmlStr.join(''));
		    		}
	    		}
	    	});
	    	//如果为编辑则设置
	    	if(selectNum>0){//下拉框
	    		$(options.field).find("select").each(function(index){
					if($(this).data("type")=="asyn"&&!hssduc.util.stringUtils.isBlank($(this).data("val"))){
						$(this).val($(this).data("val"));
					}
				});
	    	}
	    	if(radioNum>0){//单选
		    	$(options.field).find(".radio-form-module").each(function(index){
					if($(this).data("type")=="asyn"&&!hssduc.util.stringUtils.isBlank($(this).data("val"))){
						$(this).find("input[type='radio']").attr("checked",false);
						$(this).find("input[type='radio'][value='"+$(this).data("val")+"']").prop("checked",true);
					}
				});
	    	}
	    	//完成上面操作后的回调
	    	if(typeof(options.callback)=="function"){
	    		options.callback.call(this);
	    	}
	    }
	},
	"setDictText":function(opts){//设置文本字典的值
		var options = {
			field:"",
			target:"",
			data:null,
			type:"text",
			callback:function(){}
		};
	    options = $.extend(true,{},options,opts);
	    if($(options.target).length>0){
	    	$(options.target).each(function(index){
	    		var $item = $(this);
	    		var dictModule = $(this).attr("dict-module");
	    		var dictVal = $(this).attr("dict-val");
	    		if(hssduc.util.stringUtils.isBlank(dictVal)||hssduc.util.stringUtils.isBlank(dictModule)){
	    			return true;
	    		}
	    		var dictList = options.data[dictModule];
	    		if(dictList.length>0){
	    			$.each(dictList,function(){
	    				if(this.key==dictVal){
	    					if(options.type=="text"){
	    						$item.html(this.value);
	    					}else if(options.type=="value"){
	    						$item.val(this.value);
	    					}
	    				}
	    			});
	    		}
	    	})
	    }
	    //回调
    	if(typeof(options.callback)=="function"){
    		options.callback.call(this);
    	}
	},
	"getAllAddrByCustIdAndType":function(opts){//获取最新的全地址信息 
		var options = {
			contextPathUrl:"",
			customerId:"",
			addType:"",
			callback:function(){}
		};
	    options = $.extend(true,{},options,opts);
	    $.ajax({
			url : options.contextPathUrl+"/combobox/getAllAddrByCustIdAndType",
	      	type : "post",
	      	data : {"customerId":options.customerId,"addType":options.addType},
	      	dataType : "json",
	      	success : function(data){
				if(data.code=="1"){
					if(typeof(options.callback)=="function"){
						options.callback.call(this,data.data);
					}
				}else{
					layer.msg(data.msg, {icon: 2});
				}
			}
	   	});
	},
	"setSelectData":function(opts){
		var options = {
			field:"#f_form",
			callback:function(){}
		};
		options = $.extend(true,{},options,opts);
		$(options.field).find("select").each(function(index){
			if(!hssduc.util.stringUtils.isBlank($(this).attr("data-val"))){
				$(this).find("option[value='"+$(this).attr("data-val")+"']").prop("selected",true).siblings("option").prop("selected",false);
			}
		});
	}
}

/***操作相关***/
"hssduc" in window || (window.hssduc = {}) , "operate" in hssduc || (hssduc.operate = {});
hssduc.operate = {
   "formSubmit":function(opts){
	   var options = {
           formTarget:"#f_form",
           closeAndReflesh:true,
           beforeValidate:function(){return true;},
           success:function(){}
		};
	    options = $.extend(true,{},options,opts);
	    $(options.formTarget).find(".input-money-format-value").each(function(){
			$(this).val($(this).val().replace(/,/ig,""));
		});
	    if(typeof(options.beforeValidate)=="function"){
	    	if (!options.beforeValidate.call(this)) {
	            return;
	        }
		}
	    var formOptions = { 
			dataType:"json",
			type:"post",
			beforeSubmit : function() {
				var confirm = $(options.formTarget).validationEngine('validate');
				if (confirm) {
				    return true;
				} else {
					layer.closeAll("loading");
					return false;
				} 
			},
			success: function (data) {
				if(data.code=="1"){
					layer.msg(data.msg);
					if(options.closeAndReflesh){
						setTimeout(function(){
							var index = parent.layer.getFrameIndex(window.name);
							parent.refleshTable();
					        parent.layer.close(index);
						},1000);
					}
					if(typeof(options.success)=="function"){
						options.success.call(this,data);
					}
				}else{
					layer.closeAll("loading");
					layer.msg(data.msg);
					$(options.formTarget).find(".input-money-format-value").each(function(){
						$(this).val(hssduc.util.stringUtils.formatMoney($(this).val(),2, "", ",", "."));
					});
				}
			}
		};
		$(options.formTarget).ajaxSubmit(formOptions);
    }
}

"hssduc" in window || (window.hssduc = {}) , "math" in hssduc || (hssduc.math = {});
hssduc.math = {
   "getRandomInteger":function(min,max){//获取随机数字
	   var c = max-min+1; 
	   return Math.floor(Math.random() * c + min);
    },
    "delArraySame":function(arrA,arrB){//数组A删除数组B相同的值
    	var tempA = []; //临时数组1 
    	var tempB = []; //临时数组2 
    	for (var i = 0; i < arrB.length; i++) { 
    		tempA[arrB[i]] = true;
    	}; 
    	for (var i = 0; i < arrA.length; i++) { 
	    	if (!tempA[arrA[i]]) { 
	    		tempB.push(arrA[i]);
	    	} ; 
    	}; 
    	return tempB;    }
}

"hssduc" in window || (window.hssduc = {}) , "tools" in hssduc || (hssduc.tools = {});
hssduc.tools = {
    "debounce":function(fn, delay) {//去弹跳
    	var timer = null
    	return function () {
    		var context = this
    		var args = arguments;
    		clearTimeout(timer);
    		timer = setTimeout(function () {
    			fn.apply(context, args)
	    }, delay)
	  }
	},
	"throttle":function(fn, threshhold) {//节流
	  var last
	  var timer
	  threshhold || (threshhold = 250)

	  return function () {
	    var context = this
	    var args = arguments

	    var now = +new Date()

	    if (last && now < last + threshhold) {
	      clearTimeout(timer)
	      timer = setTimeout(function () {
	        last = now
	        fn.apply(context, args)
	      }, threshhold)
	    } else {
	      last = now
	      fn.apply(context, args)
	    }
	  }
	}
}

/****工具类 ****/
"hssduc" in window || (window.hssduc = {}) , "util" in hssduc || (hssduc.util = {});
/**
* 日期工具类
* @type {{DATE_TIME: number, HOUR_TIME: number, MINUTES_TIME: number, parseDate: "parseDate", format: "format", getDiffTime: "getDiffTime"}}
*/
hssduc.util.DateUtils = {
	"DATE_TIME":60*60*24,// 一天秒数
	"HOUR_TIME":60*60,// 一小时秒数
	"MINUTES_TIME":60,// 一分钟秒数
	/**
	 * 字符串转日期对象
	 * @param dateStr
	 * @returns {*}
	 */
	"parseDate" : function(dateStr){// 将字符串转为Date对象
	    var dateTime = null;
	    if(/(\d{4}).*?(\d{2}).*?(\d{2}).*?(\d{2}).*?(\d{2}).*?(\d{2})/.test(dateStr)){// yyyyMMddHHmmss
	        dateTime = new Date(RegExp.$1,(RegExp.$2*1-1),RegExp.$3,RegExp.$4*1,RegExp.$5*1,RegExp.$6*1);
	    }else if(/(\d{4}).*?(\d{2}).*?(\d{2}).*?(\d{2}).*?(\d{2})/.test(dateStr)){ // yyyyMMddHHmm
	        dateTime = new Date(RegExp.$1,(RegExp.$2*1-1),RegExp.$3,RegExp.$4*1,RegExp.$5*1);
	    }else if(/(\d{4}).*?(\d{2}).*?(\d{2}).*?(\d{2})/.test(dateStr)){ // yyyyMMddHH
	        dateTime = new Date(RegExp.$1,(RegExp.$2*1-1),RegExp.$3,RegExp.$4*1);
	    }else if(/(\d{4}).*?(\d{2}).*?(\d{2})/.test(dateStr)){// yyyyMMdd
	        dateTime = new Date(RegExp.$1,(RegExp.$2*1-1),RegExp.$3);
	    }
	    return dateTime;
	},
	/**
	 * 将日期对象格式化为i指定格式的字符串
	 * @param date
	 * @param format
	 * @returns {string}
	 */
	"format" : function(date,format){
	    format = format || "yyyy-MM-dd";
	    if($.type(date) == "string"){
	        date = hssduc.util.DateUtils.parseDate(date);
	    }
	    format = format.replace("yyyy",date.getFullYear()).replace("MM",(date.getMonth()+1) < 10 ? "0"+(date.getMonth()+1) : (date.getMonth()+1));
	    format = format.replace("dd",date.getDate() < 10 ? "0"+date.getDate() : date.getDate());
	    format = format.replace("HH",date.getHours() < 10 ? "0"+date.getHours() : date.getHours());
	    format = format.replace("mm",date.getMinutes() < 10 ? "0"+date.getMinutes() : date.getMinutes());
	    format = format.replace("ss",date.getSeconds() < 10 ? "0"+date.getSeconds() : date.getSeconds());
	    return format;
	},
	/**
	 * 将时间戳格式化为指定格式的字符串
	 * @param value
	 * @param format
	 * @returns {string}
	 */
	"formatTimeStamp" : function(value,format){
	    format = format || "yyyy-MM-dd HH:mm";
	    var date;
	    if (value instanceof Date) {
	    	date = value;
		} else {
			date = new Date(value);  
		}
	    format = format.replace("yyyy",date.getFullYear()).replace("MM",(date.getMonth()+1) < 10 ? "0"+(date.getMonth()+1) : (date.getMonth()+1));
	    format = format.replace("dd",date.getDate() < 10 ? "0"+date.getDate() : date.getDate());
	    format = format.replace("HH",date.getHours() < 10 ? "0"+date.getHours() : date.getHours());
	    format = format.replace("mm",date.getMinutes() < 10 ? "0"+date.getMinutes() : date.getMinutes());
	    format = format.replace("ss",date.getSeconds() < 10 ? "0"+date.getSeconds() : date.getSeconds());
	    return format;
	},
	/***
	 * 获取时间差
	 * @param current
	 * @param end
	 * @param opts
	 * @returns {"dates" : dates,"hours" : hours,"minutes" : minutes,"seconds" : difTime}
	 */
	"getDiffTime" : function(current,end,opts){ // 获取时间差 , 返回时间差额对象
	    var options = {"isFormat":true};
	    options = $.extend(true,{},options,opts);
	    if($.type(end) != "date"){
	        if($.type(end) != "string"){
	            return {};
	        }
	        end = hssduc.util.DateUtils.parseDate(end);
	    }
	    if($.type(current) != "date"){
	        if($.type(current) != "string"){
	            return {};
	        }
	        current = hssduc.util.DateUtils.parseDate(current);
	    }
	    var difTime = (end.getTime() - current.getTime())/1000;
	    var dates = 0,hours = 0,minutes = 0;
	    if(difTime > 0){
	        if(difTime > hssduc.util.DateUtils.DATE_TIME){
	            dates = Math.floor(difTime / hssduc.util.DateUtils.DATE_TIME);
	            difTime = difTime - dates*hssduc.util.DateUtils.DATE_TIME;
	            if(options.isFormat){
	                dates = dates < 10 ? "0"+dates : dates;
	            }
	        }
	        if(difTime > hssduc.util.DateUtils.HOUR_TIME){
	            hours = Math.floor(difTime / hssduc.util.DateUtils.HOUR_TIME);
	            difTime = difTime - hours*hssduc.util.DateUtils.HOUR_TIME;
	            if(options.isFormat){
	                hours = hours < 10 ? "0"+hours : hours;
	            }
	        }
	        if(difTime > hssduc.util.DateUtils.MINUTES_TIME){
	            minutes = Math.floor(difTime / hssduc.util.DateUtils.MINUTES_TIME);
	            difTime = difTime - minutes*hssduc.util.DateUtils.MINUTES_TIME;
	            if(options.isFormat){
	                minutes = minutes < 10 ? "0"+minutes : minutes;
	            }
	        }
	        difTime = Math.floor(difTime);
	        if(options.isFormat){
	            difTime = difTime < 10 ? "0"+difTime : difTime;
	        }
	    }
	    return {"dates" : dates,"hours" : hours,"minutes" : minutes,"seconds" : difTime};
	},
	/**
	 * 日期计算
	 * @param strInterval string  可选值 y 年 m月 d日 w星期 ww周 h时 n分 s秒  
	 * @param num int
	 * @param date Date 日期对象
	 * @return Date 返回日期对象
	 */
	"getDateAdd" : function(strInterval, num, date){
	     date =  arguments[2] || new Date();
	     if($.type(date) != "date"){
	         if($.type(date) != "string"){
	             return;
	         }
	         date = hssduc.util.DateUtils.parseDate(date);
	     }
	     switch (strInterval) { 
	         case 's' :return new Date(date.getTime() + (1000 * num));  
	         case 'n' :return new Date(date.getTime() + (60000 * num));  
	         case 'h' :return new Date(date.getTime() + (3600000 * num));  
	         case 'd' :return new Date(date.getTime() + (86400000 * num));  
	         case 'w' :return new Date(date.getTime() + ((86400000 * 7) * num));  
	         case 'm' :return new Date(date.getFullYear(), (date.getMonth()) + num, date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds());  
	         case 'y' :return new Date((date.getFullYear() + num), date.getMonth(), date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds());  
	     }  
	},
	/**
	 * 日期对象转换为毫秒数
	 * @param date
	 * @returns {int}
	 */
	"getDateToLong" : function(date){
		if($.type(date) != "date"){
	        if($.type(date) != "string"){
	            return;
	        }
	        date = hssduc.util.DateUtils.parseDate(date);
	    }
	    return date.getTime();
	}
};

/**
 * 事件工具类
 */
hssduc.util.event = {
	cancelBubble: function(){ //解决冒泡
		var e = hssduc.util.event.getEvent();
	    if(window.event){
	        e.cancelBubble=true;//阻止冒泡
	    }else if(e.preventDefault){
	        e.stopPropagation();//阻止冒泡
	    }
	},
	getEvent: function(){
		if(window.event)    {return window.event;}
	    func=getEvent.caller;
	    while(func!=null){
	        var arg0=func.arguments[0];
	        if(arg0){
	            if((arg0.constructor==Event || arg0.constructor ==MouseEvent
	                || arg0.constructor==KeyboardEvent)
	                ||(typeof(arg0)=="object" && arg0.preventDefault
	                && arg0.stopPropagation)){
	                return arg0;
	            }
	        }
	        func=func.caller;
	    }
	    return null;
	}
}

/**
 * 字符串工具类
 */
hssduc.util.stringUtils = {
	"isBlank" : function(value){
		if(typeof(value)!="undefined"&&value!=null&&value!=""||parseInt(value)==0){
			return false;
		}else{
			return true;
		}
	},
	"formatMoney" : function (number,places, symbol, thousand, decimal, unit) {
        places = !isNaN(places = Math.abs(places)) ? places : 2;
        symbol = symbol !== undefined ? symbol : "$";
        thousand = thousand || ",";
        decimal = decimal || ".";
        unit = unit !== undefined ? unit : "";
        var negative = number < 0 ? "-" : "",
            i = parseInt(number = Math.abs(+number || 0).toFixed(places), 10) + "",
            j = (j = i.length) > 3 ? j % 3 : 0;
        return symbol + negative + (j ? i.substr(0, j) + thousand : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousand) + (places ? decimal + Math.abs(number - i).toFixed(places).slice(2) : "") + unit;
    }
}

/**
 * 字符串工具类
 */
hssduc.util.objUtils = {
	"isEmpty" : function(obj){
		for(var key in obj) {
			return false;
		}
		return true;
	}
}

/*** 本地存储  ***/
"hssduc" in window || (window.hssduc = {}) , "storage" in hssduc || (hssduc.storage = {});
/***
 * 本地存储工具类
 * @type {{addString: addString, getString: getString, addJSON: addJSON, getJSON: getJSON, removeItem: removeItem, removeAll: removeAll}}
 */
hssduc.storage.LocalStorage = {
    /***
     * 添加字符串到存储
     * @param key
     * @param value
     */
    addString : function(key,value){
        if(!window.localStorage){return;}
        window.localStorage.setItem(key,value);
    },
    /***
     * 从存储获取字符串
     * @param key
     * @returns {*}
     */
    getString : function(key){
        if(!window.localStorage){return;}
        return window.localStorage.getItem(key);
    },
    /***
     * 添加JSON对象
     * @param key
     * @param value
     */
    addJSON : function(key,value){
        if(!window.localStorage){return;}
        window.localStorage.setItem(key,JSON.stringify(value));
    },
    /***
     * 获取JSON对象
     * @param key
     * @returns {*}
     */
    getJSON : function(key){
        if(!window.localStorage){return;}
        if(window.localStorage.getItem(key)){
            return JSON.parse(window.localStorage.getItem(key));
        }
        return null;
    },
    /***
     * 按key删除项
     * @param key
     */
    removeItem : function(key){
        if(!window.localStorage){return;}
        window.localStorage.removeItem(key);
    },
    /**
     * 删除所有本地存储
     */
    removeAll : function(){
        if(!window.localStorage){return;}
        window.localStorage.clear();
    }
}

/***获取input光标位置**/
$.fn.getCursorPosition = function () {  
    var el = $(this).get(0);  
    var pos = 0;  
    if ('selectionStart' in el) {  
        pos = el.selectionStart;  
    } else if ('selection' in document) {  
        el.focus();  
        var Sel = document.selection.createRange();  
        var SelLength = document.selection.createRange().text.length;  
        Sel.moveStart('character', -el.value.length);  
        pos = Sel.text.length - SelLength;  
    }  
    return pos;  
}  

/***设置input光标位置**/
$.fn.setCursorPosition = function(position) {
    if (this.lengh == 0)
        return this;
    return $(this).setSelection(position, position);
};
 
/***设置input选中位置**/
$.fn.setSelection = function(selectionStart, selectionEnd) {
    if (this.lengh == 0)
        return this;
    input = this[0];
 
    if (input.createTextRange) {
        var range = input.createTextRange();
        range.collapse(true);
        range.moveEnd('character', selectionEnd);
        range.moveStart('character', selectionStart);
        range.select();
    } else if (input.setSelectionRange) {
        input.focus();
        input.setSelectionRange(selectionStart, selectionEnd);
    }
 
    return this;
};