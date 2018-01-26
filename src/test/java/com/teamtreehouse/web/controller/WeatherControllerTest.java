package com.teamtreehouse.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class WeatherControllerTest {

    //set mock environment - application context, manages bean definitions
    private MockMvc mockMvc;
    private WeatherController controller;

    @Before
    public void setup(){
        controller = new WeatherController();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void homeShouldRenderDetailView() throws Exception{
        mockMvc.perform(get("/"))
                .andExpect(view().name("weather/detail"));
    }

    @Test
    public void search_ShouldRedirectWithPathParam() throws Exception{
        mockMvc.perform(get("/search")
                .param("q", "60657"))
                .andExpect(redirectedUrl("/search/60657"));
    }




}
