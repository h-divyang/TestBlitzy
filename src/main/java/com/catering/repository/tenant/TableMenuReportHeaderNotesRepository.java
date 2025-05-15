package com.catering.repository.tenant;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.catering.model.tenant.TableMenuReportHeaderNotesModel;

/**
 * Repository interface for accessing and manipulating table menu report header notes data.
 * 
 * This interface provides methods for querying and updating the `TableMenuReportHeaderNotesModel`
 * for a specific order ID, as well as retrieving specific order details for header notes.
 */
public interface TableMenuReportHeaderNotesRepository extends JpaRepository<TableMenuReportHeaderNotesModel, Long> {

	/**
	 * Finds a table menu report header note by the specified order ID.
	 * 
	 * @param orderId the ID of the order
	 * @return the `TableMenuReportHeaderNotesModel` object associated with the order ID
	 */
	TableMenuReportHeaderNotesModel findByOrderId(Long orderId);

	/**
	 * Checks if a table menu report header note exists for the specified order ID.
	 * 
	 * @param orderId the ID of the order
	 * @return `true` if the record exists, `false` otherwise
	 */
	boolean existsByOrderId(Long orderId);

	/**
	 * Updates the table menu report header notes for a specific order.
	 * 
	 * This method updates the notes in the default, preferred, and supportive languages for the given order ID.
	 * 
	 * @param orderId the ID of the order
	 * @param notesDefaultLang the default language note
	 * @param notesPreferLang the preferred language note
	 * @param notesSupportiveLang the supportive language note
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE table_menu_report_header_notes tmrhn "
		+ "SET tmrhn.note_default_lang = :notesDefaultLang, "
		+ "tmrhn.note_prefer_lang = :notesPreferLang, "
		+ "tmrhn.note_supportive_lang = :notesSupportiveLang "
		+ "WHERE tmrhn.fk_customer_order_details_id = :orderId", nativeQuery = true)
	void updateRecord(@Param("orderId") Long orderId, @Param("notesDefaultLang") String notesDefaultLang, @Param("notesPreferLang") String notesPreferLang, @Param("notesSupportiveLang") String notesSupportiveLang);

	/**
	 * Retrieves a concatenated string containing the groom's and bride's names and community for a given order ID.
	 * 
	 * @param orderId the ID of the order
	 * @return a string containing the groom's and bride's names and community, formatted for the report header
	 */
	@Query(value = "SELECT CONCAT(CONCAT(CASE "
		+ "WHEN cod.groom_community IS NOT NULL AND cod.groom_community != '' THEN cod.groom_community "
		+ "WHEN cod.bride_community IS NOT NULL AND cod.bride_community != '' THEN cod.bride_community "
		+ "ELSE '' "
		+ "END, '<br>'), "
		+ "CASE "
		+ "WHEN cod.groom_name IS NOT NULL AND cod.groom_name != '' AND cod.bride_name IS NOT NULL AND cod.bride_name != '' THEN CONCAT(cod.groom_name, ' Weds ', cod.bride_name) "
		+ "WHEN cod.groom_name IS NOT NULL AND cod.groom_name != '' THEN cod.groom_name "
		+ "WHEN cod.bride_name IS NOT NULL AND cod.bride_name != '' THEN cod.bride_name "
		+ "ELSE '' "
		+ "END "
		+ ") AS header "
		+ "FROM customer_order_details cod "
		+ "WHERE cod.id = :orderId", nativeQuery = true)
	String getGroomAndBrideName(@Param("orderId") Long orderId);

}