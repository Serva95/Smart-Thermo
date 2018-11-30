<%@page session="false"%>
<%@page import="model.session.mo.LoggedUser"%>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  LoggedUser loggedUser = (LoggedUser) request.getAttribute("loggedUser");
  String email = (String) request.getAttribute("email");
  String applicationMessage = (String) request.getAttribute("applicationMessage");
%>
<%@page session="false"%>
<!DOCTYPE HTML>
<html lang="it-IT">
  <head>
      <meta charset="UTF-8">
      <%@include file="headheader.inc"%>
      <title>Page Redirection</title>      
  </head>
  <body>
      <div id="page-wrapper">
          <%@include file="header.inc"%>
          <section id="main" class="container">
              <div class="row">
                  <div class="12u">
                      <section class="box">
                          <%=applicationMessage%>
                          <h3><a href='/Smart_Thermo/Connector'>Clicca qui per tornare alla home</a></h3>
                      </section>
                  </div>
              </div>
          </section>
      </div>
      <%@include file="bottomjs.inc"%>
  </body>
</html>
