package kdg.be.Controller;

import com.jayway.jsonpath.JsonPath;
import kdg.be.Config.Configuration;
import kdg.be.Repositories.LoyaltyClassRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.CrossOrigin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientManagerControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    LoyaltyClassRepository repo;
    JsonPath jsonPath;

    @Test
    @WithMockUser(username = "clientmanager", password = "clientmanager", roles = "clientmanager")
    void test() throws Exception {
        mockMvc.perform(get("/api/internal/loyalty")
        ).andExpect(status().isOk());
    }


    @Test
    void PostClient() throws Exception {
        mockMvc.perform(post("/api/internal/orders/all")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    void showLoyaltyClassesShouldBeSecured() throws Exception {
        mockMvc.perform(get("/api/internal/loyalty").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "clientmanager", password = "clientmanager", roles = "clientmanager")
    void showLoyaltyClassesShouldShowAllClasses() throws Exception {
   mockMvc.perform(get("/api/internal/loyalty")
           .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));


    }
    @Test
    @WithMockUser(username = "clientmanager", password = "clientmanager", roles = "clientmanager")
    void createLoyaltyClassShouldCreateClass() throws Exception {
        JSONObject loyalty = new JSONObject();
        loyalty.put("name", "Diamond");
        loyalty.put("minimumPoints", 500000);
        loyalty.put("reduction", 0.5);
        mockMvc.perform(post("/api/internal/loyalty/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(loyalty.toString())
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$[5].name").value("Diamond"));;
    }

    @Test
    void allCustomersShouldBeSecured() throws Exception {
        mockMvc.perform(get("/api/internal/customers").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "clientmanager", password = "clientmanager", roles = "clientmanager")
    void allCustomersShouldShowAllCustomers() throws Exception {
        mockMvc.perform(get("/api/internal/customers").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void allProductsShouldBeSecured() throws Exception {
        mockMvc.perform(get("/api/internal/products/products").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "clientmanager", password = "clientmanager", roles = "clientmanager")
    void allProductsShouldShowAllProducts() throws Exception {
        mockMvc.perform(get("/api/internal/products/products").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void allOrdersShouldBeSecured() throws Exception {
        mockMvc.perform(get("/api/internal/orders/all").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "clientmanager", password = "clientmanager", roles = "clientmanager")
    void allOrdersShouldShowAllOrders() throws Exception {
        mockMvc.perform(get("/api/internal/orders/all").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

