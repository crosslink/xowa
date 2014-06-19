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
import gplx.xowa.langs.numbers.*;
public class Pf_site_pagesincategory extends Pf_func_base {
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, Bry_bfr bb) {
		byte[] val_dat_ary = Eval_argx(ctx, src, caller, self); if (Bry_.Len_eq_0(val_dat_ary)) {bb.Add_int_fixed(0, 1); return;}
		val_dat_ary = Xoa_ttl.Replace_spaces(val_dat_ary);
		Xow_wiki wiki = ctx.Wiki();
		int ctg_len = wiki.Db_mgr().Load_mgr().Load_ctg_count(val_dat_ary);
		if (ctg_len == 0) {bb.Add_int_fixed(0, 1); return;}

		Xol_lang lang = wiki.Lang();
		if (trie == null) trie = Xol_kwd_mgr.trie_(lang.Kwd_mgr(), Xol_kwd_grp_.Id_str_rawsuffix);
		int self_args_len = self.Args_len();
		boolean fmt_num = true;
		if (self_args_len == 1) {
			byte[] arg1 = Pf_func_.Eval_arg_or_empty(ctx, src, caller, self, self_args_len, 0);
			if (arg1 != Bry_.Empty && trie.MatchAtCurExact(arg1, 0, arg1.length) != null)
				fmt_num = false;
		}
		Bry_bfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_b128().Mkr_rls();
		byte[] ctg_len_bry = tmp_bfr.Add_int_variable(ctg_len).XtoAryAndClear();			
		byte[] rslt = fmt_num ? lang.Num_mgr().Format_num(ctg_len_bry) : lang.Num_mgr().Raw(ctg_len_bry);
		bb.Add(rslt);
	}	private ByteTrieMgr_slim trie;
	@Override public int Id() {return Xol_kwd_grp_.Id_site_pagesincategory;}
	@Override public Pf_func New(int id, byte[] name) {return new Pf_site_pagesincategory().Name_(name);}
	public static final Pf_site_pagesincategory _ = new Pf_site_pagesincategory(); Pf_site_pagesincategory() {}
}
