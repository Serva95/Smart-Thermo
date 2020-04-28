<header id="header">
    <h1><a href="Connector"><img src="images/logo.png" width="185px"/></a></h1>
    <nav id="nav">
        <ul>
            <li><a href="Connector">Home</a></li>
            <%if (loggedOn) {%>
            <li>
                <form name="logoutForm" action="Login" method="post">
                    <input type="hidden" name="ca" value="LoginManagement.logoutUser"/>
                </form>
                <form name="getTemp" action="Connector" method="post" >
                    <input type="hidden" name="ca" value="HomeManagement.gettempview"/>
                </form>
                <form name="profileView" action="Connector" method="post">
                    <input type="hidden" name="ca" value="UtenteManagement.profileView"/>
                </form>
                <form name="termostato" action="Connector" method="post">
                    <input type="hidden" name="ca" value="TermoManagement.view"/>
                </form>
            </li>
            <li>
                <a href="#" class="icon fa-angle-down">Benvenuto <%=loggedUser.getUsername()%>!</a>
                <ul>
                    <li><a href="Connector?ca=HomeManagement.gettempview">Vedi dati Temperatura</a></li>
                    <li><a href="Connector?ca=TermoManagement.view">Pagina Termostato</a></li>
                    <li><a href="Connector?ca=UtenteManagement.profileView">Profilo</a><li/>
                    <li><a href="javascript:logoutForm.submit()">Logout</a></li>
                </ul>
            </li>
            <%} else {%>
            <li><a href="javascript:loginForm.submit()" class="button">Login</a></li>
            <%}%>
        </ul>
    </nav>
</header>