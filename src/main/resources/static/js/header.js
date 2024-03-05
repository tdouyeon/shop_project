document.addEventListener("DOMContentLoaded", function () {
  var searchButton = document.querySelector(".search_button");
  var searchInput = document.querySelector(".navbar__search .search");

  searchButton.addEventListener("mouseenter", function () {
    searchInput.classList.add("show"); // show 클래스 추가
    setTimeout(function () {
      searchInput.classList.remove("initial-hidden"); // initial-hidden 클래스 제거
    }, 50); // 0.05초(50ms) 후에 제거
  });

  searchButton.addEventListener("mouseleave", function () {
    searchInput.classList.remove("show"); // show 클래스 제거
    setTimeout(function () {
      searchInput.classList.add("initial-hidden"); // initial-hidden 클래스 추가
    }, 300); // 0.3초(300ms) 후에 추가
  });
});
function scrollHeader() {
  const header = document.getElementById("navbar");
  // When the scroll is greater than 100 viewport height, add the scroll-header class to the header tag
  if (this.scrollY >= 100) {
    header.classList.add("scroll-header");
  } else {
    header.classList.remove("scroll-header");
  }
}
window.addEventListener("scroll", scrollHeader);
