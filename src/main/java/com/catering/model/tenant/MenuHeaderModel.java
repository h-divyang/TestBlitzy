package com.catering.model.tenant;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The MenuHeaderModel class represents a menu header in the catering system.
 * It extends the IdModel class, which provides an ID for the menu header.
 * 
 * <p>A menu header contains the title and sequence number of a menu section.</p>
 * 
 * <p>This class is annotated with the @Entity annotation to indicate that it is a persistent entity
 * in the database. The @Table annotation specifies the name of the corresponding database table.</p>
 * 
 * <p>Use the getter and setter methods to access and modify the attributes of the menu header.</p>
 * 
 * <p>The class also provides a builder pattern using the @Builder annotation, allowing for
 * easy and flexible creation of MenuHeaderModel instances.</p>
 * 
 * @author Krushali Talaviya
 * @since July 2023
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "menu_header")
public class MenuHeaderModel extends CommonMenuModel {

}