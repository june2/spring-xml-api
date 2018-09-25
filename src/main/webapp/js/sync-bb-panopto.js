'use strict';
var QueryString = function() {
	// This function is anonymous, is executed immediately and 
	// the return value is assigned to QueryString!
	var query_string = {};
	var query = window.location.search.substring(1);
	var vars = query.split("&");
	for (var i = 0; i < vars.length; i++) {
		var pair = vars[i].split("=");
		// If first entry with this name
		if (typeof query_string[pair[0]] === "undefined") {
			query_string[pair[0]] = decodeURIComponent(pair[1]);
			// If second entry with this name
		} else if (typeof query_string[pair[0]] === "string") {
			var arr = [ query_string[pair[0]], decodeURIComponent(pair[1]) ];
			query_string[pair[0]] = arr;
			// If third or later entry with this name
		} else {
			query_string[pair[0]].push(decodeURIComponent(pair[1]));
		}
	}
	return query_string;
}();

function callAjax(method, url, param, callback){
	var xhr = new XMLHttpRequest();
	xhr.open(method, url);
	xhr.setRequestHeader('Content-Type', 'application/json');
	xhr.onreadystatechange = function () {
		if (xhr.readyState === 4) {  
			
			console.log("code : " + xhr.status);
			var obj = JSON.parse(xhr.responseText);
			console.log("obj : " + obj);
			
			if(!obj.result){
				alert("등록되지 않은 아이디입니다. / unprovisioned userId");
				history.go(-1);
			}			
	    }  
	}
	xhr.send(param);
}


//Ajax Call
callAjax("POST", "/webapps/ppto-panopto-online-attendance-BBLEARN/panopto-sync-userId?course_id="+QueryString.course_id+"&content_id="+QueryString.content_id, null, function(data){
		
});


