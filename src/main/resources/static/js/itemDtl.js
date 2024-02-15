        $(document).ready(function(){
  // 리뷰 탭 클릭 시 스크롤 이동

$(".item_dtl_header div:nth-child(1)").on("click", function () {
  var detailSectionTop = $(".text-center").offset().top;
  $("html, body").animate({ scrollTop: detailSectionTop }, 500);
});
  $(".item_dtl_header div:nth-child(2)").on("click", function () {
      var reviewSectionTop = $(".review_title_c").offset().top;
      $("html, body").animate({ scrollTop: reviewSectionTop }, 500);
  });

  // 스크롤 이벤트 처리 (리뷰 섹션이 화면 상단에 닿으면 상세설명 탭으로 변경)
  var header = $(".item_dtl_header");
  var headerOffset = header.offset().top;

  $(window).scroll(function () {
      if ($(window).scrollTop() >= headerOffset) {
          header.css({
              "position": "fixed",
              "top": "0",
              "width": "725",
              "background-color": "#fff",
              "z-index": "1000"
          });
      } else {
          header.css({
              "position": "relative",
              "top": "auto",
              "background-color": "initial",
              "z-index": "auto"
          });
      }
  });
           calculateTotalPrice();

           $("#count").change(function(){
               calculateTotalPrice();
           });
               var header = $(".item_dtl_header");
    var headerOffset = header.offset().top;

    $(window).scroll(function () {
        if ($(window).scrollTop() >= headerOffset) {
            header.css({
                "position": "fixed",
                "top": "0",
                "width": "725",
                "background-color": "#fff",
                "z-index": "1000"
            });
        } else {
            header.css({
                "position": "relative",
                "top": "auto",
                "background-color": "initial",
                "z-index": "auto"
            });
        }
    });
        });

        function calculateTotalPrice(){
           var count = $("#count").val();
           var price = $("#price").val();
           var totalPrice = price * count;
           $("#totalPrice").html(totalPrice + '원');
        }

        function order(){
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");

            var url = "/order";
            var paramData = {
                itemId : $("#itemId").val(),
                count : $("#count").val()
            }

            var param = JSON.stringify(paramData);

            $.ajax({
                url : url,
                type : "POST",
                contentType : "application/json",
                data : param,
                beforeSend : function(xhr){
                    /*데이터 전송하기 전에 헤더이 csrf 값을 설정*/
                    xhr.setRequestHeader(header, token);
                },
                dataType : "json",
                cache : false,
                success : function(result, status){
                    alert("주문이 완료 되었습니다.");
                    location.href='/';
                },
                error : function(jqXHR, status, error){
                    if(jqXHR.status == '401'){
                        alert('로그인 후 이용해주세요.');
                        location.href='/members/login';
                    }else{
                        alert(jqXHR.responseText);
                    }
                }
            });
         }

        function addCart(){
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");

            var url = "/cart";

            var paramData = {
                itemId : $("#itemId").val(),
                count : $("#count").val()
            };
            var param = JSON.stringify(paramData);

            $.ajax({
                url : url,
                type : "POST",
                contentType : "application/json",
                data : param,
                beforeSend : function(xhr){
                    /*데이터 전송하기 전에 헤더이 csrf 값을 설정*/
                    xhr.setRequestHeader(header, token);
                },
                dataType : "json",
                cache : false,
                success : function(result, status){
                    alert("상품을 장바구니에 담았습니다.");
                    location.href='/';
                },
                error : function(jqXHR, status, error){
                    if(jqXHR.status == '401'){
                        alert('로그인 후 이용해주세요.');
                        location.href='/members/login';
                    }else{
                        alert(jqXHR.responseText);
                    }
                }
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