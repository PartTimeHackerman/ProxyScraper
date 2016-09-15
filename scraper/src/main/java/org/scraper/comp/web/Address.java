package org.scraper.comp.web;

import com.google.gson.annotations.SerializedName;

public class Address {

	private String address;

	private Type type;

	private String poster;

	public Address(String address, Type type, String poster){
		this.address = address;
		this.type = type;
		this.poster = poster;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}


	public enum Type{

		@SerializedName("0")
		NORMAL,
		@SerializedName("1")
		CSS,
		@SerializedName("2")
		OCR,
		@SerializedName("3")
		UNCHECKED,
		@SerializedName("4")
		BLACK,
	}
}
