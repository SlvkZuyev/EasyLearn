package com.slvk.words20.database.helpers;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.common.data.BitmapTeleporter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.slvk.words20.App;
import com.slvk.words20.PairOfWords;
import com.slvk.words20.database.AppDatabase;
import com.slvk.words20.database.DescriptionDao;
import com.slvk.words20.database.ThemeDescription;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {
    SQLHelper dbHelper;
    DatabaseReference myRef;
    FirebaseDatabase database;
    DescriptionDao themesDao;
    String[] savedThemes;
    String[] themesFromDb;
    Context context;
    //ThemeDescription.AppDatabase roomDB;

    public FirebaseHelper(SQLiteDatabase db, Context context){
        database = FirebaseDatabase.getInstance();
        dbHelper = new SQLHelper(db);
        this.context = context;

        AppDatabase roomDB = App.getInstance().getRoomDatabase();
        themesDao = roomDB.getDescriptionDao();
    }

    public void setContext(Context context){
        this.context = context;
    }

    //TODO OnThemeDeleted and OnThemeChanged
    public void setThemesListener(){
        refreshData();
        myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    refreshData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
/*
1. Удалить фото
2. Удалить слова
3. Удалить строку из описаний тем
 */
    private void deleteThemes(List<ThemeDescription> themeDescriptions){
        for(ThemeDescription td : themeDescriptions){
            deletePhoto(td.getTable_name());
            dbHelper.deleteTable(td.getTable_name());
            themesDao.delete(td);
        }
    }

    private void deletePhoto(String filePath){
        File imgToDelete = new File(filePath);
        if(imgToDelete.delete()){
            Log.e("DELETE", "Img deleting IS successful");
        } else {
            Log.e("DELETE", "Img deleting was not successful");
        }
    }



    private List<ThemeDescription> getListOfAddedThemes(List<ThemeDescription> savedThemes, List<ThemeDescription> loadedThemes){
        List<ThemeDescription> addedThemes = new ArrayList<>();
        for(ThemeDescription theme: loadedThemes){
            if(!savedThemes.contains(theme)){
                addedThemes.add(theme);
            }
        }
        return addedThemes;
    }

    private List<ThemeDescription> getListOfDeletedThemes(List<ThemeDescription> savedThemes, List<ThemeDescription> loadedThemes){
        List<ThemeDescription> deletedThemes = new ArrayList<>();
        for(ThemeDescription theme : savedThemes){
            if(!loadedThemes.contains(theme)){
                deletedThemes.add(theme);
            }
        }
        return deletedThemes;
    }

private List<ThemeDescription> getLoadedThemes(DataSnapshot dataSnapshot){
        List<ThemeDescription> loadedThemes = new ArrayList<>();
        for(DataSnapshot ds: dataSnapshot.getChildren()){
            loadedThemes.add(createThemeDescription(ds));
        }
        return loadedThemes;
}

private List<ThemeDescription> getSavedThemes(){
        return themesDao.getAllThemes();
}

    private void addThemes(List<ThemeDescription> addedThemes){
        for(ThemeDescription theme : addedThemes){
                saveTheme(theme);
        }
    }

    private ThemeDescription createThemeDescription(DataSnapshot ds) {
        ThemeDescription td = new ThemeDescription();

        td.setTheme_name(ds.child("name").getValue().toString());
        td.setTheme_description(ds.child("description").getValue().toString());
        td.setImageURL(ds.child("imageUrl").getValue().toString());

        return td;
    }

    private void saveTheme(ThemeDescription themeDescription){
        String themeName = themeDescription.getTheme_name(); //get name of theme
        themesDao.addDescription(themeDescription); //Добавляем тему в список тем
        saveImage(themeDescription.getImageURL(), themeDescription.getTheme_name());

        String tableName = String.valueOf(themesDao.getTableIdByThemeName(themeName));
        dbHelper.AddTable(tableName);

        myRef = database.getReference(themeName).child("words");
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                List<String> pair_ids = GetChildrenList(task.getResult());
                for(String id : pair_ids){
                        SavePairByID(id, themeName);
                }
            }
        });
    }

    private List<String> GetChildrenList(DataSnapshot dataSnapshot){
        List<String> children_list = new ArrayList<>();
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            children_list.add(ds.getKey());
        }
        return children_list;
    }

    private void SavePairByID(String id, String theme){
        myRef = database.getReference(theme).child("words");
        String table_name = themesDao.getTableIdByThemeName(theme);
        myRef.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                    PairOfWords pair = task.getResult().getValue(PairOfWords.class);
                    dbHelper.SavePair(pair, table_name);

            }
        });
    }

    private void refreshData(){
        myRef = database.getReference();
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                List<ThemeDescription> savedThemes = getSavedThemes(); //Список сохраненных тем
                List<ThemeDescription> loadedThemes = getLoadedThemes(task.getResult());//Список тем из базы

                List<ThemeDescription> deletedThemes = getListOfDeletedThemes(savedThemes, loadedThemes);
                List<ThemeDescription> addedThemes = getListOfAddedThemes(savedThemes, loadedThemes);

                if(!deletedThemes.isEmpty()){
                    deleteThemes(deletedThemes);
                }

                if(!addedThemes.isEmpty()){
                    addThemes(addedThemes);
                }
            }
        });
    }

    private void saveImage(String url, String themeName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imgRef = storage.getReferenceFromUrl(url);

        final long ONE_MEGABYTE = 1024 * 1024;
        imgRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                ContextWrapper cw = new ContextWrapper(context);
                File directory = cw.getDir("images", Context.MODE_PRIVATE);
                File file = new File(directory, themeName + ".png");
                if (!file.exists()) {
                    Log.d("path", file.toString());
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(file);
                        fos.write(bytes);
                        Log.d("TAG", "onSuccess: ");
                        themesDao.setPath(file.getPath(), themeName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context, "Небольшие технические шоколадки с загрузкой картинок", Toast.LENGTH_LONG).show();
            }
        });


    }



}




