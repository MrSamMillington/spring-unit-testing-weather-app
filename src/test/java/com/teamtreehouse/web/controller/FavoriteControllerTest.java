package com.teamtreehouse.web.controller;


import com.teamtreehouse.domain.Favorite;
import com.teamtreehouse.service.FavoriteNotFoundException;
import com.teamtreehouse.service.FavoriteService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

import static com.teamtreehouse.domain.Favorite.FavoriteBuilder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FavoriteControllerTest {

    //need to mock services that the controllers rely on - use mockito or easymoc
    private MockMvc mockMvc;

    @InjectMocks
    private FavoriteController controller;

    @Mock
    private FavoriteService service;


    @Before
    public void setup(){

        //DO NOT INSTANTIATE A NEW FAV CONTROLLER!!!! YOU NEED THE @INJECTMOCKS CONTROLLER
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    //Triple A, arrange, act, assert

    @Test
    public void index_ShouldIncludeFavoritesInModel() throws Exception{
        // Arrange the mock behavior
        List<Favorite> favorites = Arrays.asList(
                new FavoriteBuilder(1L).withAddress("Chicago").withPlaceId("chicago1").build(),
                new FavoriteBuilder(2L).withAddress("Omaha").withPlaceId("omaha1").build()
        );
        when(service.findAll()).thenReturn(favorites);

        // Act (perform the MVC request) and Assert results
        mockMvc.perform(get("/favorites"))
                .andExpect(status().isOk())
                .andExpect(view().name("favorite/index"))
                .andExpect(model().attribute("favorites",favorites));
        verify(service).findAll();
    }

    @Test
    public void add_ShouldRedirectToNewFavorite() throws Exception{
        //Arrange the mock behavior
        //mocking the save method

        doAnswer(invocation -> {
            Favorite f = (Favorite)invocation.getArguments()[0];
            f.setId(1L);
            return null;
        }).when(service).save(any(Favorite.class)); //when save is called on ANY Favorite object


        //Act (perform the MVC request) and Assert results
        mockMvc.perform((post("/favorites").param("formattedAddress", "chicago", "il").param("placeId", "windycity")))
                .andExpect(redirectedUrl("/favorites/1"));


        verify(service).save(any(Favorite.class));
    }

    @Test
    public void detail_ShouldErrorOnNotFound() throws Exception{
        //Arrange the mock behavior
        //need to throw the Favnotfoundex
        when(service.findById(1L)).thenThrow(FavoriteNotFoundException.class);

        //Act (perform the MVC request) and Assert results
        mockMvc.perform(get("/favorites/1"))
                .andExpect(view().name("error"))
                .andExpect(model().attribute("ex",Matchers.instanceOf(FavoriteNotFoundException.class)));

        verify(service).findById(1L);
    }

    @Test
    public void detail_ShouldRedirectOnRemove() throws Exception{

        //when save is called on ANY Favorite object
        //dont need a when(...) as method is void

        //Act (perform the MVC request) and Assert results
        mockMvc.perform((post("/favorites/1/delete")))
                .andExpect(redirectedUrl("/favorites"));
        //verfiy service.delete(1L) is called
        verify(service).delete(1L);


    }



}
