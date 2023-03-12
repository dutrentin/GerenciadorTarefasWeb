package br.com.avaliacao.dto;

import java.util.Date;

public class TaskDTO {
    private Long id;
    private String title;
    private boolean status;
    private String description;
    private Date creationDate;
    private Date dateLastEdited;
    private Date dateConclusion;

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
