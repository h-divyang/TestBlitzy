package com.catering.dao.cash_payment_receipt;

import java.util.List;

import org.springframework.stereotype.Service;

import com.catering.dto.tenant.request.CashBankPaymentReceiptCommonResultListDto;
import com.catering.dto.tenant.request.PaymentContactCustomDto;
import com.catering.dto.tenant.request.VoucherNumberDto;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Service implementation for managing cash payment receipts.
 * This service provides methods to fetch payment contact information, 
 * voucher numbers, and supplier contacts from the database using 
 * native queries executed via the {@link CashPaymentReceiptNativeQueryDao}.
 */
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CashPaymentReceiptNativeQueryServiceImpl implements CashPaymentReceiptNativeQueryService {

	/**
	 * Data access object for executing native SQL queries related to cash payment receipts.
	 */
	CashPaymentReceiptNativeQueryDao cashPaymentReceiptNativeQueryDao;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PaymentContactCustomDto> fetchContactOfLabourAllocation() {
		return cashPaymentReceiptNativeQueryDao.fetchContactOfLabourAllocation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PaymentContactCustomDto> fetchContactOfOrderByOrderType(int orderTypeId) {
		return cashPaymentReceiptNativeQueryDao.fetchContactOfOrderByOrderType(orderTypeId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PaymentContactCustomDto> fetchContactOfPurchaseBill() {
		return cashPaymentReceiptNativeQueryDao.fetchContactOfPurchaseBill();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PaymentContactCustomDto> fetchContactOfInvoice() {
		return cashPaymentReceiptNativeQueryDao.fetchContactOfInvoice();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PaymentContactCustomDto> fetchContactOfDebitNote() {
		return cashPaymentReceiptNativeQueryDao.fetchContactOfDebitNote();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<PaymentContactCustomDto> fetchPaymentContactList() {
		return cashPaymentReceiptNativeQueryDao.fetchPaymentContactList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<VoucherNumberDto> fetchVoucherNumberOfLabourAllocation() {
		return cashPaymentReceiptNativeQueryDao.fetchVoucherNumberOfLabourAllocation();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<VoucherNumberDto> fetchVoucherNumberOfOrderByOrderType(int orderTypeId, int voucherTypeId) {
		return cashPaymentReceiptNativeQueryDao.fetchVoucherNumberOfOrderByOrderType(orderTypeId, voucherTypeId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<VoucherNumberDto> fetchVoucherNumberOfPurchaseBill() {
		return cashPaymentReceiptNativeQueryDao.fetchVoucherNumberOfPurchaseBill();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<VoucherNumberDto> fetchVoucherNumberOfDebitNote() {
		return cashPaymentReceiptNativeQueryDao.fetchVoucherNumberOfDebitNote();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<VoucherNumberDto> fetchVoucherNumberOfInvoice() {
		return cashPaymentReceiptNativeQueryDao.fetchVoucherNumberOfInvoice();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer isUseInCashOrBank(long id, List<Integer> voucherTypeIds) {
		return cashPaymentReceiptNativeQueryDao.isUseInCashOrBank(id, voucherTypeIds);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CashBankPaymentReceiptCommonResultListDto getSuppilerContact(Long cashPaymentReceiptId) {
		return cashPaymentReceiptNativeQueryDao.getSuppilerContactOfCashPaymentReceiptDetails(cashPaymentReceiptId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<VoucherNumberDto> fetchVoucherNumberOfAdvancePayment() {
		return cashPaymentReceiptNativeQueryDao.fetchVoucherNumberOfAdvancePayment();
	}

}