$(document).ready(function () {
  $("searchBtn").on("click", function (e) {
    e.preventDefault(); //검색버튼 클릭시 form 태그 전송을 막습니다.
    page(0);
  });
});

function page(page) {
  var searchDateType = $("#searchDateType").val();
  var searchSellStatus = $("#searchSellStatus").val();
  var searchBy = $("#searchBy").val();
  var searchQuery = $("#searchQuery").val();

  location.href =
    "/admin/items/" +
    page +
    "?searchDateType=" +
    searchDateType +
    "&searchSellStatus=" +
    searchSellStatus +
    "&searchBy=" +
    searchBy +
    "&searchQuery=" +
    searchQuery;
}
