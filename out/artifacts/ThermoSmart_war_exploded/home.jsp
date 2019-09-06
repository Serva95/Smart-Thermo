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
                <h3 style="font-weight: bold; text-shadow: -1px 0 black, 0 1px black, 1px 0 black, 0 -1px black;"><%if(loggedOn){out.print("Benvenuto "+loggedUser.getUsername());}else{out.print("Login to see more details");} %></h3>
                
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
                    </ul>
                </footer>
            </section>
        </div>
        <%@include file="bottomjs.inc"%>
    </body>
</html>