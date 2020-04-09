package com.example.clientcontentprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private CheeseAdapter mCheeseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Cursor c = fetchCheesesCursor();

        RecyclerView list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(list.getContext()));
        mCheeseAdapter = new CheeseAdapter();
        mCheeseAdapter.setCheeses(c);
        list.setAdapter(mCheeseAdapter);

    }

    protected Cursor fetchCheesesCursor(){
        Uri allCheeses = Uri.parse("content://com.example.android.contentprovidersample.provider/cheeses");
        String[] projection = new String[]
                {"_id", "name"};
        Cursor c;
        CursorLoader cursorLoader = new CursorLoader(
                this,
                allCheeses,
                projection,
                null,
                null,
                "name" + " DESC");
        c = cursorLoader.loadInBackground();
        return c;
    }

    private void printCheeses(Cursor c)
    {
        if (c.moveToFirst()) {
            do{
                String cheeseId = c.getString(c.getColumnIndex("_id"));
                String cheeseName =  c.getString(c.getColumnIndex("name"));
                Log.v("Content Providers", cheeseId + ", " + cheeseName);
            } while (c.moveToNext());
        }
    }

    private static class CheeseAdapter extends RecyclerView.Adapter<CheeseAdapter.ViewHolder> {

        private Cursor mCursor;

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (mCursor.moveToPosition(position)) {
                holder.mText.setText(mCursor.getString(mCursor.getColumnIndex("name")));
            }
        }

        @Override
        public int getItemCount() {
            return mCursor == null ? 0 : mCursor.getCount();
        }


        void setCheeses(Cursor cursor) {
            mCursor = cursor;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            final TextView mText;

            ViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(
                        android.R.layout.simple_list_item_1, parent, false));
                mText = itemView.findViewById(android.R.id.text1);
            }

        }

    }
}