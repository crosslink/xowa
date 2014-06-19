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
package gplx.xowa; import gplx.*;
import org.junit.*;
public class Xop_under_lxr_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before public void init() {fxt.Reset();}
	@After public void term() {fxt.Init_para_n_();}
	@Test   public void Toc_basic() {
		fxt.Test_parse_page_all_str("a__TOC__b", "ab");
	}
	@Test   public void Toc_match_failed() {
		fxt.Test_parse_page_all_str("a__TOCA__b", "a__TOCA__b");
	}
	@Test   public void Toc_match_ci() {
		fxt.Test_parse_page_all_str("a__toc__b", "ab");
	}
	@Test   public void Notoc_basic() {
		fxt.Wtr_cfg().Toc_show_(true);	// NOTE: must enable in order for TOC to show (and to make sure NOTOC suppresses) 
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl
		(	"__NOTOC__"
		,	"==a=="
		,	"==b=="
		,	"==c=="
		,	"==d=="
		), String_.Concat_lines_nl
		(	"<h2><span class='mw-headline' id='a'>a</span></h2>"
		,	""
		,	"<h2><span class='mw-headline' id='b'>b</span></h2>"
		,	""
		,	"<h2><span class='mw-headline' id='c'>c</span></h2>"
		,	""
		,	"<h2><span class='mw-headline' id='d'>d</span></h2>"
		));
		fxt.Wtr_cfg().Toc_show_(false);
	}
	@Test   public void Ignore_pre() {
		fxt.Init_para_y_();
		fxt.Test_parse_page_all_str("a\n   __NOTOC__\n", String_.Concat_lines_nl
		(	"<p>a"
		,	"</p>"		// NOTE: do not capture "   " in front of __NOTOC__; confirmed against MW; DATE:2014-02-19
		,	""
		,	"<p><br/>"
		,	"</p>"
		));
		fxt.Init_para_n_();
	}
	@Test   public void Toc_works() {	// PURPOSE: make sure "suppressed" pre does not somehow suppress TOC
		fxt.Init_para_y_();
		fxt.Test_parse_page_all_str("a\n__TOC__\n==b==\n", String_.Concat_lines_nl
		( "<p>a"
		, "</p>"
		, "<div id=\"toc\" class=\"toc\">"
		, "  <div id=\"toctitle\">"
		, "    <h2>Contents</h2>"
		, "  </div>"
		, "  <ul>"
		, "    <li class=\"toclevel-1 tocsection-1\"><a href=\"#b\"><span class=\"tocnumber\">1</span> <span class=\"toctext\">b</span></a>"
		, "    </li>"
		, "  </ul>"
		, "</div>"
		, ""
		, "<h2>b</h2>"
		));
		fxt.Init_para_n_();
	}		
	@Test  public void Ignore_pre_after() {	// PURPOSE: "__TOC__\s\n" must be trimmed at end, else false pre; assertion only (no code exists to handle this test);  DATE:2013-07-08
		fxt.Init_para_y_();
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl
		(	"a"
		,	"__NOTOC__ "
		,	"b"
		), String_.Concat_lines_nl
		(	"<p>a"
		,	"</p>"		// NOTE: do not capture " "; confirmed against MW; DATE:2014-02-19
		,	""
		,	"<p>b"
		,	"</p>"
		));
		fxt.Init_para_n_();
	}
	@Test  public void Disambig() {	// PURPOSE: ignore "__DISAMBIG__"; EX:{{disambiguation}} DATE:2013-07-24
		fxt.Test_parse_page_all_str("__DISAMBIG__", "");
	}
	@Test  public void Nocontentconvert() {	 // simple test; test for flag only; DATE:2014-02-06
		Tfds.Eq(fxt.Page().Lang_convert_content(), true);
		Tfds.Eq(fxt.Page().Lang_convert_title(), true);
		fxt.Test_parse_page_all_str("__NOCONTENTCONVERT__ __NOTITLECONVERT__", " ");
		Tfds.Eq(fxt.Page().Lang_convert_content(), false);
		Tfds.Eq(fxt.Page().Lang_convert_title(), false);
	}
	@Test  public void Eos() {	// PURPOSE: check that __ at eos doesn't fail; es.s:Luisa de Bustamante: 3; DATE:2014-02-15
		fxt.Test_parse_page_all_str("__", "__");
	}
	@Test  public void Ws_preserve() {	// preserve ws; DATE:2014-04-07
		fxt.Init_para_y_();
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl
		(	"a"
		,	" __TOC__ "
		,	"b"
		), String_.Concat_lines_nl
		(	"<p>a"
		,	"</p>"
		,	" "
		,	""
		,	"<p>b"
		,	"</p>"
		));
		fxt.Init_para_n_();
	}
	@Test  public void Hook_alt() {	// PURPOSE: ja wikis use alternate __; DATE:2014-03-04
		Xow_wiki wiki = fxt.Wiki(); Xol_lang lang = wiki.Lang();
		Xol_kwd_grp kwd_grp = lang.Kwd_mgr().Get_or_new(Xol_kwd_grp_.Id_toc);
		kwd_grp.Srl_load(true, new byte[][] {Bry_.new_utf8_("＿＿TOC＿＿")});
		wiki.Parser().Init_by_lang(lang);
		fxt.Test_parse_page_all_str("a＿＿TOC＿＿b", "ab");
	}
}
