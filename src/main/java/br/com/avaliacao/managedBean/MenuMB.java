//package br.com.avaliacao.managedBean;
//
//import java.io.IOException;
//
//import javax.annotation.PostConstruct;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.RequestScoped;
//
//import br.com.avaliacao.model.Person;
//import br.com.avaliacao.utils.BaseBeans;
//
//@RequestScoped
//@ManagedBean(name="menuMB")
//public class MenuMB extends BaseBeans{
//	
//	public String redirectCadPerson() throws IOException{
//		cleanPerson();
//		return "/public/cadPerson.faces?faces-redirect=true";
//	}
//
//	public String redirectListPerson() throws IOException{
//		cleanPerson();
//		return "/public/listPerson.faces?faces-redirect=true";
//	}
//	
//	public String redirectEditPerson(){
//		return "/public/cadPerson.faces?faces-redirect=true";
//	}
//	
//	private void cleanPerson() throws IOException {
//		PersonAddEditMB personAddEditMB = new PersonAddEditMB();
//		personAddEditMB.setPerson(new Person());
//		personAddEditMB.setPhoto(null);
//		personAddEditMB.getImagePhoto();
//		personAddEditMB.PersonAddEditMB();
//	}
//
//}
//
