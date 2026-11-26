package com.example.todo_api.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.todo_api.exception.TodoNotFoundException;
import com.example.todo_api.model.Todo;

/**
 * Todo の作成・取得・更新・削除を行うクラスです。
 *
 * <p>{@code @Service} を付けると、Spring がこのクラスを1つだけ作り、
 * Controller に渡してくれます（DI: 依存性の注入）。
 * データはアプリを止めると消えるメモリ上の Map に置きます。DB は使いません。
 */
@Service
public class TodoService {

    /**
     * 登録順を保つ Map。キーは ID、値は Todo です。
     * LinkedHashMap は入れた順番を覚えるので、一覧が登録順になります。
     */
    private final Map<Long, Todo> store = new LinkedHashMap<>();

    /** 次に付ける ID。1, 2, 3... と増えていきます。 */
    private final AtomicLong idSequence = new AtomicLong(1);

    /**
     * Todo を新しく作ります。ID はここで採番します。
     *
     * @param request クライアントが送った title と completed
     * @return 保存した Todo（ID 付き）
     */
    public synchronized Todo create(Todo request) {
        String title = requireTitle(request.getTitle());
        long id = idSequence.getAndIncrement();
        Todo todo = new Todo();
        todo.setId(id);
        todo.setTitle(title);
        todo.setCompleted(request.isCompleted());
        store.put(id, todo);
        return todo;
    }

    /** 登録されている Todo をすべて返します。1件も無ければ空のリストです。 */
    public synchronized List<Todo> findAll() {
        // Map 本体を外に渡すと呼び出し側が中身を壊せるので、リストをコピーして返します。
        return new ArrayList<>(store.values());
    }

    /**
     * ID で1件探します。無ければ例外になります。
     */
    public synchronized Todo findById(Long id) {
        Todo todo = store.get(id);
        if (todo == null) {
            throw new TodoNotFoundException(id);
        }
        return todo;
    }

    /**
     * 既存の Todo の title と completed を書き換えます。
     * URL の ID を正とし、ボディに書かれた id は使いません。
     */
    public synchronized Todo update(Long id, Todo request) {
        Todo existing = findById(id);
        existing.setTitle(requireTitle(request.getTitle()));
        existing.setCompleted(request.isCompleted());
        return existing;
    }

    /** ID の Todo を消します。無ければ例外になります。 */
    public synchronized void delete(Long id) {
        if (store.remove(id) == null) {
            throw new TodoNotFoundException(id);
        }
    }

    /** title が空なら登録・更新できないようにします。 */
    private String requireTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title は必須です");
        }
        return title.trim();
    }
}
