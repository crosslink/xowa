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
package gplx.xowa.langs.cnvs; import gplx.*; import gplx.xowa.*; import gplx.xowa.langs.*;
import org.junit.*;
import gplx.php.*;
public class Xol_mw_parse_tst {
	private Xol_mw_parse_fxt fxt = new Xol_mw_parse_fxt();
	@Test   public void Basic() {
		fxt.Test_convert("$zh2Hant = array('a' => 'A', 'b' => 'B',);", String_.Concat_lines_nl
		( "// zh_zh-hant"
		, "app.langs.get('zh').converts.get('zh-hant').add_bulk("
		, "<:['"
		, "a|A"
		, "b|B"
		, "']:>"
		, ");"
		));
	}
//		@Test   public void Run() {
//			Io_url src_dir = Io_url_.new_dir_("C:\\xowa\\user\\anonymous\\lang\\mediawiki\\converts\\");
//			Io_url trg_dir = Io_url_.new_dir_("C:\\xowa\\user\\anonymous\\");
//			fxt.Test_run(src_dir, trg_dir);
//		}
}
class Xol_mw_parse_grp {
	public byte[] Lng() {return lng;} public Xol_mw_parse_grp Lng_(byte[] v) {lng = v; return this;} private byte[] lng;
	public byte[] Vnt() {return vnt;} public Xol_mw_parse_grp Vnt_(byte[] v) {vnt = v; return this;} private byte[] vnt;
	public Xol_mw_parse_itm[] Itms() {return itms;} public Xol_mw_parse_grp Itms_(Xol_mw_parse_itm[] v) {itms = v; return this;} private Xol_mw_parse_itm[] itms;
	public void Write_as_gfs(Bry_bfr bfr) {
		int itms_len = itms.length;
		Write_bgn(bfr);
		for (int i = 0; i < itms_len; i++) {
			Xol_mw_parse_itm itm = (Xol_mw_parse_itm)itms[i];
			Write_itm(bfr, itm);
		}
		Write_end(bfr);
	}
	private void Write_bgn(Bry_bfr bfr) {
		bfr.Add_str("// ").Add(lng).Add_str("_").Add(vnt).Add_byte_nl();
		bfr.Add_str("app.langs.get('");
		bfr.Add(lng);
		bfr.Add_str("').converts.get('");
		bfr.Add(vnt);
		bfr.Add_str("').add_bulk(");
		bfr.Add_byte_nl().Add_str("<:['").Add_byte_nl();
	}
	private void Write_itm(Bry_bfr bfr, Xol_mw_parse_itm itm) {
		bfr.Add(itm.Src());
		bfr.Add_byte_pipe();
		bfr.Add(itm.Trg());
		bfr.Add_byte_nl();
	}
	private void Write_end(Bry_bfr bfr) {
		bfr.Add_str("']:>").Add_byte_nl();
		bfr.Add_str(");").Add_byte_nl();
	}
}
class Xol_mw_parse_itm {
	public Xol_mw_parse_itm(byte[] src, byte[] trg) {this.src = src; this.trg = trg;}
	public byte[] Src() {return src;} private byte[] src;
	public byte[] Trg() {return trg;} private byte[] trg;
}
class Xol_mw_parse_fxt {
	public void Test_convert(String mw, String expd) {
		Xol_mw_parse_grp[] actl_ary = Parse(Bry_.new_utf8_(mw));
		Bry_bfr bfr = Bry_bfr.new_();
		actl_ary[0].Write_as_gfs(bfr);
		Tfds.Eq_str_lines(expd, bfr.XtoStr());
	}
	public void Test_run(Io_url src_dir, Io_url trg_dir) {
		Bry_bfr bfr = Bry_bfr.new_();
		Io_url[] fils = Io_mgr._.QueryDir_fils(src_dir);
		int fils_len = fils.length;
		for (int i = 0; i < fils_len; i++) {
			Io_url fil = fils[i];
			byte[] src = Io_mgr._.LoadFilBry(fil);
			Xol_mw_parse_grp[] itms = Parse(src);
			int itms_len = itms.length;
			String lang_name = String_.Lower(String_.Mid(fil.NameOnly(), 0, 2));	// ZhConversion.php -> Zh
			for (int j = 0; j < itms_len; j++) {
				Xol_mw_parse_grp itm = itms[j];
				itm.Write_as_gfs(bfr);
			}
			Io_url trg_fil = Xol_cnv_mgr.Bld_url(trg_dir, lang_name);
			Io_mgr._.SaveFilBry(trg_fil, bfr.XtoAryAndClear());
		}
	}
	public Xol_mw_parse_grp[] Parse(byte[] src) {
		ListAdp list = ListAdp_.new_();
		Php_parser parser = new Php_parser();
		Gfo_msg_log msg_log = new Gfo_msg_log("xowa");
		Php_evaluator evaluator = new Php_evaluator(msg_log);
		parser.Parse_tkns(src, evaluator);
		Php_line[] lines = (Php_line[])evaluator.List().XtoAry(Php_line.class);
		int lines_len = lines.length;
		for (int i = 0; i < lines_len; i++) {
			Php_line_assign line = (Php_line_assign)lines[i];
			Xol_mw_parse_grp grp = Parse_grp(line);
			list.Add(grp);
		}
		return (Xol_mw_parse_grp[])list.XtoAry(Xol_mw_parse_grp.class);
	}
	private ListAdp tmp_itm_list = ListAdp_.new_();
	private Xol_mw_parse_grp Parse_grp(Php_line_assign line) {
		Xol_mw_parse_grp grp = new Xol_mw_parse_grp();
		byte[] key =  line.Key().Val_obj_bry();					// EX: "zh2Hant"
		key = Bry_.Lower_ascii(key);						// EX: "zh2hant"
		byte[][] parts = Bry_.Split(key, Byte_ascii.Num_2);	// EX: "zh", "hant"
		byte[] src = parts[0];
		byte[] trg = Bry_.Add(parts[0], new byte[] {Byte_ascii.Dash}, parts[1]);
		grp.Lng_(src).Vnt_(trg);
		Parse_itms(line, grp);
		return grp;
	}
	private void Parse_itms(Php_line_assign line, Xol_mw_parse_grp grp) {
		Php_itm_ary ary = (Php_itm_ary)line.Val();
		tmp_itm_list.Clear();
		int subs_len = ary.Subs_len();
		for (int i = 0; i < subs_len; i++) {
			Php_itm_kv kv = (Php_itm_kv)ary.Subs_get(i);
			Xol_mw_parse_itm itm = new Xol_mw_parse_itm(kv.Key().Val_obj_bry(), kv.Val().Val_obj_bry());
			tmp_itm_list.Add(itm);
		}
		grp.Itms_((Xol_mw_parse_itm[])tmp_itm_list.XtoAry(Xol_mw_parse_itm.class));
	}
}
