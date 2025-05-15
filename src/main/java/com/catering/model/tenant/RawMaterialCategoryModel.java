package com.catering.model.tenant;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.math.NumberUtils;

import com.catering.interfaces.Priority;
import com.catering.model.audit.AuditByIdModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "raw_material_category")
public class RawMaterialCategoryModel extends AuditByIdModel implements Priority {

	@Column(name = "name_default_lang")
	private String nameDefaultLang;

	@Column(name = "name_prefer_lang")
	private String namePreferLang;

	@Column(name = "name_supportive_lang")
	private String nameSupportiveLang;

	@JoinColumn(name = "fk_raw_material_category_type_id")
	@ManyToOne(targetEntity = RawMaterialCategoryTypeModel.class)
	private RawMaterialCategoryTypeModel rawMaterialCategoryType;

	@Column(name = "is_direct_order")
	private Boolean isDirectOrder;

	@Column(name = "priority")
	private Integer priority;

	@OneToMany(mappedBy = "rawMaterialCategory")
	private List<RawMaterialModel> rawMaterials;

	/**
	 * Creates a new instance of {@link RawMaterialCategoryModel} with the name and priority initialized based on the provided query.
	 * The name fields are set for each language (default, preferred, and supportive), and the priority is parsed from the query if it is a valid number.
	 *
	 * This method is useful for searching or filtering raw material categories by their name and priority.
	 *
	 * @param query The search query that will populate the name fields (for each language) and the priority field of the raw material category model.
	 * @return A new instance of {@link RawMaterialCategoryModel} with the name and priority set according to the query value.
	 */
	public static RawMaterialCategoryModel ofSearchingModel(String query) {
		return RawMaterialCategoryModel.builder().
				nameDefaultLang(query)
				.namePreferLang(query)
				.nameSupportiveLang(query)
				.priority(NumberUtils.isDigits(query) ? Integer.parseInt(query) : null)
				.build();
	}

}