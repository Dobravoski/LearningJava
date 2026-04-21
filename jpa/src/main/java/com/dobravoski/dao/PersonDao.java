package com.dobravoski.dao;

import com.dobravoski.model.entities.Person;
import jakarta.persistence.EntityManager;

import java.util.List;

public class PersonDao {

    EntityManager em;

    public PersonDao(EntityManager em) {
        this.em = em;
    }

    public Person findById(Integer id) {
        return em.find(Person.class, id);
    }

    public List<Person> findAll() {
        return em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
    }

    public void insert(Person person) {
        em.getTransaction().begin();
        em.persist(person);
        em.getTransaction().commit();
    }

    public void update(Person person) {
        em.getTransaction().begin();
        em.merge(person);
        em.getTransaction().commit();
    }

    public void delete(Person person) {
        em.getTransaction().begin();
        Person managed = em.contains(person) ? person : em.merge(person);
        em.remove(managed);
        em.getTransaction().commit();
    }
}
