riot.tag2('administrator-user-list', '<div> <div class="row"> <div class="col-md-12"> <div class="row"> <div class="col-md-6"> <button class="btn btn-primary list-pointer" data-toggle="modal" data-target="#document_admin_Modal" onclick="{modal.modalOnCreate}"> <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新規作成 </button> </div> <div class="col-md-6"> <form> <input type="text" placeholder="検索ワードを入力" ref="search_key"> </form> </div> </div> <br> </div> <table class="table table-hover col-md-12"> <thead> <tr class="row"> <th onclick="{_func.sortDocumentList}" id="sort_origText" ref="origText" class="list-pointer col-md-4"> ユーザー名 <span class="{status.sortIcon[0]}" aria-hidden="true" aria-label="Right Align"></span> </th> <th onclick="{_func.sortDocumentList}" id="sort_transText" ref="transText" class="list-pointer col-md-3"> ユーザー権限 <span class="{status.sortIcon[1]}" aria-hidden="true" aria-label="Right Align"></span> </th> <th onclick="{_func.sortDocumentList}" id="sort_remarks" ref="remarks" class="list-pointer col-md-2"> 状態 <span class="{status.sortIcon[2]}" aria-hidden="true" aria-label="Right Align"></span> </th> <th onclick="{_func.sortDocumentList}" class="col-md-3"> <span class="{status.sortIcon[2]}" aria-hidden="true" aria-label="Right Align"></span> </th> </tr> </thead> <tbody> <tr each="{item, i in status._data}" name="{item.publicId}" class="list-pointer row"> <th onclick="{modal.modalOn}" class="col-md-4" id="view_origText_{item.publicId}" data-toggle="modal" data-target="#document_admin_Modal"> {item.userName} </th> <th onclick="{modal.modalOn}" class="col-md-3" id="view_transText_{item.publicId}" data-toggle="modal" data-target="#document_admin_Modal"> {status.userPrivilege(item.userPrivileges)} </th> <th onclick="{modal.modalOn}" class="col-md-2" id="view_remarks_{item.publicId}" data-toggle="modal" data-target="#document_admin_Modal"> {status.userDeleted(item.deleted)} </th> <th class="col-md-3"> <button class="btn btn-primary btn-sm" id="admin_{item.publicId}" onclick="{_func.editUser}"> <span class="glyphicon glyphicon-wrench" aria-hidden="true" aria-label="Right Align" id="adminspan_{item.publicId}"></span> 編集 </button> <button class="btn btn-primary btn-sm" id="delete_{item.publicId}" onclick="{_func.deleteUser}"> <span class="glyphicon glyphicon-trash" aria-hidden="true" aria-label="Right Align" id="deletespan_{item.publicId}"></span> 削除 </button> </th> </tr> </tbody> </table> </div> <div class="modal fade" id="document_admin_Modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true"> <div class="modal-dialog modal-lg" role="document"> <div class="modal-content"> <div class="modal-header"> <h5 class="modal-title" id="exampleModalLongTitle">Modal title</h5> <button type="button" class="close list-pointer" data-dismiss="modal" aria-label="Close"> <span aria-hidden="true">&times;</span> </button> </div> <div class="modal-body"> <form onsubmit="{_func.saveDocument}"> <input if="{!status.newFlag}" type="hidden" name="publicId" riot-value="{modal._document.publicId}"> <input type="hidden" name="ymlFileId" riot-value="{opts.publicid}"> <div class="row"> <div class="form-group col-md-12"> <label>Key:</label> <input required type="text" class="form-control" riot-value="{modal._document.key}" name="key"> </div> </div> <div class="row"> <div class="form-group col-md-6"> <div>原文</div> <textarea name="origText" rows="14" class="form-control">{modal._document.origText}</textarea> </div> <div class="col-md-6"> <div class="form-group col-md-12"> <div>Google翻訳</div> <textarea name="transGoogleText" rows="6" class="form-control">{modal._document.transGoogleText}</textarea> </div> <div class="form-group col-md-12"> <div>翻訳</div> <textarea name="transText" rows="6" class="form-control">{modal._document.transText}</textarea> </div> </div> </div> <div class="row"> <div class="form-group col-md-12"> <label>備考欄:</label> <input required type="text" class="form-control" riot-value="{modal._document.remarks}" name="remarks"> </div> </div> <div class="row"> <div class="form-group col-md-12"> <input type="radio" name="status" value="50" checked="{checked:modal.checkStatus()}">翻訳完了 <input type="radio" name="status" value="0" checked="{checked:!modal.checkStatus()}">未完了 </div> </div> <div class="modal-footer"> <button type="button" class="btn btn-secondary list-pointer" data-dismiss="modal"> Close </button> <button type="submit" class="btn btn-primary list-pointer">Save changes</button> </div> </form> </div> </div> </div> </div> </div>', 'administrator-user-list .list-pointer,[data-is="administrator-user-list"] .list-pointer{ cursor: pointer; }', '', function(opts) {

        var self = this;
        this.mixin(OptsMixin);

        this.status = {
            _data: [],
            headingName: [],

            sortState: [],
            sortIcon: [],
            edit: false,
            newCreate: false,
            editName: null,
            editVersion: null,

            pageCount: 10,

            pageMaxNation: 5,

            pageMax: null,

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

        this.on('mount', self._func.init);

});
riot.tag2('debug-login-signup-pregisted', '<a href="{api}">{opts.user}</a>', '', '', function(opts) {
        this.api="/signup/regist?hash="+opts.user

});
riot.tag2('document-list', '<div class="container"> <div class="row"> <div class="col-xs-12"> <form> <input type="text" placeholder="検索ワードを入力" ref="search_key"> </form> </div> <div class="col-xs-12"> <table class="table table-hover"> <thead> <tr> <th onclick="{_func.sortDocumentList}" id="sort_origText" ref="origText" class="list-pointer"> 原文 <span class="{status.sortIcon[0]}" aria-hidden="true" aria-label="Right Align"></span> </th> <th onclick="{_func.sortDocumentList}" id="sort_transText" ref="transText" class="list-pointer"> 翻訳文(確定) <span class="{status.sortIcon[1]}" aria-hidden="true" aria-label="Right Align"></span> </th> <th onclick="{_func.sortDocumentList}" id="sort_remarks" ref="remarks" class="list-pointer"> 備考 <span class="{status.sortIcon[2]}" aria-hidden="true" aria-label="Right Align"></span> </th> <th ref="edit_delete"></th> </tr> </thead> <tbody> <tr each="{item, i in status.getData()}" name="{item.publicId}" if="{status.pageLabelCheck(status.pageIdLabel[i].label)}" class="list-pointer"> <th onclick="{modal.modalOn}" id="view_origText_{item.publicId}" class="popup-modal"> {item.origText} </th> <th onclick="{modal.modalOn}" id="view_transText_{item.publicId}" class="popup-modal"> {item.transText} </th> <th onclick="{modal.modalOn}" id="view_remarks_{item.publicId}" class="popup-modal"> {item.remarks} </th> </tr> </tbody> </table> </div> <div class="col-xs-12"> <ul class="pagination"> <li id="page_li_previous" onclick="{_func.pageNation}"> <a href="#" aria-label="Previous" id="page_a_previous"> <span aria-hidden="true" id="page_span_previous"> &laquo; </span> </a> </li> <li each="{page,i in status.getPage()}" id="page_li_{page.label}" class="{page.pageClass} " onclick="{_func.pageNation}"> <a href="#" id="page_a_{page.label}"> <span aria-hidden="true" id="page_span_{page.label}"> {page.label} </span> </a> </li> <li id="page_li_next" class="pageL1" onclick="{_func.pageNation}"> <a href="#" aria-label="Next" id="page_a_next"> <span aria-hidden="true" id="page_span_next"> &raquo; </span> </a> </li> </ul> </div> </div> </div> <div class="white-popup-block mfp-hide" id="document-popup-modal"> <div class="row"> <p class="col-xs-12">「閉じる」か「背景」をクリックするとモーダルウィンドウを終了します。</p> <form> <div class="form-group col-xs-12"> <label>Key:</label> <input required type="text" class="form-control" riot-value="{modal._document.key}" name="key"> </div> <div class="col-xs-6"> <div> 原文 </div> <textarea name="origText" row="12" class="form-control orig-text"> {modal._document.origText} </textarea> </div> <div class="col-xs-6"> <div class="col-xs-12"> <div> Google翻訳文 </div> <textarea name="transGoogleText" row="6" class="form-control"> {modal._document.transGoogleText} </textarea> </div> <div class="col-xs-12"> <div> 翻訳文 </div> <textarea name="transText" row="6" class="form-control"> {modal._document.transText} </textarea> </div> </div> <div class="col-xs-12"> <label>備考欄:</label> <input required type="text" class="form-control" riot-value="{modal._document.remarks}" name="remarks"> </div> <div></div> <button class="btn btn-primary">保存</button> <button class="btn btn-warning" onclick="{modal.modalOff}">キャンセル</button> </form> </div> </div>', 'document-list .list-pointer,[data-is="document-list"] .list-pointer{ cursor: pointer; } document-list .orig-text,[data-is="document-list"] .orig-text{ height:100%; }', '', function(opts) {
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

        this.status = {
            _data: [],
            headingName: [],

            sortState: [],
            sortIcon: [],
            edit: false,
            newCreate: false,
            publicYmlFileId: null,
            publicDocumentId: null,
            editName: null,
            editVersion: null,

            pageCount: 5,

            pageMaxNation: 5,

            pageMax: null,

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

        this.on('mount', self._func.init);

});
riot.tag2('footer-list', '<footer class="footer"> <div class="container"> <p class="text-muted">GAME TRANSLATION</p> </div> </footer>', 'footer-list html,[data-is="footer-list"] html{ position: relative; min-height: 100%; } footer-list body,[data-is="footer-list"] body{ padding-top: 70px; margin-bottom: 60px; } footer-list .footer,[data-is="footer-list"] .footer{ position: absolute; bottom: 0; width: 100%; height: 60px; background-color: #f5f5f5; } footer-list .container,[data-is="footer-list"] .container{ width: auto; max-width: 680px; padding: 0 15px; } footer-list .container .text-muted,[data-is="footer-list"] .container .text-muted{ margin: 20px 0; }', '', function(opts) {
});
riot.tag2('header-game', '<nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark"> <a class="navbar-brand" href="#">Game Translation</a> <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation"> <span class="navbar-toggler-icon"></span> </button> <div class="collapse navbar-collapse" id="navbarCollapse"> </div> </nav>', '', '', function(opts) {
        var self = this;

        this.status = {
            navtype: opts.navtype,

            navCheck: function (nt) {
                if (nt === self.status.navtype) {
                    return true;
                } else {
                    return false;
                }
            }
        }

});
riot.tag2('header-nav', '<nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark"> <a class="navbar-brand" href="#">Game Translation</a> <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation"> <span class="navbar-toggler-icon"></span> </button> <div class="collapse navbar-collapse" id="navbarCollapse"> <ul class="navbar-nav mr-auto"> <li class="nav-item {active:status.navCheck(\'game\')}"> <a class="nav-link" href="/game">Game <span if="{status.navCheck(game)}" class="sr-only">(current)</span> </a> </li> <li class="nav-item {active:status.navCheck(\'mypage\')}" if="{status.userCheck()}"> <a class="nav-link" href="/mypage">MyPage <span if="{status.navCheck(mypage)}" class="sr-only">(current)</span> </a> </li> <li class="nav-item {active:status.navCheck(\'contact\')}" if="{status.userCheck()}"> <a class="nav-link" href="#">Contact <span if="{status.navCheck(contact)}" class="sr-only">(current)</span> </a> </li> <li class="nav-item {active:status.navCheck(\'system\')}" if="{status.systemUserCheck()}"> <a class="nav-link" href="/system">System <span if="{status.navCheck(contact)}" class="sr-only">(current)</span> </a> </li> </ul> <span class="navbar-text"> ようこそ{status.userData.userName}様 </span> &nbsp;&nbsp; <button class="btn btn-outline-success my-2 my-sm-0 list-pointer" onclick="{_func.logout}"> Logout </button> </div> </nav>', '', '', function(opts) {
        var self = this;

        this.status = {
            userData: null,

            navCheck: function (nt) {
                return (nt === opts.navtype)
            },

            userCheck: function () {
                return (!(self.status.userData.userPrivileges === 0));
            },

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

});
riot.tag2('login-sign-up', '<div class="container"> <div class="row"> <div class="col-md-2"></div> <div class="card card-container col-md-8"> <br> <form class="form-signin" onsubmit="{_func.signUp}"> <div class="form-group row"> <label class="col-md-3 col-form-label">ユーザー名</label> <div class="col-md-9"> <input type="text" class="form-control" name="userName" ref="userName" placeholder="Name" required> </div> </div> <div class="form-group row"> <label class="col-md-3 col-form-label">Email</label> <div class="col-md-9"> <input class="form-control" name="mail" ref="mail" placeholder="Email" required type="email"> </div> </div> <div class="form-group row"> <label class="col-md-3 col-form-label">パスワード</label> <div class="col-md-9"> <input type="password" class="form-control" name="pass" ref="pass" placeholder="8桁のパスワード" required> </div> </div> <div class="form-group row"> <label class="col-md-3 col-form-label">パスワード(確認)</label> <div class="col-md-9"> <input type="password" class="form-control" name="rePass" ref="rePass" placeholder="パスワード(確認)" required> </div> </div> <div class="form-group row"> <label class="col-3 col-form-label">生年月日</label> <div class="col-9"> <input class="form-control" name="birthday" ref="birthday" required type="date"> </div> </div> <div class="form-group row"> <label class="col-3 col-form-label">電話番号</label> <div class="col-9"> <input type="tel" class="form-control" name="mobileNumber" ref="mobileNumber" placeholder="00000000"> </div> </div> <div class="row"> <div class="col-md-9 offset-md-3"> <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#confirmationModal" onclick="{_func.confirm}">&nbsp&nbsp確認&nbsp&nbsp </button> <button type="button" class="btn btn-primary" onclick="{_func.cancel}">キャンセル</button> </div> </div> <div class="modal fade" id="confirmationModal" tabindex="-1" role="dialog" aria-labelledby="confirmationModalLabel" aria-hidden="true"> <div class="modal-dialog" role="document"> <div class="modal-content"> <div class="modal-header"> <h5 class="modal-title" id="confirmationModalLabel">確認フォーム</h5> <button type="button" class="close" data-dismiss="modal" aria-label="Close"> <span aria-hidden="true">&times;</span> </button> </div> <div class="modal-body"> <table class="table"> <tbody> <tr> <td>ユーザー名</td> <td>{refs.userName.value}</td> </tr> <tr> <td>Email</td> <td>{refs.mail.value}</td> </tr> <tr> <td>パスワード</td> <td>{refs.pass.value}</td> </tr> <tr> <td>生年月日</td> <td>{refs.birthday.value}</td> </tr> <tr> <td>電話番号</td> <td>{refs.mobileNumber.value}</td> </tr> </tbody> </table> </div> <div class="modal-footer"> <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button> <button type="submit" class="btn btn-primary">Save changes</button> </div> </div> </div> </div> </form> <br> </div> <div class="col-md-2"></div> </div> </div>', '', '', function(opts) {
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
});
riot.tag2('login-top', '<div class="container"> <div class="row"> <div class="col-md-3"></div> <div class="card card-container col-md-6"> <h1>ログイン</h1> <form class="form-signin" onsubmit="{_func.login}"> <div class="form-group"> <input id="inputEmail" name="mail" class="form-control" placeholder="Email address" required autofocus type="email"> </div> <div class="form-group"> <input type="password" id="inputPassword" name="pass" class="form-control" placeholder="Password" required> </div> <button class="btn btn-lg btn-primary btn-block btn-signin list-pointer" type="submit">ログイン</button> </form> <br> <a href="#" class="forgot-password"> Forgot the password? </a> <hr> <div> <form onsubmit="{_func.login}"> <input type="hidden" name="mail" value="kakoseiya@gmail.com"> <input type="hidden" name="pass" value="guest04140414"> <button class="btn btn-primary list-pointer" type="submit"> ゲストでログインする </button> <button class="btn btn-primary list-pointer" type="button" onclick="{_func.createUser}"> 新規作成 </button> </form> </div> <br> </div> <div class="col-md-3"></div> </div> </div>', '', '', function(opts) {
        var self = this;

        this._func = {
            createUser: function () {
                location.href = "/signup"
            },
            login: function (e) {
                e.preventUpdate = true;
                e.preventDefault();
                console.log("e:", e)
                var form = new FormData(e.target),
                    api = "/login",
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
                        if (json.code === 200) {
                            location.href = json.message;
                        }
                    })
                    .catch(function (error) {
                        console.log('Save Error:', error);
                    })
            }
        }

});
riot.tag2('translation-document', '<div> <div class="row"> <div class="col-md-12"> <div class="row"> <div class="col-md-6"> <button class="btn btn-primary list-pointer" data-toggle="modal" data-target="#document_admin_Modal" onclick="{modal.modalOnCreate}"> <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新規作成 </button> </div> <div class="col-md-6"> <form> <input type="text" placeholder="検索ワードを入力" ref="search_key"> </form> </div> </div> <br> </div> <table class="table table-hover col-md-12"> <thead> <tr class="row"> <th onclick="{_func.sortDocumentList}" id="sort_origText" ref="origText" class="list-pointer col-md-6"> 原文 <span class="{status.sortIcon[0]}" aria-hidden="true" aria-label="Right Align"></span> </th> <th onclick="{_func.sortDocumentList}" id="sort_transText" ref="transText" class="list-pointer col-md-3"> 翻訳文 <span class="{status.sortIcon[1]}" aria-hidden="true" aria-label="Right Align"></span> </th> <th onclick="{_func.sortDocumentList}" id="sort_remarks" ref="remarks" class="list-pointer col-md-3"> 備考 <span class="{status.sortIcon[2]}" aria-hidden="true" aria-label="Right Align"></span> </th> </tr> </thead> <tbody> <tr each="{item, i in status.getData()}" name="{item.publicId}" class="list-pointer row"> <th onclick="{modal.modalOn}" class="col-md-6" id="view_origText_{item.publicId}" data-toggle="modal" data-target="#document_admin_Modal"> {item.origText} </th> <th onclick="{modal.modalOn}" class="col-md-3" id="view_transText_{item.publicId}" data-toggle="modal" data-target="#document_admin_Modal"> {item.transText} </th> <th onclick="{modal.modalOn}" class="col-md-3" id="view_remarks_{item.publicId}" data-toggle="modal" data-target="#document_admin_Modal"> {item.remarks} </th> </tr> </tbody> </table> </div> <div class="modal fade" id="document_admin_Modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true"> <div class="modal-dialog modal-lg" role="document"> <div class="modal-content"> <div class="modal-header"> <h5 class="modal-title" id="exampleModalLongTitle">Modal title</h5> <button type="button" class="close list-pointer" data-dismiss="modal" aria-label="Close"> <span aria-hidden="true">&times;</span> </button> </div> <div class="modal-body"> <form onsubmit="{_func.saveDocument}"> <input if="{!status.newFlag}" type="hidden" name="publicId" riot-value="{modal._document.publicId}"> <input type="hidden" name="ymlFileId" riot-value="{opts.publicid}"> <div class="row"> <div class="form-group col-md-12"> <label>Key:</label> <input required type="text" class="form-control" riot-value="{modal._document.key}" name="key"> </div> </div> <div class="row"> <div class="form-group col-md-6"> <div>原文</div> <textarea name="origText" rows="14" class="form-control">{modal._document.origText}</textarea> </div> <div class="col-md-6"> <div class="form-group col-md-12"> <div>Google翻訳</div> <textarea name="transGoogleText" rows="6" class="form-control">{modal._document.transGoogleText}</textarea> </div> <div class="form-group col-md-12"> <div>翻訳</div> <textarea name="transText" rows="6" class="form-control">{modal._document.transText}</textarea> </div> </div> </div> <div class="row"> <div class="form-group col-md-12"> <label>備考欄:</label> <input required type="text" class="form-control" riot-value="{modal._document.remarks}" name="remarks"> </div> </div> <div class="row"> <div class="form-group col-md-12"> <input type="radio" name="status" value="50" checked="{checked:modal.checkStatus()}">翻訳完了 <input type="radio" name="status" value="0" checked="{checked:!modal.checkStatus()}">未完了 </div> </div> <div class="modal-footer"> <button type="button" class="btn btn-secondary list-pointer" data-dismiss="modal"> Close </button> <button type="submit" class="btn btn-primary list-pointer">Save changes</button> </div> </form> </div> </div> </div> </div> </div>', 'translation-document .list-pointer,[data-is="translation-document"] .list-pointer{ cursor: pointer; }', '', function(opts) {

        var self = this;
        this.mixin(OptsMixin);

        this.status = {
            _data: [],
            headingName: [],

            sortState: [],
            sortIcon: [],
            edit: false,
            newCreate: false,
            publicYmlFileId: opts.publicid,
            publicDocumentId: null,
            editName: null,
            editVersion: null,

            pageCount: 5,

            pageMaxNation: 5,

            pageMax: null,

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

        this.on('mount', self._func.init);

});
riot.tag2('translation-game-document', '<div class="container"> <nav aria-label="パンくずリスト"> <ol class="breadcrumb mb-1"> <li class="breadcrumb-item"><a href="/game">Game</a></li> <li class="breadcrumb-item"><a href="/game/file?id={status.gamePublicId}">Ymlファイル</a></li> <li class="breadcrumb-item active" aria-current="page">ドキュメント</li> </ol> </nav> <br> <nav class="nav nav-tabs" id="myTab" role="tablist"> <a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true"> List </a> <a class="nav-item nav-link" id="nav-profile-tab" data-toggle="tab" href="#nav-profile" role="tab" aria-controls="nav-profile" aria-selected="false"> 概要 </a> <a class="nav-item nav-link" id="nav-contact-tab" data-toggle="tab" href="#nav-contact" role="tab" aria-controls="nav-contact" aria-selected="false"> Chat </a> </nav> <div class="tab-content" id="nav-tabContent"> <div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab"> <br> <translation-document publicid="{opts.publicid}"></translation-document> </div> <div class="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab"> dddddddddddd... </div> <div class="tab-pane fade" id="nav-contact" role="tabpanel" aria-labelledby="nav-contact-tab"> vvvvvvvvvsssssss... </div> </div> </div>', '', '', function(opts) {
        var self = this;
        this.status = {
            gamePublicId: null,
            ymlFilePublicId: opts.publicid,
            fileTag: false,
            documentTag: false,
        };

        this._func = {
            init: function () {
                self.status.fileTag = true;
                self.status.documentTag = false;
                console.log('ssss', self.status.ymlFilePublicId)
                console.log('ssss', opts);
                var api = "/file?id="+self.status.ymlFilePublicId,
                    opt = {
                        method: "GET",
                        credentials: "include"
                    };
                console.log("api",api);
                fetch(api, opt)
                    .then(function (response) {
                        return response.json();
                    })
                    .then(function (json) {
                        console.log('json::',json)
                        self.status.gamePublicId = json.data.game.publicId;
                        console.log(self.status.gamePublicId);
                        self.update();
                    })
                    .catch(function (error) {
                        console.log('error:', error);
                    })

                riot.mount('translation-document');

            }
        };

        this.on('mount', this._func.init());

});
riot.tag2('translation-game-item', '<div class="card"> <div class="card-block"> <h4 class="card-title">{opts.name}</h4> <h6 class="card-subtitle mb-2 text-muted">Card subtitle</h6> <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card\'s content.</p> <a href="#" class="card-link">Card link</a> <a href="#" class="card-link">Another link</a> </div> </div>', 'translation-game-item .list-pointer,[data-is="translation-game-item"] .list-pointer{ cursor: pointer; }', '', function(opts) {
});
riot.tag2('translation-game-list', '<div class="container"> <div class="row"> <div class="col-md-12"> <form> <input type="text" id="search_key" placeholder="検索ワードを入力" ref="search_key"> </form> </div> </div> <br> <div class="row"> <div class="col-md-12" each="{item,i in status._data}"> <div class="card"> <div class="card-body"> <h4 class="card-title list-pointer" onclick="{_func.viewFileList}" id="view_{item.publicId}"> {item.name} </h4> <h6 class="card-subtitle mb-2 text-muted">{item.version}</h6> <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card\'s content.</p> <a href="#" class="card-link">Card link</a> <a href="#" class="card-link">Another link</a> </div> </div> <br> </div> </div> </div>', 'translation-game-list .list-pointer,[data-is="translation-game-list"] .list-pointer{ cursor: pointer; }', '', function(opts) {
        var self = this;
        this.mixin(OptsMixin);

        this.status = {
            _data: [],
            headingName: [],

            sortState: [],
            sortIcon: [],
            edit: false,
            newCreate: false,
            publicGameId: null,
            editName: null,
            editVersion: null,

            pageCount: 5,

            pageMaxNation: 5,

            pageMax: null,

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

        this.on('mount', self._func.init);

        riot.mount('game-item');

});
riot.tag2('translation-game-overview', '<div id="game_overview"></div>', '', '', function(opts) {
        var self = this;
        var obs = null;
        var md = window.markdownit();

        this.status = {
            gamePublicId: opts.gamepublicid,
            _data: null
        };

        this._func = {
            init: function () {
                var api = '/game/get?id=' + opts.gamepublicid,
                    opt = {
                        method: "GET",
                        credentials: "include",
                    };
                fetch(api, opt)
                    .then(function (response) {
                        return response.json();
                    })
                    .then(function (json) {
                        console.log('JSON')
                        self.status._data = json.data;
                    })
                    .then(function () {
                        var result = md.render(self.status._data.overview);
                        result = self._func.getHtml(result);
                        $('#game_overview').html(result);
                    })
                    .catch(function (error) {
                        console.log('ERROR:', error);
                    })

            },
            getHtml: function (selector) {
                var html = selector;
                html = html.replace(/&lt;/g, '<');
                html = html.replace(/&gt;/g, '>');
                html = html.replace(/&amp;/g, '&');
                return html;
            }
        };

        this.on('mount', self._func.init);

});
riot.tag2('translation-game-yml', '<div class="container"> <nav aria-label="パンくずリスト"> <ol class="breadcrumb mb-1"> <li class="breadcrumb-item"><a href="/game">Game</a></li> <li class="breadcrumb-item active" aria-current="page">Ymlファイル</li> </ol> </nav> <br> <nav class="nav nav-tabs" id="myTab" role="tablist"> <a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true"> List </a> <a class="nav-item nav-link" id="nav-profile-tab" data-toggle="tab" href="#nav-profile" role="tab" aria-controls="nav-profile" aria-selected="false"> 概要 </a> <a class="nav-item nav-link" id="nav-contact-tab" data-toggle="tab" href="#nav-contact" role="tab" aria-controls="nav-contact" aria-selected="false"> Chat </a> </nav> <div class="tab-content" id="nav-tabContent"> <div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab"> <br> <translation-yml-file publicid="{status.gamePublicId}"></translation-yml-file> </div> <div class="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab"> <translation-game-overview gamepublicid="{opts.publicid}"></translation-game-overview> </div> <div class="tab-pane fade" id="nav-contact" role="tabpanel" aria-labelledby="nav-contact-tab"> vvvvvvvvvsssssss... </div> </div> </div>', '', '', function(opts) {
        var self = this;
        var obs = null;

        this.status = {
            gamePublicId: opts.publicid,
            gameOverview: null,
            fileTag: false,
            documentTag: false,
        };

        this._func = {
            init: function () {
                self.status.fileTag = true;
                self.status.documentTag = false;
                obs = riot.observable();
                riot.mount('translation-yml-file');
                riot.mount('translation-game-overview');

            }
        };

        this.on('mount', this._func.init());

});
riot.tag2('translation-yml-file', '<div class="row"> <div class="col"> <form> <input type="text" placeholder="検索ワードを入力" ref="search_key"> </form> </div> <div class="col"> <button class="btn btn-primary" data-toggle="modal" data-target="#newCreateModal" onclick="{status.newCreateOn}"> <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新規作成 </button> </div> <br> </div> <div class="row"> <div class="col-md-12" each="{item,i in status._data}"> <div class="card"> <div class="card-body"> <h4 class="card-title" id="card_view_{item.publicId}" onclick="{_func.viewDocumentList}"> {item.name}</h4> <p class="card-text"> {item.summary} </p> <div class="progress"> <div class="progress-bar" role="progressbar" riot-style="width: {item.rate}%;" aria-valuenow="25" aria-valuemin="0" aria-valuemax="100"> {item.rate}% </div> </div> <a href="#" class="card-link" data-toggle="modal" data-target="#editModal" onclick="{_func.editYmlFile}" id="card_{item.publicId}_{item.name}"> 編集 </a> <a href="#" class="card-link">削除</a> <a href="#" class="card-link" download="{item.name}" id="download_{item.publicId}" onclick="{_func.downloadYml}">ダウンロード</a> </div> </div> <br> </div> <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="editModalLabel" aria-hidden="true"> <div class="modal-dialog" role="document"> <div class="modal-content"> <div class="modal-header"> <h5 class="modal-title" id="editModalLabel">Modal title</h5> <button type="button" class="close" data-dismiss="modal" aria-label="Close"> <span aria-hidden="true">&times;</span> </button> </div> <form class="form-horizontal" onsubmit="{_func.adminYmlFile}"> <div class="modal-body"> <input type="hidden" name="publicId" riot-value="{status.publicGameId}"> <div class="form-group"> <label class="control-label col-md-3">File Name</label> <div class="col-md-9"> <input type="text" class="form-control" name="title" placeholder="{status.editName}"> </div> </div> </div> <div class="modal-footer"> <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="{_func.cancel}"> Close </button> <button type="submit" class="btn btn-primary">Save changes</button> </div> </form> </div> </div> </div> <div class="modal fade" id="newCreateModal" tabindex="-1" role="dialog" aria-labelledby="newCreateModalLabel" aria-hidden="true"> <div class="modal-dialog" role="document"> <div class="modal-content"> <div class="modal-header"> <h5 class="modal-title" id="newCreateModalLabel">Modal title</h5> <button type="button" class="close" data-dismiss="modal" aria-label="Close"> <span aria-hidden="true">&times;</span> </button> </div> <form onsubmit="{_func.saveYmlFile}"> <div class="modal-body"> <input type="hidden" name="gamePublicId" riot-value="{opts.publicid}"> <div class="form-group row"> <label class="control-label col-md-3">File Name</label> <input type="text" class="form-control col-md-8" name="fileName" placeholder="File Name"> </div> <div class="form-group row"> <label class="control-label col-md-3">備考欄</label> <textarea name="summary" rows="2" class="form-control col-md-8"></textarea> </div> <div class="form-group row"> <label class="control-label col-md-3">ファイル</label> <div class="col-md-8"> <input type="file" name="YmlFile" accept=".yml"> </div> </div> </div> <div class="modal-footer"> <button type="button" class="btn btn-secondary" data-dismiss="modal" onclick="{_func.cancel}"> Close </button> <button type="submit" class="btn btn-primary">Save changes</button> </div> </form> </div> </div> </div> </div>', '', '', function(opts) {
        var self = this;
        this.mixin(OptsMixin);

        this.status = {
            _data: [],
            headingName: [],

            sortState: [],
            sortIcon: [],
            edit: false,
            newCreate: false,
            publicGameId: null,
            publicYmlFileId: null,
            editName: null,
            editVersion: null,

            pageCount: 5,

            pageMaxNation: 5,

            pageMax: null,

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

        this.on('mount', self._func.init);

});
riot.tag2('user-mypage-admin', '<table class="table table-bordered"> <tbody> <tr> <td>ユーザー名</td> <td>{status._data.userName}</td> </tr> <tr> <td>Email</td> <td>{status._data.mail}</td> </tr> <tr> <td>パスワード</td> <td>{status._data.pass}</td> </tr> <tr> <td>生年月日</td> <td>{status._data.birthDay[0]}/{status._data.birthDay[1]}/{status._data.birthDay[2]}</td> </tr> <tr> <td>電話番号</td> <td>{status._data.mobileNumber}</td> </tr> </tbody> </table> <button type="button" class="btn btn-primary list-pointer" data-toggle="modal" data-target="#user_admin_Modal"> 編集 </button> <button type="button" class="btn btn-primary list-pointer"> 退会申請 </button> <div class="modal fade" id="user_admin_Modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true"> <div class="modal-dialog modal-lg" role="document"> <div class="modal-content"> <div class="modal-header"> <h5 class="modal-title" id="exampleModalLongTitle">ユーザー編集</h5> <button type="button" class="close list-pointer" data-dismiss="modal" aria-label="Close"> <span aria-hidden="true">&times;</span> </button> </div> <div class="modal-body"> <form onsubmit="{_func.saveAccount}"> <input type="hidden" name="publicId" riot-value="{status._data.publicUserId}"> <div class="form-group row"> <label class="col-md-3 col-form-label">ユーザー名</label> <div class="col-md-9"> <input type="text" class="form-control" name="userName" ref="userName" riot-value="{status._data.userName}" required> </div> </div> <div class="form-group row"> <label class="col-md-3 col-form-label">Email</label> <div class="col-md-9"> <input class="form-control" name="mail" ref="mail" riot-value="{status._data.mail}" required type="{\'email\'}"> </div> </div> <div class="form-group row"> <label class="col-md-3 col-form-label">パスワード</label> <div class="col-md-9"> <input type="password" class="form-control" name="pass" ref="pass"> </div> </div> <div class="form-group row"> <label class="col-md-3 col-form-label">パスワード(確認)</label> <div class="col-md-9"> <input type="password" class="form-control" name="rePass" ref="rePass"> </div> </div> <div class="form-group row"> <label class="col-md-3 col-form-label">電話番号</label> <div class="col-md-9"> <input type="tel" class="form-control" name="mobileNumber" ref="mobileNumber" riot-value="{status._data.mobileNumber}" required> </div> </div> <div class="modal-footer"> <button type="button" class="btn btn-secondary list-pointer" data-dismiss="modal"> Close </button> <button type="submit" class="btn btn-primary list-pointer">Save changes</button> </div> </form> </div> </div> </div> </div>', '', '', function(opts) {
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

});
riot.tag2('user-mypage-info', '<table class="table table-bordered"> <tbody> <tr> <td>ユーザー名</td> <td>{status._data.userName}</td> </tr> <tr> <td>Email</td> <td>{status._data.mail}</td> </tr> <tr> <td>パスワード</td> <td>{status._data.pass}</td> </tr> <tr> <td>生年月日</td> <td>{status._data.birthDay[0]}/{status._data.birthDay[1]}/{status._data.birthDay[2]}</td> </tr> <tr> <td>電話番号</td> <td>{status._data.mobileNumber}</td> </tr> </tbody> </table> <button type="button" class="btn btn-primary list-pointer" data-toggle="modal" data-target="#user_admin_Modal"> 編集 </button> <button type="button" class="btn btn-primary list-pointer"> 退会申請 </button> <div class="modal fade" id="user_admin_Modal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle" aria-hidden="true"> <div class="modal-dialog modal-lg" role="document"> <div class="modal-content"> <div class="modal-header"> <h5 class="modal-title" id="exampleModalLongTitle">ユーザー編集</h5> <button type="button" class="close list-pointer" data-dismiss="modal" aria-label="Close"> <span aria-hidden="true">&times;</span> </button> </div> <div class="modal-body"> <form onsubmit="{_func.saveAccount}"> <input type="hidden" name="publicId" riot-value="{status._data.publicUserId}"> <div class="form-group row"> <label class="col-md-3 col-form-label">ユーザー名</label> <div class="col-md-9"> <input type="text" class="form-control" name="userName" ref="userName" riot-value="{status._data.userName}" required> </div> </div> <div class="form-group row"> <label class="col-md-3 col-form-label">Email</label> <div class="col-md-9"> <input class="form-control" name="mail" ref="mail" riot-value="{status._data.mail}" required type="{\'email\'}"> </div> </div> <div class="form-group row"> <label class="col-md-3 col-form-label">パスワード</label> <div class="col-md-9"> <input type="password" class="form-control" name="pass" ref="pass"> </div> </div> <div class="form-group row"> <label class="col-md-3 col-form-label">パスワード(確認)</label> <div class="col-md-9"> <input type="password" class="form-control" name="rePass" ref="rePass"> </div> </div> <div class="form-group row"> <label class="col-md-3 col-form-label">電話番号</label> <div class="col-md-9"> <input type="tel" class="form-control" name="mobileNumber" ref="mobileNumber" riot-value="{status._data.mobileNumber}" required> </div> </div> <div class="modal-footer"> <button type="button" class="btn btn-secondary list-pointer" data-dismiss="modal"> Close </button> <button type="submit" class="btn btn-primary list-pointer">Save changes</button> </div> </form> </div> </div> </div> </div>', '', '', function(opts) {
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

});
riot.tag2('user-mypage', '<div class="container"> <nav class="nav nav-tabs" id="myPageTab" role="tablist"> <a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab" aria-controls="nav-home" aria-selected="true"> ユーザー情報 </a> <a class="nav-item nav-link" id="nav-profile-tab" data-toggle="tab" href="#nav-profile" role="tab" aria-controls="nav-profile" aria-selected="false"> メッセージ </a> <a class="nav-item nav-link" id="nav-profile-tab2" data-toggle="tab" href="#nav-profile2" role="tab" aria-controls="nav-profile2" aria-selected="false"> メッセージ </a> </nav> <div class="tab-content" id="nav-tabContent"> <div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab"> <br> <user-mypage-info user="{opts.user}"></user-mypage-info> </div> <div class="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab"> dddddddddddd... </div> <div class="tab-pane fade" id="nav-profile2" role="tabpanel" aria-labelledby="nav-profile-tab2"> asdfsdafdddd... </div> </div> </div>', '', '', function(opts) {
        var self = this;
        this.mixin(OptsMixin);

});
riot.tag2('yml-file-list', '<div class="container"> <div class="row"> <div class="col-xs-12"> <form> <input type="text" placeholder="検索ワードを入力" ref="search_key"> </form> </div> <div class="col-xs-12"> <table class="table table-hover"> <thead> <tr> <th onclick="{_func.sortYmlFileList}" id="sort_name" ref="name" class="list-pointer"> File Name <span class="{status.sortIcon[0]}" aria-hidden="true" aria-label="Right Align"></span> </th> <th onclick="{_func.sortYmlFileList}" id="sort_summary" ref="summary" class="list-pointer"> 概要 <span class="{status.sortIcon[1]}" aria-hidden="true" aria-label="Right Align"></span> </th> <th onclick="{_func.sortYmlFileList}" id="sort_rate" ref="rate" class="list-pointer"> 翻訳率 [%] <span class="{status.sortIcon[2]}" aria-hidden="true" aria-label="Right Align"></span> </th> <th ref="edit_delete"></th> </tr> </thead> <tbody> <tr each="{item, i in status.getData()}" name="{item.publicId}" if="{status.pageLabelCheck(status.pageIdLabel[i].label)}" class="list-pointer"> <th onclick="{_func.viewDocumentList}" id="view_name_{item.publicId}">{item.name}</th> <th onclick="{_func.viewDocumentList}" id="view_summary_{item.publicId}">{item.summary}</th> <th onclick="{_func.viewDocumentList}" id="view_rate_{item.publicId}">{item.rate.toFixed(1)}</th> <th> <button class="btn btn-primary" id="admin_{item.publicId}_{item.name}_{item.version}" onclick="{_func.editYmlFile}"> <span class="glyphicon glyphicon-wrench" aria-hidden="true" aria-label="Right Align" id="adminspan_{item.publicId}_{item.name}_{item.version}"></span> 編集 </button> <button class="btn btn-primary" id="delete_{item.publicId}" onclick="{_func.deleteYmlFile}"> <span class="glyphicon glyphicon-trash" aria-hidden="true" aria-label="Right Align" id="deletespan_{item.publicId}"></span> 削除 </button> </th> </tr> </tbody> </table> </div> <div class="col-xs-12"> <ul class="pagination"> <li id="page_li_previous" onclick="{_func.pageNation}"> <a href="#" aria-label="Previous" id="page_a_previous"> <span aria-hidden="true" id="page_span_previous"> &laquo; </span> </a> </li> <li each="{page,i in status.getPage()}" id="page_li_{page.label}" class="{page.pageClass} " onclick="{_func.pageNation}"> <a href="#" id="page_a_{page.label}"> <span aria-hidden="true" id="page_span_{page.label}"> {page.label} </span> </a> </li> <li id="page_li_next" class="pageL1" onclick="{_func.pageNation}"> <a href="#" aria-label="Next" id="page_a_next"> <span aria-hidden="true" id="page_span_next"> &raquo; </span> </a> </li> </ul> </div> <div if="{!status.edit && !status.newCreate}" class="col-xs-6"> <button class="btn btn-primary" onclick="{status.newCreateOn}"> <span class="glyphicon glyphicon-plus" aria-hidden="true"></span> 新規作成 </button> </div> <div if="{!status.edit && status.newCreate}" class="col-xs-12"> <form class="form-horizontal" onsubmit="{_func.saveYmlFile}"> <div class="form-group"> <label class="control-label col-xs-3">File Name</label> <div class="col-xs-9"> <input type="text" class="form-control" name="title" placeholder="File Name"> </div> <br> <label class="control-label col-xs-3">ファイルアップロード</label> <div class="col-xs-9"> <input type="file" name="file"> </div> </div> <div class="col-xs-9 col-xs-offset-3"> <button type="submit" class="btn btn-primary glyphicon glyphicon-save">新規保存</button> <button class="btn btn-primary glyphicon glyphicon-remove" onclick="{status.newCreateOff}"> キャンセル </button> </div> </form> </div> <div if="{status.edit && !status.newCreate}" class="col-xs-12"> <form class="form-horizontal" onsubmit="{_func.adminYmlFile}"> <input type="hidden" name="publicId" riot-value="{status.publicGameId}"> <div class="form-group"> <label class="control-label col-xs-3">File Name</label> <div class="col-xs-9"> <input type="text" class="form-control" name="title" placeholder="{status.editName}"> </div> </div> <div class="col-xs-9 col-xs-offset-3"> <button type="submit" class="btn btn-primary"> <span class="glyphicon glyphicon-save" aria-hidden="true"></span> 保存 </button> <button class="btn btn-primary" onclick="{_func.cancel}"> <span class="glyphicon glyphicon-remove" aria-hidden="true"></span> キャンセル </button> </div> </form> </div> </div> </div>', 'yml-file-list .list-pointer,[data-is="yml-file-list"] .list-pointer{ cursor: pointer; }', '', function(opts) {
        var self = this;
        this.mixin(OptsMixin);

        this.status = {
            _data: [],
            headingName: [],

            sortState: [],
            sortIcon: [],
            edit: false,
            newCreate: false,
            publicGameId: null,
            publicYmlFileId: null,
            editName: null,
            editVersion: null,

            pageCount: 5,

            pageMaxNation: 5,

            pageMax: null,

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

        this.on('mount', self._func.init);

});