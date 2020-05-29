package com.dpaksoni.assignment.tableview;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dpaksoni.assignment.R;
import com.dpaksoni.assignment.models.CountryCount;
import com.dpaksoni.assignment.tableview.holder.CellViewHolder;
import com.dpaksoni.assignment.tableview.holder.ColumnHeaderViewHolder;
import com.dpaksoni.assignment.tableview.holder.RowHeaderViewHolder;
import com.dpaksoni.assignment.tableview.model.CellModel;
import com.dpaksoni.assignment.tableview.model.ColumnHeaderModel;
import com.dpaksoni.assignment.tableview.model.RowHeaderModel;
import com.dpaksoni.assignment.viewmodel.CountViewModel;
import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractSorterViewHolder;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

import java.util.List;

/**
 * Created by evrencoskun on 27.11.2017.
 */

public class CountryTableAdapter extends AbstractTableAdapter<ColumnHeaderModel, RowHeaderModel,
        CellModel> {

    private CountViewModel myTableViewModel;

    public CountryTableAdapter(Context p_jContext) {
        super(p_jContext);
    }

    @Override
    public AbstractViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.tableview_cell_layout, parent, false);

        return new CellViewHolder(layout);

    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object p_jValue, int
            p_nXPosition, int p_nYPosition) {
        CellModel cell = (CellModel) p_jValue;

        ((CellViewHolder) holder).setCellModel(cell, p_nXPosition);
        if(p_nYPosition == 0) {
            ((CellViewHolder) holder).cell_textview.setTypeface(null, Typeface.BOLD);
        }
        else {
            ((CellViewHolder) holder).cell_textview.setTypeface(null, Typeface.NORMAL);
        }
    }

    @Override
    public AbstractSorterViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout
                .tableview_column_header_layout, parent, false);

        return new ColumnHeaderViewHolder(layout, getTableView());
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object p_jValue, int
            p_nXPosition) {
        ColumnHeaderModel columnHeader = (ColumnHeaderModel) p_jValue;

        // Get the holder to update cell item text
        ColumnHeaderViewHolder columnHeaderViewHolder = (ColumnHeaderViewHolder) holder;

        columnHeaderViewHolder.setColumnHeaderModel(columnHeader, p_nXPosition);
    }

    @Override
    public AbstractViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {

        // Get Row Header xml Layout
        View layout = LayoutInflater.from(mContext).inflate(R.layout.tableview_row_header_layout,
                parent, false);

        // Create a Row Header ViewHolder
        return new RowHeaderViewHolder(layout);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object p_jValue, int
            p_nYPosition) {

        RowHeaderModel rowHeaderModel = (RowHeaderModel) p_jValue;

        RowHeaderViewHolder rowHeaderViewHolder = (RowHeaderViewHolder) holder;
        rowHeaderViewHolder.row_header_textview.setText(rowHeaderModel.getData());

    }

    @Override
    public View onCreateCornerView() {
        return LayoutInflater.from(mContext).inflate(R.layout.tableview_corner_layout, null, false);
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCellItemViewType(int position) {
        return 0;
    }


    /**
     * This method is not a generic Adapter method. It helps to generate lists from single user
     * list for this adapter.
     */
    public void setCountryList(List<ColumnHeaderModel> headerModels,
                               List<RowHeaderModel> rowHeaderModels, List<List<CellModel>> cellModels) {

        // Now we got what we need to show on TableView.
        setAllItems(headerModels, rowHeaderModels, cellModels);
    }
}
