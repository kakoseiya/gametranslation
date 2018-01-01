<translation-game-yml>
    <div class="container">
        <nav aria-label="パンくずリスト">
            <ol class="breadcrumb mb-1">
                <li class="breadcrumb-item"><a href="/game">Game</a></li>
                <li class="breadcrumb-item active" aria-current="page">Ymlファイル</li>
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
                <translation-yml-file publicId="{status.gamePublicId}"></translation-yml-file>
            </div>
            <div class="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab">
                <translation-game-overview gamepublicid="{opts.publicid}"></translation-game-overview>

            </div>
            <div class="tab-pane fade" id="nav-contact" role="tabpanel" aria-labelledby="nav-contact-tab">
                vvvvvvvvvsssssss...
            </div>
        </div>
    </div>


    <script>
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

        //初期設定
        this.on('mount', this._func.init());


    </script>
</translation-game-yml>