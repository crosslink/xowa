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
package gplx.xowa.bldrs.files; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.dbs.*; import gplx.xowa.dbs.*; import gplx.xowa.bldrs.oimgs.*;
class Xob_orig_regy_tbl {
	public static void Create_table(Db_provider p) {Sqlite_engine_.Tbl_create_and_delete(p, Tbl_name, Tbl_sql);}
	public static void Create_data(Gfo_usr_dlg usr_dlg, Db_provider p, Xodb_db_file file_registry_db, boolean repo_0_is_remote, Xow_wiki repo_0_wiki, Xow_wiki repo_1_wiki, boolean wiki_ns_for_file_is_case_match_all) {
		usr_dlg.Prog_many("", "", "inserting lnki_regy");
		p.Exec_sql(Sql_create_data);
		Sqlite_engine_.Idx_create(usr_dlg, p, "orig_regy", Idx_ttl);
		Sqlite_engine_.Db_attach(p, "page_db", file_registry_db.Url().Raw());
		Io_url repo_0_dir = repo_0_wiki.Fsys_mgr().Root_dir(), repo_1_dir = repo_1_wiki.Fsys_mgr().Root_dir();
		byte repo_0_tid = Xof_repo_itm.Repo_local, repo_1_tid = Xof_repo_itm.Repo_remote;
		boolean local_is_remote = Bry_.Eq(repo_0_wiki.Domain_bry(), repo_1_wiki.Domain_bry());
		if (	repo_0_is_remote														// .gfs manually marked specifes repo_0 as remote
			||	(	Bry_.Eq(repo_0_wiki.Domain_bry(), Xow_wiki_.Domain_commons_bry)	// repo_0 = commons; force repo_0 to be remote; else all orig_repo will be 1; DATE:2014-02-01
				&&	local_is_remote														// repo_0 = repo_1
				)
			) {
			repo_0_tid = Xof_repo_itm.Repo_remote;
			repo_1_tid = Xof_repo_itm.Repo_local;
		}
		Create_data_for_repo(usr_dlg, p, Byte_.int_(repo_0_tid), repo_0_dir.GenSubFil(Xodb_db_file.Name__wiki_image));
		if (!local_is_remote) {	// only run for repo_1 if local != remote; only affects commons
			Create_data_for_repo(usr_dlg, p, Byte_.int_(repo_1_tid), repo_1_dir.GenSubFil(Xodb_db_file.Name__wiki_image));
			if (wiki_ns_for_file_is_case_match_all) {
				Io_url repo_remote_dir = repo_0_is_remote ? repo_0_dir : repo_1_dir;
				Create_data_for_cs(usr_dlg, p, repo_remote_dir);
			}
		}
		Sqlite_engine_.Db_detach(p, "page_db");
		Sqlite_engine_.Idx_create(usr_dlg, p, "orig_regy", Idx_xfer_temp);
	}
	private static void Create_data_for_repo(Gfo_usr_dlg usr_dlg, Db_provider cur, byte wiki_tid, Io_url join) {
		usr_dlg.Note_many("", "", "inserting page: ~{0}", join.NameOnly());
		Sqlite_engine_.Db_attach(cur, "image_db", join.Raw());
		cur.Exec_sql(String_.Format(Sql_update_repo_page, wiki_tid));
		cur.Exec_sql(String_.Format(Sql_update_repo_redirect, wiki_tid));
		Sqlite_engine_.Db_detach(cur, "image_db");
	}
	private static void Create_data_for_cs(Gfo_usr_dlg usr_dlg, Db_provider p, Io_url repo_remote_dir) {
		p.Exec_sql(Xob_orig_regy_tbl.Sql_cs_mark_dupes);	// orig_regy: find dupes; see note in SQL
		p.Exec_sql(Xob_orig_regy_tbl.Sql_cs_update_ttls);	// orig_regy: update lnki_ttl with lnki_commons_ttl
		Create_data_for_repo(usr_dlg, p, Xof_repo_itm.Repo_remote, repo_remote_dir.GenSubFil(Xodb_db_file.Name__wiki_image));
		p.Exec_sql(Xob_lnki_regy_tbl.Sql_cs_mark_changed);	// lnki_regy: update lnki_ttl with lnki_commons_ttl
		p.Exec_sql(Xob_lnki_regy_tbl.Sql_cs_update_ttls);	// lnki_regy: update lnki_ttl with lnki_commons_ttl
	}
	public static final String Tbl_name = "orig_regy"
	, Fld_lnki_id = "lnki_id", Fld_lnki_ttl = "lnki_ttl", Fld_lnki_ext = "lnki_ext", Fld_lnki_count = "lnki_count"
	, Fld_orig_repo = "orig_repo", Fld_orig_page_id = "orig_page_id"
	, Fld_orig_redirect_id = "orig_redirect_id", Fld_orig_redirect_ttl = "orig_redirect_ttl", Fld_orig_file_id = "orig_file_id", Fld_orig_file_ttl = "orig_file_ttl"
	, Fld_orig_size = "orig_size", Fld_orig_w = "orig_w", Fld_orig_h = "orig_h", Fld_orig_bits = "orig_bits"
	, Fld_orig_media_type = "orig_media_type", Fld_orig_minor_mime = "orig_minor_mime", Fld_orig_file_ext = "orig_file_ext";
	private static final Db_idx_itm
		Idx_ttl     		= Db_idx_itm.sql_("CREATE INDEX IF NOT EXISTS orig_regy__ttl           ON orig_regy (lnki_ttl);")
	,   Idx_xfer_temp 		= Db_idx_itm.sql_("CREATE INDEX IF NOT EXISTS orig_regy__xfer_temp     ON orig_regy (lnki_ttl, orig_file_ttl);")
	;
	private static final String
		Tbl_sql = String_.Concat_lines_nl
	(	"CREATE TABLE IF NOT EXISTS orig_regy"
	,	"( lnki_id                     integer             NOT NULL			PRIMARY KEY"	// NOTE: must be PRIMARY KEY, else later REPLACE INTO will create dupe rows
	,	", lnki_ttl                    varchar(256)        NOT NULL"
	,	", lnki_commons_ttl            varchar(256)        NULL"
	,	", orig_commons_flag           integer             NULL"
	,	", lnki_ext                    integer             NOT NULL"
	,	", lnki_count                  integer             NOT NULL"
	,	", orig_repo                   integer             NULL"
	,	", orig_page_id                integer             NULL"
	,	", orig_redirect_id            integer             NULL"
	,	", orig_redirect_ttl           varchar(256)        NULL"
	,	", orig_file_id                integer             NULL"
	,	", orig_file_ttl               varchar(256)        NULL"
	,	", orig_file_ext               integer             NULL"
	,	", orig_size                   integer             NULL"
	,	", orig_w                      integer             NULL"
	,	", orig_h                      integer             NULL"
	,	", orig_bits                   smallint            NULL"
	,	", orig_media_type             varchar(64)         NULL"
	,	", orig_minor_mime             varchar(32)         NULL"
	,	");"
	)
	,	Sql_create_data = String_.Concat_lines_nl
	(	"INSERT INTO orig_regy (lnki_id, lnki_ttl, lnki_commons_ttl, orig_commons_flag, lnki_ext, lnki_count)"
	,	"SELECT  Min(lnki_id)"
	,	",       lnki_ttl"
	,	",       lnki_commons_ttl"
	,	",       NULL"
	,	",       lnki_ext"
	,	",       Sum(lnki_count)"
	,	"FROM    lnki_regy"
	,	"GROUP BY lnki_ttl"
	,	",       lnki_ext"
	,	"ORDER BY 1"	// must order by lnki_id since it is PRIMARY KEY, else sqlite will spend hours shuffling rows in table
	,	";"
	)
	,	Sql_update_repo_page = String_.Concat_lines_nl
	(	"REPLACE INTO orig_regy"
	,	"SELECT  o.lnki_id"
	,	",       o.lnki_ttl"
	,	",       o.lnki_commons_ttl"
	,	",       o.orig_commons_flag"
	,	",       o.lnki_ext"
	,	",       o.lnki_count"
	,	",       {0}"
	,	",       m.src_id"
	,	",       NULL"
	,	",       NULL"
	,	",       m.src_id"
	,	",       m.src_ttl"
	,	",       i.img_ext_id"
	,	",       i.img_size"
	,	",       i.img_width"
	,	",       i.img_height"
	,	",       i.img_bits"
	,	",       i.img_media_type"
	,	",       i.img_minor_mime"
	,	"FROM    orig_regy o"
	,	"        JOIN image_db.image i ON o.lnki_ttl = i.img_name"
	,	"        JOIN page_db.page_regy m ON m.repo_id = {0} AND m.itm_tid = 0 AND o.lnki_ttl = m.src_ttl"
	,	"WHERE   o.orig_file_ttl IS NULL"
	,	"ORDER BY 1"	// must order by lnki_id since it is PRIMARY KEY, else sqlite will spend hours shuffling rows in table
	,	";"
	)
	,	Sql_update_repo_redirect = String_.Concat_lines_nl
	(	"REPLACE INTO orig_regy"
	,	"SELECT  o.lnki_id"
	,	",       o.lnki_ttl"
	,	",       o.lnki_commons_ttl"
	,	",       o.orig_commons_flag"
	,	",       o.lnki_ext"
	,	",       o.lnki_count"
	,	",       {0}"
	,	",       m.src_id"
	,	",       m.trg_id"
	,	",       m.trg_ttl"
	,	",       m.trg_id"
	,	",       m.trg_ttl"
	,	",       i.img_ext_id"
	,	",       i.img_size"
	,	",       i.img_width"
	,	",       i.img_height"
	,	",       i.img_bits"
	,	",       i.img_media_type"
	,	",       i.img_minor_mime"
	,	"FROM    orig_regy o"
	,	"        JOIN page_db.page_regy m ON m.repo_id = {0} AND m.itm_tid = 1 AND o.lnki_ttl = m.src_ttl"
	,	"            JOIN image_db.image i ON m.trg_ttl = i.img_name"
	,	"WHERE   o.orig_file_ttl IS NULL"
	,	"ORDER BY 1"	// must order by lnki_id since it is PRIMARY KEY, else sqlite will spend hours shuffling rows in table
	,	";"
	)
	,	Sql_cs_mark_dupes = String_.Concat_lines_nl
	(	"REPLACE INTO orig_regy"
	,	"SELECT  o.lnki_id"
	,	",       o.lnki_ttl"
	,	",       o.lnki_commons_ttl"
	,	",       1"
	,	",       o.lnki_ext"
	,	",       o.lnki_count"
	,	",       o.orig_repo"
	,	",       o.orig_page_id"
	,	",       o.orig_redirect_id"
	,	",       o.orig_redirect_ttl"
	,	",       o.orig_file_id"
	,	",       o.orig_file_ttl"
	,	",       o.orig_file_ext"
	,	",       o.orig_size"
	,	",       o.orig_w"
	,	",       o.orig_h"
	,	",       o.orig_bits"
	,	",       o.orig_media_type"
	,	",       o.orig_minor_mime"
	,	"FROM    orig_regy o"
	,	"        JOIN orig_regy o2 ON o.lnki_commons_ttl = o2.lnki_ttl"	// EX: 2 rows in table (1) A.jpg; and (2) "a.jpg,A.jpg"; do not insert row (2) b/c row (1) exists;
	,	"WHERE   o.orig_file_ttl IS NULL"
	,	"ORDER BY 1"	// must order by lnki_id since it is PRIMARY KEY, else sqlite will spend hours shuffling rows in table
	,	";"
	)
	,	Sql_cs_update_ttls = String_.Concat_lines_nl
	(	"UPDATE  orig_regy"
	,	"SET     lnki_ttl = lnki_commons_ttl"
	,	",       orig_commons_flag = 2"
	,	"WHERE   orig_file_ttl IS NULL"			// orig not found
	,	"AND     lnki_commons_ttl IS NOT NULL"	// orig commons ttl exists
	,	"AND     orig_commons_flag IS NULL"		// orig is not dupe
	,	";"
	)
	;
}
