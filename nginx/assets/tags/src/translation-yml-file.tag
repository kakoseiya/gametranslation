<translation-yml-file>

    <div class="row">
        <!--検索機能を追加する(未実装)-->
        <div class="col">
            <form>
                <input type="text" placeholder="検索ワードを入力" 　ref="search_key">
            </form>
        </div>
        <div class="col">
            <!--新規作成ボタン-->
            <button class="btn btn-primary" data-toggle="modal" data-target="#newCreateModal"
                    onclick="{status.newCreateOn}">
                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                新規作成
            </button>
        </div>
        <br>
    </div>
    <div class="row">
        <div class="col-md-12" each="{item,i in status._data}">
            <div class="card">
                <div class="card-body">
                    <h4 class="card-title" id="card_view_{item.publicId}" onclick="{_func.viewDocumentList}">
                        {item.name}</h4>
                    <p class="card-text">
                        {item.summary}
                    </p>
                    <div class="progress">
                        <div class="progress-bar" role="progressbar" style="width: {item.rate}%;" aria-valuenow="25"
                             aria-valuemin="0" aria-valuemax="100">
                            {item.rate}%
                        </div>
                    </div>

                    <a href="#" class="card-link" 　data-toggle="modal" data-target="#editModal"
                       onclick="{_func.editYmlFile}"
                       id="card_{item.publicId}_{item.name}">
                        編集
                    </a>
                    <a href="#" class="card-link">削除</a>
                    <a href="#" class="card-link" download="{item.name}"
                       id="download_{item.publicId}" onclick="{_func.downloadYml}">ダウンロード</a>
                </div>
            </div>
            <br>
        </div>

        <!-- 編集用Modal -->
        <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editModalLabel"
             aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editModalLabel">Modal title</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>

                    <form class="form-horizontal" onsubmit="{_func.adminYmlFile}">
                        <div class="modal-body">
                            <input type="hidden" name="publicId" value="{status.publicGameId}">
                            <div class="form-group">
                                <label class="control-label col-md-3">File Name</label>
                                <div class="col-md-9">
                                    <input type="text" class="form-control" name="title"
                                           placeholder="{status.editName}">
                                </div>
                            </div>

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal"
                                    onclick="{_func.cancel}">
                                Close
                            </button>
                            <button type="submit" class="btn btn-primary">Save changes</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- 新規作成用Modal -->
        <div class="modal fade" id="newCreateModal" tabindex="-1" role="dialog"
             aria-labelledby="newCreateModalLabel"
             aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="newCreateModalLabel">Modal title</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <form onsubmit="{_func.saveYmlFile}">
                        <div class="modal-body">
                            <input type="hidden" name="gamePublicId" value="{opts.publicid}">
                            <div class="form-group row">
                                <label class="control-label col-md-3">File Name</label>
                                <input type="text" class="form-control col-md-8" name="fileName"
                                       placeholder="File Name">
                            </div>
                            <div class="form-group row">
                                <label class="control-label col-md-3">備考欄</label>
                                <textarea name="summary"
                                          rows="2"
                                          class="form-control col-md-8"></textarea>
                            </div>
                            <div class="form-group row">
                                <label class="control-label col-md-3">ファイル</label>
                                <div class="col-md-8">
                                    <input type="file" name="YmlFile" accept=".yml">
                                </div>
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal"
                                    onclick="{_func.cancel}">
                                Close
                            </button>
                            <button type="submit" class="btn btn-primary">Save changes</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>


    </div>


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
            publicGameId: null,
            publicYmlFileId: null,
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
            tagChange: true,

            getData: function () {
                return self.status._data;
            },
            setData: function (data) {
                self.status._data = data;
                self.status.pageMax = Math.ceil(data.length / self.status.pageCount);
            },
            editOn: function () {
                self.status.edit = true;
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
            },

            pageLabelCheck: function (label) {
                if (label == self.status.pageNumber) {
                    return true;
                }
            }
        };

        this._func = {
            init: function () {
                self.status.publicGameId = opts.publicid;
                var api = "/file/" + self.status.publicGameId,
                    opt = {
                        method: "GET",
                        credentials: "include"
                    };
                self.status.headingName[0] = 'name';
                self.status.headingName[1] = 'summary';
                self.status.headingName[2] = 'rate';
                self.status.pageNumber = 1;
                for (var i = 0; i < self.status.headingName.length; i++) {
                    self.status.sortIcon[i] = 'glyphicon glyphicon-sort';
                }
                fetch(api, opt)
                    .then(function (response) {
                        console.log('response:', response)
                        if (response.ok) {
                            response.json()
                                .then(function (json) {
                                    self.status.setData(json.data);
                                    self.status.pageFileCheck();
                                    self.status.setPageIdLabel();
                                    self.status.pageLabel[self.status.pageNumber - 1].pageClass = 'active';
                                    for (var i = 0; i < self.status.headingName.length; i++) {
                                        self.status.sortState[i] = 1;
                                    }
                                })
                                .then(function () {
                                    self.update();
                                })
                        } else {
                        }
                    })
                    .catch(function (error) {
                        console.log('error:' + error);
                    });


            },


            sortYmlFileList: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                var es = e.target.id.split('_');
                var soData = self.sortData(es[1], self.status.headingName, self.status.sortIcon, self.status.sortState, self.status._data);
                self.status.sortIcon = soData[0];
                self.status.sortState = soData[1];
                self.status.setData(soData[2]);
                self.update();
            },

            viewDocumentList: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                console.log(e.target.id);
                var splitId = e.target.id.split('_');
                console.log('obs:', self);
                self.status.tagChange = false;
                self.update();
                var api = "/game/file/document?id=" + splitId[2];
                location.href = api;
            },

            saveYmlFile: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                var form = new FormData(e.target),
                    api = "/game/ymlfile?id=" + opts.publicid,
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
                        console.log('json:', json)
                        self.status.newCreateOff();
                        self._func.init();
                        self.update();
                    })
                    .catch(function (error) {
                        console.log('Save Error:', error);
                    })
            },

            deleteYmlFile: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                var splitId = e.target.id.split('_'),
                    api = '/game/file' + '?id=' + splitId[1],
                    opt = {
                        method: "DELETE",
                        credentials: "include"
                    };

                fetch(api, opt)
                    .then(function (response) {
                        return response.json();
                    })
                    .then(function (json) {
                        console.log('削除成功:', json);
                        self._func.init();
                        self.update();
                    })
                    .catch(function (error) {
                        console.log('error:' + error);
                    });

            },

            adminYmlFile: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                var form = new FormData(e.target),
                    api = "/game/file",
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
                        self.status.editOff();
                        self._func.init();
                        self.update();
                    })
                    .catch(function (error) {
                        console.log('Edit Save Error:', error);
                    })
            },

            editYmlFile: function (e) {
                if (!self.status.edit) {
                    e.preventUpdate = true;
                    e.preventDefault();
                    self.status.newCreateOff();
                    var es = e.target.id.split("_"),
                        filename = es[2];
                    console.log('es.length:', es);
                    for (var i = 3; i < es.length; i++) {
                        filename = filename + '_' + es[i];
                        console.log(filename);
                    }
                    self.status.setIdName(es[1], filename);
                    self.status.editOn();
                    self.update();
                } else {
                    self.status.editOff();
                }
            },

            cancel: function () {
                self.status.edit = false;
                self.newCreate = false;
                self.status.publicYmlFileId = null;
                self.status.editName = null;
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

            downloadYml: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                console.log("doyml:", e.target.id)
                var es = e.target.id.split("_"),
                    ymltext = "",
                    api = "/file?id=" + es[1],
                    opt = {
                        method: "GET",
                        credentials: "include",
                    };
                console.log("es", es);
                fetch(api, opt)
                    .then(function (response) {
                        return response.json();
                    })
                    .then(function (json) {
                        console.log("JSON:", json);
                        ymltext = "l_" + json.data.lang + ":\r\n";
                        for (var i = 0; i < json.data.documents.length; i++) {
                            ymltext += " " + json.data.documents[i].key
                                + " \"" + json.data.documents[i].transText + "\"\r\n";
                        }
                        console.log("ymlText:\r\n", ymltext);
                        var encYmlText = new Uint8Array([ymltext]);
                        console.log("encYmlText:", encYmlText);
                        console.log("document:", document);
                        console.log(document.getElementById("download_" + es[1]));
                        // BOMは文字化け対策
                        var bom = new Uint8Array([0xEF, 0xBB, 0xBF]),
                            blob = new Blob(
                                [bom, ymltext],
                                {
                                    type: "application/x-msdownload"
                                });
                        console.log("Blob:", blob);
                        console.log("window", window);
                        var link = document.createElement('a');
                        link.setAttribute("download", json.data.name);
                        link.href = URL.createObjectURL(blob);

                        if (window.navigator.msSaveBlob) {
                            // window.navigator.msSaveBlob(blob, downloadFileName);
                            window.navigator.msSaveOrOpenBlob(blob, downloadFileName);
                        }
                        else {
                            var evt = document.createEvent("MouseEvents");
                            evt.initEvent("click", false, true);
                            link.dispatchEvent(evt);
                        }


                    })
                    .catch(function (error) {
                        console.log("error:", error);
                    })

            },

            searchKey: function () {
                var keyword = self.refs.search_key.value;

            }


        }

        //マウント時に初期化
        this.on('mount', self._func.init);

    </script>


</translation-yml-file>