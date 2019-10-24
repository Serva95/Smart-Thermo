<%@page import="model.mo.Stanza"%>
<%@page session="false"%>
<%@page import="model.session.mo.LoggedUser"%>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  LoggedUser loggedUser = (LoggedUser) request.getAttribute("loggedUser");
  Stanza[] stanza = (Stanza[]) request.getAttribute("stanza");
  int size = 0;
  if(stanza!=null) size = stanza.length;
%>

<!DOCTYPE HTML>
<html lang="it-IT">
    <head>
        <meta charset="UTF-8">
        <%@include file="headheader.inc"%>
        <title>Gestione Stanze</title>
        <script>
            function elimina(el) {
                document.deleteStanza.codice.value = el;
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
                                <div class="4u 12u(mobilep)">
                                    <h3>Le tue stanze</h3>
                                </div>
                                <div class="4u 12u(mobilep)">
                                    <a class="button" href="javascript:nuovaStanza.submit()">Crea una nuova stanza</a>
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
                                    for(int i=0; i<size; i++){ %>
                                <div class="6u 12u(mobilep)">
                                    <h3><b>Nome:</b> <%=stanza[i].getNome()%></h3>
                                </div>
                                <div class="1u 12u(mobilep)">
                                    <a onclick="elimina(<%=stanza[i].getId()%>)" title="Elimina Stanza"><i class="far fa-trash-alt fa-3x"></i></a>
                                </div>
                                <div class="5u 12u(mobilep)">
                                    <form name="EditProd" method="post" action="Dispatcher">
                                        <input type="hidden" name="codProd" value="<%=stanza[i].getId()%>"/>
                                        <input type="hidden" name="ca" value="TermoManagement.editView"/>
                                        <input type="submit" value="Modifica <%if(stanza[i].getNome().length()>15){out.println(stanza[i].getNome().substring(0, 15)+"...");}else{out.println(stanza[i].getNome());}%>"/>
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