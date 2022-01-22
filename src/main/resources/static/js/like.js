function like(id, isLiked){
   var likeButton = document.getElementById('likeButton' + id);
   var likeForm = document.getElementById('likeForm' + id);
   var likesCount = document.getElementById('likesCount' + id);
   let count = likesCount.innerHTML;
   var span = document.getElementById('span' + id);


   if (isLiked) {
    count--;
    span.innerHTML = '<i class="far fa-heart"></i>';
    likeButton.setAttribute("onclick", 'like(' + id + ', false)');
   } else {
    count++;
    span.innerHTML = '<i class="fas fa-heart" style="color: red;"></i>';
    likeButton.setAttribute("onclick", 'like(' + id + ', true)');
   }
   likesCount.innerHTML = count;
}

function likeComment(id, isLiked){
   var likeButtonComment = document.getElementById('likeButtonComment' + id);
   var likeFormComment = document.getElementById('likeFormComment' + id);
   var likesCountComment = document.getElementById('likesCountComment' + id);
   let count = likesCountComment.innerHTML;
   var spanComment = document.getElementById('spanComment' + id);

   if (isLiked) {
    count--;
    spanComment.innerHTML = '<i class="far fa-heart"></i>';
    likeButtonComment.setAttribute("onclick", 'likeComment(' + id + ', false)');
   } else {
    count++;
    spanComment.innerHTML = '<i class="fas fa-heart" style="color: red;"></i>';
    likeButtonComment.setAttribute("onclick", 'likeComment(' + id + ', true)');
   }
   likesCountComment.innerHTML = count;
}

function hideFromUsers (isHide, id) {
    var button = document.getElementById('hideButton' + id);
    if (isHide) {
        button.innerHTML = '<i class="far fa-eye"></i>';
        button.setAttribute("onclick", 'hideFromUsers(false, ' + id + ')');
     } else {
        button.innerHTML = '<i class="far fa-eye-slash"></i>';
         button.setAttribute("onclick", 'hideFromUsers(true, ' + id + ')');
     }
}

function deletePost (id){
    var subform = document.getElementById('subForm' + id).submit();

    var form = document.getElementById('post' + id).remove();
    console.log(form);
}