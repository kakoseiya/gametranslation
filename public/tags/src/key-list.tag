<key-list>
    <div id="key-list-table">
        <table id="key-table" class="table table-hover">
            <thead>
            <tr>
                <th onclick="{_func.sortKeyList}" id="sort_{headingName[0]}" ref="{headingName[0]}">File
                    Name
                </th>
            </tr>
            </thead>
            <tbody>
            <tr each="{item, i in key_data}" id="{item.publicId}">
                <th onclick="{_func.viewDocumentEditor}" id="name_{item.publicId}">{item.name}</th>
            </tr>
            </tbody>
        </table>
    </div>

    <script>
        var self = this,
            ymlFilePublicId = opts.publicId;
        api = '/key/' + ymlFilePublicId,
            opt = {
                method: 'GET',
                credentials: 'include'
            };

        this.sortState = [];
        this.key_data;
        this.mixin(OptsMixin);
        this.headingName=[];
        this.headingName[0] = 'name';

        fetch(api, opt)
            .then(function (response) {
                return response.json();
            })
            .then(function (json) {
                self.key_data = json.data;
                for (var i = 0; i < self.headingName.length; i++) {
                    self.sortState[i] = 1;
                }
                self.update();
            })
            .catch(function (error) {
                console.log('error:', error);
            });


        this._func = {
            sortKeyList: function (e) {
                e.preventDefault();
                var es = e.target.id.split('_');
                for (var i = 0; i < self.sortState.length; i++) {
                    if (es[1] == self.headingName[i]) {
                        switch (self.sortState[i]) {
                            case 1:
                                self.sortState[i] = 2;
                                self.key_data.sort(self.sortJson(es[1], 'asc'));
                                self.update();
                                break;
                            case 2:
                                self.sortState[i] = 3;
                                self.key_data.sort(self.sortJson(es[1], 'desc'));
                                self.update();
                                break;
                            case 3:
                                self.sortState[i] = 2;
                                self.key_data.sort(self.sortJson(es[1], 'asc'));
                                self.update();
                                break;
                        }
                    }
                }
            },
            viewDocumentEditor: function (e) {
                e.preventDefault();
            }
        }
    </script>

</key-list>