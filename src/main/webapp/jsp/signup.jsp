<!DOCTYPE html>
<html>
<head>
    <title>Signup</title>
    <link rel="stylesheet" href="../css/style.css">
    <script src="../js/formValidation.js" defer></script>
</head>
<body>
    <div class="form-container">
        <h2>Sign Up</h2>
        <form action="/ecommerce/signup" method="post" onsubmit="return validateSignup()">
            <input type="text" name="username" placeholder="Username" id="signupUsername" required>
            <input type="email" name="email" placeholder="Email" id="signupEmail" required>
            <input type="password" name="password" placeholder="Password" id="signupPassword" required>
            <input type="password" name="confirmPassword" placeholder="Confirm Password" id="signupConfirmPassword" required>
            <button type="submit">Sign Up</button>
            <p>Already have an account? <a href="jsp/login.jsp">Login here</a></p>
        </form>
    </div>
</body>
</html>
