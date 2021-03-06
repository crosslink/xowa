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
public class Xob_xdat_file_tst {
	@Test  public void Find() {
		Xob_xdat_file rdr = rdr_("!!!!%|!!!!%|!!!!%|!!!!%|!!!!%|", "0|b", "1|d", "2|f", "3|h", "4|j");
		tst_ReadAt(rdr, 0, "0|b");
		tst_ReadAt(rdr, 1, "1|d");
		tst_ReadAt(rdr, 2, "2|f");
		tst_ReadAt(rdr, 3, "3|h");
		tst_ReadAt(rdr, 4, "4|j");
		tst_Find(rdr, "b", 0, false);
		tst_Find(rdr, "j", 4, false);
		tst_Find(rdr, "a", 0, false);
		tst_Find(rdr, "c", 1, false);
		tst_Find(rdr, "k", 4, false);
	}
	@Test  public void Update() {
		Xob_xdat_file rdr = rdr_("!!!!%|!!!!%|!!!!%|!!!!%|!!!!%|", "0|b", "1|d", "2|f", "3|h", "4|j");
		tst_Update(rdr, 3, "3|h1\n", String_.Concat_lines_nl_skip_last
		(	"!!!!%|!!!!%|!!!!%|!!!!&|!!!!%|"
		,	"0|b"
		,	"1|d"
		,	"2|f"
		,	"3|h1"
		,	"4|j"
		,	""
		));
	}
	@Test  public void Insert() {
		Xob_xdat_file rdr = rdr_("!!!!%|!!!!%|!!!!%|!!!!%|!!!!%|", "0|b", "1|d", "2|f", "3|h", "4|j");
		tst_Insert(rdr, "5|k\n", String_.Concat_lines_nl_skip_last
		(	"!!!!%|!!!!%|!!!!%|!!!!%|!!!!%|!!!!%|"
		,	"0|b"
		,	"1|d"
		,	"2|f"
		,	"3|h"
		,	"4|j"
		,	"5|k"
		,	""
		));
	}
	@Test  public void Sort() {
		Xob_xdat_file rdr = rdr_("!!!!%|!!!!%|!!!!%|!!!!%|!!!!%|", "4|j", "2|f", "0|b", "1|d", "3|h");
		Bry_comparer_bgn_eos comparer = new Bry_comparer_bgn_eos(2);
		tst_Sort(rdr, comparer, String_.Concat_lines_nl_skip_last
		(	"!!!!%|!!!!%|!!!!%|!!!!%|!!!!%|"
		,	"0|b"
		,	"1|d"
		,	"2|f"
		,	"3|h"
		,	"4|j"
		,	""
		));
	}
	@Test   public void Rebuild_header() {
		String orig = String_.Concat_lines_nl(""								, "4|j", "2|f", "0|b", "1|d", "3|h");
		String expd = String_.Concat_lines_nl("!!!!%|!!!!%|!!!!%|!!!!%|!!!!%|"	, "4|j", "2|f", "0|b", "1|d", "3|h");
		Rebuild_header_tst(orig, expd);
	}
	private void Rebuild_header_tst(String orig, String expd) {		
		Tfds.Eq_str_lines(expd, String_.new_ascii_(Xob_xdat_file.Rebuid_header(Bry_.new_ascii_(orig), Bry_.new_ascii_("\n"))));		
	}
	Bry_bfr tmp = Bry_bfr.new_();
	private void tst_Sort(Xob_xdat_file rdr, gplx.lists.ComparerAble comparer, String expd) {
		rdr.Sort(tmp, comparer);
		Chk_file(rdr, expd);
	}
	private void tst_Insert(Xob_xdat_file rdr, String new_val, String expd) {
		rdr.Insert(tmp, Bry_.new_utf8_(new_val));
		Chk_file(rdr, expd);
	}
	private void tst_Update(Xob_xdat_file rdr, int idx, String new_val, String expd) {
		Xob_xdat_itm itm = new Xob_xdat_itm(); 
		rdr.GetAt(itm, idx);
		rdr.Update(tmp, itm, Bry_.new_utf8_(new_val));
		Chk_file(rdr, expd);
	}
	private void Chk_file(Xob_xdat_file rdr, String expd) {
		Io_url url = Io_url_.new_fil_("mem/test.xdat");
		rdr.Save(url);
		String actl = Io_mgr._.LoadFilStr(url);
		Tfds.Eq_str_lines(expd, actl);		
	}
	private void tst_Find(Xob_xdat_file rdr, String find, int expd, boolean exact) {
		rdr.Find(itm, Bry_.new_utf8_(find), 2, Byte_ascii.NewLine, exact);
		int id = Bry_.X_to_int_or(Bry_.Mid(itm.Itm_bry(), 0, 1), -1);
			Tfds.Eq(expd, id);
	}
	private void tst_ReadAt(Xob_xdat_file rdr, int i, String expd) {rdr.GetAt(itm, i); Tfds.Eq(expd, String_.new_utf8_(itm.Itm_bry()));}
	Xob_xdat_itm itm = new Xob_xdat_itm();
	Xob_xdat_file rdr_(String... lines) {
		String_bldr sb = String_bldr_.new_();
		int len = lines.length;
		for (int i = 0; i < len; i++) {
			String line = lines[i];
			sb.Add(line).Add_char_nl();
		}
		byte[] bry = Bry_.new_utf8_(sb.XtoStr());
		return new Xob_xdat_file().Parse(bry, bry.length, Io_url_.Null);
	}
}
