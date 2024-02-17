$(document).ready(function(){
    var errorMessage = document.getElementById("errorMessage").value;
    if(errorMessage != ""){
        alert(errorMessage);
    }
        var firstCategory = "처음";
        console.log("오니?");
        // 서브카테고리 업데이트 함수 호출
        updateSubcategories(firstCategory);

    bindDomEvent();
        // #category의 변경 이벤트에 대한 리스너 등록
  $("#category").on("change", function() {
    // 선택된 값 가져오기
    var selectedCategory = $(this).val();

    // 모든 옵션 컨테이너 숨기기
    $(".option-container").hide();

    // 선택된 카테고리에 해당하는 옵션 컨테이너 보여주기
    $("#" + selectedCategory + "Options").show();

    // submitButton 활성화
    $("#submitButton").prop("disabled", false);
  });
  });

  function bindDomEvent(){
    $(".imageFile.form-control").on("change", function(){
        // a.jpg
        var fileName = $(this).val().split("\\").pop();
        var fileExt = fileName.substring(fileName.lastIndexOf(".")+1);
        //확장자 추출
        fileExt = fileExt.toLowerCase(); // 소문자 소환

        if(fileExt != "jpg" && fileExt != "jpeg" && fileExt != "gif"
        && fileExt != "png" && fileExt != "bmp"){
            alert("이미지 파일만 등록이 가능합니다.");
            $(this).val("");
            return;
        }
    });
  }
  var num1 = 0;
  function setImage(event){
  var reader = new FileReader();

  reader.onload = function(event){
  var img = document.createElement("img");
  img.setAttribute("src", event.target.result);
  img.setAttribute("class", "col-lg-6");
  document.getElementsByClassName("image_container")[num1].appendChild(img);
  num1 += 1;
  };
  reader.readAsDataURL(event.target.files[0]);
  }
  var num3 = 0;
  function setImage2(event){
  var reader = new FileReader();

  reader.onload = function(event){
  const ori = document.getElementById("card-img-top");
  if(ori != null){
    ori.remove();
  }
  var img = document.createElement("img");
  img.setAttribute("src", event.target.result);
  img.setAttribute("class", "col-lg-6");
  document.getElementsByClassName("image_container2")[num3].appendChild(img);
  if(num3 == 2) {
  num3 = 0;
  }
  else{
  num3 += 1;
  }
  };

  reader.readAsDataURL(event.target.files[0]);
  }

  var num2 = 0;

  function setDetailImage(event) {
    var reader = new FileReader();

    reader.onload = function(event) {
      var img = document.createElement("img");
      img.setAttribute("src", event.target.result);
      img.setAttribute("class", "child-img");

      document.getElementsByClassName("image_detail_container")[num2].appendChild(img);
      num2 += 1;
    };

  reader.readAsDataURL(event.target.files[0]);
}
  function setDetailImage2(event){
  var reader = new FileReader();

  reader.onload = function(event){
  const ori = document.getElementById("card-img-bottom");
  if(ori != null){
  ori.remove();
  }
  var img = document.createElement("img");
  img.setAttribute("src", event.target.result);
  img.setAttribute("class", "child-img");

      img.onload = function () {
        // 부모 요소의 높이를 자식 요소에 맞게 조절
        var parent = document.getElementById("card-img-bottom");
        console.log(img.offsetHeight);
        parent.style.height = img.offsetHeight + "px";
      };
      document.getElementsByClassName("image_detail_container2")[num2].appendChild(img);

  };

  reader.readAsDataURL(event.target.files[0]);
  }
  $('#category').change(function () {
        updateSubcategories();
    });

  function updateSubcategories(selectElement) {
      // 선택된 옵션을 가져오기
      var selectedOption;
      if (selectElement === "처음") {
      selectedOption = "bag";
      } else {
      selectedOption = selectElement.options[selectElement.selectedIndex].value;
      }
      // 선택된 옵션을 출력 또는 다른 작업에 활용할 수 있음
      console.log(selectedOption);
      var token = $("meta[name='_csrf']").attr("content");
      var header = $("meta[name='_csrf_header']").attr("content");

      // Ajax 요청
      $.ajax({
          type: 'POST',
          url: '/admin/getSubcategories',
          data: { category: selectedOption },
          beforeSend: function (xhr) {
              xhr.setRequestHeader(header, token);
          },
          success: function (data) {
              // 서버에서 받은 데이터로 서브카테고리 목록 업데이트
              updateSubcategoryOptions(data);
          },
          error: function () {
              console.error('Failed to fetch subcategories');
          }
      });
  }
          function updateSubcategoryOptions(subcategories) {
              var subcategorySelect = $('#subcategory');
              subcategorySelect.empty();

              $.each(subcategories, function(index, category) {
                  subcategorySelect.append($('<option>', {
                      value: category.id,
                      text: category.name
                  }));
              });
          }

