$(document).off().on('click', '#sendEmail', function (e) {
      e.preventDefault();
    var email = $("#email").val();
    var emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    if(email === "") {
      alert("이메일을 입력해주세요.");
    }
    else if(!emailRegex.test(email)) {
    alert("이메일 형식에 맞게 작성해주세요");
  } else {
      var token = $("meta[name='_csrf']").attr('content')
      var header = $("meta[name='_csrf_header']").attr('content')
      var email = $('#email').val()
      var url = '/members/' + email + '/mailConfirm'

      $.ajax({
        url: url,
        type: 'post',
        data: email,
        beforeSend: function (xhr) {
          xhr.setRequestHeader(header, token);
        },
        dataType: 'text',
        cache: false,
        success: function (result) {
          alert('인증번호가 전송되었습니다.')
          $('.confirm').css('display', 'flex');
        },
        error: function (jqXHR, status, error) {
          if (jqXHR.status == '401') {
          } else {
            alert(jqXHR.responseText);
          }
        },
      })
  }
  })
$(document).on('click', '#checkEmail', function (e) {
  e.preventDefault();
      var emailNum = $("#confirmNumber").val();
      if(emailNum === "") {
      alert("인증번호를 입력해주세요.")
      } else {
       var token = $("meta[name='_csrf']").attr('content')
        var header = $("meta[name='_csrf_header']").attr('content')
        var number = $('#confirmNumber').val()
        var url = '/members/' + number + '/confirmNumber'

        $.ajax({
          url: url,
          type: 'post',
          data: number,
          beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token)
          },
          dataType: 'text',
          cache: false,
          async: false,
          success: function (result, status) {
            alert(result)

            $('.confirm').css('display', 'none')
            $('#sendEmail').html('인증 완료')
            $('#sendEmail').css('background-color', 'white')
            $('#sendEmail').css('color', 'black')
            $('#sendEmail').css('border', '1px solid gray')
          },
          error: function (jqXHR, status, error) {
            if (jqXHR.status == '400') {
              alert('인증번호가 잘못 입력되었습니다')
              location.href = '/members/new'
            } else {
              alert(jqXHR.responseText)
            }
          },
        })
      }}
)
window.onload = function () {
  var errorMessage = document.getElementById('errorMessage').value
  if (errorMessage != null && errorMessage !== '') {
    alert(errorMessage)
  }
  const checkAll = document.getElementById('chkAll')
  const chks = document.querySelectorAll('.chk')
  const chkBoxLength = chks.length

  checkAll.addEventListener('click', function (event) {
    if (event.target.checked) {
      chks.forEach(function (value) {
        value.checked = true
      })
    } else {
      chks.forEach(function (value) {
        value.checked = false
      })
    }
  })
  for (chk of chks) {
    chk.addEventListener('click', function () {
      let count = 0
      chks.forEach(function (value) {
        if (value.checked == true) {
          count++
        }
      })
      if (count !== chkBoxLength) {
        checkAll.checked = false
      } else {
        checkAll.checked = true
      }
    })
  }
}

$(document).one("click", "#sendNum", function (e) {
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


$(document).one('click', '#checkN', function (e) {
  e.preventDefault()
  var token = $("meta[name='_csrf']").attr('content')
  var header = $("meta[name='_csrf_header']").attr('content')
  var num = $('#userNum').val()
  var url = '/members/' + num + '/confirmNum'

  $.ajax({
    url: url,
    type: 'post',
    data: num,
    beforeSend: function (xhr) {
      xhr.setRequestHeader(header, token)
    },
    dataType: 'text',
    cache: false,
    async: false,
    success: function (result, status) {
      alert(result)

      $('.confirmN').css('display', 'none')
      $('#sendNum').html('인증 완료')
      $('#sendNum').css('background-color', 'white')
      $('#sendNum').css('color', 'black')
      $('#sendNum').css('border', '1px solid gray')
    },
    error: function (jqXHR, status, error) {
      if (jqXHR.status == '400') {
        alert('인증번호가 잘못 입력되었습니다');

      } else {
        alert(jqXHR.responseText)
      }
    },
  })
})

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
      // 비밀번호 불일치
      $("#passwordCheckBtn").html("인증 완료");
      $("#passwordCheckBtn").css("background-color", "white");
      $("#passwordCheckBtn").css("color", "black");
      $("#passwordCheckBtn").css("border", "1px solid gray");
    }
  } else {
    alert("비밀번호를 입력해주세요.");
  }
});


$(document).on('submit', '#submitBtn', function (e) {
         var passwordBtn = $('#passwordCheckBtn').html();
        if (passwordBtn !== "인증 완료") {
            // 비밀번호 불일치 시 폼 제출을 중지하고 사용자에게 메시지 표시
            e.preventDefault();
        alert('비밀번호 확인은 필수입니다.');
        } else {
            // 비밀번호 일치 시 폼을 계속 제출
            // 필요한 경우 추가적인 로직을 수행할 수 있음
        }
    });
