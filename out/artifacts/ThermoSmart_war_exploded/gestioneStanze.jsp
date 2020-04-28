<%@page import="model.mo.Stanza"%>
<%@page session="false"%>
<%@page import="model.session.mo.LoggedUser"%>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  LoggedUser loggedUser = (LoggedUser) request.getAttribute("loggedUser");
  Stanza[] stanze = (Stanza[]) request.getAttribute("stanze");
  String appMsg = (String) request.getAttribute("appMsg");
  int size = 0;
  if(stanze!=null) size = stanze.length;
%>

<!DOCTYPE HTML>
<html lang="it-IT">
    <head>
        <meta charset="UTF-8">
        <%@include file="headheader.inc"%>
        <title>Gestione Stanze</title>
        <script>
            function elimina(el) {
                document.deleteStanza.id.value = el;
                document.deleteStanza.submit();
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
                            <h3>Qui puoi gestire le tue stanze </h3>
                            <hr>
                            <div class="row uniform 50%">
                                <%if(appMsg != null && appMsg.length()>1){ %>
                                <div class="12u 12u(mobilep)">
                                    <h3 style="color: red"><%out.print(appMsg); %></h3>
                                </div>
                                <%} %>
                                <div class="4u 12u(mobilep)">
                                    <h3>Le tue stanze</h3>
                                </div>
                                <div class="4u 12u(mobilep)">
                                    <a class="button" href="Connector?ca=TermoManagement.insertNewView">Crea una nuova stanza</a>
                                </div>
                                <div class="4u 12u(mobilep)">
                                    <a href='/Connector' class="button special" >Clicca qui per tornare alla home</a>
                                </div>
                                <div class="12u 12u(mobilep)">
                                    <br>
                                </div>
                                <% if (size<=0){ %>
                                <div class="12u 12u(mobilep)">
                                    <h3>Ancora non &egrave; presente alcuna stanza. Inizia creandone una nuova.</h3>
                                </div>
                                <% }else{
                                    for(Stanza stanza : stanze){ %>
                                <div class="6u 12u(mobilep)">
                                    <a href="Connector?ca=TermoManagement.viewRoom&id=<%=stanza.getId()%>"><h3><b>Stanza:</b> <%=stanza.getNome()%></h3></a>
                                </div>
                                <div class="1u 12u(mobilep)">
                                    <a onclick="elimina(<%=stanza.getId()%>)" title="Elimina Stanza"><i class="far fa-trash-alt fa-3x"></i></a>
                                </div>
                                <div class="5u 12u(mobilep)">
                                    <form name="EditStanza" method="post" action="Connector">
                                        <input type="hidden" name="codProd" value="<%=stanza.getId()%>"/>
                                        <input type="hidden" name="ca" value="TermoManagement.editView"/>
                                        <input type="submit" value="Modifica <%if(stanza.getNome().length()>15){out.println(stanza.getNome().substring(0, 15)+"...");}else{out.println(stanza.getNome());}%>"/>
                                    </form>
                                </div>
                                <div class="12u 12u(mobilep)">
                                    <br>
                                </div>
                                <%} }%>
                            </div>
                        </section>
                    </div>
                </div>
            </section>
        </div>
        <form name="nuovaStanza" action="Connector" method="post">
            <input type="hidden" name="ca" value="TermoManagement.insertNewView"/>
        </form>
        <form name="deleteStanza" action="Connector" method="post">
            <input type="hidden" name="ca" value="TermoManagement.delete"/>
            <input type="hidden" name="id"/>
        </form>
        <%@include file="bottomjs.inc"%>
    </body>
</html>