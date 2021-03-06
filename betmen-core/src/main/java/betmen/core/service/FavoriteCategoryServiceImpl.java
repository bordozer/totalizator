package betmen.core.service;

import betmen.core.entity.Category;
import betmen.core.entity.FavoriteCategory;
import betmen.core.entity.User;
import betmen.core.repository.FavoriteCategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteCategoryServiceImpl implements FavoriteCategoryService {

    @Autowired
    private FavoriteCategoryDao favoriteCategoryRepository;

    @Override
    @Transactional
    public void addToFavorites(final User user, final Category category) {
        if (isInFavorites(user, category)) {
            return;
        }
        favoriteCategoryRepository.save(new FavoriteCategory(user, category));
    }

    @Override
    @Transactional
    public void removeFromFavorites(final User user, final Category category) {
        if (!isInFavorites(user, category)) {
            return;
        }
        favoriteCategoryRepository.delete(favoriteCategoryRepository.find(user.getId(), category.getId()).getId());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isInFavorites(final User user, final Category category) {
        return isInFavorites(user.getId(), category.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isInFavorites(final int userId, final int categoryId) {
        return favoriteCategoryRepository.find(userId, categoryId) != null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> loadUserFavoriteCategories(final User user) {

        return favoriteCategoryRepository.loadAllForUser(user.getId()).stream()
                .map(favoriteCategory -> favoriteCategory.getCategory())
                .collect(Collectors.toList())
                ;
    }
}
