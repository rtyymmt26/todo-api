package com.example.todo_api;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TodoApiApplicationTests {

	@Test
    void main() {
        try (MockedStatic<SpringApplication> mocked =
                Mockito.mockStatic(SpringApplication.class)) {

            TodoApiApplication.main(new String[]{});

            mocked.verify(() ->
                    SpringApplication.run(
                            TodoApiApplication.class,
                            new String[]{}));
        }
    }

}
