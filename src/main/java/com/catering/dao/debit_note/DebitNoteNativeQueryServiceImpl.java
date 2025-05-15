package com.catering.dao.debit_note;

import java.util.List;

import org.springframework.stereotype.Service;

import com.catering.dto.tenant.request.CommonCalculationFieldDto;
import com.catering.dto.tenant.request.DebitNotePurchaseBillDropDownDto;
import com.catering.dto.tenant.request.DebitNoteRawMaterialDropDownDto;
import com.catering.dto.tenant.request.PurchaseBillOrderRawMaterialDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link DebitNoteNativeQueryService} interface.
 * Provides concrete methods to handle native queries for managing Debit Note-related data.
 * 
 * This class uses the {@link DebitNoteNativeQueryDao} to interact with the database
 * and fetch the necessary data.
 * 
 * Annotations:
 * - {@code @RequiredArgsConstructor}: Automatically generates a constructor for all final fields.
 * - {@code @FieldDefaults}: Sets the access level and makes fields final by default.
 * - {@code @Service}: Marks this class as a Spring service component.
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class DebitNoteNativeQueryServiceImpl implements DebitNoteNativeQueryService {

	/**
	 * DAO for executing native queries related to Debit Notes.
	 */
	DebitNoteNativeQueryDao debitNoteNativeQueryDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DebitNotePurchaseBillDropDownDto> getDebitNotePurchaseBillDropDown(Long id) {
		return debitNoteNativeQueryDao.getDebitNotePurchaseBillDropDown(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PurchaseBillOrderRawMaterialDto> getDebitNotePurchaseBillRawMaterial(Long purchaseBillId) {
		return debitNoteNativeQueryDao.getDebitNotePurchaseBillRawMaterial(purchaseBillId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommonCalculationFieldDto getDebitNoteListCalculation(Long id) {
		return debitNoteNativeQueryDao.getDebitNoteListCalculation(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DebitNoteRawMaterialDropDownDto> getDebitNoteRawMaterial() {
		return debitNoteNativeQueryDao.getDebitNoteRawMaterial();
	}

}