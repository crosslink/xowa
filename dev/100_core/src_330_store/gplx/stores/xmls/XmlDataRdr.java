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
package gplx.stores.xmls; import gplx.*; import gplx.stores.*;
import gplx.xmls.*; /*Xpath_*/
public class XmlDataRdr extends DataRdr_base implements DataRdr {
	@Override public String NameOfNode() {return nde.Name();} public String XtoStr() {return nde.Xml_outer();}
	@Override public int FieldCount() {return nde.Atrs() == null ? 0 : nde.Atrs().Count();} // nde.Attributes == null when nde is XmlText; ex: <node>val</node>
	@Override public String KeyAt(int i) {return nde.Atrs().FetchAt(i).Name();}
	@Override public Object ReadAt(int i) {
		XmlAtr attrib = nde.Atrs().FetchAt(i);
		return (attrib == null) ? null : attrib.Value();
	}
	@Override public Object Read(String key) {
		return nde.Atrs().FetchValOr(key, null);
	}
	public boolean MoveNextPeer() {
		if (++pos >= peerList.Count()){	// moved out Of range
			nde = null;
			return false; 
		}
		nde = peerList.FetchAt(pos);
		return true;
	}
	@Override public DataRdr Subs() {
		XmlNdeList list = Xpath_.SelectElements(nde);
		XmlDataRdr rv = new XmlDataRdr();
		rv.ctor_(list, null);
		return rv;
	}
	@Override public DataRdr Subs_byName_moveFirst(String name) {
		DataRdr subRdr = Subs_byName(name);
		boolean hasFirst = subRdr.MoveNextPeer();
		return (hasFirst) ? subRdr : DataRdr_.Null;
	}
	public DataRdr Subs_byName(String name) {
		XmlNdeList list = Xpath_.SelectAll(nde, name);
		XmlDataRdr rv = new XmlDataRdr();
		rv.ctor_(list, null);
		return rv;
	}		
	public void Rls() {nde = null; peerList = null;}
	public String NodeValue_get() {
		if (nde.SubNdes().Count() != 1) return "";
		XmlNde sub = nde.SubNdes().FetchAt(0);
		return (sub.NdeType_textOrEntityReference()) ? sub.Text_inner() : "";
	}
	public String Node_OuterXml() {return nde.Xml_outer();}
	@Override public SrlMgr SrlMgr_new(Object o) {return new XmlDataRdr();}
	void LoadString(String raw) {
		XmlDoc xdoc = XmlDoc_.parse_(raw);
		XmlNdeList list = Xpath_.SelectElements(xdoc.Root());
		ctor_(list, xdoc.Root());
	}
	void ctor_(XmlNdeList peerList, XmlNde nde) {
		this.peerList = peerList; this.nde = nde; pos = -1;
	}
	
	XmlNde nde = null;
	XmlNdeList peerList = null; int pos = -1;
	@gplx.Internal protected XmlDataRdr(String raw) {this.LoadString(raw); this.Parse_set(true);}
	XmlDataRdr() {this.Parse_set(true);}
}
