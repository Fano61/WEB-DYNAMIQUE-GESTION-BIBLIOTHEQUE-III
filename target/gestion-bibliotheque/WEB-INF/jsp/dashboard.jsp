<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<html>
<head>
    <title>Dashboard</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/stylee.css">
</head>
<body>
<div class="container">
<h2>Statistiques</h2>

<h2> Livres les plus empruntés</h2>
<%
    List topLivres = (List) request.getAttribute("topLivres");
    if (topLivres != null && !topLivres.isEmpty()) {
%>
    <ul>
    <%
        for (Object livre : topLivres) {
    %>
        <li><%= livre %></li>
    <%
        }
    %>
    </ul>
<%
    } else {
%>
    <p>Aucune donnée à afficher.</p>
<%
    }
%>

<h2> Adhérents les plus actifs</h2>
<%
    List topAdherents = (List) request.getAttribute("topAdherents");
    if (topAdherents != null && !topAdherents.isEmpty()) {
%>
    <ul>
    <%
        for (Object adh : topAdherents) {
    %>
        <li><%= adh %></li>
    <%
        }
    %>
    </ul>
<%
    } else {
%>
    <p>Aucune donnée à afficher.</p>
<%
    }
%>

<h3> Emprunts par type d’adhérent</h3>
<%
    List profils = (List) request.getAttribute("profils");
    if (profils != null && !profils.isEmpty()) {
%>
    <ul>
    <%
        for (Object obj : profils) {
            Map profil = (Map) obj;
            Object typeAdherent = profil.get("type_adherent");
            Object total = profil.get("total");
    %>
        <li><%= typeAdherent %> : <%= total %> emprunts</li>
    <%
        }
    %>
    </ul>
<%
    } else {
%>
    <p>Aucune donnée à afficher.</p>
<%
    }
%>

<h3> Taux de retard</h3>
<%
    Object tauxRetard = request.getAttribute("tauxRetard");
    if (tauxRetard != null) {
%>
    <p><%= tauxRetard %>% des emprunts sont en retard.</p>
<%
    } else {
%>
    <p>Pas de données sur le taux de retard.</p>
<%
    }
%>

<h3> Historique des emprunts</h3>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Livre</th>
        <th>Adhérent</th>
        <th>Date emprunt</th>
        <th>Retour prévu</th>
    </tr>
    <%
        List<Map<String, Object>> histEmp = (List<Map<String, Object>>) request.getAttribute("historiqueEmprunts");
        if (histEmp != null) {
            for (Map<String, Object> row : histEmp) {
    %>
        <tr>
            <td><%= row.get("id") %></td>
            <td><%= row.get("titre") %></td>
            <td><%= row.get("nom") %> <%= row.get("prenom") %></td>
            <td><%= row.get("dateEmprunt") %></td>
            <td><%= row.get("dateRetourPrevue") %></td>
        </tr>
    <%
            }
        }
    %>
</table>

<br>

<h3> Historique des retours</h3>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Livre</th>
        <th>Adhérent</th>
        <th>Date retour</th>
    </tr>
    <%
        List<Map<String, Object>> histRet = (List<Map<String, Object>>) request.getAttribute("historiqueRetours");
        if (histRet != null) {
            for (Map<String, Object> row : histRet) {
    %>
        <tr>
            <td><%= row.get("id") %></td>
            <td><%= row.get("titre") %></td>
            <td><%= row.get("nom") %> <%= row.get("prenom") %></td>
            <td><%= row.get("dateRetour") %></td>
        </tr>
    <%
            }
        }
    %>
</table>

<p><a href="<%= request.getContextPath() %>/emprunt">Nouvel emprunt</a></p>
<p><a href="<%= request.getContextPath() %>/retour">Retourner un livre</a></p>
<p><a href="<%= request.getContextPath() %>/logout">Se déconnecter</a></p>

</div>
</body>
</html>
