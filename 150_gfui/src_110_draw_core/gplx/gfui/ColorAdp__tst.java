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
package gplx.gfui; import gplx.*;
import org.junit.*;
public class ColorAdp__tst {
	@Test  public void parse_hex_() {
		tst_parse_hex_("#00000000", 0, 0, 0, 0);
		tst_parse_hex_("#000102FF", 0, 1, 2, 255);
		tst_parse_hex_("#FF000102", 255, 0, 1, 2);
	}
	@Test  public void parse_int_() {
		tst_parse_int_(0, 0, 0, 0, 0);
		tst_parse_int_(255, 0, 0, 0, 255);
		tst_parse_int_(65535, 0, 0, 255, 255);
		tst_parse_int_(16777215, 0, 255, 255, 255);
		tst_parse_int_(Int_.MaxValue, 127, 255, 255, 255);
		tst_parse_int_(-1, 255, 255, 255, 255);
	}
	@Test  public void parse_() {
		tst_parse_("0,0,0,0", 0, 0, 0, 0);	// parse all ints
		tst_parse_("0,0,0", 255, 0, 0, 0);	// a=255, parse rest
		tst_parse_("255", 0, 0, 0, 255);	// parse as single int
	}
	void tst_parse_hex_(String raw, int a, int r, int g, int b) {
		ColorAdp color = ColorAdp_.parse_hex_(raw);
		tst_ColorAdp(color, a, r, g, b);
		Tfds.Eq(color.XtoHexStr(), raw);
	}
	void tst_parse_int_(int val, int a, int r, int g, int b) {
		ColorAdp color = ColorAdp_.new_int_(val);
		tst_ColorAdp(color, a, r, g, b);
		Tfds.Eq(color.Value(), val);
	}
	void tst_parse_(String s, int alpha, int red, int green, int blue) {tst_ColorAdp(ColorAdp_.parse_(s), alpha, red, green, blue);}
	void tst_ColorAdp(ColorAdp color, int alpha, int red, int green, int blue) {
		TfdsTstr_fxt tstr = TfdsTstr_fxt.new_();
		tstr.Eq_str(color.Alpha(), alpha, "alpha");
		tstr.Eq_str(color.Red(), red, "red");
		tstr.Eq_str(color.Green(), green, "green");
		tstr.Eq_str(color.Blue(), blue, "blue");
		tstr.tst_Equal("color");
	}
}	
