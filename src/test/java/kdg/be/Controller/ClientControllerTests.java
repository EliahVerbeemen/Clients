package kdg.be.Controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.JsonPath;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.lessThan;
import static org.springframework.http.RequestEntity.patch;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ClientControllerTests {

    @Autowired
    MockMvc mockMvc;








    @Test
@Order(1)
    void test() throws Exception {
        mockMvc.perform(post("/api/order")
                       .contentType(MediaType.APPLICATION_JSON)
                      .content("{\"1\": 1}")
                       .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }



    @Test
    @Order(2)
    void placeOrders() throws Exception {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        mockMvc.perform(post("/api/order").content("{\"products\":{\"1\":500}}")

                        .with(jwt().authorities(new SimpleGrantedAuthority("user"))
                                .jwt(jwt -> jwt.claim(StandardClaimNames.EMAIL, "myTestEmail"))).accept(MediaType.APPLICATION_JSON)
                  .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
            .andExpect(jsonPath("$.discount").value(0))
                .andExpect(jsonPath("$.totalPrice").value(2500.0))
              .andExpect(jsonPath("$.orderDate").value(LocalDate.now().format(pattern)))
              .andExpect(jsonPath("$.orderStatus").value("Niet_bevestigd"))
              .andExpect(jsonPath("$.products",Matchers.hasEntry("1",500)));




    }





    @Test
    @Order(3)
    void repeatOrder() throws Exception {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        mockMvc.perform(post("/api/order/history/{orderId}",1)

                .with(jwt().authorities(new SimpleGrantedAuthority("user"))
                        .jwt(jwt -> jwt.claim(StandardClaimNames.EMAIL, "myTestEmail"))).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.discount").value(0))
                .andExpect(jsonPath("$.totalPrice").value(2500.0))
                .andExpect(jsonPath("$.orderDate").value(LocalDate.now().format(pattern)))
                .andExpect(jsonPath("$.orderStatus").value("Niet_bevestigd"))
                .andExpect(jsonPath("$.products",Matchers.hasEntry("1",500)));
    }


    @Test
@Order(4)
    void confirmOrder() throws Exception {

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

       mockMvc.perform(MockMvcRequestBuilders.patch("/api/order/confirm/{orderId}",1)

                .with(jwt().authorities(new SimpleGrantedAuthority("user"))
                        .jwt(jwt -> jwt.claim(StandardClaimNames.EMAIL, "myTestEmail"))).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(jsonPath("$.discount").value(0))
                       .andExpect(jsonPath("$.totalPrice").value(2500.0))
                        .andExpect(jsonPath("$.orderDate").value(LocalDate.now().format(pattern)))
                        .andExpect(jsonPath("$.orderStatus").value("Bevestigd"))
                        .andExpect(jsonPath("$.products",Matchers.hasEntry("1",500)));


    }







//TODO Loging, Transient, error
    //Mislukte cancel
@Test
@Order(5)
void cancelOrder() throws Exception {

    DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");



    mockMvc.perform(MockMvcRequestBuilders.patch("/api/order/cancel/{orderId}",2)

                    .with(jwt().authorities(new SimpleGrantedAuthority("user"))
                            .jwt(jwt -> jwt.claim(StandardClaimNames.EMAIL, "myTestEmail"))).accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
            .andExpect(jsonPath("$.discount").value(0))
            .andExpect(jsonPath("$.totalPrice").value(2500.0))
            .andExpect(jsonPath("$.orderDate").value(LocalDate.now().format(pattern)))
         //TODO leren spellen
            .andExpect(jsonPath("$.orderStatus").value("Geannulleerd"))
            .andExpect(jsonPath("$.products",Matchers.hasEntry("1",500)));


}


    @Test
    @Order(6)
    void orderAlreadyConfirmed() throws Exception {

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");



        mockMvc.perform(MockMvcRequestBuilders.patch("/api/order/cancel/{orderId}",1)

                        .with(jwt().authorities(new SimpleGrantedAuthority("user"))
                                .jwt(jwt -> jwt.claim(StandardClaimNames.EMAIL, "myTestEmail"))).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isConflict());




    }

    @Test
    @Order(6)
    void orderCanBeConfirmed() throws Exception {

        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");



    MvcResult mvcResult=    mockMvc.perform(MockMvcRequestBuilders.patch("/api/order/cancel/{orderId}",2)

                .with(jwt().authorities(new SimpleGrantedAuthority("user"))
                        .jwt(jwt -> jwt.claim(StandardClaimNames.EMAIL, "myTestEmail"))).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

System.out.println(mvcResult);


    }


    @Test
    @Order(7)
    void getClientWithToken() throws Exception {


  mockMvc.perform(get("/api/client").with(jwt().authorities(new SimpleGrantedAuthority("user"))
                .jwt(jwt -> jwt.claim(StandardClaimNames.EMAIL, "myTestEmail"))).accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andExpect(jsonPath("$.points").value(250)).andExpect(jsonPath("$.clientType").value("B2C")).andExpect(jsonPath("$.email").value("myTestEmail"))
                  .andExpect(jsonPath("$.email").value("myTestEmail")).andExpect(jsonPath("$.clientId",lessThan(3)));


    }

    @Test
    @Order(8)
    void getLoyality() throws Exception {


        MvcResult mvcResult = mockMvc.perform(get("/api/loyality").with(jwt().authorities(new SimpleGrantedAuthority("user"))
                .jwt(jwt -> jwt.claim(StandardClaimNames.EMAIL, "myTestEmail"))).accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());

    }





    @Test
    @Order(9)
    void getOrders() throws Exception {


       MvcResult mvcResult = mockMvc.perform(get("/api/order").with(jwt().authorities(new SimpleGrantedAuthority("user"))
                        .jwt(jwt -> jwt.claim(StandardClaimNames.EMAIL, "myTestEmail"))).accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk()).andReturn();
System.out.println(mvcResult.getResponse().getContentAsString());

    }



}