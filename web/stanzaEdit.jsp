<%@page session="false"%>
<%@page import="model.session.mo.LoggedUser"%>
<%@ page import="java.time.DayOfWeek" %>
<%@ page import="java.time.format.TextStyle" %>
<%@ page import="java.util.Locale" %>
<%@ page import="model.mo.Stanza" %>
<%@ page import="java.time.LocalTime" %>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  LoggedUser loggedUser = (LoggedUser) request.getAttribute("loggedUser");
  String appMessage = (String)  request.getAttribute("appMessage");
  DayOfWeek[] dayOfWeek = DayOfWeek.values();
  Stanza stanza = (Stanza) request.getAttribute("stanza");
    LocalTime[][] orariOnOff = stanza.getTurnOnOffTimes();
%>
<!DOCTYPE html>
<html>
<head>
    <title>Modifica la stanza: <%=stanza.getNome()%></title>
    <%@include file="headheader.inc"%>
</head>
<body>
<div id="page-wrapper">
    <%@include file="header.inc"%>
    <section id="main" class="container">
        <div class="row">
            <div class="12u">
                <section class="box">
                    <h3>Modifica la stanza: <%=stanza.getNome()%></h3>
                    <form method="post" action="Connector" autocomplete="on" name="confirm">
                        <input type="hidden" name="ca" value="TermoManagement.update"/>
                        <input type="hidden" name="id" value="<%=stanza.getId()%>"/>
                        <div class="row uniform 50%">
                            <div class="6u 12u(mobilep)">
                                <h3>Nome della stanza</h3>
                                <input type="text" name="name" id="name" value="<%=stanza.getNome()%>" placeholder="Nome della stanza" maxlength="45" required pattern="[A-Za-z0-9 ]+" title="Solo lettere o numeri"/>
                            </div>
                            <div class="12u 12u(mobilep)">
                                <h3><b>Temperature</b></h3>
                            </div>
                            <div class="9u 12u(mobilep)">
                                <h4>Temperatura Massima (Se la temperatura della stanza supera questo valore il riscaldamento della stessa si bloccher&agrave;) - Deve essere minore di 99&deg;C</h4>
                            </div>
                            <div class="3u 12u(mobilep)">
                                <input type="number" id="tempMax" value="<%=stanza.getMaxTemp()%>" placeholder="Massima Temperatura" min="0" max="99" step="0.1" required/>
                            </div>
                            <div class="12u 12u(mobilep)">
                                <hr>
                            </div>
                            <div class="9u 12u(mobilep)">
                                <h4>Temperatura Minima (Nei momenti in cui il riscaldamento &egrave; in modalit&agrave; programma, se la temperatura scender&agrave; sotto questo valore, verr&agrave; attivato il riscaldamento per riportarla a tale valore)  - Deve essere minore della temperatura massima, minore di 99&deg;C e maggiore di 10&deg;C</h4>
                            </div>
                            <div class="3u 12u(mobilep)">
                                <input type="number" id="tempMin" value="<%=stanza.getMinTemp()%>" placeholder="Minima Temperatura" min="0" max="99" step="0.1" required/>
                            </div>
                            <div class="12u 12u(mobilep)">
                                <hr>
                            </div>
                            <div class="9u 12u(mobilep)">
                                <h4>Temperatura Minima Assoluta (Temperatura sotto la quale il sistema &egrave; sempre attivo e riscalda anche se &egrave; impostato su spento. Questo avviene per evitare temperature troppo basse che potrebbero danneggiare le tubature.) - Deve essere minore di 10&deg;C e maggiore di 0&deg;C</h4>
                            </div>
                            <div class="3u 12u(mobilep)">
                                <input type="number" id="tempMinAbs" value="<%=stanza.getAbsoluteMin()%>" placeholder="Minima Temperatura Assoluta" min="0" max="99" step="0.1" required/>
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
                                    <br>Ricorda inoltre che l'orario di spegnimento della fascia 1 deve essere antecedente a quello di accensione della fascia 2 e cos&igrave; via, altrimenti verrano scartati i dati non corretti</h3>
                                <h4><i>Solo la prima fascia &egrave; obbligatoria, le altre, se lasciate vuote, verranno ignorate. Ricorda: se lasci vuota la fascia 2, la fascia 3 non verr&agrave; letta.</i></h4>
                            </div>
                            <% for(int i=0; i<7; i++){%>
                            <div class="<%if(i!=0){%>6u 6u<%}else{%>12u 12u<%}%>(mobilep)">
                                <%if(i!=6){%>
                                <h3>Fascie del <%=dayOfWeek[i].getDisplayName(TextStyle.FULL, Locale.ITALIAN)%></h3>
                                <%}else{%>
                                <h3>Fascie della <%=dayOfWeek[i].getDisplayName(TextStyle.FULL, Locale.ITALIAN)%></h3>
                                <%}%>
                            </div>
                            <%if(i!=0){%>
                            <div class="6u 6u(mobilep)">
                                <a class="button" id="copia" onclick="copia(<%=i%>)">Copia dal luned&igrave;</a>
                            </div>
                            <%}%>
                            <div class="6u 12u(mobilep)">
                                <h4>Orario Accensione Fascia 1</h4>
                                <input type="time" name="timeonuno<%=i%>" id="timeOnUno<%=i%>" min="00:00" max="23:59" required value="<%=orariOnOff[i][0]%>"/>
                            </div>
                            <div class="6u 12u(mobilep)">
                                <h4>Orario Spegnimento Fascia 1</h4>
                                <input type="time" name="timeoffuno<%=i%>" id="timeOffUno<%=i%>" min="00:00" max="23:59" required value="<%=orariOnOff[i][1]%>"/>
                            </div>
                            <div class="6u 12u(mobilep)">
                                <h4>Orario Accensione Fascia 2</h4>
                                <input type="time" name="timeondue<%=i%>" id="timeOnDue<%=i%>" min="00:00" max="23:59" <%if(orariOnOff[i][2]!=null) out.print("value=\"" +orariOnOff[i][2]+'"');%>/>
                            </div>
                            <div class="6u 12u(mobilep)">
                                <h4>Orario Spegnimento Fascia 2</h4>
                                <input type="time" name="timeoffdue<%=i%>" id="timeOffDue<%=i%>" min="00:00" max="23:59" <%if(orariOnOff[i][3]!=null) out.print("value=\"" +orariOnOff[i][3]+'"');%>/>
                            </div>
                            <div class="6u 12u(mobilep)">
                                <h4>Orario Accensione Fascia 3</h4>
                                <input type="time" name="timeontre<%=i%>" id="timeOnTre<%=i%>" min="00:00" max="23:59" <%if(orariOnOff[i][4]!=null) out.print("value=\"" +orariOnOff[i][4]+'"');%>/>
                            </div>
                            <div class="6u 12u(mobilep)">
                                <h4>Orario Spegnimento Fascia 3</h4>
                                <input type="time" name="timeofftre<%=i%>" id="timeOffTre<%=i%>" min="00:00" max="23:59" <%if(orariOnOff[i][5]!=null) out.print("value=\"" +orariOnOff[i][5]+'"');%>/>
                            </div>
                            <div class="12u 12u(mobilep)">
                                <hr>
                            </div>
                            <%}%>
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
<script type="application/javascript">
    function getElId(element) {
        return document.getElementById(element)
    }
    function isminimum() {
        var max = parseFloat(getElId("tempMax").value);
        var min = parseFloat(getElId("tempMin").value);
        var absmin = parseFloat(getElId("tempMinAbs").value);
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
                getElId("inviadati").value = "Salva le modifiche";
            }
        }
    }
    function copia(sectionID) {
        $("#timeOnUno"+sectionID).val($("#timeOnUno0").val())
        $("#timeOffUno"+sectionID).val($("#timeOffUno0").val())
        $("#timeOnDue"+sectionID).val($("#timeOnDue0").val())
        $("#timeOffDue"+sectionID).val($("#timeOffDue0").val())
        $("#timeOnTre"+sectionID).val($("#timeOnTre0").val())
        $("#timeOffTre"+sectionID).val($("#timeOffTre0").val())
    }
</script>
<%@include file="bottomjs.inc"%>
</body>
</html>