package com.gp.web.model;

public class UserBelonging {

	private Long belongId;
	
	private String name;
	
	private String type;
	
	private Boolean postVisible;

	private Boolean postAcceptable;
	
	public Long getBelongId() {
		return belongId;
	}

	public void setBelongId(Long belongId) {
		this.belongId = belongId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getPostVisible() {
		return postVisible;
	}

	public void setPostVisible(Boolean postVisible) {
		this.postVisible = postVisible;
	}

	public Boolean getPostAcceptable() {
		return postAcceptable;
	}

	public void setPostAcceptable(Boolean postAcceptable) {
		this.postAcceptable = postAcceptable;
	}
	
}
