package com.jokrapp.android;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jokrapp.R;
import com.jokrapp.android.data.model.Joke;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.ViewHolder> {
    private ArrayList<Joke> itemsData;

    private PopupMenu.OnMenuItemClickListener menuItemClickListener;

    private FavoritesFragment parent;


    public FavoritesListAdapter(ArrayList<Joke> itemsData, FavoritesFragment parent, PopupMenu.OnMenuItemClickListener menuItemClickListener) {
        this.itemsData = itemsData;
        this.menuItemClickListener = menuItemClickListener;
        this.parent = parent;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FavoritesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items, null);

        // create ViewHolder;
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        Joke joke = itemsData.get(position);
        if (joke.getTitle() != null && joke.getTitle().length() > 0) {
            viewHolder.itemTitle.setText(joke.getTitle());
        } else {
            viewHolder.itemTitle.setText("untitled");
        }
        if(joke.getUserName() != null) {
            viewHolder.itemLikes.setText("by " + joke.getUserName() + ", " + joke.getLikes() + " points");
        }else{
            viewHolder.itemLikes.setText("by unknown"  + ", " + joke.getLikes() + " points");
        }
        if (joke.getTags() != null && joke.getTags().length() > 0) {
            viewHolder.itemTags.setText(joke.getTags());
        } else {
            viewHolder.itemTags.setText("-");
        }
        viewHolder.menu.setTag(joke);
        viewHolder.playIcon.setTag(joke);
        viewHolder.playIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Joke j = (Joke) v.getTag();

                JokePlayer.getInstance().playJoke((Joke) v.getTag(),true);

            }
        });

        String url = Constants.USER_PIC_FETCH +"?userId="+ joke.getUserId();
        Picasso.with(parent.getContext()).load(url)
                .resize(110, 110).centerCrop().error(R.drawable.dots).transform(new CropCircleTransformation()).into(viewHolder.playIcon);

        viewHolder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(v.getTag());
                Joke j = (Joke) v.getTag();
                parent.setCurrentSelectedJoke(j);
                PopupMenu popup = new PopupMenu(v.getContext(), v);
                popup.setOnMenuItemClickListener(menuItemClickListener);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.favorites_jokeitem, popup.getMenu());
                popup.show();

            }
        });
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemTags;
        public TextView itemLikes;
        public TextView itemTitle;
        public ImageView playIcon;
        public ImageView menu;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            itemTitle = (TextView) itemLayoutView.findViewById(R.id.item_title);
            itemTags = (TextView) itemLayoutView.findViewById(R.id.item_tags);
            itemLikes = (TextView) itemLayoutView.findViewById(R.id.item_likes);
            playIcon = (ImageButton) itemLayoutView.findViewById(R.id.button_playJoke);
            menu = (ImageView) itemLayoutView.findViewById(R.id.button_list_menu);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.size();
    }
}

