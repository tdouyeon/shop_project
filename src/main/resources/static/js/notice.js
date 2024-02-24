        function collapse(element) {
            var before = document.getElementsByClassName("active")[0]               // 기존에 활성화된 버튼
            if (before && document.getElementsByClassName("active")[0] != element) {  // 자신 이외에 이미 활성화된 버튼이 있으면
                before.nextElementSibling.style.maxHeight = null;   // 기존에 펼쳐진 내용 접고
                before.classList.remove("active");                  // 버튼 비활성화
            }
            element.classList.toggle("active");
            // 활성화 여부 toggle 클래스가 존재하는지 확인하고 클래스를 제거. 해당하는 클래스가 없으면 클래스를 요소에 추가
            // 버튼이 활성화되면 class="notice_title" -> class="notice_title active" 클래스 추가됨

            // nextElementSibling: 다음 Element를 반환해주는 method
            var notice_content = element.nextElementSibling;
            if (notice_content.style.maxHeight != 0) {         // 버튼 다음 요소가 펼쳐져 있으면
                notice_content.style.maxHeight = null;         // 접기
            } else {
                notice_content.style.maxHeight = notice_content.scrollHeight + "px";  // 접혀있는 경우 펼치기
            }
        }