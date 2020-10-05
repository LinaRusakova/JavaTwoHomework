package Homework3;

import java.util.*;

public class WordsArray {

    public static void main(String[] args) {
        String[] words = {"Ежевика", "Сендвич", "Наушники", "Собака", "Портвейн", "Барабаны", "Компьютер", "Листовка", "Учебник", "Дичь",
                "Кубик", "Доска", "Стакан", "Корова", "Гитара", "Джойстик", "Наушники", "Листовка", "Портвейн", "Наушники"};

        System.out.println(getStringIntegerHashMap(words).keySet());

        System.out.println(getStringIntegerHashMap(words));

    }

    private static HashMap<String, Integer> getStringIntegerHashMap(String[] words) {
        HashMap<String, Integer> wordsMap = new HashMap<>();

        for (String word : words) {
            if (!wordsMap.containsKey(word)) {
                wordsMap.put(word, 1);
            } else {
                wordsMap.replace(word, wordsMap.get(word), wordsMap.get(word) + 1);
            }
        }
        return wordsMap;
    }

}
