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
import gplx.lists.*;/*StackAdp*/
class GfmlTypeMgr {
	public GfmlTypRegy TypeRegy() {return typeRegy;} GfmlTypRegy typeRegy = GfmlTypRegy.new_();
	public GfmlFldPool FldPool() {return fldPool;} GfmlFldPool fldPool = GfmlFldPool.new_(GfmlType_.new_any_());
	public void NdeBgn(GfmlNde nde, String ownerTypeKey) {
		Resolve(nde, ownerTypeKey);
		stack.Push(fldPool);
		fldPool = GfmlFldPool.new_(nde.Type());
		ownerNdeStack.Push(ownerNde);
		ownerNde = nde;
	}	GfmlNde ownerNde;
	public void NdeEnd() {
		if (stack.Count() == 0) return;
		fldPool = (GfmlFldPool)stack.Pop();
		ownerNde = (GfmlNde)ownerNdeStack.Pop();
	}
	public void NdeResolve(GfmlNde nde, String ownerTypeKey) {
		Resolve(nde, ownerTypeKey);
		fldPool = GfmlFldPool.new_(nde.Type());
	}
	public void AtrExec(GfmlNde nde, GfmlAtr atr) {
		String atrKey = atr.Key();
		boolean isNull = String_.Eq(atrKey, GfmlTkn_.NullRaw);
		GfmlFld atrFld = isNull
			? fldPool.Keyed_PopNext()
			: fldPool.Keyed_PopByKey(atrKey);
		if (isNull) {
			atr.Key_set(atrFld.Name());
			nde.SubKeys().RegisterKey(atrFld.Name(), atr);
		}
	}
	void Resolve(GfmlNde nde, String ownerTypeKey) {
		GfmlType type = GfmlType_.Null;
		if (!String_.Eq(ownerTypeKey, GfmlType_.AnyKey)) {		// ownerType is specificType (!AnyType); lookup nde by ownerTypeKey + nde.Hnd
			String ndeHndOrKey = String_.Len_eq_0(nde.Hnd()) ? nde.Key() : nde.Hnd();
			if (String_.Len_eq_0(ndeHndOrKey)) ndeHndOrKey = nde.Hnd();
			String typeKey = GfmlType_.MakeKey(ownerTypeKey, ndeHndOrKey);
			type = typeRegy.FetchOrNull(typeKey, nde.DocPos());
		}
		if (type == GfmlType_.Null) {							// typeKey not found; try nde.Hnd only
			type = FetchByNde(nde);
		}
		if (!type.IsTypeAny())									// add defaults (skip AnyKey for perf)
			GfmlTypeCompiler.AddDefaultAtrs(nde, type, typeRegy);
		nde.Type_set(type);
	}
	GfmlType FetchByNde(GfmlNde nde) {
		GfmlType rv = GfmlType_.Null;
		String ndeKey = nde.Key(), ndeHnd = nde.Hnd();
		String typKey = nde.Type().Key(); GfmlFld fld;
		if (nde.KeyedSubObj()) {
			if (String_.Len_eq_0(ndeKey)) {
				fld = fldPool.Keyed_PopNext();
				typKey = (fld == GfmlFld.Null) ? GfmlType_.AnyKey : fld.TypeKey();
				if (ownerNde != null && !String_.Len_eq_0(fld.Name()))	// HACK: String_.Len_eq_0(fld.Name()) needed for DoesNotUsurpDatTknForName
					ownerNde.SubKeys().RegisterKey(fld.Name(), nde);
			}
			else {
				fldPool.Keyed_PopByKey(ndeKey);
				typKey = ndeKey;
			}
		}
		else {
			if (String_.Len_eq_0(ndeHnd)) {
				fld = fldPool.DefaultMember();
				typKey = (fld == GfmlFld.Null) ? GfmlType_.AnyKey : fld.TypeKey();
				if (String_.Len_eq_0(nde.Hnd()))		// type found, and curNde.Id is null; generate Id (DataRdr depends on this)
					nde.Hnd_set(fld.Name());
			}
			else
				typKey = ndeHnd;
		}
		if (typKey == null) throw Err_.new_("could not identity type for node").Add("ndeKey", ndeKey).Add("ndeHnd", ndeHnd).Add("typKey", nde.Type().Key());
		rv = typeRegy.FetchOrNull(typKey, nde.DocPos());
		return (rv == GfmlType_.Null)
			? GfmlType_.new_any_()									// unknown typeKey; name is not known (see EX:2) -> create new anyType
			: rv;
	}

	public void OverridePool(GfmlType type) {
		GfmlType owner = GfmlType_.new_any_();
		owner.SubFlds().Add(GfmlFld.new_(false, type.NdeName(), type.Key()));
		fldPool = GfmlFldPool.new_(owner);
	}
	StackAdp stack = StackAdp_.new_(), ownerNdeStack = StackAdp_.new_();
        public static GfmlTypeMgr new_() {return new GfmlTypeMgr();} GfmlTypeMgr() {}
}
class GfmlFldPool {
	public GfmlTkn Keyed_PopNextAsTkn() {return GfmlTkn_.val_(Keyed_PopNext().Name());} // helper method for GfmlFrame_nde
	public GfmlFld Keyed_PopNext() {
		if (keyedRegy.Count() == 0) return GfmlFld.Null;
		GfmlFld rv = (GfmlFld)keyedRegy.FetchAt(0);
		keyedRegy.Del(rv.Name());
		return rv;
	}
	public GfmlFld Keyed_PopByKey(String key) {
		GfmlFld rv = (GfmlFld)keyedRegy.Fetch(key); if (rv == null) return GfmlFld.Null;
		keyedRegy.Del(rv.Name());
		return rv;
	}
	@gplx.Internal protected int Keyd_Count() {return keyedRegy.Count();}
	@gplx.Internal protected GfmlFld Keyd_FetchAt(int i) {return (GfmlFld)keyedRegy.FetchAt(i);}
	public GfmlFld DefaultMember() {return defaultMember;} GfmlFld defaultMember = GfmlFld.Null;
	@gplx.Internal protected GfmlType Type() {return type;} GfmlType type = GfmlType_.Null;
	void InitByType(GfmlType type) {
		this.type = type;
		for (int i = 0; i < type.SubFlds().Count(); i++) {
			GfmlFld fld = (GfmlFld)type.SubFlds().FetchAt(i);
			if (fld.Name_isKey())
				keyedRegy.Add(fld.Name(), fld);
			else {
				defaultMember = fld;
			}
		}
	}
	OrderedHash keyedRegy = OrderedHash_.new_();
	public static GfmlFldPool new_(GfmlType type) {
		GfmlFldPool rv = new GfmlFldPool();
		rv.InitByType(type);
		return rv;
	}	GfmlFldPool() {}
}	
