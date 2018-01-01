<header-nav>
    <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
        <a class="navbar-brand" href="#">Game Translation</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse"
                aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarCollapse">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item {active:status.navCheck('game')}">
                    <a class="nav-link" href="/game">Game
                        <span if="{status.navCheck(game)}" class="sr-only">(current)</span>
                    </a>
                </li>
                <li class="nav-item {active:status.navCheck('mypage')}" if="{status.userCheck()}">
                    <a class="nav-link" href="/mypage">MyPage
                        <span if="{status.navCheck(mypage)}" class="sr-only">(current)</span>
                    </a>
                </li>
                <li class="nav-item {active:status.navCheck('contact')}" if="{status.userCheck()}">
                    <a class="nav-link" href="#">Contact
                        <span if="{status.navCheck(contact)}" class="sr-only">(current)</span>
                    </a>
                </li>
                <li class="nav-item {active:status.navCheck('system')}" if="{status.systemUserCheck()}">
                        <a class="nav-link" href="/system">System
                        <span if="{status.navCheck(contact)}" class="sr-only">(current)</span>
                    </a>
                </li>
            </ul>
            <span class="navbar-text">
            ようこそ{status.userData.userName}様
            </span>
            &nbsp;&nbsp;
            <button class="btn btn-outline-success my-2 my-sm-0 list-pointer" onclick="{_func.logout}">
                Logout
            </button>
        </div>
    </nav>


    <script>
        var self = this;

        this.status = {
            userData: null,

            navCheck: function (nt) {
                return (nt === opts.navtype)
            },

            /**
             * ユーザーがゲストかどうかをチェックする
             * ゲストだったらfalseを返す
             */
            userCheck: function () {
                return (!(self.status.userData.userPrivileges === 0));
            },

            /**
             * ユーザーがSYSTEM_MANAGERかどうかをチェックする
             * システム管理者ならtrueを返す
             */
            systemUserCheck: function () {
                return (self.status.userData.userPrivileges === 2000);
            }
        };

        this._func = {
            init: function () {
                var api = "/user/account",
                    opt = {
                        method: "GET",
                        credentials: "include"
                    };
                fetch(api, opt)
                    .then(function (response) {
                        console.log('response:', response)
                        if (response.ok) {
                            response.json()
                                .then(function (json) {
                                    self.status.userData = json.data;
                                })
                                .then(function () {
                                    self.update();
                                })
                        } else {
                            console.log('Network response was not ok.');
                        }
                    })
                    .catch(function (error) {
                        console.log('error:' + error);
                    });
            },
            logout: function () {
                location.href = "/logout"
            }
        }

        this.on('mount', self._func.init());

    </script>
</header-nav>