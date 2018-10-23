package com.tushar.primesetups;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tushar.primesetups.fragments.setup_main;
import com.tushar.primesetups.models.Result;
import com.tushar.primesetups.models.tempdata;
import com.tushar.primesetups.utils.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Suleiman on 19/10/16.
 */

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int HERO = 2;


    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w150";

    private List<Result> appsResuls;
    private Context context;
    public com.tushar.primesetups.models.tempdata tempdata;

    private OnItemClicked onClick;
    private OnImageClicked onImageClick;


    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;

    public PaginationAdapter(Context context, PaginationAdapterCallback callback) {
        this.context = context;
        this.mCallback = callback;
        appsResuls = new ArrayList<>();
    }

    public interface OnItemClicked {
        void onItemClick(String name);
    }
    public interface OnImageClicked {
        void onImageClick(String imagepos,ImageView sharedimage);
    }

    public List<Result> getApps() {
        return appsResuls;
    }

    public void setApps(List<Result> appsResuls) {
        this.appsResuls = appsResuls;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.list, parent, false);
                viewHolder = new MovieVH(viewItem);
                break;
            case LOADING:
                if(MainActivity.layoutmanager.equals("stack")){
                    View viewLoading = inflater.inflate(R.layout.item_progressstack, parent, false);
                    viewHolder = new LoadingVH(viewLoading);
                }
                else{
                    View viewLoading = inflater.inflate(R.layout.item_progressgrid, parent, false);
                    viewHolder = new LoadingVH(viewLoading);
                }


                break;
//            case HERO:
//                View viewHero = inflater.inflate(R.layout.item_hero, parent, false);
//                viewHolder = new HeroVH(viewHero);
//                break;
        }
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Result result = appsResuls.get(position); // Movie

        switch (getItemViewType(position)) {




            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;
                ViewCompat.setTransitionName(movieVH.image,result.getSetup_name());
                movieVH.setupname.setText(result.getSetup_name());

tempdata = new tempdata();

                setup_main.TOTALCOUNT = 10;


                movieVH.openbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tempdata.setSetup_name(result.getSetup_name());
                        tempdata.setImage_url(result.getImage_url());
                        tempdata.setWall_name(result.getWall_name());
                        tempdata.setWall_url(result.getWall_url());
                        tempdata.setIcon_name(result.getIcon_name());
                        tempdata.setIcon_url(result.getIcon_url());
                        tempdata.setLauncher_name(result.getLauncher_name());
                        tempdata.setLauncher_url(result.getLauncher_url());
                        tempdata.setWid_name(result.getWid_name());
                        tempdata.setWid_url(result.getWid_url());
                        tempdata.setBc_url(result.getBc_url());
                        tempdata.setDev_name(result.getDev_name());
                        tempdata.setCount(result.getCount());
                        tempdata.setNote(result.getNote());

                        onImageClick.onImageClick("tk",movieVH.image);
                    }
                });



                Glide.with(context).load(result.getImage_url())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                return false;
                            }
                        }).into(movieVH.image);

                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return appsResuls == null ? 0 : appsResuls.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return (position == appsResuls.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        } else {
            return (position == appsResuls.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        }
    }



    public void add(Result r) {
        appsResuls.add(r);
        notifyItemInserted(appsResuls.size() - 1);
    }

    public void addAll(List<Result> moveResults) {
        for (Result result : moveResults) {
            add(result);
        }
    }

    public void remove(Result r) {
        int position = appsResuls.indexOf(r);
        if (position > -1) {
            appsResuls.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Result());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = appsResuls.size() - 1;
        Result result = getItem(position);

        if (result != null) {
            appsResuls.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Result getItem(int position) {
        return appsResuls.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(appsResuls.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Header ViewHolder
     */
    protected class HeroVH extends RecyclerView.ViewHolder {
        private TextView mMovieTitle;
        private TextView mMovieDesc;
        private TextView mYear; // displays "year | language"
        private ImageView mPosterImg;

        public HeroVH(View itemView) {
            super(itemView);

//            mMovieTitle = (TextView) itemView.findViewById(R.id.movie_title);
//            mMovieDesc = (TextView) itemView.findViewById(R.id.movie_desc);
//            mYear = (TextView) itemView.findViewById(R.id.movie_year);
//            mPosterImg = (ImageView) itemView.findViewById(R.id.movie_poster);
        }
    }

    /**
     * Main list's content ViewHolder
     */
    protected class MovieVH extends RecyclerView.ViewHolder {
        private TextView setupname;

        private ImageView image;
        private ImageView wishlist;
        private ProgressBar mProgress;
        private CardView openbtn;


        public MovieVH(View itemView) {
            super(itemView);

            setupname = (TextView) itemView.findViewById(R.id.setupname);

            image = (ImageView) itemView.findViewById(R.id.setupimage);
           // wishlist = (ImageView) itemView.findViewById(R.id.wishlist);
                if(MainActivity.layoutmanager.equals("grid")) {
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(0, 0, 0, 0);
                    image.setLayoutParams(lp);
                }
            openbtn = (CardView) itemView.findViewById(R.id.openbtn);

        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

                    showRetry(false, null);
                    mCallback.retryPageLoad();

                    break;
            }
        }
    }

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
    public void setOnImageClick(OnImageClicked onImageClick)
    {
        this.onImageClick=onImageClick;
    }


}
