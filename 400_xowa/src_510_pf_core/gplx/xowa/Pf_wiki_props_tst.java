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
package gplx.xowa; import gplx.*;
import org.junit.*;
public class Pf_wiki_props_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before	public void setup()						{fxt.Reset(); fxt.Wiki().Stats().NumPages_(1).NumArticles_(2).NumFiles_(3).NumEdits_(4).NumViews_(5).NumUsers_(6).NumUsersActive_(7).NumAdmins_(8);}
	@After public void teardown()				{}
	@Test  public void NumPages()					{fxt.Test_parse_tmpl_str_test("{{NUMBEROFPAGES}}"				, "{{test}}", "1");}
	@Test  public void NumArticles()				{fxt.Test_parse_tmpl_str_test("{{NUMBEROFARTICLES}}"				, "{{test}}", "2");}
	@Test  public void NumFiles()					{fxt.Test_parse_tmpl_str_test("{{NUMBEROFFILES}}"				, "{{test}}", "3");}
	@Test  public void NumEdits()					{fxt.Test_parse_tmpl_str_test("{{NUMBEROFEDITS}}"				, "{{test}}", "4");}
	@Test  public void NumViews()					{fxt.Test_parse_tmpl_str_test("{{NUMBEROFVIEWS}}"				, "{{test}}", "5");}
	@Test  public void NumUsers()					{fxt.Test_parse_tmpl_str_test("{{NUMBEROFUSERS}}"				, "{{test}}", "6");}
	@Test  public void NumUsersActive()				{fxt.Test_parse_tmpl_str_test("{{NUMBEROFACTIVEUSERS}}"			, "{{test}}", "7");}
	@Test  public void NumAdmins()					{fxt.Test_parse_tmpl_str_test("{{NUMBEROFADMINS}}"				, "{{test}}", "8");}

	@Test  public void NumArticles_fmt()			{Set_numArticles_(1234); fxt.Test_parse_tmpl_str_test("{{NUMBEROFARTICLES}}"				, "{{test}}", "1,234"); Set_numArticles_(2);}
	@Test  public void NumArticles_raw()			{Set_numArticles_(1234); fxt.Test_parse_tmpl_str_test("{{NUMBEROFARTICLES:R}}"			, "{{test}}", "1234"); Set_numArticles_(2);}
	Pf_wiki_props_tst Set_numArticles_(int v) {fxt.Wiki().Stats().NumArticles_(v); return this;}
}
