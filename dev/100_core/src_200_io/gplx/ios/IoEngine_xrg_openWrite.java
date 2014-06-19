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
package gplx.ios; import gplx.*;
public class IoEngine_xrg_openWrite {
	public Io_url Url() {return url;} public IoEngine_xrg_openWrite Url_(Io_url val) {url = val; return this;} Io_url url;
	public boolean ReadOnlyIgnored() {return readOnlyIgnored;} public IoEngine_xrg_openWrite ReadOnlyIgnored_() {return ReadOnlyIgnored_(true);} public IoEngine_xrg_openWrite ReadOnlyIgnored_(boolean v) {readOnlyIgnored = v; return this;} private boolean readOnlyIgnored = false;
	public boolean MissingIgnored() {return missingIgnored;} public IoEngine_xrg_openWrite MissingIgnored_() {return MissingIgnored_(true);} public IoEngine_xrg_openWrite MissingIgnored_(boolean v) {missingIgnored = v; return this;} private boolean missingIgnored = false;
	public byte Mode() {return mode;} public IoEngine_xrg_openWrite Mode_(byte v) {mode = v; return this;} private byte mode = IoStream_.Mode_wtr_create;
	public IoEngine_xrg_openWrite Mode_update_() {return Mode_(IoStream_.Mode_wtr_update);}
	public IoStream Exec() {return IoEnginePool._.Fetch(url.Info().EngineKey()).OpenStreamWrite(this);}
	public static IoEngine_xrg_openWrite new_(Io_url url) {
		IoEngine_xrg_openWrite rv = new IoEngine_xrg_openWrite();
		rv.url = url;
		return rv;
	}	IoEngine_xrg_openWrite() {}
}
