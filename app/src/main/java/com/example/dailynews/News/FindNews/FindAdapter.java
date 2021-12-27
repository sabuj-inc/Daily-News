package com.example.dailynews.News.FindNews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailynews.R;

import java.util.ArrayList;

public class FindAdapter extends RecyclerView.Adapter<FindAdapter.ViewHolder> {
    Context context;
    ArrayList<ExampleModel> exampleModel;
    private OnItemClickListener mListener;
    int selected_position = -1;
    public FindAdapter(Context context, ArrayList<ExampleModel> exampleModel) {
        this.context = context;
        this.exampleModel = exampleModel;
    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.find_layout,parent,false);
        return new ViewHolder(view,mListener);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(exampleModel.get(position).getFindString());
        if (position == selected_position) {
            holder.root.setCardBackgroundColor(context.getResources().getColor(R.color.card_select_background));
        } else {
            holder.root.setCardBackgroundColor(context.getResources().getColor(R.color.card_unselect_background));
        }


    }

    @Override
    public int getItemCount() {
        return exampleModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        CardView root;
        public ViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            textView = itemView.findViewById(R.id.findText);
            root = itemView.findViewById(R.id.root);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                            selected_position = position;
                            notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    }
}
