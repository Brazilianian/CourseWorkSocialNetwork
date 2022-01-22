function setImage(input){

    var img1 = document.getElementById('img1');
    var img2 = document.getElementById('img2');

    let file = input.files[0];
    let reader = new FileReader();
    reader.readAsDataURL(file);

    reader.onload = function(){
        if(img1 != null){
            img1.src = reader.result;
        } else{
            img2.src = reader.result;
        }
    }
}