<%@page session="false"%>
<%@page import="model.session.mo.LoggedUser"%>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  LoggedUser loggedUser = (LoggedUser) request.getAttribute("loggedUser");
  String applicationMessage = (String) request.getAttribute("applicationMessage");
%>

<!DOCTYPE HTML>
<html>
    <head>
        <title>Thermo Pi home page</title>
        <%@include file="headheader.inc"%>
    </head>
    <body class="landing">
        <div id="page-wrapper">
            <%@include file="header.inc"%>
            <section id="banner">
                <h2>Thermo Pi</h2>
                <p>Home of your Smart Thermo Pi</p>
                <h3 style="font-weight: bold; text-shadow: -1px 0 black, 0 1px black, 1px 0 black, 0 -1px black;">Login to see more details</h3>
                
            </section>
                
            <!-- Main -->
            <section id="main" class="container">
                
                <section class="box special">
                    <header class="major">
                        <h2>This is you smart Thermo Pi administration panel</h2>
                        <p>Here you can manage whatever you need remotely in your thermo pi</p>
                    </header>
                </section>
                
                <footer id="footer">
                    <ul class="copyright">
                        <li>&copy; Serva. All rights reserved.</li>
                        <li><a href="javascript:LoginAdmin.submit()">META - Login</a></li>
                        <li><a href="javascript:trytmp.submit()">View actual temp</a></li>
                    </ul>
                </footer>
                <form method="post" action="Connector" autocomplete="on" name="LoginAdmin">
                    <input type="hidden" name="ca" value="LoginManagement.adminLoginView"/>
                </form>
                <form method="post" action="Connector" autocomplete="on" name="trytmp">
                    <input type="hidden" name="ca" value="HomeManagement.gettempview"/>
                </form>
            </section>
        </div>
        <%@include file="bottomjs.inc"%>
    </body>
</html>