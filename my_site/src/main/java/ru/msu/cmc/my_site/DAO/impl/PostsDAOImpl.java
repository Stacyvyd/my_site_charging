package ru.msu.cmc.my_site.DAO.impl;

import org.springframework.stereotype.Repository;
import ru.msu.cmc.my_site.DAO.PostsDAO;
import ru.msu.cmc.my_site.models.Posts;

@Repository
public class PostsDAOImpl
        extends CommonDAOImpl<Posts, Long>
        implements PostsDAO {

    public PostsDAOImpl() {
        super(Posts.class);
    }
}