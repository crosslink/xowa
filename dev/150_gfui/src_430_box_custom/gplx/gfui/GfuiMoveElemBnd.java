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
public class GfuiMoveElemBnd implements IptBnd, GfoInvkAble, InjectAble {
	public String Key() {return "gplx.gfui.moveWidget";}
	public ListAdp Ipts() {return args;} ListAdp args = ListAdp_.new_();
	public IptEventType EventTypes() {return IptEventType_.add_(IptEventType_.KeyDown, IptEventType_.MouseDown, IptEventType_.MouseMove, IptEventType_.MouseUp);}
	public void Exec(IptEventData iptData) {
		int val = iptData.EventType().Val();
		if		(val == IptEventType_.KeyDown.Val())		ExecKeyDown(iptData);
		else if (val == IptEventType_.MouseDown.Val())		ExecMouseDown(iptData);
		else if (val == IptEventType_.MouseUp.Val())		ExecMouseUp(iptData);
		else if (val == IptEventType_.MouseMove.Val())		ExecMouseMove(iptData);
	}
	public GfuiElem TargetElem() {return targetElem;} public void TargetElem_set(GfuiElem v) {this.targetElem = v;} GfuiElem targetElem;
	public static final String target_idk = "target";
	@gplx.Virtual public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, target_idk))				return targetElem;
		else if	(ctx.Match(k, "key"))					return key;
		else return GfoInvkAble_.Rv_unhandled;
	}

	public String Key_of_GfuiElem() {return keyIdf;} public void Key_of_GfuiElem_(String val) {keyIdf = val;} private String keyIdf = "moveElemBtnBnd";
	public void Inject(Object owner) {
		GfuiElem ownerElem = (GfuiElem)owner;
		ownerElem.IptBnds().Add(this);
		ownerElem.IptBnds().EventsToFwd_set(IptEventType_.None);	// NOTE: suppress bubbling else ctrl+right will cause mediaPlayer to jump forward
	}

	void ExecMouseDown(IptEventData msg) {
		moving = true;
		anchor = msg.MousePos();
	}
	void ExecMouseMove(IptEventData msg) {
		if (!moving) return;
		targetElem.Pos_(msg.MousePos().Op_subtract(anchor).Op_add(targetElem.Pos()));
	}
	void ExecMouseUp(IptEventData msg) {
		moving = false;
	}
	void ExecKeyDown(IptEventData msg) {
		PointAdp current = targetElem.Pos();
		PointAdp offset = PointAdp_.cast_(hash.Fetch(msg.EventArg()));
		targetElem.Pos_(current.Op_add(offset));
	}
	@gplx.Internal protected void Key_set(String key) {this.key = key;} private String key;
	public Object Srl(GfoMsg owner) {return IptBnd_.Srl(owner, this);}

	boolean moving = false;
	PointAdp anchor = PointAdp_.Zero; HashAdp hash = HashAdp_.new_();
	public static GfuiMoveElemBnd new_() {return new GfuiMoveElemBnd();}
	GfuiMoveElemBnd() {
		args.AddMany(IptMouseBtn_.Left, IptMouseMove.AnyDirection);
		IptBndArgsBldr.AddWithData(args, hash, IptKey_.Ctrl.Add(IptKey_.Up), PointAdp_.new_(0, -10));
		IptBndArgsBldr.AddWithData(args, hash, IptKey_.Ctrl.Add(IptKey_.Down), PointAdp_.new_(0, 10));
		IptBndArgsBldr.AddWithData(args, hash, IptKey_.Ctrl.Add(IptKey_.Left), PointAdp_.new_(-10, 0));
		IptBndArgsBldr.AddWithData(args, hash, IptKey_.Ctrl.Add(IptKey_.Right), PointAdp_.new_(10, 0));
	}
}
class IptBndArgsBldr {
	public static void AddWithData(ListAdp list, HashAdp hash, IptArg arg, Object data) {
		list.Add(arg);
		hash.Add(arg, data);			
	}
}
