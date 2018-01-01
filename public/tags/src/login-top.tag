<login-top>
    <div class="container">
        <div class="row">
            <div class="col-md-3"></div>
            <div class="card card-container col-md-6">
                <h1>ログイン</h1>
                <form class="form-signin" onsubmit="{_func.login}">
                    <div class="form-group">
                        <input type="email" id="inputEmail" name="mail"
                               class="form-control" placeholder="Email address" required
                               autofocus>
                    </div>
                    <div class="form-group">
                        <input type="password" id="inputPassword" name="pass"
                               class="form-control" placeholder="Password" required>
                    </div>
                    <button class="btn btn-lg btn-primary btn-block btn-signin list-pointer" type="submit">ログイン</button>
                </form><!-- /form -->
                <br>
                <a href="#" class="forgot-password">
                    Forgot the password?
                </a>
                <hr>
                <div>
                    <form onsubmit="{_func.login}">
                        <input type="hidden" name="mail" value="kakoseiya@gmail.com">
                        <input type="hidden" name="pass" value="guest04140414">
                        <button class="btn btn-primary list-pointer" type="submit">
                            ゲストでログインする
                        </button>
                        <button class="btn btn-primary list-pointer" type="button" onclick="{_func.createUser}">
                            新規作成
                        </button>
                    </form>
                </div>
                <br>
            </div><!-- /card-container -->
            <div class="col-md-3"></div>
        </div>
    </div><!-- /container -->

    <script>
        var self = this;

        this._func = {
            createUser: function () {
                location.href = "/signup"
            },
            login: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                console.log("e:", e)
                var form = new FormData(e.target),
                    api = "/login",
                    opt = {
                        method: "POST",
                        credentials: "include",
                        body: form
                    };
                console.log("e.form:", form);
                fetch(api, opt)
                    .then(function (response) {
                        return response.json()
                    })
                    .then(function (json) {
                        console.log('json:', json);
                        if (json.code === 200) {
                            location.href = json.message;
                        }
                    })
                    .catch(function (error) {
                        console.log('Save Error:', error);
                    })
            }
        }

    </script>
</login-top>