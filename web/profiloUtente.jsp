<%@page session="false"%>
<%@page import="model.mo.Utente"%>
<%@page import="model.session.mo.LoggedUser"%>
<%@page import="model.mo.Sessione" %>

<%
    boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
    LoggedUser loggedUser = (LoggedUser) request.getAttribute("loggedUser");
    Utente user = (Utente) request.getAttribute("utente");
    Sessione sessioni[] = (Sessione[]) request.getAttribute("sessioni");
%>

<!DOCTYPE HTML>
<html>
<head>
    <title>Il tuo profilo</title>
    <%@include file="headheader.inc"%>
    <script>
        function elimina(v) {
            document.deleteSession.codice.value = v;
            document.deleteSession.submit();
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
                        <div class="12u 12u(mobilep)">
                            <h3>Questa &egrave; la tua pagina <%=user.getUsername()%></h3>
                        </div>
                        <div class="12u 12u(mobilep)">
                            <h4>Email: <%=user.getEmail()%></h4>
                        </div>
                        <div class="12u 12u(mobilep)">
                            <h4>Username: <%=user.getUsername()%></h4>
                        </div>
                        <div class="12u 12u(mobilep)">
                            <h4>Codice identificativo utente: <%=user.getCodice()%></h4>
                        </div>
                        <div class="12u 12u(mobilep)">
                            <h4>Inizio hash univoco sessione attuale: <%=loggedUser.getUniqid().substring(0,7)%></h4>
                        </div>
                        <div class="12u 12u(mobilep)">
                            <a href="javascript:editProfile.submit()" class="button">Modifica Profilo</a>
                        </div>
                        <div class="12u 12u(mobilep)">
                            <hr>
                        </div>
                        <div class="12u 12u(mobilep)">
                            <h3>Queste sono le tue sessioni attive</h3>
                        </div>
                    </div>
                    <% for (Sessione sessione : sessioni) { %>
                        <div class="row uniform 50%">
                            <div class="3u 12u(mobilep)">
                                <h4>Iniziata il: <%=sessione.getLoginDate()%></h4>
                            </div>
                            <div class="3u 12u(mobilep)">
                                <h4>Terminer&agrave; il: <%=sessione.getRememberUntil()%></h4>
                            </div>
                            <div class="3u 12u(mobilep)">
                                <h4>Inizio hash univoco: <%=sessione.getSessionId().substring(0,7)%></h4>
                            </div>
                            <%if(!sessione.getSessionId().equalsIgnoreCase(loggedUser.getUniqid())){%>
                            <form ic-post-to="Connector" class="3u 12u(mobilep)">
                                <input type="hidden" name="customClass" value="UtenteManagement"/>
                                <input type="hidden" name="js" value="yes"/>
                                <input type="hidden" name="livesearch" value="deleteSession"/>
                                <input type="hidden" name="codice" value="<%=sessione.getSessionId()%>"/>
                                <input type="submit" class="button" value="Termina Sessione"/>
                            </form>
                            <%}else{%>
                            <div class="3u 12u(mobilep)">
                                <a onclick="elimina('<%=sessione.getSessionId()%>')" class="button">Termina Sessione</a>
                            </div>
                            <%}%>
                            <div class="12u 12u(mobilep)">
                                <hr>
                            </div>
                        </div>
                        <%}%>
                    <div class="row uniform 50%">
                        <div class="12u 12u(mobilep)">
                            Attenzione l'azione di eliminazione &egrave; <b>IRREVERSIBILE</b>
                        </div>
                        <div class="12u 12u(mobilep)">
                            <a href="javascript:deleteProfile.submit()" class="button special" disabled>ELIMINA TOTALMENTE il profilo</a>
                        </div>
                    </div>
                </section>
            </div>
        </div>
        <%@include file="footer.inc"%>
    </section>
</div>
<form name="editProfile" action="Connector" method="post">
    <input type="hidden" name="ca" value="UtenteManagement.profileEdit"/>
</form>
<form name="deleteProfile" action="Connector" method="post">
    <input type="hidden" name="ca" value="UtenteManagement.delete"/>
</form>
<form name="deleteSession" action="Connector" method="post">
    <input type="hidden" name="codice" value=""/>
    <input type="hidden" name="ca" value="UtenteManagement.deleteSession"/>
</form>
<%@include file="bottomjs.inc"%>
</body>
</html>