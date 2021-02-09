package com.example.myfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity<CustomAdapter> extends AppCompatActivity {
   int images[]= {R.drawable.arabian,R.drawable.italian,R.drawable.north,R.drawable.south};
   String names[]={"ARABIAN","ITALIAN","NORTH INDIAN","SOUTH INDIAN"};
    String desc[]={"Arabian food","Italian food","SouthIndian food","North Indian food"};
      List<ItemsModel> itemsList = new ArrayList();

   GridView gridView;
    MainActivity.CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
              gridView=findViewById(R.id.gridView);
          for(int i=0;i<names.length;i++)
          {
              ItemsModel itemsModel=new ItemsModel(names[i],desc[i],images[i]);
              itemsList.add(itemsModel);
          }
     customAdapter = new MainActivity.CustomAdapter(itemsList,this);

          gridView.setAdapter((ListAdapter) customAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        MenuItem menuItem=menu.findItem(R.id.search_view);
        SearchView searchView=(SearchView) menuItem.getActionView();
       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
              customAdapter.getFilter().filter(newText);
               return true;
           }
       });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.search_view)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class CustomAdapter extends BaseAdapter implements Filterable {
         private List<ItemsModel> itemsModelList;
            private List<ItemsModel> itemsModelListFiltered;
           private Context context;

        public CustomAdapter(List<ItemsModel> itemsModelList, Context context) {
            this.itemsModelList = itemsModelList;
            this.itemsModelListFiltered = itemsModelList;
            this.context = context;

        }

        @Override
        public int getCount() {
            return itemsModelListFiltered.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view =getLayoutInflater().inflate(R.layout.row_items,null);
            ImageView imageView=view.findViewById(R.id.imageView);
            TextView tvNames=view.findViewById(R.id.tvName);
            TextView tvDesc= view.findViewById(R.id.tvDesc);

            imageView.setImageResource(itemsModelListFiltered.get(position).getImage());
            tvNames.setText(itemsModelListFiltered.get(position).getName());
            tvDesc.setText(itemsModelListFiltered.get(position).getDesc());
             view.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     startActivity(new Intent(MainActivity.this,ItemViewActivity.class).putExtra("item", (Parcelable) itemsModelListFiltered.get(position)));

                 }
             });
            return view;
        }

        @Override
        public Filter getFilter() {
            Filter filter =new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults=new FilterResults();
                     if(constraint==null||constraint.length()==0){
                         filterResults.count=itemsModelList.size();
                         filterResults.values=itemsModelList;
                     }
                     else {
                          String searchstr= constraint.toString().toLowerCase();
                          List<ItemsModel> resultData=new ArrayList<>();
                          for(ItemsModel itemsModel:itemsModelList)
                          {
                              if(itemsModel.getName().contains(searchstr)||itemsModel.getDesc().contains(searchstr)){

                              }
                              filterResults.count=resultData.size();
                              filterResults.values=resultData;
                          }
                     }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                itemsModelListFiltered= (List<ItemsModel>) results.values;
                notifyDataSetChanged();
                }
            };
            return null;
        }
    }
}