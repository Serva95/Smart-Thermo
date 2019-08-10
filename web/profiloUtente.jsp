<%@page session="false"%>
<%@page import="model.mo.Utente"%>
<%@page import="model.session.mo.LoggedUser"%>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  LoggedUser loggedUser = (LoggedUser) request.getAttribute("loggedUser");
  Utente user = (Utente) request.getAttribute("utente");
%>

<!DOCTYPE HTML>
<html>
    <head>
        <title>Il tuo profilo</title>
        <%@include file="headheader.inc"%>
    </head>
    <body>
        <div id="page-wrapper">
            <%@include file="header.inc"%>
            <section id="main" class="container">
                <div class="row">
                    <div class="12u">
                        <section class="box">
                            <header>
                                <h3>Questa &egrave; la tua pagina <%=user.getUsername()%></h3>
                            </header>
                            <h4>Email: <%=user.getEmail()%></h4>
                            <h4>Username: <%=user.getUsername()%></h4>
                            <br>
                            <a href="javascript:editProfile.submit()" class="button">Modifica Profilo</a>
                            <br><br><br>
                            <a href="javascript:deleteProfile.submit()" class="button special">ELIMINA TOTALMENTE il profilo</a>
                            <br>
                            Attenzione l'azione di eliminazione &egrave; <b>IRREVERSIBILE</b>
                        </section>
                    </div>
                </div>
                <%@include file="footer.inc"%>
            </section>
        </div>
        <form name="editProfile" action="Dispatcher" method="post">
            <input type="hidden" name="ca" value="UtenteManagement.profileEdit"/>
        </form>
        <form name="deleteProfile" action="Dispatcher" method="post">
            <input type="hidden" name="ca" value="UtenteManagement.delete"/>
        </form>
        <%@include file="bottomjs.inc"%>
    </body>
</html>