package Homework3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PhoneBook {
    private final HashMap<String, HashSet<String>> phoneBook;

    public PhoneBook() {
        this.phoneBook = new HashMap<>();
    }

    public PhoneBook(String secondName, String phoneNumber) {
        this.phoneBook = new HashMap<>();
        this.add(secondName, phoneNumber);
    }

    public void add(String secondName, String phoneNumber) {
        if (!this.phoneBook.containsKey(secondName)) {
            this.phoneBook.put(secondName, new HashSet<>(Arrays.asList(phoneNumber)));
        } else {
            Set<String> tempSet = this.phoneBook.get(secondName); //Так и не понял, как сделать запись добавления в одну строку, если это вообще возможно.
            tempSet.add(phoneNumber);
        }

    }

    public void get(String secondName) {
        for (String value : this.phoneBook.get(secondName)) {
            System.out.printf("Фамилия: %s, телефон: %s%n", secondName, value);
        }
    }

    @Override
    public String toString() {
        return "PhoneBook{" +
                "phoneBook=" + phoneBook +
                '}';
    }

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook("Шевченко", "+7(945)044-22-18");
        phoneBook.add("Сурикян", "+7(910)453-25-23");
        phoneBook.add("Васильев", "+7(955)453-23-25");
        phoneBook.add("Аркадийчев", "+7(983)442-12-53");
        phoneBook.add("Землянский", "+7(983)534-13-76");
        phoneBook.add("Протопопов", "+7(904)863-44-65");
        phoneBook.add("Васильев", "+7(922)258-17-16");
        phoneBook.add("Шевченко", "+7(956)835-94-86");
        phoneBook.add("Сурикян", "+7(904)338-43-83");
        phoneBook.add("Землянский", "+7(983)873-27-66");
        phoneBook.add("Егоров", "+7(933)445-33-93");

        phoneBook.get("Протопопов");
        phoneBook.get("Сурикян");
        phoneBook.get("Аркадийчев");
        phoneBook.get("Васильев");
    }
}
