<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Emprunter un livre</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/style.css">
</head>
<body>
    <div class="container">
    <h2>Formulaire d’emprunt</h2>

    <form method="post" action="<%= request.getContextPath() %>/emprunt">
        <label for="idAdherent">Adhérent :</label>
        <select id="idAdherent" name="idAdherent" required>
            <option value="">-- Sélectionnez un adhérent --</option>
            <%
                java.util.List adherents = (java.util.List) request.getAttribute("adherents");
                if (adherents != null) {
                    for (Object obj : adherents) {
                        java.util.Map adh = (java.util.Map) obj;
                        Object id = adh.get("id");
                        Object nom = adh.get("nom");
                        Object prenom = adh.get("prenom");
            %>
                        <option value="<%= id %>"><%= nom %> <%= prenom %> (ID: <%= id %>)</option>
            <%
                    }
                }
            %>
        </select>
        <br><br>

        <label for="idExemplaire">Exemplaire :</label>
        <select id="idExemplaire" name="idExemplaire" required>
            <option value="">-- Sélectionnez un exemplaire --</option>
            <%
                java.util.List exemplaires = (java.util.List) request.getAttribute("exemplairesDisponibles");
                if (exemplaires != null) {
                    for (Object obj : exemplaires) {
                        java.util.Map ex = (java.util.Map) obj;
                        Object id = ex.get("id");
                        Object titre = ex.get("titre");
            %>
                        <option value="<%= id %>"><%= titre %> (ID: <%= id %>)</option>
            <%
                    }
                }
            %>
        </select>
        <br><br>

        <input type="submit" value="Emprunter" />
    </form>

    <%
        String message = (String) request.getAttribute("message");
        if (message != null && !message.isEmpty()) {
    %>
        <p style="color:blue"><%= message %></p>
    <%
        }
    %>

    <p><a href="<%= request.getContextPath() %>/retour">Retourner un livre</a></p>
    <p><a href="<%= request.getContextPath() %>/dashboard">Voir le dashboard</a></p>
    </div>
</body>
</html>
