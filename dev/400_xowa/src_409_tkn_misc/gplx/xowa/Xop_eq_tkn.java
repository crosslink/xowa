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
public class Xop_eq_tkn extends Xop_tkn_itm_base {//20111222
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_eq;}
	public int Eq_len() {return eq_len;} private int eq_len = -1;
	public int Eq_ws_rhs_bgn() {return eq_ws_rhs_bgn;} public Xop_eq_tkn Eq_ws_rhs_bgn_(int v) {eq_ws_rhs_bgn = v; return this;} private int eq_ws_rhs_bgn = -1;
	public Xop_eq_tkn(int bgn, int end, int eq_len) {this.Tkn_ini_pos(false, bgn, end); this.eq_len = eq_len;}
}
