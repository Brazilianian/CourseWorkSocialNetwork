function readMore(id) {
  var dots = document.getElementById("dots");
  console.log(dots);
  var moreText = document.getElementById("more");
  console.log(moreText);
  var btnText = document.getElementById("myBtn" + id);
  console.log(btnText);

  if (dots.style.display === "none") {
    dots.style.display = "inline";
    btnText.innerHTML = "Читати далі";
    moreText.style.display = "none";
  } else {
    dots.style.display = "none";
    btnText.innerHTML = "Сховати";
    moreText.style.display = "inline";
  }
}
