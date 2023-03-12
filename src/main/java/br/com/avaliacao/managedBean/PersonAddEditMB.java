package br.com.avaliacao.managedBean;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.avaliacao.dto.PersonTransferDTO;
import br.com.avaliacao.model.Person;
import br.com.avaliacao.model.Task;
import br.com.avaliacao.utils.BaseBeans;
import br.com.avaliacao.utils.LoadConfigs;
import br.com.avaliacao.utils.UtilsEnum;

@SessionScoped
@ManagedBean(name="personAddEditMB")
public class PersonAddEditMB extends BaseBeans {

	private static final long serialVersionUID = 4149608960827376871L;
	
	private Person person;
	private byte[] photo;
	private WebTarget target;
	private boolean isEdit;
	private String hostPath;
	private RestTemplate template = new RestTemplate();
	
	
	@PostConstruct
	public void PersonAddEditMB(){
		hostPath = LoadConfigs.getHostPath();
			
		if(person == null){
			person = new Person();
		}
		if(!isEdit){
			person = new Person();
		}
	}
	
	public String saveOrEdit() throws IOException{
		boolean returnMethod = true;
		if(person != null){
			if(person.getId() == null || person.getId() == 0){
				returnMethod = saveMethod();
			}else{
				returnMethod = edit();
			}
		}
		if(returnMethod){
			clean();
		}
		return redirectListPerson();
	}

	
	public String redirectListPerson() throws IOException{
		clean();
		return "/public/person/listPerson.faces?faces-redirect=true";
	}
	
	public String redirectCadPerson() throws IOException{
		clean();
		return "/public/person/cadPerson.faces?faces-redirect=true";
	}
	
	public String redirectEditPerson(){
		return "/public/person/cadPerson.faces?faces-redirect=true";
	}
	
	private boolean saveMethod() {
		person.setAtivo(true);
		ResponseEntity<Person> respPerson = null;
		try{
			StringBuilder uri = new StringBuilder();
			uri.append("/persons/save");
			
			template = new RestTemplate();
			//UriComponents uri = UriComponentsBuilder.newInstance()
					//.host(hostPath)
					//.path("/persons/save")
					//.build();
			
			respPerson = template.postForEntity(hostPath + uri.toString(), person, Person.class);
			
		}catch(Exception ex){
			ex.printStackTrace();
			getMessageErrorConnect();
			return false;
		}
		
		return true;
	}

	private boolean validateReturn(Response response) {
		if(response != null){
			if(response.getStatus() == UtilsEnum.CONFLITO.value){
				getMessageErrorDuplicate();
				return false;
			}
			if(response.getStatus() == UtilsEnum.OK.value || response.getStatus() == UtilsEnum.CRIADO.value){
				getMessageAddSuccess();
			}else{
				getMessageAddError();
				return false;
			}
		}
		return true;
	}
	
	public String delete(){
		person.setAtivo(false);
		deletePerson();
		return "/public/person/listPerson.faces?faces-redirect=true";
	}
	
	private void deletePerson() {
		try {
			
			StringBuilder uri = new StringBuilder();
			uri.append("/persons/remove/");
			uri.append(person.getId());
			
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
			uri.append("/persons/update");
			
			template = new RestTemplate();
	
			template.put(hostPath + uri.toString(), person);
			
		}catch(Exception ex){
			ex.printStackTrace();
				getMessageEditError();
			return false;
		}
		
		return true;
	}

	private boolean returnEditMethod(boolean ehEdicao, Response response) {
		if(response != null){
			if(response.getStatus() == UtilsEnum.CONFLITO.value){
				getMessageErrorDuplicate();
				return false;
			}
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
	
	
	private void getMessageErrorConnect() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro de conexão com o servidor!", null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	private void getMessageErrorDuplicate() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Este CPF já está cadastrado!", null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	private void getMessageAddSuccess() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Pessoa cadastrada com sucesso!",null);
		FacesContext.getCurrentInstance().addMessage("Sucess Message ", msg);
	}
	
	private void getMessageDeleteSuccess() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Pessoa removida com sucesso!",null);
		FacesContext.getCurrentInstance().addMessage("Sucess Message ", msg);
	}
	
	private void getMessageEditSuccess() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,"Pessoa editada com sucesso!",null);
		FacesContext.getCurrentInstance().addMessage("Sucess Message ", msg);
	}
	
	private void getMessageAddError() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro ao cadastrar nova pessoa!", null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	private void getMessageDeleteError() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro ao remover pessoa com id " + person.getId(), null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	private void getMessageEditError() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro ao editar pessoa com id " + person.getId(), null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public StreamedContent getImagePhoto() throws IOException {
		return null;
	}
	
	public void clean(){
		person = new Person();
	}

	public void fileUploadHandlerLogo(FileUploadEvent event) throws Exception {
		
	}
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}
	
}
