package com.store.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name="product")
@XmlRootElement
public class Product implements Serializable{
	
	private static final long serialVersionUID = 1L;
		
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "product_id")
	private long productId;
	
	@Column(name = "product_sku", nullable=false, unique=true)
	@NotEmpty
	@Length(min=1,max=45)
	private String productSku;
	
	@Column(name = "product_name",nullable=false)
	@NotEmpty
	@Length(min=1,max=45)
	private String productName;
	
	@Column(name = "product_created")
	@Convert(converter = LocalDateConverter.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate productCreated;
	

	@Column(name = "product_lastupdate")
	@Convert(converter = LocalDateConverter.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate productLastUpdated;
	
	public Product(){
		
	}

	public Product(String sku,String productName){
		this.productSku = sku;
		this.productName = productName;
		this.productCreated = LocalDate.now();
		this.productLastUpdated = LocalDate.now();
	}
	
	public Product(String sku,String productName, LocalDate createdDate, LocalDate updatedDate){
		this.productSku = sku;
		this.productName = productName;
		this.productCreated = createdDate;
		this.productLastUpdated = updatedDate;
	}
	
	public Product(Long id,String sku,String productName, LocalDate createdDate, LocalDate updatedDate){
		this.productId = id;
		this.productSku = sku;
		this.productName = productName;
		this.productCreated = createdDate;
		this.productLastUpdated = updatedDate;
	}

	public long getProductId() {
		return productId;
	}
	
	public void setProductId(long productId) {
		this.productId = productId;
	}
	
	public String getProductSku() {
		return productSku;
	}


	public void setProductSku(String productSku) {
		this.productSku = productSku;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public LocalDate getProductCreated() {
		return productCreated;
	}

	public void setProductCreated(LocalDate productCreated) {
		this.productCreated = productCreated;
	}

	public LocalDate getProductLastUpdated() {
		return productLastUpdated;
	}

	public void setProductLastUpdated(LocalDate productLastUpdated) {
		this.productLastUpdated = productLastUpdated;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((productName == null) ? 0 : productName.hashCode());
		result = prime * result
				+ ((productSku == null) ? 0 : productSku.hashCode());
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
		Product other = (Product) obj;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		if (productSku == null) {
			if (other.productSku != null)
				return false;
		} else if (!productSku.equals(other.productSku))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productSku=" + productSku
				+ ", productName=" + productName + ", productCreated="
				+ productCreated + ", productLastUpdated=" + productLastUpdated
				+ "]";
	}
	


}
