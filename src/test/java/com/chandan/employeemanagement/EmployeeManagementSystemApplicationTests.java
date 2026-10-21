
package com.chandan.employeemanagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeManagementSystemApplicationTests {

    @Autowired
    private MockMvc mockMvc;

   
	
	@Test
	void getAllEmployeesShouldReturnOk() throws Exception {
		mockMvc.perform(get("/api/employees"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
	}
	
@Test
void addEmployeeShouldReturnCreated() throws Exception {
    String employeeJson = """
            {
                "firstName": "Test",
                "lastName": "Employee",
                "email": "test.employee161@example.com",
                "department": "Testing",
                "salary": 30000
            }
            """;

    mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                    .post("/api/employees")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .content(employeeJson)
    )
	.andExpect(status().isCreated())
	.andExpect(content().json("""
			{
				"email": "test.employee161@example.com"
			}
			"""));
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

    mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                    .post("/api/employees")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .content(employeeJson)
    ).andExpect(status().isBadRequest());
}

@Test
void getNonexistentEmployeeShouldReturnNotFound() throws Exception {
    mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                    .get("/api/employees/999999")
    ).andExpect(status().isNotFound());
}



	
@Test
void deleteEmployeeShouldReturnNoContent() throws Exception {
    String employeeJson = """
            {
                "firstName": "Delete",
                "lastName": "Test",
                "email": "delete.test@example.com",
                "department": "Testing",
                "salary": 30000
            }
            """;

    String response = mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                    .post("/api/employees")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .content(employeeJson)
    )
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    
String employeeId = response.split("\"id\":")[1]
        .replaceAll("[^0-9].*", "")
        .trim();


    mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                    .delete("/api/employees/" + employeeId)
    ).andExpect(status().isNoContent());
}

@Test
void updateEmployeeShouldReturnUpdatedEmployee() throws Exception {
    String employeeJson = """
            {
                "firstName": "Update",
                "lastName": "Test",
                "email": "update.test@example.com",
                "department": "Testing",
                "salary": 30000
            }
            """;

    String response = mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                    .post("/api/employees")
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .content(employeeJson)
    )
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    String employeeId = response.split("\"id\":")[1]
            .replaceAll("[^0-9].*", "")
            .trim();

    String updatedEmployeeJson = """
            {
                "firstName": "Updated",
                "lastName": "Employee",
                "email": "updated.employee@example.com",
                "department": "Data Science",
                "salary": 45000
            }
            """;

    mockMvc.perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                    .put("/api/employees/" + employeeId)
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .content(updatedEmployeeJson)
    )
            .andExpect(status().isOk())
            .andExpect(content().json("""
                    {
                        "firstName": "Updated",
                        "lastName": "Employee",
                        "email": "updated.employee@example.com",
                        "department": "Data Science",
                        "salary": 45000
                    }
                    """));
}



}
