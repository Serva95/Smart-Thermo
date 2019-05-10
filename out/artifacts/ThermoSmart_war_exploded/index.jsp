<%@page session="false"%>
<!DOCTYPE HTML>
<html lang="it-IT">
  <head>
    <meta charset="UTF-8">
    <%@include file="headheader.inc"%>
    <meta http-equiv="refresh" content="0; url=/Connector">
    <script type="text/javascript">
      function onLoadHandler() {
        window.location.href = "/Connector";
        //window.location.href = "/Smart_Thermo/Connector";
      }
      window.addEventListener("load", onLoadHandler);
    </script>
    <title>Page Redirection</title>
  </head>
  <body>
    If you are not redirected automatically, follow the <a href='/Connector'>link</a>
  </body>
</html>