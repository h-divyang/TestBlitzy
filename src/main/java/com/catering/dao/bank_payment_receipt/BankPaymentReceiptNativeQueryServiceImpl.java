package com.catering.dao.bank_payment_receipt;

import org.springframework.stereotype.Service;
import com.catering.dto.tenant.request.CashBankPaymentReceiptCommonResultListDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of the {@link BankPaymentReceiptNativeQueryService} interface. 
 * This service handles the business logic related to querying supplier contact details 
 * for a given bank payment receipt. It interacts with the DAO layer to fetch the contact details 
 * and total amount associated with the bank payment receipt.
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class BankPaymentReceiptNativeQueryServiceImpl implements BankPaymentReceiptNativeQueryService {

	/**
	 * Data Access Object (DAO) for performing database operations related to the {@link BankPaymentReceiptNativeQuery}.
	 * This DAO interacts with the database to execute native queries and retrieve information regarding 
	 * bank payment receipts, including supplier contact details and other related data.
	 */
	BankPaymentReceiptNativeQueryDao bankPaymentReceiptNativeQueryDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CashBankPaymentReceiptCommonResultListDto getSuppilerContact(Long bankPaymentReceiptId) {
		return bankPaymentReceiptNativeQueryDao.getSuppilerContactOfBankPaymentReceiptDetails(bankPaymentReceiptId);
	}

}