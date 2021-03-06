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
package gplx.xowa.langs.numbers; import gplx.*; import gplx.xowa.*; import gplx.xowa.langs.*;
public class Xol_num_grp_fmtr {
	public boolean Mode_is_regx() {return digit_grouping_pattern == null || Bry_.Eq(digit_grouping_pattern, Digit_grouping_pattern_normal);}
	public byte[] Digit_grouping_pattern() {return digit_grouping_pattern;} public void Digit_grouping_pattern_(byte[] v) {digit_grouping_pattern = v;} private byte[] digit_grouping_pattern;
	public void Clear() {digit_grouping_pattern = null;}
	public byte[] Fmt_regx(Bry_bfr bfr, byte[] src) {// NOTE: specific code to handle preg_replace( '/(\d{3})(?=\d)(?!\d*\.)/', '$1,', strrev( $number ) ) );"; DATE:2014-04-15
		int src_len = src.length;
		int bgn = 0;
		int pos = bgn;
		boolean dirty = false;
		int grp_len = 3;
		while (true) {
			if (pos == src_len) break;
			byte b = src[pos];
			switch (b) {
				case Byte_ascii.Num_0: case Byte_ascii.Num_1: case Byte_ascii.Num_2: case Byte_ascii.Num_3: case Byte_ascii.Num_4:
				case Byte_ascii.Num_5: case Byte_ascii.Num_6: case Byte_ascii.Num_7: case Byte_ascii.Num_8: case Byte_ascii.Num_9: {
					int num_end = Bry_finder.Find_fwd_while_num(src, pos, src_len);
					int num_len = num_end - pos;
					if (num_len > grp_len) {
						if (!dirty) {
							bfr.Add_mid(src, bgn, pos);
							dirty = true;
						}
						Fmt_grp(bfr, src, pos, num_end, num_len, grp_len);
					}
					else {
						if (dirty)
							bfr.Add_mid(src, pos, num_end);
					}
					pos = num_end;
					break;
				}
				case Byte_ascii.Dot: {
					int num_end = Bry_finder.Find_fwd_while_num(src, pos + 1, src_len);	// +1 to skip dot
					if (dirty)
						bfr.Add_mid(src, pos, num_end);
					pos = num_end;
					break;
				}
				default:
					if (dirty)
						bfr.Add_byte(b);
					++pos;
					break;
			}
		}
		return dirty ? bfr.XtoAryAndClear() : src;
	}
	private void Fmt_grp(Bry_bfr bfr, byte[] src, int bgn, int end, int len, int grp_len) {
		int seg_0 = bgn + (len % grp_len);	// 5 digit number will have seg_0 of 2; 12345 -> 12,345
		for (int i = bgn; i < end; i++) {
			if (	i != bgn						// never format at bgn; necessary for even multiples of grp_len (6, 9)
				&& (	i == seg_0					// seg_0
					|| (i - seg_0) % grp_len == 0	// seg_n
					)
				) {
				bfr.Add_byte(Byte_ascii.Comma);	// MW: hard-coded
			}
			bfr.Add_byte(src[i]);
		}
	}
	private static final byte[] Digit_grouping_pattern_normal = Bry_.new_ascii_("###,###,###");
}
