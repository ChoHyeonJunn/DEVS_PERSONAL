<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>피드 수정</title>
<!-- START :: HEADER FORM -->
   <%@ include file="../form/header.jsp"%>
<!-- END :: HEADER FORM -->

</head>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.form/4.2.2/jquery.form.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
</head>
<body>
   <h1>글수정</h1>
   
   <div id="preview">
   		<img id="previewImg">
   </div>
   
   <form action="/feed/updateFeed" method="post" enctype="multipart/form-data">
            <input type="hidden" name="member_code" value="${sessionLoginMember.member_code } ">
            <input type="hidden" name="board_code" value="${board_code }">
          <input type="file" id="file" name="mpfile">
          <textarea id="board_content" name="board_content" rows="10" cols="80">${board_content }</textarea>
         <input type="submit" value="등록">
   </form>
   
   <br>
   
   <div id="progressbar" style="border:0px; height:10px; width:500px;"></div>
   

   

   
</body>
   <script>
   var hashTagList;

   var jsonArray = new Array();
   
  $(function(){
     var input = $("#board_content");
     input.keyup(function(){
        
        if(window.event.keyCode == 32){
           getHashTag();
           console.log(hashTagList);
        }

     })
  })

  
  function getHashTag(){
       var hashTags = [];
       var jsonTempArray = new Array();
     var content = $("#board_content").val();
     
     var splitedArray = content.split(' ');
     var linkedContent = '';
     for(var word in splitedArray)
     {
       word = splitedArray[word];
        if(word.indexOf('#') == 0)
        {
            hashTags.push(word);
           
        }
     }
     hashTagList = hashTags;
     
     for(var i=0; i<hashTagList.length; i++){
        var json = new Object()
        
        json.tag = hashTagList[i];
        jsonTempArray.push(json)
        
     }
     jsonArray = jsonTempArray;
  }   
   
   
   
	   $("#file").change(function(){
		   if($("#file").val() != ""){
			   var ext = $('#file').val().split('.').pop().toLowerCase();
			   if($.inArray(ext, ['jpg','png','jpeg','mp4']) == -1){
				   alert("이미지 파일은 jpg, jpeg, png, 동영상 파일은 mp4 파일만 첨부 가능합니다.");
				   $("#file").val("");
				   return;

			   } else {
				   readURL(this);
			   }
		   }
			}
	   );
  
       function readURL(input) {
           if (input.files && input.files[0]) {
        	   var ext = $('#file').val().split('.').pop().toLowerCase();
        	   if(ext=="mp4"){
        		   $('#previewImg').attr({'src':'/resources/images/social/player.png'});
			   }else{
				   var reader = new FileReader(); //파일을 읽기 위한 FileReader객체 생성
	               reader.onload = function (e) {
	               //파일 읽어들이기를 성공했을때 호출되는 이벤트 핸들러
	               	
	               
	                   $('#previewImg').attr('src', e.target.result);
	                   //이미지 Tag의 SRC속성에 읽어들인 File내용을 지정
	                   //(아래 코드에서 읽어들인 dataURL형식)
	               }                   
	               reader.readAsDataURL(input.files[0]);
	               //File내용을 읽어 dataURL형식의 문자열로 저장
			   }
               
           }
       }//readURL()--




   
   $(function() {
      var progressbar = $("#progressbar");
      var progressLabel = $(".progress-label");
      progressbar.progressbar({
         value: true,
         change: function() {
                     progressLabel.text("Current Progress: " + progressbar.progressbar("value") + "%");
                  },
         complete: function() {
                        progressLabel.text("Complete!");
                        $(".ui-dialog button").last().trigger("focus");
                  }
      });
      $('form').ajaxForm({
         url: "/feed/updateFeed",
         type: "POST",
         beforeSubmit: function(arr, $form, options) {
                        progressbar.progressbar( "value", 0 );
                     },
         uploadProgress: function(event, position, total, percentComplete) {
                        progressbar.progressbar( "value", percentComplete );
                     },
         success: function(text, status, xhr, element) {
			progressbar.progressbar( "value", 100 );
             
             console.log(JSON.stringify(text), text.board_code);
             
             var json = new Object()
             
             json.board_code = text.board_code;
             jsonArray.push(json)
             console.log(jsonArray)
             
             $.ajax({
                type:"POST",
                url:"/feed/updateHashtag",
                data : JSON.stringify(jsonArray),
                contentType: "application/json",
                dataType:"json",
                
                success: function(data){
                   console.log(JSON.stringify(data))
                   var result = data.res;
                   if (result > 0){
                      console.log("update 완료");
                   } else {
                      console.log("update 실패");
                   }
                   
                   location.href="/member/profile?member_code=${sessionLoginMember.member_code}";
                }
             })
             
             
          },
         error: function(){
            alert("등록 실패");
         }
      });
   });
   </script>
</html>