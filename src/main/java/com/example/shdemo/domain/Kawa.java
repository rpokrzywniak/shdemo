package com.example.shdemo.domain;

import javax.persistence.*;

@Entity
@NamedQueries({
		@NamedQuery(name = "kawa.available", query = "Select m from Kawa m where m.sold = false")
})
public class Kawa {

	private Long id;
	private Double price;
	private String name;
	private int weight;
	private boolean sold = false;
	private Person person;

	public Kawa() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public boolean isSold() {
		return sold;
	}

	public void setSold(boolean sold) {
		this.sold = sold;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(
			name = "PERSON_ID")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
}
