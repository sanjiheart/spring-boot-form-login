const { src, dest, series, parallel, watch } = require('gulp'),
    minifyHtml = require('gulp-minify-html'),
    cleanCss = require('gulp-clean-css'),
    uglifyJs = require('gulp-uglify-es').default,
    yarn = require('gulp-yarn'),
    inject = require('gulp-inject'),
    merge = require('merge-stream'),
    clean = require('gulp-clean'),
    connect = require('gulp-connect');

const SRC_PATH = 'web',
    BUILD_PATH = 'src/main/resources/static/web',
    SRC_LIBS_PATH = `${SRC_PATH}/node_modules`,
    BUILD_LIBS_PATH = `${BUILD_PATH}/node_modules`;

function copyFiles() {
    return merge(
        src(`${SRC_PATH}/imgs/**`).pipe(dest(`${BUILD_PATH}/imgs`)),
        src(`${SRC_PATH}/fonts/**`).pipe(dest(`${BUILD_PATH}/fonts`))
    );
}

// install libs
function execYarn() {
    return src(`${SRC_PATH}/package.json`)
        .pipe(yarn());
}

// copy all used libs
function copyLibs() {
    return merge(
        src(`${SRC_LIBS_PATH}/vue/dist/**`).pipe(dest(`${BUILD_LIBS_PATH}/vue/dist`)),
        src(`${SRC_LIBS_PATH}/axios/dist/**`).pipe(dest(`${BUILD_LIBS_PATH}/axios/dist`)),
        src(`${SRC_LIBS_PATH}/bootstrap/dist/**`).pipe(dest(`${BUILD_LIBS_PATH}/bootstrap/dist`)),
        src(`${SRC_LIBS_PATH}/bootswatch/dist/lumen/**`).pipe(dest(`${BUILD_LIBS_PATH}/bootswatch/dist/lumen`)),
        src(`${SRC_LIBS_PATH}/@fortawesome/fontawesome-free/**`).pipe(dest(`${BUILD_LIBS_PATH}/@fortawesome/fontawesome-free`))
    );
}

function minifyCss() {
    return src(`${SRC_PATH}/css/*.css`)
        .pipe(cleanCss())
        .pipe(dest(`${BUILD_PATH}/css`));
}

// uglify JS files
function minifyJs() {
    return src(`${SRC_PATH}/js/*.js`)
        .pipe(uglifyJs().on('error', function (e) {
            console.log(e);
        }))
        .pipe(dest(`${BUILD_PATH}/js`));
}

// inject & minify HTML files
function injectLibs() {
    var targets = src(`${SRC_PATH}/*.html`);
    var sources = src([`${SRC_LIBS_PATH}/bootswatch/dist/lumen/bootstrap.min.css`,
    `${SRC_LIBS_PATH}/@fortawesome/fontawesome-free/css/all.min.css`,
    `${SRC_PATH}/css/global.css`,
    `${SRC_LIBS_PATH}/vue/dist/vue.js`,
    `${SRC_LIBS_PATH}/axios/dist/axios.min.js`,
    `${SRC_LIBS_PATH}/bootstrap/dist/js/bootstrap.min.js`,
    `${SRC_LIBS_PATH}/@fortawesome/fontawesome-free/js/all.min.js`,
    `${SRC_PATH}/js/envs.js`
    ], { read: false });
    return targets.pipe(inject(sources, { ignorePath: SRC_PATH, addRootSlash: false }))
        .pipe(minifyHtml())
        .pipe(dest(BUILD_PATH));
}

function cleanAll() {
    return src(BUILD_PATH, { allowEmpty: true })
        .pipe(clean());
}

function reloadHtml() {
    return src(BUILD_PATH + '/**/*.html').pipe(connect.reload());
}

function watchFiles() {
    return watch([`${SRC_PATH}/**/*.css`, `${SRC_PATH}/**/*.js`, `${SRC_PATH}/**/*.html`]
        , series(copyFiles, execYarn, copyLibs, minifyCss, minifyJs, injectLibs, reloadHtml));
}

function connectServer() {
    connect.server({
        root: BUILD_PATH.replace('/web', ''),
        port: 9095,
        livereload: true
    });
}

exports.build = series(cleanAll, parallel(series(execYarn, copyLibs, minifyCss, minifyJs, injectLibs), copyFiles));
exports.watch = parallel(connectServer, watchFiles);