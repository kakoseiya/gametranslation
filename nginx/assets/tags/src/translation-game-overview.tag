<translation-game-overview>

    <div id="game_overview"></div>

    <script>
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

        //マウント時に初期化
        this.on('mount', self._func.init);

    </script>

</translation-game-overview>