package com.example.todo_api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.todo_api.model.Todo;
import com.example.todo_api.service.TodoService;

/**
 * Todo API の入り口です。HTTP リクエストを受け取り、Service に処理を頼んで、結果を JSON で返します。
 *
 * <p>{@code @RestController} は「戻り値を JSON にする Controller」という意味です。
 * URL の共通部分 {@code /api/todos} はクラスにまとめて書いています。
 *
 * <pre>
 * POST   /api/todos      Todo を作成する
 * GET    /api/todos      一覧を取得する
 * GET    /api/todos/{id} 1件取得する
 * PUT    /api/todos/{id} 1件更新する
 * DELETE /api/todos/{id} 1件削除する
 * </pre>
 */
@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    /**
     * コンストラクタ注入です。
     * {@code new TodoService()} とは書かず、Spring が作った Service を受け取っています。
     */
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    /**
     * Todo を作成します。成功すると 201 Created です。
     * Location ヘッダに、作った Todo の URL を入れます。
     */
    @PostMapping("/post")
    public ResponseEntity<Todo> create(@RequestBody Todo request) {
        Todo created = todoService.create(request);
        URI location = URI.create("/api/todos/post/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    /** Todo の一覧を返します。0件のときは {@code []} です。 */
    @GetMapping("/get")
    public ResponseEntity<List<Todo>> findAll() {
        return ResponseEntity.ok(todoService.findAll());
    }

    /**
     * ID で1件取得します。
     * {@code @PathVariable} は URL の {@code {id}} を引数に束ねます。例: /api/todos/1
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Todo> findById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.findById(id));
    }

    /**
     * 1件更新します。送る JSON は作成時と同じ形です。
     * <pre>
     * { "title": "新しい内容", "completed": true }
     * </pre>
     */
    @PutMapping("/put/{id}")
    public ResponseEntity<Todo> update(@PathVariable Long id, @RequestBody Todo request) {
        return ResponseEntity.ok(todoService.update(id, request));
    }

    /**
     * 1件削除します。成功すると本文なしの 204 No Content です。
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
