package com.taskapp.dataaccess;

public class LogDataAccess {
    private final String filePath;


    public LogDataAccess() {
        filePath = "app/src/main/resources/logs.csv";
    }

    public LogDataAccess(String filePath) {
        this.filePath = filePath;
    }

    /**
     * ログをCSVファイルに保存します。
     *
     * @param log 保存するログ
     */
    // public void save() {
    // }

    /**
     * すべてのログを取得します。
     *
     * @return すべてのログのリスト
     */
    // public List<Log> findAll() {

    // }

    /**
     * 指定したタスクコードに該当するログを削除します。
     *
     * @param taskCode 削除するログのタスクコード
     */
    // public void deleteByTaskCode() {

    // }

}