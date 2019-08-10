<%@page session="false"%>
<%@page import="model.mo.Utente"%>
<%@page import="model.session.mo.LoggedUser"%>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  LoggedUser loggedUser = (LoggedUser) request.getAttribute("loggedUser");
  Utente user = (Utente) request.getAttribute("utente");
  String applicationMessage = (String) request.getAttribute("applicationMessage");
%>

<!DOCTYPE HTML>
<html>
    <head>
        <title>Il tuo profilo</title>
        <%@include file="headheader.inc"%>
        <script>
            function compare(){
                var repwd = document.getElementById("repwd").value;
                var pwd = document.getElementById("pwd").value;
                var rs = repwd.localeCompare(pwd);
                var btn = document.getElementById("inviadati");
                if(rs===0){
                    btn.disabled = "";
                    btn.value = "Conferma Dati";
                }else{
                    btn.value = "password e ripeti password sono diversi";
                    btn.disabled = "disabled";
                }
            }
        </script>
    </head>
    <body>
        <div id="page-wrapper">
            <%@include file="header.inc"%>
            <section id="main" class="container">
                <div class="row">
                    <div class="12u">
                        <section class="box_less_space">
                            <header>
                                <h3>Modifica Profilo</h3>
                            </header>
                            <form method="post" action="Dispatcher" autocomplete="on" name="updateRegister" enctype="multipart/form-data" accept-charset="ISO-8859-1">
                                <input type="hidden" name="ca" value="UtenteManagement.update"/>
                                <input type="hidden" name="cod" value="<%=user.getCodice()%>"/>
                                <div class="row uniform 50%">
                                    <div class="6u 12u(mobilep)">
                                        <input type="text" name="username" id="username" value="<%=user.getUsername()%>" placeholder="Username per recensioni" maxlength="30" required />
                                    </div>
                                    <div class="6u 12u(mobilep)">
                                        <input type="email" name="email" id="email" value="<%=user.getEmail()%>" placeholder="Email" maxlength="50" required />
                                    </div>
                                    <div class="6u 12u(mobilep)">
                                        <input type="password" name="pwd" id="pwd" value="" minlength="8" maxlength="30" placeholder="Password" onkeyup="compare()"/>
                                    </div>    
                                    <div class="6u 12u(mobilep)">
                                        <input type="password" name="repwd" id="repwd" value="" minlength="8" maxlength="30" placeholder="Ripeti password" onkeyup="compare()"/>
                                    </div>
                                    <div class="row uniform">
                                        <div class="12u">
                                            <ul class="actions">
                                                <li><input type="submit" value="Conferma Dati" id="inviadati"/></li>
                                                <li><input type="reset" value="Reset" class="alt" /></li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </section>
                    </div>
                </div>
                <%@include file="footer.inc"%>
            </section>
        </div>
        <%@include file="bottomjs.inc"%>
    </body>
</html>