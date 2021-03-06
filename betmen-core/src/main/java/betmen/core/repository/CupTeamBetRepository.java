package betmen.core.repository;

import betmen.core.entity.Cup;
import betmen.core.entity.CupTeamBet;
import betmen.core.entity.Team;
import betmen.core.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CupTeamBetRepository implements CupTeamBetDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<CupTeamBet> loadAll() {
        return em.createNamedQuery(CupTeamBet.LOAD_ALL, CupTeamBet.class)
                .getResultList();
    }

    @Override
    @Cacheable(value = CACHE_ENTRY, key = "#id")
    public CupTeamBet load(final int id) {
        return em.find(CupTeamBet.class, id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_ENTRY, key = "#entry.id")
            , @CacheEvict(value = CACHE_QUERY, allEntries = true)
    })
    public CupTeamBet save(final CupTeamBet entry) {
        return em.merge(entry);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_ENTRY, key = "#id")
            , @CacheEvict(value = CACHE_QUERY, allEntries = true)
    })
    public void delete(final int id) {
        em.remove(load(id));
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<CupTeamBet> load(final Cup cup, final User user) {
        return em.createNamedQuery(CupTeamBet.LOAD_ALL_FOR_CUP_AND_USER, CupTeamBet.class)
                .setParameter("cupId", cup.getId())
                .setParameter("userId", user.getId())
                .getResultList();
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public CupTeamBet load(final Cup cup, final User user, final int cupPosition) {
        final List<CupTeamBet> result = em.createNamedQuery(CupTeamBet.LOAD_ALL_FOR_CUP_AND_USER_AND_POSITION, CupTeamBet.class)
                .setParameter("cupId", cup.getId())
                .setParameter("userId", user.getId())
                .setParameter("cupPosition", cupPosition)
                .getResultList();

        return result.size() == 1 ? result.get(0) : null;
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public CupTeamBet load(final Cup cup, final Team team, final User user) {
        final List<CupTeamBet> result = em.createNamedQuery(CupTeamBet.LOAD_ALL_FOR_CUP_AND_TEAM_AND_USER, CupTeamBet.class)
                .setParameter("cupId", cup.getId())
                .setParameter("teamId", team.getId())
                .setParameter("userId", user.getId())
                .getResultList();

        return result.size() == 1 ? result.get(0) : null;
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<CupTeamBet> load(final Cup cup) {
        return em.createNamedQuery(CupTeamBet.LOAD_ALL_FOR_CUP, CupTeamBet.class)
                .setParameter("cupId", cup.getId())
                .getResultList();
    }

    @Override
    public void clearForCup(final Cup cup) {

        em.createQuery("delete from CupTeamBet where cupId= :cupId")
                .setParameter("cupId", cup.getId())
                .executeUpdate();
    }
}
