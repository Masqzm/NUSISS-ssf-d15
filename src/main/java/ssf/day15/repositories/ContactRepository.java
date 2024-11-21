package ssf.day15.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import ssf.day15.controller.ContactController;
import ssf.day15.models.Contact;

// Interfaces with db
@Repository     
public class ContactRepository {
    private final Logger logger = Logger.getLogger(ContactRepository.class.getName());

    // DependencyInject (DI) the RedisTemplate into ContactRepository
    @Autowired @Qualifier("redis-0")
    //private RedisTemplate<String, Object> template;
    private RedisTemplate<String, String> template;

    // Redis cmd:
    // hset abc123 name fred
    // hset abc123 email fred@gmail.com
    // hset abc123 phone 12341234
    public void insertContact(Contact contact) {
        // ListOperations<String, Object> listOps = template.opsForList();
        // ValueOperations<String, Object> valueOps = template.opsForValue();

        // <Key serializer, hash key, hash value>
        HashOperations<String, String, String> hashOps = template.opsForHash();
        // hashOps.put(contact.getId(), "name", contact.getName());
        // hashOps.put(contact.getId(), "email", contact.getEmail());
        // hashOps.put(contact.getId(), "phone", contact.getPhone());

        Map<String, String> values = new HashMap<>();
        values.put("name", contact.getName());
        values.put("email", contact.getEmail());
        values.put("phone", contact.getPhone());
        hashOps.putAll(contact.getId(), values);        // more efficient than individual hashOps.put() above
    }

    // Redis cmd:
    // keys *
    public Set<String> getContactIDs() {
        return template.keys("*");
    }

    // hgetall id
    public Optional<Contact> getContactByID(String id) {
        HashOperations<String, String, String> hashOps = template.opsForHash();
        Map<String, String> contact = hashOps.entries(id);

        if(contact.isEmpty())
            return Optional.empty();    

        Contact result = new Contact();
        result.setName(contact.get("name").toString());
        result.setEmail(contact.get("email").toString());
        result.setPhone(contact.get("phone").toString());
            
        return Optional.of(result);
    }
}
