<script language="javascript">

function request(nameRist){
                document.openRistPage.nameRist.value = nameRist.name;
                document.openRistPage.subm.value = nameRist.id;
                document.openRistPage.submit();
            }
</script>

<form name="openRistPage" method="post" action="Dispatcher">
    <input type="hidden" name="nameRist"/>
    <input type="hidden" name="subm"/>
    <input type="hidden" name="ca" value="RistoranteManagement.show"/>     
</form>

<form name="menu" method="post" action="Dispatcher">
    <input type="hidden" name="codice" value="<%=rst.getCodice()%>"/>
    <input type="hidden" name="ca" value="RistoranteManagement.showMenu"/>   
</form>

<form name="back" method="post" action="Dispatcher">
    <input type="hidden" name="address" value="<%=rst.getCity()%>"/>
    <input type="hidden" name="ca" value="RistoranteManagement.search"/>  
</form>

<section class="boxshortlft_top_padded">
    <ul class="alt">
        <li><a onclick="request(this)" name="<%=rst.getCodice()%>" id="info">Info</a></li>
        <li><a href="javascript:document.menu.submit()">Men&ugrave</a></li>
        <li><a onclick="request(this)" name="<%=rst.getCodice()%>" id="rec">Recensioni</a></li>
        <li><a href="javascript:document.back.submit()">Indietro</a></li>
    </ul>
</section>