package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    final static int MIN_WEIGHT = 1;
    final static int MAX_WEIGHT = 10;

    final static int MIN_VALUE = 1;
    final static int MAX_VALUE = 10;

    final static int NUMBER_OF_ITEMS = 24;
    final static int MAX_TOTAL_WEIGHT = 9;

    public static void main(String[] args) {

        System.out.println(">>>>>>>>>>>>>>>>>> 1. generate new list of items");
        long startTime = System.nanoTime();
        List<Item> listOfAllItems = generateListOfItems(NUMBER_OF_ITEMS, MIN_WEIGHT, MAX_WEIGHT, MIN_VALUE, MAX_VALUE);
        System.out.println((System.nanoTime() - startTime)/1000000000 + "s. \n");

        System.out.println(">>>>>>>>>>>>>>>>>> 2. generate all combinations of index");
        startTime = System.nanoTime();
        List<int[]> listOfAllIndexCombinations = getListOfAllIndexCombinations(NUMBER_OF_ITEMS);
        System.out.println((System.nanoTime() - startTime)/1000000000 + "s. \n");

        System.out.println(">>>>>>>>>>>>>>>>>> 3. generate all combinations of items");
        startTime = System.nanoTime();
        ArrayList<List<Item>> listOfAllItemsCombinations = getListOfAllItemsCombinations(listOfAllItems, listOfAllIndexCombinations);
        System.out.println((System.nanoTime() - startTime)/1000000000 + "s. \n");

        System.out.println(">>>>>>>>>>>>>>>>>> 4. generate all combinations with weight below maximum");
        startTime = System.nanoTime();
        List<List<Item>> listOfCorrectItemsCombinations = getListOfCorrectItemsCombinations(listOfAllItemsCombinations, MAX_TOTAL_WEIGHT);
        System.out.println((System.nanoTime() - startTime)/1000000000 + "s. \n");

        System.out.println(">>>>>>>>>>>>>>>>>> 5. find the best combination");
        startTime = System.nanoTime();
        List<Item> mostValuableCombination = findMaxValuableCombination(listOfCorrectItemsCombinations);
        System.out.println((System.nanoTime() - startTime)/1000000000 + "s. \n");

        System.out.println(">>>>>>>>>>>>>>>>>> 6. print most valuable combination");
        printSumKnapsack(mostValuableCombination);

    }

    private static List<Item> findMaxValuableCombination(List<List<Item>> listOfCorrectItemsCombinations) {
        List<Item> mostValuableCombination = listOfCorrectItemsCombinations.get(0);
        int mostValuableCombinationValue = 0;

        for (List<Item> combination : listOfCorrectItemsCombinations) {
            int sumValue = combination.stream().map(i -> i.getValue()).reduce(0, Integer::sum);

            if (sumValue > mostValuableCombinationValue) {
                mostValuableCombinationValue = sumValue;
                mostValuableCombination = combination;
            }
        }
        System.out.println("best value: " + mostValuableCombinationValue + "$");
        return mostValuableCombination;
    }

    private static List<List<Item>> getListOfCorrectItemsCombinations(List<List<Item>> listOfAllItemsCombinations, int maxTotalWeight) {
        List<List<Item>> correctCombinationsOfItems = listOfAllItemsCombinations
                .stream()
                .filter(l -> sumWeightOfKnapsack(l) <= maxTotalWeight)
                .collect(Collectors.toList());

        System.out.println(correctCombinationsOfItems.size() + " correct combinations of items");
        return correctCombinationsOfItems;
    }

    private static void printAllKnapsacks(List<List<Item>> listOfAllItemsCombinations) {
        for (List<Item> listOfItems : listOfAllItemsCombinations) {
            printKnapsack(listOfItems);
            System.out.println("");
        }
    }

    private static ArrayList<List<Item>> getListOfAllItemsCombinations(List<Item> listOfAllItems, List<int[]> listOfAllIndexCombinations) {
        ArrayList<List<Item>> listOfAllItemsCombinations = new ArrayList<>();
        int m =0;
        for (int[] listOfIndex : listOfAllIndexCombinations) {
            List<Item> listOfItems = new ArrayList<>();

            for (int index : listOfIndex) {
                listOfItems.add(listOfAllItems.get(index));
            }
            m++;
            if(m==1000000) {
                m=0;
                System.out.print("-");
            }
            listOfAllItemsCombinations.add(listOfItems);
        }
        System.out.println(listOfAllItemsCombinations.size() + " combinations of items");
        return listOfAllItemsCombinations;
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

    private static List<int[]> getListOfAllIndexCombinations(int numberOfItems) {
        List<int[]> listOfAllIndexCombinations = new ArrayList<>();
        for (int i=1; i<=numberOfItems; i++) {
            //generate list of combinations with i-elements
            List<int[]> listOfIndexCombinationsWithIElements = generate(numberOfItems, i);

            //add all combinations to the masterList
            for (int[] list : listOfIndexCombinationsWithIElements) {
                listOfAllIndexCombinations.add(list);
            }
        }
        System.out.println(listOfAllIndexCombinations.size() + " combinations of index");
        return listOfAllIndexCombinations;
    }

    private static void printAllCombinations(List<int[]> listOfAllIndexCombinations) {
        System.out.println("");
        for (int[] listOfLists : listOfAllIndexCombinations) {
            System.out.print("[");
            for (int element : listOfLists) {
                System.out.print(element + "-");
            }
            System.out.print("]");
            System.out.println("");
        }
    }

    public static List<int[]> generate(int n, int r) {
        List<int[]> combinations = new ArrayList<>();
        int[] combination = new int[r];

        // initialize with lowest lexicographic combination
        for (int i = 0; i < r; i++) {
            combination[i] = i;
        }

        while (combination[r - 1] < n) {
            combinations.add(combination.clone());

            // generate next combination in lexicographic order
            int t = r - 1;
            while (t != 0 && combination[t] == n - r + t) {
                t--;
            }
            combination[t]++;
            for (int i = t + 1; i < r; i++) {
                combination[i] = combination[i - 1] + 1;
            }
        }

        return combinations;
    }

    private static void printKnapsack(List<Item> knapsack) {
        for (Item item : knapsack) {
            System.out.print(item);
        }
    }

    private static void printSumKnapsack(List<Item> knapsack) {
        int sumWeight = 0;
        int sumValue = 0;
        for (Item item : knapsack) {
            System.out.println(item);
            sumWeight = sumWeight + item.getWeight();
            sumValue = sumValue + item.getValue();
        }
        Item sumKnapsack = new Item(sumWeight, sumValue);
        System.out.println("-----------");
        System.out.println(sumKnapsack);
    }

    private static int sumWeightOfKnapsack(List<Item> knapsack) {
        int sumWeight = 0;
        for (Item item : knapsack) {
            sumWeight = sumWeight + item.getWeight();
        }
        return sumWeight;
    }

}
        
    

