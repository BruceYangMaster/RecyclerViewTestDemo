package com.deepblue.recyclerviewtestdemo;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.LayoutManager;
import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by gerry on 2016/3/25.
 * adapter包装类 实现任意layoutmanager加head foot 加载更多 点击事件回调 等
 */

public class RecyclerViewAdapterPaker extends Adapter {
    private final int HEAD = 0X100;
    private final int FOOT = 0X110;
    private final int LOAD_MORE = 0X120;
    private boolean isEnd;
    private boolean isLoading;
    private ILoadMore loadMore;
    protected IOnClickListener onClickListener;
    private IOnItemClickListener onItemClickListener;
    private IBaseView head;
    private IBaseView foot;
    private IloadMoreView moreView;
    private Adapter adapter;
    private Context context;
    private Boolean isHeadFullSpan = true;
    private Boolean isFootFullSpan = true;
    private Boolean canLoadMore = true;

    public RecyclerViewAdapterPaker(Adapter adapter, Context context) {
        this.adapter = adapter;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEAD) {
            return new HeadViewHolder(head.onCreateView(parent));
        } else if (viewType == FOOT) {
            return new FootViewHolder(foot.onCreateView(parent));
        } else if (viewType == LOAD_MORE) {
            if (moreView == null) {
                return new LoadMoreHolder(LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.load_more, parent, false));
            } else {
                return new LoadMoreHolder(moreView.onCreateView(parent));
            }
        } else {
            final ViewHolder viewHolder = adapter.onCreateViewHolder(parent, viewType);
            viewHolder.itemView.setOnClickListener(listener);
            return viewHolder;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder instanceof HeadViewHolder) {
            head.onBindView();
        } else if (holder instanceof FootViewHolder) {
            foot.onBindView();
        } else if (holder instanceof LoadMoreHolder) {
            if (loadMore != null && !isLoading && !isEnd) {
                isLoading = true;
                loadMore.loadMore();
            }
            if (moreView != null) {
                moreView.onBindView(isEnd);
            } else {
                LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
                if (isEnd) {
                    loadMoreHolder.progressBarLoad.setVisibility(View.GONE);
                    loadMoreHolder.tvLoad.setText(context.getString(R.string.no_more));
                } else {
                    loadMoreHolder.progressBarLoad.setVisibility(View.VISIBLE);
                    loadMoreHolder.tvLoad.setText(context.getString(R.string.loading));
                }
            }
        } else {
            holder.itemView.setTag(position);
            adapter.onBindViewHolder(holder, head != null ? position - 1 : position);
        }

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClickListener(head != null ? (Integer) v.getTag() - 1 : (Integer) v.getTag());
        }
    };

    @Override
    public int getItemCount() {
        return adapter.getItemCount() == 0 ? ((head != null ? 1 : 0) + (foot != null ? 1 : 0)) : ((head != null ? 1 : 0) + (foot != null ? 1 : 0)) + (canLoadMore ? 1 : 0) + adapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (head != null && position == 0) {
            return HEAD;
        } else if (foot != null && position == ((head != null ? 1 : 0) + adapter.getItemCount())) {
            return FOOT;
        } else if (canLoadMore && position == ((head != null ? 1 : 0) + (foot != null ? 1 : 0) + adapter.getItemCount())) {
            return LOAD_MORE;
        } else {
            return adapter.getItemViewType(position);
        }
    }


    public interface ILoadMore {
        void loadMore();
    }

    class LoadMoreHolder extends ViewHolder {
        @BindView(R.id.tv_load)
        TextView tvLoad;
        @BindView(R.id.progressBar_load)
        ProgressBar progressBarLoad;

        LoadMoreHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface IOnItemClickListener {
        void onItemClickListener(int position);
    }

    public interface IOnClickListener {
        void onclickListener(View view, int pisition);
    }

    public void setOnClickListener(IOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setIsHeadFullSpan(Boolean isHeadFullSpan) {
        this.isHeadFullSpan = isHeadFullSpan;
    }

    public void setIsFootFullSpan(Boolean isFootFullSpan) {
        this.isFootFullSpan = isFootFullSpan;
    }

    public void setCanLoadMore(Boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    class HeadViewHolder extends ViewHolder {
        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    class FootViewHolder extends ViewHolder {
        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setHead(IBaseView head) {
        this.head = head;
    }

    public void setFoot(IBaseView foot) {
        this.foot = foot;
    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    public void setOnItemClickListener(IOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setMoreView(IloadMoreView moreView) {
        this.moreView = moreView;
    }

    public void setLoadMoreComplete() {
        isLoading = false;
    }

    @Override
    public final void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridManager.getSpanSizeLookup();
            gridManager
                    .setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            int size;
                            if ((isHeadFullSpan && getItemViewType(position) == HEAD) || (isFootFullSpan && getItemViewType(position) == FOOT) || getItemViewType(position) == LOAD_MORE) {
                                size = gridManager
                                        .getSpanCount();
                            } else if (spanSizeLookup != null) {
                                size = spanSizeLookup.getSpanSize(position);
                            } else {
                                size = 1;
                            }
                            return size;
                        }
                    });

        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp instanceof StaggeredGridLayoutManager.LayoutParams
                && ((isHeadFullSpan && holder.getItemViewType() == HEAD)
                || (isFootFullSpan && holder.getItemViewType() == FOOT)
                || holder.getItemViewType() == LOAD_MORE)) {
            ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(true);
        }
    }

    public void setIsEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

}
