package com.vania.services.implement;

import com.vania.entities.MenuCategory;
import com.vania.exceptions.BadRequestException;
import com.vania.exceptions.NotFoundException;
import com.vania.repositories.MenuCategoryRepository;
import com.vania.services.MenuCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuCategoryServiceImpl implements MenuCategoryService {

    @Autowired
    MenuCategoryRepository menuCategoryRepository;

    @Override
    public List<MenuCategory> getAllMenuCategory() {
        return menuCategoryRepository.findAll();
    }

    @Override
    public MenuCategory createMenuCategory(MenuCategory menuCategory) {
        validatingMenuCategoryNameEmpty(menuCategory.getCategoryName());
        validatingMenuCategoryNameIsExist(menuCategory.getCategoryName());
        return menuCategoryRepository.save(menuCategory);
    }

    @Override
    public void deleteMenuCategoryById(String id) {
        getMenuCategoryById(id);
        menuCategoryRepository.deleteById(id);
    }

    @Override
    public MenuCategory updateMenuCategory(MenuCategory menuCategory) {
        validatingMenuCategoryNameEmpty(menuCategory.getCategoryName());
        return menuCategoryRepository.save(menuCategory);
    }

    @Override
    public MenuCategory getMenuCategoryById(String id) {
        if (!(menuCategoryRepository.findById(id).isPresent())) throw new NotFoundException("Menu category with id : " + id + " is not found.");
        return menuCategoryRepository.findById(id).get();
    }

    private void validatingMenuCategoryNameEmpty(String value) {
        if (value.isEmpty()) throw new BadRequestException("Category name can't be empty");
    }

    private void validatingMenuCategoryNameIsExist(String value) {
        if (menuCategoryRepository.existsByCategoryNameIsLike(value)) throw new BadRequestException("Category name with name : " + value + " is already exists");
    }
}
