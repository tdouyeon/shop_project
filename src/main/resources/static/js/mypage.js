function cancelOrder(orderId) {
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");

  var url = "/order/" + orderId + "/cancel";
  var paramData = {
    orderId: orderId,
  };

  var param = JSON.stringify(paramData);

  $.ajax({
    url: url,
    type: "POST",
    contentType: "application/json",
    data: param,
    beforeSend: function (xhr) {
      /*데이터 전송하기 전에 헤더이 csrf 값을 설정*/
      xhr.setRequestHeader(header, token);
    },
    dataType: "json",
    cache: false,
    success: function (result, status) {
      alert("주문이 취소 되었습니다.");
      location.href = "/mypage/mypage";
    },
    error: function (jqXHR, status, error) {
      if (jqXHR.status == "401") {
        alert("로그인 후 이용해주세요.");
        location.href = "/members/login";
      } else {
        alert(jqXHR.responseText);
      }
    },
  });
}
function makeReview(orderItemId) {
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");

  var url = "/order/" + orderItemId + "/review";
  var paramData = {
    orderItemId: orderItemId,
  };

  var param = JSON.stringify(paramData);

  $.ajax({
    url: url,
    type: "POST",
    contentType: "application/json",
    data: param,
    beforeSend: function (xhr) {
      /*데이터 전송하기 전에 헤더이 csrf 값을 설정*/
      xhr.setRequestHeader(header, token);
    },
    dataType: "json",
    cache: false,
    success: function (result, status) {
      location.href = "/mypage/submit_review?orderItemId=" + result;
    },
    error: function (jqXHR, status, error) {
      if (jqXHR.status == "401") {
        alert("로그인 후 이용해주세요.");
        location.href = "/members/login";
      } else {
        alert(jqXHR.responseText);
      }
    },
  });
}
function reOrder(orderId) {
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");

  var url = "/order/" + orderId + "/reOrder";
  var paramData = {
    orderId: orderId,
  };

  var param = JSON.stringify(paramData);

  $.ajax({
    url: url,
    type: "POST",
    contentType: "application/json",
    data: param,
    beforeSend: function (xhr) {
      /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
      xhr.setRequestHeader(header, token);
    },
    dataType: "json",
    cache: false,
    success: function (result, status) {
      location.href = "/cart";
    },
    error: function (jqXHR, status, error) {
      if (jqXHR.status == "401") {
        location.href = "/members/login";
      }
      alert(jqXHR);
    },
  });
}
document.addEventListener("DOMContentLoaded", function () {
  var swiper = new Swiper(".mySwiper", {
    slidesPerView: 3,
    centeredSlides: true,
    spaceBetween: 30,
    pagination: {
      el: ".swiper-pagination",
      type: "fraction",
    },
    navigation: {
      nextEl: ".swiper-button-next",
      prevEl: ".swiper-button-prev",
    },
  });
});
function deleteMember() {
  var email = document
    .getElementById("deleteMemberButton")
    .getAttribute("value");
  console.log(email);
  var url = "/members/deleteMember/" + email; // URL 수정
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");

  $.ajax({
    url: url,
    type: "DELETE",
    contentType: "application/json",
    beforeSend: function (xhr) {
      xhr.setRequestHeader(header, token);
    },
    dataType: "text", // 이 부분을 추가
    cache: false,
    success: function (response) {
      alert(response);
      location.href = "/";
    },
    error: function (jqXHR, status, error) {
      alert("회원 탈퇴 중 에러가 발생하였습니다.");
      alert(jqXHR.responseText);
    },
  });
}
