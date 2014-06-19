/* xowa.ready.js: Adds sortable and collapsible behavior to pages
Copyright (C) 2013 Schnark (<https://de.wikipedia.org/wiki/Benutzer:Schnark>)

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/
document.addEventListener('DOMContentLoaded', function () {
function getValue (key) {
    return window.xowa_global_values[key] || null;
}
function addCSS (css) {
    var s = document.createElement('style');
    document.getElementsByTagName('head')[0].appendChild(s);
    s.appendChild(document.createTextNode(css));
}
function load (file, callback) {
    var script = document.createElement('script');
    if (callback) {
       script.onload = callback;
    }
    script.async = false;
    script.setAttribute('src', window.xowa_root_dir + 'bin/any/javascript/xowa/article_view/' + file);
    document.getElementsByTagName('head')[0].appendChild(script);
}

function initJQuery () {
    //for jquery.tablesorter.js
    jQuery.escapeRE = function (s) {
        return s.replace(/([\\{}()|.?*+\-\^$\[\]])/g, '\\$1');
    };
    //for mediawiki.toc.js
    jQuery.cookie = xowaCookie;
    //fire the ready event
    jQuery.ready();
}

function xowaCookie (key, val, cookieData) {
    if (val == null)  // accessor; EX: $.cookie('key');
        return getValue(key);
    else              // mutator;  EX: $.cookie('key', 'val');
        window.xowa_global_values[key] = val;
}

function initMediawiki () {
    window.mediaWiki = {
        msg: getValue,
        log: function () {},
        config: {
            get: getValue
        },
        //simulate mediaWiki.hook: Execute functions queued for 'wikipage.content' directly, and ignore anything else
        hook: function (name) {
            return name === 'wikipage.content' ? {
              add: function (f) {
                     f(window.jQuery ? jQuery('#mw-content-text') : null);
              },
              remove: function () {},
              fire: function () {}
            } : {
              add: function () {},
              remove: function () {},
              fire: function () {}
            };
        }
    };
}

function initCollapsible () {
    /* from jquery.makeCollapsible.css */
    addCSS('.mw-collapsible-toggle{float:right;}li .mw-collapsible-toggle{float:none;}.mw-collapsible-toggle-li{list-style:none;}');
		var options = {};
    options.collapsed = getValue('collapsible-collapsed');
    $('table.collapsible, .mw-collapsible').makeCollapsible(options);
}

function initTablesorter () {
    /* from jquery.tablesorter.css */
    addCSS('table.jquery-tablesorter th.headerSort{background-image:url(data:image/gif;base64,R0lGODlhFQAJAIABAAAAAAAAACH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4wLWMwNjAgNjEuMTM0Nzc3LCAyMDEwLzAyLzEyLTE3OjMyOjAwICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAxODAxMTc0MDcyMDY4MTE4OEM2REYyN0ExMDhBNDJFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjdCNTAyODcwMEY4NjExRTBBMzkyQzAyM0E1RDk3RDc3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjdCNTAyODZGMEY4NjExRTBBMzkyQzAyM0E1RDk3RDc3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzUgTWFjaW50b3NoIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6MDE4MDExNzQwNzIwNjgxMTg4QzZERjI3QTEwOEE0MkUiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6MDE4MDExNzQwNzIwNjgxMTg4QzZERjI3QTEwOEE0MkUiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4B//79/Pv6+fj39vX08/Lx8O/u7ezr6uno5+bl5OPi4eDf3t3c29rZ2NfW1dTT0tHQz87NzMvKycjHxsXEw8LBwL++vby7urm4t7a1tLOysbCvrq2sq6qpqKempaSjoqGgn56dnJuamZiXlpWUk5KRkI+OjYyLiomIh4aFhIOCgYB/fn18e3p5eHd2dXRzcnFwb25tbGtqaWhnZmVkY2JhYF9eXVxbWllYV1ZVVFNSUVBPTk1MS0pJSEdGRURDQkFAPz49PDs6OTg3NjU0MzIxMC8uLSwrKikoJyYlJCMiISAfHh0cGxoZGBcWFRQTEhEQDw4NDAsKCQgHBgUEAwIBAAAh+QQBAAABACwAAAAAFQAJAAACF4yPgMsJ2mJ4VDKKrd4GVz5lYPeMiVUAADs=);cursor:pointer;background-repeat:no-repeat;background-position:center right;padding-right:21px}table.jquery-tablesorter th.headerSortUp{background-image:url(data:image/gif;base64,R0lGODlhFQAEAIABAAAAAAAAACH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4wLWMwNjAgNjEuMTM0Nzc3LCAyMDEwLzAyLzEyLTE3OjMyOjAwICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAzODAxMTc0MDcyMDY4MTE4OEM2REYyN0ExMDhBNDJFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjdCNTAyODc0MEY4NjExRTBBMzkyQzAyM0E1RDk3RDc3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjdCNTAyODczMEY4NjExRTBBMzkyQzAyM0E1RDk3RDc3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzUgTWFjaW50b3NoIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6MDM4MDExNzQwNzIwNjgxMTg4QzZERjI3QTEwOEE0MkUiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6MDM4MDExNzQwNzIwNjgxMTg4QzZERjI3QTEwOEE0MkUiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4B//79/Pv6+fj39vX08/Lx8O/u7ezr6uno5+bl5OPi4eDf3t3c29rZ2NfW1dTT0tHQz87NzMvKycjHxsXEw8LBwL++vby7urm4t7a1tLOysbCvrq2sq6qpqKempaSjoqGgn56dnJuamZiXlpWUk5KRkI+OjYyLiomIh4aFhIOCgYB/fn18e3p5eHd2dXRzcnFwb25tbGtqaWhnZmVkY2JhYF9eXVxbWllYV1ZVVFNSUVBPTk1MS0pJSEdGRURDQkFAPz49PDs6OTg3NjU0MzIxMC8uLSwrKikoJyYlJCMiISAfHh0cGxoZGBcWFRQTEhEQDw4NDAsKCQgHBgUEAwIBAAAh+QQBAAABACwAAAAAFQAEAAACDYwfoAvoz9qbZ9FrJC0AOw==);}table.jquery-tablesorter th.headerSortDown{background-image:url(data:image/gif;base64,R0lGODlhFQAEAIABAAAAAAAAACH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4wLWMwNjAgNjEuMTM0Nzc3LCAyMDEwLzAyLzEyLTE3OjMyOjAwICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtbG5zOnhtcD0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLyIgeG1wTU06T3JpZ2luYWxEb2N1bWVudElEPSJ4bXAuZGlkOjAyODAxMTc0MDcyMDY4MTE4OEM2REYyN0ExMDhBNDJFIiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOjhFNzNGQjI3MEY4NjExRTBBMzkyQzAyM0E1RDk3RDc3IiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOjhFNzNGQjI2MEY4NjExRTBBMzkyQzAyM0E1RDk3RDc3IiB4bXA6Q3JlYXRvclRvb2w9IkFkb2JlIFBob3Rvc2hvcCBDUzUgTWFjaW50b3NoIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6MDI4MDExNzQwNzIwNjgxMTg4QzZERjI3QTEwOEE0MkUiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6MDI4MDExNzQwNzIwNjgxMTg4QzZERjI3QTEwOEE0MkUiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz4B//79/Pv6+fj39vX08/Lx8O/u7ezr6uno5+bl5OPi4eDf3t3c29rZ2NfW1dTT0tHQz87NzMvKycjHxsXEw8LBwL++vby7urm4t7a1tLOysbCvrq2sq6qpqKempaSjoqGgn56dnJuamZiXlpWUk5KRkI+OjYyLiomIh4aFhIOCgYB/fn18e3p5eHd2dXRzcnFwb25tbGtqaWhnZmVkY2JhYF9eXVxbWllYV1ZVVFNSUVBPTk1MS0pJSEdGRURDQkFAPz49PDs6OTg3NjU0MzIxMC8uLSwrKikoJyYlJCMiISAfHh0cGxoZGBcWFRQTEhEQDw4NDAsKCQgHBgUEAwIBAAAh+QQBAAABACwAAAAAFQAEAAACDYyPAcmtsJyDVDKKWQEAOw==);}');
    $('table.sortable').tablesorter();
}
function initReferenceTooltips () {
   addCSS('.popupref {position: fixed; z-index: 1000; padding: 5px; border: 2px solid #00f; background-color: #def; max-width: 500px; overflow: auto;}');
}
function initToc() {
  $('#toc').toggleToc();
}
function init () {
    var needCollapsible = document.querySelectorAll('table.collapsible, .mw-collapsible').length,
        needSortable    = document.querySelectorAll('table.sortable').length,
        needReference   = document.querySelectorAll('sup.reference a, .reference sup a').length
        ;
    var needGalleryPacked   = xowa_global_values['gallery-packed-enabled']
    ,   needToc             = xowa_global_values['toc-enabled']
    ,   needNavframe        = xowa_global_values['navframe-enabled']
    ;
    if (needCollapsible || needSortable || needReference || needGalleryPacked || needToc || needNavframe) {
       load('jquery.js', initJQuery);
       initMediawiki();
    }
    if (needCollapsible) {
       load('jquery.makeCollapsible.js', initCollapsible);
    }
    if (needSortable) {
       load('jquery.tablesorter.js', initTablesorter);
    }
    if (needReference) {
       load('jquery.reference-tooltips.js', initReferenceTooltips);
    }
    if (needGalleryPacked) {
       load('mediawiki.page.gallery.js');
    }
    if (needToc) {
       load('mediawiki.toc.js', initToc);
    }
    if (needNavframe) {
      load('mediawiki.gadget.navframe.js');
    }
}
init();

}, false);