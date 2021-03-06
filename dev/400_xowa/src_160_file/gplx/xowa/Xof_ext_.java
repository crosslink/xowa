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
public class Xof_ext_ {
	// NOTE: do not change ids; ids are saved to image databases;
	public static final int Id_unknown = 0
	, Id_png = 1, Id_jpg = 2, Id_jpeg = 3, Id_gif = 4, Id_tif = 5, Id_tiff = 6
	, Id_svg = 7, Id_djvu = 8, Id_pdf = 9
	, Id_mid = 10, Id_ogg = 11, Id_oga = 12, Id_ogv = 13, Id_webm = 14
	, Id_flac = 15, Id_bmp = 16, Id_xcf = 17;
	public static final int Id__max = 18;
	public static final byte[] 
	  Bry_png = Bry_.new_ascii_("png"), Bry_jpg = Bry_.new_ascii_("jpg"), Bry_jpeg = Bry_.new_ascii_("jpeg")
	, Bry_gif = Bry_.new_ascii_("gif"), Bry_tif = Bry_.new_ascii_("tif"), Bry_tiff = Bry_.new_ascii_("tiff")
	, Bry_svg = Bry_.new_ascii_("svg"), Bry_djvu = Bry_.new_ascii_("djvu"), Bry_pdf = Bry_.new_ascii_("pdf")
	, Bry_mid = Bry_.new_ascii_("mid"), Bry_ogg = Bry_.new_ascii_("ogg"), Bry_oga = Bry_.new_ascii_("oga")
	, Bry_ogv = Bry_.new_ascii_("ogv"), Bry_webm = Bry_.new_ascii_("webm")
	, Bry_flac = Bry_.new_ascii_("flac"), Bry_bmp = Bry_.new_ascii_("bmp"), Bry_xcf = Bry_.new_ascii_("xcf")
	;
	public static final byte[][] Bry__ary = new byte[][]
	{ Bry_.Empty, Bry_png, Bry_jpg, Bry_jpeg, Bry_gif, Bry_tif, Bry_tiff
	, Bry_svg, Bry_djvu, Bry_pdf, Bry_mid, Bry_ogg, Bry_oga, Bry_ogv, Bry_webm
	, Bry_flac, Bry_bmp, Bry_xcf
	};
	public static final byte[][] Mime_type__ary = new byte[][] 
	{ Bry_.new_ascii_("application/octet-stream")
	, Bry_.new_ascii_("image/png"), Bry_.new_ascii_("image/jpg"), Bry_.new_ascii_("image/jpeg")
	, Bry_.new_ascii_("image/gif"), Bry_.new_ascii_("image/tiff"), Bry_.new_ascii_("image/tiff")
	, Bry_.new_ascii_("image/svg+xml"), Bry_.new_ascii_("image/x.djvu"), Bry_.new_ascii_("application/pdf")
	, Bry_.new_ascii_("application/x-midi"), Bry_.new_ascii_("video/ogg"), Bry_.new_ascii_("audio/oga")
	, Bry_.new_ascii_("video/ogg"), Bry_.new_ascii_("video/webm")
	, Bry_.new_ascii_("audio/flac"), Bry_.new_ascii_("image/bmp"), Bry_.new_ascii_("image/xcf")
	};
	private static final HashAdp id_hash = id_hash_new_();
	private static HashAdp id_hash_new_() {
		HashAdp rv = HashAdp_.new_bry_();
		id_hash_new_(rv, Bry_png, Id_png);
		id_hash_new_(rv, Bry_jpg, Id_jpg);
		id_hash_new_(rv, Bry_jpeg, Id_jpeg);
		id_hash_new_(rv, Bry_gif, Id_gif);
		id_hash_new_(rv, Bry_tif, Id_tif);
		id_hash_new_(rv, Bry_tiff, Id_tiff);
		id_hash_new_(rv, Bry_svg, Id_svg);
		id_hash_new_(rv, Bry_djvu, Id_djvu);
		id_hash_new_(rv, Bry_pdf, Id_pdf);
		id_hash_new_(rv, Bry_mid, Id_mid);
		id_hash_new_(rv, Bry_ogg, Id_ogg);
		id_hash_new_(rv, Bry_oga, Id_oga);
		id_hash_new_(rv, Bry_ogv, Id_ogv);
		id_hash_new_(rv, Bry_webm, Id_webm);
		id_hash_new_(rv, Bry_flac, Id_flac);
		id_hash_new_(rv, Bry_bmp, Id_bmp);
		id_hash_new_(rv, Bry_xcf, Id_xcf);
		return rv;
	}
	private static void id_hash_new_(HashAdp hash, byte[] key, int val) {hash.Add(key, Int_obj_val.new_(val));}

	private static final Hash_adp_bry ext_hash = Hash_adp_bry.ci_().Add_bry_bry(Bry_png).Add_bry_bry(Bry_jpg).Add_bry_bry(Bry_jpeg).Add_bry_bry(Bry_gif).Add_bry_bry(Bry_tif).Add_bry_bry(Bry_tiff).Add_bry_bry(Bry_svg).Add_bry_bry(Bry_djvu).Add_bry_bry(Bry_pdf).Add_bry_bry(Bry_mid).Add_bry_bry(Bry_ogg).Add_bry_bry(Bry_oga).Add_bry_bry(Bry_ogv).Add_bry_bry(Bry_webm);
	private static final Xof_ext[] Ary = new Xof_ext[Id__max];

	public static byte[] Get_ext_by_id_(int id) {
		if (id < 0 || id >= Id__max) throw Err_.new_fmt_("index out of bounds; {id}", id);
		return Bry__ary[id];
	}
	public static int Get_id_by_ext_(byte[] ext_bry) {
		Object o = id_hash.Fetch(ext_bry);
		return o == null ? Id_unknown : ((Int_obj_val)o).Val();
	}
	public static Xof_ext new_by_ttl_(byte[] ttl) {
		int ttl_len = ttl.length;
		int dot_pos = Bry_finder.Find_bwd(ttl, Byte_ascii.Dot);
		byte[] ext = (dot_pos == Bry_.NotFound || dot_pos == ttl_len) ? Bry_.Empty : Bry_.Xto_str_lower(ttl, dot_pos + 1, ttl_len); // +1 to bgn after .
		return new_(Get_id_by_ext_(ext), ext);
	}
	public static Xof_ext new_by_ext_(byte[] ext)	{return new_(Get_id_by_ext_(ext), ext);}
	public static Xof_ext new_by_id_(int id)		{return new_(id, Get_ext_by_id_(id));}
	public static Xof_ext new_(int id, byte[] ext) {
		Xof_ext rv = Ary[id];
		if (rv == null) {
			rv = new Xof_ext(id, ext);
			Ary[id] = rv;
		}
		return rv;
	}
	public static byte[] Lower_ext(byte[] ttl) {
		int dot_pos = Bry_finder.Find_bwd(ttl, Byte_ascii.Dot);
		int ttl_len = ttl.length;
		if (dot_pos == Bry_.NotFound || dot_pos == ttl_len - 1) return ttl;
		Object o = ext_hash.Get_by_mid(ttl, dot_pos + 1, ttl_len);
		if (o == null) return ttl;
		byte[] ext = (byte[])o;
		boolean match = Bry_.Match(ttl, dot_pos, ttl_len, ext);
		if (match) return ttl;
		int ext_len = ext.length;
		for (int i = 0; i < ext_len; i++)
			ttl[i + dot_pos + 1] = ext[i];
		return ttl;
	}
	public static boolean Orig_file_is_img(int v) {	// identifies if orig_file can be used for <img src>; EX: png is valid, but svg, ogv, pdf is not
		switch (v) {
			case Id_png: case Id_jpg: case Id_jpeg:
			case Id_gif: case Id_tif: case Id_tiff:				return true;
			default:											return false;
		}
	}
	public static boolean Id_supports_page(int v) {	// identifies if tid supports page in lnki; EX: [[File:A.pdf|page=1]]; REF: https://en.wikipedia.org/wiki/Wikipedia:Picture_tutorial; DATE:2014-01-18
		switch (v) {
			case Id_pdf: case Id_djvu:							return true;
			default:											return false;
		}
	}
	public static boolean Id_supports_thumbtime(int v) {	// identifies if tid supports thumbtime in lnki; EX: [[File:A.ogv|thumbtime=1]]; 
		switch (v) {
			case Id_ogg: case Id_ogv: case Id_webm:				return true;
			default:											return false;
		}
	}
	public static boolean Id_is_image(int id) {
		switch (id) {
			case Xof_ext_.Id_png: case Xof_ext_.Id_jpg: case Xof_ext_.Id_jpeg:
			case Xof_ext_.Id_gif: case Xof_ext_.Id_tif: case Xof_ext_.Id_tiff:
			case Xof_ext_.Id_svg:
			case Xof_ext_.Id_bmp: case Xof_ext_.Id_xcf:
				return true;
			default:
				return false;
		}
	}
	public static boolean Id_is_thumbable_img(int id) {
		switch (id) {
			case Xof_ext_.Id_png: case Xof_ext_.Id_jpg: case Xof_ext_.Id_jpeg:
			case Xof_ext_.Id_gif: case Xof_ext_.Id_tif: case Xof_ext_.Id_tiff:
			case Xof_ext_.Id_svg: case Xof_ext_.Id_djvu: case Xof_ext_.Id_pdf:
			case Xof_ext_.Id_bmp: case Xof_ext_.Id_xcf:
				return true;
			default:
				return false;
		}
	}
	public static boolean Id_is_audio(int id) {
		switch (id) {
			case Xof_ext_.Id_mid: case Xof_ext_.Id_oga: case Xof_ext_.Id_flac: case Xof_ext_.Id_ogg: return true;
			default: return false;
		}
	}
	public static boolean Id_is_video(int id) {return id == Xof_ext_.Id_ogv || id == Xof_ext_.Id_ogg || id == Xof_ext_.Id_webm;}	// NOTE: ogg can be vid; EX.WP: Comet; Encke_tail_rip_off.ogg
	public static boolean Id_is_video_strict(int id) {return id == Xof_ext_.Id_ogv || id == Xof_ext_.Id_webm;}	// NOTE: ogg can be aud / vid; EX.WP: Comet; Encke_tail_rip_off.ogg
	public static boolean Id_is_audio_strict(int id) {	// same as above, but deliberately exclude ambiguous ogg
		switch (id) {
			case Xof_ext_.Id_mid: case Xof_ext_.Id_oga: case Xof_ext_.Id_flac: return true;
			default: return false;
		}
	}
	public static boolean Id_is_media(int id) {return Id_is_audio(id) || Id_is_video(id);}
	public static boolean Id_needs_convert(int id) {
		switch (id) {
			case Xof_ext_.Id_svg: case Xof_ext_.Id_djvu: case Xof_ext_.Id_pdf: return true;
			default: return false;
		}
	}
	public static int Id_view(int id) {
		switch (id) {
			case Xof_ext_.Id_svg: case Xof_ext_.Id_bmp: case Xof_ext_.Id_xcf:								return Xof_ext_.Id_png;
			case Xof_ext_.Id_tif: case Xof_ext_.Id_tiff: case Xof_ext_.Id_djvu: case Xof_ext_.Id_pdf:
			case Xof_ext_.Id_ogg: case Xof_ext_.Id_ogv: case Xof_ext_.Id_webm:								return Xof_ext_.Id_jpg;
			default:																						return id;
		}
	}
}
