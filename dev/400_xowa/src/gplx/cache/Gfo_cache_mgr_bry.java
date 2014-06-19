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
package gplx.cache; import gplx.*;
public class Gfo_cache_mgr_bry extends Gfo_cache_mgr_base {
	public Object Get_or_null(byte[] key) {return Base_get_or_null(key);}
	public void Add(byte[] key, Object val) {Base_add(key, val);}
	public void Del(byte[] key) {Base_del(key);}
}
class Gfo_cache_itm {
	public Gfo_cache_itm(Object key, Object val) {this.key = key; this.val = val; this.Touched_update();}
	public Object Key() {return key;} private Object key;
	public Object Val() {return val;} private Object val;
	public long Touched() {return touched;} private long touched;
	public Gfo_cache_itm Touched_update() {touched = Env_.TickCount(); return this;}
}
class Gfo_cache_itm_comparer implements gplx.lists.ComparerAble {
	public int compare(Object lhsObj, Object rhsObj) {
		Gfo_cache_itm lhs = (Gfo_cache_itm)lhsObj;
		Gfo_cache_itm rhs = (Gfo_cache_itm)rhsObj;
		return Long_.Compare(lhs.Touched(), rhs.Touched());
	}
	public static final Gfo_cache_itm_comparer Touched_asc = new Gfo_cache_itm_comparer(); 
}
