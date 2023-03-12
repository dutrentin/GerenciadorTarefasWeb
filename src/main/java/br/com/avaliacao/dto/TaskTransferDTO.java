package br.com.avaliacao.dto;

import java.util.ArrayList;
import java.util.List;

public class TaskTransferDTO {
	
	private int total;
    private List<TaskDTO> tasks;
    
    public TaskTransferDTO() {
    	tasks = new ArrayList<>();
    }
    
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<TaskDTO> getTasks() {
		return tasks;
	}
	public void setTasks(List<TaskDTO> tasks) {
		this.tasks = tasks;
	}

}
