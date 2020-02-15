<%@page session="false"%>
<%@page import="model.session.mo.LoggedUser"%>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  LoggedUser loggedUser = (LoggedUser) request.getAttribute("loggedUser");
  String appMessage = (String)  request.getAttribute("appMessage");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Registrazione Stanza</title>
    <%@include file="headheader.inc"%>
</head>
<body>
<div id="page-wrapper">
    <%@include file="header.inc"%>
    <section id="main" class="container">
        <div class="row">
            <div class="12u">
                <section class="box">
                    <h3>Registrazione Nuova Stanza</h3>
                    <form method="post" action="Connector" autocomplete="on" name="confirm">
                        <input type="hidden" name="ca" value="TermoManagement.insert"/>
                        <div class="row uniform 50%">
                            <div class="6u 12u(mobilep)">
                                <h3>Nome della stanza</h3>
                                <input type="text" name="name" id="name" placeholder="Nome della stanza" maxlength="45" required pattern="[A-Za-z0-9 ]+" title="Solo lettere o numeri"/>
                            </div>
                            <div class="12u 12u(mobilep)">
                                <h3><b>Temperature</b></h3>
                            </div>
                            <div class="6u 12u(mobilep)">
                                <h4>Temperatura Massima</h4>
                                <input type="number" name="tempMax" id="tempMax" onkeyup="isminimum()" onchange="isminimum()" placeholder="Massima Temperatura" min="0" max="99" step="0.1" required/>
                            </div>
                            <div class="6u 12u(mobilep)">
                                <h4>Temperatura Minima</h4>
                                <input type="number" name="tempMin" id="tempMin" onkeyup="isminimum()" onchange="isminimum()" placeholder="Minima Temperatura" min="0" max="99" step="0.1" required/>
                            </div>
                            <div class="6u 12u(mobilep)">
                                <h4>Temperatura Minima Assoluta (Temperatura sotto la quale il sistema &egrave; sempre acceso poich&eacute; &egrave; molto bassa)</h4>
                                <input type="number" name="tempMinAbs" id="tempMinAbs" onkeyup="isminimum()" onchange="isminimum()" placeholder="Minima Temperatura Assoluta" min="0" max="99" step="0.1" required/>
                            </div>
                            <div class="12u 12u(mobilep)">
                                <h3><b>Orari di accensione/spegnimento</b></h3>
                                <h3>Ricorda che l'orario di spegnimento deve essere successivo a quello di accensione
                                    <br>Ricorda inoltre che l'orario di spegnimento della fascia 1 deve essere antecedente a quello di accensione della fascia 2 e cos&igrave; via, altrimenti non verrano considerati i dati non corretti</h3>
                            </div>
                            <div class="6u 12u(mobilep)">
                                <h4>Orario Accensione Fascia 1</h4>
                                <input type="time" name="timeonuno" id="timeOnUno" onkeyup="fone()" onchange="fone()" min="00:00" max="23:59" required/>
                            </div>
                            <div class="6u 12u(mobilep)">
                                <h4>Orario Spegnimento Fascia 1</h4>
                                <input type="time" name="timeoffuno" id="timeOffUno" onkeyup="fone()" onchange="fone()" min="00:00" max="23:59" required/>
                            </div>
                            <div class="6u 12u(mobilep)">
                                <h4>Orario Accensione Fascia 2</h4>
                                <input type="time" name="timeondue" id="timeOnDue" min="00:00" max="23:59"/>
                            </div>
                            <div class="6u 12u(mobilep)">
                                <h4>Orario Spegnimento Fascia 2</h4>
                                <input type="time" name="timeoffdue" id="timeOffDue" min="00:00" max="23:59"/>
                            </div>
                            <div class="6u 12u(mobilep)">
                                <h4>Orario Accensione Fascia 3</h4>
                                <input type="time" name="timeontre" id="timeOnTre" min="00:00" max="23:59"/>
                            </div>
                            <div class="6u 12u(mobilep)">
                                <h4>Orario Spegnimento Fascia 3</h4>
                                <input type="time" name="timeofftre" id="timeOffTre" min="00:00" max="23:59"/>
                            </div>
                        </div>
                        <div class="row uniform">
                            <div class="12u">
                                <ul class="actions">
                                    <li><input type="submit" value="Conferma inserimento" id="inviadati"/></li>
                                    <li><input type="reset" value="Reset" class="alt" /></li>
                                    <li><a class="button special" href="javascript:annulla.submit()">Annulla</a></li>
                                </ul>
                            </div>
                        </div>
                    </form>
                </section>
            </div>
        </div>
    </section>
</div>
<form name="annulla" action="Connector" method="post">
    <input type="hidden" name="ca" value="TermoManagement.view"/>
</form>
<script>
    function fone() {
        var min = document.getElementById("timeonuno").value;
        var max = document.getElementById("timeoffuno").value;
        if (min !== "" && max !== "") {
            if(min > max){
                document.getElementById("timeoffuno").value = min;
            }
        }
    }
    function isminimum() {
        var max = document.getElementById("tempMax").value;
        var min = document.getElementById("tempMin").value;
        var absmin = document.getElementById("tempMinAbs").value;
        if (min != null && absmin != null && max != null) {
            if(max > 99) {
                alert("Temperatura massima consentita 99C");
                document.getElementById("tempMax").value = 99;
            }
            if(min > 99) {
                alert("Temperatura massima consentita 99C");
                document.getElementById("tempMin").value = 99;
            }
            if(absmin > 99) {
                alert("Temperatura massima consentita 99C");
                document.getElementById("tempMinAbs").value = 99;
            }
            if(max <0) {
                alert("Temperatura minima consentita 0C");
                document.getElementById("tempMax").value = 0;
            }
            if(min <0) {
                alert("Temperatura minima consentita 0C");
                document.getElementById("tempMin").value = 0;
            }
            if(absmin <0) {
                alert("Temperatura minima consentita 0C");
                document.getElementById("tempMinAbs").value = 0;
            }
            /**altri controlli*/
            if (min <= absmin) {
                document.getElementById("tempMinAbs").value = min;
            }
            if(max <= min){
                document.getElementById("tempMax").value = min;
            }
        }
    }
</script>
<%@include file="bottomjs.inc"%>
</body>
</html>