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
            console.log("오니?");
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
        console.log("오긴 와?");
        console.log(data.length);
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