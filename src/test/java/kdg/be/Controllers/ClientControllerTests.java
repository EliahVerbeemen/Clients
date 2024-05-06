package kdg.be.Controllers;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTests {

    @Autowired
    MockMvc mockMvc;

    JsonPath jsonPath;

    @Test
    void GetCustomerShouldShowCustomer() throws Exception {
        mockMvc.perform(get("/api/klant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void CreateCustomerShouldCreateCustomer() throws Exception {
        mockMvc.perform(post("/api/klant")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"ClientType\":  \"B2C\", " +
                        "\"points\": 0}"))
                .andExpect(status().isCreated());
    }

    @Test
    void DeleteCustomerShouldDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/klant")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1}"))
                .andExpect(status().isOk());
    }
}
