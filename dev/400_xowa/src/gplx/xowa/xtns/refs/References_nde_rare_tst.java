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
package gplx.xowa.xtns.refs; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class References_nde_rare_tst {		
	@Before public void init() {fxt.Page().Ref_mgr().Grps_clear(); fxt.Reset();} private Xop_fxt fxt = new Xop_fxt();
	@After public void term() {fxt.Init_para_n_();}
	@Test  public void Recursive() {	// PURPOSE: handle recursive situations; EX: ja.w:Kソリューション ; ja.w:Template:cite web。; DATE:2014-03-05
		fxt.Init_page_create("Template:Recursive", "<ref>{{Recursive}}</ref>");
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skip_last
		(	"<ref>{{Recursive}}</ref>"
		,	"<references/>"
		), String_.Concat_lines_nl_skip_last
		(	"<sup id=\"cite_ref-1\" class=\"reference\"><a href=\"#cite_note-1\">[2]</a></sup>"
		,	"<ol class=\"references\">"
		,	"<li id=\"cite_note-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-0\">^</a></span> <span class=\"reference-text\"><span class=\"error\">Template loop detected:Recursive</span></span></li>"
		,	"<li id=\"cite_note-1\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-1\">^</a></span> <span class=\"reference-text\"><sup id=\"cite_ref-0\" class=\"reference\"><a href=\"#cite_note-0\">[1]</a></sup></span></li>"
		,	"</ol>"
		,	""
		));
	}
	@Test  public void Backlabel_out_of_range() {	// PURPOSE: handle more backlabels than expected; PAGE:en.w:List_of_Russula_species; DATE:2014-06-07
		Ref_html_wtr_cfg cfg = fxt.Wiki().Html_mgr().Hdom_wtr().Ref_wtr().Cfg();
		byte[][] old = cfg.Backlabels();
		cfg.Backlabels_(Bry_.Ary("a"));
		fxt.Wiki().Msg_mgr().Get_or_make(Ref_html_wtr_cfg.Msg_backlabels_err).Atrs_set(Bry_.new_ascii_("Ran out of custom link labels for group ~{0}."), true, false);
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skip_last
		(	"<ref name='ref_1'>a</ref><ref name='ref_1'>b</ref>"
		,	"<references/>"
		), String_.Concat_lines_nl_skip_last
		(	"<sup id=\"cite_ref-ref_1_0-0\" class=\"reference\"><a href=\"#cite_note-ref_1-0\">[1]</a></sup><sup id=\"cite_ref-ref_1_0-1\" class=\"reference\"><a href=\"#cite_note-ref_1-0\">[1]</a></sup>"
		,	"<ol class=\"references\">"
		,	"<li id=\"cite_note-ref_1-0\"><span class=\"mw-cite-backlink\">^ <sup><a href=\"#cite_ref-ref_1_0-0\">a</a></sup> <sup><a href=\"#cite_ref-ref_1_0-1\">Ran out of custom link labels for group 1.</a></sup></span> <span class=\"reference-text\">a</span></li>"
		,	"</ol>"
		,	""
		));
		cfg.Backlabels_(old);
	}
}
