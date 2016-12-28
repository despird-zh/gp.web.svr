package com.gp.web.model;

public class UserMetaSummary {

	private String name;
	
	private int sourceId;
	
	private String sourceName;
	
	private String sourceShort;
	
	private String[] badges;
	
	private String signature;
	
	private TreeNode[][] treeNodes; 
	
	private int fileSum;
	
	private int taskSum;
	
	private int postSum;
	
	private int shareSum;
	
	private String imagePath;
	
	private String sinceDate;

	public String getSinceDate() {
		return sinceDate;
	}

	public void setSinceDate(String sinceDate) {
		this.sinceDate = sinceDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public int getFileSum() {
		return fileSum;
	}

	public void setFileSum(int fileSum) {
		this.fileSum = fileSum;
	}

	public int getTaskSum() {
		return taskSum;
	}

	public void setTaskSum(int taskSum) {
		this.taskSum = taskSum;
	}

	public int getPostSum() {
		return postSum;
	}

	public void setPostSum(int postSum) {
		this.postSum = postSum;
	}

	public int getShareSum() {
		return shareSum;
	}

	public void setShareSum(int shareSum) {
		this.shareSum = shareSum;
	}

	public String getSourceShort() {
		return sourceShort;
	}

	public void setSourceShort(String sourceShort) {
		this.sourceShort = sourceShort;
	}

	public String[] getBadges() {
		return badges;
	}

	public void setBadges(String[] badges) {
		this.badges = badges;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public TreeNode[][] getTreeNodes() {
		return treeNodes;
	}

	public void setTreeNodes(TreeNode[][] treeNodes) {
		this.treeNodes = treeNodes;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}
