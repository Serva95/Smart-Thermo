<%@page session="false"%>
<%@page import="model.session.mo.LoggedUser"%>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  LoggedUser loggedUser = (LoggedUser) request.getAttribute("loggedUser");
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Registrazione Stanza</title>
        <%@include file="headheader.inc"%>
    </head>
    <body>
        <div id="page-wrapper">
            <%@include file="header.inc"%>
            <section id="main" class="container">
                <div class="row">
                    <div class="12u">
                        <section class="box">
                            <h3>Registrazione Nuova Stanza</h3>
                            <form method="post" action="Connector" autocomplete="on" name="confirm">
                                <input type="hidden" name="ca" value="ThermoManagement.insert"/>
                                <div class="row uniform 50%">
                                    <div class="6u 12u(mobilep)">
                                        <input type="text" name="name" id="name" value="" placeholder="Nome della stanza" maxlength="45" required pattern="[A-Za-z0-9 ]+" title="Solo lettere o numeri"/>
                                    </div>
                                    <div class="3u 12u(mobilep)">
                                        <input type="number" name="tempMax" id="tempMax" value="" placeholder="Massima Temperatura" min="0" max="99" step="0.1" required/>
                                    </div>
                                    <div class="3u 12u(mobilep)">
                                        <input type="number" name="tempMin" id="tempMin" value="" placeholder="Minima Temperatura" min="0" max="99" step="0.1" required/>
                                    </div>
                                    
                                    <div class="3u 12u(narrower)">
                                        <input type="checkbox" id="underOK" name="underOK" checked>
                                        <label for="underOK">Consentito ai minori di 18 anni</label>
                                    </div>
                                        
                                    <div class="6u 12u(mobilep)">
                                        <br>
                                        <select name="categoria" id="categoria" required>
                                            <option value="">Scegli la categoria a cui appartiene il prodotto</option>
                                            <option value="contorno">Contorno</option>
                                            <option value="dolce">Dolce</option>
                                            <option value="insalata">Insalata</option>
                                        </select>
                                    </div>
                                    <div class="6u 12u(mobilep)">
                                        <br>
                                        <input type="checkbox" id="aggiunte" name="aggiunte">
                                        <label for="aggiunte">Al prodotto possono esse fatte delle aggiunte ? (per esempio doppia mozzarella sulla pizza - prosciutto nella piadina)</label>
                                    </div>
                                    <div class="12u 12u(mobilep)">
                                        <h4>Per aggiungere degli ingredienti prima cercali, se compaiono a destra clicca sul loro nome per aggiungerli, se non compaiono gli ingredienti da te desiderati clicca sul bottone inserisci</h4>
                                    </div>
                                    
                                        <div class="6u 12u(mobilep)">
                                            <input type="text" name="cerca" id="cerca" onkeyup="StartSearch(this)" placeholder="Ingredienti del prodotto" maxlength="25"/>
                                        </div>
                                        <div class="2u 12u(mobilep)">
                                            <a class="button" onclick="manualadd()">Inserisci</a>
                                        </div>
                                        <div class="4u 12u(mobilep)">
                                            <div id="ls"></div>
                                        </div>
                                    <div class="8u 12u(mobilep)">
                                        <h3>Ingredienti:</h3>
                                        <div id="li"></div>
                                        <input type="hidden" name="ingredienti" value="" id="ingredienti"/>
                                    </div>
                                    <div class="row uniform 50%">
                                        <div class="12u 12u(mobilep)">
                                            <h3>Allergeni del prodotto</h3>
                                        </div>
                                        <div class="2u 12u(narrower)">
                                            <input type="checkbox" id="1" name="1">
                                            <label for="1">Glutine</label>
                                        </div>
                                    </div>
                                    <div class="12u">
                                        <br>
                                        <textarea name="descr" id="descr" placeholder="Inserisci una breve descrizione del prodotto" rows="6" maxlength="450" required></textarea>
                                    </div>
                                    
                                    <div class="6u 12u(mobilep)">
                                        <label for="prodpic">Scegli se vuoi l'immagine del prodotto. Potrai aggiungerla in seguito se non ne hai una ora</label>
                                    </div>
                                    <div class="6u 12u(mobilep)">
                                        <input type="file" name="prodpic" id="prodpic" accept="image/*"/>
                                    </div>
                                    <div class="row uniform">
                                        <div class="12u">
                                            <ul class="actions">
                                                <li><input type="submit" value="Conferma inserimento" id="inviadati"/></li>
                                                <li><input type="reset" value="Reset" class="alt" /></li>
                                                <li><a class="button special" href="javascript:annulla.submit()">Annulla</a></li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </form>   
                        </section>
                    </div>
                </div>
            </section>
        </div>
        <form name="annulla" action="Connector" method="post">
            <input type="hidden" name="ca" value="TermoManagement.view"/>
        </form>
        <%@include file="bottomjs.inc"%>
    </body>
</html>