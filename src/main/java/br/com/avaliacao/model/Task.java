package br.com.avaliacao.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class Task implements Serializable{
	private static final long serialVersionUID = 4048407229498398444L;
	
	private Long id;
	private String title;
	private boolean status;
	private String description;
	private Date creationDate;
	private Date dateLastEdited;
	private Date dateConclusion;
	
	
	private List<Task> tasks;
	private int totalSize;
	
	public Task(){
		status = true;
	}
	
    public Task(Long id){
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getDateLastEdited() {
		return dateLastEdited;
	}

	public void setDateLastEdited(Date dateLastEdited) {
		this.dateLastEdited = dateLastEdited;
	}

	public Date getDateConclusion() {
		return dateConclusion;
	}

	public void setDateConclusion(Date dateConclusion) {
		this.dateConclusion = dateConclusion;
	}
	
	
}
