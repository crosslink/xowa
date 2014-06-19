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
package gplx.xowa.xtns.scribunto; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Scrib_proc_mgr {
	private OrderedHash hash = OrderedHash_.new_();
	public void Clear() {hash.Clear();}
	public Scrib_proc Get_by_key(String key) {return (Scrib_proc)hash.Fetch(key);}
	public Scrib_proc Get_at(int i) {return (Scrib_proc)hash.FetchAt(i);}
	public void Set(String key, Scrib_proc proc) {
		hash.AddReplace(key, proc);		// WORKAROUND: AddReplace b/c some libraries reuse proc name; EX: getGlobalSiteId is used by mw.wikibase.lua and mw.wikibase.entity.lua
	}
	public Scrib_proc Set(Scrib_lib lib, String proc_name, int proc_id) {
		Scrib_proc proc = new Scrib_proc(lib, proc_name, proc_id);
		Set(proc.Proc_key(), proc);
		return proc;
	}
	public int Len() {return hash.Count();}
	public void Init_by_lib(Scrib_lib lib, String[] proc_names) {
		hash.Clear();
		int len = proc_names.length;
		for (int i = 0; i < len; i++) {
			String proc_name = proc_names[i];
			Scrib_proc proc = new Scrib_proc(lib, proc_name, i);	// ASSUME: Proc_id consts matches order in which Init is called
			hash.Add(proc_name, proc);
		}
	}
}
