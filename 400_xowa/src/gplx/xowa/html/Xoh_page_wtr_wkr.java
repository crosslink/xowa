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
import gplx.html.*; import gplx.xowa.html.portal.*;
import gplx.xowa.wikis.*; import gplx.xowa.gui.*; import gplx.xowa.xtns.wdatas.*;	
public class Xoh_page_wtr_wkr implements Bry_fmtr_arg {
	private Xop_ctx ctx; private Xoa_page page; private Bry_bfr tmp_bfr = Bry_bfr.reset_(255); 
	public Xoh_page_wtr_wkr(byte page_mode) {this.page_mode = page_mode;} private byte page_mode;
	public Wdata_xwiki_link_wtr Wdata_lang_wtr() {return wtr_page_lang;} private Wdata_xwiki_link_wtr wtr_page_lang = new Wdata_xwiki_link_wtr();
	public Xoh_page_wtr_wkr Page_(Xoa_page v) {this.page = v; return this;} 
	public Xoh_page_wtr_wkr Mgr_(Xoh_page_wtr_mgr v) {this.mgr = v; return this;} private Xoh_page_wtr_mgr mgr;
	public boolean Ctgs_enabled() {return ctgs_enabled;} public Xoh_page_wtr_wkr Ctgs_enabled_(boolean v) {ctgs_enabled = v; return this;} private boolean ctgs_enabled = true;
	public byte[] Write(Xoh_page_wtr_mgr mgr, Xoa_page page, Xop_ctx ctx, Bry_bfr html_bfr) {
		this.mgr = mgr; this.page = page; this.ctx = ctx; 
		Xow_wiki wiki = page.Wiki(); Xoa_app app = wiki.App();
		ctx.Cur_page_(page); // HACK: must update page for toc_mgr; WHEN: Xoa_page rewrite
		Bry_fmtr fmtr = null;
		if (mgr.Html_capable()) {
			wtr_page_lang.Page_(page);
			byte view_mode = page_mode;
			switch (page_mode) {
				case Xog_page_mode.Tid_edit:	fmtr = mgr.Page_edit_fmtr(); break;
				case Xog_page_mode.Tid_html:	fmtr = mgr.Page_read_fmtr(); view_mode = Xog_page_mode.Tid_read; break; // set view_mode to read, so that "read" is highlighted in HTML
				case Xog_page_mode.Tid_read:	fmtr = mgr.Page_read_fmtr(); 
					ctx.Cur_page().Lnki_redlinks_mgr().Page_bgn();	// not sure if this is the best place to put it, but redlinks (a) must only fire once; (b) must fire before html generation; (c) cannot fire during edit (preview will handle separately)
					break;
			}
			Write_page(html_bfr, app, wiki, mgr, page, view_mode, fmtr, this);
			if (page_mode == Xog_page_mode.Tid_html)	// if html, write page again, but wrap it in html skin this time
				Write_page(html_bfr, app, wiki, mgr, page, page_mode, mgr.Page_html_fmtr(), Html_utl.Escape_html_as_str(html_bfr.XtoStrAndClear()));
			wtr_page_lang.Page_(null);
		}
		else
			XferAry(html_bfr, 0);
		this.page = null;
		return html_bfr.XtoAryAndClear();
	}
	private void Write_page(Bry_bfr html_bfr, Xoa_app app, Xow_wiki wiki, Xoh_page_wtr_mgr mgr, Xoa_page page, byte view_tid, Bry_fmtr fmtr, Object page_data) {
		byte[] custom_html = page.Html_data().Custom_html();
		if (custom_html != null) {
			html_bfr.Add(custom_html);
			return;
		}
		DateAdp page_modified_on_dte = page.Revision_data().Modified_on();
		byte[] page_modified_on_msg = wiki.Msg_mgr().Val_by_id_args(Xol_msg_itm_.Id_portal_lastmodified, page_modified_on_dte.XtoStr_fmt_yyyy_MM_dd(), page_modified_on_dte.XtoStr_fmt_HHmm());
		byte[] html_content_editable = wiki.Gui_mgr().Cfg_browser().Content_editable() ? Content_editable_bry : Bry_.Empty;
		byte[] page_content_sub = Xoh_page_wtr_wkr_.Bld_page_content_sub(app, wiki, page, tmp_bfr);
		byte[] js_wikidata_bry = Wdata_wiki_mgr.Wiki_page_is_json(wiki.Domain_tid(), page.Ttl().Ns().Id()) ? app.User().Lang().Fragment_mgr().Html_js_wikidata() : Bry_.Empty;
		byte[] js_edit_toolbar_bry = view_tid == Xog_page_mode.Tid_edit ? wiki.Fragment_mgr().Html_js_edit_toolbar() : Bry_.Empty;
		page.Xtn_mgr().Exec();
		Xow_portal_mgr portal_mgr = wiki.Html_mgr().Portal_mgr().Init_assert();
		fmtr.Bld_bfr_many(html_bfr, page.Revision_data().Id()
		, Xoh_page_wtr_wkr_.Bld_page_name(tmp_bfr, page.Ttl(), null)					// NOTE: page_name does not show display_title (<i>). always pass in null
		, Xoh_page_wtr_wkr_.Bld_page_name(tmp_bfr, page.Ttl(), page.Display_ttl())
		, page_content_sub, page_data, wtr_page_lang, page_modified_on_msg, page.Lang().Dir_bry() 
		, mgr.Css_common_bry(), mgr.Css_wiki_bry(), html_content_editable
		, page.Html_data().Module_mgr().Init(app, wiki, page).Init_dflts()
		, portal_mgr.Div_personal_bry(), portal_mgr.Div_ns_bry(app.Utl_bry_bfr_mkr(), page.Ttl(), wiki.Ns_mgr()), portal_mgr.Div_view_bry(app.Utl_bry_bfr_mkr(), view_tid, page.Html_data().Search_text())
		, portal_mgr.Div_logo_bry(), portal_mgr.Div_home_bry(), page.Html_data().Portal_div_xtn(), portal_mgr.Div_wikis_bry(app.Utl_bry_bfr_mkr()), portal_mgr.Sidebar_mgr().Html_bry()
		, mgr.Edit_rename_div_bry(page.Ttl()), page.Html_data().Edit_preview()
		, Xoa_app_.Version, Xoa_app_.Build_date
		, app.Fsys_mgr().Root_dir().To_http_file_bry()
		, wiki.Fragment_mgr().Html_js_table(), js_wikidata_bry, js_edit_toolbar_bry, app.Tcp_server().Running_str()
		);
		Xoh_page_wtr_wkr_.Bld_head_end(html_bfr, page);
		Xoh_page_wtr_wkr_.Bld_html_end(html_bfr, page);
	}
	public void XferAry(Bry_bfr html_bfr, int idx) {
		Xow_wiki wiki = page.Wiki(); Xoa_app app = wiki.App();
		byte[] data_raw = page.Data_raw();
		int ns_id = page.Ttl().Ns().Id();
		byte page_tid = Xow_page_tid.Identify(wiki.Domain_tid(), ns_id, page.Ttl().Page_db());
		if (page_mode == Xog_page_mode.Tid_edit) {
			Write_mode_edit(html_bfr, data_raw, ns_id, page_tid);
			return;
		}
		int bfr_page_bgn = html_bfr.Len();
		boolean page_tid_uses_pre = false;
		switch (page_tid) {
			case Xow_page_tid.Tid_json:
				app.Wiki_mgr().Wdata_mgr().Write_json_as_html(html_bfr, data_raw);
				break;
			case Xow_page_tid.Tid_js:
			case Xow_page_tid.Tid_css:
			case Xow_page_tid.Tid_lua:
				Xoh_html_wtr_escaper.Escape(page.Wiki().App(), tmp_bfr, data_raw, 0, data_raw.length, false, false);
				app.Html_mgr().Page_mgr().Content_code_fmtr().Bld_bfr_many(html_bfr, tmp_bfr);
				tmp_bfr.Clear();
				page_tid_uses_pre = true;
				break;
			case Xow_page_tid.Tid_wikitext:
				if	(ns_id == Xow_ns_.Id_mediaWiki) {	// if MediaWiki and wikitext, must be a message; convert args back to php; DATE:2014-06-13
					html_bfr.Add(gplx.xowa.apps.Xoa_gfs_php_mgr.Xto_php(tmp_bfr, Bool_.N, data_raw));
					return;
				}
				if	(ns_id == Xow_ns_.Id_file)			// if File ns, add boilerplate header
					app.File_main_wkr().Bld_html(wiki, ctx, html_bfr, page.Ttl(), wiki.Cfg_file_page(), page.File_queue());
				gplx.xowa.html.tidy.Xoh_tidy_mgr tidy_mgr = app.Html_mgr().Tidy_mgr();
				boolean tidy_enabled = tidy_mgr.Enabled();
				Bry_bfr hdom_bfr = tidy_enabled ? app.Utl_bry_bfr_mkr().Get_m001() : html_bfr;	// if tidy, then write to tidy_bfr; note that html_bfr already has <html> and <head> written to it, so this can't be passed to tidy; DATE:2014-06-11
				wiki.Html_mgr().Hdom_wtr().Write_all(hdom_bfr, page.Wiki().Ctx(), page.Root().Data_mid(), page.Root());
				if (tidy_enabled) {
					tidy_mgr.Run_tidy_html(page, hdom_bfr);
					html_bfr.Add_bfr_and_clear(hdom_bfr);
					hdom_bfr.Mkr_rls();
				}
				if (ns_id == Xow_ns_.Id_category)		// if Category, render rest of html (Subcategories; Pages; Files); note that a category may have other html which requires wikitext processing
					wiki.Html_mgr().Ns_ctg().Bld_html(page, html_bfr);
				int ctgs_len = page.Category_list().length;	// add Categories
				if (ctgs_enabled && ctgs_len > 0) {
					app.Usr_dlg().Prog_many("", "", "loading categories: count=~{0}", ctgs_len);
					if (app.Ctg_mgr().Pagecats_grouping_enabled())
						app.Ctg_mgr().Pagectgs_wtr().Write(html_bfr, wiki, page);
					else
						wiki.Html_mgr().Ctg_mgr().Bld(html_bfr, page, ctgs_len);
				}
				break;
		}
		if (	wiki.Domain_tid() != Xow_wiki_domain_.Tid_home	// allow home wiki to use javascript
			&&  !page_tid_uses_pre) {							// if .js, .css or .lua, skip test; may have js fragments, but entire text is escaped and put in pre; don't show spurious warning; DATE:2013-11-21
			int bfr_page_end = html_bfr.Len();
			byte[] cleaned = app.Utl_js_cleaner().Clean(wiki, html_bfr.Bfr(), bfr_page_bgn, bfr_page_end);
			if (cleaned != null) {
				html_bfr.Del_by(bfr_page_end - bfr_page_bgn);
				html_bfr.Add(cleaned);
				app.Usr_dlg().Warn_many("", "", "javascript detected: wiki=~{0} ~{1}", wiki.Domain_str(), String_.new_utf8_(page.Ttl().Full_txt()));
			}
		}
	}
	private void Write_mode_edit(Bry_bfr html_bfr, byte[] data_raw, int ns_id, byte page_tid) {
		if	(	ns_id == Xow_ns_.Id_mediaWiki			// if MediaWiki and wikitext, must be a message; convert args back to php; DATE:2014-06-13
			&&	page_tid == Xow_page_tid.Tid_wikitext
			)
			data_raw = gplx.xowa.apps.Xoa_gfs_php_mgr.Xto_php(tmp_bfr, Bool_.N, data_raw);
		int data_raw_len = data_raw.length;
		if (mgr.Html_capable())
			Xoh_html_wtr_escaper.Escape(page.Wiki().App(), html_bfr, data_raw, 0, data_raw_len, false, false);	// NOTE: must escape; assume that browser will automatically escape (&lt;) (which Mozilla does)
		else
			html_bfr.Add(data_raw);
		if (data_raw_len > 0)		// do not add nl if empty String
			html_bfr.Add_byte_nl();	// per MW:EditPage.php: "Ensure there's a newline at the end, otherwise adding lines is awkward."
	}
	private static final byte[] Content_editable_bry = Bry_.new_ascii_(" contenteditable=\"true\"");
}
