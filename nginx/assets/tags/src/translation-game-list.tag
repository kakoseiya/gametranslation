<translation-game-list>
    <div class="container">
        <div class="row">

            <!--検索機能を追加する(未実装)-->
            <div class="col-md-12">
                <form>
                    <input type="text" id="search_key" placeholder="検索ワードを入力" 　ref="search_key">
                </form>
            </div>
        </div>
        <br>
        <div class="row">
            <div class="col-md-12" each="{item,i in status._data}">
                <div class="card">
                    <div class="card-body">
                        <h4 class="card-title list-pointer"
                            onclick="{_func.viewFileList}"
                        id="view_{item.publicId}">
                            {item.name}
                        </h4>
                        <h6 class="card-subtitle mb-2 text-muted">{item.version}</h6>
                        <p class="card-text">Some quick example text to build on the card title and make up the bulk of
                            the card's content.</p>
                        <a href="#" class="card-link">Card link</a>
                        <a href="#" class="card-link">Another link</a>
                    </div>
                </div>
                <br>
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
                        console.log('response:', response)
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
                var es = e.target.id.split('_');
                var soData = self.sortData(es[1], self.status.headingName, self.status.sortIcon, self.status.sortState, self.status._data);
                self.status.sortIcon = soData[0];
                self.status.sortState = soData[1];
                self.status.setData(soData[2]);
                self.update();
            },

            viewFileList: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                console.log(e.target.id);
                var splitId = e.target.id.split('_'),
                    api = '/game/file' + '?id=' + splitId[1];
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
                var splitId = e.target.id,
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
                if (self.status.edit) {
                    self.status.editOff();
                    self.update();
                } else {
                    self.status.newCreateOff();
                    console.log('editGame:', e);
                    var es = e.target.id.split("_");
                    self.status.setIdNameVersion(es[1], es[2], es[3]);
                    self.status.editOn();
                    self.update();
                }
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

        riot.mount('game-item');


    </script>

</translation-game-list>