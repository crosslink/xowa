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
package gplx.dbs; import gplx.*;
public class Db_connect_sqlite extends Db_connect_base {
	@Override public String Key_of_db_connect() {return KeyDef;} public static final String KeyDef = "sqlite";
	public Io_url Url() {return url;} Io_url url;
	public static Db_connect load_(Io_url url) {
		return Db_connect_.parse_(BldRaw(GfoMsg_.new_cast_("Db_connect")
			.Add("gplx_key", KeyDef)
			.Add("data source", url.Xto_api())
			.Add("version", 3)
			));
	}
	public static Db_connect make_(Io_url url) {
		Io_mgr._.CreateDirIfAbsent(url.OwnerDir());
		return Db_connect_.parse_(BldRaw(GfoMsg_.new_cast_("Db_connect")
			.Add("gplx_key", KeyDef)
			.Add("data source", url.Xto_api())
			.Add("version", 3)	
			));
	}
	@Override public Db_connect Clone_of_db_connect(String raw, GfoMsg m) {
		Db_connect_sqlite rv = new Db_connect_sqlite();
		String dataSourceUrl = m.ReadStr("data source");
		rv.url = Io_url_.new_any_(dataSourceUrl);
		rv.Ctor_of_db_connect("", dataSourceUrl, raw, BldApi(m, KeyVal_.new_("version", "3")));
		return rv;
	}
	public static final Db_connect_sqlite _ = new Db_connect_sqlite(); Db_connect_sqlite() {}
}