package br.com.avaliacao.utils;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.avaliacao.model.Person;

@FacesConverter("entityConverter")
public class EntityConverter implements Converter{
	
	private RestTemplate template = new RestTemplate();

	@Override
	public Person getAsObject(FacesContext context, UIComponent component, String value) {
		String hostPath = LoadConfigs.getHostPath();
		ResponseEntity<Person> respPersons = null;
		template = new RestTemplate();
		
		respPersons = template.getForEntity(hostPath + "/persons/name/" + value, Person.class);
		
		Person person = respPersons.getBody();
		return person;
		
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Person person = (Person) value;
            return person.getName();
        }
        else {
            return null;
        }
	}
	

}
