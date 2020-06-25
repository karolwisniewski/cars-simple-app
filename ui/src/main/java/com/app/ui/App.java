package com.app.ui;

import com.app.service.CarsService;
import com.app.ui.menu.MenuService;

public class App {
    public static void main(String[] args) {
        final String FILENAME = "init_data.json";
        CarsService carsService = new CarsService(FILENAME);
        MenuService menuService = new MenuService(carsService);
        menuService.showMainMenu();
    }
}
