package com.vania.services.implement;

import com.vania.entities.Menu;
import com.vania.entities.MenuCategory;
import com.vania.exceptions.BadRequestException;
import com.vania.exceptions.ForbiddenException;
import com.vania.repositories.MenuRepository;
import com.vania.services.MenuCategoryService;
import com.vania.services.MenuService;
import com.vania.services.FileService;

import com.vania.exceptions.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.io.IOException;
import java.math.BigDecimal;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuCategoryService menuCategoryService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FileService fileService;

    @Override
    public Menu getMenuById(String id) {
        if (!(menuRepository.findById(id).isPresent()))
            throw new NotFoundException("Menu with id : " + id + " is not found.");
        return menuRepository.findById(id).get();
    }

    @Override
    public List<Menu> getAllMenu() {
        return menuRepository.findAll();
    }

    @Override
    public Menu createMenu(Menu menu) {
        validatingMenuNameIsExist(menu.getMenuName());
        if(menu.getPrice().equals(new BigDecimal(0))) throw new ForbiddenException("Price can not be zero");
        validatingMenuNameEmpty(menu.getMenuName());
        validatingPriceEmpty(menu.getPrice());
        validatingMenuCategoryEmpty(menu.getIdMenuCategory());
        MenuCategory menuCategory = menuCategoryService.getMenuCategoryById(menu.getIdMenuCategory());
        menu.setMenuCategory(menuCategory);
        return menuRepository.save(menu);
    }

    @Override
    public Menu createMenuWithImage(String menuInput, MultipartFile image) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Menu menu = objectMapper.readValue(menuInput, Menu.class);
        menu = createMenu(menu);
        fileService.saveFile(image, menu.getIdMenu());
        return menu;
    }

    @Override
    public void deleteMenuById(String id) {
        getMenuById(id);
        menuRepository.deleteById(id);
    }

    @Override
    public Menu updateMenuWithImage(String menuInput, MultipartFile image) throws IOException {
        Menu menu = menuRepository.save(objectMapper.readValue(menuInput, Menu.class));
        validatingMenuNameEmpty(menu.getMenuName());
        validatingPriceEmpty(menu.getPrice());
        validatingMenuCategoryEmpty(menu.getIdMenuCategory());
        fileService.saveFile(image, menu.getIdMenu());
        return menu;
    }

    @Override
    public Menu updateMenu(Menu menu) {
        if(menu.getPrice().equals(new BigDecimal(0))) throw new ForbiddenException("Price can not be zero");
        validatingMenuNameEmpty(menu.getMenuName());
        validatingPriceEmpty(menu.getPrice());
        validatingMenuCategoryEmpty(menu.getIdMenuCategory());
        return menuRepository.save(menu);
    }

    private void validatingMenuNameIsExist(String value) {
        if (menuRepository.existsByMenuNameIsLike(value))
            throw new BadRequestException("Menu name with name : " + value + " already exist");
    }

    private void validatingMenuNameEmpty(String value) {
        if (value.isEmpty()) throw new BadRequestException("Menu name can't be empty");
    }

    private void validatingPriceEmpty(BigDecimal value) {
        if (value == null) throw new BadRequestException("Menu price can't be empty");
    }


    private void validatingMenuCategoryEmpty(String value) {
        if (value.isEmpty()) throw new BadRequestException("Menu Category can't be empty");
    }
}