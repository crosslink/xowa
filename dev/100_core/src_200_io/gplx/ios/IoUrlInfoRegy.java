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
package gplx.ios; import gplx.*;
public class IoUrlInfoRegy implements GfoInvkAble {
	public void Reg(IoUrlInfo info) {hash.AddReplace(info.Key(), info);}
	public IoUrlInfo Match(String raw) {
		if (String_.Len(raw) == 0) return IoUrlInfo_.Nil;
		for (int i = hash.Count(); i > 0; i--) {
			IoUrlInfo info = (IoUrlInfo)hash.FetchAt(i - 1);
			if (info.Match(raw)) return info;
		}
		throw Err_.new_("could not match ioPathInfo").Add("raw", raw).Add("count", hash.Count());
	}
	public void Reset() {
		hash.Clear();
		Reg(IoUrlInfo_rel.new_(Op_sys.Cur().Tid_is_wnt() ? (IoUrlInfo)IoUrlInfo_wnt._ : (IoUrlInfo)IoUrlInfo_lnx._));
		Reg(IoUrlInfo_.Mem);
		Reg(IoUrlInfo_lnx._);
		Reg(IoUrlInfo_wnt._);
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_Add)) {
			String srcDirStr = m.ReadStr("srcDir");
			String trgDirStr = m.ReadStr("trgDir");
			String engineKey = m.ReadStrOr("engineKey", IoEngine_.SysKey);
			if (ctx.Deny()) return this;
			IoUrlInfo_alias alias =  IoUrlInfo_alias.new_(srcDirStr, trgDirStr, engineKey);
			IoUrlInfoRegy._.Reg(alias);
		}
		return this;
	}	public static final String Invk_Add = "Add";
	OrderedHash hash = OrderedHash_.new_();
        public static final IoUrlInfoRegy _ = new IoUrlInfoRegy();
	IoUrlInfoRegy() {
		this.Reset();
	}
}