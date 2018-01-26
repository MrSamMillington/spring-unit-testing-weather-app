package com.teamtreehouse.dao;

//creating an application context as creating mocks for implementations is too time consuming
//this is an integration test

//DB unit injects test data

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.teamtreehouse.Application;
import com.teamtreehouse.domain.Favorite;
import com.teamtreehouse.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@SpringApplicationConfiguration(Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@DatabaseSetup("classpath:favorites.xml")
@TestExecutionListeners({   //testexecutionlistner defines listener api for reacting to test execution events. -> autowire dep injected from application
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
public class FavoriteDaoTest {

    @Autowired
    private FavoriteDao dao;

    @Before
    public void setup(){
        User user = new User();
        user.setId(1L);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null));
    }

    @Test
    public void findAll_ShouldReturnTwo() throws Exception{
        assertThat(dao.findAll(),hasSize(2));
    }

    @Test
    public void save_ShouldPersistEntity() throws Exception{
        String placeId = "Favplace#1";
        Favorite fav = new Favorite.FavoriteBuilder().withId(4L).withAddress("Fav").withPlaceId(placeId).build();
        dao.saveForCurrentUser(fav);
        assertThat(dao.findByPlaceId(placeId),notNullValue(Favorite.class));
    }

}
