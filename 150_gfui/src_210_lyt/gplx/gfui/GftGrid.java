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
public class GftGrid {
	public String Key() {return key;} public GftGrid Key_(String v) {key = v; return this;} private String key;
	public ListAdp Bands() {return bands;} ListAdp bands = ListAdp_.new_();
	public ListAdp SubLyts() {return subLyts;} ListAdp subLyts = ListAdp_.new_();
	public void Clear() {bands.Clear(); subLyts.Clear(); bandDir = DirInt.Fwd;}
	public DirInt Bands_dir() {return bandDir;} public GftGrid Bands_dir_(DirInt v) {bandDir = v; return this;} DirInt bandDir = DirInt.Fwd;
	public GftGrid SubLyts_get(String key) {
		for (int i = 0; i < subLyts.Count(); i++) {
			GftGrid grid = (GftGrid)subLyts.FetchAt(i);
			if (String_.Eq(key, grid.Key())) return grid;
		}
		return null;
	}
	public GftBand Bands_get(String key) {
		for (int i = 0; i < bands.Count(); i++) {
			GftBand band = (GftBand)bands.FetchAt(i);
			if (String_.Eq(key, band.Key())) return band;
		}
		return null;
	}
	public GftGrid Bands_add(GftBand band) {
		bands.Add(band.Clone(this, bands.Count()));
		return this;
	}
	public GftGrid Bands_add(int count, GftBand band) {
		for (int i = 0; i < count; i++) {
			GftBand copy = band.Clone(this, bands.Count() + i);
			bands.Add(copy);
		}
		return this;
	}
	@gplx.Internal protected void Bands_delAt(int i) {bands.DelAt(i);}
	@gplx.Internal protected boolean Bands_has(String key) {return Bands_indexOf(key) != ListAdp_.NotFound;}
	@gplx.Internal protected void Bands_del(String key) {
		int idx = Bands_indexOf(key);
		if (idx != ListAdp_.NotFound) bands.DelAt(idx);
	}
	int Bands_indexOf(String key) {
		int curIdx = ListAdp_.NotFound;
		for (int i = 0; i < bands.Count(); i++) {
			GftBand band = (GftBand)bands.FetchAt(i);
			if (String_.Eq(key, band.Key())) {
				curIdx = i;
				break;
			}
		}
		return curIdx;
	}
	public GftGrid Bands_set(int idx, GftBand orig) {return Bands_set(idx, idx, orig);}
	public GftGrid Bands_set(int bgn, int end, GftBand orig) {
		int len = end - bgn + 1;
		for (int i = 0; i < len; i++) {
			GftBand copy = orig.Clone(this, bgn + i);
			bands.Add(copy);
		}
		return this;
	}
	public void Exec(GftItem owner, GftItem... ary) {
		ExecLyts(owner, ary);
		ExecBands(owner, ary);
	}
	void ExecLyts(GftItem owner, GftItem[] ary) {
		int idx = 0;
		for (int i = 0; i < subLyts.Count(); i++) {
			GftGrid subGrid = (GftGrid)subLyts.FetchAt(i);
			GftItem[] subAry = new GftItem[subGrid.Bands_cellCount()];
			for (int j = 0; j < subAry.length; j++) {
				subAry[j] = ary[idx++];
			}
			subGrid.Exec(owner, subAry);
		}
	}
	void ExecBands(GftItem owner, GftItem[] ary) {
		if (bands.Count() == 0) return;
		int availY = owner.Gft_h();
		GftBand band = null;
		int bgn = bandDir.GetValByDir(bands.LastIndex(), 0);
		int end = bandDir.GetValByDir(-1, bands.Count());
		for (int i = bgn; i != end; i += bandDir.Val()) {
			band = (GftBand)bands.FetchAt(i);
			if (band.Len1().Key() == GftSizeCalc_abs.KEY) {
				GftSizeCalc_abs calc = (GftSizeCalc_abs)band.Len1();
				availY -= calc.Val();
			}
		}
		int bandIdx = 0;
		band = (GftBand)bands.FetchAt(bandIdx);
		band.Items().Clear();
		int y = bandDir.GetValByDir(owner.Gft_h(), 0);
		for (int itmIdx = 0; itmIdx < ary.length; itmIdx++) {
			GftItem itm = ary[itmIdx];
			if (band.Items().Count() >= band.Cells().Count()) {
				int h = band.Len1().Calc(this, band, owner, itm, availY);
				band.Calc(owner, y, h);
				y += h * bandDir.Val();
				if (bandIdx + 1 >= bands.Count()) throw Err_.new_("error retrieving band").Add("owner", owner.Key_of_GfuiElem()).Add("item", itm.Key_of_GfuiElem()).Add("bandIdx", bandIdx + 1).Add("count", bands.Count());
				band = (GftBand)bands.FetchAt(++bandIdx);
				band.Items().Clear();
			}
			band.Items_add(itm);
		}
		band.Calc(owner, y, band.Len1().Calc(this, band, owner, null, availY));
	}
	int Bands_cellCount() {
		int rv = 0;
		for (int i = 0; i < bands.Count(); i++) {
			GftBand band = (GftBand)bands.FetchAt(i);
			rv += band.Cells().Count();
		}
		return rv;
	}
	public static GftGrid new_() {return new GftGrid();} GftGrid() {}
	public static void LytExecRecur(GfuiElemBase owner) {
		if (owner.Lyt() != null) owner.Lyt_exec();
		for (int i = 0; i < owner.SubElems().Count(); i++) {
			GfuiElemBase sub = (GfuiElemBase)owner.SubElems().FetchAt(i);
			LytExecRecur(sub);
		}
	}
}
