package com.company;

import org.paukov.combinatorics3.Generator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    final static int MIN_WEIGHT = 1;
    final static int MAX_WEIGHT = 10;

    final static int MIN_VALUE = 1;
    final static int MAX_VALUE = 10;

    final static int NUMBER_OF_ITEMS = 26;
    final static int MAX_TOTAL_WEIGHT = 9;

    public static void main(String[] args) {

        System.out.println(">>>>>>>>>>>>>>>>>> 1. generate new list of items");
        long startTime1 = System.nanoTime();
        List<Item> listOfAllItems = generateListOfItems(NUMBER_OF_ITEMS, MIN_WEIGHT, MAX_WEIGHT, MIN_VALUE, MAX_VALUE);
        System.out.println((System.nanoTime() - startTime1)/1000000000 + "s. \n");


        System.out.println(">>>>>>>>>>>>>>>>>> 2. find all combinations of items");
        long startTime2 = System.nanoTime();
        List<List<Item>> allCombinations = getAllCombinations(listOfAllItems);
        long time2 = System.nanoTime() - startTime2;
        System.out.println(time2/1000000000 + "s. \n");


        System.out.println(">>>>>>>>>>>>>>>>>> 3. find the best combination of items");
        long startTime3 = System.nanoTime();
        List<Item> betsKnapsack = findTheBestKnapsack(allCombinations);
        long time3 = System.nanoTime() - startTime3;
        System.out.println(time3/1000000000 + "s. \n");

        System.out.println(">>>>>>>>>>>>>>>>>> 4. print most valuable combination");
//        printKnapsack(betsKnapsack);
        printSumKnapsack(betsKnapsack);


        System.out.println(">>>>>>>>>>>>>>>>>> 5. do 2. and 3. in one stream to compare time");
        long startTime4 = System.nanoTime();
        printSumKnapsack(
                Generator
                .subset(listOfAllItems)
                .simple()
                .stream()
                .filter(l -> sumWeightOfKnapsack(l) <= MAX_TOTAL_WEIGHT)
                .max(Comparator.comparingInt(Main::sumValueOfKnapsack))
                .get()
        );
        long time4 = System.nanoTime() - startTime4;
        System.out.println((time4) /1000000000 + "s. \n");


        System.out.println(">>>>>>>>>>>>>>>>>> 6. time compare");
        System.out.println("step 2+3 -> " + (time2+time3)/1000000000 + "s. \n");
        System.out.println("step 4 -> " + (time4)/1000000000 + "s. \n");
        System.out.println("dif -> " + (time2 + time3 - time4)/1000000000 + "s. \n");


    }



    private static List<Item> findTheBestKnapsack(List<List<Item>> listOfAllCombinations) {
        List<Item> theBestCombination = listOfAllCombinations
                .stream()
                .filter(l -> sumWeightOfKnapsack(l) <= MAX_TOTAL_WEIGHT)
                .max(Comparator.comparingInt(Main::sumValueOfKnapsack))
                .get();

        System.out.println("combination found!");

        return theBestCombination;
    }

    private static List<List<Item>> getAllCombinations(List<Item> listOfAllItems) {
        List<List<Item>> allCombinations = Generator
                .subset(listOfAllItems)
                .simple()
                .stream()
                .collect(Collectors.toList());

        String pattern = "###,###.###";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String numberOfCombinations = decimalFormat.format(allCombinations.size());

        System.out.println(numberOfCombinations + " combinations generated");
        return allCombinations;
    }

    private static List<Item> generateListOfItems(int numberOfItems, int minWeight, int maxWeight, int minValue, int maxValue) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < numberOfItems; i++) {
            int weight = (int) Math.floor(Math.random() * (maxWeight - minWeight + 1) + minWeight);
            int value = (int) Math.floor(Math.random() * (maxValue - minValue + 1) + minValue);
            Item item = new Item(weight, value);
            items.add(item);
        }
        System.out.println(numberOfItems + " items generated");
        return items;
    }

    private static void printKnapsack(List<Item> knapsack) {
        for (Item item : knapsack) {
            System.out.println(item);
        }
    }

    private static void printSumKnapsack(List<Item> knapsack) {
        int sumWeight = sumWeightOfKnapsack(knapsack);
        int sumValue = sumValueOfKnapsack(knapsack);

        Item sumKnapsack = new Item(sumWeight, sumValue);
        System.out.println("-----------");
        System.out.println(sumKnapsack);
    }

    private static int sumWeightOfKnapsack(List<Item> knapsack) {
        return knapsack.stream().map(Item::getWeight).reduce(0, Integer::sum);
    }

    private static int sumValueOfKnapsack(List<Item> knapsack) {
        return knapsack.stream().map(Item::getValue).reduce(0, Integer::sum);
    }

}
        
    

