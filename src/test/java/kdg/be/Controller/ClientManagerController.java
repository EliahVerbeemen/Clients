package kdg.be.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientManagerController {



    @Autowired
    MockMvc mockMvc;


    @Test
    @WithMockUser(username = "clientmanager", password = "clientmanager", roles = "clientmanager")

    void test() throws Exception {
        mockMvc.perform(get("/api/internal/loyality")
                //   .contentType(MediaType.APPLICATION_JSON)
                //    .content("{\"id\": 1}")
                //  .accept(MediaType.APPLICATION_JSON))
        ).andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "clientmanager", password = "clientmanager", roles = "clientmanager")

    void PostClient() throws Exception {
        mockMvc.perform(post("/api/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clientType\":\"B2B\",\"id\": 1,\"points\":50}")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.points").value(0))
                .andExpect(jsonPath("$.clientType").value("B2B"));

    }





}
