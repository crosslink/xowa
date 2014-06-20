document.addEventListener( "DOMContentLoaded", function(){
var math = document.querySelectorAll('[id^="xowa_math_txt"]');
if (math.length) {
window.mathJax_Config = function () {
  MathJax.Hub.Config({
    root: window.xowa_root_dir + 'bin/any/javascript/xowa/mathjax',
    config: ["TeX-AMS-texvc_HTML.js"],
    "v1.0-compatible": false,
    styles: { ".mtext": { "font-family": "sans-serif ! important", "font-size": "80%" } },
    displayAlign: "left",
    menuSettings: { zoom: "Click" },
    "HTML-CSS": { imageFont: null, availableFonts: ["TeX"] }
  });
  MathJax.OutputJax.fontDir = window.xowa_root_dir + 'bin/any/javascript/xowa/mathjax/fonts';
}

  var config = 'mathJax_Config();',
      script1 = document.createElement( 'script' ),
      script2 = document.createElement( 'script' );
  script1.setAttribute( 'type', 'text/x-mathjax-config' );
  script1.text = config;
  document.getElementsByTagName('head')[0].appendChild( script1 );

  script2.setAttribute( 'src', window.xowa_root_dir + 'bin/any/javascript/xowa/mathjax/MathJax.js?config=default' );
  document.getElementsByTagName('head')[0].appendChild( script2 );

}
}, false );