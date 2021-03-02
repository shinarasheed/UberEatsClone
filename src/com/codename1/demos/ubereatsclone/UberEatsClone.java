/*
 * Copyright (c) 2012, Codename One and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Codename One designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Codename One through http://www.codenameone.com/ if you
 * need additional information or have any questions.
 */

package com.codename1.demos.ubereatsclone;

import com.codename1.demos.ubereatsclone.controllers.*;
import com.codename1.demos.ubereatsclone.interfaces.Account;
import com.codename1.demos.ubereatsclone.models.*;
import com.codename1.demos.ubereatsclone.views.*;
import com.codename1.io.Log;
import com.codename1.rad.controllers.ApplicationController;
import com.codename1.rad.controllers.ControllerEvent;
import com.codename1.rad.models.Entity;
import com.codename1.rad.nodes.ActionNode;
import com.codename1.rad.nodes.ViewNode;
import com.codename1.rad.ui.UI;
import com.codename1.ui.Button;
import com.codename1.ui.Display;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class UberEatsClone extends ApplicationController {

    Entity account;
    private static boolean isDarkMode = false;

    public static final ActionNode enterMainWindow = UI.action();
    public static final ActionNode enterFirstIntroduction = UI.action();
    public static final ActionNode enterSecondIntroduction = UI.action();
    public static final ActionNode enterThirdIntroduction = UI.action();
    public static final ActionNode enterSetLocation = UI.action();
    public static final ActionNode logout = UI.action();
    public static final ActionNode darkMode = UI.action();

    public static final ActionNode.Category SKIP_TO_MAIN_WINDOW = new ActionNode.Category();

    public static final int DEBUG_MODE_WITHOUT_SIGNING = 0;
    public static final int DEBUG_MODE_WITH_SIGNING = 1;

    public static final int mode = DEBUG_MODE_WITHOUT_SIGNING;

    @Override
    public void actionPerformed(ControllerEvent evt) {
        if (evt instanceof StartEvent) {
            evt.consume();

            ViewNode viewNode = new ViewNode(
                    UI.actions(SignInView.COMPLETE_SIGNING_IN, enterFirstIntroduction),
                    UI.actions(FirstIntroductionView.FINISHED_FIRST_INTRO, enterSecondIntroduction),
                    UI.actions(SecondIntroductionView.FINISHED_SECOND_INTRO, enterThirdIntroduction),
                    UI.actions(ThirdIntroductionView.FINISHED_THIRD_INTRO, enterSetLocation),
                    UI.actions(SetFirstLocationView.COMPLETE_SETTING_ADDRESS, enterMainWindow),
                    UI.actions(ProfileView.LOG_OUT, logout),
                    UI.actions(ProfileView.LOG_OUT, darkMode),
                    UI.actions(SKIP_TO_MAIN_WINDOW, enterMainWindow)
            );

            if (mode == DEBUG_MODE_WITH_SIGNING){

                account = new AccountModel();
                new AccountController(this, account, viewNode).getView().show();

                addActionListener(logout, event-> {
                    account = new AccountModel();
                    new AccountController(this, account, viewNode).getView().showBack();
                });

                addActionListener(enterFirstIntroduction, event->{
                    event.consume();
                    new FirstIntroductionController(this, account, viewNode).getView().show();
                });

                addActionListener(enterSecondIntroduction, event->{
                    event.consume();
                    new SecondIntroductionController(this, account, viewNode).getView().show();
                });

                addActionListener(enterThirdIntroduction, event->{
                    event.consume();
                    new ThirdIntroductionController(this, account, viewNode).getView().show();
                });

                addActionListener(enterSetLocation, event -> {
                    event.consume();
                    new SetFirstLocationController(this, account, viewNode).getView().show();
                });

                addActionListener(enterMainWindow, event->{
                    event.consume();
                    new MainWindowController(this, createDemoMainWindowEntity(account), viewNode).getView().show();
                });


            }else if (mode == DEBUG_MODE_WITHOUT_SIGNING){
                new MainWindowController(this, createDemoMainWindowEntity(null), viewNode).getView().show();
            }
        }
    }

    private Entity createDemoMainWindowEntity(Entity accountEntity){
        Entity account;
        if (accountEntity == null){
             account = new AccountModel();
            account.set(Account.firstName, "Codename");
            account.set(Account.lastName, "One");
            account.set(Account.emailAddress, "sergey@gmail.com");
            account.set(Account.password, "sd12eqwf134qsd");
            account.set(Account.mobileNumber, "0544123415");
        }else{
            account = accountEntity;
        }
        List restaurantsList = new ArrayList();
        for (int i = 0; i < 10; i++){
            restaurantsList.add(createRestaurantDemoModel());
        }
        return new MainWindowModel(account, restaurantsList);
    }

    private Entity createRestaurantDemoModel(){
        RestaurantModel restaurant = new RestaurantModel("Sergio's Pizza", "https://sergeycodenameone.github.io/restaurant.png", "Italian", 4.7, 5, "https://sergeycodenameone.github.io/restaurant-icon.png",30, createDemoMenu());
        return restaurant;
    }

    private List<Entity> createDemoMenu(){
        List<Entity> menu = new ArrayList<>();

        menu.add(new FoodCategoryModel("Pizzas", createPizzaMenu()));
        menu.add(new FoodCategoryModel("Pastas", createPastaMenu()));
        menu.add(new FoodCategoryModel("Desserts", createDessertsMenu()));
        menu.add(new FoodCategoryModel("Drinks", createDrinksMenu()));
        return menu;
    }

    private List<Entity> createPastaMenu(){
        List<Entity> dishes = new ArrayList<>();
        dishes.add(new DishModel("Ravioli", "Ravioli pasta", "https://sergeycodenameone.github.io/pasta-image.jpg", 4, createDemoAddOns()));
        dishes.add(new DishModel("Fettuccine", "Fettuccine pasta", "https://sergeycodenameone.github.io/pasta-image.jpg", 5, createDemoAddOns()));
        dishes.add(new DishModel("Linguini", "Linguini pasta", "https://sergeycodenameone.github.io/pasta-image.jpg", 2, createDemoAddOns()));
        dishes.add(new DishModel("Rotelle", "Rotelle pasta", "https://sergeycodenameone.github.io/pasta-image.jpg", 3, createDemoAddOns()));
        dishes.add(new DishModel("Ditalini", "Ditalini pasta", "https://sergeycodenameone.github.io/pasta-image.jpg", 4.5, createDemoAddOns()));
        dishes.add(new DishModel("Tortellini", "Tortellini pasta", "https://sergeycodenameone.github.io/pasta-image.jpg", 6.4, createDemoAddOns()));

        return dishes;
    }

    private List<Entity> createDrinksMenu(){
        List<Entity> dishes = new ArrayList<>();
        dishes.add(new DishModel("Cola", "Cola with ice", "https://sergeycodenameone.github.io/orange-juice.jpg", 4.60, createDemoAddOns()));
        dishes.add(new DishModel("Soda", "Soda with ice", "https://sergeycodenameone.github.io/orange-juice.jpg", 4.60, createDemoAddOns()));
        dishes.add(new DishModel("Orange Juice", "Orange Juice with ice", "https://sergeycodenameone.github.io/orange-juice.jpg", 4.60, createDemoAddOns()));
        dishes.add(new DishModel("Apple juice", "Apple juice with ice", "https://sergeycodenameone.github.io/orange-juice.jpg", 4.60, createDemoAddOns()));
        dishes.add(new DishModel("Vanilla Milkshake", "Vanilla Milkshake with cream", "https://sergeycodenameone.github.io/orange-juice.jpg", 4.60, createDemoAddOns()));
        dishes.add(new DishModel("Chocolate Milkshake", "Chocolate Milkshake with cream", "https://sergeycodenameone.github.io/orange-juice.jpg", 4.60, createDemoAddOns()));

        return dishes;
    }

    private List<Entity> createDessertsMenu(){
        List<Entity> dishes = new ArrayList<>();
        dishes.add(new DishModel("Apple Pie", "Apple Pie with ice-cream", "https://sergeycodenameone.github.io/pancakes-image.jpg", 4.60, createDemoAddOns()));
        dishes.add(new DishModel("Chocolate Cake", "Chocolate Cake with ice-cream", "https://sergeycodenameone.github.io/pancakes-image.jpg", 4.60, createDemoAddOns()));
        dishes.add(new DishModel("Pancakes", "Pancakes with ice-cream", "https://sergeycodenameone.github.io/pancakes-image.jpg", 4.60, createDemoAddOns()));
        dishes.add(new DishModel("Cupcakes", "Cupcakes with ice-cream", "https://sergeycodenameone.github.io/pancakes-image.jpg", 4.60, createDemoAddOns()));
        dishes.add(new DishModel("Vanilla Ice Cream", "Vanilla Ice Cream", "https://sergeycodenameone.github.io/pancakes-image.jpg", 4.60, createDemoAddOns()));
        dishes.add(new DishModel("Chocolate Ice Cream", "Chocolate Ice Cream", "https://sergeycodenameone.github.io/pancakes-image.jpg", 4.60, createDemoAddOns()));

        return dishes;
    }

    private List<Entity> createPizzaMenu(){
        List<Entity> dishes = new ArrayList<>();
        dishes.add(new DishModel("Neapolitan Pizza", "Large pizza", "https://sergeycodenameone.github.io/pizza-image.jpg", 4.60, createDemoAddOns()));
        dishes.add(new DishModel("Chicago Pizza", "Medium pizza", "https://sergeycodenameone.github.io/pizza-image.jpg", 4.60, createDemoAddOns()));
        dishes.add(new DishModel("New York-Style Pizza", "Small pizza", "https://sergeycodenameone.github.io/pizza-image.jpg", 4.60, createDemoAddOns()));
        dishes.add(new DishModel("Sicilian Pizza", "Medium pizza", "https://sergeycodenameone.github.io/pizza-image.jpg", 4.60, createDemoAddOns()));
        dishes.add(new DishModel("Greek Pizza", "Small pizza", "https://sergeycodenameone.github.io/pizza-image.jpg", 4.60, createDemoAddOns()));
        dishes.add(new DishModel("California Pizza", "Medium pizza", "https://sergeycodenameone.github.io/pizza-image.jpg", 4.60, createDemoAddOns()));

        return dishes;
    }


    private List<Entity> createDemoAddOns(){
        List addOns = new ArrayList();

        addOns.add(new DishAddOnModel("mushrooms", "https://sergeycodenameone.github.io/mushrooms-image.jpg", 4.0));
        addOns.add(new DishAddOnModel("tomato", "https://sergeycodenameone.github.io/tomato-image.jpg", 2.0));
        addOns.add(new DishAddOnModel("cheese", "https://sergeycodenameone.github.io/cheese-image.jpg", 3.0));
        addOns.add(new DishAddOnModel("onion", "https://sergeycodenameone.github.io/onion-image.jpg", 1.0));

        return addOns;
    }

    private void initTheme() {
        darkMode = !darkMode;
        String themeFileName = darkMode ? "/dark-theme" : "/theme";
        try {
            Resources theme = Resources.openLayered(themeFileName);
            UIManager.getInstance().addThemeProps(theme.getTheme(theme.getThemeResourceNames()[0]));
        }catch(IOException e){
            Log.e(e);
        }
        Button darkModeCmd = Display.getInstance().getCurrent().getToolbar().findCommandComponent(darkModeCommand);
        if(darkMode){
            darkModeCmd.setIcon(darkModeImageDark);
        }else{
            darkModeCmd.setIcon(darkModeImageLight);
        }

        ClockDemo.refreshClockColor();
        Display.getInstance().getCurrent().refreshTheme();
    }
}
