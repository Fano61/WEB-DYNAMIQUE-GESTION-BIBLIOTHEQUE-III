<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Connexion Bibliothécaire</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/style.css">
</head>
<body>
<h2>Connexion</h2>
<form action="<%= request.getContextPath() %>/login" method="post">
    <label>Utilisateur :</label>
    <input type="text" name="username" required><br><br>
    <label>Mot de passe :</label>
    <input type="password" name="password" required><br><br>
    <input type="submit" value="Se connecter">
</form>

<% 
    String error = (String) request.getAttribute("error");
    if (error != null && !error.isEmpty()) { 
%>
    <p style="color:red"><%= error %></p>
<% 
    } 
%>

</body>
</html>
