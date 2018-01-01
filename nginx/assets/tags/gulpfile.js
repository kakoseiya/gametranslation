// glup・使用する各プラグインを最初に読み込む
var gulp = require("gulp"),
    uglify = require("gulp-uglify"),
    concat = require("gulp-concat"),
    runSequence = require('run-sequence');

// それぞれのプラグインで行う処理を書いていく

gulp.task('js.concat', function() {
    return gulp.src('dist/!(m-)*.js')
        .pipe(concat('main.js'))
        .pipe(gulp.dest('app/'));
});
gulp.task('js.uglify', function() {
    return gulp.src('app/main.js')
        // .pipe(plumber())
        .pipe(uglify({
            preserveComments : "some"
        }))
        .on('error',function(e){
            console.log(e);
        })
        .pipe(concat('main.min.js'))
        .pipe(gulp.dest('app/'));
});
gulp.task('js.concat_admin', function() {
    return gulp.src('dist/m-*.js')
        .pipe(concat('admin-main.js'))
        .pipe(gulp.dest('app/'));
});
gulp.task('js.uglify_admin', function() {
    return gulp.src('app/admin-main.js')
    // .pipe(plumber())
        .pipe(uglify())
        .pipe(concat('admin-main.min.js'))
        .pipe(gulp.dest('app/'));
});


// 監視して処理するのをひとまとめにしておく。
gulp.task('js', ['js.concat', 'js.uglify','js.concat_admin', 'js.uglify_admin']);

// ファイルを監視して実行させる
gulp.task('default', function() {
    runSequence('js.concat', 'js.uglify','js.concat_admin', 'js.uglify_admin');
});