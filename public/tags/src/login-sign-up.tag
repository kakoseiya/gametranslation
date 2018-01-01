<login-sign-up>
    <div class="container">
        <div class="row">
            <div class="col-md-2"></div>
            <div class="card card-container col-md-8">
                <br>
                <form class="form-signin" onsubmit="{_func.signUp}">
                    <div class="form-group row">
                        <label class="col-md-3 col-form-label">ユーザー名</label>
                        <div class="col-md-9">
                            <input type="text" class="form-control"
                                   name="userName" ref="userName" placeholder="Name" required>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-md-3 col-form-label">Email</label>
                        <div class="col-md-9">
                            <input type="email" class="form-control"
                                   name="mail" ref="mail" placeholder="Email" required>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-md-3 col-form-label">パスワード</label>
                        <div class="col-md-9">
                            <input type="password" class="form-control"
                                   name="pass" ref="pass" placeholder="8桁のパスワード" required>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-md-3 col-form-label">パスワード(確認)</label>
                        <div class="col-md-9">
                            <input type="password" class="form-control"
                                   name="rePass" ref="rePass" placeholder="パスワード(確認)" required>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label">生年月日</label>
                        <div class="col-9">
                            <input type="date" class="form-control"
                                   name="birthday" ref="birthday" required>
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-3 col-form-label">電話番号</label>
                        <div class="col-9">
                            <input type="tel" class="form-control" name="mobileNumber" ref="mobileNumber"
                                   placeholder="00000000">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-9 offset-md-3">
                            <button type="button" class="btn btn-primary" data-toggle="modal"
                                    data-target="#confirmationModal" onclick="{_func.confirm}">&nbsp&nbsp確認&nbsp&nbsp
                            </button>
                            <button type="button" class="btn btn-primary" onclick="{_func.cancel}">キャンセル</button>
                        </div>
                    </div>

                    <!-- フォーム確認用Modal -->
                    <div class="modal fade" id="confirmationModal" tabindex="-1" role="dialog"
                         aria-labelledby="confirmationModalLabel"
                         aria-hidden="true">
                        <div class="modal-dialog" role="document">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="confirmationModalLabel">確認フォーム</h5>
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                        <span aria-hidden="true">&times;</span>
                                    </button>
                                </div>
                                <div class="modal-body">
                                    <table class="table">
                                        <tbody>
                                        <tr>
                                            <td>ユーザー名</td>
                                            <td>{refs.userName.value}</td>
                                        </tr>
                                        <tr>
                                            <td>Email</td>
                                            <td>{refs.mail.value}</td>
                                        </tr>
                                        <tr>
                                            <td>パスワード</td>
                                            <td>{refs.pass.value}</td>
                                        </tr>
                                        <tr>
                                            <td>生年月日</td>
                                            <td>{refs.birthday.value}</td>
                                        </tr>
                                        <tr>
                                            <td>電話番号</td>
                                            <td>{refs.mobileNumber.value}</td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                                    <button type="submit" class="btn btn-primary">Save changes</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
                <br>
            </div>
            <div class="col-md-2"></div>
        </div>
    </div>


    <script>
        var self = this;

        this._func = {
            confirm: function (e) {

            },

            signUp: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                console.log("e:", e)
                var form = new FormData(e.target),
                    api = "/signup",
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
                        if(json.code==200){
                            location.href="/debug/signup/preregisted?hash="+json.message
                        }
                    })
                    .catch(function (error) {
                        console.log('Save Error:', error);
                    })
            },
            cancel:function () {
                location.href="/login";
            }
        }
    </script>


</login-sign-up>