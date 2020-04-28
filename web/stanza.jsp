<%@page session="false"%>
<%@page import="model.session.mo.LoggedUser"%>
<%@ page import="model.mo.Stanza" %>
<%@ page import="java.time.LocalTime" %>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  LoggedUser loggedUser = (LoggedUser) request.getAttribute("loggedUser");
  Stanza stanza = (Stanza) request.getAttribute("stanza");
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
                          <%=stanza.getId()%>
                          <br>
                          <%=stanza.getNome()%>
                          <br>
                          <%=stanza.getMaxTemp()%>
                          <br>
                          <%=stanza.getMinTemp()%>
                          <br>
                          <%=stanza.getAbsoluteMin()%>
                          <br>
                          <%LocalTime[][] giorniOnOff = stanza.getTurnOnOffTimes();
                              for(int i=0; i<7; i++) {
                                  out.print(giorniOnOff[i][0]+"<br>");
                                  out.print(giorniOnOff[i][1]+"<br>");
                                  out.print(giorniOnOff[i][2]+"<br>");
                                  out.print(giorniOnOff[i][3]+"<br>");
                                  out.print(giorniOnOff[i][4]+"<br>");
                                  out.print(giorniOnOff[i][5]+"<br>");
                          }%>
                          <br>
                          Pagina ancora in costruzione. Torna pi&ugrave; tardi.
                          <h3><a href='/Connector'>Clicca qui per tornare alla home</a></h3>
                      </section>
                  </div>
              </div>
          </section>
      </div>
      <%@include file="bottomjs.inc"%>
  </body>
</html>
