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
package gplx.xowa.users.history; import gplx.*; import gplx.xowa.*; import gplx.xowa.users.*;
public class Xou_history_mgr implements GfoInvkAble {// app.user.history
	public Xou_history_mgr(Xou_user user) {this.app = user.App();} private Xoa_app app; boolean load_chk = false; //boolean enabled = true;
	Xou_history_html html_mgr = new Xou_history_html(); Xou_history_sorter sorter = new Xou_history_sorter().Sort_fld_(Xou_history_itm.Fld_view_end).Ascending_(false);
	public int Count() {return itms.Count();} private OrderedHash itms = OrderedHash_.new_bry_();		
	public void Clear() {itms.Clear();}
	@gplx.Internal protected Xou_history_itm Get_at(int i) {return (Xou_history_itm)itms.FetchAt(i);}		
	public String Get_at_last(Xoa_app app) {
		if (!load_chk) Load(app);
		int len = itms.Count(); if (len == 0) return String_.new_ascii_(Xoa_page_.Main_page_bry);	// if no history, return Main_page (which should go to home/wiki/Main_page)
		Xou_history_itm itm = (Xou_history_itm)itms.FetchAt(0);
		return String_.new_utf8_(Bry_.Add(itm.Wiki(), Xoh_href_parser.Href_wiki_bry, itm.Page()));
	}
	public boolean Has(byte[] wiki, byte[] page) {
		if (!load_chk) Load(app);
		byte[] key = Xou_history_itm.key_(wiki, page);
		return itms.Has(key);
	}
	public void Add(Xoa_page page) {
		Xoa_url url = page.Url();
		Xoa_ttl ttl = page.Ttl();
		byte[] page_ttl = null;
		ListAdp redirect_list = page.Redirect_list();
		if (redirect_list.Count() > 0)		// page was redirected; add src ttl to history, not trg; EX: UK -> United Kingdom; add "UK"; DATE:2014-02-28
			page_ttl = (byte[])page.Redirect_list().FetchAt(0);
		else {
			page_ttl = Bry_.Add(ttl.Ns().Name_db_w_colon(), ttl.Page_txt());  // use ttl.Page_txt() b/c it normalizes space/casing (url.Page_txt does not)
			if (url.Args().length > 0)
				page_ttl = Bry_.Add(page_ttl, url.Args_all_as_bry());
		}
		Add(url, ttl, page_ttl);
	}
	public void Add(Xoa_url url, Xoa_ttl ttl, byte[] page_ttl) {
		if (!load_chk) Load(app);
		if (	ttl.Ns().Id_special()
			&&	(	Bry_.Eq(ttl.Page_db(), Xou_history_mgr.Ttl_name)	// do not add XowaPageHistory to history
				||	Bry_.Eq(ttl.Page_db(), gplx.xowa.specials.xowa.default_tab.Default_tab_page.Ttl_name_bry)
				)
			) return; 
		byte[] key = Xou_history_itm.key_(url.Wiki_bry(), page_ttl);
		Xou_history_itm itm = (Xou_history_itm)itms.Fetch(key);
		if (itm == null) {
			itm = new Xou_history_itm(url.Wiki_bry(), page_ttl);
			itms.Add(key, itm);
		}
		itm.Tally();
	}
	public void Sort() {itms.SortBy(sorter);}
	public void Load(Xoa_app app) {
		if (load_chk) return;
		load_chk = true;
		itms.Clear();
		Xou_history_itm_srl.Load(Io_mgr._.LoadFilBry(app.User().Fsys_mgr().App_data_history_fil()), itms);
		itms.SortBy(sorter);
	}
	public void Save(Xoa_app app) {
		if (!load_chk) return; // nothing loaded; nothing to save
		int itms_len = itms.Count();
		if (itms_len == 0) return;	// no items; occurs when history disable;
		itms.SortBy(sorter);
		if (itms_len > current_itms_max) itms = Archive(app);
		byte[] ary = Xou_history_itm_srl.Save(itms);
		Io_mgr._.SaveFilBry(app.User().Fsys_mgr().App_data_history_fil(), ary);
	}
	public OrderedHash Archive(Xoa_app app) {
		itms.SortBy(sorter);
		int itms_len = itms.Count();
		OrderedHash current_itms = OrderedHash_.new_bry_();
		OrderedHash archive_itms = OrderedHash_.new_bry_();
		for (int i = 0; i < itms_len; i++) {
			Xou_history_itm  itm = (Xou_history_itm)itms.FetchAt(i);
			OrderedHash itms_hash = (i < current_itms_reset) ? current_itms : archive_itms;
			itms_hash.Add(itm.Key(), itm);
		}
		byte[] ary = Xou_history_itm_srl.Save(archive_itms);
		Io_url url = app.User().Fsys_mgr().App_data_history_fil().GenNewNameOnly(DateAdp_.Now().XtoStr_fmt_yyyyMMdd_HHmmss_fff());
		Io_mgr._.SaveFilBry(url, ary);
		return current_itms;
	}	int current_itms_max = 512, current_itms_reset = 256;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
//			if		(ctx.Match(k, Invk_enabled))				return Yn.X_to_str(enabled);
//			else if	(ctx.Match(k, Invk_enabled_))				enabled = m.ReadBool("v");
		if		(ctx.Match(k, Invk_html_grp))				return String_.new_utf8_(html_mgr.Html_grp().Fmt());
		else if	(ctx.Match(k, Invk_html_grp_))				html_mgr.Html_grp().Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_html_itm))				return String_.new_utf8_(html_mgr.Html_itm().Fmt());
		else if	(ctx.Match(k, Invk_html_itm_))				html_mgr.Html_itm().Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_current_itms_max_))		current_itms_max = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_current_itms_reset_))	current_itms_reset = m.ReadInt("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	public static final String Invk_html_grp = "html_grp", Invk_html_grp_ = "html_grp_", Invk_html_itm = "html_itm", Invk_html_itm_ = "html_itm_", Invk_current_itms_max_ = "current_itms_max_", Invk_current_itms_reset_ = "current_itms_reset_";
	//Invk_enabled = "enabled", Invk_enabled_ = "enabled_", 
	public static final byte[] Ttl_name = Bry_.new_ascii_("XowaPageHistory");
	public static final byte[] Ttl_full = Bry_.new_ascii_("Special:XowaPageHistory");
}	
class Xou_history_itm_srl {
	public static void Load(byte[] ary, OrderedHash list) {
	try {
		list.Clear();
		int aryLen = ary.length;
		if (aryLen == 0) return; // no file
		Int_obj_ref pos = Int_obj_ref.zero_();
		while (true) {
			if (pos.Val() == aryLen) break;
			Xou_history_itm itm = Xou_history_itm.csv_(ary, pos);
			byte[] key = itm.Key();
			Xou_history_itm existing = (Xou_history_itm)list.Fetch(key);
			if (existing == null)	// new itm; add
				list.Add(itm.Key(), itm);
			else					// existing itm; update
				existing.Merge(itm);
		}
	} catch (Exception e) {throw Err_.parse_type_exc_(e, Xou_history_itm.class, String_.new_utf8_(ary));}
	}
	public static byte[] Save(OrderedHash list) {
		Bry_bfr bb = Bry_bfr.new_();
		int listLen = list.Count();
		for (int i = 0; i < listLen; i++)
			((Xou_history_itm)list.FetchAt(i)).Save(bb);
		return bb.XtoAry();
	}
}
