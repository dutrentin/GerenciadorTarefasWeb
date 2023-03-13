package br.com.avaliacao.utils;

import java.io.Serializable;
import java.util.Date;

public class FilterTask implements Serializable{
	
	private static final long serialVersionUID = 7833360363874843256L;
	
	private String filterTitle;
	private boolean filterStatus;
	private Date creationDate;
	private Date dateConclusion;
	private Date dateInitial;
    private Date dateFinal;
	private int maxResults;
	private int currentPage;
	private String order;
	
	public FilterTask(){
		filterStatus = true;
	}
	
	public String getFilterTitle() {
		return filterTitle;
	}
	public void setFilterTitle(String filterTitle) {
		this.filterTitle = filterTitle;
	}
	public boolean isFilterStatus() {
		return filterStatus;
	}
	public void setFilterStatus(boolean filterStatus) {
		this.filterStatus = filterStatus;
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
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getDateConclusion() {
		return dateConclusion;
	}
	public void setDateConclusion(Date dateConclusion) {
		this.dateConclusion = dateConclusion;
	}

	public Date getDateInitial() {
		return dateInitial;
	}

	public void setDateInitial(Date dateInitial) {
		this.dateInitial = dateInitial;
	}

	public Date getDateFinal() {
		return dateFinal;
	}

	public void setDateFinal(Date dateFinal) {
		this.dateFinal = dateFinal;
	}
	
	
	
     
}
