<html lang="en">
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <title>Form - HTML5</title>
    <link rel="stylesheet" type="text/css" href="../css/bootstrap.css">
</head>
<body class="bg-light">
<#if userSigned == false>
    <#include "firstNavBar.ftl">
<#else>
    <#include "navbar.ftl">
</#if>
<div class="container">
    <div class="py-3 text-center">
        <h2>Log in</h2>
    </div>
    <div class="row justify-content-lg-center">
        <div class="col col-lg-3">
            <form class="needs-validation" method="post" action="/registerUser/">
                <div class="row">
                    <div class="mb-2" style="width: 100%;">
                        <label for="firstName">Name</label>
                        <input type="text" class="form-control" name="name" width="col-md-auto" required>
                        <div class="invalid-feedback">
                            Valid first name is required.
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="mb-2" style="width: 100%;">
                        <label for="firstName">Usename</label>
                        <input type="text" class="form-control" name="username" width="col-md-auto" required>
                        <div class="invalid-feedback">
                            Valid first name is required.
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div style="width: 100%;" class="mb-2">
                        <label for="sector">Password</label>
                        <input type="password" class="form-control col-md-auto" name="password" required>
                        <div class="invalid-feedback">
                            Please enter your shipping address.
                        </div>
                    </div>
                </div>
                <#if usuario =="admin">
                <div class="row">
                    <div style="width: 100%;" class="mb-2">
                        <input type="checkbox" name="admin" value="true"> This User is <strong>Admin</strong><br>
                    </div>
                </div>
                </#if>
                <hr>
                <button class="btn btn-primary btn-md btn-block" type="submit" id="sendButton">Send!</button>
            </form>
        </div>
    </div>
    <footer class="my-5 pt-5 text-muted text-center text-small">
        <p class="mb-1">2019 Dhamar's URl Shortener</p>
    </footer>
</div>
</body>
</html>