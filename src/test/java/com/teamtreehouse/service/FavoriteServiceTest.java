package com.teamtreehouse.service;

import com.teamtreehouse.dao.FavoriteDao;
import com.teamtreehouse.domain.Favorite;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class FavoriteServiceTest {

    @Mock
    private FavoriteDao favoriteDao;

    @InjectMocks
    private FavoriteService favoriteService = new FavoriteServiceImpl();


    @Test
    public void findAll_ShouldReturnTwo() throws Exception{
        //Mock the DAO
        List<Favorite> favs = Arrays.asList(
                new Favorite(),
                new Favorite()
        );

        //Configure mock DAO to return the list
        when(favoriteDao.findAll()).thenReturn(favs);

        //Assert both returned
        assertEquals("findAll should return two favorites", 2, favoriteService.findAll().size());

        verify(favoriteDao).findAll();

    }

    @Test
    public void findById_ShouldReturnOne() throws Exception{

        //Mock the DAO to return a fav object when called
        when(favoriteDao.findOne(1L)).thenReturn(new Favorite());

        //assert it returns a fav
        assertThat(favoriteService.findById(1L), instanceOf(Favorite.class));

        verify(favoriteDao).findOne(1L);

    }

    //test is expected to throw exception
    @Test(expected = FavoriteNotFoundException.class)
    public void findById_ShouldThrowFavoriteNotFoundException(){
        when(favoriteDao.findOne(1L)).thenReturn(null);

        favoriteService.findById(1L);

        verify(favoriteService).findById(1L);

    }


}
