package com.adanitownship.driver.language;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.adanitownship.driver.R;
import com.adanitownship.driver.networkResponse.LanguageResponse;
import com.adanitownship.driver.utils.PreferenceManager;
import com.adanitownship.driver.utils.Tools;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.MyLanguageHolder> {

    private final Context context;
    private final List<LanguageResponse.Language> language;
    private LanguageInterface languageInterface;
    private final int autoSelectedItem;
    PreferenceManager preferenceManager;

    public void update() {
        notifyDataSetChanged();
    }

    private void set() {
        for (LanguageResponse.Language st : language) {
            st.setClicked(false);
        }
    }

    public void setUp(LanguageInterface up) {
        this.languageInterface = up;
    }

    public interface LanguageInterface {
        void click(LanguageResponse.Language st);
    }


    public LanguageAdapter(Context context, List<LanguageResponse.Language> language) {
        this.context = context;
        this.language = language;
        autoSelectedItem = 0;
        preferenceManager = new PreferenceManager(context);
    }

    @NonNull
    @Override
    public MyLanguageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_language_new, parent, false);

        return new MyLanguageHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyLanguageHolder h, int position) {

        h.tvLanguageName.setText(language.get(position).getLanguageName());
        h.tvLanguageSecondary.setText(language.get(position).getLanguageName1());

        /*if (language.get(position).getLanguageName1() != null && language.get(position).getLanguageName1().length() > 0) {
            h.tvLanguageName.setText(language.get(position).getLanguageName());
            h.tvLanguageSecondary.setText(language.get(position).getLanguageName1());
        } else {
            h.tvLanguageName.setText(language.get(position).getLanguageName());
        }*/

        Tools.displayBusinessCardLogo(context, h.languageIcon, language.get(position).getLanguageIcon());

        if (language.get(position).isClicked()) {

            h.tvLanguageSelection.setVisibility(View.VISIBLE);
            h.relLayItemView.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_border_language_green));

            h.itemView.setOnClickListener(v -> {
                set();
                language.get(position).setClicked(false);
                if (languageInterface != null) {
                    languageInterface.click(language.get(position));
                }
            });
        } else {
            h.relLayItemView.setBackground(ContextCompat.getDrawable(context,R.drawable.bg_border_language));
            h.tvLanguageSelection.setVisibility(View.GONE);

            h.itemView.setOnClickListener(v -> {
                set();
                language.get(position).setClicked(true);
                if (languageInterface != null) {
                    languageInterface.click(language.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return language.size();
    }

    static class MyLanguageHolder extends RecyclerView.ViewHolder {


        TextView tvLanguageName,tvLanguageSecondary;
        ImageView tvLanguageSelection,languageIcon;
        RelativeLayout relLayItemView;


        public MyLanguageHolder(@NonNull View itemView) {
            super(itemView);
          tvLanguageName = itemView.findViewById(R.id.tvLanguageName);
            tvLanguageSecondary = itemView.findViewById(R.id.tvLanguageSecondary);
            tvLanguageSelection = itemView.findViewById(R.id.tvLanguageSelection);
            languageIcon = itemView.findViewById(R.id.languageIcon);
            relLayItemView = itemView.findViewById(R.id.relLayItemView);
        }
    }
}
