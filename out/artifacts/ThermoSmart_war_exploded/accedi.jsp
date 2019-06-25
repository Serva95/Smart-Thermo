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
        <title>Login</title>
        <%@include file="headheader.inc"%>
    </head>
    <body>
        <div id="page-wrapper">
            <%@include file="header.inc"%>
            <section id="main" class="container 100%">
                <header>
                    <h2>Login</h2>
                    <h3><%if(am!=null){out.println(am);}%></h3>
                </header>
                <div class="row">
                    <div class="2u">&nbsp;</div>
                    <div class="8u 12u(mobilep)">
                        <div class="box">
                            <form method="post" action="Login" name="loginUtente" autocomplete="on">
                                <input type="hidden" name="ca" value="LoginManagement.loginUser"/>
                                <div class="row uniform 50%">
                                    <div class="6u 12u(mobilep)">
                                        <input type="text" name="username" id="username" value="" placeholder="Username" required size="50" />
                                    </div>
                                    <div class="6u 12u(mobilep)">
                                        <input type="password" name="pwd" id="pwd" value="" minlength="8" maxlength="30" required placeholder="Password"/>
                                    </div>
                                    <div class="12u 12u(mobilep)">
                                        <input type="checkbox" name="remain" id="remain" checked>
                                        <label for="remain">Remember me for 7 days</label>
                                    </div> 
                                </div>   
                                <div class="row uniform">
                                    <div class="12u">
                                        <ul class="actions">
                                            <li><input type="submit" value="Login" /></li>
                                            <li><input type="reset" value="Reset" class="alt" /></li>
                                        </ul>
                                    </div>
                                </div>
                            </form>
                            <!--<h3>
                                <a href="javascript:registerForm.submit()" >Need an account ? <b>Click here to register</b></a>
                            </h3>-->
                        </div>
                    </div>
                </div>  
            </section>
        </div>
        <form name="registerForm" method="post" action="Login">
            <input type="hidden" name="ca" value="RegisterManagement.registerView"/>     
        </form>
        <%@include file="bottomjs.inc"%>
    </body>
</html>
