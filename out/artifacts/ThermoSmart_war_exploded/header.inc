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
            </li>
            <li>
                <form name="profileView" action="Login" method="post">
                    <input type="hidden" name="ca" value="UtenteManagement.profileView"/>
                </form>
            </li>
            <li>
                <a href="#" class="icon fa-angle-down">Welcome <%=loggedUser.getUsername()%>!</a>
                <ul>
                    <li><a href="javascript:profileView.submit()">Profilo</a><li/>
                    <li><a href="javascript:logoutForm.submit()">Logout</a></li>
                </ul>
            </li>
            <%} else {%>
                <li><a href="javascript:loginForm.submit()" class="button">Login/Register</a></li>
            <%}%>
            <li>
                <a href="Connector?ca=AiutoManagement.view" class="button">Help</a>
            </li>
        </ul>
    </nav>
</header>