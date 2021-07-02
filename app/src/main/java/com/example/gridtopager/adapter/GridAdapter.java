package com.example.gridtopager.adapter;

import static com.example.gridtopager.adapter.ImageData.IMAGE_DRAWABLES;

import android.graphics.drawable.Drawable;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.gridtopager.MainActivity;
import com.example.gridtopager.R;
import com.example.gridtopager.fragment.GridFragment;
import com.example.gridtopager.fragment.ImageFragment;
import com.example.gridtopager.fragment.ImagePagerFragment;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 展示图片网格的fragment
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ImageViewHolder> {

    /**
     * 处理ViewHolders中图片加载事件和点击事件的监听器
     */
    private interface ViewHolderListener {

        void OnLoadCompleted(ImageView imageView, int adapterPosition);

        void onItemClick(View view, int adapterPosition);
    }

    private final RequestManager requestManager;
    private final ViewHolderListener viewHolderListener;

    public GridAdapter(Fragment fragment) {
        this.requestManager = Glide.with(fragment);
        this.viewHolderListener = new ViewHolderListenerImpl(fragment);
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_card, parent, false);
        return new ImageViewHolder(view, requestManager, viewHolderListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        // 视图绑定数据
        holder.onBind();
    }

    @Override
    public int getItemCount() {
        return IMAGE_DRAWABLES.length;
    }

    private static class ViewHolderListenerImpl implements ViewHolderListener {

        private final Fragment fragment;
        private AtomicBoolean enterTransitionStarted;

        public ViewHolderListenerImpl(Fragment fragment) {
            this.fragment = fragment;
            this.enterTransitionStarted = new AtomicBoolean();
        }

        @Override
        public void OnLoadCompleted(ImageView imageView, int adapterPosition) {
            // Call startPostponedEnterTransition only when the 'selected' image loading is completed.
            if (MainActivity.currentPosition != adapterPosition) {
                return;
            }
            if (enterTransitionStarted.getAndSet(true)) {
                return;
            }
            fragment.startPostponedEnterTransition();
        }

        @Override
        public void onItemClick(View view, int adapterPosition) {

            MainActivity.currentPosition = adapterPosition;

            ImageView imageView = view.findViewById(R.id.card_image);

            ((TransitionSet) fragment.getExitTransition()).excludeTarget(view, true);

            fragment.getFragmentManager()
                    .beginTransaction()
                    // 设置自定义动画过渡效果
//                    .setCustomAnimations(
//                            R.anim.slide_in,
//                            R.anim.fade_out,
//                            R.anim.fade_in,
//                            R.anim.slide_out
//                    )
                    .setReorderingAllowed(true)
                    .addSharedElement(imageView, imageView.getTransitionName())
                    .replace(R.id.fragment_container, new ImagePagerFragment(), ImageFragment.class.getSimpleName())
                    .addToBackStack(null)
                    .commit();
        }
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        private final ImageView image;
        private final RequestManager requestManager;
        private final ViewHolderListener viewHolderListener;

        ImageViewHolder(@NonNull View itemView, RequestManager requestManager,
                               ViewHolderListener viewHolderListener) {
            super(itemView);
            this.image = itemView.findViewById(R.id.card_image);
            this.requestManager = requestManager;
            this.viewHolderListener = viewHolderListener;
            itemView.findViewById(R.id.card_image).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            viewHolderListener.onItemClick(view, getAbsoluteAdapterPosition());
        }

        public void onBind() {
            int position = getAbsoluteAdapterPosition();
            setImage(position);
            image.setTransitionName(String.valueOf(IMAGE_DRAWABLES[position]));
        }

        private void setImage(final int position) {
            requestManager
                    .load(IMAGE_DRAWABLES[position])
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            viewHolderListener.OnLoadCompleted(image, position);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            viewHolderListener.OnLoadCompleted(image, position);
                            return false;
                        }
                    })
                    .into(image);
        }
    }
}
