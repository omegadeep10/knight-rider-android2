package edu.mga.knight_rider.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.mga.knight_rider.MainActivity;
import edu.mga.knight_rider.R;
import edu.mga.knight_rider.models.Message;
import edu.mga.knight_rider.models.Passenger;
import edu.mga.knight_rider.models.Trip;

/**
 * Created by omega on 022, Mar, 22.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Message> messages;

    public ChatAdapter(Context context, ArrayList<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message currentMessage = messages.get(position);
        SimpleDateFormat dt = new SimpleDateFormat("MM/dd/YYYY - hh:mm a", Locale.US);
        dt.setTimeZone(TimeZone.getDefault());

        Glide.with(context)
                .load(currentMessage.getProfilePicture())
                .into(holder.profilePic);

        holder.fullName.setText(currentMessage.getFirstName() + " " + currentMessage.getLastName());
        holder.chatMessage.setText(currentMessage.getComment());
        holder.chatDate.setText(dt.format(currentMessage.getLogDate()));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView profilePic;
        public TextView fullName;
        public TextView chatDate;
        public TextView chatMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            profilePic = (CircleImageView) itemView.findViewById(R.id.profile_pic);
            fullName = (TextView) itemView.findViewById(R.id.name);
            chatDate = (TextView) itemView.findViewById(R.id.time);
            chatMessage = (TextView) itemView.findViewById(R.id.chat_messsage);
        }
    }
}
