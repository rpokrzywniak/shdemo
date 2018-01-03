package com.example.shdemo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import com.example.shdemo.domain.Kawa;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.example.shdemo.domain.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class SellingManagerTest {

	@Autowired
	SellingManager sellingManager;

	private final String NAME_1 = "Bolek";
	private final String PIN_1 = "1234";

	private final String NAME_2 = "Lolek";
	private final String PIN_2 = "4321";


	private final String NAMEK_1 = "JACOBS";
	private final Double PRICE_1 = 16.23;
	private final int WEIGHT_1= 500;

	private final String NAMEK_2 = "TCHIBO";
	private final Double PRICE_2 = 14.67;
	private final int WEIGHT_2= 1000;


	@Test
	public void addClientCheck() {

		List<Person> retrievedClients = sellingManager.getAllClients();

		// If there is a client with PIN_1 delete it
		for (Person client : retrievedClients) {
			if (client.getPin().equals(PIN_1)) {
				sellingManager.deleteClient(client);
			}
		}

		Person person = new Person();
		person.setFirstName(NAME_1);
		person.setPin(PIN_1);
		// ... other properties here

		// Pin is Unique
		sellingManager.addClient(person);

		Person retrievedClient = sellingManager.findClientByPin(PIN_1);

		assertEquals(NAME_1, retrievedClient.getFirstName());
		assertEquals(PIN_1, retrievedClient.getPin());
		// ... check other properties here
	}

	@Test
	public void addKawaCheck() {

		Kawa kawa = new Kawa();
		kawa.setWeight(WEIGHT_1);
		kawa.setPrice(PRICE_1);
		kawa.setName(NAMEK_1);
		// ... other properties here

		Long kawaId = sellingManager.addNewKawa(kawa);

		Kawa retrievedKawa = sellingManager.findKawaById(kawaId);
		assertEquals(PRICE_1, retrievedKawa.getPrice());
		assertEquals(WEIGHT_1, retrievedKawa.getWeight());
		assertEquals(NAMEK_1, retrievedKawa.getName());
		// ... check other properties here

	}

	@Test
	public void sellKawaCheck() {

		Person person = new Person();
		person.setFirstName(NAME_2);
		person.setPin(PIN_2);

		sellingManager.addClient(person);

		Person retrievedPerson = sellingManager.findClientByPin(PIN_2);

		Kawa kawa = new Kawa();
		kawa.setPrice(PRICE_2);
		kawa.setWeight(WEIGHT_2);
		kawa.setName(NAMEK_2);

		Long kawaId = sellingManager.addNewKawa(kawa);

		sellingManager.sellKawa(retrievedPerson.getId(), kawaId);

		List<Kawa> ownedKawas = sellingManager.getOwnedKawas(retrievedPerson);

		assertEquals(1, ownedKawas.size());
		assertEquals(WEIGHT_2, ownedKawas.get(0).getWeight());
		assertEquals(PRICE_2, ownedKawas.get(0).getPrice());
		assertEquals(NAMEK_2, ownedKawas.get(0).getName());
	}

	@Test
	public void checkBidirectional()
	{
		Person person = new Person();
		person.setFirstName(NAME_2);
		person.setPin(PIN_2);

		sellingManager.addClient(person);

		Kawa kawa = new Kawa();
		kawa.setPrice(PRICE_2);
		kawa.setWeight(WEIGHT_2);
		kawa.setName(NAMEK_2);

		Long kawaId = sellingManager.addNewKawa(kawa);

		sellingManager.sellKawa(person.getId(), kawaId);

		Person kawaOwner = sellingManager.getKawaById(kawaId).getPerson();

		assertNotNull(kawaOwner);
		assertEquals(NAME_2, kawaOwner.getFirstName());
		assertEquals(PIN_2, kawaOwner.getPin());
	}

}
