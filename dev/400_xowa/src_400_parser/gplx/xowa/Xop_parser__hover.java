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
public class Xop_parser__hover {
	public int Find_min() {return find_min;} public void Find_min_(int v) {find_min = v;} private int find_min = 32;
	public int Block_len() {return block_len;} public void Block_len_(int v) {block_len = v;} private int block_len = Io_mgr.Len_kb;
//		private byte find_tid = Find_tid_words;
	public byte[] Parse(Xow_wiki wiki, Xop_ctx ctx, byte[] src) {
		Xop_parser parser = wiki.Parser();
		Xop_tkn_mkr tkn_mkr = ctx.Tkn_mkr();
		Xop_root_tkn root = tkn_mkr.Root(src);
		ByteTrieMgr_fast trie = parser.Tmpl_lxr_mgr().Trie();
		int len = src.length;
		int pos = Xop_parser_.Doc_bgn_bos;
		int find_cur = 0;
		ctx.Sys_load_tmpls_(false);
		int tkn_idx = 0;
		boolean find_needed = true;
		boolean word_bgn = false;
		int tkn_end = 0;
		while (find_needed) {
			int block_end = pos + block_len;
			if (block_end > len) block_len = len;
			parser.Parse_loop(root, ctx, tkn_mkr, src, trie, pos, block_end);
			pos = block_end;
			// if stack_len > 0, parse till stack_len == 0 || eos
			int root_len = root.Subs_len();
			for (int i = tkn_idx; i < root_len; i++) {
				Xop_tkn_itm tkn = root.Subs_get(i);
				switch (tkn.Tkn_tid()) {
					case Xop_tkn_itm_.Tid_txt:
						word_bgn = true;
						break;
					case Xop_tkn_itm_.Tid_space: case Xop_tkn_itm_.Tid_tab: case Xop_tkn_itm_.Tid_newLine:
						if (word_bgn) {
							word_bgn = false;
							++find_cur;
							find_needed = find_cur < find_min;
							if (!find_needed)
								tkn_end = i;
						}
						break;
				}
				if (!find_needed)
					break;
			}
			tkn_idx = root_len;
		}
		Xop_root_tkn root2 = tkn_mkr.Root(src);
		for (int i = 0; i < tkn_end; i++) {
			root2.Subs_add(root.Subs_get(i));
		}
		byte[] wiki_text = Xot_tmpl_wtr._.Write_all(ctx, root2, src);
		ctx.Para().Enabled_n_();
		root.Clear();
		parser.Parse_wtxt_to_wdom(root, ctx, tkn_mkr, wiki_text, Xop_parser_.Doc_bgn_bos);
		Bry_bfr bfr = wiki.Utl_bry_bfr_mkr().Get_k004();
		wiki.Html_mgr().Hdom_wtr().Write_all(bfr, ctx, src, root);
		return bfr.Mkr_rls().XtoAryAndClear();
	}
//		private static final byte Find_tid_words = 1, Find_tid_sentences = 1;
}
