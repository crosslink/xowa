/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012 gnosygnu@gmail.com

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
package gplx.xowa.html; import gplx.*; import gplx.xowa.*;
import gplx.xowa.gui.*;
public class Xoh_page_wtr_mgr implements GfoInvkAble {
	private Xoh_page_wtr_wkr edit_wtr, html_wtr, read_wtr; private Xoa_app app;
	public Xoh_page_wtr_mgr(Xoa_app app, boolean html_capable) {
		this.app = app;
		page_edit_fmtr.Fmt_(String_.Concat_lines_nl_skip_last
		(	"<html>"
		,	"<head>"
		,	"  <meta http-equiv=\"content-type\" content=\"text/html;charset=UTF-8\" />"
		,	"  <script language='javascript'>"
		,	"    window.onload = function() {"
		,	"      var elem = document.getElementById('wikiText');"
		,	"      elem.selectionStart = 0;"
		,	"      elem.selectionEnd = 0;"
		,	"      window.setTimeout(function(){elem.focus();}, 100);"
		,	"    }"
		,	"  </script>"
		,	"</head>"
		,	"<body spellcheck='false'>"
		,	"  <textarea id='wikiText' style='width:99%;height:99%;border:0;'>"
		,	"~{page_data}</textarea>"
		,	"</body>"
		,	"</html>"
		));
		this.html_capable = html_capable;
		read_wtr = new Xoh_page_wtr_wkr(Xog_page_mode.Tid_read);
		edit_wtr = new Xoh_page_wtr_wkr(Xog_page_mode.Tid_edit);
		html_wtr = new Xoh_page_wtr_wkr(Xog_page_mode.Tid_html);
	}
	public boolean Html_capable() {return html_capable;} public Xoh_page_wtr_mgr Html_capable_(boolean v) {html_capable = v; return this;} private boolean html_capable;
	public Bry_fmtr Page_read_fmtr() {return page_read_fmtr;}
	public byte[] Css_common_bry() {return css_common_bry;} public Xoh_page_wtr_mgr Css_common_bry_(Io_url v) {css_common_bry = app.Url_converter_fsys().Encode_http(v); return this;} private byte[] css_common_bry;
	public byte[] Css_wiki_bry() {return css_wiki_bry;} public Xoh_page_wtr_mgr Css_wiki_bry_(Io_url v) {css_wiki_bry = app.Url_converter_fsys().Encode_http(v); return this;} private byte[] css_wiki_bry;
	private Bry_fmtr page_read_fmtr = Bry_fmtr.new_(""
		, "page_id", "page_name", "page_title", "page_content_sub", "page_data", "page_langs", "page_modified_on_msg", "page_lang_ltr"
		, "html_css_common_path", "html_css_wiki_path", "html_content_editable"
		, "xowa_head_script"
		, "portal_div_personal", "portal_div_ns", "portal_div_view"
		, "portal_div_logo", "portal_div_home", "portal_div_xtn", "portal_div_wikis", "portal_sidebar"
	    , "edit_div_rename", "edit_div_preview"
		, "app_version", "app_build_date", "app_root_dir", "js_article_view_vars", "js_wikidata", "js_edit_toolbar", "xowa_mode_is_server"
		);
	public Bry_fmtr Page_edit_fmtr() {return page_edit_fmtr;} private Bry_fmtr page_edit_fmtr = Bry_fmtr.new_(""
		, "page_id", "page_name", "page_title", "page_content_sub", "page_data", "page_langs", "page_modified_on_msg", "page_lang_ltr"
		, "html_css_common_path", "html_css_wiki_path", "html_content_editable"
		, "xowa_head_script"
		, "portal_div_personal", "portal_div_ns", "portal_div_view"
		, "portal_div_logo", "portal_div_home", "portal_div_xtn", "portal_div_wikis", "portal_sidebar"
	    , "edit_div_rename", "edit_div_preview"
		, "app_version", "app_build_date", "app_root_dir", "js_article_view_vars", "js_wikidata", "js_edit_toolbar", "xowa_mode_is_server"
		);
	public Bry_fmtr Page_html_fmtr() {return page_html_fmtr;} private Bry_fmtr page_html_fmtr = Bry_fmtr.new_(""
		, "page_id", "page_name", "page_title", "page_content_sub", "page_data", "page_langs", "page_modified_on_msg", "page_lang_ltr"
		, "html_css_common_path", "html_css_wiki_path", "html_content_editable"
		, "xowa_head_script"
		, "portal_div_personal", "portal_div_ns", "portal_div_view"
		, "portal_div_logo", "portal_div_home", "portal_div_xtn", "portal_div_wikis", "portal_sidebar"
	    , "edit_div_rename", "edit_div_preview"
		, "app_version", "app_build_date", "app_root_dir", "js_article_view_vars", "js_wikidata", "js_edit_toolbar", "xowa_mode_is_server"
		);
	private Bry_bfr tmp_bfr = Bry_bfr.reset_(255);
	public byte[] Edit_rename_div_bry(Xoa_ttl ttl) {			
		return div_edit_rename_fmtr.Bld_bry_many(tmp_bfr, ttl.Full_db());
	}	private Bry_fmtr div_edit_rename_fmtr = Bry_fmtr.new_(String_.Concat_lines_nl
	(	"  <input id='xowa_edit_rename_box' width='120' height='20' />" 
	,	"  <a href='xowa-cmd:app.gui.main_win.page_edit_rename;' class='xowa_anchor_button' style='width:100px;max-width:1024px;'>" 
	,	"    Rename page" 
	,	"  </a>"
	,	"  <a href='/wiki/Special:MovePage?wpOldTitle=~{src_full_db}' class='xowa_anchor_button' style='width:100px;max-width:1024px;'>"
	,	"    Special:MovePage" 
	,	"  </a>"
	),	"src_full_db");
	public void Init_(boolean v) {init = v;} private boolean init = true;
	public byte[] Gen(Xoa_page page, byte output_tid) {
		Xoh_page_wtr_wkr wtr = Wkr(output_tid);
		Xow_wiki wiki = page.Wiki();
		if (init) {
			init = false;
			page_edit_fmtr.Eval_mgr_(wiki.Eval_mgr());
			page_read_fmtr.Eval_mgr_(wiki.Eval_mgr());
			page_html_fmtr.Eval_mgr_(wiki.Eval_mgr());
		}
		Bry_bfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_m001();
		byte[] bry = wtr.Write(this, page, wiki.Ctx(), tmp_bfr);
		tmp_bfr.Mkr_rls();
		return bry;
	}
	public Xoh_page_wtr_wkr Wkr(byte output_tid) {
		switch (output_tid) {
			case Xog_page_mode.Tid_edit: return edit_wtr;
			case Xog_page_mode.Tid_html: return html_wtr;
			case Xog_page_mode.Tid_read: return read_wtr;
			default: throw Err_.unhandled(output_tid);
		}
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_page_read_))						page_read_fmtr.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_page_edit_))						page_edit_fmtr.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_page_html_))						page_html_fmtr.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_xowa_div_edit_rename_))			div_edit_rename_fmtr.Fmt_(m.ReadBry("v"));
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	public static final String Invk_page_read_ = "page_read_", Invk_page_edit_ = "page_edit_", Invk_page_html_ = "page_html_", Invk_xowa_div_edit_rename_ = "xowa_div_edit_rename_";
}
/*
NOTE_1:xowa_anchor_button
. used for media (WP forces javascript with oggplayer. wanted "simpler" model)
. display:inline-block; must be set for centering to work; see USSR and anthem; (display:block; must be enabled in order for padding to work)
. text-align:center; forces img to be in center

General notes:
. contentSub div is needed; EX.WP: Battle of Spotsylvania Court House
*/
