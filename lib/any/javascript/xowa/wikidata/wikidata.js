/* wikidata.js: Formats Wikidata JSON text in an HTML layout (with dynamic name resolution)
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
/*global xowa_mode_is_server, xowa_exec, xowa_exec_async*/
/*jshint forin: false */
"use strict";
var i18n = {};
var claimFormatters;

function initI18N () {
	var key;
	function translatePlaceholders (msg) {
		return msg.replace(/~\{(\d+)\}/g, function (all, n) {
			return '$' + (Number(n) + 1);
		});
	}
	for (key in window.xowa_wikidata_i18n) {
		i18n[key] = translatePlaceholders(window.xowa_wikidata_i18n[key]);
	}
	i18n.languages = i18n.languages.split(';');
	i18n.paren = i18n['word-separator'] + i18n.parentheses;
	i18n.months = ['', i18n.jan, i18n.feb, i18n.mar, i18n.apr, i18n.may, i18n.jun, i18n.jul, i18n.aug, i18n.sep, i18n.oct, i18n.nov, i18n.dec];
	i18n.yearSuffixes = ['$1', i18n.decade, i18n.century, i18n.millenium, i18n.years1e4, i18n.years1e5, i18n.years1e6, i18n.years1e7, i18n.years1e8, i18n.years1e9];
}

function initClaimFormatters () {
	return {
		//TODO add more formats once more formats are defined
		'string': escape,
		'globecoordinate': getGlobecoordinateValue(i18n.degree, i18n.minute, i18n.second, '&nbsp;',
			i18n.north, i18n.south, i18n.west, i18n.east, i18n.plusminus, i18n.meters,
			i18n.paren, i18n['comma-separator']),
		'quantity': getQuantityValue(i18n.plus, i18n.minus, i18n.plusminus, '&nbsp;'),
		'time': getTimeValue(i18n.yearSuffixes, [i18n.bc, i18n.ago, i18n.inTime], i18n.julian,
			i18n.months, ['$1. $2 $3', '$1 $2'], '$1h', ':', i18n['word-separator'],
			i18n.plus, i18n.minus, i18n.plusminus, i18n.paren, i18n['comma-separator']), //TODO localize (date format, time)
		'wikibase-entityid': function (e) {
			return makeWDLink('Q' + escape(String(e['numeric-id'])));
		}
	};
}

//generic helper functions

function escape (text) {
	return text.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}

function makeLink (page, wiki) {
	var lang = wiki.replace(/wiki.*/, '').replace(/_/g, '-'), project = wiki.replace(/^.*wiki/, 'wiki');
	if (project === 'wiki') {
		project = 'wikipedia';
	}
	return '<a href="/site/' + lang + '.' + project + '.org/wiki/' + encodeURI(page) + '">' + escape(page) + '</a>';
}

function makeWDLink (item) {
	item = item.toUpperCase();
	return '<a class="replaceWithLabel" href="/wiki/' + (item.charAt(0) === 'P' ? 'Property:' : '') + item + '">' + item + '</a>';
}

// parsers
function getGlobecoordinateValue (deg, min, sec, sep, n, s, w, e, plusminus, m, parenthesis, comma) {
	function decToDeg (dec) {
		dec = Math.abs(dec);
		var ret = [Math.floor(dec)];
		dec -= ret[0];
		dec *= 60;
		ret.push(Math.floor(dec));
		dec -= ret[1];
		dec *= 60;
		ret.push(dec);
		return ret;
	}

	return function (coord) {
		var lat = decToDeg(coord.latitude), long = decToDeg(coord.longitude), prec = decToDeg(coord.precision), paren = [];
		prec[2] = Math.round(prec[2]);
		lat = lat[0] + deg + sep + lat[1] + min + sep + Math.round(lat[2]) + sec + sep + (coord.latitude >= 0 ? n : s);
		long = long[0] + deg + sep + long[1] + min + sep + Math.round(long[2]) + sec + sep + (coord.longitude >= 0 ? e : w);
		prec = prec[0] ? plusminus + prec[0] + deg :
			prec[1] ? plusminus + prec[1] + min :
			prec[2] ? plusminus + prec[2] + sec : null;
		if (prec) {
			paren.push(prec);
		}
		if (coord.altitude) {
			paren.push(coord.altitude + m); //FIXME is this correct?
		}
		if (coord.globe && coord.globe.indexOf('http:\/\/www.wikidata.org\/entity\/Q' === 0)) {
			paren.push(makeWDLink(coord.globe.substr(31)));
		}
		paren = paren.join(comma);
		if (paren) {
			paren = parenthesis.replace(/\$1/g, paren);
		}
		return lat + comma + long + paren;
	};
}

function getQuantityValue (plus, minus, plusminus, sep) {
	return function (quantity) {
		var	n = Number(quantity.amount), display = String(n),
			min = Number(quantity.lowerBound), d1 = n - min,
			max = Number(quantity.upperBound), d2 = max - n;
		if (d1 === d2) {
			if (d1 !== 0) {
				display += plusminus + d1;
			}
		} else {
			if (d2 !== 0) {
				display += plus + d2;
			}
			if (d1 !== 0) {
				display += minus + d1;
			}
		}
		if (quantity.unit !== '1') {
			display += sep + quantity.unit;
		}
		return display;
	};
}

function getTimeValue (yearFormat, addYearFormat, jul, months, dateFormat, h, timeSep, sep, plus, minus, plusminus, parenthesis, comma) {
	function parseString (s) {
		var match, year, month, day, hour, minute, second;
		match = /^([+\-])(\d{11})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2})Z$/.exec(s || '');
		if (!match) {
			return false;
		}
		year = Number(match[2]);
		if (match[1] === '-') {
			year = -year;
		}
		month = Number(match[3]);
		day = Number(match[4]);
		hour = Number(match[5]);
		minute = Number(match[6]);
		second = Number(match[7]);
		return {
			year: year,
			month: month,
			day: day,
			hour: hour,
			minute: minute,
			second: second
		};
	}

	function toJulian (date) {
		var	a = Math.floor((14 - date.month) / 12),
			y = date.year + 4800 - a,
			m = date.month + 12 * a - 3,
			julian = date.day + Math.floor((153 * m + 2) / 5) + 365 * y + Math.floor(y / 4) - Math.floor(y / 100) + Math.floor(y / 400) - 32045,
			c = julian + 32082,
			d = Math.floor((4 * c + 3) / 1461),
			e = c - Math.floor((1461 * d) / 4),
			n = Math.floor((5 * e + 2) / 153);
		date.year = d - 4800 + Math.floor(n / 10);
		date.month = n + 3 - 12 * Math.floor(n / 10);
		date.day = e - Math.floor((153 * n + 2) / 5) + 1;
	}

	function formatYear (year, round) {
		var template = yearFormat[round];
		if (year <= 0) {
			if (round < 4) {
				template = addYearFormat[0].replace(/\$1/, template);
			} else {
				template = addYearFormat[1].replace(/\$1/, template);
			}
		} else {
			if (round >= 4) {
				template = addYearFormat[2].replace(/\$1/, template);
			}
		}
		switch (round) {
		case 0: year = (year <= 0 ? -(year - 1) : year); break;
		case 1: year = Math.floor((Math.abs(year)) / Math.pow(10, round)); break;
		default: year = Math.floor((Math.abs(year) - 1) / Math.pow(10, round)) + 1;
		}
		return template.replace(/\$1/, year);
	}

	function formatDate (day, month, year) {
		year = formatYear(year, 0);
		return day ?
			dateFormat[0].replace(/\$1/, day).replace(/\$2/, months[month]).replace(/\$3/, year) :
			dateFormat[1].replace(/\$1/, months[month]).replace(/\$2/, year);
	}
	
	function formatTime (hour, minute, second) {
	//TODO leading zeros?
		if (minute === -1) {
			return h.replace(/\$1/, hour);
		}
		return hour + timeSep + minute + (second > -1 ? timeSep + second : '');
	}

	return function (time) {
		//TODO use time.timezone
		var date, output, beforeafter = '', calendar = '';
		date = parseString(time.time);
		if (!date) {
			return escape(time.time);
		}
		if (time.calendarmodel === 'http://www.wikidata.org/entity/Q1985786') {
			toJulian(date);
			calendar = jul;
		}
		if (time.precision <= 9) {
			output = formatYear(date.year, 9 - time.precision);
		} else if (time.precision === 10) {
			output = formatDate(0, date.month, date.year);
		} else if (time.precision === 11) {
			output = formatDate(date.day, date.month, date.year);
		} else if (time.precision === 12) {
			output = formatTime(date.hour, -1, -1) + sep + formatDate(date.day, date.month, date.year);
		} else if (time.precision === 13) {
			output = formatTime(date.hour, date.minute, -1) + sep + formatDate(date.day, date.month, date.year);
		} else {
			output = formatTime(date.hour, date.minute, date.second) + sep + formatDate(date.day, date.month, date.year);
		}
		output += calendar;
		if (time.before === 0) {
			if (time.after !== 0) {
				beforeafter = plus + time.after;
			}
		} else /* time.before !== 0 */ {
			if (time.after === 0) {
				beforeafter = minus + time.before;
			} else if (time.before === time.after) {
				beforeafter = plusminus + time.before;
			} else {
				beforeafter = minus + time.before + comma + plus + time.after;
			}
		}
		if (beforeafter) {
			output += parenthesis.replace(/\$1/, beforeafter);
		}
		return output;
	};
}

// run f for all elements of list, and pause for d2 milliseconds every d1 milliseconds
function eachAsync (list, f, d1, d2) {
	var i = 0, done = false;
	function next () {
		f(list[i]);
		i++;
		if (i === list.length) {
			window.status = '';
			done = true;
		}
	}
	function bulk () {
		var now = (new Date()).getTime();
		while (!done && now + d1 > (new Date()).getTime()) {
			next();
		}
	}
	function nextBulk () {
		bulk();
		if (!done) {
			window.setTimeout(nextBulk, d2);
		}
	}
	nextBulk();
}

var cachedLabels = {};

function pausecomp (millis) { // REF: http://stackoverflow.com/questions/951021/what-do-i-do-if-i-want-a-javascript-version-of-sleep
	var date = new Date(), curDate;
	do {
		curDate = new Date();
	} while (curDate - date < millis);
}

function replaceLinkLabel (link) {
	var item = link.textContent.toUpperCase(), label, langs = i18n.languages.join(';') + ';xowa_title';
	if (item.charAt(0) === 'P') {
		item = 'Property:' + item;
	}
	label = cachedLabels[item]; // get label from cache
	if (!label) { // label not found
		if (xowa_mode_is_server) {
			pausecomp(100); // HACK: needed, else async only fires once;
			xowa_exec_async(function (result) {
				var label = result[0];
				cachedLabels[item] = label;
				if (label) {
					link.textContent = label;
				}
			}, 'wikidata_get_label', langs, item);
		} else {
			label = xowa_exec('wikidata_get_label', langs, item)[0];
			cachedLabels[item] = label;
		}
	}
	if (label) { // NOTE: this is only called by sync mode
		link.textContent = label;
	}
}

function replaceAllLinkLabels () {
	var links = document.getElementsByClassName('replaceWithLabel');
	eachAsync(links, replaceLinkLabel, 100, 200);
}

//functions for toc
var toc = [];

function escapeToc (h) {
	return h == null
    ? 'Unknown'  // handle new wiki types that are not defined in XOWA and are not in i18n; getLinksHtml(data, 'wikisource') requires a 'xowa-wikidata-links-wikisource' in i18n
    : encodeURI(h.replace(/ /g, '_')).replace(/%3A/g, ':').replace(/%/g, '.')
    ;
}

function addToToc (h) {
	toc.push(h);
	return escapeToc(h);
}

function getTocHtml () {
	if (toc.length <= 3) {
		return '';
	}
	var tocEntries = [], i;
	for (i = 0; i < toc.length; i++) {
		tocEntries.push('<li class="toclevel-1 tocsection-' + (i + 1) + '"><a href="#' + escapeToc(toc[i]) + '"><span class="tocnumber">' + (i + 1) + '</span> <span class="toctext">' + toc[i] + '</span></a>');
	}
	return '<p><div id="toc" class="toc"><div id="toctitle"><h2>' + i18n.toc + '</h2></div><ul>' + tocEntries.join('') + '</ul></div></p>';
}

function makeH2 (h) {
	return '<h2 id="' + addToToc(h) + '">' + h + '</h2>';
}

//functions to format data of current item/prop

function formatAliases (aliases) {
/*Q1: "aliases": {
        "it": { "2":"cosmo", "3":"spazio"},
        "nl": [ "universum", "kosmos", "cosmos"],
        "da": []
      }
I like such consistent formats... */

	if (aliases.length === 0) {
		return false;
	}
	if (aliases.length) {
		return escape(aliases.join('\n')).replace(/\n/g, '<br />');
	}
	var i, html = [];
	for (i in aliases) {
		html.push(escape(aliases[i]));
	}
	return html.length ? html.join('<br />') : false;
}

function getFormattedTR (data, format, suff) {
	var i, tr = [], id;
	function makeRow(id, val) {
		val = format(val, id);
		if (val) {
			tr.push('<tr><td><code>' + id + '</code></td><td>' + val + '</td></tr>');
		}
	}
	for (i = 0; i < i18n.languages.length; i++) {
		id = i18n.languages[i] + (suff || '');
		if (id in data) {
			makeRow(id, data[id]);
			delete data[id];
		}
	}
	for (id in data) {
		makeRow(id, data[id]);
	}
	return tr;
}

function getLabelsHtml (data) {
	if (!data) {
		return '';
	}
	var tr = getFormattedTR(data, escape), h = i18n.labels;
	return tr.length ? makeH2(h) + '<p><table class="wikitable"><tr><th>' + i18n.language + '</th><th>' + i18n.label + '</th></tr>' + tr.join('') + '</table></p>' : '';
}

function getAliasesHtml (data) {
	if (!data) {
		return '';
	}
	var tr = getFormattedTR(data, formatAliases), h = i18n.aliasesHead;
	return tr.length ? makeH2(h) + '<p><table class="wikitable"><tr><th>' + i18n.language + '</th><th>' + i18n.aliases + '</th></tr>' + tr.join('') + '</table></p>' : '';
}

function getDescriptionsHtml (data) {
	if (!data) {
		return '';
	}
	var tr = getFormattedTR(data, escape), h = i18n.descriptions;
	return tr.length ? makeH2(h) + '<p><table class="wikitable"><tr><th>' + i18n.language + '</th><th>' + i18n.description + '</th></tr>' + tr.join('') + '</table></p>' : '';
}

function getLinksHtml (data, type) {
	if (!data) {
		return '';
	}
	var tr = getFormattedTR(data, function (page, wiki) {
		if (type === 'special' || wiki.lastIndexOf(type) + type.length === wiki.length) {
			return makeLink(page.name || page, wiki); //page.name for new version with badges, page for old version
		}
	}, type), h = i18n['links-' + type];
	return tr.length ? makeH2(h) + '<p><table class="wikitable"><tr><th>' + i18n.wiki + '</th><th>' + i18n.link + '</th></tr>' + tr.join('') + '</table></p>' : '';
}

function getAllLinksHtml (data) {
	if (!data) {
		return '';
	}
	var	specialWikis = ['commonswiki' /*, 'specieswiki' */],
		specialData = {}, i;
	for (i = 0; i < specialWikis.length; i++) {
		if (specialWikis[i] in data) {
			specialData[specialWikis[i]] = data[specialWikis[i]];
			delete data[specialWikis[i]];
		}
	}
	return	getLinksHtml(data, 'wiki') +
			getLinksHtml(data, 'wikivoyage') +
			getLinksHtml(data, 'wikisource') +
			//getLinksHtml(data, 'wiktionary') +
			getLinksHtml(data, 'wikiquote') +
			//getLinksHtml(data, 'wikibooks') +
			//getLinksHtml(data, 'wikinews') +
			getLinksHtml(specialData, 'special');
}

function getValueHtml (data) {
	var isval = data[0], prop = data[1], type = data[2], val = data[3];
	switch (isval) {
	case 'value':
		if (type in claimFormatters) {
			val = claimFormatters[type](val);
		} else {
			val = escape(JSON.stringify(val)) + ' (' + type + ')';
		}
		break;
	case 'novalue':
		val = i18n.novalue;
		break;
	case 'somevalue':
		val = i18n.somevalue;
		break;
	default: //this shouldn't happen
		val = escape(isval);
	}
	return '<td>' + makeWDLink('P' + prop) + '</td><td>' + val + '</td>';
}

function getRefsHtml (data) {
	if (data.length === 0) {
		return '';
	}
	var i, tr = [];
	for (i = 0; i < data.length; i++) {
		tr.push('<tr>' + getValueHtml(data[i][0]) + '</tr>');
	}
	return '<table><tr><th>' + i18n.property + '</th><th>' + i18n.value + '</th></tr>' + tr.join('') + '</table>';
}

function getQualyfiersHtml (data) {
	if (data.length === 0) {
		return '';
	}
	var i, tr = [];
	for (i = 0; i < data.length; i++) {
		tr.push('<tr>' + getValueHtml(data[i]) + '</tr>');
	}
	return '<table><tr><th>' + i18n.property + '</th><th>' + i18n.value + '</th></tr>' + tr.join('') + '</table>';
}

function getRankHtml (rank) {
	switch (rank) {
	case 0: return i18n.deprecated;
	case 1: return i18n.normal;
	case 2: return i18n.preferred;
	//this shouldn't happen
	default: return escape(String(rank));
	}
}

function getClaimHtml (data) {
	return '<tr>' + getValueHtml(data.m) + '<td>' + getRefsHtml(data.refs) + '</td><td>' + getQualyfiersHtml(data.q) + '</td><td>' + getRankHtml(data.rank) + '</td></tr>';
}

function getClaimsHtml (data) {
	if (!data || data.length === 0) {
		return '';
	}
	var i, tr = [], h = i18n.claims;
	for (i = 0; i < data.length; i++) {
		tr.push(getClaimHtml(data[i]));
	}
	return makeH2(h) + '<p><table class="wikitable"><tr><th>' + i18n.property + '</th><th>' + i18n.value + '</th><th>' + i18n.references + '</th><th>' + i18n.qualifiers + '</th><th>' + i18n.rank + '</th></tr>' + tr.join('') + '</table></p>';
}

function getHtml (text) {
	var	data = JSON.parse(text), h = i18n.json,
		html = 
    getClaimsHtml(data.claims) +
    getLabelsHtml(data.label) +
		getAliasesHtml(data.aliases) +
		getDescriptionsHtml(data.description) +
		getAllLinksHtml(data.links) +
		makeH2(h) + '<pre style="overflow: auto;">' + escape(text) + '</pre>';
	return getTocHtml() + html;
}

function init () {
	initI18N();
	claimFormatters = initClaimFormatters();
	document.addEventListener('DOMContentLoaded', function () {
		var content = document.getElementById('xowa-wikidata-json');
		if (!content) {
			return;
		}
		content.innerHTML = getHtml(content.textContent);
		replaceAllLinkLabels();
	}, false);
}

init();

})();