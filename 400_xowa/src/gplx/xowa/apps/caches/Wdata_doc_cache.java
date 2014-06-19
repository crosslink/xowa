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
package gplx.xowa.apps.caches; import gplx.*; import gplx.xowa.*; import gplx.xowa.apps.*;
import gplx.xowa.xtns.wdatas.*;
public class Wdata_doc_cache {
	private Hash_adp_bry hash = Hash_adp_bry.cs_();
	public void Add(byte[] qid, Wdata_doc doc) {hash.Add(qid, doc);}
	public Wdata_doc Get_or_null(byte[] qid) {return (Wdata_doc)hash.Get_by_bry(qid);}
	public void Free_mem_all() {this.Clear();}
	public void Clear() {hash.Clear();}
}
