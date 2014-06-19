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
public class Xop_tblw_wkr_double_pipe_tst {
	@Before public void init() {fxt.Reset(); fxt.Init_para_y_();} private Xop_fxt fxt = new Xop_fxt();
	@After public void term() {fxt.Init_para_n_();}
	@Test  public void No_tblw() {			// PURPOSE: if || has no tblw, treat as lnki; none; DATE:2014-05-06
		fxt.Test_parse_page_all_str("[[A||b|c]]", String_.Concat_lines_nl_skip_last
		( "<p><a href=\"/wiki/A\">b|c</a>"	// NOTE: technically this should be "|b|c", but difficult to implement; DATE:2014-05-06
		, "</p>"
		, ""
		));
	}
	@Test  public void Image_map() {// PURPOSE: if || is inside table and imagemap, treat as lnki; EX:w:United_States_presidential_election,_1992; DATE:2014-03-29; DATE:2014-05-06
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skip_last
		( "{|"
		, "|-"
		, "| z"
		, "<imagemap>"
		, "File:A.png||123px|b"	// NOTE: "||" should not be tblw; also should not be pipe + text; if it is pipe + text, then caption will be "|123px" and width will be -1; DATE:2014-05-06
		, "</imagemap>"
		, "|}"
		) , String_.Concat_lines_nl_skip_last
		( "<table>"
		, "  <tr>"
		, "    <td> z"
		, "<a href=\"/wiki/File:A.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"b\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/123px.png\" width=\"123\" height=\"0\" /></a>"	// NOTE: width must be 123, not 0
		, "    </td>"
		, "  </tr>"
		, "</table>"
		, ""
		)
		);
	}

	@Test  public void Tblw_lnki_nth() {	// PURPOSE: if || is nth pipe, then treat as lnki; EX:en.w:Main_Page;de.w:Main_Page; DATE:2014-05-06
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skip_last
		( "{|"
		, "|[[File:A.png|b||c]]"
		, "|}"
		) , String_.Concat_lines_nl_skip_last
		( "<table>"
		, "  <tr>"
		, "    <td><a href=\"/wiki/File:A.png\" class=\"image\" xowa_title=\"A.png\"><img id=\"xowa_file_img_0\" alt=\"c\" src=\"file:///mem/wiki/repo/trg/orig/7/0/A.png\" width=\"0\" height=\"0\" /></a>"
		, "    </td>"
		, "  </tr>"
		, "</table>"
		, ""
		)
		);
	}
	@Test  public void Tblw_lnki_list_1st() {	// PURPOSE: if || is 1st pipe, but inside list, then treat as lnki; EX:w:Second_Boer_War; DATE:2014-05-05
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skip_last
		( "{|"
		, "|"
		, "*[[A||b]]"
		, "|}"
		) , String_.Concat_lines_nl_skip_last
		( "<table>"
		, "  <tr>"
		, "    <td>"
		, ""
		, "      <ul>"
		, "        <li><a href=\"/wiki/A\">b</a>"	// NOTE: technically this should be "|b", but difficult to implement; DATE:2014-05-06
		, "        </li>"
		, "      </ul>"
		, "    </td>"
		, "  </tr>"
		, "</table>"
		, ""
		)
		);
	}
}
