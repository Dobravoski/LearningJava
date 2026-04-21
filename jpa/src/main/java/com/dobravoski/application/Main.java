package com.dobravoski.application;

import com.dobravoski.dao.PersonDao;
import com.dobravoski.model.entities.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa");
        EntityManager em = emf.createEntityManager();
        Scanner sc = new Scanner(System.in);

        menu(em, sc);

        sc.close();
        em.close();
        emf.close();
    }

    public static void menu(EntityManager em, Scanner sc) {
        outer: while (true) {
            PersonDao personDao = new PersonDao(em);

            System.out.println("1 - Insert");
            System.out.println("2 - Update");
            System.out.println("3 - Find all");
            System.out.println("4 - Find by id");
            System.out.println("5 - Delete");
            System.out.println("6 - Exit");
            System.out.print(">> ");

            Person person;
            switch (Integer.parseInt(sc.nextLine())) {
                case 1:
                    System.out.print("Name: ");
                    String name = sc.nextLine();
                    System.out.print("Email: ");
                    String email = sc.nextLine();
                    person = new Person(name, email);
                    personDao.insert(person);
                    break;
                case 2:
                    personDao.findAll().forEach(System.out::println);
                    System.out.print(">> ");
                    person = personDao.findById(Integer.parseInt(sc.nextLine()));
                    System.out.println("1 - Name");
                    System.out.println("2 - Email");
                    switch (Integer.parseInt(sc.nextLine())) {
                        case 1:
                            System.out.print("Name: ");
                            person.setName(sc.nextLine());
                            break;
                        case 2:
                            System.out.print("Email: ");
                            person.setEmail(sc.nextLine());
                            break;
                    }
                    personDao.update(person);
                    break;
                case 3:
                    personDao.findAll().forEach(System.out::println);
                    break;
                case 4:
                    System.out.print("Id: ");
                    System.out.println(personDao.findById(Integer.parseInt(sc.nextLine())));
                    break;
                case 5:
                    personDao.findAll().forEach(System.out::println);
                    System.out.print(">> ");
                    personDao.delete(personDao.findById(Integer.parseInt(sc.nextLine())));
                    break;
                case 6:
                    break outer;
            }
        }
    }
}
