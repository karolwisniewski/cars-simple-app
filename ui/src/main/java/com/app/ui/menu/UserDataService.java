package com.app.ui.menu;

import com.app.converter.model.enums.Color;
import com.app.service.enums.SortItem;
import com.app.ui.exception.UserDataServiceException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class UserDataService {

    private static final Scanner sc = new Scanner(System.in);

    public static String getString(String message) {
        System.out.println(message);
        return sc.nextLine();
    }

    public static int getInt(String message) {
        System.out.println(message);

        String value = sc.nextLine();
        if (!value.matches("\\d+")) {
            throw new UserDataServiceException("value is not a number");
        }
        return Integer.parseInt(value);
    }
    public static double getDouble(String message) {
        System.out.println(message);

        String value = sc.nextLine();
        if (!value.matches("\\d+")) {
            throw new UserDataServiceException("value is not a number");
        }
        return Double.parseDouble(value);
    }

    public static BigDecimal getBigDecimal(String message) {
        System.out.println(message);

        String value = sc.nextLine();
        if (!value.matches("\\d+(.\\d)?")) {
            throw new UserDataServiceException("value is not a number");
        }
        return new BigDecimal(value);
    }

    public static Color getColor() {
        AtomicInteger counter = new AtomicInteger(1);
        String message = Arrays
                .stream(Color.values())
                .map(color -> counter.getAndIncrement() + ". " + color)
                .collect(Collectors.joining("\n"));
        System.out.println(message);
        int choice = getInt("Choose color number: ");

        if (choice < 1 || choice > Color.values().length) {
            throw new UserDataServiceException("no color with number "+ choice);
        }

        return Color.values()[choice - 1];
    }

    public static SortItem getSortItem() {
        AtomicInteger counter = new AtomicInteger(1);
        String message = Arrays
                .stream(SortItem.values())
                .map(sortItem -> counter.getAndIncrement() + ". " + sortItem)
                .collect(Collectors.joining("\n"));
        System.out.println(message);
        int choice = getInt("Choose sort item number: ");

        if (choice < 1 || choice > SortItem.values().length) {
            throw new UserDataServiceException("no sort item with number "+ choice);
        }

        return SortItem.values()[choice - 1];
    }

    public static boolean getBoolean(String message) {
        System.out.println(message + "[Y/N]?");
        return sc.nextLine().toLowerCase().equals("y");
    }

    public void close() {
        if (sc != null) {
            sc.close();
        }
    }

}
