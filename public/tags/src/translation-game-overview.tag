<translation-game-overview>

    <div id="game_overview"></div>

    <script>
        var self = this;
        var obs = null;

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
                        self.status._data = json.data;
                    })
                    .catch(function (error) {
                        console.log('ERROR:', error);
                    })

            },
            getHtml: function (selector) {
                var html = $(selector).html();
                html = html.replace(/&lt;/g, '<');
                html = html.replace(/&gt;/g, '>');
                html = html.replace(/&amp;/g, '&');

                return html;
            }
        };

        //マウント時に初期化
        this.on('mount', self._func.init);

        window.onload = function () {
            console.log('WINDOW:', window)
            var md = window.markdownit();
            var result = md.render(self.status._data.overview);
            console.log('MD:', result);
        };


    </script>

</translation-game-overview>