        $(document).ready(function(){
            var errorMessage = document.getElementById("errorMessage").value;
            if(errorMessage != null){
                alert(errorMessage);
            }
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
	function setDetailImage(event){
		var reader = new FileReader();

		reader.onload = function(event){
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
			document.getElementsByClassName("image_detail_container2")[num2].appendChild(img);
		};

		reader.readAsDataURL(event.target.files[0]);
	}
        $(document).on("change", "#category", function () {
        var outerwearOptions = document.getElementById('outerwearOptions');
        var submitButton = document.getElementById('submitButton');

        if (this.value === 'clothing') {
            outerwearOptions.classList.add('active');
            submitButton.disabled = false;
        } else {
            outerwearOptions.classList.remove('active');
            submitButton.disabled = true;
        }
    });