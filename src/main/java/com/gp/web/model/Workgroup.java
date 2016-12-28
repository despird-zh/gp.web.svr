package com.gp.web.model;

import java.util.List;

public class Workgroup {
	
	private Long workgroupId;
	
	private String workgroupName;
	
	private String state;
	
	private String admin;
	
	private String manager;
	
	private String managerName;
	
	private String adminName;
	
	private Long orgId;
	
	private String orgName;
	
	private String description;
	
	private Integer storageId;
	
	private String storageName;
	
	private boolean publishOn;
	
	private boolean netdiskOn;
	
	private Integer publishCapacity;
	
	private Integer netdiskCapacity;
	
	private boolean topicOn;
	
	private boolean shareOn;
	
	private boolean linkOn;
	
	private boolean taskOn;
	
	private float taskWeight;

	private String sourceName;
	
	private String createDate;
	
	private String entityCode;
	
	private String nodeCode;
	
	private String imagePath;
	
	private List<Tag> tags ;
	
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

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStorageId() {
		return storageId;
	}

	public void setStorageId(Integer storageId) {
		this.storageId = storageId;
	}

	public boolean getPublishOn() {
		return publishOn;
	}

	public void setPublishOn(boolean publishOn) {
		this.publishOn = publishOn;
	}

	public boolean getNetdiskOn() {
		return netdiskOn;
	}

	public void setNetdiskOn(boolean netdiskOn) {
		this.netdiskOn = netdiskOn;
	}

	public Integer getPublishCapacity() {
		return publishCapacity;
	}

	public void setPublishCapacity(Integer publishCapacity) {
		this.publishCapacity = publishCapacity;
	}

	public Integer getNetdiskCapacity() {
		return netdiskCapacity;
	}

	public void setNetdiskCapacity(Integer netdiskCapacity) {
		this.netdiskCapacity = netdiskCapacity;
	}

	public boolean getTopicOn() {
		return topicOn;
	}

	public void setTopicOn(boolean topicOn) {
		this.topicOn = topicOn;
	}

	public boolean getShareOn() {
		return shareOn;
	}

	public void setShareOn(boolean shareOn) {
		this.shareOn = shareOn;
	}

	public boolean getLinkOn() {
		return linkOn;
	}

	public void setLinkOn(boolean linkOn) {
		this.linkOn = linkOn;
	}

	public boolean getTaskOn() {
		return taskOn;
	}

	public void setTaskOn(boolean taskOn) {
		this.taskOn = taskOn;
	}

	public float getTaskWeight() {
		return taskWeight;
	}

	public void setTaskWeight(float taskWeight) {
		this.taskWeight = taskWeight;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getEntityCode() {
		return entityCode;
	}

	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	public String getNodeCode() {
		return nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getStorageName() {
		return storageName;
	}

	public void setStorageName(String storageName) {
		this.storageName = storageName;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
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
}
