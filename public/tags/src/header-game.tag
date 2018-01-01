<header-game>
    <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
        <a class="navbar-brand" href="#">Game Translation</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarCollapse">

        </div>
    </nav>

    <script>
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

    </script>
</header-game>