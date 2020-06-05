<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- START :: HEADER FORM -->
   <%@ include file="../form/header.jsp"%>
<!-- END :: HEADER FORM -->

<!-- START :: feed -->
<style type="text/css">

   .profile_info{
      padding: 10px;
      border-bottom: 1px solid gray; 
      height: 60px;
   }


</style>
   <script type="text/javascript">
   
      /* 무한 스크롤 eventListner */
      let isEnd = false;
      var startNo = 1;
      var searchNo = 0;
      var member_code = ${sessionLoginMember.member_code};
      
      $(function(){   
         
         $(window).scroll(function(){
            var scrollTop = $(window).scrollTop();   // 현재 브라우저 스크롤이 있는 위치
            var documentHeight = $(document).height();   // 문서의 총 높이
            var windowHeight = $(window).height();   // 브라우저에 보여지는 높이
            
            console.log(
                  "documentHeight : " + $(document).height()
                  + " | scrollTop : " + $(window).scrollTop() 
                  + " | windowHeight : " + $(window).height()
                  + " | scrollTop + windowHeight = " + ($(window).scrollTop() + $(window).height())
                  );
            
            if(documentHeight < scrollTop + windowHeight + 1){
               selectFeedList();   
            }
         })
         
         selectFeedList();   
      })
   
      /* feed List ajax 통신 */
      function selectFeedList(){   
         if(isEnd == true)
            return;
         
         $.ajax({
            type:"GET",
            url:"/feed/likeFeedList",
            data : {"startNo":startNo, "member_code":member_code},
            dataType:"json",
            async:false,
            success: function(data){
               // 가져온 데이터가 15개 이하 (막지막 sub리스트)일 경우 무한 스크롤 종료
               let length = data.length;
               if(length < 15){
                  isEnd = true;
                  console.log("******마지막 컨텐츠까지 다 가져옴******");
               }
               fillFeedList(data);
            }
         })
            startNo += 15;
   
      }
      
      /* feed list 뿌리기 */
      function fillFeedList(data){   
         
         $.each(data, function(key, value){
            var feed = $('<div>').attr({'class':'feed', 'style':'border:1px solid black; margin:10px;'});
            $('.feedrow').append(feed);
            var board_regdate = moment(value.board_regdate).format("YYYY-MM-DD hh:mm");
            var board_code = value.board_code;
            var member_id = value.member_id;
            var profile_info = $('<div>').attr({'class':'profile_info'});
            var profile_img = $('<img>').attr({'class':'profile_img', 'style':'border-radius: 70%; width: 40px; height: 40px;', 'src':'/resources/images/profileupload/'+value.member_img_server_name});
            var profile_id = $('<a>').attr({'class':'profile_id', 'href':"/member/headerSearch?search="+member_id}).html(member_id);
            feed.append(profile_info);
            profile_info.append(profile_img).append(profile_id);
            
            
            if(value.board_file_ext == 'mp4'){
               var vid_div = $('<div>').attr({'class' : 'feedContainer'});
               var vid = $('<video>').attr({'src':'/resources/images/feedupload/'+value.board_file_server_name, 'controls':'true'});
               var contents = $('<p>').attr({'class':'contents'}).html(value.board_content);
               feed.append(vid_div);
               vid_div.append(vid).append(contents);
               
            } else {
               var img_div = $("<div>").attr({"class":"feedContainer"});
               var img = $('<img>').attr({'src':'/resources/images/feedupload/'+value.board_file_server_name});
               var contents = $('<p>').attr({'class':'contents'}).html(value.board_content);
               feed.append(img_div);
               img_div.append(img).append(contents);
            }
            feed.append('<p>'+ board_regdate+ ' 에 등록됨</p>');
            
            var button_div = $("<div>").attr({'class':'buttonContainer'});
            feed.append(button_div);
            
            button_div.append("<input type='button' value='댓글' class='reply_more' onclick=''>");
            button_div.append("<input type='button' value='DM' class='sendDM' onclick=''>");
            
            $.ajax({
               type:"GET",
               url:"/feed/isLiked",
               data:{ "board_code":board_code, "member_code":${sessionLoginMember.member_code} },
               async:false,
               success: function(like){
                  if(like.res>0){
                     button_div.prepend("<input type='button' value='좋아요 취소' id='"+board_code+"' class='unlike'>");
                  } else{
                     button_div.prepend("<input type='button' value='좋아요' id='"+board_code+"' class='like'>");
                  }
               },
               error : function(){
                  alert("AJAX 좋아요 버튼 출력 실패");
               }
            })
            
            
            
            
            $.ajax({
               type:"GET",
               url: "/feed/feedreply",
               data: {"board_code" : board_code},
               dataType: "JSON",
               async:false,
               success: function(msg){
                  if(msg != null){
                  var reply_div = $("<div>").attr({'class':'replyContainer'});
                  var rep_id_div = $('<span>').attr({'class' : 'reply_member_id_div'});
                  var rep_id = $('<a>').attr({'class':'reply_member_id'});
                  var rep_content = $('<span>').attr({'class':'reply_content'});
                  
                  feed.append(reply_div);
                  reply_div.append(rep_id_div);
                  rep_id_div.append(rep_id);
                  rep_id.text(msg.member_id);
                  reply_div.append(rep_content);
                  rep_content.text(msg.reply_content);
                  }
                  
                  
               }
            })
            

         })
   
      }
      
      
      

      
      
      // 좋아요
      
      $(document).on('click', '.like', function (){
         var board_code = $(this).attr('id');
         var member_code = ${sessionLoginMember.member_code};
         $.ajax({
            type:"POST",
            url:"/feed/boardLike",
            data:{"board_code" : board_code, "member_code":member_code},
            success: function(msg){
               if(msg.res == 1){
                  alert("좋아요 성공");
                  $(this).parent().prepend("<input type='button' value='좋아요 취소' id='"+board_code+"' class='unlike'>");
                  $(this).remove();
                  $('#'+board_code).load(location.href+'#'+board_code);
               }else{
                  alert("좋아요 실패");
               }
            },
            error: function(){
               alert("통신실패");
            }
         })
      })
      
      // 좋아요 취소
      $(document).on('click', '.unlike', function (){
         var board_code = $(this).attr('id');
         var member_code = ${sessionLoginMember.member_code};
         $.ajax({
            type:"POST",
            url:"/feed/boardUnlike",
            data:{"board_code" : board_code, "member_code":member_code},
            success: function(msg){
               if(msg.res == 1){
                  alert("좋아요 취소 성공");
                  $(this).parent().prepend("<input type='button' value='좋아요' id='"+board_code+"' class='like'>");
                  $(this).remove();
                  $('#'+board_code).load(location.href+'#'+board_code);
               }else{
                  alert("좋아요 취소 실패");
               }
            },
            error: function(){
               alert("통신실패");
            }
         })
      })
   </script>
<!-- END :: feed -->
</head>

<body>
   
   <section class="container w-75">
      <div class="feedrow">
         
         
         
         
      </div>
   </section>
   
   
</body>
</html>