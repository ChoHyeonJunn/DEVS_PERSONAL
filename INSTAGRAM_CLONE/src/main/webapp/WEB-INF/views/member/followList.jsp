<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
   <%@ include file="../form/header.jsp"%>
   <style type="text/css">
   .img{
      border-radius: 100px;
      height: 60px;
      width: 60px;
   }
   .id{
   font-size: xx-large;
   font-weight: bold;
   }
   .div{ 
   width:300px;
   height:300px;
   margin:0 auto;
   }
   </style>
</head>
<body>
<div class="div">
<h1 class="h1">팔로우 리스트</h1>
<c:forEach var="list" items="${followList }">
   <img
                        id="profile_image" 
                        class="img"
                        src="<c:choose>
                               <c:when test="${not empty list.member_img_server_name}">
                                  /resources/images/profileupload/${list.member_img_server_name }
                               </c:when>
                               <c:otherwise>
                                  /resources/images/profile/add.png
                               </c:otherwise>
                            </c:choose>"
                     >
&nbsp;
<span class="id"><a href="/member/headerSearch?search=${list.member_id }">${list.member_id }</a></span>
<br>
</c:forEach>
</div>
</body>
</html>