package com.brighton.cls.domain;

import java.util.Date;

public class CatalogDetail {
	private Long Id;
	private String title;
	private String description;
	private boolean open = false;
	private String dashboardJson;
	private String type;
	private String createdBy;
	private Date createdOn;
	private String updatedOn;
	private Date updatedBy;
	private String lastModified;

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public String getDashboardJson() {
		return dashboardJson;
	}

	public void setDashboardJson(String dashboardJson) {
		this.dashboardJson = dashboardJson;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(String updatedOn) {
		this.updatedOn = updatedOn;
	}

	public Date getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Date updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	@Override
	public String toString() {
		return "CatalogDetail [Id=" + Id + ", title=" + title + ", description=" + description + ", open=" + open
				+ ", dashboardJson=" + dashboardJson + ", type=" + type + ", createdBy=" + createdBy + ", createdOn="
				+ createdOn + ", updatedOn=" + updatedOn + ", updatedBy=" + updatedBy + ", lastModified=" + lastModified
				+ "]";
	}

}
