package com.sdbi.delightfulmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MusicAdapter extends BaseAdapter {
    ArrayList<MusicInfo> Musiclist;
    LayoutInflater inflater;
    ImageView mmitemiv;
    Context context;

    public MusicAdapter(Context context, ArrayList<MusicInfo> Musiclist) {
        this.Musiclist = Musiclist;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return Musiclist.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.music_list_item, null);
            holder = new ViewHolder();
            holder.mmitemtv1 = view.findViewById(R.id.mmitemtv1);
            holder.mmitemtv2 = view.findViewById(R.id.mmitemtv2);
            holder.mmitemiv = view.findViewById(R.id.mmitemiv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.mmitemtv1.setText(Musiclist.get(position).getName());
        holder.mmitemtv2.setText(Musiclist.get(position).getArtist());

        return view;
    }


    public class ViewHolder {
        public TextView mmitemtv1;
        public TextView mmitemtv2;
        public ImageView mmitemiv;
    }
}
