<game-list>
    <div class="container">
        <div class="row">

            <!--検索機能を追加する(未実装)-->
            <div class="col-xs-12">
                <form>
                    <input type="text" id="search_key" placeholder="検索ワードを入力" 　ref="search_key">
                </form>
            </div>

            <!--表を表示させる-->
            <div class="col-xs-12">
                <table class="table table-hover">
                    <thead>
                    <tr>
                        <th onclick="{_func.sortGameList}" name="name" ref="name" class="list-pointer">
                            Game Name
                            <span class="{status.sortIcon[0]}" aria-hidden="true" aria-label="Right Align"></span>
                        </th>
                        <th onclick="{_func.sortGameList}" name="version" ref="version" class="list-pointer">
                            Version
                            <span class="{status.sortIcon[1]}" aria-hidden="true" aria-label="Right Align"></span>
                        </th>
                        <th ref="edit_delete"></th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr each="{item, i in status.getData()}"
                        name="{item.publicId}"
                        if="{status.pageLabelCheck(status.pageIdLabel[i].label)}"
                        class="list-pointer">
                        <th onclick="{_func.viewFileList}" name="{item.publicId}">{item.name}</th>
                        <th onclick="{_func.viewFileList}" name="{item.publicId}">{item.version}</th>
                        <th>
                            <button class="btn btn-primary" id="admin_{item.publicId}_{item.name}_{item.version}"
                                    onclick="{_func.editGame}">
                                <span class="glyphicon glyphicon-wrench" aria-hidden="true"
                                      aria-label="Right Align"></span>
                                編集
                            </button>
                            <button class="btn btn-primary" name="{item.publicId}" onclick="{_func.deleteGame}">
                                <span class="glyphicon glyphicon-trash" aria-hidden="true"
                                      aria-label="Right Align"></span>
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
                    <li name="previous" onclick="{_func.pageNation}">
                        <a href="#" aria-label="Previous" name="previous">
                                <span aria-hidden="true" name="previous">
                                    &laquo;
                                </span>
                        </a>
                    </li>

                    <li each="{page,i in status.getPage()}" name="{page.label}"
                        class="{page.pageClass} " onclick="{_func.pageNation}">
                        <a href="#" name="{page.label}">
                                <span aria-hidden="true" name="{page.label}">
                                    {page.label}
                                </span>
                        </a>
                    </li>

                    <li name="next" class="pageL1" onclick="{_func.pageNation}">
                        <a href="#" aria-label="Next" name="next">
                                <span aria-hidden="true" name="next">
                                    &raquo;
                                </span>
                        </a>
                    </li>
                </ul>
            </div>

            <!--新規作成ボタン-->
            <div if="{!status.edit && !status.newCreate}" class="col-xs-12">
                <button class="btn btn-primary" onclick="{status.newCreateOn}">
                    <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
                    新規作成
                </button>
            </div>

            <!--新規作成用Form-->
            <div if="{!status.edit && status.newCreate}" class="col-xs-12">
                <form class="form-horizontal" onsubmit="{_func.saveGame}">
                    <div class="form-group">
                        <label class="control-label col-xs-3">Game Title</label>
                        <div class="col-xs-9">
                            <input type="text" class="form-control" name="title" placeholder="Game Title">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-3">Version</label>
                        <div class="col-xs-9">
                            <input type="text" class="form-control" name="version" placeholder="v1.4">
                        </div>
                    </div>
                    <div class="col-xs-12 col-xs-offset-3">
                        <button type="submit" class="btn btn-primary">新規保存</button>
                        <button class="btn btn-primary" onclick="{status.newCreateOff}">キャンセル</button>
                    </div>
                </form>

            </div>

            <!--編集用Form-->
            <div if="{status.edit && !status.newCreate}" class="col-xs-12">
                <form class="form-horizontal" onsubmit="{_func.adminGame}">
                    <input type="hidden" name="publicId" value="{status.publicGameId}">
                    <div class="form-group">
                        <label class="control-label col-xs-3">Game Title</label>
                        <div class="col-xs-9">
                            <input type="text" class="form-control" name="title" placeholder="{status.editName}">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-3">Version</label>
                        <div class="col-xs-9">
                            <input type="text" class="form-control" name="version" placeholder="{status.editVersion}">
                        </div>
                    </div>
                    <div class="col-xs-12 col-xs-offset-3">
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
                self.status.pageMax = parseInt(data.length / self.status.pageCount) + 1;
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

            setIdNameVersion: function (id, name, version) {
                self.status.publicGameId = id;
                self.status.editName = name;
                self.status.editVersion = version;
            },

            pageGameCheck: function () {
                self.status.pageLabel = self.pageCheck(self.status.pageMax, self.status.pageNumber);
                console.log(self.status.pageLabel);
            },

            getPage: function () {
                return self.status.pageLabel;
            },

            setPageIdLabel: function () {
                for (var i = 0; i < self.status._data.length; i++) {
                    self.status.pageIdLabel[i] = {
                        label: parseInt(i / self.status.pageCount) + 1,
                        id: self.status._data[i].publicId,
                    }
                }
                self.status.pageMax = parseInt(self.status._data.length / self.status.pageCount) + 1;
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
                var api = "/game/list",
                    opt = {
                        method: "GET",
                        credentials: "include"
                    };
                self.status.headingName[0] = 'name';
                self.status.headingName[1] = 'version';
                self.status.pageNumber = 1;
                for (var i = 0; i < self.status.headingName.length; i++) {
                    self.status.sortIcon[i] = 'glyphicon glyphicon-sort';
                }

                console.log('init:', self.status);
                fetch(api, opt)
                    .then(function (response) {
                        if (response.ok) {
                            response.json()
                                .then(function (json) {
                                    self.status.setData(json.data);
                                    console.log('setData:', self.status);
                                    self.status.pageGameCheck();
                                    self.status.setPageIdLabel();
                                    self.status.pageLabel[self.status.pageNumber - 1].pageClass = 'active';
                                    for (var i = 0; i < self.status.headingName.length; i++) {
                                        self.status.sortState[i] = 1;
                                    }

                                })
                                .then(function () {
                                    console.log(self.status.sortState);
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


            sortGameList: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                var es = e.target.name;
                console.log('es:sort:', es);
                var soData = self.sortData(es, self.status.headingName, self.status.sortIcon, self.status.sortState, self.status._data);
                self.status.sortIcon = soData[0];
                self.status.sortState = soData[1];
                self.status.setData(soData[2]);
                self.update();
            },

            viewFileList: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                console.log(e.target.id);
                var splitId = e.target.name,
                    api = '/game/file' + '?id=' + splitId;
                location.href = api;
            },

            saveGame: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                var form = new FormData(e.target),
                    api = "/game",
                    opt = {
                        method: "POST",
                        credentials: "include",
                        body: form
                    };
                console.log('Error:', e);
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

            deleteGame: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                var splitId = e.target.name,
                    api = '/game' + '?id=' + splitId[1],
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

            adminGame: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                var form = new FormData(e.target),
                    api = "/game",
                    opt = {
                        method: "POST",
                        credentials: "include",
                        body: form
                    };
                console.log('Error:', e);
                fetch(api, opt)
                    .then(function (response) {
                        return response.json()
                    })
                    .then(function (json) {
                        console.log('json:', json)
                        self.status.editOff();
                        self._func.init();
                        self.update();
                    })
                    .catch(function (error) {
                        console.log('Edit Save Error:', error);
                    })
            },

            editGame: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                console.log('editGame:', e);
                var es = e.target.id.split("_");
                self.status.setIdNameVersion(es[1], es[2], es[3]);
                self.status.editOn();
                self.update();
            },

            cancel: function () {
                self.status.edit = false;
                self.newCreate = false;
                self.status.publicGameId = null;
                self.status.editName = null;
                self.status.editVersion = null;
            },

            pageNation: function (e) {
                e.preventDefault();
                console.log('e:', e);
                var es = e.path[1].name;
                console.log('pageNation:', es);
                var paJump = self.pageJump(es, self.status.pageNumber, self.status.pageMax);
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

</game-list>