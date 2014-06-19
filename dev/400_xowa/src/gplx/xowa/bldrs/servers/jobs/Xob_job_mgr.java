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
package gplx.xowa.bldrs.servers.jobs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*; import gplx.xowa.bldrs.servers.*;
public class Xob_job_mgr {
	private OrderedHash jobs = OrderedHash_.new_();
	public int Count() {return jobs.Count();}
	public Xob_job_itm Get_at(int i) {return (Xob_job_itm)jobs.FetchAt(i);}
	public Xob_job_itm Get(String k) {return (Xob_job_itm)jobs.Fetch(k);}
	public void Load(String text) {
		jobs.Add(null, null);
	}
}
