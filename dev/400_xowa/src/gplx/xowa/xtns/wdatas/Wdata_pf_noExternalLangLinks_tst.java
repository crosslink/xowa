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
package gplx.xowa.xtns.wdatas; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Wdata_pf_noExternalLangLinks_tst {
	@Before public void init() {fxt.Clear();} Wdata_pf_noExternalLangLinks_fxt fxt = new Wdata_pf_noExternalLangLinks_fxt();
	@Test   public void Basic() {
		fxt.Clear().Expd_enabled_(true).Test_parse("{{noexternallanglinks}}");
		fxt.Clear().Expd_enabled_(false).Expd_sort_(false).Expd_langs_().Test_parse("");
	}
	@Test   public void Selected() {
		fxt.Clear().Expd_enabled_(true).Expd_langs_("en", "fr").Test_parse("{{noexternallanglinks:en|fr}}");
	}
	@Test   public void Sort() {
		fxt.Clear().Expd_enabled_(true).Expd_sort_(true).Expd_langs_().Test_parse("{{noexternallanglinks:*}}");
	}
}
class Wdata_pf_noExternalLangLinks_fxt {
	public Wdata_pf_noExternalLangLinks_fxt Clear() {
		if (parser_fxt == null) {
			parser_fxt = new Xop_fxt();
			app = parser_fxt.App();
			wiki = parser_fxt.Wiki();
			data = wiki.Ctx().Cur_page().Wdata_external_lang_links();
		}
		expd_sort = expd_enabled = Bool_.__byte;
		expd_langs = null;
		data.Reset();
		return this;
	}	private Xop_fxt parser_fxt; Xoa_app app; Xow_wiki wiki; Wdata_external_lang_links_data data;
	public Wdata_pf_noExternalLangLinks_fxt Expd_enabled_(boolean v) {expd_enabled = v ? Bool_.Y_byte : Bool_.N_byte; return this;} private byte expd_enabled;
	public Wdata_pf_noExternalLangLinks_fxt Expd_sort_(boolean v) {expd_sort = v ? Bool_.Y_byte : Bool_.N_byte; return this;} private byte expd_sort;
	public Wdata_pf_noExternalLangLinks_fxt Expd_langs_(String... v) {expd_langs = v; return this;} private String[] expd_langs;
	public void Test_parse(String raw) {
		byte[] expd = parser_fxt.Test_parse_tmpl_str_rv(raw);
		Tfds.Eq(Bry_.Empty, expd);
		if (expd_enabled != Bool_.__byte) Tfds.Eq(expd_enabled == Bool_.Y_byte, data.Enabled());
		if (expd_sort != Bool_.__byte) Tfds.Eq(expd_sort == Bool_.Y_byte, data.Sort());
		if (expd_langs != null) Tfds.Eq_ary_str(expd_langs, Data_langs_xto_str_ary());
	}
	String[] Data_langs_xto_str_ary() {
		int len = data.Langs_len();
		String[] rv = new String[len];
		for (int i = 0; i < len; i++) {
			rv[i] = String_.new_utf8_((byte[])data.Langs_get_at(i));
		}
		return rv;
	}
}
