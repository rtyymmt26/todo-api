package com.example.todo_api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * HTTP の入り口が要件どおり動くかを確認するテストです。
 * サーバは起動せず、Spring の中でリクエストを再現します。
 */
@SpringBootTest
class TodoControllerTest {

        private final MockMvc mockMvc;

        @Autowired
        TodoControllerTest(WebApplicationContext context) {
                this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        }

        @Test
        void crudFlow() throws Exception {
                mockMvc.perform(get("/api/todos/get"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$").isEmpty());

                mockMvc.perform(post("/api/todos/post")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\":\" 牛乳を買う \",\"completed\":false}"))
                                .andExpect(status().isCreated())
                                .andExpect(header().string("Location", "/api/todos/post/1"))
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.title").value("牛乳を買う"))
                                .andExpect(jsonPath("$.completed").value(false));

                mockMvc.perform(get("/api/todos/get/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.title").value("牛乳を買う"));

                mockMvc.perform(put("/api/todos/put/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\":\"牛乳を買った\",\"completed\":true}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.completed").value(true));

                mockMvc.perform(get("/api/todos/get"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(1));

                mockMvc.perform(delete("/api/todos/delete/1"))
                                .andExpect(status().isNoContent());

                mockMvc.perform(get("/api/todos/get/1"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value("Todo が見つかりません。id=1"));

                mockMvc.perform(delete("/api/todos/delete/1"))
                                .andExpect(status().isNotFound());
        }

        @Test
        void blankTitleIsBadRequest() throws Exception {
                mockMvc.perform(post("/api/todos/post")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"title\":\"   \"}"))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("title は必須です"));
        }

        @Test
        void nullTitleIsBadRequest() throws Exception {
                mockMvc.perform(post("/api/todos/post")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.message").value("title は必須です"));
        }
}
