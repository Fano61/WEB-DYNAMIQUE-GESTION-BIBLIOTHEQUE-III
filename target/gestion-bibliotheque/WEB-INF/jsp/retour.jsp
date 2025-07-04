<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Retourner un livre</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/style.css">
</head>
<body>
    <div class="container">
    <h2>Formulaire de retour</h2>

    <form method="post" action="<%= request.getContextPath() %>/retour">
        <label for="idAdherent">Adhérent :</label>
        <select id="idAdherent" name="idAdherent" required>
            <option value="">-- Sélectionnez un adhérent --</option>
            <%
                java.util.List adherents = (java.util.List) request.getAttribute("adherents");
                if (adherents != null) {
                    for (Object obj : adherents) {
                        // supposons que chaque adhérent est un objet avec getId(), getNom(), getPrenom()
                        // adapte selon ta classe
                        // exemple avec Java Beans ou Maps, ici en brut pour l'idée
                        // si c'est Map :
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
                java.util.List exemplaires = (java.util.List) request.getAttribute("exemplairesEmpruntes");
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

        <input type="submit" value="Retourner" />
    </form>

    <%
        String message = (String) request.getAttribute("message");
        if (message != null && !message.isEmpty()) {
    %>
        <p style="color:blue"><%= message %></p>
    <%
        }
    %>
    </div>
</body>
</html>
