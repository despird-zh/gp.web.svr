package com.gp.web.model;

import java.util.Set;

public class CabinetItem {
	
	private String itemName;
	
	private String timeElapse;
	
	private String itemType;
	
	private String classification;
	
	private long itemId;
	
	private boolean externalOwned;
	
	private String account;
	
	private String description;
	
	private ItemStat sourceStat;
	
	private ItemStat childStat;
	
	private ItemStat favoriteStat;
	
	private ItemStat versionStat;
	
	private ItemStat propStat;
	
	private Set<Tag> tags;
	
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public boolean isExternalOwned() {
		return externalOwned;
	}

	public void setExternalOwned(boolean externalOwned) {
		this.externalOwned = externalOwned;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTimeElapse() {
		return timeElapse;
	}

	public void setTimeElapse(String timeElapse) {
		this.timeElapse = timeElapse;
	}

	public ItemStat getChildStat() {
		return childStat;
	}

	public void setChildStat(ItemStat childStat) {
		this.childStat = childStat;
	}

	public ItemStat getFavoriteStat() {
		return favoriteStat;
	}

	public void setFavoriteStat(ItemStat favoriteStat) {
		this.favoriteStat = favoriteStat;
	}

	public ItemStat getVersionStat() {
		return versionStat;
	}

	public void setVersionStat(ItemStat versionStat) {
		this.versionStat = versionStat;
	}

	public ItemStat getPropStat() {
		return propStat;
	}

	public void setPropStat(ItemStat propStat) {
		this.propStat = propStat;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public ItemStat getSourceStat() {
		return sourceStat;
	}

	public void setSourceStat(ItemStat sourceStat) {
		this.sourceStat = sourceStat;
	}

}
