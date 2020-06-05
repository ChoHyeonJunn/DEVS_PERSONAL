<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>피드 상세보기</title>
<!-- START :: HEADER FORM -->
<%@ include file="../form/header.jsp"%>
<!-- END :: HEADER FORM -->
</head>
<body>


		<div id="contents">
			<div id="div-left">
				<div id="media">
					<c:choose>
						<c:when test="${feed.board_file_ext eq 'mp4'}">
							<video id="video" src="/resources/images/feedupload/${feed.board_file_server_name}" controls></video>
						</c:when>
						<c:otherwise>
							<img id="image" src="/resources/images/feedupload/${feed.board_file_server_name }">
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			
			<div id="div-right">
				<div id="user-info">
					<img class="profile_image" src="/resources/images/profileupload/${feed.member_img_server_name }" style="height:30px; width:30px;">
					<a href="/member/headerSearch?search=${feed.member_id}">${feed.member_id }</a>
				</div>
				<div id="like-count">
					<span>좋아요 ${feed.board_like_count }개</span>
				</div>
				
				<div id="buttons">
					<img src="" onclick="">
				</div>
				
				<div id="board-content">
					<p>${feed.board_content }</p>
					<p>${feed.board_regdate }에 등록됨</p>
				</div>
				
				<div id="replyList">
					<c:forEach var="re" items="${reply }">
					<div class="reply">
						<img class="profil_image" src="/resources/images/profileupload/${re.member_img_server_name }" style="height:30px; width:30px;">
						<a href="/member/headerSearch?search=${re.member_id}">${re.member_id }</a>
						<span>${re.reply_content }</span>
						<span>${re.reply_regdate }에 등록됨</span>
					</div>
					</c:forEach>
				</div>
				
				<div id="reply-insert-box">
					<form id="replyform" action="/feed/insertReply" method="post">
						<input type="hidden" id="reply_board_code" name="board_code" value="${feed.board_code }">
						<input type="hidden" name="member_code" value="${sessionLoginMember.member_code}">
						<textarea name="reply_content" placeholder="댓글 달기"></textarea>
						<input type="button" value="게시" onclick="insertReply();">
					</form>
				</div>
			</div>	
			
		</div>
		
<script type="text/javascript">
	function insertReply(){
		var params = $("#replyform").serialize();
		$.ajax({
			type:"POST", 
			url:"/feed/insertReply",
			data: params,
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
						location.reload();
					}
				})
			}
		})
	}
</script>

</body>
</html>