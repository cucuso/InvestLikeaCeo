package com.eduardodennis.investlikeaceo;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import investlikeceo.eduardodennis.com.investlikeaceo.R;

import com.eduardodennis.investlikeaceo.data.Stock;


/**
 * Created by Eddie on 4/21/2015.
 */
public class StockAdapter extends RecyclerView.Adapter<StockViewHolder> {
    private List<Stock> stockList;
    private String percentageTxt;
    private String priceTxt;

    public StockAdapter(List<Stock> stockList) {
        this.stockList = stockList;
    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

         priceTxt = parent.getContext().getString(R.string.priceTxt);
         percentageTxt = parent.getContext().getString(R.string.percentageTxt);

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);

        return new StockViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(StockViewHolder holder, int position) {


        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        Stock stock = stockList.get(position);


        final String stSymbol = stock.getSymbol();
        holder.vTicker.setText(stock.getSymbol());
        holder.vPrice.setText(Html.fromHtml(priceTxt+"<b>"+formatter.format(stock.getPrice())+"</b>"));
        holder.vWeight.setText(Html.fromHtml(percentageTxt + "<b>%" + new DecimalFormat("#.##").format(stock.getWeight()).toString() + "</b>"));





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(view.getContext(), StockDetail.class);
                i.putExtra("ticker", stSymbol);
                view.getContext().startActivity(i);

            }

        });


    }



    @Override
    public int getItemCount() {
        return stockList.size();
    }
}


class StockViewHolder extends RecyclerView.ViewHolder {

    protected TextView vTicker;
    protected TextView vPrice;
    protected TextView vWeight;




    public StockViewHolder(View v) {
        super(v);
        vTicker = (TextView) v.findViewById(R.id.ticker);
        vPrice = (TextView) v.findViewById(R.id.price);
        vWeight = (TextView) v.findViewById(R.id.weight);


    }
}