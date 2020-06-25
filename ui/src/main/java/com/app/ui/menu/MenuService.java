package com.app.ui.menu;

import com.app.converter.model.Car;
import com.app.converter.model.enums.Color;
import com.app.service.CarsService;
import com.app.service.enums.SortItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.app.ui.menu.UserDataService.*;

@RequiredArgsConstructor
public class MenuService {

    private final CarsService carsService;

    private int printMenu() {
        System.out.println("1. Show all cars");
        System.out.println("2. Sort collection by chosen item");
        System.out.println("3. Get cars with mileage value largest that entered");
        System.out.println("4. Get map with colour key and value as collection od cars with this colour");
        System.out.println("5. Get map with model key and value ase most expensive car object");
        System.out.println("6. Get statistic for price and mileage");
        System.out.println("7. Get most expensive car");
        System.out.println("8. Get car collection with sorted components list");
        System.out.println("9. Get map with component key and value as cars collection which has this component ");
        System.out.println("10. Get cars with price between entered range");
        System.out.println("11. Exit");
        return getInt("Choose option:");
    }

    public void showMainMenu() {
        do {
            int choice = printMenu();
            switch (choice) {
                case 1 -> display();
                case 2 -> sortByItem();
                case 3 -> withLargestMileage();
                case 4 -> colourCarsCollectionMap();
                case 5 -> modelMostExpensiveCarMap();
                case 6 -> priceAndMileageStatistic();
                case 7 -> mostExpensiveCar();
                case 8 -> sortComponents();
                case 9 -> componentCarsCollectionMap();
                case 10 -> carsInRange();
                case 11 -> {
                    System.out.println("Have a nice day");
                    return;
                }
                default -> System.out.println("No option with number " + choice);
            }
        } while (true);
    }

    private void display() {
        System.out.println(carsService);
    }

    private void sortByItem() {
        SortItem sortItem = UserDataService.getSortItem();
        boolean descending = UserDataService.getBoolean("Descending");

        var sortedItems = carsService.sortBy(sortItem, descending);
        System.out.println(toJson(sortedItems));
    }

    private void withLargestMileage() {
        double mileage = getDouble("Enter lowest mileage value");

        List<Car> cars =  carsService.withLargestMileage(mileage);
        System.out.println(toJson(cars));
    }
    private void colourCarsCollectionMap(){
        Map<Color, Long> colourCarsMap = carsService.colorRepetitions();
        System.out.println(toJson(colourCarsMap));
    }

    private void modelMostExpensiveCarMap(){
         Map<String, Car> modelCarMap =  carsService.biggestPrice();
        System.out.println(toJson(modelCarMap));
    }

    private void priceAndMileageStatistic(){
        System.out.println(carsService.statistics());
    }

    private void mostExpensiveCar (){
        List<Car> cars = carsService.mostExpensive();
        System.out.println(toJson(cars));
    }

    private void sortComponents(){
        List<Car> cars = carsService.sortComponentsList();
        System.out.println(toJson(cars));
    }

    private static <T> String toJson(T element) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(element);
    }

    private void componentCarsCollectionMap(){
        Map<String, List<Car>> componentCarsMap = carsService.componentsWithCarList();
        System.out.println(toJson(componentCarsMap));
    }

    private void carsInRange(){
        BigDecimal min = getBigDecimal("Enter min value");
        BigDecimal max = getBigDecimal("Enter max value");
        List<Car> cars = carsService.priceInRange(min, max) ;
        System.out.println(toJson(cars));
    }

}
