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
import gplx.criterias.*;
class Sql_where {
	public Criteria Crt() {return crt;} Criteria crt;
	public static Sql_where merge_or_new_(Sql_where where, Criteria crt) {
		return where == null
			? Sql_where.new_(crt)
			: Sql_where.new_(Criteria_.And(where.Crt(), crt));
	}
	public static Sql_where new_(Criteria crt) {
		Sql_where rv = new Sql_where();
		rv.crt = crt;
		return rv;
	}	Sql_where() {}
}
