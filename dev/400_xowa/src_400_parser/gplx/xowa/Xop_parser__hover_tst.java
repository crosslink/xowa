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
public class Xop_parser__hover_tst {
	@Before public void init() {fxt.Clear();} private Xop_parser__hover_fxt fxt = new Xop_parser__hover_fxt();
	@Test  public void Basic() {
		fxt.Test_parse("a b c d", "a b");
		fxt.Test_parse("abc def ghi", "abc def");
//			fxt.Test_parse("''ab'' ''cd'' ef", "a b");
	}
}
class Xop_parser__hover_fxt {
	private Xow_wiki wiki;
	private Xop_parser__hover parser;
	public void Clear() {
		Xoa_app app = Xoa_app_fxt.app_();
		wiki = Xoa_app_fxt.wiki_(app, "en.wiki");
		parser = new Xop_parser__hover();
		parser.Find_min_(2);
		parser.Block_len_(4);
	}
	public Xop_parser__hover_fxt Init_find_min(int v) {parser.Find_min_(v); return this;}
	public Xop_parser__hover_fxt Init_block_len(int v) {parser.Block_len_(v); return this;}
	public void Test_parse(String raw, String expd)  {
		byte[] actl = parser.Parse(wiki, wiki.Ctx(), Bry_.new_utf8_(raw));
		Tfds.Eq(expd, String_.new_utf8_(actl));
	}
}
