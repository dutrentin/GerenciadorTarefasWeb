package br.com.avaliacao.managedBean;

import java.io.ByteArrayInputStream;
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

import br.com.avaliacao.model.Person;
import br.com.avaliacao.utils.UtilsEnum;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.avaliacao.utils.BaseBeans;
import br.com.avaliacao.utils.FileUtils;
import br.com.avaliacao.utils.LoadConfigs;

@SessionScoped
@ManagedBean(name="personAddEditMB")
public class PersonAddEditMB extends BaseBeans {

	private static final long serialVersionUID = 4149608960827376871L;
	
	private Person person;
	private byte[] photo;
	private WebTarget target;
	private boolean isEdit;
	
	
	@PostConstruct
	public void PersonAddEditMB(){
		target = LoadConfigs.loadConfigs();
			
		if(person == null){
			person = new Person();
		}
		if(!isEdit){
			person = new Person();
		}
	}
	
	public String saveOrEdit(){
		boolean returnMethod = true;
		if(person != null){
			if(person.getId() == null || person.getId() == 0){
				returnMethod = saveMethod();
			}else{
				returnMethod = edit(true);
			}
		}
//		if(!returnMethod){
//			return "/public/addPerson.faces?faces-redirect=true";
//		}
		if(returnMethod){
			clean();
		}
		return "/public/addPerson.faces?faces-redirect=true";
	}

	
	public String redirectListPerson() throws IOException{
		clean();
		return "/public/listPerson.faces?faces-redirect=true";
	}
	
	public String redirectCadPerson() throws IOException{
		clean();
		return "/public/cadPerson.faces?faces-redirect=true";
	}
	
	public String redirectEditPerson(){
		return "/public/cadPerson.faces?faces-redirect=true";
	}
	
	private boolean saveMethod() {
		person.setAtivo(true);
		Entity<Person> entity = Entity.entity(person, MediaType.APPLICATION_XML);
		Response response = null;
		try{
			response = target.path("/persons").request().post(entity);
		}catch(Exception ex){
			getMessageErrorConnect();
			return false;
		}
		
		boolean returnValue = validateReturn(response);
			return returnValue;
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
		edit(false);
		return "/public/listPerson.faces?faces-redirect=true";
	}
	
	public boolean edit(boolean ehEdicao){
		Response response  = null;
		Entity<Person> entity = null;
		try{
			if(person.getId() != null){
				if(ehEdicao){
					entity = Entity.entity(person, MediaType.APPLICATION_XML);
					response = target.path("/persons").request().put(entity);
				}else{
					person.setAtivo(false);
					entity = Entity.entity(person, MediaType.APPLICATION_XML);
					response = target.path("/persons").request().put(entity);
				}
				System.out.println();
			}
		}catch(Exception ex){
			ex.printStackTrace();
			if(ehEdicao){
				getMessageEditError();
			}else{
				getMessageDeleteError();
			}
			return false;
		}
		
		return returnEditMethod(ehEdicao, response);
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
		DefaultStreamedContent retorno = null;
//		person.setId(1L);
		if(person.getFoto() != null){
			retorno = new DefaultStreamedContent(new ByteArrayInputStream(person.getFoto()));
			retorno.setName("Foto");
			retorno.setContentEncoding("UTF-8");
//			retorno.setContentType("image/png");
			return retorno;
		}
		
        if(person == null || person.getFoto() == null){
        	System.out.println("Sem foto");
        }else{
        	return new DefaultStreamedContent(new ByteArrayInputStream(person.getFoto()));
        }
		return null;
	}
	
	public void clean(){
		person = new Person();
	}

	public void fileUploadHandlerLogo(FileUploadEvent event) throws Exception {
		photo = FileUtils.toByteArray(event);
		person.setFoto(photo);
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
