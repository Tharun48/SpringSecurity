package com.SpringSecuritysec1.Springsecurity.rest;

import com.SpringSecuritysec1.Springsecurity.model.Contact;
import com.SpringSecuritysec1.Springsecurity.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ContactController {

    @Autowired
    ContactRepository contactRepository;
    ContactController(ContactRepository contactRepository){
        this.contactRepository=contactRepository;
    }


    @GetMapping("/contact")
    public String getContactDetails() {
        return "Contact details of user";
    }

    @PostMapping("/contact")
    @PreFilter("filterObject.contactName!='test'")
    public List<Contact> createContact(@RequestBody List<Contact> contact) {
        if(!contact.isEmpty()) {
            List<Contact> contactList = new ArrayList<>();
            for(Contact c : contact) {
                Contact savedContact = contactRepository.save(c);
                contactList.add(savedContact);
            }
            return contactList;
        }return null;
    }
    /*
    @PostMapping("/contact")
    @PostFilter("filterObject.contactName!='test'")
    public List<Contact> createContact(@RequestBody List<Contact> contact) {
        if(!contact.isEmpty()) {
            List<Contact> contactList = new ArrayList<>();
            for(Contact c : contact) {
                Contact savedContact = contactRepository.save(c);
                contactList.add(savedContact);
            }
            return contactList;
        }return null;
    }
    */
}
