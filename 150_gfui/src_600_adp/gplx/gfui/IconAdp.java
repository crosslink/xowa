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
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.net.MalformedURLException;
import java.net.URL;
public class IconAdp {
	public Icon UnderIcon() {return icon;} final Icon icon;
	public Image XtoImage() {return ((ImageIcon)icon).getImage();}		
	public Io_url Url() {return url;} Io_url url = Io_url_.Null;
	IconAdp(Icon icon) {this.icon = icon;}
	public static IconAdp new_(Icon icon) {return new IconAdp(icon);}
	public static IconAdp file_(Io_url url) {
				Icon icon = new ImageIcon(url.Xto_api());
		IconAdp rv = new IconAdp(icon);
		rv.url = url;
		return rv;
			}
	public static void regy_loadDir_(Io_url imgDir) {GfoRegy._.RegDir(imgDir, "*.png", true, "_", ".");} 
	public static void regy_loadDir_shallow(Io_url imgDir) {GfoRegy._.RegDir(imgDir, "*.png", false, "_", ".");} 
	public static IconAdp regy_(String key) {
		GfoRegyItm itm = GfoRegy._.FetchOrNull(key);
		if (itm == null) {UsrDlg_._.Warn("missing icon; key={0}", key); return null;}
		if	(itm.ValType() != GfoRegyItm.ValType_Url) throw Err_.new_("regyItm should be of type url").Add("key", key);
		return IconAdp.file_(itm.Url());
	}
	public static IconAdp as_(Object obj) {return obj instanceof IconAdp ? (IconAdp)obj : null;}
}
