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
class GfmlVarTkn implements GfmlTkn {
	public int ObjType() {return GfmlObj_.Type_tkn;}
	public String Key() {return key;} private String key;
	public String Raw() {return GfmlTknAry_.XtoRaw(ary);}
	public String Val() {
		String s = ctx.Fetch_Val(varKey); if (s == null) return val;
		val = s;
		return val;
	}	String val;
	public GfmlBldrCmd Cmd_of_Tkn() {return GfmlBldrCmd_.Null;}
	public GfmlTkn[] SubTkns() {return ary;} GfmlTkn[] ary;
	public GfmlTkn MakeNew(String rawNew, String valNew) {throw Err_.invalid_op_("makeNew cannot make copy of token with only raw").Add("key", key).Add("rawNew", rawNew).Add("valNew", valNew);}
	public String TknType() {return "evalTkn";}

	GfmlVarCtx ctx; String varKey;
	@gplx.Internal protected GfmlVarTkn(String key, GfmlTkn[] ary, GfmlVarCtx ctx, String varKey) {
		this.key = key; this.ary = ary;
		this.ctx = ctx; this.varKey = varKey;
		this.val = ctx.Fetch_Val(varKey);
	}
}
