package a01.is61m.floatreader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper = new DBHelper(this);

    private View.OnClickListener mCorkyListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent()
                    .setType("*/*")
                    .setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
        }
    };

    private String getRealPathFromURI(Uri contentURI) {

        Cursor returnCursor =
                getContentResolver().query(contentURI, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String path = returnCursor.getString(nameIndex);
        return path;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        HorizontalScrollView sv = (HorizontalScrollView) findViewById(R.id.scroll_view);
        if (sv != null) {
            outState.putInt("scrollX", sv.getScrollX());
            outState.putInt("scrollY", sv.getScrollY());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData(); //The uri with the location of the file
            getRealPathFromURI(selectedfile);
            File myFile = new File(selectedfile.getEncodedPath());
            addBook(new Book(myFile.getName(), myFile.getPath()));
            dbHelper.appendRow(myFile);
        }
    }

    void addBook(Book book) {
        LinearLayout ln = (LinearLayout) findViewById(R.id.book_host);
        View bookView = getLayoutInflater().inflate(R.layout.book, null);
        TextView tv = (TextView) bookView.findViewById(R.id.book_name);
        tv.setText(book.getName());
        ln.addView(bookView);
    }

    private HorizontalScrollView sv;
    private int scrollX;
    private int scrollY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sv = (HorizontalScrollView) findViewById(R.id.scroll_view);
        if (savedInstanceState != null) {
            scrollX = savedInstanceState.getInt("scrollX"); // Defaults to 0
            scrollY = savedInstanceState.getInt("scrollY");
            sv.post(new Runnable() {
                @Override
                public void run() {
                    sv.scrollTo(scrollX, scrollY);
                }
            });
        }
        Button button = (Button) findViewById(R.id.button);
        if (button != null) {
            button.setOnClickListener(mCorkyListener);
        }

        ArrayList<Book> books = dbHelper.getBooks();
        for (int i = 0; i < books.size(); i++) {
            addBook(books.get(i));
        }
    }
}
