package com.example.shdemo.service;

import java.util.List;

import com.example.shdemo.domain.Kawa;
import com.example.shdemo.domain.Person;

public interface SellingManager {
	
	void addClient(Person person);
	List<Person> getAllClients();
	void deleteClient(Person person);
	Person findClientByPin(String pin);
	
	Long addNewKawa(Kawa kawa);
	List<Kawa> getAvailableKawas();
	void disposeKawa(Person person, Kawa kawa);
	Kawa findKawaById(Long id);

	List<Kawa> getOwnedKawas(Person person);
	void sellKawa(Long personId, Long kawaId);

	Kawa getKawaById(long id);

	void deleteKawas();
	void deleteClients();

}
