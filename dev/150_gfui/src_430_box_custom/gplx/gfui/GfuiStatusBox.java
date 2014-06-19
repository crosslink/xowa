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
public class GfuiStatusBox extends GfuiTextBox implements UsrMsgWkr { 	public GfuiStatusBox Active_(boolean v)			{active = v; return this;} private boolean active = true;
	public GfuiStatusBox VisibilityDuration_(int v) {timer.Interval_(v); visibilityDuration = v; return this;} int visibilityDuration;
	@Override public void Opened_cbk() {
		super.Opened_cbk();
		UsrDlg_._.Reg(UsrMsgWkr_.Type_Note, this);
	}
	public void ExecUsrMsg(int type, UsrMsg umsg) {
		if (	!active
			||	!this.OwnerElem().Visible() // WORKAROUND.WINFORMS: else application hangs on Invoke
			||	!this.Opened_done()
			) return;
		this.CreateControlIfNeeded();		// WORKAROUND.WINFORMS: else will sometimes throw: Cannot call Invoke or InvokeAsync on a control until the window handle has been created
		this.VisibilityDuration_(umsg.VisibilityDuration());		
		String text = String_.Replace(umsg.XtoStr(), Op_sys.Cur().Nl_str(), " "); // replace NewLine with " " since TextBox cannot show NewLine
		Invoke(GfoInvkAbleCmd.arg_(this, Invk_WriteText, text));
	}
	public void WriteText(String text) {
		if (!this.Visible()) {
			this.Visible_set(true);
			this.Zorder_front();
		}
		this.Text_(text);
		timer.Enabled_off().Enabled_on();	// Enabled_off().Enabled_on() resets timer; timer can be at second 1 of 3, and this will reset back to timer 0
		GfuiEnv_.DoEvents();				// WORKAROUND.WINFORMS: needed, else text will not refresh (ex: splash screen); NOTE!!: will cause other timers to fire! (ex: mediaPlaylistMgr)
	}
	@Override public void Focus() {
		this.Focus_able_(true);
		super.Focus();
		this.Focus_able_(false);
	}
	@Override public boolean DisposeCbk() {
		super.DisposeCbk();
		timer.Rls();
		UsrDlg_._.RegOff(UsrMsgWkr_.Type_Note, this);
		if (timerCmd != null) timerCmd.Rls();
		return true;
	}
	void HideWindow() {
		timer.Enabled_off();
		Text_("");
		this.Visible_set(false);
		this.Zorder_back();
	}
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_HideWindow))			HideWindow();
		else if	(ctx.Match(k, Invk_Text_empty))			{timer.Enabled_off(); Text_(GfuiTextBox_.NewLine + Text());}
		else if	(ctx.Match(k, Invk_WriteText))			WriteText(m.ReadStr("v"));
		else return super.Invk(ctx, ikey, k, m);
		return this;
	}	static final String Invk_HideWindow = "HideWindow", Invk_WriteText = "WriteText", Invk_Text_empty = "Text_empty";
	TimerAdp timer;
	@Override public void ctor_GfuiBox_base(KeyValHash ctorArgs) {
		super.ctor_GfuiBox_base(ctorArgs);
		this.Border_on_(false);
		this.Focus_able_(false);
		this.Visible_set(false);
		timer = TimerAdp.new_(this, Invk_HideWindow, 2000, false);
	}
	@Override public void ctor_kit_GfuiElemBase(Gfui_kit kit, String key, GxwElem underElem, KeyValHash ctorArgs) {
		super.ctor_kit_GfuiElemBase(kit, key, underElem, ctorArgs);
		this.Border_on_(false);
		this.Focus_able_(false);
		this.Visible_set(true);
		timerCmd = kit.New_cmd_sync(this);
		timer = TimerAdp.new_(timerCmd, GfuiInvkCmd_.Invk_sync, 2000, false);
	}	GfuiInvkCmd timerCmd;
	public void ctor_kit_GfuiElemBase_drd(Gfui_kit kit, String key, GxwElem underElem, KeyValHash ctorArgs) {
		super.ctor_kit_GfuiElemBase(kit, key, underElem, ctorArgs);
		this.Border_on_(false);
		this.Focus_able_(false);
		this.Visible_set(true);
		timerCmd = kit.New_cmd_sync(this);
//			timer = TimerAdp.new_(timerCmd, GfuiInvkCmd_.Invk_sync, 2000, false);
	}
}
class GfuiStatusBox_ {
	public static GfuiStatusBox new_(String key) {
		GfuiStatusBox rv = new GfuiStatusBox();
		rv.ctor_GfuiBox_base(GfuiElem_.init_focusAble_false_());
		rv.Key_of_GfuiElem_(key);
		return rv;
	}
	public static GfuiStatusBox kit_(Gfui_kit kit, String key, GxwElem underElem) {
		GfuiStatusBox rv = new GfuiStatusBox();
		rv.ctor_kit_GfuiElemBase(kit, key, underElem, GfuiElem_.init_focusAble_false_());
		return rv;
	}
}
