package kdg.be;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClientApiApplicationTests {

    @Autowired
    MockMvc mockMvc;
    @Test
    void contextLoads() {
    }

    @Test
    void addCustomer() throws Exception {

        mockMvc.perform(post("/api/client").contentType(MediaType.APPLICATION_JSON).content(String.valueOf(5))).andExpect(status().isNotFound());



    }

}
