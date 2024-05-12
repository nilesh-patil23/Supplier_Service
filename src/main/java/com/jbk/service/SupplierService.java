package com.jbk.service;

import javax.validation.Valid;

import com.jbk.model.SupplierModel;

public interface SupplierService {
	
	public boolean addSupplier(SupplierModel suppliermodel);
	
	public SupplierModel getSupplierById(long supplierId);
	
	public boolean updateSupplier(SupplierModel supplierModel);
	
    public Object deleteSupplierById(long supplierId);

}
