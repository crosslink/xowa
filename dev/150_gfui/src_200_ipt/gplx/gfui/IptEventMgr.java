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
class IptEventMgr implements GfoInvkAble {
	@gplx.Internal protected static void ExecKeyDown(GfuiElem sender, IptEvtDataKey keyState) {
		keyHandled = false; keyStateCur = keyState; // cache for simultaneous ipt events (ex: key.ctrl + mouse.left)
		IptEventData iptData = IptEventData.new_(sender, IptEventType_.KeyDown, keyState.Key(), keyState, mouseStateCur);
//			if (keyState.Key().Eq(IptKey_.add_(IptKey_.F1))) {
//				Tfds.Write(keyState.Key(), keyState.Key().Val());
//			}
		sender.IptBnds().Process(iptData); 
		SendData(iptData);
		keyHandled = keyState.Handled(); // WORKAROUND (WinForms): cache keyHandled b/c KeyDown.Handled=true does not make KeyPress.Handled=true;
	}
	@gplx.Internal protected static void ExecKeyPress(GfuiElem sender, IptEvtDataKeyHeld keyPressState) {
//            Tfds.Write(keyPressState.KeyChar());
		if (keyHandled) {keyPressState.Handled_set(true); return;}
		IptEventData iptData = IptEventData.new_(sender, IptEventType_.KeyPress, IptKeyStrMgr._.FetchByKeyPress((int)(byte)keyPressState.KeyChar()), keyStateCur, keyPressState, mouseStateCur);
		sender.IptBnds().Process(iptData);
		SendData(iptData);
	}
	@gplx.Internal protected static void ExecKeyUp(GfuiElem sender, IptEvtDataKey keyState) {
		keyStateCur = IptEvtDataKey.Null;			// keyStateCur no longer needed; set to Null
		if (keyHandled) {keyState.Handled_set(true); return;}
		IptEventData iptData = IptEventData.new_(sender, IptEventType_.KeyUp, keyState.Key(), keyState, mouseStateCur);
		sender.IptBnds().Process(iptData);
		SendData(iptData);
	}
	@gplx.Internal protected static void ExecMouseDown(GfuiElem sender, IptEvtDataMouse mouseState) {
		mouseStateCur = mouseState;				// cache for simultaneous ipt events (ex: key.ctrl + mouse.left)
		if (sender.IptBnds().Has(IptEventType_.MousePress)) {
			if (mousePressTimer == null) mousePressTimer = TimerAdp.new_(EventSink2, Tmr_cmd, 100, false);
			senderCur = sender; mousePressTimer.Enabled_on();
		}
		IptEventData iptData = IptEventData.new_(sender, IptEventType_.MouseDown, mouseState.Button(), keyStateCur, mouseState);
		sender.IptBnds().Process(iptData);
		SendData(iptData);
	}
	@gplx.Internal protected static void ExecMouseMove(GfuiElem sender, IptEvtDataMouse mouseState) {
		IptEventData iptData = IptEventData.new_(sender, IptEventType_.MouseMove, IptMouseMove.AnyDirection, keyStateCur, mouseState);
		sender.IptBnds().Process(iptData);
		// SendData(iptData); // TOMBSTONE: do not send mouseMove events for PERF and DESIGN reasons
	}
	@gplx.Internal protected static void ExecMouseUp(GfuiElem sender, IptEvtDataMouse mouseState) {
		mouseStateCur = IptEvtDataMouse.Null;		// mouseStateCur no longer needed; set to Null
		if (mousePressTimer != null)
		mousePressTimer.Enabled_off();
		IptEventData iptData = IptEventData.new_(sender, IptEventType_.MouseUp, mouseState.Button(), keyStateCur, mouseState);
		sender.IptBnds().Process(iptData);
		SendData(iptData);
	}
	@gplx.Internal protected static void ExecMouseWheel(GfuiElem sender, IptEvtDataMouse mouseState) {
		IptEventData iptData = IptEventData.new_(sender, IptEventType_.MouseWheel, mouseState.Wheel(), keyStateCur, mouseState);
		sender.IptBnds().Process(iptData);
		SendData(iptData);
	}
	@gplx.Internal protected static void ExecMousePress(GfuiElem sender, IptEvtDataMouse mouseState) {
		IptEventData iptData = IptEventData.new_(sender, IptEventType_.MousePress, mouseState.Button(), keyStateCur, mouseState);
		sender.IptBnds().Process(iptData);
		SendData(iptData);
	}
	static void MousePressTick() {
		IptEventMgr.ExecMousePress(senderCur, mouseStateCur);
	}
	static void SendData(IptEventData iptData) {
		if (StopFwd(iptData)) return;
		GfsCtx ctx = GfsCtx.new_();
		GfoMsg m = GfoMsg_.new_cast_(GfuiElemKeys.IptRcvd_evt).Add("iptData", iptData);
		GfoEvMgr_.PubMsg(iptData.Sender(), ctx, GfuiElemKeys.IptRcvd_evt, m);
	}
	public static boolean StopFwd(IptEventData iptData) {	// check if (a) control bubbles event; (b) iptData.Handled
		return !IptEventType_.Has(iptData.Sender().IptBnds().EventsToFwd(), iptData.EventType())
			|| iptData.Handled();
	}
	static boolean keyHandled = false; static IptEvtDataKey keyStateCur = IptEvtDataKey.Null; static IptEvtDataMouse mouseStateCur = IptEvtDataMouse.Null;
	static TimerAdp mousePressTimer; static GfuiElem senderCur;
	public static final IptEventMgr EventSink2 = new IptEventMgr(); IptEventMgr() {}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Tmr_cmd))		MousePressTick();
		else return GfoInvkAble_.Rv_unhandled;
		return this;
	}	public static final String Tmr_cmd = "Tmr";
}
