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
package gplx.xowa.xtns.relatedArticles; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.html.*;
public class Articles_func extends Pf_func_base {
	@Override public int Id() {return Xol_kwd_grp_.Id_relatedArticles;}
	@Override public Pf_func New(int id, byte[] name) {return new Articles_func().Name_(name);}
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, Bry_bfr bfr) {
		byte[] argx = this.Eval_argx(ctx, src, caller, self);
		Articles_html_xtn_itm xtn_itm = (Articles_html_xtn_itm)ctx.Cur_page().Xtn_mgr().Get_or_null(Articles_html_xtn_itm.Const_key);
		if (xtn_itm == null) {
			xtn_itm = new Articles_html_xtn_itm();
			xtn_itm.Init(ctx.Wiki(), ctx.Cur_page());
			ctx.Cur_page().Xtn_mgr().Add(xtn_itm);
		}
		Parse(xtn_itm, argx);
	}
	private void Parse(Articles_html_xtn_itm xtn_itm, byte[] argx) {
		int pos = Bry_finder.Find_fwd(argx, Const_dlm); if (pos == Bry_finder.Not_found) return;
		byte[] ttl	= Bry_.Trim(Bry_.Mid(argx, 0, pos));
		byte[] text = Bry_.Trim(Bry_.Mid(argx, pos + Const_dlm.length));
		xtn_itm.Add(new Articles_itm(ttl, text));
	}
	public static final Articles_func _ = new Articles_func(); Articles_func() {}
	private static final byte[] Const_dlm = new byte[] {Byte_ascii.Amp, Byte_ascii.Amp};
}
class Articles_itm {
	public Articles_itm(byte[] ttl, byte[] text) {this.ttl = ttl; this.text = text;}
	public byte[] Ttl() {return ttl;} private byte[] ttl;
	public byte[] Text() {return text;} private byte[] text;
}
class Articles_itm_fmtr implements Bry_fmtr_arg {
	private Xow_wiki wiki; private ListAdp itms;
	public void Init(Xow_wiki wiki, ListAdp itms) {this.wiki = wiki; this.itms = itms;}
	public void XferAry(Bry_bfr bfr, int idx) {
		int len = itms.Count();
		for (int i = 0; i < len; i++) {
			Articles_itm itm = (Articles_itm)itms.FetchAt(i);
			Xoa_ttl ttl = Xoa_ttl.parse_(wiki, itm.Ttl()); if (ttl == null) continue;
			fmtr.Bld_bfr(bfr, ttl.Full_db(), itm.Text());
		}
	}
	private static final Bry_fmtr fmtr = Bry_fmtr.new_("\n      <li class=\"interwiki-relart\"><a href=\"/wiki/~{ttl}\">~{text}</a></li>",  "ttl", "text");
	public static final Articles_itm_fmtr _ = new Articles_itm_fmtr(); Articles_itm_fmtr() {}
}
class Articles_html_xtn_itm implements Xoh_xtn_itm {
	private Xow_wiki wiki; private Xoa_page page;
	private ListAdp itms = ListAdp_.new_();
	public byte[] Key() {return Const_key;} public static final byte[] Const_key = Bry_.new_utf8_("related_articles");
	public void Init(Xow_wiki wiki, Xoa_page page) {this.wiki = wiki; this.page = page;}
	public void Add(Articles_itm itm) {itms.Add(itm);}
	public void Exec() {
		itms_fmtr.Init(wiki, itms);
		html_fmtr.Bld_bfr_many(page.Html_data().Portal_div_xtn(), wiki.Msg_mgr().Val_by_key_obj("relatedarticles-title"), itms_fmtr);
	}
	private static final Articles_itm_fmtr itms_fmtr = Articles_itm_fmtr._;
	private static final Bry_fmtr html_fmtr = Bry_fmtr.new_(String_.Concat_lines_nl_skip_last
	(  "<div class=\"portal\" role=\"navigation\" id=\"p-relatedarticles\">"
	, "  <h3>~{h3}</h3>"
	, "  <div class=\"body\">"
	, "    <ul>~{itms}"
	, "    </ul>"
	, "  </div>"
	, "</div>"
	), "h3", "itms");
}
