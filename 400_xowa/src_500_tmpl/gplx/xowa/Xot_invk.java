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
public interface Xot_invk {
	byte Defn_tid();
	boolean Root_frame();
	int Src_bgn();
	int Src_end();
	byte[] Frame_ttl(); void Frame_ttl_(byte[] v);
	int Args_len();
	Arg_nde_tkn Name_tkn();
	Arg_nde_tkn Args_get_by_idx(int i);
	Arg_nde_tkn Args_eval_by_idx(byte[] src, int idx);
	Arg_nde_tkn Args_get_by_key(byte[] src, byte[] key);
	byte Scrib_frame_tid(); void Scrib_frame_tid_(byte v);
}
