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
public class Sqlite_engine_ {
	public static void Db_attach(Db_provider p, String alias, String url) {
		String s = String_.Format("ATTACH '{0}' AS {1};", url, alias);
		Db_qry qry = Db_qry_sql.xtn_(s);
		p.Exec_qry(qry);
	}
	public static void Db_detach(Db_provider p, String alias) {
		String s = String_.Format("DETACH '{0}';", alias);
		Db_qry qry = Db_qry_sql.xtn_(s);
		p.Exec_qry(qry);
	}
	public static void Tbl_create_and_delete(Db_provider p, String tbl_name, String tbl_sql) {
		Tbl_delete(p, tbl_name);
		Db_qry qry = Db_qry_sql.ddl_(tbl_sql);
		p.Exec_qry(qry);
	}
	public static void Tbl_create(Db_provider p, String tbl_name, String tbl_sql) {
		Db_qry qry = Db_qry_sql.ddl_(tbl_sql);
		p.Exec_qry(qry);
	}
	public static void Tbl_delete_many(Db_provider p, String... tbls) {
		int len = tbls.length;
		for (int i = 0; i < len; i++)
			Tbl_delete(p, tbls[i]);
	}
	public static void Tbl_delete(Db_provider p, String tbl) {
		Db_qry qry = Db_qry_sql.ddl_("DROP TABLE IF EXISTS " + tbl + ";");
		p.Exec_qry(qry);
	}
	public static void Tbl_rename(Db_provider p, String src, String trg) {
		Db_qry qry = Db_qry_sql.ddl_(String_.Format("ALTER TABLE {0} RENAME TO {1};", src, trg));
		p.Exec_qry(qry);
	}
	public static void Pragma_page_size_4096(Db_provider p) {Pragma_page_size(p, 4096);}
	public static void Pragma_page_size(Db_provider p, int val) {
		Db_qry qry = Db_qry_sql.ddl_("PRAGMA page_size = " + Int_.XtoStr(val) + ";");
		p.Exec_qry(qry);
	}
	public static void Idx_create(Db_provider p, Db_idx_itm... idxs) {Idx_create(Gfo_usr_dlg_.Null, p, "", idxs);}
	public static void Idx_create(Gfo_usr_dlg usr_dlg, Db_provider p, String file_id, Db_idx_itm... idxs) {
		int len = idxs.length;
		p.Txn_mgr().Txn_end_all();	// commit any pending transactions
		for (int i = 0; i < len; i++) {
			p.Txn_mgr().Txn_bgn_if_none();
			String index = idxs[i].Xto_sql();
			usr_dlg.Prog_many("", "", "creating index: ~{0} ~{1}", file_id, index);
			p.Exec_qry(Db_qry_sql.ddl_(index));
			p.Txn_mgr().Txn_end_all();
		}
	}
	public static Db_provider Provider_load_or_fail_(Io_url url) {
		boolean exists = Io_mgr._.ExistsFil(url);
		if (!exists) throw Err_.new_fmt_("db does not exist; url=~{0}", url.Raw());
		Db_connect connect = Db_connect_sqlite.load_(url); 
		return Db_provider_.new_(connect);
	}
	public static Db_provider Provider_load_or_make_(Io_url url) {return Provider_load_or_make_(url, Bool_obj_ref.n_());}
	public static Db_provider Provider_load_or_make_(Io_url url, Bool_obj_ref created) {
		boolean exists = Io_mgr._.ExistsFil(url);
		created.Val_(!exists);
		Db_connect connect = exists ? Db_connect_sqlite.load_(url) : Db_connect_sqlite.make_(url); 
		Db_provider p = Db_provider_.new_(connect);
		if (!exists)
			Pragma_page_size(p, 4096);
		return p;
	}
	public static final int Stmt_arg_max = 999;		// 999 is max number of variables allowed by sqlite
	public static final boolean Supports_read_binary_stream = false;	
	public static final boolean Supports_indexed_by = true;			
	public static String X_date_to_str(DateAdp v) {return v == Date_null ? "" : v.XtoStr_fmt_iso_8561();}
	public static final DateAdp Date_null = null;
	public static final byte Wildcard_byte = Byte_ascii.Hash;
}
