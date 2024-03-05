$(document).ready(function () {
  $("input[name=cartChkBox]").change(function () {
    getOrderTotalPrice();
  });
});

function getOrderTotalPrice() {
  var orderTotalPrice = 0;
  $("input[name=cartChkBox]:checked").each(function () {
    var cartItemId = $(this).val();
    var price = $("#price_" + cartItemId).attr("data-price");
    var count = $("#count_" + cartItemId).val();
    orderTotalPrice += price * count;
  });

  $("#orderTotalPrice").html(orderTotalPrice + "원");
}

function changeCount(obj) {
  var count = obj.value;
  var cartItemId = obj.id.split("_")[1];
  var price = $("#price_" + cartItemId).data("price");
  var totalPrice = count * price;
  $("#totalPrice_" + cartItemId).html(totalPrice + "원");
  getOrderTotalPrice();
  updateCartItemCount(cartItemId, count);
}
function checkAll() {
  if ($("#checkall").prop("checked")) {
    $("input[name=cartChkBox]").prop("checked", true);
  } else {
    $("input[name=cartChkBox]").prop("checked", false);
  }
  getOrderTotalPrice();
}

function updateCartItemCount(cartItemId, count) {
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");

  var url = "/cartItem/" + cartItemId + "?count=" + count;

  $.ajax({
    url: url,
    type: "PATCH",
    beforeSend: function (xhr) {
      xhr.setRequestHeader(header, token);
    },
    dataType: "json",
    cache: false,
    success: function (result, status) {
      console.log("cartItem count update success");
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

function deleteCartItem(obj) {
  var cartItemId = obj.dataset.id;
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");

  var url = "/cartItem/" + cartItemId;

  $.ajax({
    url: url,
    type: "DELETE",
    beforeSend: function (xhr) {
      xhr.setRequestHeader(header, token);
    },
    dataType: "json",
    cache: false,
    success: function (result, status) {
      location.href = "/cart";
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

function orders() {
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");

  $.ajax({
    url: "/members/inf",
    type: "GET",
    beforeSend: function (xhr) {
      xhr.setRequestHeader(header, token);
    },
    success: function (member) {
      var itemNm = $("#itemNm").text();
      // 테스트 용이라 실제로 값을 집어넣진 못함
      var amount = $("#count").val();
      var IMP = window.IMP;
      IMP.init("imp15341074");
      IMP.request_pay(
        {
          pg: "kakaopay",
          pay_method: "card",
          merchant_uid: "merchant_" + new Date().getTime(),
          name: "테스트 상점",
          amount: 1004,
          buyer_email: member.email,
          buyer_name: member.name,
          buyer_tel: member.tel,
          buyer_addr: member.address,
        },
        function (rsp) {
          console.log(rsp);
          $.ajax({
            type: "POST",
            url: "/order/" + rsp.imp_uid,
            beforeSend: function (xhr) {
              /*데이터 전송하기 전에 헤더이 csrf 값을 설정*/
              xhr.setRequestHeader(header, token);
            },
          }).done(function (data) {
            if (rsp.paid_amount === data.response.amount) {
              alert("결제 성공");

              var url = "/cart/orders";

              var dataList = new Array();
              var paramData = new Object();

              $("input[name=cartChkBox]:checked").each(function () {
                var cartItemId = $(this).val();
                var data = new Object();
                data["cartItemId"] = cartItemId;
                dataList.push(data);
              });

              paramData["cartOrderDtoList"] = dataList;
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
                  alert("주문이 완료 되었습니다.");
                  location.href = "/cart";
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
          });
        }
      );
    },
  });
}
