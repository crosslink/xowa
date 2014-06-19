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
package gplx.gfui; import gplx.*;
public interface Gfui_dlg_msg {
	Gfui_dlg_msg Init_msg_(String v);
	Gfui_dlg_msg Init_ico_(int v);
	Gfui_dlg_msg Init_btns_(int... ary);
	int Ask();
	boolean Ask(int expd);
}
class Gfui_dlg_msg_null implements Gfui_dlg_msg {
	public Gfui_dlg_msg Init_msg_(String v) {return this;}
	public Gfui_dlg_msg Init_ico_(int v) {return this;}
	public Gfui_dlg_msg Init_btns_(int... ary) {return this;}
	public boolean Ask(int expd) {return false;}
	public int Ask() {return Int_.MinValue;}
	public static final Gfui_dlg_msg_null _ = new Gfui_dlg_msg_null(); Gfui_dlg_msg_null() {}
}
