<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script src="${pageContext.request.contextPath}/js/formValidation.js" defer></script>
</head>
<body>
    <div class="form-container">
        <h2>Login</h2>

        <!-- Error Message Box -->
        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post" onsubmit="return validateLogin()">
            <input type="text" name="username" placeholder="Username" id="loginUsername" required>
            <input type="password" name="password" placeholder="Password" id="loginPassword" required>
            <button type="submit">Login</button>
            <p>Don't have an account? <a href="jsp/signup.jsp">Sign up here</a></p>
        </form>
    </div>
</body>
</html>
