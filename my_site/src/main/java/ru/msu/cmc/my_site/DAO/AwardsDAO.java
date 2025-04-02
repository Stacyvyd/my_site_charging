package ru.msu.cmc.my_site.DAO;

import ru.msu.cmc.my_site.models.Awards;

import java.util.List;

public interface AwardsDAO extends CommonDAO<Awards, Long> {
    List<Awards> filterAwards(String namePart);
}