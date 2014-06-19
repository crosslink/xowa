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
import java.io.*;
import java.util.zip.*;
public class Io_zip_mgr_base implements Io_zip_mgr {
	public void Zip_fil(Io_url src_fil, Io_url trg_fil) {
		byte[] src_bry = Io_mgr._.LoadFilBry(src_fil);
		byte[] trg_bry = Zip_bry(src_bry, 0, src_bry.length);
		Io_mgr._.SaveFilBry(trg_fil, trg_bry);
	}
		public void Zip_dir(Io_url src_dir, Io_url trg_fil) {
		try {
			byte[] bry = new byte[4096];
			FileOutputStream fil_strm = new FileOutputStream(trg_fil.Raw());
			ZipOutputStream zip_strm = new ZipOutputStream(fil_strm);
			Zip_dir__add_dir(zip_strm, bry, "", src_dir, Zip_dir__get_subs(src_dir));
			zip_strm.flush();
			zip_strm.close();
		} 	catch(IOException exc) {throw Err_.err_(exc, "error duing zip: src=~{0} trg=~{1}", src_dir.Raw(), trg_fil.Raw());}
	}
	private void Zip_dir__add_dir(ZipOutputStream zip_strm, byte[] bry, String zip_path, Io_url owner_dir, Io_url[] subs) {
		int len = subs.length;
		for (int i = 0; i < len; i++) {
			Io_url sub = subs[i];
			String sub_path = zip_path + sub.NameAndExt_noDirSpr(); 
			if (sub.Type_dir())
				Zip_dir__add_dir(zip_strm, bry, sub_path + "/", sub, Zip_dir__get_subs(sub));
			else
				Zip_dir__add_fil(zip_strm, bry, sub_path, sub);
		}
	}
	private void Zip_dir__add_fil(ZipOutputStream zip_strm, byte[] bry, String zip_path, Io_url fil_url) {
		try {
			int len;
			FileInputStream fil_strm = new FileInputStream(fil_url.Raw());
			zip_strm.putNextEntry(new ZipEntry(zip_path));
			while ((len = fil_strm.read(bry)) > 0)
				zip_strm.write(bry, 0, len);
			fil_strm.close();
		} 	catch(IOException exc) {throw Err_.err_(exc, "error duing zip: src=~{0}", zip_path);}
	}
	private Io_url[] Zip_dir__get_subs(Io_url url) {
		return Io_mgr._.QueryDir_args(url).DirInclude_().ExecAsUrlAry();
	}
		public byte[] Zip_bry(byte[] src, int bgn, int len)  {
				ByteArrayInputStream src_stream = new ByteArrayInputStream(src, bgn, len);
		ByteArrayOutputStream trg_stream = new ByteArrayOutputStream(len);
		try {
			ZipOutputStream trgZip = new ZipOutputStream(trg_stream);
			ZipEntry entry = new ZipEntry("file");
			trgZip.putNextEntry(entry);
			int count;
			while((count = src_stream.read(tmp, 0, tmpLen)) != -1) {
				trgZip.write(tmp, 0, count);
			}
			trgZip.close();
		}	catch(Exception e) {throw Err_.new_("failed to zip").Add("err", e.getMessage());}
		return trg_stream.toByteArray();
			}
	public byte[] Unzip_bry(byte[] src, int bgn, int len)  {
				ByteArrayInputStream src_stream = new ByteArrayInputStream(src, bgn, len);
		ByteArrayOutputStream trg_stream = new ByteArrayOutputStream(len);
		try {
			ZipInputStream srcZip = new ZipInputStream(src_stream);
			int count;
			while(srcZip.getNextEntry() != null) {
				while ((count = srcZip.read(tmp, 0, tmpLen)) != -1) {
					trg_stream.write(tmp, 0, count);
				}
			}
		}	catch(Exception e) {throw Err_.new_("failed to unzip").Add("err", e.getMessage());}
		return trg_stream.toByteArray();
			}
	public void Unzip_to_dir(Io_url src_fil, Io_url trg_dir) {
				byte[] buffer = new byte[4096];
		try{
			Io_mgr._.CreateDirIfAbsent(trg_dir);
			
			ZipInputStream zip_strm = new ZipInputStream(new FileInputStream(src_fil.Raw()));
			ZipEntry zip_eny = zip_strm.getNextEntry();
			while (zip_eny != null) {
				String itm_name = zip_eny.getName();
				if (Op_sys.Cur().Tid_is_wnt()) itm_name = String_.Replace(itm_name, "/", "\\");
				Io_url itm_url = Io_url_.new_any_(trg_dir.GenSubFil(itm_name).Raw());
				Io_mgr._.CreateDirIfAbsent(itm_url.OwnerDir());	// make sure owner dir exists
				if (itm_url.Type_fil()) {
					Io_mgr._.SaveFilStr_args(itm_url, "").Exec();
					File itm_file = new File(itm_url.Raw());
					FileOutputStream itm_strm = new FileOutputStream(itm_file);						 
					int len;
					while ((len = zip_strm.read(buffer)) > 0)
						itm_strm.write(buffer, 0, len);
					itm_strm.close();	 
				}
				zip_eny = zip_strm.getNextEntry();
			}
			zip_strm.closeEntry();
			zip_strm.close();
		} 	catch(IOException exc) {throw Err_.err_(exc, "error duing unzip: src=~{0} trg=~{1}", src_fil.Raw(), trg_dir.Raw());}
			}
		byte[] tmp = new byte[4096]; int tmpLen = 4096;
		public static final Io_zip_mgr _ = new Io_zip_mgr_base();
}
