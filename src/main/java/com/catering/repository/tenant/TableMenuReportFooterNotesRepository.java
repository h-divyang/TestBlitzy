package com.catering.repository.tenant;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.catering.model.tenant.TableMenuReportFooterNotesModel;

/**
 * Repository interface for accessing and manipulating table menu report footer notes data.
 * 
 * This interface provides methods for querying and updating the `TableMenuReportFooterNotesModel`
 * for a specific order ID.
 */
public interface TableMenuReportFooterNotesRepository extends JpaRepository<TableMenuReportFooterNotesModel, Long> {

	/**
	 * Finds a table menu report footer note by the specified order ID.
	 * 
	 * @param orderId the ID of the order
	 * @return the `TableMenuReportFooterNotesModel` object associated with the order ID
	 */
	TableMenuReportFooterNotesModel findByOrderId(Long orderId);

	/**
	 * Checks if a table menu report footer note exists for the specified order ID.
	 * 
	 * @param orderId the ID of the order
	 * @return `true` if the record exists, `false` otherwise
	 */
	boolean existsByOrderId(Long orderId);

	/**
	 * Updates the table menu report footer notes for a specific order.
	 * 
	 * This method updates the footer notes in the default, preferred, and supportive languages for the given order ID.
	 * 
	 * @param orderId the ID of the order
	 * @param notesDefaultLang the default language note
	 * @param notesPreferLang the preferred language note
	 * @param notesSupportiveLang the supportive language note
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE table_menu_report_footer_notes tmrfn "
		+ "SET tmrfn.note_default_lang = :notesDefaultLang, "
		+ "	tmrfn.note_prefer_lang = :notesPreferLang, "
		+ "	tmrfn.note_supportive_lang = :notesSupportiveLang "
		+ "WHERE tmrfn.fk_customer_order_details_id = :orderId", nativeQuery = true)
	void updateRecord(@Param("orderId") Long orderId, @Param("notesDefaultLang") String notesDefaultLang, @Param("notesPreferLang") String notesPreferLang, @Param("notesSupportiveLang") String notesSupportiveLang);

}