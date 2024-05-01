package com.adanitownship.driver.network.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.adanitownship.driver.R;
import com.adanitownship.driver.networkResponse.BookingRequestListResponse;

import java.util.ArrayList;
import java.util.List;

public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.MyViewHolder> {

    List<BookingRequestListResponse.Booking> bookingList;
    List<BookingRequestListResponse.Booking> searchbookingList;
    Context context;
    public static ItemSingleClickListener sClickListener;


    public RequestListAdapter(List<BookingRequestListResponse.Booking> bookingList, Context context) {
        this.bookingList = bookingList;
        this.searchbookingList = bookingList;
        this.context = context;
    }

    public interface ItemSingleClickListener {
        void onAcceptItemClickListener(BookingRequestListResponse.Booking booking);

        void onRejectItemClickListener(BookingRequestListResponse.Booking booking);
        void onPickUpItemClickListener(BookingRequestListResponse.Booking booking);
        void onDropItemClickListener(BookingRequestListResponse.Booking booking);
    }

    public void setOnItemClickListener(ItemSingleClickListener clickListener) {
        sClickListener = clickListener;
    }


    @SuppressLint("NotifyDataSetChanged")
    public void update(List<BookingRequestListResponse.Booking> bookingList1) {
        this.bookingList = bookingList1;
        this.searchbookingList = bookingList1;
        notifyDataSetChanged();
    }


    public void search(CharSequence charSequence, RelativeLayout linearLayout, RecyclerView recyclerView) {

        try {
            String charString = charSequence.toString().trim();
            if (charString.isEmpty()) {
                searchbookingList = bookingList;
                recyclerView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
            } else {
                int flag = 0;
                List<BookingRequestListResponse.Booking> filteredList = new ArrayList<>();
                for (BookingRequestListResponse.Booking row : bookingList) {

                    if (row.getUserFullName().toLowerCase().contains(charString.toLowerCase()) || row.getPickupLocationName().toLowerCase().contains(charString.toLowerCase()) || row.getDropLocationName().toLowerCase().contains(charString.toLowerCase()) || row.getPickupDate().toLowerCase().contains(charString.toLowerCase()) || row.getPickupTime().toLowerCase().contains(charString.toLowerCase())) {
                        filteredList.add(row);
                        flag = 1;
                    }
                }

                if (flag == 1) {
                    searchbookingList = filteredList;
                    recyclerView.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }

            }
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_booking, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.lin_PickUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sClickListener.onPickUpItemClickListener(searchbookingList.get(position));
            }
        });
        holder.lin_Drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sClickListener.onDropItemClickListener(searchbookingList.get(position));
            }
        });
        holder.lin_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sClickListener.onAcceptItemClickListener(searchbookingList.get(position));
            }
        });
        holder.lin_Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sClickListener.onRejectItemClickListener(searchbookingList.get(position));
            }
        });
        holder.txt_FromLocation.setText(searchbookingList.get(position).getPickupLocationName());
        holder.txt_ToLocation.setText(searchbookingList.get(position).getDropLocationName());
        holder.txt_DateDetail.setText(searchbookingList.get(position).getPickupDate());
        holder.txt_timeDetail.setText(searchbookingList.get(position).getPickupTime());
        holder.txt_PersonName.setText(searchbookingList.get(position).getUserFullName());
        holder.txt_MobileNumber.setText(searchbookingList.get(position).getUserContactNumber());
        holder.txtPeopleCount.setText(searchbookingList.get(position).getPassengerCount());


        if (searchbookingList.get(position).getRequestStatus().equalsIgnoreCase("3")) {
            holder.lin_accept.setVisibility(View.VISIBLE);
            holder.lin_Reject.setVisibility(View.VISIBLE);
            holder.lin_Drop.setVisibility(View.GONE);
            holder.lin_PickUP.setVisibility(View.GONE);
//            holder.txt_Status.setText(searchbookingList.get(position).getRequestStatusName());
            holder.txt_Status.setTextColor(ContextCompat.getColor(context ,R.color.colorPurplePrimary));
            holder.lin_StatusHeader.setBackgroundColor(ContextCompat.getColor(context ,R.color.colorPurplePrimary_Light));
            holder.lin_Details.setBackgroundColor(ContextCompat.getColor(context ,R.color.colorPurplePrimary_LightBG));
        } else if (searchbookingList.get(position).getRequestStatus().equalsIgnoreCase("5")) {
            holder.lin_accept.setVisibility(View.GONE);
            holder.lin_PickUP.setVisibility(View.VISIBLE);
            holder.lin_Reject.setVisibility(View.GONE);
            holder.lin_Drop.setVisibility(View.GONE);
            holder.txt_Status.setTextColor(ContextCompat.getColor(context ,R.color.colorPurplePrimary));
            holder.lin_StatusHeader.setBackgroundColor(ContextCompat.getColor(context ,R.color.colorPurplePrimary_Light));
            holder.lin_Details.setBackgroundColor(ContextCompat.getColor(context ,R.color.colorPurplePrimary_LightBG));
        } else if (searchbookingList.get(position).getRequestStatus().equalsIgnoreCase("6")) {
            holder.lin_accept.setVisibility(View.GONE);
            holder.lin_Reject.setVisibility(View.GONE);
            holder.lin_Drop.setVisibility(View.GONE);
            holder.lin_PickUP.setVisibility(View.GONE);
            holder.lin_StatusHeader.setBackgroundColor(ContextCompat.getColor(context ,R.color.colorRedPrimary_LightBG));
            holder.lin_Details.setBackgroundColor(ContextCompat.getColor(context ,R.color.colorRedPrimary_Light));
            holder.txt_Status.setText(searchbookingList.get(position).getRequestStatusName());
            holder.txt_Status.setTextColor(ContextCompat.getColor(context ,R.color.colorRedPrimary));

//            holder.lin_Reject.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    holder.lin_Reject.setClickable(false);
//                    Toast.makeText(context, "You have already submitted the reason", Toast.LENGTH_LONG).show();
//                }
//            });
        } else if (searchbookingList.get(position).getRequestStatus().equalsIgnoreCase("7")) {
            holder.lin_accept.setVisibility(View.GONE);
            holder.lin_Drop.setVisibility(View.VISIBLE);
            holder.lin_Reject.setVisibility(View.GONE);
            holder.lin_PickUP.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return searchbookingList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lin_accept, lin_Reject, lin_PickUP, lin_Drop , lin_StatusHeader , lin_Details;
        TextView txt_FromLocation, txt_ToLocation, txt_DateDetail, txt_timeDetail, txt_PersonName, txt_MobileNumber, txtPeopleCount, txtAccept, txtReject , txt_Status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            lin_accept = itemView.findViewById(R.id.lin_accept);
            lin_Reject = itemView.findViewById(R.id.lin_Reject);
            txtAccept = itemView.findViewById(R.id.txtAccept);
            txtReject = itemView.findViewById(R.id.txtReject);
            lin_PickUP = itemView.findViewById(R.id.lin_PickUP);
            lin_Drop = itemView.findViewById(R.id.lin_Drop);
            lin_StatusHeader = itemView.findViewById(R.id.lin_StatusHeader);
            lin_Details = itemView.findViewById(R.id.lin_Details);
            txt_FromLocation = itemView.findViewById(R.id.txt_FromLocation);
            txt_ToLocation = itemView.findViewById(R.id.txt_ToLocation);
            txt_DateDetail = itemView.findViewById(R.id.txt_DateDetail);
            txt_timeDetail = itemView.findViewById(R.id.txt_timeDetail);
            txt_PersonName = itemView.findViewById(R.id.txt_PersonName);
            txt_MobileNumber = itemView.findViewById(R.id.txt_MobileNumber);
            txtPeopleCount = itemView.findViewById(R.id.txtPeopleCount);
            txt_Status = itemView.findViewById(R.id.txt_Status);


        }
    }
}