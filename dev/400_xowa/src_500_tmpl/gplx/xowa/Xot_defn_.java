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
public class Xot_defn_ {
	public static final Xot_defn Null = Xot_defn_null._;
	public static final byte Tid_null = 0, Tid_func = 1, Tid_tmpl = 2, Tid_subst = Xol_kwd_grp_.Id_subst, Tid_safesubst = Xol_kwd_grp_.Id_safesubst, Tid_raw = Xol_kwd_grp_.Id_raw;
	public static boolean Tid_is_subst(byte v) {
		switch (v) {
			case Tid_subst: case Tid_safesubst: return true;
			default: return false;
		}
	}
}
