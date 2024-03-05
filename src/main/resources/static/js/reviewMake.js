document.addEventListener("DOMContentLoaded", function () {
  const ratingInputs = document.querySelectorAll(".rating input");
  const ratingLabels = document.querySelectorAll(".rating label");

  function updateRating(clickedIndex) {
    // 클릭한 별까지 채우기
    console.log(clickedIndex);
    for (let i = 0; i <= clickedIndex; i++) {
      ratingInputs[i].checked = true;
      ratingLabels[i].classList.add("filled");
    }

    // 클릭하지 않은 별 비우기
    for (let i = clickedIndex + 1; i < ratingInputs.length; i++) {
      console.log(ratingInputs.length);
      ratingInputs[i].checked = false;
      ratingLabels[i].classList.remove("filled");
    }
  }

  ratingLabels.forEach((label, index) => {
    label.addEventListener("click", function () {
      const clickedIndex = index;
      updateRating(clickedIndex);
    });
  });
});
function previewImages(input) {
  var preview = document.getElementById("preview");
  while (preview.firstChild) {
    preview.removeChild(preview.firstChild);
  }

  if (input.files && input.files.length > 0) {
    for (var i = 0; i < input.files.length; i++) {
      var reader = new FileReader();

      reader.onload = function (e) {
        var image = document.createElement("img");
        image.src = e.target.result;
        preview.appendChild(image);
      };

      reader.readAsDataURL(input.files[i]);
    }
  }
}
