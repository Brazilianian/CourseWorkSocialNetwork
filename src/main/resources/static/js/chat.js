function setImages(){

    let files = document.getElementById('image').files;
    let mainDiv = document.getElementById('items');

    mainDiv.innerHTML = '';
    document.getElementById('audio').value = null;
    document.getElementById('video').value = null;

    for(let i = 0; i < files.length; i++){

        let reader = new FileReader();
        let file = files[i];
        reader.readAsDataURL(file);

        reader.onload = function(){
            let img = document.createElement('img');

            img.src = reader.result;
            img.setAttribute('class', 'mini-img');
            console.log(img);
            mainDiv.appendChild(img);
        }
    }
}

function setVideos(){

    let files = document.getElementById('video').files;
    let mainDiv = document.getElementById('items');

    mainDiv.innerHTML = '';
    document.getElementById('audio').value = null;
    document.getElementById('image').value = null;

    for(let i = 0; i < files.length; i++){

        let reader = new FileReader();
        let file = files[i];
        reader.readAsDataURL(file);

        reader.onload = function(){
            let video = document.createElement('video');
           video.controls = 'controls';
           video.src = reader.result;
           video.setAttribute('class', 'mini-video');

           mainDiv.appendChild(video);
        }
    }
}

function setAudios(){

    var files = document.getElementById('audio').files;
    var mainDiv = document.getElementById('items');

    mainDiv.innerHTML = '';
    document.getElementById('video').value = null;
    document.getElementById('image').value = null;


    for(let i = 0; i < files.length; i++){

        let reader = new FileReader();
        var file = files[i];
        reader.readAsDataURL(file);

        reader.onload = function(){
            var audio = document.createElement('audio');
            audio.controls = 'controls';
            audio.src = reader.result;
            audio.setAttribute('class', 'mini-audio');

            mainDiv.appendChild(audio);
        }
    }
}

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
   console.log(span);
   likesCount.innerHTML = count;
}