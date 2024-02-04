$(document).ready(function () {
  // 비밀번호 변경 버튼 클릭 시 AJAX 요청 보내기
  $("#submitBtn").click(function (e) {
    e.preventDefault();
    var checkPasswd = $("#passwordCheckBtn").html();
    if (checkPasswd !== "인증 완료") {
      alert("비밀번호 확인은 필수입니다.");
    } else {
      var formData = $("#changePasswdForm").serialize();
      $.ajax({
          type: "POST",
          url: "/members/changePasswd",
          data: formData,
          success: function (response) {
              // 변경 성공 시 알림창 띄우기
              alert(response);
              location.href = "/";
          },
          error: function (xhr, status, error) {
              // 변경 실패 시 알림창 띄우기
              alert("Failed to change password. Please try again.");
          }
      });
  }
  });
});

$(document).on("click", "#passwordCheckBtn", function (e) {
e.preventDefault();
var beforePasswd = $("#password").val();
var afterPasswd = $("#passwordCheck").val();
if (beforePasswd !== "" && afterPasswd !== "") {
if (beforePasswd !== afterPasswd) {
// 비밀번호 일치
$("#passwordCheck").val("");
alert("비밀번호가 일치하지 않습니다.");
} else if(!(beforePasswd.length >= 8 && beforePasswd.length <= 16)) {
   alert("비밀번호를 8자 이상, 16자 이하로 입력해 주세요.");
}else {
  alert("비밀번호 확인 되었습니다.");
// 비밀번호 불일치호
$("#password").prop("readonly", true);
$("#passwordCheck").prop("readonly", true);
$("#passwordCheckBtn").html("인증 완료");
$("#passwordCheckBtn").css("background-color", "white");
$("#passwordCheckBtn").css("color", "black");
$("#passwordCheckBtn").css("border", "1px solid gray");
}
} else {
alert("비밀번호를 입력해주세요.");
}
});