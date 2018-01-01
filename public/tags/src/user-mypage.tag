<user-mypage>
    <div class="container">
        <nav class="nav nav-tabs" id="myPageTab" role="tablist">
            <a class="nav-item nav-link active" id="nav-home-tab" data-toggle="tab" href="#nav-home" role="tab"
               aria-controls="nav-home" aria-selected="true">
                ユーザー情報
            </a>
            <a class="nav-item nav-link" id="nav-profile-tab" data-toggle="tab" href="#nav-profile" role="tab"
               aria-controls="nav-profile" aria-selected="false">
                メッセージ
            </a>
        </nav>
        <div class="tab-content" id="nav-tabContent">
            <div class="tab-pane fade show active" id="nav-home" role="tabpanel" aria-labelledby="nav-home-tab">
                <br>
                <user-mypage-admin user="{opts.user}"></user-mypage-admin>
            </div>
            <div class="tab-pane fade" id="nav-profile" role="tabpanel" aria-labelledby="nav-profile-tab">
                dddddddddddd...

            </div>
        </div>
    </div>


    <script>
        var self = this;
        this.mixin(OptsMixin);




    </script>
</user-mypage>