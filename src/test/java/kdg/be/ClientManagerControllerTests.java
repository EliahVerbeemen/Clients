package kdg.be;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import com.jayway.jsonpath.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class ClientManagerControllerTests {

    @Autowired
    MockMvc mockMvc;

    JsonPath jsonPath;

    @Test
    void showLoyalityClassesShouldBeSecured() throws Exception {
        mockMvc.perform(get("/loyality").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "clientmanager", password = "clientmanager", roles = "clientmanager")
    void showLoyalityClassesShouldShowAllClasses() throws Exception {
        mockMvc.perform(get("/loyality").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void allCustomersShouldBeSecured() throws Exception {
        mockMvc.perform(get("/customers").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "clientmanager", password = "clientmanager", roles = "clientmanager")
    void allCustomersShouldShowAllCustomers() throws Exception {
        mockMvc.perform(get("/customers").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void allProdcutsShouldBeSecured() throws Exception {
        mockMvc.perform(get("/products/products").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "clientmanager", password = "clientmanager", roles = "clientmanager")
    void allProductsShouldShowAllProducts() throws Exception {
        mockMvc.perform(get("/products/products").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void allOrdersShouldBeSecured() throws Exception {
        mockMvc.perform(get("/orders/all").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "clientmanager", password = "clientmanager", roles = "clientmanager")
    void allOrdersShouldShowAllOrders() throws Exception {
        mockMvc.perform(get("/orders/all").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
