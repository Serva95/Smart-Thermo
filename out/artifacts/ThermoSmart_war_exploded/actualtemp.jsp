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
</head>
<body>
<div id="page-wrapper">
    <%@include file="header.inc"%>
    <section id="main" class="container">
        <div class="row">
            <div class="12u">
                <section class="box">
                    <div class="row uniform 50%">
                        <div id="ls" class="12u 12u(mobilep)">Loading...</div>
                        <div class="12u 12u(mobilep)"><br></div>
                        <!--<div style="width:30em">-->
                        <div class="chart-container oneLineGraph">
                            <canvas id="tempChart"></canvas>
                        </div>
                        <div class="12u 12u(mobilep)"><br></div>
                        <div class="chart-container oneLineGraph">
                            <canvas id="humChart"></canvas>
                        </div>
                        <div class="12u 12u(mobilep)"><hr></div>
                        <div class="8u 12u(mobilep)">
                            <h3>Scegli il numero di dati che vuoi vedere per le medie</h3>
                        </div>
                        <div class="4u 12u(mobilep)">
                            <select id="datanumber" onchange="updatedata(this.value)">
                                <option value="7">7
                                <option value="15">15
                                <option value="30">30
                                <option value="60">60
                                <option value="100">100
                            </select>
                        </div>
                        <div class="12u 12u(mobilep)"><br></div>
                        <div class="chart-container oneLineGraph">
                            <canvas id="medhumChart"></canvas>
                        </div>
                        <div class="12u 12u(mobilep)"><br></div>
                        <div class="chart-container oneLineGraph">
                            <canvas id="medtempChart"></canvas>
                        </div>
                        <div class="12u 12u(mobilep)"><br></div>
                        <h3><a href='${pageContext.request.contextPath}/'>Clicca qui per tornare alla home</a></h3>
                    </div>
                </section>
            </div>
        </div>
    </section>
</div>
        <script>
            function updateReal(){
                $.post('Connector', {livesearch: "gettemp"}, function(data) {
                    $("#ls").html("<h2><b>TEMP:</b> "+data+"</h2>");
                });
                setTimeout( updateReal, 300000 );
                //300 -> 288 lectures per day - once every 5 minutes
            }
            function updateTemps(){
                $.post('Connector', {livesearch: "updateTemps", dati:tempChart.data.labels[tempChart.data.labels.length - 1]}, function(data) {
                    if(data.length>5 && data.length<35){
                        //output from the post call
                        //21.5?01:10:18?55.9
                        //0-4  5-8     14-4
                        //for temp
                        tempChart.data.labels.push(data.substr(5,8));
                        tempChart.data.datasets[0].data.push(data.substr(0,4));

                        if(tempChart.data.labels.length >= 100) {
                            tempChart.data.labels.shift();
                            tempChart.data.datasets[0].data.shift();
                        }
                        tempChart.update();
                        //for hum
                        humChart.data.labels.push(data.substr(5,8));
                        humChart.data.datasets[0].data.push(data.substr(14,4));

                        if(humChart.data.labels.length >= 100){
                            humChart.data.labels.shift();
                            humChart.data.datasets[0].data.shift();
                        }
                        humChart.update();
                    }
                });
                setTimeout( updateTemps, 840000 );
                // un update ogni 14 minuti
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
                    datasets: [{label: 'Temperatures \u00B0C',
                            data: [<%for(Lettura outread : days){out.print(String.valueOf(outread.getTemp()) + ",");}%>],
                            fill:!1, borderColor:"#3cb371", lineTension:0.1}]
                },options:{responsive:!0,maintainAspectRatio:!1,aspectRatio:1,scales: {yAxes: [{ticks: { beginAtZero:!1}}]}}
            });

            var labelsin = [<%for(Lettura outmedrd : meds){out.print("'" + outmedrd.getReadingDate().toString() + "'" + ",");}%>];
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
                data:{labels:labelsin,datasets:[{label:'Medium Temps \u00B0C',data:medtmpsin,fill:!1,borderColor:"#216bff",lineTension:0.1}]},
                options:{responsive:!0,maintainAspectRatio:!1,aspectRatio:1,scales:{yAxes:[{ticks:{beginAtZero:!1}}]}}
            });

            function updatedata(numberOfValues){
                $.post('Connector', {livesearch: "getmeds", number: numberOfValues}, function(data) {
                    if(data.length>15){
                        var datesN = data.search("#");
                        var dates = data.slice(0, datesN);
                        data = data.slice(datesN+1, data.length);
                        var tempsN = data.search("#");
                        var temps = data.slice(0, tempsN);
                        data = data.slice(tempsN+1, data.length);
                        var hums = data;

                        var valueNumber = medhumChart.data.labels.length;
                        var i;
                        for(i=0; i<valueNumber; i++) {
                            medhumChart.data.labels.pop();
                            medhumChart.data.datasets[0].data.pop();

                            medtempChart.data.labels.pop();
                            medtempChart.data.datasets[0].data.pop();
                        }

                        for(i=0; i<numberOfValues; i++) {
                            var tmpN = dates.search(",");
                            var tmpDates = dates.slice(0, tmpN);
                            medhumChart.data.labels.push(tmpDates);
                            //update only once because the var with labels is the same for the 2 meds graphs
                            //so update both will result in duplicate labels for the dates
                            dates = dates.slice(tmpN+1, dates.length);

                            tmpN = temps.search(",");
                            var tmpTemps = temps.slice(0, tmpN);
                            medtempChart.data.datasets[0].data.push(tmpTemps);

                            temps = temps.slice(tmpN+1, temps.length);

                            tmpN = hums.search(",");
                            var tmpHums = hums.slice(0, tmpN);
                            medhumChart.data.datasets[0].data.push(tmpHums);
                            hums = hums.slice(tmpN+1, hums.length);
                        }
                        medhumChart.update();
                        medtempChart.update();
                    }
                });
            }
        </script>
        <%@include file="bottomjs.inc"%>
    </body>
</html>