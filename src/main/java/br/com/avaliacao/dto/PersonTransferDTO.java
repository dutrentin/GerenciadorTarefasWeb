package br.com.avaliacao.dto;

import java.util.ArrayList;
import java.util.List;

public class PersonTransferDTO {
	
	private int total;
    private List<PersonDTO> persons;

    public PersonTransferDTO(){
        persons = new ArrayList<>();
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<PersonDTO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonDTO> persons) {
        this.persons = persons;
    }
	 
	 

	
}
