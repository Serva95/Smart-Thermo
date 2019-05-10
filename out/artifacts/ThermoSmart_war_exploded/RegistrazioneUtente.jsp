<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.LocalDateTime"%>
<%@page session="false"%>
<%@page import="model.session.mo.LoggedUser"%>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  LoggedUser loggedUser = (LoggedUser) request.getAttribute("loggedUser");
  String am = (String) request.getAttribute("applicationMessage");
%>
<!DOCTYPE html>
<html>
    <head>
        <title>User Register</title>
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
                        
            function onLoadHandler(){
                var btn = document.getElementById("inviadati");
                //btn.value = "Completa tutti i campi";
                btn.disabled = "disabled";
            }
            
            window.addEventListener("load", onLoadHandler, {capture: true});
        </script>
    </head>
    <body>
        <div id="page-wrapper">
            <%@include file="header.inc"%>
            <section id="main" class="container">
                <div class="row">
                    <div class="12u">
                        <section class="box">
                            <h3>Register</h3>
                            <form method="post" action="Login" autocomplete="on" name="confirmRegister">
                                <input type="hidden" name="ca" value="RegisterManagement.insert"/>
                                <div class="row uniform 50%">
                                    <div class="6u 12u(mobilep)">
                                        <input type="text" name="username" id="username" value="" placeholder="Username" maxlength="30" required />
                                    </div>
                                    <div class="6u 12u(mobilep)">
                                        <input type="email" name="email" id="email" value="" placeholder="Email" maxlength="50" required />
                                    </div>
                                    <div class="6u 12u(mobilep)">
                                        <input type="password" name="pwd" id="pwd" value="" minlength="8" maxlength="30" required placeholder="Password" onkeyup="compare()"/>
                                    </div>    
                                    <div class="6u 12u(mobilep)">
                                        <input type="password" name="repwd" id="repwd" value="" minlength="8" maxlength="30" required placeholder="Repeat password" onkeyup="compare()"/>
                                    </div>
                                    <br>
                                    <div class="12u">
                                        <ul class="actions">
                                            <li><input type="submit" value="Completa tutti i campi" id="inviadati"/></li>
                                            <li><input type="reset" value="Reset" class="alt" /></li>
                                        </ul>
                                    </div>
                                </div>
                            </form>   
                        </section>
                    </div>
                </div>
            </section>
        </div>
        <%@include file="bottomjs.inc"%>
    </body>
</html>