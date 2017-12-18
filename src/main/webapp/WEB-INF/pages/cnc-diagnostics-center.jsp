<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>CPMC Diagnostics Center</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css"
          integrity="sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M" crossorigin="anonymous">
    <script src="<c:url value="/resources/js/jquery-3.2.1.min.js"/>"></script>

    <style>
        #rcorners1 {
            border-radius: 25px;
            border: 2px solid darkblue;
            background: wheat;
            padding: 20px;
            width: auto;
            height: 425px;
        }

        #rcorners3 {
            border-radius: 25px;
            border: 2px solid darkblue;
            background: wheat;
            padding: 20px;
            width: auto;
            height: 425px;
        }

        #rcorners4 {
            border-radius: 25px;
            border: 2px solid darkblue;
            background: wheat;
            padding: 20px;
            width: auto;
            height: 425px;
        }

        #rcorners2 {
            border-radius: 25px;
            border: 2px solid darkblue;
            background: yellow;
            padding: 20px;
            width: auto;
            height: 75px;
        }

        .flex-container {
            display: -webkit-flex;
            display: flex;
            -webkit-flex-flow: row wrap;
            flex-flow: row wrap;
            text-align: center;
        }

        .flex-container > * {
            padding: 15px;
            -webkit-flex: 1 100%;
            flex: 1 100%;
        }

        .article {
            text-align: left;
            /*border-radius: 25px;*/
            /*border: 2px solid darkblue;*/
        }

        .aside {
            position: relative;
            float: left;
            width: 195px;
            top: 0px;
            bottom: 0px;
            background-color: #ebddca;
            height: 100vh;
        }

        header {
            background: black;
            color: white;
        }

        footer {
            background: #aaa;
            color: white;
        }

        .nav {
            background: #eee;
        }

        .nav ul {
            list-style-type: none;
            padding: 0;
        }

        .nav ul a {
            text-decoration: none;
        }

        @media all and (min-width: 768px) {
            .nav {
                text-align: left;
                -webkit-flex: 1 auto;
                flex: 1 auto;
                -webkit-order: 1;
                order: 1;
            }

            .article {
                -webkit-flex: 5 0px;
                flex: 5 0px;
                -webkit-order: 2;
                order: 2;
            }

            footer {
                -webkit-order: 3;
                order: 3;
            }
        }

        table {
            border-collapse: collapse;
            width: 100%;
        }

        th, td {
            padding: 8px;
            text-align: center;
            border-bottom: 1px solid #ddd;
        }

        tr:hover {
            background-color: #f5f5f5
        }
    </style>

    <script>
        function diagnose() {
            var codeAxes = document.getElementById("codeAxes");
            var levelAxes = document.getElementById("levelAxes");
            var causeAxes = document.getElementById("causeAxes");
            codeAxes.innerHTML = "W1";
            levelAxes.innerHTML = "WARNING";
            causeAxes.innerHTML = "Y-Axes movement is not normal";

            var codeDrill = document.getElementById("codeDrill");
            var levelDrill = document.getElementById("levelDrill");
            var causeDrill = document.getElementById("causeDrill");
            codeDrill.innerHTML = "E3";
            levelDrill.innerHTML = "ERROR";
            causeDrill.innerHTML = "The Spindle is not spinning";

/*
            $.ajax({
                url: "http://uaf132943.ddns.uark.edu:1080/ping",
                dataType: "text",
                success: function (data) {
                },
                complete: function (data) {
                    console.log(data.responseText);
                    if (data.responseText === "C1") {
                        code.innerHTML = data.responseText;
                        level.innerHTML = "WARNING";
                        cause.innerHTML = "Agent-Adapter connection may not working";
                    } else if (data.responseText === "C2") {
                        code.innerHTML = data.responseText;
                        level.innerHTML = "ERROR";
                        cause.innerHTML = "Machine not found";
                    } else if (data.responseText === "NONE") {
                        code.innerHTML = "";
                        level.innerHTML = "";
                        cause.innerHTML = "";
                    }
                },
                timeout: 12000  // two minutes
            });
*/
        }

        function clearTable() {
            var code = document.getElementById("code");
            var level = document.getElementById("level");
            var cause = document.getElementById("cause");
            code.innerHTML = "";
            level.innerHTML = "";
            cause.innerHTML = "";
        }

        function diagnoseCNC() {
            alert("TBD");
        }

        function diagnosisCheck() {
            var divAxesCheckLight = document.getElementById("diagnosticAxesCheckLight");
            var divDrillCheckLight = document.getElementById("diagnosticDrillCheckLight");
            var divNetworkCheckLight = document.getElementById("diagnosticNetworkCheckLight");
            $.ajax({
                url: "${flagUrl}",
                dataType: "text",
                success: function (data) {
                },
                complete: function (data) {
                    // axes
                    if (data.responseText.yAxesFlag === 'WARNING') {
                        divAxesCheckLight.style.backgroundColor = "yellow";
                    } else if (data.responseText.yAxesFlag === 'ERROR') {
                        divAxesCheckLight.style.backgroundColor = "red";
                    } else {
                        divAxesCheckLight.style.backgroundColor = "green";
                    }
                    divAxesCheckLight.style.backgroundColor = "yellow";
                    // drill
                    if (data.responseText.drillFlag === 'WARNING') {
                        divDrillCheckLight.style.backgroundColor = "yellow";
                    } else if (data.responseText.drillFlag === 'ERROR') {
                        divDrillCheckLight.style.backgroundColor = "red";
                    } else {
                        divDrillCheckLight.style.backgroundColor = "green";
                    }
                    divDrillCheckLight.style.backgroundColor = "red";
                    // network
                    if (data.responseText.networkFlag === 'WARNING') {
                        divNetworkCheckLight.style.backgroundColor = "yellow";
                    } else if (data.responseText.networkFlag === 'ERROR') {
                        divNetworkCheckLight.style.backgroundColor = "red";
                    } else {
                        divNetworkCheckLight.style.backgroundColor = "green";
                    }
                }
            });
        }
        window.setInterval(diagnosisCheck, 1000);

    </script>
</head>

<body>
<div id="rcorners2">
    <h4 style="text-align: center">CPMC Diagnostics Center</h4>
</div>
<table>
    <tr>
        <td>
            <div id="rcorners1">
                <h3 style="text-align: center">CNC Y-axes Fault Diagnosis</h3>
                <p style="text-align: center">[GREEN = OK, YELLOW = WARNING, and RED = ERROR]</p>
                <div id="diagnosticAxesCheckLight" style="text-align: center; width: auto; height: 75px;
                        background-color: green; border: 3px; border-color: black;
                                                border-radius: 25px"></div>
                <br/>
                <br/>
                <div style="text-align: center">
                    <input type="button" class="btn btn-warning" value="Test Y-Axes" onclick="diagnose()">
                </div>
                <br>
                <br>
                <div>
                    <table>
                        <tr>
                            <th>
                                <input type="button" class="btn btn-success" value="Clear" onclick="clearTable()"/>
                            </th>
                            <th>Code</th>
                            <th>Level</th>
                            <th>Cause</th>
                        </tr>
                        <tr>
                            <td></td>
                            <td><p id="codeAxes"></p></td>
                            <td><p id="levelAxes"></p></td>
                            <td><p id="causeAxes"></p></td>
                        </tr>
                    </table>
                </div>
            </div>
        </td>
        <td>
            <div id="rcorners3">
                <h3 style="text-align: center">CNC Drill Diagnosis</h3>
                <p style="text-align: center">[GREEN = OK, YELLOW = WARNING, and RED = ERROR]</p>
                <div id="diagnosticDrillCheckLight" style="text-align: center; width: auto; height: 75px;
                        background-color: green; border: 3px; border-color: black;
                                                border-radius: 25px"></div>
                <br/>
                <br/>
                <div style="text-align: center">
                    <input type="button" class="btn btn-warning" value="Test Drill" onclick="diagnoseCNC()">
                </div>
                <br>
                <br>
                <div>
                    <table>
                        <tr>
                            <th>
                                <input type="button" class="btn btn-success" value="Clear" onclick="clearTable()"/>
                            </th>
                            <th>Code</th>
                            <th>Level</th>
                            <th>Cause</th>
                        </tr>
                        <tr>
                            <td></td>
                            <td><p id="codeDrill"></p></td>
                            <td><p id="levelDrill"></p></td>
                            <td><p id="causeDrill"></p></td>
                        </tr>
                    </table>
                </div>
            </div>
        </td>
        <td>
            <div id="rcorners4">
                <h3 style="text-align: center">CNC Network Diagnosis</h3>
                <p style="text-align: center">[GREEN = OK, YELLOW = WARNING, and RED = ERROR]</p>
                <div id="diagnosticNetworkCheckLight" style="text-align: center; width: auto; height: 75px;
                        background-color: green; border: 3px; border-color: black;
                                                border-radius: 25px"></div>
                <br/>
                <br/>
                <div style="text-align: center">
                    <input type="button" class="btn btn-warning" value="Test Network" onclick="diagnoseCNC()">
                </div>
                <br>
                <br>
                <div>
                    <table>
                        <tr>
                            <th>
                                <input type="button" class="btn btn-success" value="Clear" onclick="clearTable()" />
                            </th>
                            <th>Code</th>
                            <th>Level</th>
                            <th>Cause</th>
                        </tr>
                        <tr>
                            <td></td>
                            <td><p id="codeNetwork"></p></td>
                            <td><p id="levelNetwork"></p></td>
                            <td><p id="causeNetwork"></p></td>
                        </tr>
                    </table>
                </div>
            </div>
        </td>
    </tr>
</table>

</body>
</html>