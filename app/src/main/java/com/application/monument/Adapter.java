package com.application.monument;


import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.application.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Locale;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {

    Context context;//объект для базовых функции
    ArrayList<ItemMonument> data;//данные
    ArrayList<ItemMonument> arrayList;//для поиска
    //позволяет получить доступ к лэйаутам
    private LayoutInflater layoutInflater;
    public Adapter(Context context, ArrayList<ItemMonument> data) {
        this.context = context;
        this.data = data;
        this.arrayList = data;
    }
    @NonNull
    @Override
    //onCreateViewHolder() создает новый объект ViewHolder в тот момент, когда создаётся layout строки списка
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       layoutInflater = LayoutInflater.from(context);
       View v = layoutInflater.inflate(R.layout.card_item,viewGroup,false);
        return new ViewHolder(v);
    }
    @Override
    // Принимает объект ViewHolder и устанавливает необходимые данные для соответствующей строки
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ItemMonument mon = getItemMon(i);
        //Glide качает изображение с сайта и загружает их в кеш
        Glide.with(context)
                .load(data.get(i).getImg())
                .error(R.color.non)
                .placeholder(R.drawable.logonontext)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.imageView);

        viewHolder.textView.setText(data.get(i).getName());
        viewHolder.textView1.setText(data.get(i).getDescription());


    }
    public Object getItem(int position) {
        return data.get(position);
    }
    private ItemMonument getItemMon(int position){
        return (ItemMonument) getItem(position);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                ArrayList<ItemMonument> filteredList = new ArrayList<ItemMonument>();
                if(constraint == null || constraint.length() == 0){
                    filteredList.addAll(arrayList);
                } else {
                    String filterPatern = constraint.toString().toLowerCase().trim();
                    for(ItemMonument itemMonument : arrayList){
                        if(itemMonument.getName().toLowerCase(Locale.getDefault()).contains(filterPatern)){
                            filteredList.add(itemMonument);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayList.clear();
                arrayList.addAll((ArrayList)results.values);
                notifyDataSetChanged();
            }

        };
    }

    // Класс для нахождения нужных элементов в разметке
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView,textView1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView2);
            textView = itemView.findViewById(R.id.nametxt);
            textView1 = itemView.findViewById(R.id.descriptiontxt);

        }
    }

}
