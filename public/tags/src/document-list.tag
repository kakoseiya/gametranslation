<document-list>
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
                        <th onclick="{_func.sortDocumentList}" id="sort_origText" ref="origText" class="list-pointer">
                            原文
                            <span class="{status.sortIcon[0]}" aria-hidden="true" aria-label="Right Align"></span>
                        </th>
                        <th onclick="{_func.sortDocumentList}" id="sort_transText" ref="transText" class="list-pointer">
                            翻訳文(確定)
                            <span class="{status.sortIcon[1]}" aria-hidden="true" aria-label="Right Align"></span>
                        </th>
                        <th onclick="{_func.sortDocumentList}" id="sort_remarks" ref="remarks" class="list-pointer">
                            備考
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

                        <th onclick="{modal.modalOn}" id="view_origText_{item.publicId}" class="popup-modal">
                            {item.origText}
                        </th>
                        <th onclick="{modal.modalOn}" id="view_transText_{item.publicId}" class="popup-modal">
                            {item.transText}
                        </th>
                        <th onclick="{modal.modalOn}" id="view_remarks_{item.publicId}" class="popup-modal">
                            {item.remarks}
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
        </div>
    </div>

    <!--モーダル-->
    <div class="white-popup-block mfp-hide" id="document-popup-modal">
        <div class="row">
            <p class="col-xs-12">「閉じる」か「背景」をクリックするとモーダルウィンドウを終了します。</p>
            <!-- 編集フォーム -->
            <form>
                <!-- Keyワード -->
                <div class="form-group col-xs-12">
                    <label>Key:</label>
                    <input required type="text"
                           class="form-control"
                           value="{modal._document.key}"
                           name="key"
                    >
                </div>

                <!-- 原文 -->
                <div class="col-xs-6">
                    <div>
                        原文
                    </div>
                    <textarea name="origText"
                              row="12"
                              class="form-control orig-text"
                    >
                        {modal._document.origText}
                    </textarea>
                </div>
                <div class="col-xs-6">
                    <!-- Google翻訳文 -->
                    <div class="col-xs-12">
                        <div>
                            Google翻訳文
                        </div>
                        <textarea name="transGoogleText"
                                  row="6"
                                  class="form-control"
                        >
                            {modal._document.transGoogleText}
                        </textarea>
                    </div>
                    <!-- 翻訳文 -->
                    <div class="col-xs-12">
                        <div>
                            翻訳文
                        </div>
                        <textarea name="transText"
                                  row="6"
                                  class="form-control"
                        >
                            {modal._document.transText}
                        </textarea>
                    </div>
                </div>
                <!-- 備考欄 -->
                <div class="col-xs-12">
                    <label>備考欄:</label>
                    <input required type="text"
                           class="form-control"
                           value="{modal._document.remarks}"
                           name="remarks"
                    >
                </div>
                <div></div>
                <button class="btn btn-primary">保存</button>
                <button class="btn btn-warning" onclick="{modal.modalOff}">キャンセル</button>
            </form>
        </div>
    </div>

    <style>
        .list-pointer {
            cursor: pointer;
        }
        .orig-text{
            height:100%;
        }
    </style>

    <script>
        $(document).ready(function () {
            $('.popup-modal').magnificPopup({
                items: {
                    src: '#document-popup-modal'
                },
                type: 'inline',
                preloader: false,
                focus: '#name',
                callbacks: {
                    beforeOpen: function () {
                        if ($(window).width() < 700) {
                            this.st.focus = false;
                        } else {
                            this.st.focus = '#name';
                        }
                    }
                }
            });
        });

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
            publicYmlFileId: null,
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

            modalOn: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                var es = e.target.id.split('_');
                for (var i = 0; i < self.status._data.length; i++) {
                    if (es[2] === self.status._data[i].publicId) {
                        self.modal._document = self.status._data[i];
                    }
                }
                self.update();
            },

            modalOff:function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                $.magnificPopup.close();
            }
        };

        this._func = {
            init: function () {
                self.status.publicYmlFileId = opts.publicId;
                var api = "/document/" + self.status.publicYmlFileId,
                    opt = {
                        method: "GET",
                        credentials: "include"
                    };
                self.status.headingName[0] = 'origText';
                self.status.headingName[1] = 'transText';
                self.status.headingName[2] = 'remarks';
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

            searchKey: function () {
                var keyword = self.refs.search_key.value;
            }
        }

        //マウント時に初期化
        this.on('mount', self._func.init);


    </script>


</document-list>