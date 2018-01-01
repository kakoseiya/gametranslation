<user-mypage>
    <div class="container">
        <nav class="nav nav-tabs" id="myPageTab" role="tablist">
            <a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab"
               aria-controls="nav-home" aria-selected="true">
                ユーザー情報
            </a>
            <a class="nav-item nav-link" id="nav-profile-tab" data-toggle="tab" href="#nav-profile" role="tab"
               aria-controls="nav-profile" aria-selected="false">
                友達リスト
            </a>
            <a class="nav-item nav-link" id="nav-profile-tab2" data-toggle="tab" href="#nav-profile2" role="tab"
               aria-controls="nav-profile2" aria-selected="false">
                メッセージ
            </a>
        </nav>
        <div class="tab-content" id="nav-tabContent">
            <div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab">
                <br>
                <user-mypage-info user="{opts.user}"></user-mypage-info>
            </div>
            <div class="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab">
                dddddddddddd...

            </div>
            <div class="tab-pane fade" id="nav-profile2" role="tabpanel" aria-labelledby="nav-profile-tab2">
                <br>
                <user-mypage-message user="{opts.user}"></user-mypage-message>
            </div>
        </div>
    </div>


    <script>
        var self = this;
        this.mixin(OptsMixin);




    </script>
</user-mypage>