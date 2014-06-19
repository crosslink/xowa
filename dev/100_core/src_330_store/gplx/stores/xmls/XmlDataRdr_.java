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
package gplx.stores.xmls; import gplx.*; import gplx.stores.*;
public class XmlDataRdr_ {
	public static XmlDataRdr file_(Io_url url) {
		String text = Io_mgr._.LoadFilStr(url);
		return new XmlDataRdr(text);
	}
	public static XmlDataRdr text_(String text) {return new XmlDataRdr(text);}
}
