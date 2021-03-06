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
package gplx.dbs.joins; import gplx.*; import gplx.dbs.*;
import org.junit.*;
public abstract class Joins_base_tst {
	protected Db_provider provider;
	@Before public void setup() {
		provider = provider_();
		Db_qry_delete.new_().BaseTable_("dbs_crud_ops").Exec_qry(provider);
		Db_qry_delete.new_().BaseTable_("dbs_join1").Exec_qry(provider);
	}
	@After public void teardown() {
		provider.Rls();
	}
	protected void InnerJoin_hook() {
		provider.Exec_qry(Db_qry_insert.new_().BaseTable_("dbs_crud_ops").Arg_("id", 0).Arg_("name", "me"));
		provider.Exec_qry(Db_qry_insert.new_().BaseTable_("dbs_crud_ops").Arg_("id", 1).Arg_("name", "you"));
		provider.Exec_qry(Db_qry_insert.new_().BaseTable_("dbs_join1").Arg_("join_id", 0).Arg_("join_data", "data0"));
		provider.Exec_qry(Db_qry_insert.new_().BaseTable_("dbs_join1").Arg_("join_id", 1).Arg_("join_data", "data1"));
		Db_qry_select select = Db_qry_select.new_().From_("dbs_crud_ops").Join_("dbs_join1", "j1", Sql_join_itm.new_("join_id", "dbs_crud_ops", "id")).Cols_("id", "name", "join_data");

		DataRdr rdr = provider.Exec_qry_as_rdr(select);
		GfoNde table = GfoNde_.rdr_(rdr);
		Tfds.Eq(table.Subs().Count(), 2);
		Tfds.Eq(table.Subs().FetchAt_asGfoNde(0).Read("join_data"), "data0");
		Tfds.Eq(table.Subs().FetchAt_asGfoNde(1).Read("join_data"), "data1");
	}
	protected abstract Db_provider provider_();
}
