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
import java.util.*;	
public class RandomAdp_ implements GfoInvkAble {
	public static RandomAdp new_() {
		Random random = new Random(System.currentTimeMillis()); 
		return new RandomAdp(random);
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_Next))	return RandomAdp_.new_().Next(m.ReadInt("max"));
		else								return GfoInvkAble_.Rv_unhandled;
	}	static final String Invk_Next = "Next";
        public static final RandomAdp_ Gfs = new RandomAdp_();
}
