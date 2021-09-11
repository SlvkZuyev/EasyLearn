package com.slvk.words20.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.slvk.words20.App;
import com.slvk.words20.PairOfWords;
import com.slvk.words20.R;
import com.slvk.words20.database.DescriptionDao;
import com.slvk.words20.database.ThemeDescription;

import java.util.ArrayList;
import java.util.List;

public class WordsAdapter  extends RecyclerView.Adapter<WordsAdapter.WordViewHolder> {

    private List<PairOfWords> words;
    private CardView cv;

    public void setItems(List<PairOfWords> words){
        this.words = words;
        notifyDataSetChanged();
    }

    public void clearItems(){
        words.clear();
        notifyDataSetChanged();
    }

    public WordsAdapter(){

    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(view);
    }

    //Добавляет вьюшку в список
    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        holder.bind(words.get(position));
    }

    @Override
    public int getItemCount() {
        return words.size();
    }


    class WordViewHolder extends RecyclerView.ViewHolder {
        // Ваш ViewHolder должен содержать переменные для всех
        // View-компонентов, которым вы хотите задавать какие-либо свойства
        // в процессе работы пользователя со списком

        private TextView eng;
        private TextView rus;
        //private ImageView theme_image;

        public void bind(PairOfWords pair) {
            eng.setText(pair.ENG);
            rus.setText(pair.RUS);
        }

        // Мы также создали конструктор, который принимает на вход View-компонент строкИ
        // и ищет все дочерние компоненты
        public WordViewHolder(View itemView) {
            super(itemView);
            cv =  itemView.findViewById(R.id.cv);
            eng = itemView.findViewById(R.id.eng);
            rus = itemView.findViewById(R.id.rus);
        }

    }
}

