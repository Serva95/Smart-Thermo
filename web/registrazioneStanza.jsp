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
                            <div class="9u 12u(mobilep)">
                                <h4>Temperatura Massima (Se la temperatura della stanza supera questo valore il riscaldamento della stessa si bloccher&agrave;) - Deve essere minore di 99&deg;C</h4>
                            </div>
                            <div class="3u 12u(mobilep)">
                                <input type="number" id="tempMax" placeholder="Massima Temperatura" min="0" max="99" step="0.1" required/>
                            </div>
                            <div class="12u 12u(mobilep)">
                                <hr>
                            </div>
                            <div class="9u 12u(mobilep)">
                                <h4>Temperatura Minima (Nei momenti in cui il riscaldamento &egrave; in modalit&agrave; programma, se la temperatura scender&agrave; sotto questo valore, verr&agrave; attivato il riscaldamento per riportarla a tale valore)  - Deve essere minore della temperatura massima, minore di 99&deg;C e maggiore di 10&deg;C</h4>
                            </div>
                            <div class="3u 12u(mobilep)">
                                <input type="number" id="tempMin" placeholder="Minima Temperatura" min="0" max="99" step="0.1" required/>
                            </div>
                            <div class="12u 12u(mobilep)">
                                <hr>
                            </div>
                            <div class="9u 12u(mobilep)">
                                <h4>Temperatura Minima Assoluta (Temperatura sotto la quale il sistema &egrave; sempre attivo e riscalda anche se &egrave; impostato su spento. Questo avviene per evitare temperature troppo basse che potrebbero danneggiare le tubature.) - Deve essere minore di 10&deg;C e maggiore di 0&deg;C</h4>
                            </div>
                            <div class="3u 12u(mobilep)">
                                <input type="number" id="tempMinAbs" placeholder="Minima Temperatura Assoluta" min="0" max="99" step="0.1" required/>
                            </div>
                            <div class="12u 12u(mobilep)">
                                <hr>
                            </div>
                            <div class="3u 12u(mobilep)">
                                <input type="button" onclick="isminimum()" value="Conferma Temperature"/>
                                <div id="confermaTemp"></div>
                            </div>
                            <div class="3u 12u(mobilep)">
                                Temperatura Massima: <span id="tempMaxConfirm"></span>
                                <input type="hidden" id="settempMax" name="tempMax" value=""/>
                            </div>
                            <div class="3u 12u(mobilep)">
                                Temperatura Minima: <span id="tempMinConfirm"></span>
                                <input type="hidden" id="settempMin" name="tempMin" value=""/>
                            </div>
                            <div class="3u 12u(mobilep)">
                                Temperatura Minima assoluta: <span id="tempMinAbsConfirm"></span>
                                <input type="hidden" id="settempMinAbs" name="tempMinAbs" value=""/>
                            </div>
                            <div class="12u 12u(mobilep)">
                                <hr>
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
                                    <li><input type="submit" value="Conferma le temperature" id="inviadati" disabled="disabled"/></li>
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
    function getElId(element) {
        return document.getElementById(element)
    }
    function fone() {
        var min = getElId("timeOnUno").value;
        var max = getElId("timeOffUno").value;
        if (min !== "" && max !== "") {
            if(min > max){
                getElId("timeOffUno").value = min;
            }
        }
    }
    function isminimum() {
        var max = parseFloat(getElId("tempMax").value);
        var min = parseFloat(getElId("tempMin").value);
        var absmin = parseFloat(getElId("tempMinAbs").value);
        /*console.log(max);
        console.log(min);
        console.log(absmin);*/
        if (!isNaN(min) && !isNaN(absmin) && !isNaN(max)) {
            if(max > 99 || max <= 10 || min > 99 || min <= 10 || absmin <0 || absmin > 10 || max <= min) {
                /*confermaTemp*/
                getElId("confermaTemp").innerText = "Errore, controlla che i dati rispettino le regole di inserimento";
                getElId("inviadati").disabled = "disabled";
                getElId("inviadati").value = "Errore nelle temperature";
            }else{
                getElId("confermaTemp").innerText = "Confermate";
                getElId("settempMax").value = max;
                getElId("settempMin").value = min;
                getElId("settempMinAbs").value = absmin;
                getElId("tempMaxConfirm").innerText = max+"\xB0C";
                getElId("tempMinConfirm").innerText = min+"\xB0C";
                getElId("tempMinAbsConfirm").innerText = absmin+"\xB0C";
                getElId("inviadati").disabled = "";
                getElId("inviadati").value = "Crea la stanza";
            }
        }
    }
</script>
<%@include file="bottomjs.inc"%>
</body>
</html>