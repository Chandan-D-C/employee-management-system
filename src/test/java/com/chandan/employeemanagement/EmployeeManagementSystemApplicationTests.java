
package com.chandan.employeemanagement;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeManagementSystemApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    private String uniqueEmail(String prefix) {
        return prefix + "." + UUID.randomUUID() + "@example.com";
    }

    @Test
    void getAllEmployeesShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void addEmployeeShouldReturnCreated() throws Exception {
        String email = uniqueEmail("test.created");

        String employeeJson = """
                {
                    "firstName": "Test",
                    "lastName": "Employee",
                    "email": "%s",
                    "department": "Testing",
                    "salary": 30000
                }
                """.formatted(email);

        mockMvc.perform(post("/api/employees")
                        .contentType(APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void addEmployeeWithInvalidEmailShouldReturnBadRequest()
            throws Exception {

        String employeeJson = """
                {
                    "firstName": "Test",
                    "lastName": "Employee",
                    "email": "invalid-email",
                    "department": "Testing",
                    "salary": 30000
                }
                """;

        mockMvc.perform(post("/api/employees")
                        .contentType(APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getNonexistentEmployeeShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/employees/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEmployeeShouldReturnNoContent() throws Exception {
        String email = uniqueEmail("delete.test");

        String employeeJson = """
                {
                    "firstName": "Delete",
                    "lastName": "Test",
                    "email": "%s",
                    "department": "Testing",
                    "salary": 30000
                }
                """.formatted(email);

        String response = mockMvc.perform(post("/api/employees")
                        .contentType(APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String employeeId = response.split("\"id\":")[1]
                .replaceAll("[^0-9].*", "");

        mockMvc.perform(delete("/api/employees/" + employeeId))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateEmployeeShouldReturnUpdatedEmployee() throws Exception {
        String email = uniqueEmail("update.test");

        String employeeJson = """
                {
                    "firstName": "Update",
                    "lastName": "Test",
                    "email": "%s",
                    "department": "Testing",
                    "salary": 30000
                }
                """.formatted(email);

        String response = mockMvc.perform(post("/api/employees")
                        .contentType(APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String employeeId = response.split("\"id\":")[1]
                .replaceAll("[^0-9].*", "");

        String updatedEmail = uniqueEmail("updated.employee");

        String updatedEmployeeJson = """
                {
                    "firstName": "Updated",
                    "lastName": "Employee",
                    "email": "%s",
                    "department": "Data Science",
                    "salary": 45000
                }
                """.formatted(updatedEmail);

        mockMvc.perform(put("/api/employees/" + employeeId)
                        .contentType(APPLICATION_JSON)
                        .content(updatedEmployeeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("Employee"))
                .andExpect(jsonPath("$.email").value(updatedEmail))
                .andExpect(jsonPath("$.department").value("Data Science"))
                .andExpect(jsonPath("$.salary").value(45000));
    }
}
