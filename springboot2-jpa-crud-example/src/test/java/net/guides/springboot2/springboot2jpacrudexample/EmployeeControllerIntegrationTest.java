package net.guides.springboot2.springboot2jpacrudexample;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import net.guides.springboot2.springboot2jpacrudexample.model.Employee;
import net.guides.springboot2.springboot2jpacrudexample.model.Role;
import net.guides.springboot2.springboot2jpacrudexample.model.User;
import net.guides.springboot2.springboot2jpacrudexample.model.ERole;
import net.guides.springboot2.springboot2jpacrudexample.payload.request.LoginRequest;
import net.guides.springboot2.springboot2jpacrudexample.payload.response.JwtResponse;
import net.guides.springboot2.springboot2jpacrudexample.repository.RoleRepository;
import net.guides.springboot2.springboot2jpacrudexample.repository.UserRepository;
import net.guides.springboot2.springboot2jpacrudexample.repository.EmployeeRepository;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder encoder;

    private String jwtToken;
    private HttpHeaders headers;
    private Employee testEmployee;

    private String getRootUrl() {
        return "http://localhost:" + port;
    }

    @BeforeEach
    public void setUp() {
        // Clear existing data
        employeeRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // Create roles
        Role adminRole = new Role();
        adminRole.setName(ERole.ROLE_ADMIN);
        roleRepository.save(adminRole);

        // Create admin user
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword(encoder.encode("admin123"));
        admin.setFullName("Admin User");
        admin.setActive(true);
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        admin.setRoles(roles);
        userRepository.save(admin);

        // Create test employee
        testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setEmailId("john.doe@example.com");
        testEmployee = employeeRepository.save(testEmployee);

        // Login to get JWT token
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin123");

        ResponseEntity<JwtResponse> loginResponse = restTemplate.postForEntity(
            getRootUrl() + "/api/auth/signin", 
            loginRequest, 
            JwtResponse.class
        );

        assertNotNull(loginResponse.getBody(), "Login response should not be null");
        assertNotNull(loginResponse.getBody().getAccessToken(), "JWT token should not be null");
        
        jwtToken = loginResponse.getBody().getAccessToken();
        
        // Set up headers with JWT token
        headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void testGetAllEmployees() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            getRootUrl() + "/api/v1/employees",
            HttpMethod.GET, 
            entity, 
            String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetEmployeeById() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Employee> response = restTemplate.exchange(
            getRootUrl() + "/api/v1/employees/" + testEmployee.getId(),
            HttpMethod.GET,
            entity,
            Employee.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testEmployee.getFirstName(), response.getBody().getFirstName());
    }

    @Test
    public void testCreateEmployee() {
        Employee employee = new Employee();
        employee.setEmailId("test@example.com");
        employee.setFirstName("Test");
        employee.setLastName("User");

        HttpEntity<Employee> entity = new HttpEntity<>(employee, headers);

        ResponseEntity<Employee> response = restTemplate.exchange(
            getRootUrl() + "/api/v1/employees",
            HttpMethod.POST,
            entity,
            Employee.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test", response.getBody().getFirstName());
        assertEquals("User", response.getBody().getLastName());
    }

    @Test
    public void testUpdateEmployee() {
        HttpEntity<String> getEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Employee> getResponse = restTemplate.exchange(
            getRootUrl() + "/api/v1/employees/" + testEmployee.getId(),
            HttpMethod.GET,
            getEntity,
            Employee.class
        );

        Employee employee = getResponse.getBody();
        assertNotNull(employee, "Employee should not be null");
        
        employee.setFirstName("Updated");
        employee.setLastName("Name");

        HttpEntity<Employee> putEntity = new HttpEntity<>(employee, headers);

        ResponseEntity<Employee> putResponse = restTemplate.exchange(
            getRootUrl() + "/api/v1/employees/" + testEmployee.getId(),
            HttpMethod.PUT,
            putEntity,
            Employee.class
        );

        assertEquals(HttpStatus.OK, putResponse.getStatusCode());
        assertNotNull(putResponse.getBody());
        assertEquals("Updated", putResponse.getBody().getFirstName());
        assertEquals("Name", putResponse.getBody().getLastName());
    }

    @Test
    public void testDeleteEmployee() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<Employee> getResponse = restTemplate.exchange(
            getRootUrl() + "/api/v1/employees/" + testEmployee.getId(),
            HttpMethod.GET,
            entity,
            Employee.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        restTemplate.exchange(
            getRootUrl() + "/api/v1/employees/" + testEmployee.getId(),
            HttpMethod.DELETE,
            entity,
            Void.class
        );

        ResponseEntity<Employee> verifyDeleteResponse = restTemplate.exchange(
            getRootUrl() + "/api/v1/employees/" + testEmployee.getId(),
            HttpMethod.GET,
            entity,
            Employee.class
        );

        assertEquals(HttpStatus.NOT_FOUND, verifyDeleteResponse.getStatusCode());
    }
}
