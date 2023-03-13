package br.com.avaliacao.managedBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.avaliacao.dto.PersonDTO;
import br.com.avaliacao.dto.PersonTransferDTO;
import br.com.avaliacao.model.Person;
import br.com.avaliacao.utils.FilterPerson;
import br.com.avaliacao.utils.LoadConfigs;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.avaliacao.utils.BaseBeans;

@ViewScoped
@ManagedBean(name="personMB")
public class PersonMB extends BaseBeans{
	private static final long serialVersionUID = 6409513922359907954L;
	
	private List<Person> persons;
	private FilterPerson filter;
	private String hostPath;
	private String filtroNome;
	private RestTemplate template = new RestTemplate();
	
	private LazyDataModel<Person> model;
	Person person;
	
	@PostConstruct
	public void onLoad() {
		hostPath = LoadConfigs.getHostPath();
		
		if(persons == null){
			persons = new ArrayList<Person>();
		}
		if(filter == null){
			filter = new FilterPerson();
		}
		if(person == null){
			person = new Person();
		}
		updateDataTable();
	}
	
	public void updateDataTable(){
		model = new LazyDataModel<Person>(){
			private static final long serialVersionUID = 1L;
			
			@Override
			public List<Person> load(int first, int maxResults, String sortField,
					SortOrder sortOrder, Map<String, String> filters) {
				
				cleanList();
				
				StringBuilder uri = new StringBuilder();
				uri.append("/persons/list/{name}/{email}");
				
				Map<String, Object> uriVariables = new HashMap<String, Object>();
				
				generateFilters(uriVariables);
				
			    ResponseEntity<PersonTransferDTO> retornoPersonTransfer = null;
				template = new RestTemplate();
				
				retornoPersonTransfer = template.getForEntity(hostPath + uri.toString(), PersonTransferDTO.class, uriVariables);
				
				PersonTransferDTO personTransferDTO = retornoPersonTransfer.getBody();
				
				try {
				
					if(personTransferDTO != null && personTransferDTO.getPersons() != null && !personTransferDTO.getPersons().isEmpty()) {
						for(PersonDTO personDTO : personTransferDTO.getPersons()) {
							Person person = new Person();
							person.setAtivo(true);
							person.setEmail(personDTO.getEmail());
							person.setName(personDTO.getName());
							person.setId(personDTO.getId());
							persons.add(person);
						}
					}else {
						cleanList();
					}
				}catch (Exception e) {
					e.printStackTrace();
					getMessageErrorConnect();
				}
				
				setRowCount(persons.size());
				
				return persons;
			}

			private void cleanList() {
				persons = new ArrayList<Person>();
			}

			private void generateFilters(Map<String, Object> uriVariables) {
				if(filter.getFilterName() !=  null && !filter.getFilterName().equals("")) {
					uriVariables.put("name", filter.getFilterName());
				}else {
					uriVariables.put("name","-");
				}
				
				if(filter.getFilterEmail() !=  null && !filter.getFilterEmail().equals("")) {
					uriVariables.put("email", filter.getFilterEmail());
				}else {
					uriVariables.put("email","-");
				}
			}
			
		};
	}
	
	private void getMessageErrorConnect() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro de conexão com o servidor!", null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	public String redirectCadPerson() throws IOException{
		cleanFilters();
		return "/public/person/cadPerson.faces?faces-redirect=true";
	}
	
	public String redirectListPerson() throws IOException{
		cleanFilters();
		return "/public/person/listPerson.faces?faces-redirect=true";
	}
	

	public void cleanFilters(){
		person = new Person();
		filter = new FilterPerson();
	}
	
	public List<Person> getPersons() {
		return persons;
	}


	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	public LazyDataModel<Person> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Person> model) {
		this.model = model;
	}

	public FilterPerson getFilter() {
		return filter;
	}

	public void setFilter(FilterPerson filter) {
		this.filter = filter;
	}

	public String getFiltroNome() {
		return filtroNome;
	}

	public void setFiltroNome(String filtroNome) {
		this.filtroNome = filtroNome;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
}
