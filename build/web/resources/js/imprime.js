/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


 function imprimir(){
  var printContents = document.getElementById("imprimeDiv").innerHTML;
  var popupWin = window.open('', '_blank', 'width=600,height=600');
  popupWin.document.open();
  popupWin.document.write('<html><head>\n\
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7" crossorigin="anonymous"><link type="text/css" rel="stylesheet" href="http://localhost:8080/PAGOS/faces/javax.faces.resource/primefaces.css?ln=primefaces&v=5.0" /></head><body onload="window.print()">' + printContents + '</body></html>');
  popupWin.document.close();
 }

