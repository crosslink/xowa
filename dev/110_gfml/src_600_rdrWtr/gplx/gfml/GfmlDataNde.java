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
package gplx.gfml; import gplx.*;
import gplx.stores.*;
public class GfmlDataNde {
	public GfmlDoc Doc() {return gdoc;} GfmlDoc gdoc;
	public DataRdr XtoRdr() {
		GfmlDataRdr rv = new GfmlDataRdr();
		rv.SetNode(gdoc.RootNde());
		return rv;
	}
	public DataWtr XtoWtr() {
		GfmlDataWtr2 rv = new GfmlDataWtr2();
		rv.Doc_set(gdoc);
		return rv;
	}
	public static GfmlDataNde new_any_eol_(String raw) {return new_(String_.Replace(raw, String_.CrLf, String_.Lf));}
	public static GfmlDataNde new_(String raw) {
		GfmlDataNde rv = new GfmlDataNde();
		GfmlBldr bldr = GfmlBldr_.default_();
		bldr.Doc().RootLxr().SubLxr_Add
			( GfmlDocLxrs.AtrSpr_lxr()
			, GfmlDocLxrs.NdeDot_lxr()
			, GfmlDocLxrs.NdeHdrBgn_lxr()
			, GfmlDocLxrs.NdeHdrEnd_lxr()
			);
		rv.gdoc = bldr.XtoGfmlDoc(raw);
		return rv;
	}
	public static GfoMsg XtoMsg(String raw) {
		GfmlDoc gdoc = GfmlDataNde.new_any_eol_(raw).Doc();
		return XtoMsg(gdoc.RootNde());
	}
	public static GfoMsg XtoMsgNoRoot(String raw) {
		GfmlDoc gdoc = GfmlDataNde.new_any_eol_(raw).Doc();
		GfoMsg msg = XtoMsg(gdoc.RootNde());
		return (GfoMsg)msg.Subs_getAt(0);
	}
	static GfoMsg XtoMsg(GfmlNde gnde) {
		String msgKey = String_.Coalesce(gnde.Key(), gnde.Hnd());				
		GfoMsg msg = GfoMsg_.new_parse_(msgKey);
		for (int i = 0; i < gnde.SubKeys().Count(); i++) {
			GfmlItm subItm = (GfmlItm)gnde.SubKeys().FetchAt(i);
			if (subItm.ObjType() == GfmlObj_.Type_atr) {
				GfmlAtr subAtr = (GfmlAtr)subItm;
				String subAtrKey = String_.Len_eq_0(subAtr.Key()) ? "" : subAtr.Key(); // NOTE: needs to be "" or else will fail in GfoConsole; key will be evaluated against NullKey in GfsCtx
				msg.Add(subAtrKey, subAtr.DatTkn().Val());
			}
			else {
				GfmlNde subNde = (GfmlNde)subItm;
				GfoMsg subMsg = XtoMsg(subNde);
				msg.Subs_add(subMsg);
			}
		}
		for (int i = 0; i < gnde.SubHnds().Count(); i++) {
			GfmlItm subItm = (GfmlItm)gnde.SubHnds().FetchAt(i);
			GfmlNde subNde = (GfmlNde)subItm;
			GfoMsg subMsg = XtoMsg(subNde);
			msg.Subs_add(subMsg);
		}
		return msg;
	}
}
class GfmlDataWtr2 extends DataWtr_base implements DataWtr {
	@Override public void WriteData(String name, Object val) {
		GfmlTkn nameTkn = GfmlTkn_.raw_(name);
		GfmlTkn valTkn = GfmlTkn_.raw_(XtoStr(val));
		GfmlAtr atr = GfmlAtr.new_(nameTkn, valTkn, GfmlType_.String);
		GfmlNde nde = gdoc.RootNde().SubHnds().FetchAt(0);
		nde.SubKeys().Add(atr);
	}
	public void InitWtr(String key, Object val) {}
	public void WriteTableBgn(String name, GfoFldList fields) {}
	@Override public void WriteNodeBgn(String nodeName) {}
	public void WriteLeafBgn(String leafName) {}
	@Override public void WriteNodeEnd() {}
	public void WriteLeafEnd() {}
	public void Clear() {}
	public String XtoStr() {return "";}
	String XtoStr(Object obj) {
		if (obj == null) return "''";
		String s = obj.toString();
		return String_.Concat("'", String_.Replace(s, "'", "''"), "'");
	}
	@Override public SrlMgr SrlMgr_new(Object o) {return new GfmlDataWtr2();}
	public void Doc_set(GfmlDoc v) {gdoc = v;} GfmlDoc gdoc;
}
