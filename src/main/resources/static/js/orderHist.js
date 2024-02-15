        function cancelOrder(orderId){
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");

            var url = "/order/" + orderId +"/cancel";
            var paramData = {
                orderId : orderId,
            }

            var param = JSON.stringify(paramData);
                var page = document.getElementById('pageValue').value;


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
                    alert("주문이 취소 되었습니다.");
                    location.href='/orders/'+page;
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
                 function makeReview(orderItemId){
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");

            var url = "/order/" + orderItemId +"/review";
            var paramData = {
                orderItemId : orderItemId,
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
                    alert("이동합니다.");
                location.href='/mypage/submit_review?orderItemId='+result;
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
         function reOrder(orderId) {
                     var token = $("meta[name='_csrf']").attr("content");
                     var header = $("meta[name='_csrf_header']").attr("content");

                     var url = "/order/" + orderId + "/reOrder";
                     var paramData = {
                         orderId : orderId,
                     };

                     var param = JSON.stringify(paramData);

                     $.ajax({
                         url      : url,
                         type     : "POST",
                         contentType : "application/json",
                         data     : param,
                         beforeSend : function(xhr){
                             /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
                             xhr.setRequestHeader(header, token);
                         },
                         dataType : "json",
                         cache   : false,
                         success  : function(result, status){
                                 location.href='/cart';
                         },
                         error : function(jqXHR, status, error){
                             if (jqXHR.status == '401') {
                                 location.href = '/members/login';
                                 }
                                 alert(jqXHR);
                         }
                     });
                 }