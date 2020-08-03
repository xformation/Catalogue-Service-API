package com.brighton.cls.domain;

public class CatalogDetail {
	private Long Id;
	private String title;
	private String description;
	private boolean open = false;
	private String dashboardJson;
	
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
	
}
