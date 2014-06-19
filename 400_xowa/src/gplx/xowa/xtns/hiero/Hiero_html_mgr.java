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
package gplx.xowa.xtns.hiero; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.html.*;
class Hiero_html_mgr {
	private Bry_bfr html_bfr = Bry_bfr.reset_(Io_mgr.Len_kb), content_bfr = Bry_bfr.reset_(255), tbl_content_bfr = Bry_bfr.reset_(Io_mgr.Len_kb), temp_bfr = Bry_bfr.reset_(255);
	private boolean cartouche_opened = false;
	public static int scale = 100;
	private Hiero_prefab_mgr prefab_mgr; private Hiero_file_mgr file_mgr; private Hiero_phoneme_mgr phoneme_mgr;
	private Hiero_html_wtr wtr;
	public Hiero_html_mgr(Hiero_xtn_mgr xtn_mgr) {
		prefab_mgr = xtn_mgr.Prefab_mgr();
		file_mgr = xtn_mgr.File_mgr();
		phoneme_mgr = xtn_mgr.Phoneme_mgr();
		wtr = new Hiero_html_wtr(this, phoneme_mgr);
	}
	public void Render_blocks(Bry_bfr final_bfr, Hiero_block[] blocks, int scale, boolean hr_enabled) {
		Hiero_html_mgr.scale = scale;
		tbl_content_bfr.Clear(); content_bfr.Clear(); temp_bfr.Clear();
		cartouche_opened = false;
		if (hr_enabled)
			wtr.Hr(html_bfr);
		int blocks_len = blocks.length;
		for (int i = 0; i < blocks_len; i++) {
			Hiero_block block = blocks[i];
			if (block.Len() == 1)
				Render_block_single(content_bfr, hr_enabled, block);
			else
				Render_block_many(content_bfr, hr_enabled, block);
			if (content_bfr.Len_gt_0())
				tbl_content_bfr.Add_bfr_and_clear(content_bfr);	// $tbl_content = $tbl + $content;
		}
		if (tbl_content_bfr.Len_gt_0())
			wtr.Tbl_inner(html_bfr, tbl_content_bfr);
		wtr.Tbl_outer(final_bfr, html_bfr);
	}
	private void Render_block_single(Bry_bfr content_bfr, boolean hr_enabled, Hiero_block block) {
		byte[] code = block.Get_at(0);		// block has only one code (hence the proc name: Render_block_single)
		byte b_0 = code[0];
		switch (b_0) {
			case Byte_ascii.Bang: {			// new_line
				wtr.Tbl_eol(content_bfr);
				if (hr_enabled)
					wtr.Hr(content_bfr);
				break;
			}
			case Byte_ascii.Lt: {			// cartouche bgn
				wtr.Td(content_bfr, Render_glyph(Tkn_lt));
				cartouche_opened = true;
				wtr.Cartouche_bgn(content_bfr);
				break;
			}
			case Byte_ascii.Gt: {			// cartouche end
				wtr.Cartouche_end(content_bfr);
				cartouche_opened = false;
				wtr.Td(content_bfr, Render_glyph(Tkn_gt));
				break;
			}
			default: {						// glyph or '.'
				byte[] td_height = wtr.Td_height(Resize_glyph(code, cartouche_opened));
				wtr.Td(content_bfr, Render_glyph(code, td_height));
				break;
			}
		}
	}
	private void Render_block_many(Bry_bfr content_bfr, boolean hr_enabled, Hiero_block block) {			
		temp_bfr.Clear(); // build prefab_bry: "convert all codes into '&' to test prefabs glyph"
		int block_len = block.Len();
		boolean amp = false;
		for (int i = 0; i < block_len; i++) {
			byte[] v = block.Get_at(i);
			int v_len = v.length;
			amp = false;
			if (v_len == 1) {
				switch (v[0]) {
					case Byte_ascii.Brack_bgn: case Byte_ascii.Brack_end:
					case Byte_ascii.Paren_bgn: case Byte_ascii.Paren_end:
					case Byte_ascii.Asterisk: case Byte_ascii.Colon: case Byte_ascii.Bang:
						amp = true;
						break;
				}
			}
			if (amp)
				temp_bfr.Add_byte(Byte_ascii.Amp);
			else
				temp_bfr.Add(v);
		}
		byte[] prefab_bry = temp_bfr.XtoAryAndClear();
		Hiero_prefab_itm prefab_itm = prefab_mgr.Get_by_key(prefab_bry);
		if (prefab_itm != null) {
			byte[] td_height = wtr.Td_height(Resize_glyph(prefab_bry, cartouche_opened));
			wtr.Td(content_bfr, Render_glyph(prefab_bry, td_height));
		}
		else {
			int line_max = 0, total = 0, height = 0; // get block total height
			byte[] glyph = null;
			for (int i = 0; i < block_len; i++) {
				byte[] v = block.Get_at(i);
				int v_len = v.length;
				if (v_len == 1) {
					switch (v[0]) {
						case Byte_ascii.Colon:
							if (height > line_max)
								line_max = height;
							total += line_max;
							line_max = 0;
							continue;
						case Byte_ascii.Asterisk:
							if (height > line_max)
								line_max = height;
							continue;
					}
				}
				Hiero_phoneme_itm phoneme_itm = phoneme_mgr.Get_by_key(v);
				if (phoneme_itm != null)
					glyph = phoneme_itm.Gardiner_code();
				else
					glyph = v;
				Hiero_file_itm file_itm = file_mgr.Get_by_key(glyph);
				if (file_itm != null)
					height = 2 + file_itm.File_h();
			}
			if (height > line_max)
				line_max = height;
			total += line_max;

			// render all glyph into the block
			for (int i = 0; i < block_len; i++) {
				byte[] v = block.Get_at(i);
				int v_len = v.length;
				if (v_len == 1) {
					switch (v[0]) {
						case Byte_ascii.Colon:
							temp_bfr.Add_str("\n            <br/>");
							continue;
						case Byte_ascii.Asterisk:
							temp_bfr.Add_byte_space();
							continue;
					}
				}
				// resize the glyph according to the block total height
				byte[] td_height = wtr.Td_height(Resize_glyph(v, cartouche_opened, total));
				temp_bfr.Add(Render_glyph(v, td_height));
			}
			wtr.Td(content_bfr, temp_bfr.XtoAryAndClear());
		}
	}
	private byte[] Render_glyph(byte[] src)						{return Render_glyph(src, Bry_.Empty);}
	private byte[] Render_glyph(byte[] src, byte[] td_height) {
		int src_len = src.length; if (src_len == 0) return src; // bounds check
		byte byte_n = src[src_len - 1];
		byte[] img_cls = byte_n == Byte_ascii.Backslash		// REF.MW:isMirrored
			? Bry_cls_mirrored								// 'class="mw-mirrored" '
			: Bry_.Empty
			;
		byte[] glyph = Extract_code(src, src_len);						// trim backslashes from end; REF.MW:extractCode
		if		(Bry_.Eq(glyph, Tkn_dot_dot))						// render void block
			return wtr.Void(Bool_.N);
		else if (Bry_.Eq(glyph, Tkn_dot))							// render 1/2 width void block
			return wtr.Void(Bool_.Y);
		else if (Bry_.Eq(glyph, Tkn_lt))
			return wtr.Cartouche_img(Bool_.Y, glyph);
		else if (Bry_.Eq(glyph, Tkn_gt))
			return wtr.Cartouche_img(Bool_.N, glyph);

		Hiero_phoneme_itm phoneme_itm = phoneme_mgr.Get_by_key(glyph);
		Hiero_file_itm file_itm = null;
		byte[] glyph_esc = Html_utl.Escape_html_as_bry(glyph);
		if (phoneme_itm != null) {
			byte[] code = phoneme_itm.Gardiner_code();
			file_itm = file_mgr.Get_by_key(code);
			if (file_itm != null)
				return wtr.Img_phoneme(img_cls, td_height, glyph_esc, code);
			else
				return glyph_esc;
		}
		file_itm = file_mgr.Get_by_key(glyph);
		return file_itm != null
			? wtr.Img_file(img_cls, td_height, glyph_esc)
			: glyph_esc
			;
	}
	private int Resize_glyph(byte[] item, boolean cartouche_opened)	{return Resize_glyph(item, cartouche_opened, 0);}
	private int Resize_glyph(byte[] item, boolean cartouche_opened, int total) {
		item = Extract_code(item, item.length);
		Hiero_phoneme_itm phoneme_itm = phoneme_mgr.Get_by_key(item);
		byte[] glyph = phoneme_itm == null ? item : phoneme_itm.Gardiner_code();
		int margin = 2 * Image_margin;
		if (cartouche_opened)
			margin += 2 * (int)((Cartouche_width * scale) / 100);
		Hiero_file_itm file_itm = file_mgr.Get_by_key(glyph);
		if (file_itm != null) {
			int height = margin + file_itm.File_h();
			if (total > 0) {
				return total > Max_height
				? (int)(((((height * Max_height) / total) - margin) * scale) / 100)
				: (int)(((height - margin) * scale) / 100)
				;
			}
			else {
				return height > Max_height
				? (int)(((((Max_height * Max_height) / height) - margin) * scale) / 100)
				: (int)(((height - margin) * scale) / 100)
				;
			}
		}
		return (int)(((Max_height - margin) * scale) / 100);
	}
	private static byte[] Extract_code(byte[] src, int src_len) { // trim backslashes from end; REF.MW:extractCode
		return Bry_.Trim_end(src, Byte_ascii.Backslash, src_len);	
	}
	public static final int Image_margin = 1;
	public static final int Cartouche_width = 2;
	public static final int Max_height = 44;
	private static final byte[] Bry_cls_mirrored = Bry_.new_ascii_("class=\"mw-mirrored\" ");
	private static final byte[]
	  Tkn_lt		= new byte[] {Byte_ascii.Lt}
	, Tkn_gt		= new byte[] {Byte_ascii.Gt}
	, Tkn_dot		= new byte[] {Byte_ascii.Dot}
	, Tkn_dot_dot	= new byte[] {Byte_ascii.Dot, Byte_ascii.Dot}
	;
}
class Hiero_html_wtr {
	private Hiero_phoneme_mgr phoneme_mgr;
	private Bry_bfr temp_bfr = Bry_bfr.reset_(255);
	public Hiero_html_wtr(Hiero_html_mgr mgr, Hiero_phoneme_mgr phoneme_mgr) {this.phoneme_mgr = phoneme_mgr;}
	public void Hr(Bry_bfr bfr)			{bfr.Add(Html_consts.Hr_bry).Add_byte_nl();}
	public void Tbl_eol(Bry_bfr bfr)		{bfr.Add(Tbl_eol_bry);}	
	private static final String
	  Tbl_bgn_str = "<table class=\"mw-hiero-table\">"
	;
	private static final byte[] 
	  Tbl_eol_bry = Bry_.new_ascii_(String_.Concat_lines_nl_skip_last
	( ""
	, "        </tr>"
	, "      </table>"
	, "      " + Tbl_bgn_str 
	, "        <tr>"
	));
	public byte[] Td_height(int height) {
		return temp_bfr.Add(Option_bgn_bry).Add_int_variable(height).Add(Option_end_bry).XtoAryAndClear();
	}
	private static final byte[] 
	  Option_bgn_bry = Bry_.new_ascii_("height: ")
	, Option_end_bry = Bry_.new_ascii_("px;")
	;
	public void Td(Bry_bfr bfr, byte[] glyph) {
		bfr.Add(Td_bgn_bry).Add(glyph).Add(Td_end_bry);
	}
	private static final byte[] 
	  Td_bgn_bry = Bry_.new_ascii_("\n          <td>")
	, Td_end_bry = Bry_.new_ascii_("\n          </td>")
	;
	public void Cartouche_bgn(Bry_bfr bfr) {
		bfr.Add(Cartouche_bgn_lhs_bry).Add_int_variable((Hiero_html_mgr.Cartouche_width * Hiero_html_mgr.scale) / 100).Add(Cartouche_bgn_rhs_bry);
	}
	private static final byte[] 
	  Cartouche_bgn_lhs_bry = Bry_.new_ascii_(String_.Concat_lines_nl_skip_last
	( ""
	, "          <td>"
	, "            " + Tbl_bgn_str
	, "              <tr>"
	, "                <td class='mw-hiero-box' style='height: "
	))
	, Cartouche_bgn_rhs_bry = Bry_.new_ascii_(String_.Concat_lines_nl_skip_last
	( "px;'>"
	, "                </td>"
	, "              </tr>"
	, "              <tr>"
	, "                <td>"
	, "                  " + Tbl_bgn_str
	, "                    <tr>"
	))
	;
	public void Cartouche_end(Bry_bfr bfr) {
		bfr.Add(Cartouche_end_lhs_bry).Add_int_variable((Hiero_html_mgr.Cartouche_width * Hiero_html_mgr.scale) / 100).Add(Cartouche_end_rhs_bry);
	}
	private static final byte[] 
	  Cartouche_end_lhs_bry = Bry_.new_ascii_(String_.Concat_lines_nl_skip_last
	( ""
	, "                    </tr>"
	, "                  </table>"
	, "                </td>"
	, "              </tr>"
	, "              <tr>"
	, "                <td class='mw-hiero-box' style='height: "
	))
	, Cartouche_end_rhs_bry = Bry_.new_ascii_(String_.Concat_lines_nl_skip_last
	( "px;'>"
	, "                </td>"
	, "              </tr>"
	, "            </table>"
	, "          </td>"
	));
	public byte[] Cartouche_img(boolean bgn, byte[] glyph) { // render open / close cartouche; note that MW has two branches, but they are both the same
		int height = (int)((Hiero_html_mgr.Max_height * Hiero_html_mgr.scale) / 100);
		Hiero_phoneme_itm phoneme_itm = phoneme_mgr.Get_by_key(glyph); if (phoneme_itm == null) throw Err_.new_fmt_("missing phoneme: {0}", String_.new_utf8_(glyph));
		byte[] code = phoneme_itm.Gardiner_code();
		byte[] title = bgn ? Html_consts.Lt : Html_consts.Gt;
		return cartouche_img_fmtr.Bld_bry_many(temp_bfr, Hiero_xtn_mgr.Img_src_dir, code, height, title);
	}
	private static final Bry_fmtr cartouche_img_fmtr = Bry_fmtr.new_(String_.Concat
	( "\n            <img src='~{path}hiero_~{code}.png'"
	, " height='~{height}' title='~{title}'"
	, " alt='~{title}' />"
	)
	, "path", "code", "height", "title");
	public void Tbl_inner(Bry_bfr html_bfr, Bry_bfr text_bfr) {
		html_bfr.Add(Tbl_inner_bgn).Add_bfr_and_clear(text_bfr).Add(Tbl_inner_end); //	$html .= self::TABLE_START . "<tr>\n" . $tableContentHtml . '</tr></table>';
	}
	private static final byte[] 
	  Tbl_inner_bgn = Bry_.new_utf8_(String_.Concat_lines_nl_skip_last
	( "      <table class=\"mw-hiero-table\">"
	, "        <tr>"
	))
	, Tbl_inner_end = Bry_.new_utf8_(String_.Concat_lines_nl_skip_last
	( ""
	, "        </tr>"
	, "      </table>"
	))
	;
	public void Tbl_outer(Bry_bfr bfr, Bry_bfr html_bfr) {
		bfr.Add(Outer_tbl_bgn);
		bfr.Add_bfr_and_clear(html_bfr);
		bfr.Add(Outer_tbl_end);
	}
	private static final byte[] 
	  Outer_tbl_bgn = Bry_.new_ascii_(String_.Concat_lines_nl_skip_last
	( "<table class='mw-hiero-table mw-hiero-outer' dir='ltr'>"
	, "  <tr>"
	, "    <td>"
	, ""
	)
	)
	, Outer_tbl_end = Bry_.new_ascii_(String_.Concat_lines_nl_skip_last
	( ""
	, "    </td>"
	, "  </tr>"
	, "</table>"
	, ""
	))
	;
	public byte[] Img_phoneme(byte[] img_cls, byte[] td_height, byte[] glyph_esc, byte[] code) {
		byte[] code_esc = Html_utl.Escape_html_as_bry(temp_bfr, code);
		byte[] img_title = temp_bfr.Add(code_esc).Add_byte_space().Add_byte(Byte_ascii.Brack_bgn).Add(glyph_esc).Add_byte(Byte_ascii.Brack_end).XtoAryAndClear(); // "~{code} [~{glyph}]"
		return Img(img_cls, td_height, glyph_esc, code_esc, img_title);
	}
	public byte[] Img_file(byte[] img_cls, byte[] td_height, byte[] glyph_esc) {return Img(img_cls, td_height, glyph_esc, glyph_esc, glyph_esc);}
	private byte[] Img(byte[] img_cls, byte[] td_height, byte[] glyph, byte[] img_src_name, byte[] img_title) {
		byte[] img_src = Bld_img_src(img_src_name);
		return glyph_img_fmtr.Bld_bry_many(temp_bfr, img_cls, Hiero_html_mgr.Image_margin, td_height, img_src, img_title, glyph);
	}
	private static final Bry_fmtr 
	  glyph_img_fmtr	= Bry_fmtr.new_
	  ( "\n            <img ~{img_cls}style='margin: ~{img_margin}px; ~{option}' src='~{img_src}' title='~{img_title}' alt='~{glyph}' />", "img_cls", "img_margin", "option", "img_src", "img_title", "glyph")
	;
	public byte[] Void(boolean half) { // render void
		int width = Hiero_html_mgr.Max_height;
		if (half) width /= 2;
		return void_fmtr.Bld_bry_many(temp_bfr, width);
	}
	private static final Bry_fmtr void_fmtr = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	( ""
	, "            <table class=\"mw-hiero-table\" style=\"width: ~{width}px;\">"
	, "              <tr>"
	, "                <td>&#160;"
	, "                </td>"
	, "              </tr>"
	, "            </table>"
	)
	, "width");
	private static byte[] Bld_img_src(byte[] name) {
		return Bry_.Add(Hiero_xtn_mgr.Img_src_dir, Img_src_prefix, name, Img_src_ext);
	}
	private static final byte[] Img_src_prefix = Bry_.new_ascii_("hiero_"), Img_src_ext = Bry_.new_ascii_(".png");
}
