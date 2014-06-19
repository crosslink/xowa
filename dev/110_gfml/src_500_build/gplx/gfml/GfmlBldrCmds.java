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
package gplx.gfml; import gplx.*;
class GfmlBldrCmd_pendingTkns_add implements GfmlBldrCmd {
	public String Key() {return "gfml.pendingTkns_add";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {bldr.CurFrame().WaitingTkns().Add(tkn);}
	public static final GfmlBldrCmd_pendingTkns_add _ = new GfmlBldrCmd_pendingTkns_add(); GfmlBldrCmd_pendingTkns_add() {}
}
class GfmlBldrCmd_dataTkn_set implements GfmlBldrCmd {
	public String Key() {return "gfml.elm_data";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {bldr.CurNdeFrame().DatTkn_set(tkn);}
	public static final GfmlBldrCmd_dataTkn_set _ = new GfmlBldrCmd_dataTkn_set(); GfmlBldrCmd_dataTkn_set() {}
}
class GfmlBldrCmd_elemKey_set implements GfmlBldrCmd {
	public String Key() {return "gfml.elm_key";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {bldr.CurNdeFrame().KeyTkn_set(tkn);}
	public static final GfmlBldrCmd_elemKey_set _ = new GfmlBldrCmd_elemKey_set(); GfmlBldrCmd_elemKey_set() {}
}
class GfmlBldrCmd_ndeName_set implements GfmlBldrCmd {
	public String Key() {return "gfml.ndeName_set";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {bldr.CurNdeFrame().NdeHeader_set(tkn);}
	public static final GfmlBldrCmd_ndeName_set _ = new GfmlBldrCmd_ndeName_set(); GfmlBldrCmd_ndeName_set() {}
}
class GfmlBldrCmd_ndeBody_bgn implements GfmlBldrCmd {
	public String Key() {return "gfml.nodeBody_bgn";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {
		bldr.CurNdeFrame().NdeBody_bgn(tkn);
		bldr.CurNdeFrame().BgnPos_set(bldr.StreamPos() - String_.Len(tkn.Raw()));// stream has already advanced tkn.len so subtract
	}
	public static final GfmlBldrCmd_ndeBody_bgn _ = new GfmlBldrCmd_ndeBody_bgn(); GfmlBldrCmd_ndeBody_bgn() {}
}
class GfmlBldrCmd_ndeProp_bgn implements GfmlBldrCmd {
	public String Key() {return "cmd.gfml.ndeProp_bgn";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {bldr.CurNdeFrame().NdeProp_bgn(tkn);}
	public static final GfmlBldrCmd_ndeProp_bgn _ = new GfmlBldrCmd_ndeProp_bgn(); GfmlBldrCmd_ndeProp_bgn() {}
}
class GfmlBldrCmd_ndeHdr_bgn implements GfmlBldrCmd {
	public String Key() {return "cmd.gfml.ndeHdr_bgn";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {bldr.CurNdeFrame().NdeParen_bgn(tkn);}
	public static final GfmlBldrCmd_ndeHdr_bgn _ = new GfmlBldrCmd_ndeHdr_bgn(); GfmlBldrCmd_ndeHdr_bgn() {}
}
class GfmlBldrCmd_ndeHdr_end implements GfmlBldrCmd {
	public String Key() {return "cmd.gfml.ndeHdr_end";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {bldr.CurNdeFrame().NdeParen_end(tkn);}
	public static final GfmlBldrCmd_ndeHdr_end _ = new GfmlBldrCmd_ndeHdr_end(); GfmlBldrCmd_ndeHdr_end() {}
}
class GfmlBldrCmd_ndeInline implements GfmlBldrCmd {
	public String Key() {return "gfml.nodeInline_exec_end";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {bldr.CurNdeFrame().NdeInline(tkn);}
	public static final GfmlBldrCmd_ndeInline _ = new GfmlBldrCmd_ndeInline(); GfmlBldrCmd_ndeInline() {}
}
class GfmlBldrCmd_ndeDot implements GfmlBldrCmd {
	public String Key() {return "gfml.ndeDot";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {bldr.CurNdeFrame().NdeDot(tkn);}
	public static final GfmlBldrCmd_ndeDot _ = new GfmlBldrCmd_ndeDot(); GfmlBldrCmd_ndeDot() {}
}
class GfmlBldrCmd_atrSpr implements GfmlBldrCmd {
	public String Key() {return "gfml.nodeInline_atrSpr";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {bldr.CurNdeFrame().AtrSpr(tkn);}
        public static final GfmlBldrCmd_atrSpr _ = new GfmlBldrCmd_atrSpr(); GfmlBldrCmd_atrSpr() {}
}
class GfmlBldrCmd_frameBgn implements GfmlBldrCmd {
	public String Key() {return "gfml.frame_bgn";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {
		GfmlFrame newFrame = frame.MakeNew(lxr.SubLxr());
		bldr.Frames_push(newFrame);
		bldr.CurFrame().WaitingTkns().Add(tkn);
		bldr.CurNdeFrame().BgnPos_set(bldr.StreamPos() - String_.Len(tkn.Raw()));// stream has already advanced tkn.len so subtract
	}
	GfmlLxr lxr; GfmlFrame frame;
	public static GfmlBldrCmd_frameBgn new_(GfmlFrame frame, GfmlLxr lxr) {
		GfmlBldrCmd_frameBgn rv = new GfmlBldrCmd_frameBgn();
		rv.frame = frame; rv.lxr = lxr;
		return rv;
	}	GfmlBldrCmd_frameBgn() {}
}
class GfmlBldrCmd_frameEnd implements GfmlBldrCmd {
	public String Key() {return "gfml.frame_end";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {
		if (bldr.CurFrame().FrameType() == GfmlFrame_.Type_nde) {
			GfmlFrame_nde ndeFrm = (GfmlFrame_nde)bldr.CurFrame();
			ndeFrm.WaitingTkns_AddSym(tkn, ndeSymType);
		}
		else
			bldr.CurFrame().WaitingTkns().Add(tkn);
		if (bldr.CurNdeFrame().CurNdeStartType() == GfmlNdeStartType.Prop
			&& String_.Eq(tkn.Raw(), "}")) return;
		bldr.Frames_end();
	}
	int ndeSymType;
	public static GfmlBldrCmd_frameEnd data_() {return nde_(GfmlNdeSymType.Null);}
        public static GfmlBldrCmd_frameEnd nde_(int ndeSymType) {
		GfmlBldrCmd_frameEnd rv = new GfmlBldrCmd_frameEnd();
		rv.ndeSymType = ndeSymType;
		return rv;
	}	GfmlBldrCmd_frameEnd() {}
}
class GfmlBldrCmd_whitespace implements GfmlBldrCmd {
	public String Key() {return "gfml.whitespace_exec";}
	public void Exec(GfmlBldr bldr, GfmlTkn tkn) {
		if (bldr.CurNdeFrame().waitingTkns.Count() > 0) {
			GfmlObj t = (GfmlObj)bldr.CurNdeFrame().waitingTkns.FetchAt(bldr.CurNdeFrame().waitingTkns.Count() - 1);
			if (t.ObjType() == GfmlObj_.Type_nde)
				bldr.CurNdeFrame().IdxNdeBgn_set(bldr.CurNdeFrame().WaitingTkns().Count() + 1);
		}
		if (bldr.CurFrame().WaitingTkns().Count() == 0) {
			bldr.CurNde().SubObjs_Add(tkn);		// if curFrame begins with whitespace, add directly to node (whitespace should not belong to atr)
		}
		else
			bldr.CurFrame().WaitingTkns().Add(tkn);
	}
	public static final GfmlBldrCmd_whitespace _ = new GfmlBldrCmd_whitespace(); GfmlBldrCmd_whitespace() {}
}
