package com.catering.interfaces;

import java.time.LocalDateTime;

/**
 * Interface representing the audit information for an entity.
 * This interface includes methods to track who created and updated an entity, 
 * when it was created or updated, and how many times it has been edited.
 */
public interface Audit {

	/**
	 * Gets the ID of the user who created the entity.
	 *
	 * @return The ID of the user who created the entity.
	 */
	public Long getCreatedById();

	/**
	 * Sets the ID of the user who created the entity.
	 *
	 * @param createdById The ID of the user who created the entity.
	 */
	public void setCreatedById(Long createdById);

	/**
	 * Gets the ID of the user who last updated the entity.
	 *
	 * @return The ID of the user who last updated the entity.
	 */
	public Long getUpdatedById();

	/**
	 * Sets the ID of the user who last updated the entity.
	 *
	 * @param updatedById The ID of the user who last updated the entity.
	 */
	public void setUpdatedById(Long updatedById);

	/**
	 * Gets the timestamp of when the entity was created.
	 *
	 * @return The timestamp of when the entity was created.
	 */
	public LocalDateTime getCreatedAt();

	/**
	 * Sets the timestamp of when the entity was created.
	 *
	 * @param createdAt The timestamp of when the entity was created.
	 */
	public void setCreatedAt(LocalDateTime createdAt);

	/**
	 * Gets the timestamp of when the entity was last updated.
	 *
	 * @return The timestamp of when the entity was last updated.
	 */
	public LocalDateTime getUpdatedAt();

	/**
	 * Sets the timestamp of when the entity was last updated.
	 *
	 * @param updatedAt The timestamp of when the entity was last updated.
	 */
	public void setUpdatedAt(LocalDateTime updatedAt);

	/**
	 * Gets the number of times the entity has been edited.
	 *
	 * @return The number of edits made to the entity.
	 */
	public Integer getEditCount();

	/**
	 * Sets the number of times the entity has been edited.
	 *
	 * @param editCount The number of edits made to the entity.
	 */
	public void setEditCount(Integer editCount);

}