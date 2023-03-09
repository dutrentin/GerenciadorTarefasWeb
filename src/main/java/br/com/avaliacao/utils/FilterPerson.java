package br.com.avaliacao.utils;

import java.io.Serializable;
import java.util.Date;

public class FilterPerson implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String filterName;
	private String filterCPF;
	private String filterEmail;
	private Date filterDate;
	private int maxResults;
	private int currentPage;
	private String order;
	
	public FilterPerson(String filterName, String filterCPF,
			String filterEmail, Date filterData, int maxResults,
			int currentPage, String order) {
		super();
		this.filterName = filterName;
		this.filterCPF = filterCPF;
		this.filterEmail = filterEmail;
		this.filterDate = filterData;
		this.maxResults = maxResults;
		this.currentPage = currentPage;
		this.order = order;
	}

	public FilterPerson() {
		this.filterName = "";
		this.filterCPF = "";
		this.filterEmail = "";
		this.maxResults = 0;
		this.currentPage = 0;
		this.order = "";
	}

	public String getFilterName() {
		return filterName;
	}
	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
	public String getFilterCPF() {
		return filterCPF;
	}
	public void setFilterCPF(String filterCPF) {
		this.filterCPF = filterCPF;
	}
	public String getFilterEmail() {
		return filterEmail;
	}
	public void setFilterEmail(String filterEmail) {
		this.filterEmail = filterEmail;
	}
	public int getMaxResults() {
		return maxResults;
	}
	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}

	public Date getFilterDate() {
		return filterDate;
	}

	public void setFilterDate(Date filterDate) {
		this.filterDate = filterDate;
	}
	
}
