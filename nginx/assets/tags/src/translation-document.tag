<translation-document>
    <div>

        <div class="row">
            <div class="col-md-12">
                <div class="row">
                    <div class="col-md-6">
                        <!--新規作成ボタン-->
                        <button class="btn btn-primary list-pointer" data-toggle="modal"
                                data-target="#document_admin_Modal"
                                onclick="{modal.modalOnCreate}">
                            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                            新規作成
                        </button>

                    </div>
                    <div class="col-md-6">
                        <!--検索機能を追加する(未実装)-->
                        <form>
                            <input type="text" placeholder="検索ワードを入力" 　ref="search_key">
                        </form>
                    </div>
                </div>

                <br>
            </div>

            <!--表を表示させる-->
            <table class="table table-hover col-md-12">
                <thead>
                <tr class="row">
                    <th onclick="{_func.sortDocumentList}" id="sort_origText"
                        ref="origText" class="list-pointer col-md-6">
                        原文
                        <span class="{status.sortIcon[0]}" aria-hidden="true" aria-label="Right Align"></span>
                    </th>
                    <th onclick="{_func.sortDocumentList}" id="sort_transText"
                        ref="transText" class="list-pointer col-md-3">
                        翻訳文
                        <span class="{status.sortIcon[1]}" aria-hidden="true" aria-label="Right Align"></span>
                    </th>
                    <th onclick="{_func.sortDocumentList}" id="sort_remarks"
                        ref="remarks" class="list-pointer col-md-3">
                        備考
                        <span class="{status.sortIcon[2]}" aria-hidden="true" aria-label="Right Align"></span>
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr each="{item, i in status.getData()}"
                    name="{item.publicId}"
                    class="list-pointer row">

                    <th onclick="{modal.modalOn}" class="col-md-6"
                        id="view_origText_{item.publicId}"
                        data-toggle="modal" data-target="#document_admin_Modal">
                        {item.origText}
                    </th>
                    <th onclick="{modal.modalOn}" class="col-md-3"
                        id="view_transText_{item.publicId}"
                        data-toggle="modal" data-target="#document_admin_Modal">
                        {item.transText}
                    </th>
                    <th onclick="{modal.modalOn}" class="col-md-3"
                        id="view_remarks_{item.publicId}"
                        data-toggle="modal" data-target="#document_admin_Modal">
                        {item.remarks}
                    </th>
                </tr>
                </tbody>
            </table>

        </div>


        <!-- 編集用モーダル -->
        <div class="modal fade" id="document_admin_Modal" tabindex="-1" role="dialog"
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
                        <form onsubmit="{_func.saveDocument}">
                            <input if="{!status.newFlag}" type="hidden" name="publicId"
                                   value="{modal._document.publicId}">
                            <input type="hidden" name="ymlFileId" value="{opts.publicid}">
                            <!-- Keyワード -->
                            <div class="row">
                                <div class="form-group col-md-12">
                                    <label>Key:</label>
                                    <input required type="text"
                                           class="form-control"
                                           value="{modal._document.key}"
                                           name="key"
                                    >
                                </div>
                            </div>

                            <div class="row">
                                <!-- 原文 -->
                                <div class="form-group col-md-6">
                                    <div>原文</div>
                                    <textarea name="origText"
                                              rows="14"
                                              class="form-control">{modal._document.origText}</textarea>
                                </div>

                                <!-- 翻訳 -->
                                <div class="col-md-6">
                                    <!-- Google翻訳文 -->
                                    <div class="form-group col-md-12">
                                        <div>Google翻訳</div>
                                        <textarea name="transGoogleText"
                                                  rows="6"
                                                  class="form-control">{modal._document.transGoogleText}</textarea>
                                    </div>
                                    <!-- 翻訳文 -->
                                    <div class="form-group col-md-12">
                                        <div>翻訳</div>
                                        <textarea name="transText"
                                                  rows="6"
                                                  class="form-control">{modal._document.transText}</textarea>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <!-- 備考欄 -->
                                <div class="form-group col-md-12">
                                    <label>備考欄:</label>
                                    <input required type="text"
                                           class="form-control"
                                           value="{modal._document.remarks}"
                                           name="remarks">
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group col-md-12">
                                    <input type="radio" name="status" value="50"
                                           checked="{checked:modal.checkStatus()}">翻訳完了
                                    <input type="radio" name="status" value="0"
                                           checked="{checked:!modal.checkStatus()}">未完了
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

    </div>

    <style>
        .list-pointer {
            cursor: pointer;
        }
    </style>

    <script>

        var self = this;
        this.mixin(OptsMixin);

        //初期情報
        this.status = {
            _data: [],
            headingName: [],
            //state:1=NotSort,2=asc,3=desc
            sortState: [],
            sortIcon: [],
            edit: false,
            newCreate: false,
            publicYmlFileId: opts.publicid,
            publicDocumentId: null,
            editName: null,
            editVersion: null,
            //1ページ当たりに表示させる個数
            pageCount: 5,
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

            getData: function () {
                return self.status._data;
            },
            setData: function (data) {
                self.status._data = data;
                self.status.pageMax = Math.ceil(data.length / self.status.pageCount);
            },
            editOn: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                self.status.edit = true;
                var es = e.target.id.split('_');
                location.href = '/game/file/document?id=' + self.status.publicYmlFileId + '#document-popup-model'
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
            }
        };

        this.modal = {
            _document: null,
            modalOnCreate: function () {
                self.status.newFlag = true;
                self.modal._document = {
                    key: '',
                    origText: '',
                    transGoogleText: '',
                    transText: '',
                    remarks: '',
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
                var api = "/document/" + self.status.publicYmlFileId,
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

</translation-document>