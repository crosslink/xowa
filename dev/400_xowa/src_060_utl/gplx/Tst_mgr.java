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
package gplx;
public class Tst_mgr {
	public Tst_mgr ThrowError_n_() {throwError = false; return this;} private boolean throwError = true;
	public ListAdp Results() {return results;} ListAdp results = ListAdp_.new_();
	public KeyValHash Vars() {return vars;} KeyValHash vars = KeyValHash.new_();
	public Object Vars_get_by_key(String key) {return vars.FetchValOr(key, null);}
	public String Vars_get_bry_as_str(String key, int bgn, int end) {
		byte[] bry = (byte[])vars.FetchValOr(key, null); if (bry == null) return String_.Empty;
		if (bgn < 0 || end > bry.length || end < bgn || end < 0) return "<<OUT OF BOUNDS>>";
		return String_.new_utf8_(Bry_.Mid(bry, bgn, end));
	}
	public int Tst_val(boolean skip, String path, String name, Object expd, Object actl) {
		Tst_itm itm = Tst_itm.eq_(skip, path, name, expd, actl);
		results.Add(itm);
		return itm.Pass() ? 0 : 1;
	}
	public int Tst_val_ary(boolean skip, String path, String name, Object expd, Object actl) {	
		Tst_itm itm = Tst_itm.eq_(skip, path, name, XtoStr(expd), XtoStr(actl));
		results.Add(itm);
		return itm.Pass() ? 0 : 1;
	}
	public void Tst_obj(Tst_chkr expd, Object actl) {
		results.Clear();
		int err = Tst_sub_obj(expd, actl, "", 0);
		if (throwError && err > 0) throw Err_.new_(Build());
	}
	public void Tst_ary(String ownerPath, Tst_chkr[] expd_ary, Object[] actl_ary) {
		results.Clear();
		Tst_ary_inner(ownerPath, expd_ary, actl_ary);
	}
	private void Tst_ary_inner(String ownerPath, Tst_chkr[] expd_ary, Object[] actl_ary) {
		int expd_ary_len = expd_ary.length, actl_ary_len = actl_ary.length;
		int max_len = expd_ary_len > actl_ary_len ? expd_ary_len : actl_ary_len;
		int err = 0;
		for (int i = 0; i < max_len; i++) {
			String path = ownerPath + Int_.XtoStr(i);
			Tst_chkr expd_obj = i < expd_ary_len ? expd_ary[i] : Tst_mgr.Null_chkr;
			Object actl_obj = i < actl_ary_len ? actl_ary[i] : "<NULL OBJ>";
			String actl_type = i < actl_ary_len ? ClassAdp_.NameOf_obj(actl_obj) : "<NULL TYPE>";
			err += Tst_inner(expd_obj, actl_obj, actl_type, path, err);
		}
		if (throwError && err > 0) {
			String s = Build();
			throw Err_.new_(s);
		}
	}
	public int Tst_sub_obj(Tst_chkr expd, Object actl, String path, int err) {
		return Tst_inner(expd, actl, actl == null ? "<NULL>" : ClassAdp_.NameOf_obj(actl), path, err);
	}
	public int Tst_sub_ary(Tst_chkr[] expd_subs, Object[] actl_subs, String path, int err) {
		Tst_ary_inner(path + ".", expd_subs, actl_subs);
		return err;
	}
	int Tst_inner(Tst_chkr expd_obj, Object actl_obj, String actl_type, String path, int err) {
		if (actl_obj == null || !ClassAdp_.IsAssignableFrom(expd_obj.TypeOf(), actl_obj.getClass())) {
			results.Add(Tst_itm.fail_("!=", path, "<cast type>", ClassAdp_.NameOf_type(expd_obj.TypeOf()), actl_type));
			return 1;
//				results.Add(Tst_itm.fail_("!=", path, "<cast value>", Object_.XtoStr_OrNull(expd_obj.ValueOf()), Object_.XtoStr_OrNull(actl_obj)));
		}
		else {
			return expd_obj.Chk(this, path, actl_obj);
		}
	}
	String XtoStr(Object ary) {	
		if (ary == null) return "<NULL>";
		int len = Array_.Len(ary);
		for (int i = 0; i < len; i++) {
			Object itm = Array_.FetchAt(ary, i);
			ary_sb.Add(Object_.XtoStr_OrNullStr(itm)).Add(",");
		}
		return ary_sb.XtoStrAndClear();
	}	String_bldr ary_sb = String_bldr_.new_();
	String Build() {
		String_bldr sb = String_bldr_.new_();
		int comp_max = 0, path_max =0, name_max = 0;
		int len = results.Count();
		for (int i = 0; i < len; i++) {
			Tst_itm itm = (Tst_itm)results.FetchAt(i);
			comp_max = Max(comp_max, itm.Comp());
			path_max = Max(path_max, itm.Path());
			name_max = Max(name_max, itm.Name());
		}
		for (int i = 0; i < len; i++) {
			Tst_itm itm = (Tst_itm)results.FetchAt(i);
			sb.Add_fmt("\n{0}  {1}  {2}  '{3}'", String_.PadEnd(itm.Comp(), comp_max, " "), "#" + String_.PadEnd(itm.Path(), path_max, " "), "@" + String_.PadEnd(itm.Name(), name_max, " ") + ":", itm.Expd());
			if (!itm.Pass())
				sb.Add_fmt("\n{0}  {1}  {2}  '{3}'", String_.PadEnd("", comp_max, " "), " " + String_.PadEnd("", path_max, " "), " " + String_.PadEnd("", name_max, " ") + " ", itm.Actl());
		}
		return sb.XtoStrAndClear();
	}
	int Max(int max, String s) {int len = String_.Len(s); return len > max ? len : max;}
	public static final Tst_chkr Null_chkr = new Tst_chkr_null();
}
class Tst_itm {
	public boolean Pass() {return pass;} private boolean pass;
	public boolean Skip() {return skip;} private boolean skip;
	public String Comp() {return comp;} public Tst_itm Comp_(String v) {comp = v; return this;} private String comp = "";
	public String Path() {return path;} public Tst_itm Path_(String v) {path = v; return this;} private String path = "";
	public String Name() {return name;} public Tst_itm Name_(String v) {name = v; return this;} private String name = "";
	public String Expd() {return expd;} public Tst_itm Expd_(String v) {expd = v; return this;} private String expd = "";
	public String Actl() {return actl;} public Tst_itm Actl_(String v) {actl = v; return this;} private String actl = "";
	public static Tst_itm eq_(boolean skip, String path, String name, Object expd, Object actl) {
		boolean pass = skip ? true : Object_.Eq(expd, actl);
		String comp = pass ? "==" : "!=";
		String expd_str = Object_.XtoStr_OrNullStr(expd);
		String actl_str = Object_.XtoStr_OrNullStr(actl);
		if (skip) expd_str = actl_str;
		return new_(skip, pass, comp, path, name, expd_str, actl_str);
	}
	public static Tst_itm fail_(String comp, String path, String name, String expd, String actl) {return new_(false, false, comp, path, name, expd, actl);}
	public static Tst_itm new_(boolean skip, boolean pass, String comp, String path, String name, String expd, String actl) {
		Tst_itm rv = new Tst_itm();
		rv.skip = skip; rv.pass = pass; rv.comp = comp; rv.path = path; rv.name = name;; rv.expd = expd; rv.actl = actl;
		return rv;
	}
}
