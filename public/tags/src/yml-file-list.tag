<yml-file-list>
    <div class="container">
        <div class="row">
            <!--検索機能を追加する(未実装)-->
            <div class="col-xs-12">
                <form>
                    <input type="text" placeholder="検索ワードを入力" 　ref="search_key">
                </form>
            </div>

            <!--表を表示させる-->
            <div class="col-xs-12">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th onclick="{_func.sortYmlFileList}" id="sort_name" ref="name" class="list-pointer">
                            File Name
                            <span class="{status.sortIcon[0]}" aria-hidden="true" aria-label="Right Align"></span>
                        </th>
                        <th onclick="{_func.sortYmlFileList}" id="sort_summary" ref="summary" class="list-pointer">
                            概要
                            <span class="{status.sortIcon[1]}" aria-hidden="true" aria-label="Right Align"></span>
                        </th>
                        <th onclick="{_func.sortYmlFileList}" id="sort_rate" ref="rate" class="list-pointer">
                            翻訳率 [%]
                            <span class="{status.sortIcon[2]}" aria-hidden="true" aria-label="Right Align"></span>
                        </th>
                        <th ref="edit_delete"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr each="{item, i in status.getData()}"
                        name="{item.publicId}"
                        if="{status.pageLabelCheck(status.pageIdLabel[i].label)}"
                        class="list-pointer">
                        <th onclick="{_func.viewDocumentList}" id="view_name_{item.publicId}">{item.name}</th>
                        <th onclick="{_func.viewDocumentList}" id="view_summary_{item.publicId}">{item.summary}</th>
                        <th onclick="{_func.viewDocumentList}" id="view_rate_{item.publicId}">{item.rate.toFixed(1)}</th>
                        <th>
                            <button class="btn btn-primary" id="admin_{item.publicId}_{item.name}_{item.version}"
                                    onclick="{_func.editYmlFile}">
                                <span class="glyphicon glyphicon-wrench" aria-hidden="true"
                                      aria-label="Right Align"
                                      id="adminspan_{item.publicId}_{item.name}_{item.version}"
                                ></span>
                                編集
                            </button>
                            <button class="btn btn-primary" id="delete_{item.publicId}" onclick="{_func.deleteYmlFile}">
                                <span class="glyphicon glyphicon-trash" aria-hidden="true"
                                      aria-label="Right Align"
                                      id="deletespan_{item.publicId}"
                                ></span>
                                削除
                            </button>
                        </th>
                    </tr>
                    </tbody>
                </table>
            </div>

            <!--View Pagination-->
            <div class="col-xs-12">
                <ul class="pagination">
                    <li id="page_li_previous" onclick="{_func.pageNation}">
                        <a href="#" aria-label="Previous" id="page_a_previous">
                                <span aria-hidden="true" id="page_span_previous">
                                    &laquo;
                                </span>
                        </a>
                    </li>

                    <li each="{page,i in status.getPage()}" id="page_li_{page.label}"
                        class="{page.pageClass} " onclick="{_func.pageNation}">
                        <a href="#" id="page_a_{page.label}">
                                <span aria-hidden="true" id="page_span_{page.label}">
                                    {page.label}
                                </span>
                        </a>
                    </li>

                    <li id="page_li_next" class="pageL1" onclick="{_func.pageNation}">
                        <a href="#" aria-label="Next" id="page_a_next">
                                <span aria-hidden="true" id="page_span_next">
                                    &raquo;
                                </span>
                        </a>
                    </li>
                </ul>
            </div>

            <!--新規作成ボタン-->
            <div if="{!status.edit && !status.newCreate}" class="col-xs-6">
                <button class="btn btn-primary" onclick="{status.newCreateOn}">
                    <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                    新規作成
                </button>
            </div>

            <!--新規作成用Form-->
            <div if="{!status.edit && status.newCreate}" class="col-xs-12">
                <form class="form-horizontal" onsubmit="{_func.saveYmlFile}">
                    <div class="form-group">
                        <label class="control-label col-xs-3">File Name</label>
                        <div class="col-xs-9">
                            <input type="text" class="form-control" name="title" placeholder="File Name">

                        </div>
                        <br>
                        <label class="control-label col-xs-3">ファイルアップロード</label>
                        <div class="col-xs-9">
                            <input type="file" name="file">

                        </div>
                    </div>
                    <div class="col-xs-9 col-xs-offset-3">
                        <button type="submit" class="btn btn-primary　glyphicon glyphicon-save">新規保存</button>
                        <button class="btn btn-primary　glyphicon glyphicon-remove" onclick="{status.newCreateOff}">
                            キャンセル
                        </button>
                    </div>
                </form>

            </div>

            <!--編集用Form-->
            <div if="{status.edit && !status.newCreate}" class="col-xs-12">
                <form class="form-horizontal" onsubmit="{_func.adminYmlFile}">
                    <input type="hidden" name="publicId" value="{status.publicGameId}">
                    <div class="form-group">
                        <label class="control-label col-xs-3">File Name</label>
                        <div class="col-xs-9">
                            <input type="text" class="form-control" name="title" placeholder="{status.editName}">
                        </div>
                    </div>
                    <div class="col-xs-9 col-xs-offset-3">
                        <button type="submit" class="btn btn-primary">
                            <span class="glyphicon glyphicon-save" aria-hidden="true"></span>
                            保存
                        </button>
                        <button class="btn btn-primary" onclick="{_func.cancel}">
                            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
                            キャンセル
                        </button>
                    </div>
                </form>

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
                console.log('setPage:', self.status.pageIdLabel);
            },

            pageLabelCheck: function (label) {
                if (label == self.status.pageNumber) {
                    return true;
                }
            }
        };

        this._func = {
            init: function () {
                self.status.publicGameId = opts.publicId;
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
                console.log(self.status.sortIcon);
                fetch(api, opt)
                    .then(function (response) {
                        console.log('response:', response)
                        if (response.ok) {
                            response.json()
                                .then(function (json) {
                                    console.log('JSON:', json);
                                    self.status.setData(json.data);
                                    console.log('setData:', self.status);
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
                            console.log('Network response was not ok.');
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
                var splitId = e.target.id.split('_'),
                    api = '/game/file/document' + '?id=' + splitId[2];
                location.href = api;
            },

            saveYmlFile: function (e) {
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
                    for (var i = 3; i < es.length - 1; i++) {
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


            searchKey: function () {
                var keyword = self.refs.search_key.value;

            }


        }

        //マウント時に初期化
        this.on('mount', self._func.init);

    </script>


</yml-file-list>