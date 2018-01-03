package com.example.shdemo.service;

import java.util.ArrayList;
import java.util.List;

import com.example.shdemo.domain.Kawa;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.shdemo.domain.Person;

@Component
@Transactional
public class SellingMangerHibernateImpl implements SellingManager {

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void deleteClients() {
		sessionFactory.getCurrentSession().createQuery("delete from Person").executeUpdate();
	}

	@Override
	public void addClient(Person person) {
		person.setId(null);
		sessionFactory.getCurrentSession().persist(person);
	}
	
	@Override
	public void deleteClient(Person person) {
		person = (Person) sessionFactory.getCurrentSession().get(Person.class,
				person.getId());
		
		// lazy loading here
		for (Kawa kawa : person.getKawas()) {
			kawa.setSold(false);
			sessionFactory.getCurrentSession().update(kawa);
		}
		sessionFactory.getCurrentSession().delete(person);
	}

	@Override
	public List<Kawa> getOwnedKawas(Person person) {
		person = (Person) sessionFactory.getCurrentSession().get(Person.class,
				person.getId());
		// lazy loading here - try this code without (shallow) copying
		List<Kawa> kawas = new ArrayList<Kawa>(person.getKawas());
		return kawas;
	}

	@Override
	public Kawa getKawaById(long id) {
		Kawa kawa = (Kawa) sessionFactory.getCurrentSession().get(Kawa.class,
				id);

		return kawa;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Person> getAllClients() {
		return sessionFactory.getCurrentSession().getNamedQuery("person.all")
				.list();
	}

	@Override
	public Person findClientByPin(String pin) {
		 Person person = (Person) sessionFactory.getCurrentSession().getNamedQuery("person.byPin").setString("pin", pin).uniqueResult();
		 return person;

	}


	@Override
	public Long addNewKawa(Kawa kawa) {
		kawa.setId(null);
		return (Long) sessionFactory.getCurrentSession().save(kawa);
	}

	@Override
	public void sellKawa(Long personId, Long kawaId) {
		Person person = (Person) sessionFactory.getCurrentSession().get(
				Person.class, personId);
		Kawa kawa = (Kawa) sessionFactory.getCurrentSession()
				.get(Kawa.class, kawaId);
		kawa.setSold(true);
		kawa.setPerson(person);
		if(person.getKawas() == null)
		{
			person.setKawas(new ArrayList<Kawa>());
		}
		person.getKawas().add(kawa);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Kawa> getAvailableKawas() {
		return sessionFactory.getCurrentSession().getNamedQuery("monitor.available")
				.list();
	}

	@Override
	public void deleteKawas() {
		sessionFactory.getCurrentSession().createQuery("delete from Kawa").executeUpdate();
	}

	@Override
	public void disposeKawa(Person person, Kawa kawa) {

		person = (Person) sessionFactory.getCurrentSession().get(Person.class,
				person.getId());
		kawa = (Kawa) sessionFactory.getCurrentSession().get(Kawa.class,
				kawa.getId());

		Kawa toRemove = null;
		// lazy loading here (person.getkawas)
		for (Kawa aMonitor : person.getKawas())
			if (aMonitor.getId().compareTo(kawa.getId()) == 0) {
				toRemove = aMonitor;
				break;
			}

		if (toRemove != null)
			person.getKawas().remove(toRemove);

		kawa.setSold(false);
	}

	@Override
	public Kawa findKawaById(Long id) {
		return (Kawa) sessionFactory.getCurrentSession().get(Kawa.class, id);
	}

}
