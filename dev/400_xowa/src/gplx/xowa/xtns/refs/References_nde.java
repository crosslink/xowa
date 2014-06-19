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
import gplx.xowa.html.*;
public class References_nde implements Xox_xnde, Xop_xnde_atr_parser {
	public byte[] Group() {return group;} public References_nde Group_(byte[] v) {group = v; return this;} private byte[] group = Bry_.Empty;
	public int List_idx() {return list_idx;} public References_nde List_idx_(int v) {list_idx = v; return this;} private int list_idx;
	public void Xatr_parse(Xow_wiki wiki, byte[] src, Xop_xatr_itm xatr, Object xatr_key_obj) {
		if (xatr_key_obj == null) return;
		Byte_obj_val xatr_key = (Byte_obj_val)xatr_key_obj;
		switch (xatr_key.Val()) {
			case Xatr_id_group:		{
				group = xatr.Val_as_bry(src);
				if (Bry_.Match(group, Bry_group)) group = Bry_.Empty;  // HACK: reflist returns <references group>
				break;
			}
		}
	}	private static byte[] Bry_group = Bry_.new_ascii_("group");
	public void Xtn_parse(Xow_wiki wiki, Xop_ctx ctx, Xop_root_tkn cur_root, byte[] src, Xop_xnde_tkn xnde) {
		ctx.Para().Process_block__bgn_n__end_y(Xop_xnde_tag_.Tag_div);	// xnde generates <block_node>; <references> -> <ol>; close any blocks; EX: fr.w:Heidi_(roman); DATE:2014-02-17
		Xop_xatr_itm.Xatr_parse(wiki.App(), this, wiki.Lang().Xatrs_references(), wiki, src, xnde);
		if (xnde.CloseMode() == Xop_xnde_tkn.CloseMode_pair) {
			int itm_bgn = xnde.Tag_open_end(), itm_end = xnde.Tag_close_bgn();
			Xop_ctx references_ctx = Xop_ctx.new_sub_(wiki).References_group_(group);
			references_ctx.Para().Enabled_n_();	// disable para during <references> parsing
			byte[] references_src = Bry_.Mid(src, itm_bgn, itm_end);
			Xop_tkn_mkr tkn_mkr = ctx.Tkn_mkr();
			Xop_root_tkn root = tkn_mkr.Root(src);
			wiki.Parser().Parse_text_to_wdom(root, references_ctx, tkn_mkr, references_src, Xop_parser_.Doc_bgn_char_0);
		}
		list_idx = ctx.Cur_page().Ref_mgr().Grps_get(group).Grp_seal();	// NOTE: needs to be sealed at end; else inner refs will end up in new group; EX: <references><ref>don't seal prematurely</ref></references>
	}
	public void Xtn_write(Xoa_app app, Xoh_hdom_wtr html_wtr, Xoh_html_wtr_ctx opts, Xop_ctx ctx, Bry_bfr bfr, byte[] src, Xop_xnde_tkn xnde) {
		html_wtr.Ref_wtr().Xnde_references(html_wtr, ctx, opts, bfr, src, xnde);
	}
	public static final byte Xatr_id_group = 0;
	public static boolean Enabled = true;
}
