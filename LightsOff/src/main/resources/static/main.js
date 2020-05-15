
const stars=document.querySelector(".rating2").children;
const ratingValue=document.querySelector("#rating-value");
let index;

for(let i=0; i<stars.length; i++){
    stars[i].addEventListener("mouseover",function(){
        for(let j=0; j<stars.length; j++){
            stars[j].classList.remove("rating-normal");
            stars[j].classList.add("rating-mark");
        }
        for(let j=0; j<=i; j++){
            stars[j].classList.remove("rating-mark");
            stars[j].classList.add("rating-normal");
        }
    })
    stars[i].addEventListener("click",function(){
        ratingValue.value=i+1;
        index=i;
    })
    stars[i].addEventListener("mouseout",function(){

        for(let j=0; j<stars.length; j++){
            stars[j].classList.remove("rating-normal");
            stars[j].classList.add("rating-mark");
        }
        for(let j=0; j<=index; j++){
            stars[j].classList.remove("rating-mark");
            stars[j].classList.add("rating-normal");
        }
    })
}

