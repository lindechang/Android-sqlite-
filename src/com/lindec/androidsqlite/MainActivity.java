package com.lindec.androidsqlite;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
		implements AdapterView.OnItemClickListener, android.view.View.OnClickListener {
	private BooksDB mBooksDB;
	private Cursor mCursor;
	private EditText BookName;
	private EditText BookAuthor;
	private ListView BooksList;

	private Button add;
	private Button del;
	private Button update;

	private int BOOK_ID = 0;
	protected final static int MENU_ADD = Menu.FIRST;
	protected final static int MENU_DELETE = Menu.FIRST + 1;
	protected final static int MENU_UPDATE = Menu.FIRST + 2;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setUpViews();
	}

	public void setUpViews() {
		mBooksDB = new BooksDB(this);
		mCursor = mBooksDB.select();

		BookName = (EditText) findViewById(R.id.bookname);
		BookAuthor = (EditText) findViewById(R.id.author);
		BooksList = (ListView) findViewById(R.id.bookslist);

		add = (Button) findViewById(R.id.add);
		del = (Button) findViewById(R.id.del);
		update = (Button) findViewById(R.id.update);

		add.setOnClickListener(this);
		del.setOnClickListener(this);
		update.setOnClickListener(this);

		BooksList.setAdapter(new BooksListAdapter(this, mCursor));
		BooksList.setOnItemClickListener(this);
	}

	public void add() {
		String bookname = BookName.getText().toString();
		String author = BookAuthor.getText().toString();
		// 书名和作者都不能为空，或者退出
		if (bookname.equals("") || author.equals("")) {
			return;
		}
		mBooksDB.insert(bookname, author);
		mCursor.requery();
		BooksList.invalidateViews();
		BookName.setText("");
		BookAuthor.setText("");
		Toast.makeText(this, "Add Successed!", Toast.LENGTH_SHORT).show();
	}

	public void delete() {
		if (BOOK_ID == 0) {
			return;
		}
		mBooksDB.delete(BOOK_ID);
		mCursor.requery();
		BooksList.invalidateViews();
		BookName.setText("");
		BookAuthor.setText("");
		Toast.makeText(this, "Delete Successed!", Toast.LENGTH_SHORT).show();
	}

	public void update() {
		String bookname = BookName.getText().toString();
		String author = BookAuthor.getText().toString();
		// 书名和作者都不能为空，或者退出
		if (bookname.equals("") || author.equals("")) {
			return;
		}
		mBooksDB.update(BOOK_ID, bookname, author);
		mCursor.requery();
		BooksList.invalidateViews();
		BookName.setText("");
		BookAuthor.setText("");
		Toast.makeText(this, "Update Successed!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		mCursor.moveToPosition(position);
		BOOK_ID = mCursor.getInt(0);
		BookName.setText(mCursor.getString(1));
		BookAuthor.setText(mCursor.getString(2));

	}

	public class BooksListAdapter extends BaseAdapter {
		private Context mContext;
		private Cursor mCursor;

		public BooksListAdapter(Context context, Cursor cursor) {

			mContext = context;
			mCursor = cursor;
		}

		@Override
		public int getCount() {
			return mCursor.getCount();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView mTextView = new TextView(mContext);
			mCursor.moveToPosition(position);
			mTextView.setText(mCursor.getString(1) + "___" + mCursor.getString(2));
			return mTextView;
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.add:
			add();
			break;

		case R.id.del:
			delete();
			break;

		case R.id.update:
			update();
			break;

		default:
			break;
		}

	}

}
