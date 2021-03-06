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
package gplx.gfui; import gplx.*;
import org.junit.*;
public class GftBand_tst {
	@Before public void setup() {
		fx.Clear().ini_OwnerSize(200, 400);
	}	GftGrid_fx fx = new GftGrid_fx();
	@Test public void Bands_1() {
		fx	.ini_AddItms(2)
			.ini_Set(0, GftBand.new_().Cells_num_(2))
			.run()
			.tst_Filter(0, 1).tst_X(0, 100).tst_W_all(100).tst_H_all(20).tst_Y_all(0);
	}
	@Test public void Bands_1_half() {	// only add 1 to 2 cell-band
		fx	.ini_AddItms(1)
			.ini_Set(0, GftBand.new_().Cells_num_(2))
			.run()
			.tst_Filter(0).tst_X(0).tst_W(100).tst_H(20).tst_Y(0);
	}
	@Test public void Bands_2() {		// put cells 2, 3 on band 1
		fx	.ini_AddItms(4)
			.ini_Set(0, GftBand.new_().Cells_num_(2))
			.ini_Set(1, GftBand.new_().Cells_num_(2))
			.run()
			.tst_Filter(0, 1).tst_X(0, 100).tst_W_all(100).tst_H_all(20).tst_Y_all(0)
			.tst_Filter(2, 3).tst_X(0, 100).tst_W_all(100).tst_H_all(20).tst_Y_all(20);	// put on 2nd row
	}
	@Test public void Pct_one() {
		fx	.ini_AddItms(1)
			.ini_Set(0, GftBand.new_().Cell_pct_(50))
			.run()
			.tst_Filter(0).tst_X(0).tst_W(100).tst_H_all(20).tst_Y_all(0);
	}
	@Test public void Pct_many() {
		fx	.ini_AddItms(3)
			.ini_Set(0, GftBand.new_().Cell_pct_(20).Cell_pct_(70).Cell_pct_(10))
			.run()
			.tst_Filter(0, 2).tst_W(40, 140, 20).tst_X(0, 40, 180).tst_H_all(20).tst_Y_all(0);
	}
	@Test public void Mix_pctAtEnd() {
		fx	.ini_AddItms(2)
			.ini_Set(0, GftBand.new_().Cell_abs_(60).Cell_pct_(100))
			.run()
			.tst_Filter(0, 1).tst_X(0, 60).tst_W(60, 140).tst_H_all(20).tst_Y_all(0);
	}
	@Test public void Mix_pctAtBgn() {
		fx	.ini_AddItms(2)
			.ini_Set(0, GftBand.new_().Cell_pct_(100).Cell_abs_(60))
			.run()
			.tst_Filter(0, 1).tst_X(0, 140).tst_W(140, 60).tst_H_all(20).tst_Y_all(0);
	}
	@Test public void Mix_pctAtMid() {
		fx	.ini_AddItms(3)
			.ini_Set(0, GftBand.new_().Cell_abs_(60).Cell_pct_(100).Cell_abs_(40))
			.run()
			.tst_Filter(0, 2).tst_X(0, 60, 160).tst_W(60, 100, 40).tst_H_all(20).tst_Y_all(0);
	}
	@Test public void Height_pct() {
		fx	.ini_AddItms(1)
			.ini_Set(0, GftBand.new_().Cell_pct_(100).Len1_pct_(100))
			.run()
			.tst_Filter(0).tst_X(0).tst_W(200).tst_H_all(400).tst_Y_all(0);
	}
	@Test public void Height_mix() {
		fx	.ini_AddItms(3)
			.ini_Set(0, GftBand.new_().Cells_num_(1).Len1_abs_( 60))
			.ini_Set(1, GftBand.new_().Cells_num_(1).Len1_pct_(100))
			.ini_Set(2, GftBand.new_().Cells_num_(1).Len1_abs_( 20))
			.run()
			.tst_Filter(0).tst_H( 60).tst_Y_all( 0).tst_X(0).tst_W(200)
			.tst_Filter(1).tst_H(320).tst_Y_all( 60).tst_X(0).tst_W(200)
			.tst_Filter(2).tst_H( 20).tst_Y_all(380).tst_X(0).tst_W(200);
	}
	@Test public void RevDir() {
		fx	.ini_AddItms(2).ini_BandDir(DirInt.Bwd)
			.ini_Set(0, 1, GftBand.new_().Cells_num_(1).Len1_abs_(20))
			.run()
			.tst_Filter(0).tst_W(200).tst_H(20).tst_X(0).tst_Y(380)
			.tst_Filter(1).tst_W(200).tst_H(20).tst_X(0).tst_Y(360);
	}
	@Test public void SubLyts() {
		fx	.ini_AddItms(2).ini_AddLyts(2)
			.ini_Lyt(0).ini_Set(0, GftBand.new_().Cells_num_(1).Len1_pct_(100))
			.ini_Lyt(1).ini_Set(0, GftBand.new_().Cells_num_(1).Len1_abs_( 20)).ini_BandDir(DirInt.Bwd)
			.run()
			.tst_Filter(0).tst_W(200).tst_H(400).tst_X(0).tst_Y(  0)
			.tst_Filter(1).tst_W(200).tst_H( 20).tst_X(0).tst_Y(380);
	}
	@Test public void Var() {
		fx	.ini_AddItms(2)
			.ini_ItmWidth(0, 30).ini_ItmWidth(1, 40)
			.ini_Set(0, GftBand.new_().Cells_var_(2))
			.run()
			.tst_Filter(0, 1).tst_X(0, 30).tst_W(30, 40).tst_H_all(20).tst_Y_all(0);
	}
}
