//package com.neointernet.neo360.fragment;
//
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.neointernet.neo360.R;
//import com.neointernet.neo360.model.Video;
//
//import java.util.List;
//
//public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {
//
//    private final List<Video> mValues;
//    private final HomeFragment.OnListFragmentInteractionListener mListener;
//    private final static String TAG = "MyItemRecyclerAdapter";
//
//    public MyItemRecyclerViewAdapter(List<Video> items, HomeFragment.OnListFragmentInteractionListener listener) {
//        mValues = items;
//        mListener = listener;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.fragment_item, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position) {
//        String name = mValues.get(position).getName();
//        String size = mValues.get(position).getSize();
//
//        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(name);
//        holder.mContentView.setText(size);
//        Log.i(TAG, "position , name , size " + position + " " + name + " " + size);
//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    mListener.onListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return mValues.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public final View mView;
//        public final TextView mIdView;
//        public final TextView mContentView;
//        public Video mItem;
//
//        public ViewHolder(View view) {
//            super(view);
//            mView = view;
//            mIdView = (TextView) view.findViewById(R.id.list_id);
//            mContentView = (TextView) view.findViewById(R.id.list_content);
//        }
//
//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
//    }
//}
