<%@page import="model.mo.Stanza"%>
<%@page session="false"%>
<%@page import="model.session.mo.LoggedUser"%>
<%@ page import="model.mo.Sensore" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    LoggedUser loggedUser = (LoggedUser) request.getAttribute("loggedUser");
    Sensore[] sensori = (Sensore[]) request.getAttribute("sensori");
    String appMsg = (String) request.getAttribute("appMsg");
    int size = 0;
    if(sensori!=null) size = sensori.length;
%>

<!DOCTYPE HTML>
<html lang="it-IT">
<head>
    <meta charset="UTF-8">
    <%@include file="headheader.inc"%>
    <title>Gestione Sensori</title>
    <script>
        function elimina(el) {
            document.deleteSensore.id.value = el;
            document.deleteSensore.submit();
        }
    </script>
</head>
<body>
<div id="page-wrapper">
    <%@include file="header.inc"%>
    <section id="main" class="container">
        <div class="row">
            <div class="12u">
                <section class="box">
                    <div class="row uniform 50%">
                        <div class="2u 4u(mobilep)">
                            <a href='/Connector' class="button icon solid fa-chevron-left">Indietro</a>
                        </div>
                        <div class="10u 8u(mobilep)">
                            <h3>Qui puoi gestire i tuoi sensori</h3>
                        </div>
                        <div class="12u 12u(mobilep)"><hr></div>
                        <%if(appMsg != null && appMsg.length()>1){ %>
                        <div class="12u 12u(mobilep)">
                            <h3 style="color: red"><%out.print(appMsg); %></h3>
                        </div>
                        <%} %>
                        <div class="4u 5u(mobilep)">
                            <h3>Le tue stanze</h3>
                        </div>
                        <div class="8u 7u(mobilep)">
                            <a class="button" href="Connector?ca=TermoManagement.insertNewView">Crea un nuovo sensore</a>
                        </div>
                        <div class="12u 12u(mobilep)">
                            <br>
                        </div>
                        <% if (size<=0){ %>
                        <div class="12u 12u(mobilep)">
                            <h3>Ancora non &egrave; presente alcun sensore. Inizia creandone uno nuovo.</h3>
                        </div>
                        <% }else{
                            for(Sensore sensore : sensori){ %>
                        <div class="11u 10u(mobilep)">
                            <a href="Connector?ca=TermoManagement.viewSensor&id=<%=sensore.getId()%>" title="Clicca per vedere i dettagli del sensore"><h3><b><%=sensore.getNome().toUpperCase()%></b> (clicca per i dettagli)</h3></a>
                        </div>
                        <div class="1u 2u(mobilep)">
                            <a onclick="elimina(<%=sensore.getId()%>)" title="Elimina Sensore"><i class="far fa-trash-alt fa-3x"></i></a>
                        </div>
                        <div class="12u 12u(mobilep)">
                            <hr>
                        </div>
                        <%} }%>
                    </div>
                </section>
            </div>
        </div>
    </section>
</div>
<form name="nuovoSensore" action="Connector" method="post">
    <input type="hidden" name="ca" value="SensorManagement.insertNewView"/>
</form>
<form name="deleteSensore" action="Connector" method="post">
    <input type="hidden" name="ca" value="SensorManagement.delete"/>
    <input type="hidden" name="id"/>
</form>
<%@include file="bottomjs.inc"%>
</body>
</html>