var OptsMixin = {

    /**
     * JSONデータをkey(label)を指定してソートします
     * @param key
     * @param order
     * @returns {Function}
     */
    sortJson: function (key, order) {
        return function (a, b) {

            var order_by = 1;
            if (order.match('asc')) {
                order_by = 1;
            } else {
                order_by = -1;
            }
            if (a[key] > b[key]) {
                return order_by;
            }
            if (a[key] < b[key]) {
                return order_by * -1;
            }
        };
    },

    sortReset: function (soIcon, soState, soLength) {
        for (var i = 0; i < soLength; i++) {
            soIcon[i] = 'glyphicon glyphicon-sort';
            soState[i] = 1;
        }
        var reData = [soIcon, soState];
        return reData;
    },

    /**
     * データをソートして返します
     * @param clickName クリックした値
     * @param heCount 表の項目数
     * @param icon アイコン画像のクラスの名前
     * @param soState 各ソートステータスの状況
     * @param data 表のデータ
     * @returns {[icon,soState,data]}
     */
    sortData: function (clickName, heCount, icon, soState, data) {
        console.log('sortData')
        var resetSort = [],
            self = this;
        for (var i = 0; i < heCount.length; i++) {
            if (clickName == heCount[i]) {
                switch (soState[i]) {
                    case 1:
                        resetSort = self.sortReset(icon, soState, heCount.length);
                        icon = resetSort[0];
                        soState = resetSort[1];
                        icon[i] = 'glyphicon glyphicon-sort-by-attributes'
                        soState[i] = 2;
                        data.sort(self.sortJson(clickName, 'asc'));
                        break;

                    case 2:
                        resetSort = self.sortReset(icon, soState, heCount.length);
                        icon = resetSort[0];
                        soState = resetSort[1];
                        icon[i] = 'glyphicon glyphicon-sort-by-attributes-alt'
                        soState[i] = 3;
                        data.sort(self.sortJson(clickName, 'desc'));
                        break;

                    case 3:
                        resetSort = self.sortReset(icon, soState, heCount.length);
                        icon = resetSort[0];
                        soState = resetSort[1];
                        icon[i] = 'glyphicon glyphicon-sort-by-attributes'
                        soState[i] = 2;
                        data.sort(self.sortJson(clickName, 'asc'));
                        break;

                }
            }
        }
        var setData = [icon, soState, data];
        console.log(setData);

        return setData;
    },


    /**
     * クリックしたページの番号に応じて表示するページネーションを変更する
     * @param pL そのデータの最大ページ数
     * @param pN　現在のページの番号
     * @returns {Array}
     */
    pageCheck: function (pL, pN) {
        var pageLabel = [];
        if (pL < 5) {
            for (var i = 0; i < pL; i++) {
                pageLabel[i] = {
                    label: i + 1, pageClass: null
                };
                if(pageLabel[i].label==pN){
                    pageLabel[i].pageClass='active';
                }
            }
        } else {
            if (pN < 3 || pL - pN < 2) {
                if (pN < 3) {
                    for (var i = 0; i < 5; i++) {
                        pageLabel[i] = {
                            label: i + 1, pageClass: null
                        };
                    }
                    ;
                } else {
                    for (var i = 0; i < 5; i++) {
                        pageLabel[i] = {
                            label: pL - 5 + 1 + i, pageClass: null
                        };
                    }
                    ;
                }
            } else {
                for (var i = 0; i < 5; i++) {
                    pageLabel[i] = {
                        label: pN - 2 + i, pageClass: null
                    }
                }
            }

            for(var i=0;i<5;i++){
                if(pageLabel[i].label==pN){
                    pageLabel[i].pageClass='active';
                }
            }
        }
        return pageLabel;
    },

    /**
     * ページネーション用のクラス
     * @param buttonId クリックしたボタンの番号
     * @param paNum 現在のページ数
     * @param pageLimit ページの最大項目数
     * @returns {[null,null]}
     */
    pageJump: function (buttonId, paNum, pageLimit) {
        var paLab = [],
            self = this;
        console.log('paNum:',paNum)
        if (buttonId == 'previous' && paNum > 1) {
            paNum--;
            paLab = self.pageCheck(pageLimit, paNum);
        } else if (buttonId == 'next' && paNum < pageLimit) {
            paNum++;
            paLab = self.pageCheck(pageLimit, paNum);
        } else if (!(buttonId == 'previous') && !(buttonId == 'next')) {
            paNum = buttonId;
            paLab = self.pageCheck(pageLimit, buttonId);
        }else{
            paLab = self.pageCheck(pageLimit, paNum);
        }
        var returnPaLab = [paNum, paLab];
        return returnPaLab;
    }
}
