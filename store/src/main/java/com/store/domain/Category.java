package com.store.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.store.util.LocalDateConverter;
import com.store.util.LocalDateDeserializer;
import com.store.util.LocalDateSerializer;

@Entity
@Table(name="category")
@XmlRootElement
public class Category implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "category_id")
	private long categoryId;
	
	@Column(name = "category_name", nullable=false, unique=true)
	@NotEmpty
	@Length(min=1,max=45)
	private String categoryName;
	
	@ManyToMany(cascade=CascadeType.ALL)  
    @JoinTable(name="category_product", joinColumns=@JoinColumn(name="category_id"), inverseJoinColumns=@JoinColumn(name="product_id"))
	private Set<Product> products;
	
	@Column(name = "category_created")
	@Convert(converter = LocalDateConverter.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate categoryCreated;
	

	@Column(name = "category_lastupdated")
	@Convert(converter = LocalDateConverter.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate categoryUpdated;
	

	public Category() {

	}

	
	public Category(String categoryName) {
		this.categoryName = categoryName;
		products = new HashSet<Product>();
		categoryCreated = LocalDate.now();
		categoryUpdated = LocalDate.now();
	}
	
	public Category(String categoryName, LocalDate date) {
		this.categoryName = categoryName;
		this.products = new HashSet<Product>();
		this.categoryCreated = date;
		this.categoryUpdated = date;
	}
	
	public Category(String categoryName, Set<Product> products, LocalDate date) {
		this.categoryName = categoryName;
		this.products = products;
		this.categoryCreated = date;
		this.categoryUpdated = date;
	}
	

	public long getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(long l) {
		this.categoryId = l;		
	}


	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public Set<Product> getProducts() {
		return products;
	}


	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	
	public LocalDate getCategoryCreated() {
		return categoryCreated;
	}


	public void setCategoryCreated(LocalDate categoryCreated) {
		this.categoryCreated = categoryCreated;
	}


	public LocalDate getCategoryUpdated() {
		return categoryUpdated;
	}


	public void setCategoryUpdated(LocalDate categoryUpdated) {
		this.categoryUpdated = categoryUpdated;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((categoryName == null) ? 0 : categoryName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (categoryName == null) {
			if (other.categoryName != null)
				return false;
		} else if (!categoryName.equals(other.categoryName))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Category [categoryId=" + categoryId + ", categoryName="
				+ categoryName + ", products=" + products
				+ ", categoryCreated=" + categoryCreated + ", categoryUpdated="
				+ categoryUpdated + "]";
	}


	
	


}
