package com.example.businessLogic.functionality;

import com.example.businessLogic.dtos.lessor.LessorPostDto;
import com.example.businessLogic.models.Lessor;
import com.example.businessLogic.services.interfaces.LessorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LessorRestAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private LessorService lessorService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void givenAuthAdminRequestOnGetAllLessors_shouldSucceedWith200() throws Exception {
        lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        lessorService.create(new LessorPostDto("lessorB", "lessor2@email.com", "+380987654562"));
        lessorService.create(new LessorPostDto("lessorC", "lessor3@email.com", "+380987654563"));
        mockMvc.perform(get("/api/lessors").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void givenAuthAdminRequestOnGetLessorById_shouldSucceedWith200() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        mockMvc.perform(get("/api/lessors/"+l.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("lessorA"))
                .andExpect(jsonPath("$.email").value("lessor1@email.com"))
                .andExpect(jsonPath("$.phone").value("+380987654561"))
                .andExpect(jsonPath("$.id").value(l.getId()));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void givenAuthAdminRequestOnGetLessorById_whenIncorrectId_shouldFailWith400() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        mockMvc.perform(get("/api/lessors/"+l.getId()+1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void givenAuthAdminRequestOnPostLessor_shouldSucceedWith200() throws Exception {
        LessorPostDto l = new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(l);
        mockMvc.perform(post("/api/lessors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(l.getName()))
                .andExpect(jsonPath("$.email").value(l.getEmail()))
                .andExpect(jsonPath("$.phone").value(l.getPhone()))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void givenAuthAdminRequestOnPostLessor_whenEmailExists_shouldFailWith400() throws Exception {
        lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        LessorPostDto l = new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(l);
        mockMvc.perform(post("/api/lessors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void givenAuthAdminRequestOnPostLessor_whenIncorrectName_shouldFailWith400() throws Exception {
        LessorPostDto l = new LessorPostDto("lessorA 12345", "lessor1@email.com", "+380987654561");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(l);
        mockMvc.perform(post("/api/lessors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void givenAuthAdminRequestOnPostLessor_whenNameAbsent_shouldFailWith400() throws Exception {
        String json = "{\"email\":\"email@gmail.com\",\"phone\":\"+059089760213\"}";
        mockMvc.perform(post("/api/lessors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void givenAuthAdminRequestOnPostLessor_whenIncorrectEmail_shouldFailWith400() throws Exception {
        LessorPostDto l = new LessorPostDto("lessorA", "lessor1email.com", "+380987654561");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(l);
        mockMvc.perform(post("/api/lessors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void givenAuthAdminRequestOnPostLessor_whenEmailAbsent_shouldFailWith400() throws Exception {
        String json = "{\"name\":\"lessorA\",\"phone\":\"+059089760213\"}";
        mockMvc.perform(post("/api/lessors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void givenAuthAdminRequestOnPostLessor_whenIncorrectPhone_shouldFailWith400() throws Exception {
        LessorPostDto l = new LessorPostDto("lessorA", "lessor1@email.com", "1234-1234");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(l);
        mockMvc.perform(post("/api/lessors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void givenAuthAdminRequestOnPostLessor_whenPhoneAbsent_shouldFailWith400() throws Exception {
        String json = "{\"email\":\"email@gmail.com\",\"name\":\"lessorA\"}";
        mockMvc.perform(post("/api/lessors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json).characterEncoding(Charset.defaultCharset()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void givenAuthAdminRequestOnPutLessor_shouldSucceedWith200() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        LessorPostDto upd = new LessorPostDto("lessorABC", "lessor2@email.com", "+380987654561");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(upd);
        mockMvc.perform(put("/api/lessors/"+l.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(upd.getName()))
                .andExpect(jsonPath("$.email").value(upd.getEmail()))
                .andExpect(jsonPath("$.phone").value(upd.getPhone()))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void givenAuthAdminRequestOnDeleteLessorById_shouldSucceedWith200() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        mockMvc.perform(delete("/api/lessors/"+l.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void givenAuthAdminRequestOnDeleteLessorById_whenIncorrectId_shouldSucceedWith200() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        mockMvc.perform(delete("/api/lessors/"+l.getId()+1))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void givenAuthAdminRequestOnDeleteAllLessors_shouldSucceedWith200() throws Exception {
        lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        lessorService.create(new LessorPostDto("lessorB", "lessor2@email.com", "+380987654562"));
        lessorService.create(new LessorPostDto("lessorC", "lessor3@email.com", "+380987654563"));
        mockMvc.perform(delete("/api/lessors"))
                .andExpect(status().isOk());
    }






}
