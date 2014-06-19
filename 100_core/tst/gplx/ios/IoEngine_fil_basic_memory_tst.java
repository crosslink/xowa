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
import org.junit.*;
public class IoEngine_fil_basic_memory_tst extends IoEngine_fil_basic_base {
	@Override protected IoEngine engine_() {return IoEngine_.Mem_init_();}
	@Override protected void setup_hook() {
		root = Io_url_.mem_dir_("mem");
		fil = root.GenSubFil_nest("root", "fil.txt");
	}
	@Test  @Override public void OpenStreamRead() {
		super.OpenStreamRead ();
	}
	@Test  @Override public void SaveFilText_overwrite() {
		super.SaveFilText_overwrite();

		// bugfix: verify changed file in ownerDir's hash
		IoItmDir dirItm = fx.tst_ScanDir(fil.OwnerDir(), fil);
		IoItmFil_mem filItm = (IoItmFil_mem)dirItm.SubFils().FetchAt(0);
		Tfds.Eq(filItm.Text(), "changed");
	}
	@Test  public void RecycleFil() {
		fx.run_SaveFilText(fil, "text");
		fx.tst_ExistsPaths(true, fil);

		IoRecycleBin bin = IoRecycleBin._;
		ListAdp list = Tfds.RscDir.XtoNames(); 
//			foreach (String s in list)
//				Tfds.Write(s);
		list.DelAt(0); // remove drive
		IoEngine_xrg_recycleFil recycleXrg = bin.Send_xrg(fil)
			.RootDirNames_(list)
			.AppName_("gplx.test").Time_(DateAdp_.parse_gplx("20100102_115559123")).Uuid_(UuidAdp_.parse_("467ffb41-cdfe-402f-b22b-be855425784b"));
		recycleXrg.Exec();
		fx.tst_ExistsPaths(false, fil);
		fx.tst_ExistsPaths(true, recycleXrg.RecycleUrl());

		bin.Recover(recycleXrg.RecycleUrl());
		fx.tst_ExistsPaths(true, fil);
		fx.tst_ExistsPaths(false, recycleXrg.RecycleUrl());
	}
}
