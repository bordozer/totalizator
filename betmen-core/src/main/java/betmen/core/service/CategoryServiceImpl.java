package betmen.core.service;

import betmen.core.entity.Category;
import betmen.core.exception.UnprocessableEntityException;
import betmen.core.repository.CategoryDao;
import betmen.core.repository.jpa.CategoryJpaRepository;
import betmen.core.repository.jpa.FavoriteCategoryJpaRepository;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryRepository;
    @Autowired
    private CategoryJpaRepository categoryJpaRepository;
    @Autowired
    private FavoriteCategoryJpaRepository favoriteCategoryJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> loadAll() {
        return Lists.newArrayList(categoryRepository.loadAll());
    }

    @Override
    @Transactional
    public Category save(final Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Category load(final int id) {
        return categoryRepository.load(id);
    }

    @Override
    @Transactional
    public void delete(final int id) {
        if (!categoryJpaRepository.exists(id)) {
            throw new UnprocessableEntityException("Category does not exist");
        }
        favoriteCategoryJpaRepository.deleteAllByCategoryId(id);
        categoryRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Category findByName(final String categoryName) {
        return categoryRepository.findByName(categoryName);
    }

    @Override
    public List<Category> loadAll(final int sportKindId) {
        return categoryRepository.loadAll(sportKindId);
    }

    @Override
    public Category loadAndAssertExists(final int categoryId) {
        Category category = load(categoryId);
        if (category == null) {
            LOGGER.warn(String.format("Cannot get category with ID: %d", categoryId));
            throw new UnprocessableEntityException("League does not exist");
        }
        return category;
    }

    @Override
    public Comparator<Category> categoriesByFavoritesByName(final List<Category> favoriteCategories) {
        return (o1, o2) -> {
            if (favoriteCategories.contains(o1) && favoriteCategories.contains(o2)) {
                return o1.getCategoryName().compareToIgnoreCase(o2.getCategoryName());
            }
            if (favoriteCategories.contains(o1)) {
                return -1;
            }
            if (favoriteCategories.contains(o2)) {
                return 1;
            }
            return o1.getCategoryName().compareToIgnoreCase(o2.getCategoryName());
        };
    }

    @Override
    public Category findByCategoryName(final String categoryName) {
        return categoryJpaRepository.findByCategoryName(categoryName);
    }

    @Override
    public int categoriesCount(final int sportKindId) {
        return categoryJpaRepository.categoriesCount(sportKindId);
    }
}
