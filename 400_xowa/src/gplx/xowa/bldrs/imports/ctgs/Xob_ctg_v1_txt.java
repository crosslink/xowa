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
package gplx.xowa.bldrs.imports.ctgs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*; import gplx.xowa.bldrs.imports.*;
import gplx.ios.*;
public class Xob_ctg_v1_txt extends Xob_ctg_v1_base {
	@Override public String Wkr_key() {return KEY;} public static final String KEY = "core.make_category";
	@Override public Io_sort_cmd Make_sort_cmd() {return new Xob_make_cmd_site(bldr.Usr_dlg(), make_dir, make_fil_len);}
}
