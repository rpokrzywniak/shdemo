package com.example.shdemo.service;


import com.example.shdemo.domain.Kawa;
import com.example.shdemo.domain.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans.xml" })
public class SellingManagerTestWithoutTransactional {

    private long KawaId;

    @Autowired
    SellingManager sellingManager;

    private final String NAME_1 = "Bolek";
    private final String PIN_1 = "1234";

    @Before
            public void setup()
    {
        List<Person> retrievedClients = sellingManager.getAllClients();

        // If there is a client with PIN_1 delete it
        for (Person client : retrievedClients) {
            if (client.getPin().equals(PIN_1)) {
                sellingManager.deleteClient(client);
            }
        }

        Kawa kawa = new Kawa();
        kawa.setWeight(500);
        kawa.setPrice(16.54);
        kawa.setSold(false);
        kawa.setName("Jacobs");

        sellingManager.addNewKawa(kawa);
        Person person = new Person();
        person.setFirstName(NAME_1);
        person.setPin(PIN_1);

        sellingManager.addClient(person);

        sellingManager.sellKawa(person.getId(), kawa.getId());

        KawaId = kawa.getId();
    }
    @After
    public void cleanup()
    {
        sellingManager.deleteKawas();
        sellingManager.deleteClients();
    }

    @Test
    public void LazyErrorCheck() {
        boolean pass = false;
        Person retrievedClient = sellingManager.findClientByPin(PIN_1);

        try {
            System.out.println(retrievedClient.getKawas().size());
        } catch (org.hibernate.LazyInitializationException e) {
            e.printStackTrace();
            pass = true;
        }

        if (!pass)
            org.junit.Assert.fail();

    }

    @Test
    public void EagerCheck()
    {
        Kawa retrievedKawa = sellingManager.getKawaById(KawaId);

        assertNotNull(retrievedKawa.getPerson().getPin());
    }
}
