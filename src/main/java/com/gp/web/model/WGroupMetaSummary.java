package com.gp.web.model;

import java.util.List;

public class WGroupMetaSummary {
	
	private Long workgroupId;
	
	private String workgroupName;
	
	private String state;
	
	private String admin;
	
	private String manager;
	
	private String managerName;
	
	private String adminName;

	private String description;

	private String sinceDate;

	private String imagePath;
	
	private int fileSum ;
	
	private int subGroupSum;
	
	private int postSum;
	
	private int memberSum;
	
	private int extMemberSum;

	private int taskSum;
	
	private List<Tag> tags ;
	
	public int getFileSum() {
		return fileSum;
	}

	public void setFileSum(int fileSum) {
		this.fileSum = fileSum;
	}

	public int getSubGroupSum() {
		return subGroupSum;
	}

	public void setSubGroupSum(int subGroupSum) {
		this.subGroupSum = subGroupSum;
	}

	public int getPostSum() {
		return postSum;
	}

	public void setPostSum(int postSum) {
		this.postSum = postSum;
	}

	public int getMemberSum() {
		return memberSum;
	}

	public void setMemberSum(int memberSum) {
		this.memberSum = memberSum;
	}

	public int getExtMemberSum() {
		return extMemberSum;
	}

	public void setExtMemberSum(int extMemberSum) {
		this.extMemberSum = extMemberSum;
	}

	public Long getWorkgroupId() {
		return workgroupId;
	}

	public void setWorkgroupId(Long workgroupId) {
		this.workgroupId = workgroupId;
	}

	public String getWorkgroupName() {
		return workgroupName;
	}

	public void setWorkgroupName(String workgroupName) {
		this.workgroupName = workgroupName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getSinceDate() {
		return sinceDate;
	}

	public void setSinceDate(String sinceDate) {
		this.sinceDate = sinceDate;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTaskSum() {
		return taskSum;
	}

	public void setTaskSum(int taskSum) {
		this.taskSum = taskSum;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	
}
