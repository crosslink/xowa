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
package gplx.xowa.parsers.lnkes; import gplx.*; import gplx.xowa.*; import gplx.xowa.parsers.*;
import gplx.xowa.apps.fsys.*; import gplx.xowa.net.*;
public class Xop_lnke_wkr implements Xop_ctx_wkr {
	public void Ctor_ctx(Xop_ctx ctx) {url_parser = ctx.App().Url_parser().Url_parser();} Gfo_url_parser url_parser; Gfo_url_site_data site_data = new Gfo_url_site_data(); Xoa_url_parser xo_url_parser = new Xoa_url_parser(); Xoa_url xo_url_parser_url = new Xoa_url();
	public void Page_bgn(Xop_ctx ctx, Xop_root_tkn root) {}
	public void Page_end(Xop_ctx ctx, Xop_root_tkn root, byte[] src, int src_len) {}
	public void AutoClose(Xop_ctx ctx, byte[] src, int src_len, int bgn_pos, int cur_pos, Xop_tkn_itm tkn) {
		// "[" but no "]"; EX: "[irc://a"; NOTE: lnkes that start with protocol will be ac'd in MakeTkn_bgn; EX: "http://a"
		Xop_lnke_tkn bgn_tkn = (Xop_lnke_tkn)tkn;
		bgn_tkn.Lnke_typ_(Xop_lnke_tkn.Lnke_typ_brack_dangling);
		bgn_tkn.Src_end_(bgn_tkn.Lnke_end()); // NOTE: endPos is lnke_end, not cur_pos or src_len; EX: "[irc://a b", lnk ends at a, not b; NOTE: still bgns at [
		ctx.Msg_log().Add_itm_none(Xop_lnke_log.Dangling, src, tkn.Src_bgn(), cur_pos);
	}
	public static final String Str_xowa_protocol = "xowa-cmd:";
	public static final byte[] Bry_xowa_protocol = Bry_.new_ascii_(Str_xowa_protocol);
	public int MakeTkn_bgn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos, byte[] protocol, byte proto_tid, byte lnke_type) {
		boolean lnke_type_brack = (lnke_type == Xop_lnke_tkn.Lnke_typ_brack);
		if (!lnke_type_brack && !Valid_text_lnke(ctx, src, src_len, bgn_pos, cur_pos)) return ctx.Lxr_make_txt_(cur_pos);
		if (ctx.Stack_get_typ(Xop_tkn_itm_.Tid_lnke) != null) return ctx.Lxr_make_txt_(cur_pos); // no nested lnke; return cur lnke as text; EX: "[irc://a irc://b]" -> "<a href='irc:a'>irc:b</a>"
		if (proto_tid == Xoo_protocol_itm.Tid_xowa) return Make_tkn_xowa(ctx, tkn_mkr, root, src, src_len, bgn_pos, cur_pos, protocol, proto_tid, lnke_type);

		// HACK: need to disable lnke if enclosing type is lnki and (1) arg is "link=" or (2) in 1st arg; basically, only enable for caption tkns (and preferably, thumb only) (which should be neither 1 or 2)
		if (ctx.Cur_tkn_tid() == Xop_tkn_itm_.Tid_lnki && lnke_type == Xop_lnke_tkn.Lnke_typ_text) {
			byte mode = Lnki_linkMode_init;
			int lnki_pipe_count = 0;
			int tkn_idx = -1;
			for (int i = root.Subs_len() - 1; i > -1; i--) {
				Xop_tkn_itm link_tkn = root.Subs_get(i);
				tkn_idx = i;
				switch (link_tkn.Tkn_tid()) {
					case Xop_tkn_itm_.Tid_pipe:
						if (mode == Lnki_linkMode_text) {ctx.Lxr_make_(false); return bgn_pos + 1;}	// +1 to position after lnke_hook; EX:[[File:A.png|link=http:b.org]] position at t in http so http hook won't be invoked.
						else {i = -1; ++lnki_pipe_count;}
						break;
					case Xop_tkn_itm_.Tid_txt:
						if (mode == Lnki_linkMode_eq) mode = Lnki_linkMode_text;
						// else i = -1; // DELETE: do not be overly strict; need to handle pattern of link=http://a.org?b=http://c.org; DATE:2013-02-03
						break;
					case Xop_tkn_itm_.Tid_eq:
						if (mode == Lnki_linkMode_init) mode = Lnki_linkMode_eq;
						// else i = -1; // DELETE: do not be overly strict; need to handle pattern of link=http://a.org?b=http://c.org; DATE:2013-02-03
						break;
					case Xop_tkn_itm_.Tid_space: case Xop_tkn_itm_.Tid_tab:
						break;
				}
			}
			if (lnki_pipe_count == 0) {				
				for (int i = tkn_idx; i > -1; i--) {
					Xop_tkn_itm link_tkn = root.Subs_get(i);
					tkn_idx = i;
					switch (link_tkn.Tkn_tid()) {
//							case Xop_tkn_itm_.Tid_txt: return cur_pos;	// REMOVED:2012-11-12: was causing [[http://a.org a]] [[http://b.org b]] to fail; EX.WP:Template:Infobox_country
						case Xop_tkn_itm_.Tid_space: case Xop_tkn_itm_.Tid_tab: break;
					}
				}
			}
		}
		int lnke_bgn = bgn_pos, lnke_end = -1, brack_end_pos = -1;
		int lnke_endType = EndType_null;
		while (true) {	// loop until lnke_endType char;
			if (cur_pos == src_len) {lnke_endType = EndType_eos; lnke_end = cur_pos; break;}
			switch (src[cur_pos]) {
				case Byte_ascii.Brack_end:
					if (lnke_type_brack) {	// NOTE: check that frame begins with [ in order to end with ] 
						lnke_endType = EndType_brack; brack_end_pos = cur_pos + Launcher_app_mgr.Adj_next_char;
					}
					else {					// NOTE: frame does not begin with [ but ] encountered. mark "invalid" in order to force parser to stop before "]"
						lnke_endType = EndType_invalid;
					}
					break;
				case Byte_ascii.Space:		lnke_endType = EndType_space; break;
				case Byte_ascii.NewLine:	lnke_endType = EndType_nl; break;
				case Byte_ascii.Gt: case Byte_ascii.Lt: 
					lnke_endType = EndType_invalid;
					break;
				case Byte_ascii.Apos:
					if (cur_pos + 1 < src_len && src[cur_pos + 1] == Byte_ascii.Apos)	// NOTE: '' breaks link, but not '; EX: [http://a.org''b'']]; DATE:2013-03-18
						lnke_endType = EndType_invalid;
					break;
			}
			if  (lnke_endType == EndType_null) 	cur_pos++;
			else {
				lnke_end = cur_pos;
				cur_pos++;
				break;
			}
		}
		if (lnke_type_brack) {
			switch (lnke_endType) {
				case EndType_eos:
					if (brack_end_pos == -1) {	// eos but no ]; EX: "[irc://a"
						ctx.Subs_add(root, tkn_mkr.Txt(bgn_pos, bgn_pos + 1));// convert open brack to txt;	// FUTURE: don't make brack_tkn; just flag
						bgn_pos += 1;
						brack_end_pos = cur_pos;
						lnke_bgn = bgn_pos;
						lnke_type = Xop_lnke_tkn.Lnke_typ_brack_dangling;
					}
					break;
				case EndType_nl:
					lnke_type = Xop_lnke_tkn.Lnke_typ_brack_dangling;
					return ctx.Lxr_make_txt_(lnke_end);	// textify lnk; EX: [irc://a\n] textifies "[irc://a"
				default:
					lnke_bgn += proto_tid == Xoo_protocol_itm.Tid_relative_2 ? 2 : 1;	// if Tid_relative_2, then starts with [[; adjust by 2; EX:"[[//en" should have lnke_bgn at "//en", not "[//en"
					lnke_type = Xop_lnke_tkn.Lnke_typ_brack;
					break;
			}
		}
		else {	// else, plain text
			brack_end_pos = lnke_end;
			lnke_type = Xop_lnke_tkn.Lnke_typ_text;
			if (ctx.Cur_tkn_tid() == Xop_tkn_itm_.Tid_lnki) {	// SEE:NOTE_1
				Xop_tkn_itm prv_tkn = root.Subs_get(root.Subs_len() - 1);	// get last tkn
				if (prv_tkn.Tkn_tid() == Xop_tkn_itm_.Tid_lnki) {		// is tkn lnki?
					root.Subs_del_after(prv_tkn.Tkn_sub_idx());				// delete [[ tkn and replace with [ tkn
					root.Subs_add(tkn_mkr.Txt(prv_tkn.Src_bgn(), prv_tkn.Src_bgn() + 1));
					ctx.Stack_pop_last();							// don't forget to remove from stack
					lnke_type = Xop_lnke_tkn.Lnke_typ_brack;		// change lnke_typee to brack
					--bgn_pos;
				}
			}
		}
		if (proto_tid == Xoo_protocol_itm.Tid_relative_2)	// for "[[//", add "["; rest of code handles "[//" normally, but still want to include literal "["; DATE:2013-02-02
			ctx.Subs_add(root, tkn_mkr.Txt(lnke_bgn - 1, lnke_bgn));
		Xop_lnke_tkn tkn = tkn_mkr.Lnke(bgn_pos, brack_end_pos, protocol, proto_tid, lnke_type, lnke_bgn, lnke_end);
		url_parser.Parse_site_fast(site_data, src, lnke_bgn, lnke_end);
		int site_bgn = site_data.Site_bgn(), site_end = site_data.Site_end(); tkn.Lnke_relative_(site_data.Rel());
		Xow_xwiki_itm xwiki = ctx.App().User().Wiki().Xwiki_mgr().Get_by_mid(src, site_bgn, site_end);	// NOTE: check User_wiki.Xwiki_mgr, not App.Wiki_mgr() b/c only it is guaranteed to know all wikis on system
		if (xwiki != null) {	// lnke is to an xwiki; EX: [http://en.wikipedia.org/A a]
			Xow_wiki wiki = ctx.Wiki();
			Xoa_url_parser.Parse_url(xo_url_parser_url, ctx.App(), wiki, src, lnke_bgn, lnke_end, false);
			byte[] xwiki_wiki = xo_url_parser_url.Wiki_bry();
			byte[] xwiki_page = xo_url_parser_url.Page_bry();
			byte[] ttl_bry = xo_url_parser_url.Page_bry();
			Xoa_ttl ttl = Xoa_ttl.parse_(wiki, ttl_bry);
			if (ttl != null && ttl.Wik_itm() != null) {
				xwiki_wiki = ttl.Wik_itm().Domain();
				xwiki_page = ttl.Page_url();
			}
			tkn.Lnke_xwiki_(xwiki_wiki, xwiki_page, xo_url_parser_url.Args());
		}			
		ctx.Subs_add(root, tkn);
		if (lnke_type == Xop_lnke_tkn.Lnke_typ_brack) {
			if (lnke_endType == EndType_brack) {
				tkn.Src_end_(cur_pos);
				tkn.Subs_move(root);
				return cur_pos;
			}
			ctx.Stack_add(tkn);
			if (lnke_endType == EndType_invalid) {
				return cur_pos - 1;	// -1 to return before < or >
			}
		}
		else {
			switch (lnke_endType) {
				case EndType_space:
					ctx.Subs_add(root, tkn_mkr.Space(root, cur_pos - 1, cur_pos));
					break;			
				case EndType_nl:
				case EndType_invalid:	// NOTE that cur_pos is set after <, must subtract 1 else </xnde> will be ignored; EX: <span>irc://a</span>
					return cur_pos - 1;
			}
		}
		return cur_pos;
	}
	private static final byte Lnki_linkMode_init = 0, Lnki_linkMode_eq = 1, Lnki_linkMode_text = 2;
	private static final byte EndType_null = 0, EndType_eos = 1, EndType_brack = 2, EndType_space = 3, EndType_nl = 4, EndType_invalid = 5;
	public int MakeTkn_end(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
//			Xop_tkn_itm last_tkn = ctx.Stack_get_last();		// BLOCK:invalid_ttl_check; // TODO: backout apos changes
//			if (	last_tkn != null
//				&&	last_tkn.Tkn_tid() == Xop_tkn_itm_.Tid_lnki) {
//				Xop_lnki_tkn lnki = (Xop_lnki_tkn)last_tkn;
//				if (	lnki.Pipe_count_is_zero()) {	// always invalid
//					ctx.Stack_pop_last();
//					return Xop_lnki_wkr_.Invalidate_lnki(ctx, src, root, lnki, bgn_pos);
//				}
//			}
		int lnke_bgn_idx = ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_lnke);
		if (lnke_bgn_idx == -1) return ctx.Lxr_make_txt_(cur_pos);	// no lnke_bgn tkn; occurs when just ]; EX: "a]b"
		Xop_lnke_tkn bgnTkn = (Xop_lnke_tkn)ctx.Stack_pop_til(root, src, lnke_bgn_idx, false, bgn_pos, cur_pos);
		bgnTkn.Src_end_(cur_pos);
		bgnTkn.Subs_move(root);
		return cur_pos;
	}
	private static boolean Valid_text_lnke(Xop_ctx ctx, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		if (bgn_pos == Xop_parser_.Doc_bgn_char_0) return true;	// lnke starts at 0; always true
		int prv_pos = bgn_pos - 1; 
		byte prv_byte = src[prv_pos];
		switch (prv_byte) {
			case Byte_ascii.Num_0: case Byte_ascii.Num_1: case Byte_ascii.Num_2: case Byte_ascii.Num_3: case Byte_ascii.Num_4:
			case Byte_ascii.Num_5: case Byte_ascii.Num_6: case Byte_ascii.Num_7: case Byte_ascii.Num_8: case Byte_ascii.Num_9:
			case Byte_ascii.Ltr_A: case Byte_ascii.Ltr_B: case Byte_ascii.Ltr_C: case Byte_ascii.Ltr_D: case Byte_ascii.Ltr_E:
			case Byte_ascii.Ltr_F: case Byte_ascii.Ltr_G: case Byte_ascii.Ltr_H: case Byte_ascii.Ltr_I: case Byte_ascii.Ltr_J:
			case Byte_ascii.Ltr_K: case Byte_ascii.Ltr_L: case Byte_ascii.Ltr_M: case Byte_ascii.Ltr_N: case Byte_ascii.Ltr_O:
			case Byte_ascii.Ltr_P: case Byte_ascii.Ltr_Q: case Byte_ascii.Ltr_R: case Byte_ascii.Ltr_S: case Byte_ascii.Ltr_T:
			case Byte_ascii.Ltr_U: case Byte_ascii.Ltr_V: case Byte_ascii.Ltr_W: case Byte_ascii.Ltr_X: case Byte_ascii.Ltr_Y: case Byte_ascii.Ltr_Z:
			case Byte_ascii.Ltr_a: case Byte_ascii.Ltr_b: case Byte_ascii.Ltr_c: case Byte_ascii.Ltr_d: case Byte_ascii.Ltr_e:
			case Byte_ascii.Ltr_f: case Byte_ascii.Ltr_g: case Byte_ascii.Ltr_h: case Byte_ascii.Ltr_i: case Byte_ascii.Ltr_j:
			case Byte_ascii.Ltr_k: case Byte_ascii.Ltr_l: case Byte_ascii.Ltr_m: case Byte_ascii.Ltr_n: case Byte_ascii.Ltr_o:
			case Byte_ascii.Ltr_p: case Byte_ascii.Ltr_q: case Byte_ascii.Ltr_r: case Byte_ascii.Ltr_s: case Byte_ascii.Ltr_t:
			case Byte_ascii.Ltr_u: case Byte_ascii.Ltr_v: case Byte_ascii.Ltr_w: case Byte_ascii.Ltr_x: case Byte_ascii.Ltr_y: case Byte_ascii.Ltr_z:
				return false;	// alpha-numerical is invalid; EX: "titel:" should not generate a lnke for "tel:"
		}
		if (prv_byte >= Byte_ascii.Ascii_min && prv_byte <= Byte_ascii.Ascii_max) return true;	// consider all other ASCII chars as true; EX: \t\n !, etc; 
		prv_pos = gplx.intl.Utf8_.Get_pos0_of_char_bwd(src, prv_pos);
		prv_byte = src[prv_pos];
		boolean prv_char_is_letter = ctx.Lang().Case_mgr().Match(prv_byte, src, prv_pos, bgn_pos);
		return !prv_char_is_letter;
	}
	private int Make_tkn_xowa(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos, byte[] protocol, byte proto_tid, byte lnke_type) {
		// NOTE: fmt is [xowa-cmd:^"app.setup_mgr.import_wiki('');"^ ]
		if (lnke_type != Xop_lnke_tkn.Lnke_typ_brack) return ctx.Lxr_make_txt_(cur_pos); // NOTE: must check for [ or else C:\xowa\ will cause it to evaluate as lnke
		int proto_end_pos = cur_pos + 1;	// +1 to skip past :
		int lhs_dlm_pos = Bry_finder.Find_fwd(src, Byte_ascii.Quote, proto_end_pos, src_len); if (lhs_dlm_pos == Bry_.NotFound) return ctx.Lxr_make_txt_(cur_pos);
		int lnke_bgn_pos = lhs_dlm_pos + 1;
		byte[] rhs_dlm_bry = Bry_quote;
		if (lhs_dlm_pos - proto_end_pos > 0) {
			Bry_bfr bfr = ctx.App().Utl_bry_bfr_mkr().Get_k004();
			rhs_dlm_bry = bfr.Add(Bry_quote).Add_mid(src, proto_end_pos, lhs_dlm_pos).XtoAryAndClear();
			bfr.Mkr_rls();
		}
		int rhs_dlm_pos = Bry_finder.Find_fwd(src, rhs_dlm_bry, lnke_bgn_pos, src_len); if (rhs_dlm_pos == Bry_.NotFound) return ctx.Lxr_make_txt_(cur_pos);
		int txt_bgn = Bry_finder.Find_fwd_while_space_or_tab(src, rhs_dlm_pos + rhs_dlm_bry.length, src_len); if (txt_bgn == Bry_.NotFound) return ctx.Lxr_make_txt_(cur_pos);
		int txt_end = Bry_finder.Find_fwd(src, Byte_ascii.Brack_end, txt_bgn, src_len); if (txt_end == Bry_.NotFound) return ctx.Lxr_make_txt_(cur_pos);

		int end_pos = txt_end + 1;	// +1 to place after ]
		Xop_lnke_tkn tkn = tkn_mkr.Lnke(bgn_pos, end_pos, protocol, proto_tid, lnke_type, lnke_bgn_pos, rhs_dlm_pos);	// +1 to ignore [
		ctx.Subs_add(root, tkn);
		tkn.Subs_add(tkn_mkr.Txt(txt_bgn, txt_end));
		return end_pos;
	}	private static final byte[] Bry_quote = new byte[] {Byte_ascii.Quote};
}
/*
NOTE_1
lnke takes precedence over lnki.
EX:   [[irc://a b]]
pass: [<a href="irc://a">b</a>]		i.e. [b]    where b is a lnke with caption b and trg of irc://a
fail: <a href="irc://a">b</a>		i.e. b      where b is a lnki with caption b and trg of irc://a
*/
