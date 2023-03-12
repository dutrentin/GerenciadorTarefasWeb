package br.com.avaliacao.managedBean;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.avaliacao.dto.PersonDTO;
import br.com.avaliacao.dto.PersonTransferDTO;
import br.com.avaliacao.dto.TaskDTO;
import br.com.avaliacao.dto.TaskTransferDTO;
import br.com.avaliacao.model.Person;
import br.com.avaliacao.model.Task;
import br.com.avaliacao.utils.UtilsEnum;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.avaliacao.utils.BaseBeans;
import br.com.avaliacao.utils.FilterTask;
import br.com.avaliacao.utils.LoadConfigs;

@SessionScoped
@ManagedBean(name="taskMB")
public class TaskMB extends BaseBeans{
	
	private static final long serialVersionUID = -2559664938405246442L;
	
	private List<Task> tasks;
	private WebTarget target;
	private String hostPath;
	private FilterTask filter;
	private String statusSelectedString;
	private boolean isEdit;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
	
	private LazyDataModel<Task> model;
	private Task task;
	private RestTemplate template = new RestTemplate();
	
	private Integer selectedPerson;
	private Person personSelected;
	
	@PostConstruct
	public void onLoad() {
		personSelected = new Person();
		target = LoadConfigs.loadConfigs();
		
		hostPath = LoadConfigs.getHostPath();
		
		if(statusSelectedString == null){
			statusSelectedString = "true";
		}
		if(tasks == null){
			tasks = new ArrayList<Task>();
		}
		if(filter == null){
			filter = new FilterTask();
		}
		if(task == null){
			task = new Task();
		}
		if(!isEdit){
			task = new Task();
		}
		updateDataTable();
	}
	
	public void updateDataTable(){
		model = new LazyDataModel<Task>(){
			private static final long serialVersionUID = 1L;
			
			@Override
			public List<Task> load(int first, int maxResults, String sortField,
					SortOrder sortOrder, Map<String, String> filters) {
				
				System.out.println("Nova requisição de tarefas");
				
				if(statusSelectedString.equals("true")){
					filter.setFilterStatus(true);
				}else{
					filter.setFilterStatus(false);
				}
				
				validateEmptyFilters(sortOrder);
				StringBuilder uri = new StringBuilder();
				uri.append("/tasks/findAll/");
				
				
				uri.append(maxResults + "/");
				uri.append(first + "/");
				/*
				uri.append(filter.getFilterTitle() + "/");
				uri.append(filter.isFilterStatus() + "/");
				
				String dateFormatCreation = null;
				if(filter.getCreationDate() != null){
					dateFormatCreation = formatter.format(filter.getCreationDate());
				}
				uri.append(dateFormatCreation + "/");
				String dateFormatConclusion = null;
				if(filter.getDateConclusion() != null){
					dateFormatConclusion = formatter.format(filter.getDateConclusion());
				}
				uri.append(dateFormatConclusion + "/");
				uri.append(filter.getOrder() + "/");
				*/
				
				try{
				    List<Task> tasksReturn = new ArrayList<>();
				    
				    
				    ResponseEntity<TaskTransferDTO> retornoTaskTransfer = null;
					template = new RestTemplate();
					
					retornoTaskTransfer = template.getForEntity(hostPath + uri.toString() +"2", TaskTransferDTO.class);
					
					TaskTransferDTO taskTransferDTO = retornoTaskTransfer.getBody();
				    
				    if(taskTransferDTO != null && taskTransferDTO.getTasks() != null && !taskTransferDTO.getTasks().isEmpty()) {
				    	for(TaskDTO taskDTO : taskTransferDTO.getTasks()) {
				    		taskDTO.getCreationDate();
				    		
				    		Task task = new Task();
				    		task.setCreationDate(taskDTO.getCreationDate());
				    		task.setId(taskDTO.getId());
				    		task.setDescription(taskDTO.getDescription());
				    		task.setTitle(taskDTO.getTitle());
				    		//task.setDateLastEdited(null)
				    		task.setStatus(true);
				    		
				    		tasksReturn.add(task);
				    	}
				    }
				    
				    
				    
				    task.setTasks(tasksReturn);
				}catch(Exception ex){
					getMessageErrorConnect();
					System.out.println();
				}
				if(task != null && task.getTasks() != null){
					tasks.removeAll(tasks);
					tasks.addAll(task.getTasks());
				}
				task.setTotalSize(2);
				setRowCount(task.getTotalSize());
				
				return tasks;
			}
			
			private void validateEmptyFilters(SortOrder sortOrder) {
				if(SortOrder.ASCENDING.equals(sortOrder)){
					filter.setOrder("ASC");
				}else{
					filter.setOrder("DESC");
				}
				
				if(filter.getFilterTitle() != null){
					if(filter.getFilterTitle().equals("")){
						filter.setFilterTitle(null);
					}
				}
			}
		};
	}
	
	public String saveOrEdit(){
		boolean returnMethod = true;
		if(task != null){
			if(task.getId() == null || task.getId() == 0){
				returnMethod = saveMethod();
			}else{
				returnMethod = edit();
			}
		}
		if(returnMethod){
			clean();
		}
		return "/public/addTask.faces?faces-redirect=true";
	}

	public List<Person> completeTextPerson(String query){
		
		ResponseEntity<PersonTransferDTO> respPersons = null;
		template = new RestTemplate();
		
		respPersons = template.getForEntity(hostPath + "/persons/list", PersonTransferDTO.class);
		
		PersonTransferDTO personTransferDTO = respPersons.getBody();
		
		List<Person> returnList = new ArrayList<>();
		
		if(personTransferDTO != null && personTransferDTO.getPersons() != null && !personTransferDTO.getPersons().isEmpty()) {
			for(PersonDTO personDTO : personTransferDTO.getPersons()) {
				
				returnList.add(new Person(personDTO.getId(), personDTO.getName(), personDTO.getEmail()));
			}
		}
		
		return returnList;
		
	}
	
	
	public String redirectListTask() throws IOException{
		clean();
		statusSelectedString = "true";
		updateDataTable();
		return "/public/task/listTask.faces?faces-redirect=true";
	}
	
	public String redirectCadTask() throws IOException{
		clean();
		return "/public/task/cadTask.faces?faces-redirect=true";
	}
	
	public String redirectEditTask(){
		return "/public/task/cadTask.faces?faces-redirect=true";
	}
	
	private boolean saveMethod() {
		
		task.setStatus(true);
		
		ResponseEntity<Task> respTask = null;
		try{
			template = new RestTemplate();
			UriComponents uri = UriComponentsBuilder.newInstance()
					.host(hostPath)
					.path("/tasks/save")
					.build();
			
			task.setPerson(returnPersonById());
			
			
			respTask = template.postForEntity(hostPath + "/tasks/save", task, Task.class);
			
		}catch(Exception ex){
			ex.printStackTrace();
			getMessageErrorConnect();
			return false;
		}
		
		boolean returnValue = validateReturn(respTask);
			return returnValue;
	}

	private boolean validateReturn(ResponseEntity<Task> respTask) {
		if(respTask != null){
			if(respTask.getStatusCode().value() == UtilsEnum.OK.value || respTask.getStatusCode().value() == UtilsEnum.CRIADO.value){
				getMessageAddSuccess();
			}else{
				getMessageAddError();
				return false;
			}
		}
		return true;
	}
	
	public String delete(){
		task.setStatus(false);
		deleteTask();
		return "/public/task/listTask.faces?faces-redirect=true";
	}
	
	private void deleteTask() {
		try {
			
			StringBuilder uri = new StringBuilder();
			uri.append("/tasks/remove/");
			uri.append(task.getId());
			
			template = new RestTemplate();
	
			template.delete(hostPath + uri.toString());
			
		}catch (Exception e) {
			e.printStackTrace();
			getMessageDeleteError();
		}
		
	}
	
	public boolean edit(){
		try{
			StringBuilder uri = new StringBuilder();
			uri.append("/tasks/update");
			
			template = new RestTemplate();
	
			template.put(hostPath + uri.toString(), task);
			
		}catch(Exception ex){
			ex.printStackTrace();
				getMessageEditError();
			return false;
		}
		
		return true;
	}

	
	public String changeStatus(){
		Response response  = null;
		Entity<Task> entity = null;
		try{
			if(task.getId() != null){
				if(task.isStatus()){
					task.setStatus(false);
				}else{
					task.setStatus(true);
				}
					entity = Entity.entity(task, MediaType.APPLICATION_XML);
					response = target.path("/tasks").request().put(entity);
			}
		}catch(Exception ex){
			ex.printStackTrace();
			getMessageEditStatusError();
		}
		return "/public/listTask.faces?faces-redirect=true";
	}
	

	private boolean returnEditMethod(boolean ehEdicao, Response response) {
		if(response != null){
			if(response.getStatus() == UtilsEnum.OK.value || response.getStatus() == UtilsEnum.CRIADO.value){
				if(ehEdicao){
					getMessageEditSuccess();
				}else{
					getMessageDeleteSuccess();
				}
			}else{
				if(ehEdicao){
					getMessageEditError();
				}else{
					getMessageDeleteError();
				}
				return false;
			}
		}
		return true;
	}
	
	
	private void getMessageAddSuccess() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Tarefa cadastrada com sucesso!",null);
		FacesContext.getCurrentInstance().addMessage("Sucess Message ", msg);
	}
	
	private void getMessageDeleteSuccess() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Tarefa removida com sucesso!",null);
		FacesContext.getCurrentInstance().addMessage("Sucess Message ", msg);
	}
	
	private void getMessageEditSuccess() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Tarefa editada com sucesso!",null);
		FacesContext.getCurrentInstance().addMessage("Sucess Message ", msg);
	}
	
	private void getMessageAddError() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro ao cadastrar nova Tarefa!", null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	private void getMessageDeleteError() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro ao remover Tarefa com id " + task.getId(), null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	private void getMessageEditError() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro ao editar Tarefa com id " + task.getId(), null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	private void getMessageEditStatusError() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro ao alterar status da Tarefa com id " + task.getId(), null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	private void getMessageErrorConnect() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro de conexão com o servidor!", null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public void clean(){
		filter = new FilterTask();
		task = new Task();
	}
	
	private Person returnPersonById() {
		Person person = new Person();
		
		ResponseEntity<PersonDTO> respPerson = null;
		template = new RestTemplate();
		
		respPerson = template.getForEntity(hostPath + "/persons/" + personSelected.getId(), PersonDTO.class);
		
		if(respPerson.getBody() != null) {
			person.setId(respPerson.getBody().getId());
			person.setAtivo(true);
			person.setEmail(respPerson.getBody().getEmail());
			person.setName(respPerson.getBody().getName());	
		}
		
		return person;
		
	}
	
	public List<Task> getTasks() {
		return tasks;
	}
	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}
	public FilterTask getFilter() {
		return filter;
	}
	public void setFilter(FilterTask filter) {
		this.filter = filter;
	}
	public LazyDataModel<Task> getModel() {
		return model;
	}
	public void setModel(LazyDataModel<Task> model) {
		this.model = model;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public String getStatusSelectedString() {
		return statusSelectedString;
	}
	public void setStatusSelectedString(String statusSelectedString) {
		this.statusSelectedString = statusSelectedString;
	}
	public boolean isEdit() {
		return isEdit;
	}
	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	public Integer getSelectedPerson() {
		return selectedPerson;
	}

	public void setSelectedPerson(Integer selectedPerson) {
		this.selectedPerson = selectedPerson;
	}

	public Person getPersonSelected() {
		return personSelected;
	}

	public void setPersonSelected(Person personSelected) {
		this.personSelected = personSelected;
	}
	
	

	
	
}
