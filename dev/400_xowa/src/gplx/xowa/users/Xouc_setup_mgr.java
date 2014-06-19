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
package gplx.xowa.users; import gplx.*; import gplx.xowa.*;
public class Xouc_setup_mgr implements GfoInvkAble {
	public Xouc_setup_mgr(Xou_user user) {this.user = user;}
	public Xou_user User() {return user;} private Xou_user user;
	private String setup_completed = "";
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {			
		if		(ctx.Match(k, Invk_setup_completed))			return setup_completed;
		else if	(ctx.Match(k, Invk_setup_completed_))			setup_completed = m.ReadStr("v");
		return this;
	}	private static final String Invk_setup_completed = "setup_completed", Invk_setup_completed_ = "setup_completed_";
	public void Setup_run_check(Xoa_app app) {
		byte op_sys_tid = Op_sys.Cur().Tid();
		switch (op_sys_tid) {
			case Op_sys.Tid_drd: 
			case Op_sys.Tid_wnt: return;
		}
		String op_sys_name = Xoa_app_.Op_sys;
		String[] plats_ary = String_.Split(setup_completed, ";");
		int plats_ary_len = plats_ary.length;
		for (int i = 0; i < plats_ary_len; i++) {
			if (String_.Eq(plats_ary[i], op_sys_name)) return;
		}
//			Io_url lua_url			= app.Fsys_mgr().Bin_plat_dir().GenSubFil_nest("lua", "lua");
//			Io_url lua_install_url	= app.Fsys_mgr().Bin_plat_dir().GenSubFil_nest("lua", "lua_install");
//			Run(String_.Format("mv -f \"{0}\" \"{1}\"", lua_install_url.Raw(), lua_url.Raw()));
//			Run(String_.Format("chmod 774 \"{0}\"", lua_url.Raw()));
		Io_url setup_url = app.Fsys_mgr().Root_dir().GenSubFil_ary("setup_", op_sys_name, ".sh");
		Run("sh", String_.Format("\"{0}\"", setup_url.Raw()));
		setup_completed += op_sys_name + ";";
		app.Cfg_mgr().Set_by_all("app.user.cfg.setup.setup_completed", setup_completed);
		app.Cfg_mgr().Db_save_txt();
	}
	boolean Run(String exe, String arg) {
		boolean pass = false; String fail = "";
		try {pass = new ProcessAdp().Exe_url_(Io_url_.new_fil_(exe)).Args_str_(arg).Run_wait_sync().Exit_code_pass();}
		catch (Exception e) {
			fail = Err_.Message_gplx_brief(e);
		}
		if (!pass)
			user.App().Usr_dlg().Prog_many("xowa.setup_mgr", "run.fail", "process exec failed: ~{0} ~{1} ~{2}", exe, arg, fail);
		return pass;
	}
}
