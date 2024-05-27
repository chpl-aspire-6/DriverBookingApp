package com.adanitownship.driver.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.adanitownship.driver.R;
import com.adanitownship.driver.networkResponse.BookingRequestListResponse;
import com.adanitownship.driver.utils.FincasysTextView;
import com.bumptech.glide.Glide;

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
        void onCallNowItemClickListener(BookingRequestListResponse.Booking booking);
        void onDropLocationItemClickListener(String pos ,BookingRequestListResponse.Booking booking);
        void onPickUpLocationItemClickListener(String pos ,BookingRequestListResponse.Booking booking);
        void onPickUpItemClickListener(BookingRequestListResponse.Booking booking);
        void onDropItemClickListener(String pos ,BookingRequestListResponse.Booking booking);
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

                    if (row.getDisplayRequestId().toLowerCase().contains(charString.toLowerCase()) ||row.getUserFullName().toLowerCase().contains(charString.toLowerCase()) || row.getPickupLocationName().toLowerCase().contains(charString.toLowerCase()) || row.getDropLocationName().toLowerCase().contains(charString.toLowerCase()) || row.getPickupDate().toLowerCase().contains(charString.toLowerCase()) || row.getPickupTime().toLowerCase().contains(charString.toLowerCase())) {
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

        Glide.with(context)
                        .load(searchbookingList.get(position).getUserPhoto())
                                .placeholder(R.drawable.vector_person)
                                        .into(holder.iv_profile_photo);

        holder.lin_PickUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sClickListener.onPickUpItemClickListener(searchbookingList.get(position));
            }
        });
        holder.lin_Drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sClickListener.onDropItemClickListener(String.valueOf(position),searchbookingList.get(position));
            }
        });
          holder.txt_MobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sClickListener.onCallNowItemClickListener(searchbookingList.get(position));
            }
        });
          holder.txt_ToLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sClickListener.onDropLocationItemClickListener(String.valueOf(position),searchbookingList.get(position));
            }
        });
          holder.txt_FromLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sClickListener.onPickUpLocationItemClickListener(String.valueOf(position),searchbookingList.get(position));
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
        holder.txt_req_id.setText(searchbookingList.get(position).getDisplayRequestId());
        holder.txt_AgentAmountDetail.setText("₹"+""+searchbookingList.get(position).getCompanyPaymentAmount());
        holder.txt_CustomerDetail.setText("₹"+""+searchbookingList.get(position).getUserPaymentAmount());
        holder.txt_TransportDetail.setText(searchbookingList.get(position).getTransport_details());

        holder.txt_FromLocation.setPaintFlags(holder.txt_FromLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.txt_ToLocation.setPaintFlags(holder.txt_ToLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.txt_MobileNumber.setPaintFlags(holder.txt_MobileNumber.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        if (searchbookingList.get(position).getRequestStatus().equalsIgnoreCase("3")) {
            holder.lin_accept.setVisibility(View.VISIBLE);
            holder.lin_Reject.setVisibility(View.VISIBLE);
            holder.lin_Drop.setVisibility(View.GONE);
            holder.lin_PickUP.setVisibility(View.GONE);
        } else if (searchbookingList.get(position).getRequestStatus().equalsIgnoreCase("5")) {
            holder.lin_accept.setVisibility(View.GONE);
            holder.lin_PickUP.setVisibility(View.VISIBLE);
            holder.lin_Reject.setVisibility(View.GONE);
            holder.lin_Drop.setVisibility(View.GONE);
        } else if (searchbookingList.get(position).getRequestStatus().equalsIgnoreCase("6")) {
            holder.lin_accept.setVisibility(View.GONE);
            holder.lin_Reject.setVisibility(View.GONE);
            holder.lin_Drop.setVisibility(View.GONE);
            holder.lin_PickUP.setVisibility(View.GONE);
            holder.txt_Status.setText(searchbookingList.get(position).getRequestStatusName());
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

        LinearLayout lin_accept, lin_Reject, lin_PickUP, lin_Drop , lin_StatusHeader , lin_Details   , lin_call ;
        ImageView iv_profile_photo , img_per , img_PickUp_location,img_Drop_location;
        TextView txt_TransportDetail,txt_FromLocation,txt_AgentAmountDetail,txt_CustomerDetail, txt_req_id,txt_ToLocation, txt_DateDetail, txt_timeDetail, txt_PersonName, txt_MobileNumber, txtPeopleCount, txtAccept, txtReject ,txt_Status ;

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
            txt_req_id = itemView.findViewById(R.id.txt_req_id);
            txt_DateDetail = itemView.findViewById(R.id.txt_DateDetail);
            txt_timeDetail = itemView.findViewById(R.id.txt_timeDetail);
            txt_PersonName = itemView.findViewById(R.id.txt_PersonName);
            txt_MobileNumber = itemView.findViewById(R.id.txt_MobileNumber);
            txtPeopleCount = itemView.findViewById(R.id.txtPeopleCount);
            txt_Status = itemView.findViewById(R.id.txt_Status);
            txt_TransportDetail = itemView.findViewById(R.id.txt_TransportDetail);
            txt_CustomerDetail = itemView.findViewById(R.id.txt_CustomerDetail);
            txt_AgentAmountDetail = itemView.findViewById(R.id.txt_AgentAmountDetail);
            iv_profile_photo = itemView.findViewById(R.id.iv_profile_photo);
            img_per = itemView.findViewById(R.id.img_per);
            img_PickUp_location = itemView.findViewById(R.id.img_PickUp_location);
            img_Drop_location = itemView.findViewById(R.id.img_Drop_location);
        }
    }
}
