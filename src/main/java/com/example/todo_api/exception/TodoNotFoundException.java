package com.example.todo_api.exception;

/**
 * 指定した ID の Todo がメモリ上に無いときに投げる例外です。
 *
 * <p>Service は HTTP を知りません。見つからない事実だけを伝え、
 * 404 にするかどうかは Controller 側の例外ハンドラが決めます。
 */
public class TodoNotFoundException extends RuntimeException {

    public TodoNotFoundException(Long id) {
        super("Todo が見つかりません。id=" + id);
    }
}
