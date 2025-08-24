package com.example.hungthinhapartmentmanagement.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.hungthinhapartmentmanagement.Model.Invoice;
import com.example.hungthinhapartmentmanagement.R;
import com.example.hungthinhapartmentmanagement.Activity.ManagementInvoiceUpdateActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ManagementInvoiceAdapter extends RecyclerView.Adapter<ManagementInvoiceAdapter.InvoiceViewHolder> {
    private List<Invoice> invoiceList;
    private Context context;

    public ManagementInvoiceAdapter(Context context, List<Invoice> invoiceList) {
        this.context = context;
        this.invoiceList = (invoiceList != null) ? invoiceList : new ArrayList<>();
    }

    @NonNull
    @Override
    public InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_invoice, parent, false);
        return new InvoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceViewHolder holder, int position) {
        Invoice invoice = invoiceList.get(position);

        // Set Bill Type
        String billType;
        switch (invoice.getFeeType().toLowerCase()) {
            case "service":
                billType = "Phí dịch vụ " + invoice.getMonth() + " " + invoice.getYear();
                break;
            case "water":
                billType = "Tiền nước " + invoice.getMonth() + " " + invoice.getYear();
                break;
            case "electricity":
                billType = "Tiền điện " + invoice.getMonth() + " " + invoice.getYear();
                break;
            case "parking":
                billType = "Phí gửi xe " + invoice.getMonth() + " " + invoice.getYear();
                break;
            case "other":
                billType = "Phí phát sinh " + invoice.getMonth() + " " + invoice.getYear();
                break;
            default:
                billType = invoice.getFeeType() + " " + invoice.getMonth() + " " + invoice.getYear();
        }
        holder.tvBillType.setText(billType);

        // Set Bill Price
        String billPrice = (invoice.getMoney() != null ? invoice.getMoney() : "0") + "đ";
        holder.tvBillPrice.setText(billPrice);

        // Set Bill Status
        if (invoice.isStatus()) {
            holder.tvBillStatus.setText("Đã thanh toán");
            holder.tvBillStatus.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.tvBillStatus.setText("Chưa thanh toán");
            holder.tvBillStatus.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }

        // Set Bill Date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dueDateStr = invoice.getDueDate() != null ? "Hạn: " + sdf.format(invoice.getDueDate().toDate()) : "Hạn: N/A";
        Date currentDate = new Date();
        if (invoice.getDueDate() != null && invoice.getDueDate().toDate().before(currentDate) && !invoice.isStatus()) {
            dueDateStr += " (Quá hạn)";
            holder.tvBillDate.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else if (invoice.isStatus()) {
            holder.tvBillDate.setTextColor(context.getResources().getColor(android.R.color.black));
        } else {
            holder.tvBillDate.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        }
        holder.tvBillDate.setText(dueDateStr);

        // Set Bill Note
        String note = invoice.getNote() != null ? invoice.getNote() : "";
        holder.tvBillNote.setText("Ghi chú: " + note);

        // Add click event for all invoices
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ManagementInvoiceUpdateActivity.class);
            intent.putExtra("apartmentId", invoice.getApartmentId());
            intent.putExtra("feeType", invoice.getFeeType());
            intent.putExtra("money", invoice.getMoney());
            intent.putExtra("month", invoice.getMonth());
            intent.putExtra("note", invoice.getNote());
            intent.putExtra("year", invoice.getYear());
            intent.putExtra("dueDate", invoice.getDueDate() != null ? invoice.getDueDate().toDate().getTime() : 0);
            intent.putExtra("status", invoice.isStatus());
            intent.putExtra("documentID", invoice.getDocumentID());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    public void updateInvoices(List<Invoice> newInvoices) {
        this.invoiceList = (newInvoices != null) ? newInvoices : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class InvoiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvBillType, tvBillPrice, tvBillStatus, tvBillDate, tvBillNote;

        public InvoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBillType = itemView.findViewById(R.id.tvBillType);
            tvBillPrice = itemView.findViewById(R.id.tvBillPrice);
            tvBillStatus = itemView.findViewById(R.id.tvBillStatus);
            tvBillDate = itemView.findViewById(R.id.tvBillDate);
            tvBillNote = itemView.findViewById(R.id.tvBillNote);
        }
    }
}