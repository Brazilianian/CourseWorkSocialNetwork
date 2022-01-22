function setCarousel(input){
    addButtons(input);
    addImages(input);
}

function addButtons(input){
    document.getElementById("buttons").remove();

    var newDiv = document.createElement("div");
    newDiv.setAttribute('id', 'buttons');

    for(let i = 0; i < input.files.length; i++){
        var button = document.createElement('button');
        button.setAttribute('type', 'button');
        button.setAttribute('data-bs-target', '#carouselExampleIndicators');
        button.setAttribute('data-bs-slide-to', i);

        if(i == 0){
            button.setAttribute('class', 'active');
            button.setAttribute('aria-current', 'true');
        }

        button.setAttribute('aria-label', 'Slide ' + (i + 1));
        newDiv.appendChild(button);
    }
      document.getElementById("carousel-indicators").appendChild(newDiv);
}

function addImages(input){
    document.getElementById("carousel").remove();

    var newDiv = document.createElement("div");
    newDiv.setAttribute('id', 'carousel');

    for(let i = 0; i < input.files.length; i++){

        let reader = new FileReader();
        var file = input.files[i];
        reader.readAsDataURL(file);

        reader.onload = function(){
            var imgDiv = document.createElement("div");

            if(i == 0){
                imgDiv.setAttribute('class', 'carousel-item active');
            } else{
                imgDiv.setAttribute('class', 'carousel-item');
            }

            var img = document.createElement('img');

            img.src = reader.result;
            img.setAttribute('class', 'd-block');
            img.setAttribute("width", '100%');
            img.setAttribute("height", '100%');

            imgDiv.appendChild(img);
            newDiv.appendChild(imgDiv);
        }
    }
    document.getElementById("carousel-items").appendChild(newDiv);
}

function setImages(){
    var files = document.getElementById('image').files;
    var mainDiv = document.getElementById('items');

    mainDiv.innerHTML = '';
    document.getElementById('audio').value = null;
    document.getElementById('video').value = null;

    for(let i = 0; i < files.length; i++){

        let reader = new FileReader();
        var file = files[i];
        reader.readAsDataURL(file);

        reader.onload = function(){
            var img = document.createElement('img');

            img.src = reader.result;
            img.setAttribute('class', 'mini-img');
            mainDiv.appendChild(img);
        }
    }
}

function setVideos(){

    var files = document.getElementById('video').files;
    var mainDiv = document.getElementById('items');

    mainDiv.innerHTML = '';
    document.getElementById('audio').value = null;
    document.getElementById('image').value = null;

    for(let i = 0; i < files.length; i++){

        let reader = new FileReader();
        var file = files[i];
        reader.readAsDataURL(file);

        reader.onload = function(){
           var video = document.createElement('video');
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

function changeValue (value) {
    let button = document.getElementById('flexCheckDefault');
    button.setAttribute("onclick", 'changeValue(' + !value + ')');
    button.setAttribute("value", !value);
}