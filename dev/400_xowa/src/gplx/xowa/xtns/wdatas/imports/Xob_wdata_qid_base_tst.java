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
package gplx.xowa.xtns.wdatas.imports; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.wdatas.*;
import org.junit.*;
import gplx.xowa.wikis.*;
public class Xob_wdata_qid_base_tst {		
	@Before public void init() {fxt = new gplx.xowa.bldrs.Xob_fxt().Ctor_mem();} gplx.xowa.bldrs.Xob_fxt fxt; // NOTE: reset memory instance (don't just call clear)
	@Test  public void Basic() {	
		fxt.doc_ary_
		(	fxt.doc_wo_date_(2, "q2", Xob_wdata_pid_base_tst.json_("q2", "links", String_.Ary("enwiki", "q2_en", "frwiki", "q2_fr")))
		,	fxt.doc_wo_date_(1, "q1", Xob_wdata_pid_base_tst.json_("q1", "links", String_.Ary("enwiki", "q1_en", "frwiki", "q1_fr")))
		)
		.Fil_expd(ttl_(fxt.Wiki(), "enwiki", "000", 0)
		,	"!!!!*|!!!!*|"
		,	"Q1_en|q1"
		,	"Q2_en|q2"
		,	""
		)
		.Fil_expd
		(	reg_(fxt.Wiki(), "enwiki", "000")
		,	"0|Q1_en|Q2_en|2"
		,	""
		)
		.Fil_expd(ttl_(fxt.Wiki(), "frwiki", "000", 0)
		,	"!!!!*|!!!!*|"
		,	"Q1_fr|q1"
		,	"Q2_fr|q2"
		,	""
		)
		.Fil_expd
		(	reg_(fxt.Wiki(), "frwiki", "000")
		,	"0|Q1_fr|Q2_fr|2"
		,	""
		)
		.Run(new Xob_wdata_qid_txt().Ctor(fxt.Bldr(), this.fxt.Wiki()))
		;
	}
	@Test  public void Ns() {	
		fxt.doc_ary_
		(	fxt.doc_wo_date_(1, "11", Xob_wdata_pid_base_tst.json_("q1", "links", String_.Ary("enwiki", "Help:Q1_en", "frwiki", "Aide:Q1_fr")))
		)
		.Fil_expd(ttl_(fxt.Wiki(), "enwiki", "012", 0)
		,	"!!!!*|"
		,	"Q1_en|q1"
		,	""
		)
		.Fil_expd
		(	reg_(fxt.Wiki(), "enwiki", "012")
		,	"0|Q1_en|Q1_en|1"
		,	""
		)
		.Fil_expd(ttl_(fxt.Wiki(), "frwiki", "012", 0)
		,	"!!!!*|"
		,	"Q1_fr|q1"
		,	""
		)
		.Fil_expd
		(	reg_(fxt.Wiki(), "frwiki", "012")
		,	"0|Q1_fr|Q1_fr|1"
		,	""
		)
		.Run(new Xob_wdata_qid_txt().Ctor(fxt.Bldr(), this.fxt.Wiki()))
		;
	}
	@Test  public void Links_w_name() {	// PURPOSE: wikidata changed links node from "enwiki:A" to "enwiki:{name:A,badges:[]}"; DATE:2013-09-14
		String q1_str = String_.Concat_lines_nl
		(	"{ \"entity\":\"q1\""
		,	", \"links\":"
		,	"  { \"enwiki\":\"q1_en\""
		,	"  , \"frwiki\":\"q1_fr\""
		,	"  }"
		,	"}"
		);
		String q2_str = String_.Concat_lines_nl
		(	"{ \"entity\":[\"item\",2]"
		,	", \"links\":"
		,	"  { \"enwiki\":{\"name\":\"q2_en\",\"badges\":[]}"
		,	"  , \"frwiki\":{\"name\":\"q2_fr\",\"badges\":[]}"
		,	"  }"
		,	"}"
		);
		fxt.doc_ary_
		(	fxt.doc_wo_date_(1, "q1", q1_str)
		,	fxt.doc_wo_date_(2, "q2", q2_str)
		)
		.Fil_expd(ttl_(fxt.Wiki(), "enwiki", "000", 0)
		,	"!!!!*|!!!!*|"
		,	"Q1_en|q1"
		,	"Q2_en|q2"
		,	""
		)
		.Fil_expd
		(	reg_(fxt.Wiki(), "enwiki", "000")
		,	"0|Q1_en|Q2_en|2"
		,	""
		)
		.Fil_expd(ttl_(fxt.Wiki(), "frwiki", "000", 0)
		,	"!!!!*|!!!!*|"
		,	"Q1_fr|q1"
		,	"Q2_fr|q2"
		,	""
		)
		.Fil_expd
		(	reg_(fxt.Wiki(), "frwiki", "000")
		,	"0|Q1_fr|Q2_fr|2"
		,	""
		)
		.Run(new Xob_wdata_qid_txt().Ctor(fxt.Bldr(), this.fxt.Wiki()))
		;
	}
	public static Io_url reg_(Xow_wiki wdata, String wiki, String ns_id) {
		return Wdata_idx_wtr.dir_qid_(wdata, wiki, ns_id).GenSubFil(Xow_dir_info_.Name_reg_fil);
	}
	public static Io_url ttl_(Xow_wiki wdata, String wiki, String ns_id, int fil_id) {
		Io_url root = Wdata_idx_wtr.dir_qid_(wdata, wiki, ns_id);
		return Xow_fsys_mgr.Url_fil(root, fil_id, Xow_dir_info_.Bry_xdat);
	}
}
