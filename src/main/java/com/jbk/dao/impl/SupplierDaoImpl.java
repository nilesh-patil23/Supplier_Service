package com.jbk.dao.impl;

import javax.persistence.RollbackException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jbk.dao.SupplierDao;
import com.jbk.entity.SupplierEntity;
import com.jbk.exceptions.ResourceAlreadyExistsException;
import com.jbk.exceptions.SomethingWentWrongException;

@Repository
public class SupplierDaoImpl implements SupplierDao {

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public boolean addSupplier(SupplierEntity supplierEntity) {

		boolean isAdded = false;
		Session session = null;

		try {
			Transaction transaction;
			session = sessionFactory.openSession();

			// check its exists or not

			SupplierEntity dbEntity = getSupplierById(supplierEntity.getSupplierId());

			if (dbEntity == null) {
				session.save(supplierEntity);
				transaction = session.beginTransaction();
				transaction.commit();
				isAdded = true;
			} else {
				throw new ResourceAlreadyExistsException(
						"Supplier Already Exits with Id : " + supplierEntity.getSupplierId());

			}
		} catch (RollbackException e) {
			e.printStackTrace();
			throw new SomethingWentWrongException("Something went wrong in during add supplier,check unique field");
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return isAdded;
	}

	@Override
	public SupplierEntity getSupplierById(long supplierId) {

		SupplierEntity supplierEntity = null;
		try {
			Session session = sessionFactory.openSession();

			supplierEntity = session.get(SupplierEntity.class, supplierId);

		} catch (HibernateException e) {
			throw new SomethingWentWrongException("Connection Failure !!");

		}
		return supplierEntity;
	}

	@Override
	public boolean updateSupplierById(SupplierEntity supplierEntity) {
		boolean isUpdated = false;
		try {
			Session session = sessionFactory.openSession();

			SupplierEntity dbSupplier = getSupplierById(supplierEntity.getSupplierId());

			if (dbSupplier != null) {
				session.update(supplierEntity);
				session.beginTransaction().commit();
				isUpdated = true;

			} else {
				isUpdated = false;
				// throw new ResourceNotExistsException("Supplier Not Exists with ID : " +
				// supplierEntity.getSupplierId());
			}

		}
//		catch (ResourceNotExistsException e) {
//			throw new ResourceNotExistsException("Supplier Not Exists with ID : " + supplierEntity.getSupplierId());
//		}
		catch (Exception e) {
			throw new SomethingWentWrongException("Something Went Wrong During Update Supplier");
		}
		return isUpdated;
	}

	@Override
	public boolean deleteSupplierById(long supplierId) {
		boolean isDeleted = false;
		try {
			Session session = sessionFactory.openSession();

			SupplierEntity dbSupplier = getSupplierById(supplierId) ;

			if (dbSupplier != null) {
				session.delete(dbSupplier);
				session.beginTransaction().commit();
				isDeleted = true;

			} else {
				isDeleted = false;

			}

		}
		catch (Exception e) {
			throw new SomethingWentWrongException("Something Went Wrong During Update Supplier");
		}
		return isDeleted;
	}

}
