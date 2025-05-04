package ru.msu.cmc.my_site.DAO;

import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.models.Posts;


@Repository
public interface PostsDAO extends CommonDAO<Posts, Long>{
}