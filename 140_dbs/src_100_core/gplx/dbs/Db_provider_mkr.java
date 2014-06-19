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
public interface Db_provider_mkr {
	Db_provider Load_or_make_(Io_url db_url, Bool_obj_ref created_ref);
}
class Db_provider_mkr_sqlite implements Db_provider_mkr {
	public Db_provider Load_or_make_(Io_url db_url, Bool_obj_ref created_ref) {return Sqlite_engine_.Provider_load_or_make_(db_url, created_ref);}
        public static final Db_provider_mkr_sqlite _ = new Db_provider_mkr_sqlite(); Db_provider_mkr_sqlite() {}
}
class Db_provider_mkr_mem implements Db_provider_mkr {
	private boolean create;
	public Db_provider Load_or_make_(Io_url db_url, Bool_obj_ref create_ref) {create_ref.Val_(create); return null;}
        public static Db_provider_mkr_mem create_(boolean create) {
		Db_provider_mkr_mem rv = new Db_provider_mkr_mem();
		rv.create = create;
		return rv;
	}
}
