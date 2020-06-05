function searchAutoComplete(){
   var searchtype = "";
   
   // 검색어 자동완성
   $("input[name='search']").autocomplete({
      source: function(request, response){

    	  var searchWord = request.term;
         
         if(searchWord.substr(0,1) == '#'){
            searchtype = "tagsSearchAutoComplete";
            console.log("tagsSearchAutoComplete");
            
            $.ajax({
                  type: "POST",
                  url: "/member/tagsSearchAutoComplete",
                  data: {
                     search : request.term
                  },
                  datatType: "JSON",

                  success: function (data) {
                     console.log("autocomplate");
                        console.log(data)
                        response(
                        $.map(JSON.parse(data), function(item){
                          return{
                              label: item.tag_hash,
                              value: item.tag_hash,
                              link: item.tag_hash,
                              image: "/resources/images/profile/hashtag.png"
                           }
                        })   
                     )
                  },
                  error: function () {
                     alert("통신 실패");
                  }
               })
            
            
         } else {
            searchtype = "nameSearchAutoComplete";

            console.log("nameSearchAutoComplete");
            
            $.ajax({
                  type: "POST",
                  url: "/member/nameSearchAutoComplete",
                  data: {
                     search : request.term
                  },
                  datatType: "JSON",

                  success: function (data) {
                     console.log("autocomplate");
                        console.log(data)
                        response(
                        $.map(JSON.parse(data), function(item){
                          return{
                              label: item.member_id,
                              value: item.member_id,
                              link: item.member_id,
                              image: item.member_img_server_name
                           }
                        })   
                     )
                  },
                  error: function () {
                     alert("통신 실패");
                  }
               })
            
         }
         
          
      },
      minLength : 1,
      focus : function(event, ui){
         console.log(ui)
         $("input[name='search']").val(ui.item.value)
         return false;
      }
   }).autocomplete("instance")._renderItem = function(ul, item){
      
      if(searchtype == "tagsSearchAutoComplete") {
    	var tagtext = item.link.substring(item.link.indexOf('#')+1);
        var li_item = $("<div>").attr({"class":"my-auto", "onclick":"location.href='/feed/searchTagBoard?hashtag="+tagtext+"'"});
         var img
         if(item.image != null){
            img = $("<img>").attr({
                        "class" : "m-1",
                        "style" : "width: 30px; height: 30px; border-radius: 100%;",
                        "src" : "/resources/images/profile/hashtag.png"
                     })
         }
         
         var link = $("<a>");
         var title = $("<span>").attr({"class":"mx-2"}).text(item.label);
         li_item.append(link.append(img)).append(title)
                     
         return $("<li>").append(li_item).appendTo(ul);
         
         
         
         
         
         
         
      }
      
      else if (searchtype == "nameSearchAutoComplete"){
        var li_item = $("<div>").attr({"class":"my-auto", "onclick":"location.href='/member/headerSearch?search=" + item.link + "'"});
         var img
         if(item.image != null){
            img = $("<img>").attr({
                        "class" : "m-1",
                        "style" : "width: 30px; height: 30px; border-radius: 100%;",
                        "src" : "/resources/images/profile/" + item.image
                     })
         }else{
            img = $("<img>").attr({
               "class" : "m-1",
               "style" : "width: 30px; height: 30px; border-radius: 100%;",
               "src" : "/resources/images/profile/add.png"
            })
         }
         var link = $("<a>");
         var title = $("<span>").attr({"class":"mx-2"}).text(item.label);
         li_item.append(link.append(img)).append(title)
                     
         return $("<li>").append(li_item).appendTo(ul);
      }
      
      
   }
}