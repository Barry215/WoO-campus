package com.example.barry215.woo.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.barry215.woo.Main.Activity.EditActivity;
import com.example.barry215.woo.Main.Activity.PhotoActivity;
import com.example.barry215.woo.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by barry215 on 2016/4/5.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    public List<Bitmap> bmp2;
    public List<String> drr2;
    private Activity activity;
    private LayoutInflater mInflater;
    private OnItemClickLitener mOnItemClickLitener;
    private List<String> imgAddressList;

    public GalleryAdapter(Activity activity, List<Bitmap> bmp2, List<String> drr2 ,List<String> imgAddressList) {
        this.bmp2 = bmp2;
        this.drr2 = drr2;
        this.imgAddressList = imgAddressList;
        this.activity = activity;
        mInflater = LayoutInflater.from(activity);
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void updateData(List<Bitmap> bmp2,List<String> drr2,List<String> imgAddressList){
        this.bmp2 = bmp2;
        this.drr2 = drr2;
        this.imgAddressList = imgAddressList;
        notifyDataSetChanged();
    }

    public void updateData(List<Bitmap> bmp2,List<String> drr2){
        this.bmp2 = bmp2;
        this.drr2 = drr2;
        notifyDataSetChanged();
    }

    public List<String> getDrr(){
        return drr2;
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_published_grida, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalleryAdapter.ViewHolder holder, final int position) {

        if (position == bmp2.size()) {
            holder.iv.setImageBitmap(BitmapFactory.decodeResource(
                    activity.getResources(), R.drawable.icon_addpic_unfocused));
            holder.btn.setVisibility(View.GONE);
            if (position == 3) {
                holder.iv.setVisibility(View.GONE);
            }
        } else {
            holder.iv.setImageBitmap(bmp2.get(position));
            holder.btn.setVisibility(View.VISIBLE);
            holder.btn.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    PhotoActivity.bitmap.remove(position);
                    bmp2.get(position).recycle();
                    bmp2.remove(position);
                    drr2.remove(position);
                    imgAddressList.remove(position);
                    ((EditActivity) activity).updateImgList(imgAddressList);
                    notifyDataSetChanged();
                }
            });
        }
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return bmp2.size() < 3 ? bmp2.size() + 1 : 3;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.item_grida_image) ImageView iv;
        @Bind(R.id.item_grida_bt) Button btn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
