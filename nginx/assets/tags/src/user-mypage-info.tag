<user-mypage-info>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td>ユーザー名</td>
            <td>{status._data.userName}</td>
        </tr>
        <tr>
            <td>Email</td>
            <td>{status._data.mail}</td>
        </tr>
        <tr>
            <td>パスワード</td>
            <td>{status._data.pass}</td>
        </tr>
        <tr>
            <td>生年月日</td>
            <td>{status._data.birthDay[0]}/{status._data.birthDay[1]}/{status._data.birthDay[2]}</td>
        </tr>
        <tr>
            <td>電話番号</td>
            <td>{status._data.mobileNumber}</td>
        </tr>
        </tbody>
    </table>
    <button type="button" class="btn btn-primary list-pointer"
            data-toggle="modal" data-target="#user_admin_Modal">
        編集
    </button>
    <button type="button" class="btn btn-primary list-pointer">
        退会申請
    </button>

    <!-- 編集用モーダル -->
    <div class="modal fade" id="user_admin_Modal" tabindex="-1" role="dialog"
         aria-labelledby="exampleModalLongTitle" aria-hidden="true">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLongTitle">ユーザー編集</h5>
                    <button type="button" class="close list-pointer" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form onsubmit="{_func.saveAccount}">
                        <input type="hidden" name="publicId" value="{status._data.publicUserId}">
                        <!-- ユーザー名 -->
                        <div class="form-group row">
                            <label class="col-md-3 col-form-label">ユーザー名</label>
                            <div class="col-md-9">
                                <input type="text" class="form-control"
                                       name="userName" ref="userName" value="{status._data.userName}" required>
                            </div>
                        </div>

                        <!-- Email -->
                        <div class="form-group row">
                            <label class="col-md-3 col-form-label">Email</label>
                            <div class="col-md-9">
                                <input type="email" class="form-control"
                                       name="mail" ref="mail" value="{status._data.mail}" required>
                            </div>
                        </div>

                        <!-- パスワード -->
                        <div class="form-group row">
                            <label class="col-md-3 col-form-label">パスワード</label>
                            <div class="col-md-9">
                                <input type="password" class="form-control"
                                       name="pass" ref="pass">
                            </div>
                        </div>

                        <!-- パスワード再入力 -->
                        <div class="form-group row">
                            <label class="col-md-3 col-form-label">パスワード(確認)</label>
                            <div class="col-md-9">
                                <input type="password" class="form-control"
                                       name="rePass" ref="rePass">
                            </div>
                        </div>

                        <!-- 電話番号 -->
                        <div class="form-group row">
                            <label class="col-md-3 col-form-label">電話番号</label>
                            <div class="col-md-9">
                                <input type="tel" class="form-control"
                                       name="mobileNumber" ref="mobileNumber" value="{status._data.mobileNumber}" required>
                            </div>
                        </div>


                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary list-pointer" data-dismiss="modal">
                                Close
                            </button>
                            <button type="submit" class="btn btn-primary list-pointer">Save changes</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script>
        var self = this;
        this.mixin(OptsMixin);
        this.status = {
            _data: [],
            userId: opts.user
        }
        this._func = {
            init: function () {
                var api = "/user/account",
                    opt = {
                        method: "GET",
                        credentials: "include"
                    };
                fetch(api, opt)
                    .then(function (response) {
                        return response.json();
                    })
                    .then(function (json) {
                        self.status._data = json.data;
                        console.log("data:", self.status._data);
                        self.update();
                    })
                    .catch(function (error) {
                        console.log("error:", error);
                    })
            },
            saveAccount: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                var form = new FormData(e.target),
                    api = "/mypage/account",
                    opt = {
                        method: "POST",
                        credentials: "include",
                        body: form
                    };
                fetch(api, opt)
                    .then(function (response) {
                        return response.json()
                    })
                    .then(function (json) {
                        console.log('json:', json);
                        if(json.code==200){
                            location.href="/mypage"
                        }
                    })
                    .catch(function (error) {
                        console.log('Save Error:', error);
                    })
            }
        }
        this.on("mount", self._func.init());

    </script>
</user-mypage-info>