let star = document.querySelectorAll('input');
let showValue = document.querySelector('#rating-value');

const totalStars = 5;

let starCount;

for(let i = 0; i < star.length; i++){
    star[i].addEventListener('click', function(){
        i = this.value;

        starCount = i;

        showValue.innerHTML = i;

        const starPercentage = (starCount / totalStars) * 100;
        console.log(starPercentage);
    });
}

