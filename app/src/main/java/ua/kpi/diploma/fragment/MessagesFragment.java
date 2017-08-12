package ua.kpi.diploma.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.bassaer.chatmessageview.models.Message;
import com.github.bassaer.chatmessageview.models.User;
import com.github.bassaer.chatmessageview.views.ChatView;

import ua.kpi.diploma.R;

/**
 * @author Mykola Yashchenko
 * todo load messages via websocket
 */
public class MessagesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.messages_content, container, false);

        ChatView chatView = (ChatView) view.findViewById(R.id.chat_view);

        chatView.setRightBubbleColor(ContextCompat.getColor(getContext(), R.color.green500));
        chatView.setLeftBubbleColor(Color.WHITE);

        chatView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.blueGray500));
        chatView.setSendButtonColor(ContextCompat.getColor(getContext(), R.color.cyan900));

        chatView.setRightMessageTextColor(Color.WHITE);
        chatView.setLeftMessageTextColor(Color.BLACK);

        chatView.setUsernameTextColor(Color.WHITE);
        chatView.setSendTimeTextColor(Color.WHITE);
        chatView.setDateSeparatorColor(Color.WHITE);
        chatView.setInputTextHint("Нове повідомлення");
        chatView.setMessageMarginTop(5);
        chatView.setMessageMarginBottom(5);

        User me = user(1, "Констянтин Петрович", R.drawable.patient_pic);
        User doctor1 = user(2, "Іван Іванович", R.drawable.patient_pic);

        // fake messages
        Message message1 = new Message.Builder().setUser(doctor1)
                .setMessageText("Доброго дня!")
                .build();

        Message message2 = new Message.Builder().setUser(me)
                .setMessageText("Вітаю!")
                .setRightMessage(true)
                .setUserIconVisibility(true)
                .build();

        Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.dermatit);
        Message message3 = new Message.Builder().setUser(doctor1)
                .setPicture(picture)
                .setType(Message.Type.PICTURE)
                .build();

        Message message4 = new Message.Builder().setUser(doctor1)
                .setMessageText("Направляю до вас хворого")
                .build();

        chatView.receive(message1);
        chatView.receive(message2);
        chatView.receive(message3);
        chatView.receive(message4);

        return view;
    }

    private User user(int id, String name, int icon) {
        Bitmap bitmapIcon = BitmapFactory.decodeResource(getResources(), icon);
        return new User(id, name, bitmapIcon);
    }
}
