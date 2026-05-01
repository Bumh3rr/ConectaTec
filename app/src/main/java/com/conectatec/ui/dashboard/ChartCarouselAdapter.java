package com.conectatec.ui.dashboard;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChartCarouselAdapter extends RecyclerView.Adapter<ChartCarouselAdapter.PageViewHolder> {

    private final List<View> pages;

    public ChartCarouselAdapter(List<View> pages) {
        this.pages = pages;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View page = pages.get(viewType);
        ViewGroup p = (ViewGroup) page.getParent();
        if (p != null) p.removeView(page);
        page.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return new PageViewHolder(page);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {}

    @Override
    public int getItemCount() {
        return pages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class PageViewHolder extends RecyclerView.ViewHolder {
        PageViewHolder(@NonNull View v) {
            super(v);
        }
    }
}
