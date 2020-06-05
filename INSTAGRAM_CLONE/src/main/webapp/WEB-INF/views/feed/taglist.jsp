<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="/resources/css/profilefeed.css" type="text/css" rel="stylesheet">
<!-- START :: HEADER FORM -->
   <%@ include file="../form/header.jsp"%>
<!-- END :: HEADER FORM -->

<!-- START :: feed -->
 
</head>
<body>
<!-- 피드 목록  -->
<section class="container w-75">
		<div id="header">
			<div class="row">
				
				<div class="col-sm-4">
					
						<div class="rounded-circle border w-150 h-150 overflow-hidden mx-auto">						
							<img id="profile_image" 
								class="w-150 h-150 bg-white"
								src="/resources/images/feedupload/${vo.board_file_server_name }">
							
						</div>
				</div>
				
				<div class="col-sm-8">
					<div class="d-flex mb-2">
						<span id="hashtag">${tag }</span>
					</div>
				</div>
			</div>
		</div>



<div id="contant" class="h-150">
         <div class="row">
            
            
            <c:forEach var="list" items="${feedList }">
               <div class="contents">
                  <c:choose>
                     <c:when test="${list.board_file_ext eq 'mp4'}">
                              <div class="thumbnail" id="${list.board_code}">
                                 <c:set var="board_file_server_name" value="${list.board_file_server_name }"></c:set>
                                 <img class="fit" src="/resources/images/feedupload/${fn:substringBefore(board_file_server_name, '.')}.jpg">
                                 <img class="play-button" src="/resources/images/feedupload/playbutton.png">
                              </div>
                     </c:when>
                     <c:otherwise>
                        <div class="thumbnail" id="${list.board_code}">
                           <c:set var="file_name" value="${list.board_file_server_name }"></c:set>
                           <img class="fit" src="/resources/images/feedupload/${file_name }">
                        </div>
                     </c:otherwise>
                  </c:choose>
               </div>
            </c:forEach>
   
   
         </div>
            <!-- 모달창 -->
            <div class="bg-modal">
               <div class="close">+</div>
                  <div class="modal-box">
                  
                     <div class="div-left">
                        <div id="media" class="remove">
                           <!-- 사진 or 비디오 -->
                        </div>
                     </div>
                     
                     <div class="div-right">
                        <header id="user-info" class="remove">   
                           <!-- 글쓴이 프로필 -->
                        </header>
                        <div id="like_count" class="remove">
                           <!-- 좋아요 개수 -->
                        </div>
                        <div id="buttons">
                           <input type="button" id="unlike_button" value="좋아요 취소"onclick="unlike();">
                           <input type="button" id="like_button" value="좋아요" onclick="like();">
                        </div>
                        
                        <div id="board-content" class="remove">
                        
                        </div>
                        
                        <div id="replyList" class="remove">
                           
                        </div>
                        
                        <div class="reply-insert-box">
                           <form id="replyform" action="/feed/insertReply" method="post">
                              <input type="hidden" id="reply_board_code" name="board_code" value="">
                              <input type="hidden" name="member_code" value="${sessionLoginMember.member_code}">
                              <textarea name="reply_content" placeholder="댓글 달기"></textarea>
                              <input type="button" value="게시" onclick="insertReply();">
                           </form>
                        </div>
                     </div>
                     
                  </div>
            </div>
         
       </div>      
   </section>
   
   <script type="text/javascript">
// 모달창 띄우기
   $(".thumbnail").click(function() {
      var board_code = $(this).attr('id');
      var member_code = ${sessionLoginMember.member_code};
      var param = {"board_code":board_code, "member_code":member_code};
      // 이미지, 글쓴이 프로필 출력
      $.ajax({
         type:"GET",
         url:"/feed/selectContent",
         data:{"board_code": board_code},
         success: function(vo){
            if(vo.board_file_ext == 'mp4'){
               var vid = $('<video id="video" src="" controls></video>');
               $("#media").append(vid);
               $("#video").attr('src', '/resources/images/feedupload/'+vo.board_file_server_name);
               
            }else{
               $("#media").append('<img id="image">')
               $("#image").attr('src','/resources/images/feedupload/'+vo.board_file_server_name);
            }
            $("#user-info").append($('<img id="writer_profile_image" src="">')).append($('<a id="board_writer"></a>'));
            $("#writer_profile_image").attr('src','/resources/images/profileupload/'+vo.member_img_server_name);
            $("#board_writer").text(vo.member_id);
            $("#reply_board_code").val(board_code);
            
            if(vo.board_content == null){
            	$("#board-content").append("<span></span>");
            }else{
            	$("#board-content").append("<span>"+getHashTag(vo.board_content)+"</span>");            
            }
            
            $("#like_count").append("<span><a href='#'>좋아요 "+vo.board_like_count+"개</a></span>");
         }
      });
      
   // hashTag 로 바꿔주기
	   function getHashTag(content){
	      
	      var splitedArray = content.split(' ');
	      var linkedContent = '';
	      for(var word in splitedArray)
	      {
	        word = splitedArray[word];
	         if(word.indexOf('#') == 0)
	         {
		         var tagtext = word.substring(word.indexOf('#')+1);
	            word = '<a href="/feed/searchTagBoard?hashtag='+tagtext+'">'+word+'</a>';
	            console.log(word);
	         }
	         linkedContent += word+' ';
	      }
	      
	      return linkedContent;
	   }  
      
      
      // 좋아요 버튼
      $.ajax({
         type:"GET",
         url: "/feed/isLiked",
         data:param,
         success: function(msg){
            if(msg.res>0){
               $("#like_button").hide();
               $("#unlike_button").show();
            }else{
               $("#unlike_button").hide();
               $("#like_button").show();
            }
         },
         error: function(){
            alert("통신실패");
         }
      })
      
      // 댓글리스트 출력
      $.ajax({
         type:"GET",
         url:"/feed/replyList",
         data:{"board_code": board_code},
         dataType:"json",
         success: function(msg){
            $.each(msg, function(key, value){
               var reply_regdate = moment(value.reply_regdate).format("YYYY-MM-DD hh:mm");
               var rep = $('<div>').attr({'class':'rep'});
				$("#replyList").append(rep);
				rep.append(
                  "<img class='re-member-pic' src='"+ '/resources/images/profileupload/'+value.member_img_server_name+"'>" +
                  "<a class='re-member-id' href='/member/headerSearch?search="+value.member_id+"'>"+value.member_id+"</a>" +
                  "<span class='re-reply-content'>"+value.reply_content+"</span>"+
                  "<span class='re-reply-regdate'>"+reply_regdate+"</span>"
               );
            })
            
         }
      })
      
      $(".bg-modal").css("display", "flex");
      
      
   })
   
   
   // 댓글 등록
   function insertReply(){
      var params = $("#replyform").serialize();
      $.ajax({
         type:"POST",
         url:"/feed/insertReply",
         data:params,
         success: function(msg){
               var reply_code = msg.re_code;
               console.log(reply_code);
               $.ajax({
                  type:"GET",
                  url:"/feed/insertReplyView",
                  data:{"reply_code":reply_code},
                  dataType:"json",
                  success: function(data){
                     alert("댓글이 등록되었습니다.");
                     console.log(data);
                     
                     $("#replyList").append(
                           "<img class='re-member-pic' src='/resources/images/profileupload/${sessionLoginMemberProfile.member_img_server_name }'>"+
                           "<span class='re-member-id'>${sessionLoginMember.member_id}</span>" +
                           "<span class='re-reply-content'>"+data.reply_content+"</span>"+
                           "<span class='re-reply-regdate'>"+data.reply_regdate+"</span>"
                        );
                     $('textarea').val("");

                  },
                  error: function(){
                     alert("통신 실패");
                  }
               })
               
            },
         
         error: function(){
            alert("통신 실패");
         }
         
      });
   }
   // 좋아요
   function like(){
      var board_code = $("#reply_board_code").val();
      var member_code = ${sessionLoginMember.member_code};
      $.ajax({
         type:"POST",
         url:"/feed/boardLike",
         data:{"board_code" : board_code, "member_code":member_code},
         success: function(msg){
            if(msg.res == 1){
               $("#like_button").hide();
               $("#unlike_button").show();
               $("#like_count").find("*").remove();
               $("#like_count").append("<span><a href='#'>좋아요 "+msg.board_like_count+"개</a></span>");
            }else{
               alert("좋아요 실패");
            }
         },
         error: function(){
            alert("통신실패");
         }
      })
   }
   // 좋아요 취소
   function unlike(){
      var board_code = $("#reply_board_code").val();
      var member_code = ${sessionLoginMember.member_code};
      $.ajax({
         type:"POST",
         url:"/feed/boardUnlike",
         data:{"board_code" : board_code, "member_code":member_code},
         success: function(msg){
            if(msg.res == 1){
               $("#unlike_button").hide();
               $("#like_button").show();
               $("#like_count").find("*").remove();
               $("#like_count").append("<span><a href='#'>좋아요 "+msg.board_like_count+"개</a></span>");
            }else{
               alert("좋아요 취소 실패");
            }
         },
         error: function(){
            alert("통신실패");
         }
      })
   }
   
   // 모달 창 닫기
   $(".close").click(function(){
      $(".remove").find("*").remove();
      $(".bg-modal").css("display", "none");
   })
   
   </script>
</body>
</html>