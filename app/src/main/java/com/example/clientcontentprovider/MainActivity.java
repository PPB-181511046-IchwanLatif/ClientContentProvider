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
        Cursor cursor = fetchCheeseCursor();

        RecyclerView list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(list.getContext()));
        mCheeseAdapter = new CheeseAdapter();
        mCheeseAdapter.setCheeses(cursor);
        list.setAdapter(mCheeseAdapter);
    }

    protected Cursor fetchCheeseCursor(){
        Uri allCheeses = Uri.parse("content://com.example.android.contentprovidersample.provider/cheeses");
        String[] projection = new String[]{
                "_id", "name"
        };
        Cursor cursor;
        CursorLoader cursorLoader = new CursorLoader(
                this,
                allCheeses,
                projection,
                "name" + " LIKE ?",
                new String[] {"I%"},
                "name" + " ASC"
        );
        cursor = cursorLoader.loadInBackground();
        return cursor;
    }

    private void printCheeses(Cursor cursor){
        if(cursor.moveToFirst()){
            do {
                String cheeseId = cursor.getString(cursor.getColumnIndex("_id"));
                String cheeseName = cursor.getString(cursor.getColumnIndex("name"));
                Log.v("Content Providers", cheeseId + ", " + cheeseName);
            }
            while (cursor.moveToNext());
        }
    }

    private static class  CheeseAdapter extends RecyclerView.Adapter<CheeseAdapter.ViewHolder>{
        private Cursor cursor;

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (cursor.moveToPosition(position)){
                holder.mText.setText(cursor.getString(cursor.getColumnIndex("name")));
            }
        }

        @Override
        public int getItemCount() {
            return cursor == null ? 0 : cursor.getCount();
        }

        public void setCheeses(Cursor cursor) {
            this.cursor = cursor;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mText;

            public ViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(
                        android.R.layout.simple_list_item_1, parent, false
                ));
                mText = itemView.findViewById(android.R.id.text1);
            }
        }
    }
}
