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
import gplx.ios.*;
import gplx.gfui.*;
public interface Xof_download_wkr {
	byte Download(boolean src_is_web, String src, Io_url trg, String prog_fmt_hdr);
	IoEngine_xrg_downloadFil Download_xrg();
}
class Xof_download_wkr_test implements Xof_download_wkr {
	public IoEngine_xrg_downloadFil Download_xrg() {return IoEngine_xrg_downloadFil.new_("", Io_url_.Null).Trg_engine_key_(IoEngine_.MemKey);}
	public byte Download(boolean src_is_web, String src_str, Io_url trg_url, String prog_fmt_hdr) {
		Io_mgr._.CreateDirIfAbsent(trg_url.OwnerDir());
		Io_url src_url = Io_url_.new_fil_(src_str);
		if (!Io_mgr._.ExistsFil(src_url)) return IoEngine_xrg_downloadFil.Rslt_fail_file_not_found;
		try {Io_mgr._.CopyFil(src_url, trg_url, true);}
		catch (Exception exc) {Err_.Noop(exc); return IoEngine_xrg_downloadFil.Rslt_fail_unknown;}
		return IoEngine_xrg_downloadFil.Rslt_pass;
	}
}
class Xof_download_wkr_io implements Xof_download_wkr {
	IoEngine_xrg_downloadFil xrg = Io_mgr._.DownloadFil_args("", Io_url_.Null);
	public Xof_download_wkr_io(Xoa_app app) {}
	public IoEngine_xrg_downloadFil Download_xrg() {return xrg;}
	public String Download_err() {return download_err;} private String download_err = "";
	public byte Download(boolean src_is_web, String src_str, Io_url trg_url, String prog_fmt_hdr) {
		download_err = "";
		if (src_is_web) {
			xrg.Prog_fmt_hdr_(prog_fmt_hdr);
			xrg.Init(src_str, trg_url);
			xrg.Exec();
			return xrg.Rslt();
		}
		else {
			Io_url src_url = Io_url_.new_fil_(src_str);
			if (!Io_mgr._.ExistsFil(src_url)) return IoEngine_xrg_downloadFil.Rslt_fail_file_not_found;
			try {Io_mgr._.CopyFil(src_url, trg_url, true);}
			catch (Exception exc) {Err_.Noop(exc); return IoEngine_xrg_downloadFil.Rslt_fail_unknown;}
			return IoEngine_xrg_downloadFil.Rslt_pass;
		}
	}
}
