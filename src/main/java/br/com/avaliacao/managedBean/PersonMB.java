package br.com.avaliacao.managedBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.WebTarget;

import br.com.avaliacao.model.Person;
import br.com.avaliacao.utils.FilterPerson;
import br.com.avaliacao.utils.LoadConfigs;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import br.com.avaliacao.utils.BaseBeans;

@ViewScoped
@ManagedBean(name="personMB")
public class PersonMB extends BaseBeans{
	private static final long serialVersionUID = 6409513922359907954L;
	
	private List<Person> persons;
	private WebTarget target;
	private FilterPerson filter;
	private String filtroNome;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy");
	
	private LazyDataModel<Person> model;
	Person person;
	
	@PostConstruct
	public void onLoad() {
		target = LoadConfigs.loadConfigs();
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
		System.out.println(filtroNome);
		model = new LazyDataModel<Person>(){
			private static final long serialVersionUID = 1L;
			
			@Override
			public List<Person> load(int first, int maxResults, String sortField,
					SortOrder sortOrder, Map<String, String> filters) {
				PersonMB personMB = new PersonMB();
				personMB.getFiltroNome();
				
				System.out.println("Nova requisição");
				
				validateEmptyFilters(sortOrder);
				
				StringBuilder uri = new StringBuilder();
				uri.append("/persons/findAll/");
				uri.append(maxResults + "/");
				uri.append(first + "/");
				uri.append(filter.getFilterName() + "/");
				uri.append(filter.getFilterCPF() + "/");
				uri.append(filter.getFilterEmail() + "/");
				uri.append(filter.getOrder() + "/");
				String dateFormat = null;
				if(filter.getFilterDate() != null){
					dateFormat = formatter.format(filter.getFilterDate());
				}
				uri.append(dateFormat);
				
				
				try{
					person = target.path(uri.toString()).request().get(Person.class);
				}catch(Exception ex){
					getMessageErrorConnect();
					System.out.println();
				}
				if(person != null && person.getPersons() != null){
					persons.removeAll(persons);
					persons.addAll(person.getPersons());
				}
				
				setRowCount(person.getTotalSize());
				
				return persons;
			}
			
			private void validateEmptyFilters(SortOrder sortOrder) {
				if(SortOrder.ASCENDING.equals(sortOrder)){
					filter.setOrder("ASC");
				}else{
					filter.setOrder("DESC");
				}
				
				if(filter.getFilterName() != null){
					if(filter.getFilterName().equals("")){
						filter.setFilterName(null);
					}
				}
				
				if(filter.getFilterCPF() != null){
					if(filter.getFilterCPF().equals("")){
						filter.setFilterCPF(null);
					}
				}
				
				if(filter.getFilterEmail() != null){
					if(filter.getFilterEmail().equals("")){
						filter.setFilterEmail(null);
					}
				}
			}
		};
	}
	
	private void getMessageErrorConnect() {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erro de conexão com o servidor!", null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void cleanFilters(){
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
