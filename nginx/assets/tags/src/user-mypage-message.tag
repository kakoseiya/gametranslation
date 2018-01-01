<user-mypage-message>

        <div class="row">

            <!--表を表示させる-->
            <table class="table table-hover col-md-12">
                <tbody>
                <tr each="{item, i in status._data}"
                    name="{item.publicId}"
                    class="list-pointer row">

                    <th class="col-md-1"
                        id="view_check_{item.publicId}">
                        <input type="checkbox">
                    </th>
                    <th onclick="{modal.modalOn}" class="col-md-3"
                        id="view_senderName_{item.publicId}"
                        data-toggle="modal" data-target="#message_Modal">
                        {status.userPrivilege(item.userPrivileges)}
                    </th>
                    <th onclick="{modal.modalOn}" class="col-md-6"
                        id="view_subject_{item.publicId}"
                        data-toggle="modal" data-target="#message_Modal">
                        {status.userDeleted(item.deleted)}
                    </th>
                    <th onclick="{modal.modalOn}" class="col-md-2"
                        id="view_remark_{item.publicId}"
                        data-toggle="modal" data-target="#message_Modal">
                        {status.userDeleted(item.deleted)}
                    </th>
                </tr>
                </tbody>
            </table>

        </div>


        <!-- メッセージ表示用モーダル -->
        <div class="modal fade" id="message_Modal" tabindex="-1" role="dialog"
             aria-labelledby="exampleModalLongTitle" aria-hidden="true">
            <div class="modal-dialog modal-lg" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLongTitle">Modal title</h5>
                        <button type="button" class="close list-pointer" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">


                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary list-pointer" data-dismiss="modal">
                                    Close
                                </button>
                            </div>
                    </div>
                </div>
            </div>
        </div>



    <script>
        var self = this;
        this.mixin(OptsMixin);

        //初期情報
        this.status = {
            userId:opts.user,
            _data: [],
            headingName: [],
            //state:1=NotSort,2=asc,3=desc
            sortState: [],
            sortIcon: [],
            edit: false,
            newCreate: false,
            editName: null,
            editVersion: null,
            //1ページ当たりに表示させる個数
            pageCount: 20,
            //pageネーションの表示させる最大の個数
            pageMaxNation: 5,
            //ページの最大項目数
            pageMax: null,
            //現在のページ
            pageNumber: 1,
            pageLabel: [],
            pageIdLabel: [],
            newFlag: false,
            fileStatus: false,

            setData: function (data) {
                self.status._data = data;
                self.status.pageMax = Math.ceil(data.length / self.status.pageCount);
            },
            editOn: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                self.status.edit = true;
                var es = e.target.id.split('_');
            },
            editOff: function () {
                self.status.edit = false;
            },
            newCreateOn: function () {
                self.status.newCreate = true;
            },
            newCreateOff: function () {
                self.status.newCreate = false;
            },

            setIdName: function (id, name) {
                self.status.publicYmlFileId = id;
                self.status.editName = name;
            },

            pageFileCheck: function () {
                self.status.pageLabel = self.pageCheck(self.status.pageMax, self.status.pageNumber);
                console.log(self.status.pageLabel);
            },

            getPage: function () {
                return self.status.pageLabel;
            },

            setPageIdLabel: function () {
                for (var i = 0; i < self.status._data.length; i++) {
                    self.status.pageIdLabel[i] = {
                        label: Math.ceil((i + 1) / self.status.pageCount),
                        id: self.status._data[i].publicId,
                    }
                }
                self.status.pageMax = Math.ceil(self.status._data.length / self.status.pageCount);
                console.log('setPage:', self.status.pageIdLabel);
            },

            pageLabelCheck: function (label) {
                if (label == self.status.pageNumber) {
                    return true;
                }
            },
            userPrivilege: function (privilege) {
                switch (privilege) {
                    case 2000:
                        return "システム管理者";
                    case 20:
                        return "ユーザー管理者";
                    case 10:
                        return "一般";
                    case 0:
                        return "ゲスト";
                    default:
                        break;
                }
            },
            userDeleted: function (deleted) {
                if (deleted) {
                    return "削除";
                } else {
                    return "";
                }
            }
        };

        this.modal = {
            _user: null,
            modalOnCreate: function () {
                self.status.newFlag = true;
                self.modal._user = {
                    userName: '',
                    userPrivirage: '',
                    userStatus: '',
                    publicId: ''
                }
                self.update();
            },
            modalOn: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                self.status.newFlag = false;
                var es = e.target.id.split('_');
                for (var i = 0; i < self.status._data.length; i++) {
                    if (es[2] === self.status._data[i].publicId) {
                        self.modal._document = self.status._data[i];
                    }
                }
                console.log("modal_Document", self.modal._document);

                self.update();
            },

            modalOff: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
            },
            checkStatus: function () {
                if (self.modal._document.status == 50) {
                    console.log(true)
                    return true
                } else {

                    console.log(false)
                    return false
                }
            }
        };

        this._func = {
            init: function () {
                var api = "/system/account",
                    opt = {
                        method: "GET",
                        credentials: "include"
                    };
                for (var i = 0; i < self.status.headingName.length; i++) {
                    self.status.sortIcon[i] = 'glyphicon glyphicon-sort';
                }
                console.log("api", api);
                fetch(api, opt)
                    .then(function (response) {
                        console.log('response:', response)
                        if (response.ok) {
                            response.json()
                                .then(function (json) {
                                    console.log('JSON:', json);
                                    self.status.newFlag = true;
                                    self.status.setData(json.data);
                                    console.log('setData:', self.status);
                                    self.status.pageFileCheck();
                                    self.status.setPageIdLabel();
                                    self.modal.modalOnCreate();
                                    self.status.pageLabel[self.status.pageNumber - 1].pageClass = 'active';
                                    for (var i = 0; i < self.status.headingName.length; i++) {
                                        self.status.sortState[i] = 1;
                                    }
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


            sortDocumentList: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                var es = e.target.id.split('_');
                var soData = self.sortData(es[1], self.status.headingName, self.status.sortIcon, self.status.sortState, self.status._data);
                self.status.sortIcon = soData[0];
                self.status.sortState = soData[1];
                self.status.setData(soData[2]);
                self.update();
            },

            viewAdminDocument: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                console.log(e.target.id);
                var splitId = e.target.id.split('_'),
                    api = '/game/file/document/' + splitId[2];
                location.href = api;
            },

            pageNation: function (e) {
                e.preventDefault();
                console.log('e:', e);
                var es = e.path[1].id.split('_');
                console.log('pageNation:', es);
                var paJump = self.pageJump(es[2], self.status.pageNumber, self.status.pageMax);
                self.status.pageNumber = paJump[0];
                self.status.pageLabel = paJump[1];
                self.update();
            },

            saveDocument: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                var form = new FormData(e.target),
                    api = "/game/file/document",
                    opt = {
                        method: "POST",
                        credentials: "include",
                        body: form
                    };
                console.log("e::", e)
                fetch(api, opt)
                    .then(function (response) {
                        return response.json()
                    })
                    .then(function (json) {
                        console.log('json:', json)
                        location.href = "/game/file/document?id=" + self.status.publicYmlFileId;
                    })
                    .catch(function (error) {
                        console.log('Save Error:', error);
                    })
            },

            searchKey: function () {
                var keyword = self.refs.search_key.value;
            }
        }

        //マウント時に初期化
        this.on('mount', self._func.init);

    </script>

</user-mypage-message>