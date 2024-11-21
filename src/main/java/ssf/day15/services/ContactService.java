package ssf.day15.services;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssf.day15.models.Contact;
import ssf.day15.repositories.ContactRepository;

// Handles business logic/queries
@Service    
public class ContactService {
    @Autowired
    private ContactRepository contactRepo;

    public String insert(Contact contact) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        contact.setId(id);

        contactRepo.insertContact(contact);

        return id;
    }

    public Set<String> getContacts() {
        return contactRepo.getContactIDs();
    }

    //public Contact getContactByID(String id) {
    public Optional<Contact> getContactByID(String id) {
        return contactRepo.getContactByID(id);
    }
}
