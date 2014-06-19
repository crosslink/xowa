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
public abstract class IoEngine_dir_basic_base {
	@Before public void setup() {
		engine = engine_();
		fx = IoEngineFxt.new_();
		setup_hook();
	}	protected IoEngine engine; @gplx.Internal protected IoEngineFxt fx; protected Io_url fil, root;
	protected abstract IoEngine engine_();
	protected abstract void setup_hook();
	@Test  @gplx.Virtual public void CreateDir() {
		fx.tst_ExistsPaths(false, root);

		engine.CreateDir(root);
		fx.tst_ExistsPaths(true, root);
	}
	@Test  public void DeleteDir() {
		engine.CreateDir(root);
		fx.tst_ExistsPaths(true, root);

		engine.DeleteDir(root);
		fx.tst_ExistsPaths(false, root);
	}
	@Test  public void CreateDir_createAllOwners() {
		Io_url subDir = root.GenSubDir_nest("sub1");
		fx.tst_ExistsPaths(false, subDir, subDir.OwnerDir());

		engine.CreateDir(subDir);
		fx.tst_ExistsPaths(true, subDir, subDir.OwnerDir());
	}
//		@Test  public void DeleteDir_missing_fail() {
//			try {engine.DeleteDir(root);}
//			catch {return;}
//			Tfds.Fail_expdError();
//		}
	@Test  public void DeleteDir_missing_pass() {
		engine.DeleteDir(root);
	}
	@Test  @gplx.Virtual public void ScanDir() {
		Io_url fil = root.GenSubFil("fil1.txt"); fx.run_SaveFilText(fil, "test");
		Io_url dir1 = root.GenSubDir_nest("dir1"); engine.CreateDir(dir1); 
		Io_url dir1_1 = dir1.GenSubDir_nest("dir1_1"); engine.CreateDir(dir1_1); // NOTE: QueryDir should not recurse by default; dir1_1 should not be returned below
		
		fx.tst_ScanDir(root, dir1, fil);
	}
	@Test public void MoveDir() {
	Io_url src = root.GenSubDir_nest("src"), trg = root.GenSubDir_nest("trg");
	engine.CreateDir(src);
	fx.tst_ExistsPaths(true, src); fx.tst_ExistsPaths(false, trg);

	engine.MoveDir(src, trg);
	fx.tst_ExistsPaths(false, src); fx.tst_ExistsPaths(true, trg);
}
@Test @gplx.Virtual public void CopyDir() {
	Io_url src = root.GenSubDir_nest("src"), trg = root.GenSubDir_nest("trg");
	engine.CreateDir(src);
	fx.tst_ExistsPaths(true, src); fx.tst_ExistsPaths(false, trg);

	engine.CopyDir(src, trg);
	fx.tst_ExistsPaths(true, src, trg);
}
}
