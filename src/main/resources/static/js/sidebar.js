function loadCategories(categoryName) {
  console.log(categoryName);
  // AJAX request to send categoryName to the server
  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");
  $.ajax({
      url: '/shop/' + categoryName,
      method: 'GET',
      beforeSend: function (xhr) {
          xhr.setRequestHeader(header, token);
      },
      success: function(data) {
          // 클라이언트에서는 받아온 데이터로 화면을 업데이트
          updateItemsOnPage(data.content);
      },
      error: function() {
          console.error('Error loading category data.');
      }
  });
}

function updateItemsOnPage(data) {
  console.log('Received data:', data);

  // Clear existing content
  $('.main__content').empty();

  // Check if data is empty
  if (data.length === 0) {
      // If no data, display a message
      var noDataMessage = '<p>No items found for this category.</p>';
      $('.main__content').append(noDataMessage);
  } else {
      // Iterate through the received data and append new items to the list
      for (var i = 0; i < data.length; i++) {
          var item = data[i];
          var newItem =
              '<li class="card">' +
              '<a href="/item/' + item.id + '" class="text-dark">' +
              '<img src="' + item.imgUrl + '" class="card-img-top" alt="' + item.itemNm + '">' +
              '<div class="card-body">' +
              '<h4 class="card-title">' + item.itemNm + '</h4>' +
              '<h3 class="card-price">' + item.price + 'won</h3>' +
              '</div>' +
              '</a>' +
              '</li>';

          // Append the new item to the list
          $('.main__content').append(newItem);
      }
  }
}

function toggleSubCategories(categoryName) {
    var allSubCategories = document.querySelectorAll('.category_ch');

    // 각 하위 카테고리에 대해 반복
    allSubCategories.forEach(function(subCategory) {
        // 선택한 카테고리의 하위 항목이 아니라면 닫습니다.
        if (!subCategory.classList.contains(categoryName)) {
            subCategory.classList.remove('show');
        }
    });

    // 선택한 카테고리의 하위 항목들을 선택
    var selectedSubCategories = document.querySelectorAll('.category_ch.' + categoryName);

    // 각 선택한 하위 카테고리에 대해 반복
    selectedSubCategories.forEach(function(selectedSubCategory) {
        // 선택한 카테고리의 하위 카테고리가 열려 있다면 닫고, 그렇지 않으면 엽니다.
        if (selectedSubCategory.classList.contains('show')) {
            selectedSubCategory.classList.remove('show');
        } else {
            selectedSubCategory.classList.add('show');
        }
    });
}

$(document).ready(function() {
    $('.category_ch a').on('click', function() {
            $('.category_ch a').css({
                'color': '',
                'text-decoration':'none',
            });
        $(this).css({
            'color': 'black',
            'text-decoration': 'underline',
        });

        $('.category_ch').not(this).css({
            'color': '',
            'text-decoration':'none',
        });
    });

    // 여기에 다른 이벤트나 함수들을 추가할 수 있습니다.
    // 예: 다른 카테고리 로드 함수, 서브카테고리 토글 함수 등.
});
document.addEventListener('DOMContentLoaded', function() {
    var categoryShop = document.querySelector('.category_shop');

    categoryShop.addEventListener('mouseenter', function() {
        // hover 시작 시 처리
        var anchorElement = this.querySelector('a');
        anchorElement.style.color = 'gray';
    });

    categoryShop.addEventListener('mouseleave', function() {
        // hover 종료 시 처리
        var anchorElement = this.querySelector('a');
        anchorElement.style.color = 'black';
    });
});