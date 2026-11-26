package com.example.todo_api.model;

/**
 * TODO 1件分のデータです。
 *
 * <p>このクラスは「何を覚えておくか」だけを担当します。
 * 保存や HTTP の処理は Service / Controller に任せます。
 * データベースは使わないので、このオブジェクトをメモリ上の Map に入れます。
 */
public class Todo {

    /** サーバが付ける一意な番号。クライアントは指定しません。 */
    private Long id;

    /** やることの内容。例: 「牛乳を買う」 */
    private String title;

    /** 完了したら true、未完了なら false */
    private boolean completed;

    /**
     * 引数なしのコンストラクタ。
     * リクエストの JSON を Todo に変換するとき、Spring がこれを使います。
     */
    public Todo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    /**
     * JSON の completed を受け取ります。
     * 項目が無い、または null のときは未完了（false）にします。
     * 引数を Boolean にしているのは、省略された値を false として受け取れるようにするためです。
     */
    public void setCompleted(Boolean completed) {
        this.completed = Boolean.TRUE.equals(completed);
    }
}
