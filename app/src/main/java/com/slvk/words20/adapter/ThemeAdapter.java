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

import com.slvk.words20.App;
import com.slvk.words20.R;
import com.slvk.words20.database.DescriptionDao;
import com.slvk.words20.database.ThemeDescription;

import java.util.ArrayList;
import java.util.List;

public class ThemeAdapter  extends RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder> {

    private List<ThemeDescription> themes = new ArrayList<>();
    private CardView cv;

    public interface OnThemeClickListener{
        void onThemeClick(ThemeDescription theme);
    }

    public ThemeAdapter(OnThemeClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    public ThemeAdapter(){

    }

    public void setOnClickListener(OnThemeClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    OnThemeClickListener onClickListener;

    public void SetItems(List<ThemeDescription> themes){
        this.themes.addAll(themes);
        notifyDataSetChanged();
    }

    void ClearItems(){
        themes.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ThemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_theme, parent, false);
        return new ThemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThemeViewHolder holder, int position) {
        holder.bind(themes.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                // вызываем метод слушателя, передавая ему данные
                onClickListener.onThemeClick(themes.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return themes.size();
    }

    // Предоставляет прямую ссылку на каждый View-компонент
    // Используется для кэширования View-компонентов и последующего быстрого доступа к ним
    class ThemeViewHolder extends RecyclerView.ViewHolder {
        // Ваш ViewHolder должен содержать переменные для всех
        // View-компонентов, которым вы хотите задавать какие-либо свойства
        // в процессе работы пользователя со списком

        private TextView theme_title;
        private TextView theme_subtitle;
        private ImageView theme_image;

        public void bind(ThemeDescription theme){
            theme_title.setText(theme.getTheme_name());
            theme_subtitle.setText((theme.getTheme_description()));
            Bitmap image = getImageBitmapByThemeName(theme.getTheme_name());
            theme_image.setImageBitmap(image);
        }

        // Мы также создали конструктор, который принимает на вход View-компонент строкИ
        // и ищет все дочерние компоненты
        public ThemeViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            theme_title = itemView.findViewById(R.id.theme_title);
            theme_subtitle = itemView.findViewById(R.id.theme_subtitle);
            theme_image = itemView.findViewById(R.id.theme_image);

        }

        private Bitmap getImageBitmapByThemeName(String theme_name){
            DescriptionDao descriptionDao = App.getInstance().getRoomDatabase().getDescriptionDao();
            String path = descriptionDao.getImagePathByThemeName(theme_name);
            return BitmapFactory.decodeFile(path);
        }
    }
}
