package a01.is61m.floatreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by admin on 27.11.2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    /**
     * Имя файла базы данных
     */
    private static final String DATABASE_NAME = "books.db";

    /**
     * Версия базы данных. При изменении схемы увеличить на единицу
     */
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Вызывается при создании базы данных
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы
        String SQL_CREATE_GUESTS_TABLE = "CREATE TABLE books ("
                + "path TEXT NOT NULL, "
                + "name TEXT NOT NULL);";

        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_GUESTS_TABLE);
    }

    public void appendRow(File selectedfile) {

        String path, name;

        path = selectedfile.getAbsolutePath();
        name = selectedfile.getName();

        // Gets the database in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        // Создаем объект ContentValues, где имена столбцов ключи,
        // а информация о госте является значениями ключей
        ContentValues values = new ContentValues();
        values.put("path", path);
        values.put("name", name);

        long newRowId = db.insert("books", null, values);
    }

    public ArrayList<Book> getBooks() {
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = this.getReadableDatabase();

        // Зададим условие для выборки - список столбцов
        String[] projection = {"path", "name" };

        // Делаем запрос
        Cursor cursor = db.query(
                "books",   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        ArrayList<Book> books = new ArrayList<>();

        try {

            // Узнаем индекс каждого столбца
            int pathColumnIndex = cursor.getColumnIndex("path");
            int nameColumnIndex = cursor.getColumnIndex("name");

            // Проходим через все ряды
            while (cursor.moveToNext()) {
                // Используем индекс для получения строки или числа
                String currentPath = cursor.getString(pathColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);

                books.add(new Book(currentPath, currentName));
            }
        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
            return books;
        }
    }

    /**
     * Вызывается при обновлении схемы базы данных
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
