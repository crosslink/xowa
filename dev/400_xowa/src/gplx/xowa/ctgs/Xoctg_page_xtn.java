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
package gplx.xowa.ctgs; import gplx.*; import gplx.xowa.*;
public class Xoctg_page_xtn {
	public Xoctg_page_xtn(byte tid, byte[] sortkey) {this.tid = tid; this.sortkey = sortkey;}
	public byte[] Sortkey() {return sortkey;} public void Sortkey_(byte[] v) {this.sortkey = v;} private byte[] sortkey;
	public byte Tid() {return tid;} public void Tid_(byte v) {this.tid = v;} private byte tid;
	public byte Hidden() {return hidden;} public void Hidden_(byte v) {hidden = v;} private byte hidden = Bool_.__byte;
}
