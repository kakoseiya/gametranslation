<translation-game-document>
    <div class="container">
        <nav aria-label="パンくずリスト">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="/game">Game</a></li>
                <li class="breadcrumb-item"><a href="/game/file?id={status.gamePublicId}">Ymlファイル</a></li>
                <li class="breadcrumb-item active" aria-current="page">ドキュメント</li>
            </ol>
        </nav>
        <br>
        <nav class="nav nav-tabs" id="myTab" role="tablist">
            <a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab"
               aria-controls="nav-home" aria-selected="true">
                List
            </a>
            <a class="nav-item nav-link" id="nav-profile-tab" data-toggle="tab" href="#nav-profile" role="tab"
               aria-controls="nav-profile" aria-selected="false">
                概要
            </a>
            <a class="nav-item nav-link" id="nav-contact-tab" data-toggle="tab" href="#nav-contact" role="tab"
               aria-controls="nav-contact" aria-selected="false">
                Chat
            </a>
        </nav>
        <div class="tab-content" id="nav-tabContent">
            <div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab">
                <br>
                <translation-document publicId="{opts.publicid}"></translation-document>
            </div>
            <div class="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab">
                dddddddddddd...

            </div>
            <div class="tab-pane fade" id="nav-contact" role="tabpanel" aria-labelledby="nav-contact-tab">
                vvvvvvvvvsssssss...
            </div>
        </div>
    </div>


    <script>
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

        //初期設定
        this.on('mount', this._func.init());


    </script>
</translation-game-document>