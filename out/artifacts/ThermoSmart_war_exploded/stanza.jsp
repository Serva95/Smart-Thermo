<%@page session="false"%>
<%@page import="model.session.mo.LoggedUser"%>
<%@ page import="model.mo.Stanza" %>
<%@ page import="java.time.LocalTime" %>
<%@ page import="java.time.DayOfWeek" %>
<%@ page import="java.time.format.TextStyle" %>
<%@ page import="java.util.Locale" %>

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
    <title>Dettagli stanza <%=stanza.getNome()%></title>
</head>
<body>
<div id="page-wrapper">
    <%@include file="header.inc"%>
    <section id="main" class="container">
        <div class="row">
            <div class="12u">
                <section class="box">
                    <div class="row uniform 50%">
                        <div class="8u 8u(mobilep)">
                            <h3>Questa &egrave; la stanza: <%=stanza.getNome()%></h3>
                        </div>
                        <div class="4u 4u(mobilep)">
                            <a href="/Connector?ca=TermoManagement.editRoom&id=<%=stanza.getId()%>" class="button">Modifica <%if(stanza.getNome().length()>15){out.println(stanza.getNome().substring(0, 15)+"...");}else{out.println(stanza.getNome());}%></a>
                        </div>
                        <div class="12u 12u(mobilep)">
                            <h4>Temperatura massima impostata per la stanza: <%=stanza.getMaxTemp()%>&deg;C</h4>
                        </div>
                        <div class="12u 12u(mobilep)">
                            <h4>Temperatura minima impostata per la stanza: <%=stanza.getMinTemp()%>&deg;C</h4>
                        </div>
                        <div class="12u 12u(mobilep)">
                            <h4>Temperatura minima assoluta impostata per la stanza: <%=stanza.getAbsoluteMin()%>&deg;C</h4>
                        </div>
                        <div class="12u 12u(mobilep)">
                            <hr>
                        </div>
                        <div class="12u 12u(mobilep)">
                            <h3>Impostazione di accesione e spegnimento della settimana:</h3>
                        </div>
                        <%LocalTime[][] giorniOnOff = stanza.getTurnOnOffTimes();
                            for(int i=0; i<7; i++) {%>
                        <div class="12u 12u(mobilep)">
                            <h3><b><%=DayOfWeek.of(i+1).getDisplayName(TextStyle.FULL, Locale.ITALIAN)%></b></h3>
                        </div>
                        <%for(int j=0; j<6; j++) {
                            if (giorniOnOff[i][j] != null) { %>
                        <div class="1u 3u(mobilep)">
                            <%switch(j){
                                case 0: %>
                            <h4><b>Fascia 1</b></h4>
                            <%break;
                                case 2:%>
                            <h4><b>Fascia 2</b></h4>
                            <%break;
                                case 4:%>
                            <h4><b>Fascia 3</b></h4>
                            <%break;
                                default:
                            }%>
                        </div>
                        <div class="3u 9u(mobilep)">
                            <h4>On: <%=giorniOnOff[i][j]%> <%j++;%>- Off: <%=giorniOnOff[i][j]%></h4>
                        </div>
                        <%    }
                        }
                            out.print("<br>");
                        }%>
                    </div>
                </section>
            </div>
        </div>
    </section>
</div>
<%@include file="bottomjs.inc"%>
</body>
</html>
