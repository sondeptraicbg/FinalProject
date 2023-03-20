package com.project.swp.service;

import com.project.swp.entity.CategoryMenu;
import com.project.swp.entity.Menu;
import com.project.swp.entity.Restaurant;
import com.project.swp.repository.MenuRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuService {

    @Autowired
    private MenuRepo menuRepo;
    public List<Menu> getListHotFood() {
        return menuRepo.FindListHotFood();
    }

    public List<Menu> getListMenuByResId(int id) {
        return  menuRepo.findMenusByRestaurant_ResID(id);
    }

    public List<Menu> getAllListMenu() {
        return menuRepo.findAll();
    }

    public List<Menu> getListMenusBySearch(CategoryMenu categoryMenu, String foodName, String priceFromRaw, String priceToRaw, Restaurant restaurant){
        Double priceFrom, priceTo;
        if(priceFromRaw.equals(""))
            priceFrom = menuRepo.getMinPriceFood();
        else
            priceFrom = Double.valueOf(priceFromRaw);

        if(priceToRaw.equals(""))
            priceTo = menuRepo.getMaxPriceFood();
        else
            priceTo = Double.valueOf(priceToRaw);
        if(categoryMenu!= null)
            return  menuRepo.getMenusByCategoryMenuAndFoodNameContainingIgnoreCaseAndPriceBetweenAndRestaurant(categoryMenu, foodName, priceFrom, priceTo, restaurant);
        else
            return menuRepo.getMenusByFoodNameContainingIgnoreCaseAndPriceBetweenAndRestaurant(foodName, priceFrom, priceTo, restaurant);
    }

    public void save(Menu menu){
        menuRepo.save(menu);
    }

    public Menu getById(Integer id) throws Exception {
        Optional<Menu> result = menuRepo.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new Exception("Could not find and food with ID: " + id);
    }

    public Menu deleteById(Integer id) throws Exception {
        Optional<Menu> menuOptional = menuRepo.findById(id);
        if(menuOptional == null || menuOptional.isEmpty()){
            throw new Exception("Could not find and food with ID: " + id);
        }
        else{
            menuRepo.deleteById(id);
        }
        return null;
    }


}
