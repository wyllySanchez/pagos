// JavaScript Document
function aplicarEstilos(elemento, listaEstilos) {

    for (var estilo in listaEstilos) {

        elemento.style[estilo] = listaEstilos[estilo];
    }

}


function lupa() {
var lupa =  document.getElementById("ventana");
var alto=document.getElementById("alto").value;
var ancho=document.getElementById("ancho").value;

var ctx = lupa.getContext("2d");

ctx.clearRect(10,10,300,300);
ctx.fillStyle="#4CAF50";
ctx.fillRect(10,10,ancho,alto);


$('.carousel').carousel({
  interval: 500
});






}


