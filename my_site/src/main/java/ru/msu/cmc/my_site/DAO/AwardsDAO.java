package ru.msu.cmc.my_site.DAO;

import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.models.Awards;

import java.util.List;


@Repository
public interface AwardsDAO extends CommonDAO<Awards, Long> {
    List<Awards> filterAwards(String namePart);
}