package pe.edu.upc.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="roles")
public class Role implements Serializable{

	private static final long serialVersionUID=1L;
	
	@Id
	@GeneratedValue(generator = "generador")
	@org.hibernate.annotations.GenericGenerator(
		name = "generador",
		strategy = "increment"
	)
	private int idRole;
	
	@Column(name="nameRole")
	private String nameRole;

	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Role(int idRole, String nameRole) {
		super();
		this.idRole = idRole;
		this.nameRole = nameRole;
	}

	public int getIdRole() {
		return idRole;
	}

	public void setIdRole(int idRole) {
		this.idRole = idRole;
	}

	public String getNameRole() {
		return nameRole;
	}

	public void setNameRole(String nameRole) {
		this.nameRole = nameRole;
	}

	
	
}
