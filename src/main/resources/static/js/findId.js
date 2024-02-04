$(document).ready(function () {

 $(document).on("click", "#sendNum", function (e) {
   var token = $("meta[name='_csrf']").attr("content");
   var header = $("meta[name='_csrf_header']").attr("content");
   var to = $("#tel").val();
   var telRegex = /^\d{10,11}$/;

   if (to === "") {
     alert("연락처를 입력해주세요.");
   } else if (!telRegex.test(to)) {
     alert("하이픈(-) 제외하고 연락처 형식에 맞게 입력해주세요.");
   } else {
     $.ajax({
       url: "/members/sendSMS",
       type: "GET",
       data: {
         to: to,
       },
       beforeSend: function (xhr) {
         xhr.setRequestHeader(header, token);
       },
       success: function (data) {
         const checkNum = data;
         alert("인증번호 전송이 완료 되었습니다.");
         $(".confirm").css("display", "flex");
       },
     });
   }
 });

   $("input[name='way']").change(function () {
     if ($(this).val() === "TEL") {
       // TEL이 선택되었을 때 할 작업들을 여기에 추가
       console.log("TEL이 선택되었습니다.");

       $(".email_way").css("display", "none");
       $(".phone_way").css("display", "flex");
     }
     else {
       $(".phone_way").css("display", "none");
       $(".email_way").css("display", "flex");
     }
   });
 });
   // document 다 준비되고 나서 실행
     document.addEventListener('DOMContentLoaded', function () {
         // 모델에서 errorMessage 값을 가져와서 사용
         var errorMessage = document.getElementById("errorMessage").value;
           console.log("에러 메시지 값 확인: ", errorMessage);
           console.log("조건 확인: ", errorMessage != null, errorMessage !== "");

         if (errorMessage !== "") {
               $("#name").val("");
               $("#tel").val("");
             alert(errorMessage);
         }
     });

 $(document).one("click", "#checkNumber", function (e) {
   e.preventDefault();
   var token = $("meta[name='_csrf']").attr("content");
   var header = $("meta[name='_csrf_header']").attr("content");
   var num = $("#confirmNumber").val();
   var url = "/members/" + num + "/confirmNum";

   $.ajax({
     url: url,
     type: "post",
     data: num,
     beforeSend: function (xhr) {
       xhr.setRequestHeader(header, token);
     },
     dataType: "text",
     cache: false,
     async: false,
     success: function (result, status) {
       alert(result);

       $(".confirm").css("display", "none");
       $("#sendNum").html("인증 완료");
       $("#sendNum").css("background-color", "white");
       $("#sendNum").css("color", "black");
       $("#sendNum").css("border", "1px solid gray");
     },
     error: function (jqXHR, status, error) {
       if (jqXHR.status == "400") {
         alert("인증번호가 잘못 입력되었습니다");
       } else {
         alert(jqXHR.responseText);
       }
     },
   });
 });
   $(document).on("submit", "#memberJoinForm", function (e) {
     var checkNumber = $("#sendNum").html();
     if (checkNumber !== "인증 완료" ) {
       e.preventDefault();
       alert("인증은 필수입니다.");
     } else {
       console.log("통과");
     }
});
