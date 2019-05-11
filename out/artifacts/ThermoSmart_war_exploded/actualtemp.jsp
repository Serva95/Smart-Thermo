<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="model.mo.Lettura"%>
<%@page session="false"%>
<%@page import="model.session.mo.LoggedUser"%>

<%
  boolean loggedOn = (Boolean) request.getAttribute("loggedOn");
  LoggedUser loggedUser = (LoggedUser) request.getAttribute("loggedUser");
  String applicationMessage = (String) request.getAttribute("applicationMessage");
  Lettura[] days = (Lettura[]) request.getAttribute("days");
  Lettura[] meds = (Lettura[]) request.getAttribute("meds");
%>
<%@page session="false"%>
<!DOCTYPE HTML>
<html lang="it-IT">
    <head>
        <meta charset="UTF-8">
        <%@include file="headheader.inc"%>
        <title>Temps of the day</title>
        <script src="assets/js/chartjs-2.8.0/Chart.min.js"></script>
        <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
    </head>
    <body>
        <div id="page-wrapper">
            <%@include file="header.inc"%>
            <section id="main" class="container">
                <div class="row">
                    <div class="12u">
                        <section class="box">
                            <div id="ls">Loading...</div>
                            </br>
                            <!--<div style="width:30em">-->
                            <div class="chart-container tempgraph">
                                <canvas id="tempChart"></canvas>
                            </div>
                            <br>
                            <br>
                            <div class="chart-container tempgraph">
                                <canvas id="humChart"></canvas>
                            </div>
                            <br>
                            <br>
                            <div class="9u 12u(mobilep)">
                                <h3>Select the number of data you want to see for the mediums</h3>
                            </div>
                            <div class="3u 12u(mobilep)">
                                <select id="datanumber" onchange="updatedata(this.value)">
                                    <option value="7">7
                                    <option value="15">15
                                    <option value="30">30
                                    <option value="60">60
                                </select>
                            </div>
                            <br>
                            <div class="chart-container tempgraph">
                                <canvas id="medhumChart"></canvas>
                            </div>
                            <br>
                            <br>
                            <div class="chart-container tempgraph">
                                <canvas id="medtempChart"></canvas>
                            </div>
                            <br>
                            <h3><a href='/Connector'>Clicca qui per tornare alla home</a></h3>
                        </section>
                    </div>
                </div>
            </section>
        </div>
        <script>
            function updateReal(){
                $.post('Connector', {livesearch: "gettemp"}, function(data) {
                    document.getElementById("ls").innerHTML="<h2><b>TEMP:</b> "+data+"</h2>";
                });
                setTimeout( updateReal, 180000 );
                //180 -> 480 lectures per day - once every 3 minutes
            }
            function updateTemps(){
                $.post('Connector', {livesearch: "updateTemps", dati:tempChart.data.labels[tempChart.data.labels.length - 1]}, function(data) {
                    if(data.length>5 && data.length<35){
                        //21.5?01:10:18?55.9
                        //0-4  5-8     14-4
                        //for temp
                        tempChart.data.labels.push(data.substr(5,8));
                        tempChart.data.datasets.forEach((dataset) => {
                            dataset.data.push(data.substr(0,4));
                        });
                        tempChart.data.labels.shift();
                        tempChart.data.datasets.forEach((dataset) => {
                            dataset.data.shift();
                        });
                        tempChart.update();
                        //for hum
                        humChart.data.labels.push(data.substr(5,8));
                        humChart.data.datasets.forEach((dataset) => {
                            dataset.data.push(data.substr(14,4));
                        });
                        humChart.data.labels.shift();
                        humChart.data.datasets.forEach((dataset) => {
                            dataset.data.shift();
                        });
                        humChart.update();
                    }
                });
                setTimeout( updateTemps, 840000 );
                //setTimeout( updateTemps, 500 );
            }
            function startUpdate(){
                updateReal();
                setTimeout( updateTemps, 5000 );
            }
            window.addEventListener("load", startUpdate);
            
            var ctx = document.getElementById("tempChart").getContext("2d");
            var cth = document.getElementById("humChart").getContext("2d");
            var humChart = new Chart(cth, {
                type:'line',
                data: {
                    labels: [<%for(Lettura outread : days){out.print("'" + outread.getReadingdatetime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "'" + ",");}%>],
                    datasets: [{label: 'Humidity %',
                            data: [<%for(Lettura outread : days){out.print(String.valueOf(outread.getHum()) + ",");}%>],
                            fill:!1, borderColor:"#5f021f", lineTension:0.1}]
                },options:{responsive:!0,maintainAspectRatio:!1,aspectRatio:1,scales:{yAxes:[{ticks:{beginAtZero:!1}}]}}
            });
            var tempChart = new Chart(ctx, {
                type:'line',
                data: {
                    labels: [<%for(Lettura outread : days){out.print("'" + outread.getReadingdatetime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "'" + ",");}%>],
                    datasets: [{label: 'Temperatures �C',
                            data: [<%for(Lettura outread : days){out.print(String.valueOf(outread.getTemp()) + ",");}%>],
                            fill:!1, borderColor:"#3cb371", lineTension:0.1}]
                },options:{responsive:!0,maintainAspectRatio:!1,aspectRatio:1,scales: {yAxes: [{ticks: { beginAtZero:!1}}]}}
            });
            
            function updatedata(value){
                //$.post('Connector', {livesearch: "getmeds"}, function(data) {
                //document.getElementById("ls").innerHTML="<h2>Actual temp and hum - TEMP: "+data+"</h2>";
                //});
            }
            
            var labelsin = [<%for(Lettura outmedrd : meds){out.print("'" + outmedrd.getReadingdatetime().toLocalDate().toString() + "'" + ",");}%>];
            var medtmpsin = [<%for(Lettura outmedrd : meds){out.print(String.valueOf(outmedrd.getTemp()) + ",");}%>];
            var medhumsin = [<%for(Lettura outmedrd : meds){out.print(String.valueOf(outmedrd.getHum()) + ",");}%>];
            var ctmedh = document.getElementById("medhumChart").getContext("2d");
            var ctmedt = document.getElementById("medtempChart").getContext("2d");
            var medhumChart = new Chart(ctmedh, {
                type:'line',
                data:{labels:labelsin,datasets:[{label:'Medium Humidity %',data:medhumsin,fill:!1,borderColor:"#963c3c",lineTension:0.1}]},
                options:{responsive:!0,maintainAspectRatio:!1,aspectRatio:1,scales:{yAxes:[{ticks:{beginAtZero:!1}}]}}
            });
            var medtempChart = new Chart(ctmedt, {
                type:'line',
                data:{labels:labelsin,datasets:[{label:'Medium Temps �C',data:medtmpsin,fill:!1,borderColor:"#216bff",lineTension:0.1}]},
                options:{responsive:!0,maintainAspectRatio:!1,aspectRatio:1,scales:{yAxes:[{ticks:{beginAtZero:!1}}]}}
            });
        </script>
        <%@include file="bottomjs.inc"%>
    </body>
</html>