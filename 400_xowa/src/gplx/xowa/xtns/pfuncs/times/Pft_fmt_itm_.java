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
package gplx.xowa.xtns.pfuncs.times; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.pfuncs.*;
public class Pft_fmt_itm_ {
	public static final int
	  Tid_seg_int				=  1
	, Tid_hour_base12			=  2
	, Tid_dow_base0				=  3
	, Tid_seg_str				=  4
	, Tid_year_isLeap			=  5
	, Tid_timestamp_unix		=  6
	, Tid_raw_ary				=  7
	, Tid_raw_byt				=  8
	, Tid_dayOfYear				=  9
	, Tid_daysInMonth			= 10
	, Tid_AmPm					= 11
	, Tid_roman					= 12
	, Tid_iso_fmt				= 13
	, Tid_rfc_5322				= 14
	, Tid_raw					= 15
	, Tid_timezone_offset		= 16
	, Tid_thai					= 17
	, Tid_minguo				= 18
	;
	public static final Pft_fmt_itm 
	  Year_len4					= new Pft_fmt_itm_seg_int(DateAdp_.SegIdx_year			, 4, Bool_.Y)
	, Year_len2					= new Pft_fmt_itm_seg_int(DateAdp_.SegIdx_year			, 2, Bool_.Y)
	, Month_int_len2			= new Pft_fmt_itm_seg_int(DateAdp_.SegIdx_month			, 2, Bool_.Y)
	, Month_int					= new Pft_fmt_itm_seg_int(DateAdp_.SegIdx_month			, 2, Bool_.N)
	, Day_int_len2				= new Pft_fmt_itm_seg_int(DateAdp_.SegIdx_day			, 2, Bool_.Y)
	, Day_int					= new Pft_fmt_itm_seg_int(DateAdp_.SegIdx_day			, 2, Bool_.N)
	, Hour_base24_len2			= new Pft_fmt_itm_seg_int(DateAdp_.SegIdx_hour			, 2, Bool_.Y)
	, Hour_base24				= new Pft_fmt_itm_seg_int(DateAdp_.SegIdx_hour			, 2, Bool_.N)
	, Hour_base12_len2			= new Pft_fmt_itm_hour_base12(Bool_.Y)
	, Hour_base12				= new Pft_fmt_itm_hour_base12(Bool_.N)
	, Minute_int_len2			= new Pft_fmt_itm_seg_int(DateAdp_.SegIdx_minute		, 2, Bool_.Y)
	, Second_int_len2			= new Pft_fmt_itm_seg_int(DateAdp_.SegIdx_second		, 2, Bool_.Y)
	, Dow_base1_int				= new Pft_fmt_itm_seg_int(DateAdp_.SegIdx_dayOfWeek		, 1, Bool_.Y)
	, Dow_base0_int				= new Pft_fmt_itm_dow_base0()
	, WeekOfYear_int			= new Pft_fmt_itm_seg_int(DateAdp_.SegIdx_weekOfYear	, 2, Bool_.N)
	, WeekOfYear_int_len2		= new Pft_fmt_itm_seg_int(DateAdp_.SegIdx_weekOfYear	, 2, Bool_.Y)
	, Month_abrv				= new Pft_fmt_itm_seg_str(DateAdp_.SegIdx_month, Xol_msg_itm_.Id_dte_month_abrv_jan - Int_.Base1)		// Jan
	, Month_name				= new Pft_fmt_itm_seg_str(DateAdp_.SegIdx_month, Xol_msg_itm_.Id_dte_month_name_january - Int_.Base1)	// January
	, Month_gen					= new Pft_fmt_itm_seg_str(DateAdp_.SegIdx_month, Xol_msg_itm_.Id_dte_month_gen_january - Int_.Base1)	// January
	, Dow_abrv					= new Pft_fmt_itm_seg_str(DateAdp_.SegIdx_dayOfWeek, Xol_msg_itm_.Id_dte_dow_abrv_sun)					// Sun
	, Dow_name					= new Pft_fmt_itm_seg_str(DateAdp_.SegIdx_dayOfWeek, Xol_msg_itm_.Id_dte_dow_name_sunday)				// Sunday
	, Year_isLeap				= new Pft_fmt_itm_year_isLeap()
	, Timestamp_unix			= new Pft_fmt_itm_timestamp_unix()
	, Byte_space				= new Pft_fmt_itm_raw_byt(Byte_ascii.Space)
	, Byte_comma				= new Pft_fmt_itm_raw_byt(Byte_ascii.Comma)
	, Byte_dash					= new Pft_fmt_itm_raw_byt(Byte_ascii.Dash)
	, DayOfYear_int				= new Pft_fmt_itm_dayOfYear()
	, DaysInMonth_int			= new Pft_fmt_itm_daysInMonth()
	, AmPm_lower				= new Pft_fmt_itm_AmPm(true)
	, AmPm_upper				= new Pft_fmt_itm_AmPm(false)
	, Roman						= new Pft_fmt_itm_roman()
	, Thai						= new Pft_fmt_itm_thai()
	, Minguo					= new Pft_fmt_itm_minguo()
	, Raw						= new Pft_fmt_itm_raw()
	, Iso_fmt					= new Pft_fmt_itm_iso_fmt()
	, Rfc_5322					= new Pft_fmt_itm_rfc_5322()
	, Timezone_offset			= new Pft_fmt_itm_timezone_offset()
	;
	public static final ByteTrieMgr_fast Regy = ByteTrieMgr_fast.cs_()
	.Add(Byte_ascii.Ltr_Y		, Pft_fmt_itm_.Year_len4)				// 2012
	.Add(Byte_ascii.Ltr_y		, Pft_fmt_itm_.Year_len2)				// 12
	.Add(Byte_ascii.Ltr_L		, Pft_fmt_itm_.Year_isLeap)				// 1,0
	.Add(Byte_ascii.Ltr_o		, Pft_fmt_itm_.Year_len4)				// 2012: ISO-8601; don't know why it's different vs Ltr_Y
	.Add(Byte_ascii.Ltr_n		, Pft_fmt_itm_.Month_int)				// 1
	.Add(Byte_ascii.Ltr_m		, Pft_fmt_itm_.Month_int_len2)			// 01
	.Add(Byte_ascii.Ltr_M		, Pft_fmt_itm_.Month_abrv)				// Jan
	.Add(Byte_ascii.Ltr_F		, Pft_fmt_itm_.Month_name)				// January
	.Add("xg"					, Pft_fmt_itm_.Month_gen)				// January
	.Add(Byte_ascii.Ltr_W		, Pft_fmt_itm_.WeekOfYear_int_len2)		// 01
	.Add(Byte_ascii.Ltr_j		, Pft_fmt_itm_.Day_int)					// 1
	.Add(Byte_ascii.Ltr_d		, Pft_fmt_itm_.Day_int_len2)			// 01
	.Add(Byte_ascii.Ltr_z		, Pft_fmt_itm_.DayOfYear_int)			// 0
	.Add(Byte_ascii.Ltr_D		, Pft_fmt_itm_.Dow_abrv)				// Sun
	.Add(Byte_ascii.Ltr_l		, Pft_fmt_itm_.Dow_name)				// Sunday
	.Add(Byte_ascii.Ltr_N		, Pft_fmt_itm_.Dow_base0_int)			// 1; Sunday=7
	.Add(Byte_ascii.Ltr_w		, Pft_fmt_itm_.Dow_base1_int)			// 1; Sunday=0
	.Add(Byte_ascii.Ltr_a		, Pft_fmt_itm_.AmPm_lower)				// am/pm
	.Add(Byte_ascii.Ltr_A		, Pft_fmt_itm_.AmPm_upper)				// AM/PM
	.Add(Byte_ascii.Ltr_g		, Pft_fmt_itm_.Hour_base12)				// 1;  Base12
	.Add(Byte_ascii.Ltr_h		, Pft_fmt_itm_.Hour_base12_len2)		// 01; Base12; pad2
	.Add(Byte_ascii.Ltr_G		, Pft_fmt_itm_.Hour_base24)				// 13; Base24;
	.Add(Byte_ascii.Ltr_H		, Pft_fmt_itm_.Hour_base24_len2)		// 13; Base24; pad2
	.Add(Byte_ascii.Ltr_i		, Pft_fmt_itm_.Minute_int_len2)			// 04
	.Add(Byte_ascii.Ltr_s		, Pft_fmt_itm_.Second_int_len2)			// 05
	.Add(Byte_ascii.Ltr_t		, Pft_fmt_itm_.DaysInMonth_int)			// 31
	.Add(Byte_ascii.Ltr_U		, Pft_fmt_itm_.Timestamp_unix)			// 1343865600
	.Add(Byte_ascii.Ltr_Z		, Pft_fmt_itm_.Timezone_offset)			// timezone offset in seconds
	.Add(Byte_ascii.Ltr_c		, Pft_fmt_itm_.Iso_fmt)					// 2012-01-02T03:04:05+00:00
	.Add(Byte_ascii.Ltr_r		, Pft_fmt_itm_.Rfc_5322)				// Mon 02 Jan 2012 08:04:05 +0000
	.Add("xr"					, Pft_fmt_itm_.Roman)					// MCXI
	.Add("xkY"					, Pft_fmt_itm_.Thai)					// Year +=  543
	.Add("xoY"					, Pft_fmt_itm_.Minguo)					// Year -= 1911
	.Add("xn"					, Pft_fmt_itm_.Raw)						// NOTE: really does nothing; REF.MW: Language.php|sprintfdate does $s .= $num; DATE:2013-12-31
	.Add("xN"					, Pft_fmt_itm_.Raw)
	// TODO: foreign; space; "
	;
	public static Pft_fmt_itm[] Parse(Xop_ctx ctx, byte[] fmt) {
		ByteTrieMgr_fast trie = Pft_fmt_itm_.Regy;
		int i = 0, fmt_len = fmt.length;
		fmt_itms.Clear(); int raw_bgn = String_.Pos_neg1; byte raw_byt = Byte_.Zero;
		while (i < fmt_len) {
			byte b = fmt[i];
			Object o = trie.Match(b, fmt, i, fmt_len);
			if (o != null) {
				if (raw_bgn != String_.Pos_neg1) {fmt_itms.Add(i - raw_bgn == 1 ? new Pft_fmt_itm_raw_byt(raw_byt) : (Pft_fmt_itm)new Pft_fmt_itm_raw_ary(fmt, raw_bgn, i)); raw_bgn = String_.Pos_neg1;}
				fmt_itms.Add((Pft_fmt_itm)o);
				i = trie.Match_pos();
			}
			else {
				switch (b) {
					case Byte_ascii.Backslash:
						if (raw_bgn != String_.Pos_neg1) {fmt_itms.Add(i - raw_bgn == 1 ? new Pft_fmt_itm_raw_byt(raw_byt) : (Pft_fmt_itm)new Pft_fmt_itm_raw_ary(fmt, raw_bgn, i)); raw_bgn = String_.Pos_neg1;}
						++i; // peek next char
						if (i == fmt_len)	// trailing backslash; add one; EX: "b\" -> "b\" not "b"
							fmt_itms.Add(new Pft_fmt_itm_raw_byt(Byte_ascii.Backslash));
						else
							fmt_itms.Add(new Pft_fmt_itm_raw_byt(fmt[i]));
						++i;
						break;
					case Byte_ascii.Quote:
						if (raw_bgn != String_.Pos_neg1) {fmt_itms.Add(i - raw_bgn == 1 ? new Pft_fmt_itm_raw_byt(raw_byt) : (Pft_fmt_itm)new Pft_fmt_itm_raw_ary(fmt, raw_bgn, i)); raw_bgn = String_.Pos_neg1;}
						++i; // skip quote_bgn
						raw_bgn = i;
						while (i < fmt_len) {
							b = fmt[i];
							if (b == Byte_ascii.Quote) {
								break;
							}
							else
								++i;
						}
						fmt_itms.Add(i - raw_bgn == 0 ? new Pft_fmt_itm_raw_byt(Byte_ascii.Quote) : (Pft_fmt_itm)new Pft_fmt_itm_raw_ary(fmt, raw_bgn, i));
						raw_bgn = String_.Pos_neg1;
						++i; // skip quote_end
						break;
					default:
						if (raw_bgn == String_.Pos_neg1) {raw_bgn = i; raw_byt = b;}
						i += gplx.intl.Utf8_.Len_of_char_by_1st_byte(b);
						break;
				}
			}
		}
		if (raw_bgn != String_.Pos_neg1) {fmt_itms.Add(fmt_len - raw_bgn == 1 ? new Pft_fmt_itm_raw_byt(fmt[fmt_len - 1]) : (Pft_fmt_itm)new Pft_fmt_itm_raw_ary(fmt, raw_bgn, fmt_len)); raw_bgn = String_.Pos_neg1;}
		return (Pft_fmt_itm[])fmt_itms.XtoAry(Pft_fmt_itm.class);
	}	private static ListAdp fmt_itms = ListAdp_.new_();
}
