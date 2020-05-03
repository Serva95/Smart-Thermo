<%@page import="model.mo.Stanza"%>
<%@page session="false"%>
<%@page import="model.session.mo.LoggedUser"%>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    LoggedUser loggedUser = (LoggedUser) request.getAttribute("loggedUser");
    Stanza[] stanze = (Stanza[]) request.getAttribute("stanze");
    String appMsg = (String) request.getAttribute("appMsg");
    int size = 0;
    if(stanze!=null) size = stanze.length;
%>

<!DOCTYPE HTML>
<html lang="it-IT">
<head>
    <meta charset="UTF-8">
    <%@include file="headheader.inc"%>
    <title>Gestione Stanze</title>
    <script>
        function elimina(el) {
            document.deleteStanza.id.value = el;
            document.deleteStanza.submit();
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
                            <h3>Qui puoi gestire le tue stanze </h3>
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
                            <a class="button" href="Connector?ca=TermoManagement.insertNewView">Crea una nuova stanza</a>
                        </div>
                        <div class="12u 12u(mobilep)">
                            <br>
                        </div>
                        <% if (size<=0){ %>
                        <div class="12u 12u(mobilep)">
                            <h3>Ancora non &egrave; presente alcuna stanza. Inizia creandone una nuova.</h3>
                        </div>
                        <% }else{
                            for(Stanza stanza : stanze){ %>
                        <div class="11u 10u(mobilep)">
                            <a href="Connector?ca=TermoManagement.viewRoom&id=<%=stanza.getId()%>" title="Clicca per vedere i dettagli della stanza"><h3><b><%=stanza.getNome().toUpperCase()%></b> (clicca per i dettagli)</h3></a>
                        </div>
                        <div class="1u 2u(mobilep)">
                            <a onclick="elimina(<%=stanza.getId()%>)" title="Elimina Stanza"><i class="far fa-trash-alt fa-3x"></i></a>
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
<form name="nuovaStanza" action="Connector" method="post">
    <input type="hidden" name="ca" value="TermoManagement.insertNewView"/>
</form>
<form name="deleteStanza" action="Connector" method="post">
    <input type="hidden" name="ca" value="TermoManagement.delete"/>
    <input type="hidden" name="id"/>
</form>
<%@include file="bottomjs.inc"%>
</body>
</html>