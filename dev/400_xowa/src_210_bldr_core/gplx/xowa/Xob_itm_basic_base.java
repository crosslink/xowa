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
public abstract class Xob_itm_basic_base implements GfoInvkAble {
	protected Xoa_app app; protected Xob_bldr bldr; protected Xow_wiki wiki; protected Gfo_usr_dlg usr_dlg;
	public void Cmd_ctor(Xob_bldr bldr, Xow_wiki wiki) {this.bldr = bldr; this.wiki = wiki; this.app = bldr.App(); usr_dlg = bldr.Usr_dlg(); this.Cmd_ctor_end(bldr, wiki);}
	@gplx.Virtual protected void Cmd_ctor_end(Xob_bldr bldr, Xow_wiki wiki) {
	}
	@gplx.Virtual public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_owner))				return bldr.Cmd_mgr();
		else	return GfoInvkAble_.Rv_unhandled;
	}	private static final String Invk_owner = "owner";
}
