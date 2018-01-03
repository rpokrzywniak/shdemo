package com.example.shdemo.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "person.all", query = "Select p from Person p"),
	@NamedQuery(name = "person.byPin", query = "Select p from Person p where p.pin = :pin")
})
public class Person {

	public Person() {
	}

	private Long id;

	private String firstName = "unknown";
	private String pin = "";

	private List<Kawa> kawas;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(unique = true, nullable = false)
	public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}


	// Be careful here, both with lazy and eager fetch type
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
	public List<Kawa> getKawas() {
			return kawas;
	}

	public void setKawas(List<Kawa> kawas) {
		this.kawas = kawas;
	}
}
