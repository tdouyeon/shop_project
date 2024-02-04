$(document).ready(function () {

$("input[name='role'][value='ADMIN']").prop('checked', true);

var errorMessage = document.getElementById("errorMessage").value;
if (errorMessage != null && errorMessage !== "") {
alert(errorMessage);
}
const chks = document.querySelectorAll(".chk");
const chkBoxLength = chks.length;

$("#chkAll").on("click", function (e) {
if (e.target.checked) {
chks.forEach(function (value) {
  value.checked = true;
});
} else {
chks.forEach(function (value) {
  value.checked = false;
});
}
});
for (let chk of chks) {
chk.addEventListener("click", function () {
let count = 0;
chks.forEach(function (value) {
  if (value.checked) {
    count++;
  }
});
  if (count !== chkBoxLength) {
      $("#chkAll").prop("checked", false);
  } else {
      $("#chkAll").prop("checked", true);
  }
});
}
});



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
      $(".confirmN").css("display", "flex");
    },
  });
  }
  });

  $(document).on("click", "#checkN", function (e) {
  e.preventDefault();
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");
  var num = $("#userNum").val();
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

  $("#tel").prop("readonly", true);
  $(".confirmN").css("display", "none");
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

$(document)
  .on("click", "#sendEmail", function (e) {
    e.preventDefault();
    var email = $("#email").val();
    var emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    if (email === "") {
      alert("이메일을 입력해주세요.");
    } else if (!emailRegex.test(email)) {
      alert("이메일 형식에 맞게 작성해주세요");
    } else {
      var token = $("meta[name='_csrf']").attr("content");
      var header = $("meta[name='_csrf_header']").attr("content");
      var email = $("#email").val();
      var url = "/members/" + email + "/mailConfirm";

      $.ajax({
        url: url,
        type: "post",
        data: email,
        beforeSend: function (xhr) {
          xhr.setRequestHeader(header, token);
        },
        dataType: "text",
        cache: false,
        success: function (result) {
          alert("인증번호가 전송되었습니다.");
          $(".confirm").css("display", "flex");
        },
        error: function (jqXHR, status, error) {
          if (jqXHR.status == "401") {
          } else {
            alert(jqXHR.responseText);
          }
        },
      });
    }
  });
$(document).on("click", "#checkEmail", function (e) {
  e.preventDefault();
  var emailNum = $("#confirmNumber").val();
  if (emailNum === "") {
    alert("인증번호를 입력해주세요.");
  } else {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var number = $("#confirmNumber").val();
    var url = "/members/" + number + "/confirmNumber";

    $.ajax({
      url: url,
      type: "post",
      data: number,
      beforeSend: function (xhr) {
        xhr.setRequestHeader(header, token);
      },
      dataType: "text",
      cache: false,
      async: false,
      success: function (result, status) {
        alert(result);
        $("#email").prop("readonly", true);
        $(".confirm").css("display", "none");
        $("#sendEmail").html("인증 완료");
        $("#sendEmail").css("background-color", "white");
        $("#sendEmail").css("color", "black");
        $("#sendEmail").css("border", "1px solid gray");
      },
      error: function (jqXHR, status, error) {
        if (jqXHR.status == "400") {
          alert("인증번호가 일치하지 않습니다.");
      $("#confirmNumber").val("");
        } else {
          alert(jqXHR.responseText);
        }
      },
    });
  }
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
    } else {
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

// submit(이벤트명), form id 입력
$(document).on("submit", "#memberJoinForm", function (e) {
  var checkEmail = $("#sendEmail").html();
  var checkPassword = $("#passwordCheckBtn").html();
  var checkNumber = $("#sendNum").html();
  if (checkEmail !== "인증 완료") {
    e.preventDefault();
    alert("이메일 인증은 필수입니다.");
  } else if (checkPassword !== "인증 완료") {
    // 비밀번호 불일치 시 폼 제출을 중지하고 사용자에게 메시지 표시
    e.preventDefault();
    alert("비밀번호 확인은 필수입니다.");
  } else if (checkNumber !== "인증 완료") {
    e.preventDefault();
    alert("연락처 인증은 필수입니다.");
  } else {
    console.log("통과");
  }
});

//본 예제에서는 도로명 주소 표기 방식에 대한 법령에 따라, 내려오는 데이터를 조합하여 올바른 주소를 구성하는 방법을 설명합니다.
function execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function (data) {
      // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

      // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
      // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
      var roadAddr = data.roadAddress; // 도로명 주소 변수
      var extraRoadAddr = ""; // 참고 항목 변수

      // 법정동명이 있을 경우 추가한다. (법정리는 제외)
      // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
      if (data.bname !== "" && /[동|로|가]$/g.test(data.bname)) {
        extraRoadAddr += data.bname;
      }
      // 건물명이 있고, 공동주택일 경우 추가한다.
      if (data.buildingName !== "" && data.apartment === "Y") {
        extraRoadAddr +=
          extraRoadAddr !== "" ? ", " + data.buildingName : data.buildingName;
      }
      // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
      if (extraRoadAddr !== "") {
        extraRoadAddr = " (" + extraRoadAddr + ")";
      }

      // 우편번호와 주소 정보를 해당 필드에 넣는다.
      document.getElementById("roadAddress").value = roadAddr;

      var guideTextBox = document.getElementById("guide");
      // 사용자가 '선택 안함'을 클릭한 경우, 예상 주소라는 표시를 해준다.
      if (data.autoRoadAddress) {
        var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
        guideTextBox.innerHTML = "(예상 도로명 주소 : " + expRoadAddr + ")";
        guideTextBox.style.display = "block";
      } else if (data.autoJibunAddress) {
        var expJibunAddr = data.autoJibunAddress;
        guideTextBox.innerHTML = "(예상 지번 주소 : " + expJibunAddr + ")";
        guideTextBox.style.display = "block";
      } else {
        guideTextBox.innerHTML = "";
        guideTextBox.style.display = "none";
      }
    },
  }).open();
}
