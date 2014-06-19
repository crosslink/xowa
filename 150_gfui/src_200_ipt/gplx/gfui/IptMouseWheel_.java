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
public class IptMouseWheel_ {
	public static final IptMouseWheel
		  None	= new IptMouseWheel("wheel.none")
		, Up	= new IptMouseWheel("wheel.up")
		, Down	= new IptMouseWheel("wheel.down");
	public static IptMouseWheel parse_(String raw) {
		if		(String_.Eq(raw, None.Key()))	return None;
		else if	(String_.Eq(raw, Up.Key()))		return Up;
		else if	(String_.Eq(raw, Down.Key()))	return Down;
		else throw Err_.parse_type_(IptMouseWheel.class, raw);
	}
	@gplx.Internal protected static IptMouseWheel api_(Object obj) {
		int delta = Int_.cast_(obj);
		if		(delta > 0)		return Up;
		else if (delta < 0)		return Down;
		else					return None;
	}
}
