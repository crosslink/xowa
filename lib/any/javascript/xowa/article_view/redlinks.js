/* redlink.js: Marks missing pages with a red font
Copyright (C) 2013 Schnark (<https://de.wikipedia.org/wiki/Benutzer:Schnark>)

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
(function () {
"use strict";

var titlesPerQuery = 25, pauseBetweenQueries = 100;

function getAllLinks () {
	return document.querySelectorAll('#mw-content-text a');
}

function getInternalLinks (links) {
	var ret = {}, href, i;
	for (i = 0; i < links.length; i++) {
	var link_class = links[i].getAttribute('class');
		if (link_class === 'image' || link_class === 'xowa_nav') {
			continue;
		}
		href = links[i].getAttribute('href');
		try {
			href = decodeURIComponent(href);
		} catch (e) {
		}
		if (href.indexOf('/wiki/') === 0) {
	  href = href.substr(6).replace(/#.*/, '').replace(/^:/, '');
			if (!(href in ret)) {
				ret[href] = [];
			}
			ret[href].push(links[i]);
		}
	}
	return ret;
}

function markLinksRed (title, links) {
	var i;
	for (i = 0; i < links[title].length; i++) {
		links[title][i].className += ' new';
	}
}

function getAllTitles (links) {
	var allTitles = [], titles = [], t;
	for (t in links) {
		titles.push(t);
		if (titles.length === titlesPerQuery) {
			allTitles.push(titles);
			titles = [];
		}
	}
	if (titles.length) {
		allTitles.push(titles);
	}
	return allTitles;
}

function runTitleQuery (titles, links) {
	var result, i;
  if (xowa_mode_is_server) {
    xowa_exec_async(function(result) {
      for (i = 0; i < titles.length; i++) {
        if (result[i] === '0') {
          markLinksRed(titles[i], links);
        }
      }
    }, 'get_titles_exists', titles);
  } else {
    result = xowa_exec('get_titles_exists', titles);
    for (i = 0; i < titles.length; i++) {
      if (result[i] === '0') {
        markLinksRed(titles[i], links);
      }
    }
  }
}

function runTitleQueries (allTitles, links) {
	var i = 0;
	function runNextQuery () {
		runTitleQuery(allTitles[i], links);
		i++;
		if (i < allTitles.length) {
			window.setTimeout(runNextQuery, pauseBetweenQueries);
		}
	}
	runNextQuery();
}

function init () {
	var links = getAllLinks(), titles;
	links = getInternalLinks(links);
	titles = getAllTitles(links);
	runTitleQueries(titles, links);
}

document.addEventListener('DOMContentLoaded', init, false);

})();