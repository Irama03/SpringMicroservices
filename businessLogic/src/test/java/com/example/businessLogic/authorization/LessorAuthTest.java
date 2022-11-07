package com.example.businessLogic.authorization;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LessorAuthTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private LessorService lessorService;
    private final String LESSOR_JWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IkxFU1NPUiIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4Mi9hcGkvbGVzc29ycyIsImV4cCI6MTY5OTIxNjY3NH0.0R2lCePcEEpaWSuioMqLtKypLLy3vaHtuxvAEO1XHU8";
    private final String CLIENT_JWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IkNMSUVOVCIsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4Mi9hcGkvbGVzc29ycyIsImV4cCI6MTY5OTIxNjYwN30.MHQCXJmzbImOPVD_bUHzbW3s962Q93ls4Lb-8SNwDcg";
    private final String ADMIN_JWT = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwicm9sZSI6IkFETUlOIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MDgyL2FwaS9sZXNzb3JzIiwiZXhwIjoxNjk5MjE2NDk3fQ.YJxdKh8th1uFcTAH11oCDaaeJRSElbSYgwWZS75VP54";

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    // Tests for ADMIN role

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
    public void givenAuthAdminRequestOnDeleteAllLessors_shouldSucceedWith200() throws Exception {
        lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        lessorService.create(new LessorPostDto("lessorB", "lessor2@email.com", "+380987654562"));
        lessorService.create(new LessorPostDto("lessorC", "lessor3@email.com", "+380987654563"));
        mockMvc.perform(delete("/api/lessors"))
                .andExpect(status().isOk());
    }

    // With ADMIN_JWT token

    @Test
    public void givenAuthAdminRequestOnGetAllLessors_whenJwtInHeader_shouldSucceedWith200() throws Exception {
        lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        lessorService.create(new LessorPostDto("lessorB", "lessor2@email.com", "+380987654562"));
        lessorService.create(new LessorPostDto("lessorC", "lessor3@email.com", "+380987654563"));
        mockMvc.perform(get("/api/lessors").contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void givenAuthAdminRequestOnGetLessorById_whenJwtInHeader_shouldSucceedWith200() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        mockMvc.perform(get("/api/lessors/"+l.getId()).contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("lessorA"))
                .andExpect(jsonPath("$.email").value("lessor1@email.com"))
                .andExpect(jsonPath("$.phone").value("+380987654561"))
                .andExpect(jsonPath("$.id").value(l.getId()));
    }

    @Test
    public void givenAuthAdminRequestOnPostLessor_whenJwtInHeader_shouldSucceedWith200() throws Exception {
        LessorPostDto l = new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(l);
        mockMvc.perform(post("/api/lessors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(l.getName()))
                .andExpect(jsonPath("$.email").value(l.getEmail()))
                .andExpect(jsonPath("$.phone").value(l.getPhone()))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }


    @Test
    public void givenAuthAdminRequestOnPutLessor_whenJwtInHeader_shouldSucceedWith200() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        LessorPostDto upd = new LessorPostDto("lessorABC", "lessor2@email.com", "+380987654561");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(upd);
        mockMvc.perform(put("/api/lessors/"+l.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(upd.getName()))
                .andExpect(jsonPath("$.email").value(upd.getEmail()))
                .andExpect(jsonPath("$.phone").value(upd.getPhone()))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    public void givenAuthAdminRequestOnDeleteLessorById_whenJwtInHeader_shouldSucceedWith200() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        mockMvc.perform(delete("/api/lessors/"+l.getId())
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_JWT))
                .andExpect(status().isOk());
    }

    @Test
    public void givenAuthAdminRequestOnDeleteAllLessors_whenJwtInHeader_shouldSucceedWith200() throws Exception {
        lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        lessorService.create(new LessorPostDto("lessorB", "lessor2@email.com", "+380987654562"));
        lessorService.create(new LessorPostDto("lessorC", "lessor3@email.com", "+380987654563"));
        mockMvc.perform(delete("/api/lessors")
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_JWT))
                .andExpect(status().isOk());
    }

    // Tests for CLIENT role

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void givenAuthClientRequestOnGetAllLessors_shouldSucceedWith200() throws Exception {
        lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        lessorService.create(new LessorPostDto("lessorB", "lessor2@email.com", "+380987654562"));
        lessorService.create(new LessorPostDto("lessorC", "lessor3@email.com", "+380987654563"));
        mockMvc.perform(get("/api/lessors").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void givenAuthClientRequestOnGetLessorById_shouldSucceedWith200() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        mockMvc.perform(get("/api/lessors/"+l.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("lessorA"))
                .andExpect(jsonPath("$.email").value("lessor1@email.com"))
                .andExpect(jsonPath("$.phone").value("+380987654561"))
                .andExpect(jsonPath("$.id").value(l.getId()));
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void givenAuthClientRequestOnPostLessor_shouldFailWith403() throws Exception {
        LessorPostDto l = new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(l);
        mockMvc.perform(post("/api/lessors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void givenAuthClientRequestOnPutLessor_shouldFailWith403() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        LessorPostDto upd = new LessorPostDto("lessorABC", "lessor2@email.com", "+380987654561");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(upd);
        mockMvc.perform(put("/api/lessors/"+l.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void givenAuthClientRequestOnDeleteLessorById_shouldSucceedWith200() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        mockMvc.perform(delete("/api/lessors/"+l.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = {"CLIENT"})
    public void givenAuthClientRequestOnDeleteAllLessors_shouldSucceedWith200() throws Exception {
        lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        lessorService.create(new LessorPostDto("lessorB", "lessor2@email.com", "+380987654562"));
        lessorService.create(new LessorPostDto("lessorC", "lessor3@email.com", "+380987654563"));
        mockMvc.perform(delete("/api/lessors"))
                .andExpect(status().isForbidden());
    }

    // With CLIENT_JWT token

    @Test
    public void givenAuthClientRequestOnGetAllLessors_whenJwtInHeader_shouldSucceedWith200() throws Exception {
        lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        lessorService.create(new LessorPostDto("lessorB", "lessor2@email.com", "+380987654562"));
        lessorService.create(new LessorPostDto("lessorC", "lessor3@email.com", "+380987654563"));
        mockMvc.perform(get("/api/lessors").contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, CLIENT_JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void givenAuthClientRequestOnGetLessorById_whenJwtInHeader_shouldSucceedWith200() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        mockMvc.perform(get("/api/lessors/"+l.getId()).contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, CLIENT_JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("lessorA"))
                .andExpect(jsonPath("$.email").value("lessor1@email.com"))
                .andExpect(jsonPath("$.phone").value("+380987654561"))
                .andExpect(jsonPath("$.id").value(l.getId()));
    }

    @Test
    public void givenAuthClientRequestOnPostLessor_whenJwtInHeader_shouldFailWith403() throws Exception {
        LessorPostDto l = new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(l);
        mockMvc.perform(post("/api/lessors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header(HttpHeaders.AUTHORIZATION, CLIENT_JWT))
                .andExpect(status().isForbidden());
    }


    @Test
    public void givenAuthClientRequestOnPutLessor_whenJwtInHeader_shouldFailWith403() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        LessorPostDto upd = new LessorPostDto("lessorABC", "lessor2@email.com", "+380987654561");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(upd);
        mockMvc.perform(put("/api/lessors/"+l.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header(HttpHeaders.AUTHORIZATION, CLIENT_JWT))
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenAuthClientRequestOnDeleteLessorById_whenJwtInHeader_shouldSucceedWith200() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        mockMvc.perform(delete("/api/lessors/"+l.getId())
                        .header(HttpHeaders.AUTHORIZATION, CLIENT_JWT))
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenAuthClientRequestOnDeleteAllLessors_whenJwtInHeader_shouldSucceedWith200() throws Exception {
        lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        lessorService.create(new LessorPostDto("lessorB", "lessor2@email.com", "+380987654562"));
        lessorService.create(new LessorPostDto("lessorC", "lessor3@email.com", "+380987654563"));
        mockMvc.perform(delete("/api/lessors")
                        .header(HttpHeaders.AUTHORIZATION, CLIENT_JWT))
                .andExpect(status().isForbidden());
    }


    // Tests for LESSOR role

    @Test
    @WithMockUser(authorities = {"LESSOR"})
    public void givenAuthLessorRequestOnGetAllLessors_shouldSucceedWith200() throws Exception {
        lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        lessorService.create(new LessorPostDto("lessorB", "lessor2@email.com", "+380987654562"));
        lessorService.create(new LessorPostDto("lessorC", "lessor3@email.com", "+380987654563"));
        mockMvc.perform(get("/api/lessors").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @WithMockUser(authorities = {"LESSOR"})
    public void givenAuthLessorRequestOnGetLessorById_shouldSucceedWith200() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        mockMvc.perform(get("/api/lessors/"+l.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("lessorA"))
                .andExpect(jsonPath("$.email").value("lessor1@email.com"))
                .andExpect(jsonPath("$.phone").value("+380987654561"))
                .andExpect(jsonPath("$.id").value(l.getId()));
    }

    @Test
    @WithMockUser(authorities = {"LESSOR"})
    public void givenAuthLessorRequestOnPostLessor_shouldSucceedWith200() throws Exception {
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
    @WithMockUser(authorities = {"LESSOR"})
    public void givenAuthLessorRequestOnPutLessor_shouldSucceedWith200() throws Exception {
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
    @WithMockUser(authorities = {"LESSOR"})
    public void givenAuthLessorRequestOnDeleteLessorById_shouldSucceedWith200() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        mockMvc.perform(delete("/api/lessors/"+l.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = {"LESSOR"})
    public void givenAuthLessorRequestOnDeleteAllLessors_shouldFailWith403() throws Exception {
        lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        lessorService.create(new LessorPostDto("lessorB", "lessor2@email.com", "+380987654562"));
        lessorService.create(new LessorPostDto("lessorC", "lessor3@email.com", "+380987654563"));
        mockMvc.perform(delete("/api/lessors"))
                .andExpect(status().isForbidden());
    }


    // With LESSOR_JWT token

    @Test
    public void givenAuthLessorRequestOnGetAllLessors_whenJwtInHeader_shouldSucceedWith200() throws Exception {
        lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        lessorService.create(new LessorPostDto("lessorB", "lessor2@email.com", "+380987654562"));
        lessorService.create(new LessorPostDto("lessorC", "lessor3@email.com", "+380987654563"));
        mockMvc.perform(get("/api/lessors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, LESSOR_JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void givenAuthLessorRequestOnGetLessorById_whenJwtInHeader_shouldSucceedWith200() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        mockMvc.perform(get("/api/lessors/"+l.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, LESSOR_JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("lessorA"))
                .andExpect(jsonPath("$.email").value("lessor1@email.com"))
                .andExpect(jsonPath("$.phone").value("+380987654561"))
                .andExpect(jsonPath("$.id").value(l.getId()));
    }

    @Test
    public void givenAuthLessorRequestOnPostLessor_whenJwtInHeader_shouldSucceedWith200() throws Exception {
        LessorPostDto l = new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(l);
        mockMvc.perform(post("/api/lessors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header(HttpHeaders.AUTHORIZATION, LESSOR_JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(l.getName()))
                .andExpect(jsonPath("$.email").value(l.getEmail()))
                .andExpect(jsonPath("$.phone").value(l.getPhone()))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }


    @Test
    public void givenAuthLessorRequestOnPutLessor_whenJwtInHeader_shouldSucceedWith200() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        LessorPostDto upd = new LessorPostDto("lessorABC", "lessor2@email.com", "+380987654561");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(upd);
        mockMvc.perform(put("/api/lessors/"+l.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .header(HttpHeaders.AUTHORIZATION, LESSOR_JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(upd.getName()))
                .andExpect(jsonPath("$.email").value(upd.getEmail()))
                .andExpect(jsonPath("$.phone").value(upd.getPhone()))
                .andExpect(jsonPath("$.id").isNotEmpty());
    }


    @Test
    public void givenAuthLessorRequestOnDeleteLessorById_whenJwtInHeader_shouldSucceedWith200() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        mockMvc.perform(delete("/api/lessors/"+l.getId())
                        .header(HttpHeaders.AUTHORIZATION, LESSOR_JWT))
                .andExpect(status().isOk());
    }

    @Test
    public void givenAuthLessorRequestOnDeleteAllLessors_whenJwtInHeader_shouldFailWith403() throws Exception {
        lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        lessorService.create(new LessorPostDto("lessorB", "lessor2@email.com", "+380987654562"));
        lessorService.create(new LessorPostDto("lessorC", "lessor3@email.com", "+380987654563"));
        mockMvc.perform(delete("/api/lessors")
                        .header(HttpHeaders.AUTHORIZATION, LESSOR_JWT))
                .andExpect(status().isForbidden());
    }


    // Without JWT token

    @Test
    public void givenAuthAdminRequestOnGetAllLessors_whenJwtNotInHeader_shouldFailWith403() throws Exception {
        lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        lessorService.create(new LessorPostDto("lessorB", "lessor2@email.com", "+380987654562"));
        lessorService.create(new LessorPostDto("lessorC", "lessor3@email.com", "+380987654563"));
        mockMvc.perform(get("/api/lessors").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenAuthAdminRequestOnGetLessorById_whenJwtNotInHeader_shouldFailWith403() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        mockMvc.perform(get("/api/lessors/"+l.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenAuthAdminRequestOnPostLessor_whenJwtNotInHeader_shouldFailWith403() throws Exception {
        LessorPostDto l = new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(l);
        mockMvc.perform(post("/api/lessors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }


    @Test
    public void givenAuthAdminRequestOnPutLessor_whenJwtNotInHeader_shouldFailWith403() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        LessorPostDto upd = new LessorPostDto("lessorABC", "lessor2@email.com", "+380987654561");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(upd);
        mockMvc.perform(put("/api/lessors/"+l.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenAuthAdminRequestOnDeleteLessorById_whenJwtNotInHeader_shouldFailWith403() throws Exception {
        Lessor l = lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        mockMvc.perform(delete("/api/lessors/"+l.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void givenAuthAdminRequestOnDeleteAllLessors_whenJwtNotInHeader_shouldFailWith403() throws Exception {
        lessorService.create(new LessorPostDto("lessorA", "lessor1@email.com", "+380987654561"));
        lessorService.create(new LessorPostDto("lessorB", "lessor2@email.com", "+380987654562"));
        lessorService.create(new LessorPostDto("lessorC", "lessor3@email.com", "+380987654563"));
        mockMvc.perform(delete("/api/lessors"))
                .andExpect(status().isForbidden());
    }













}
