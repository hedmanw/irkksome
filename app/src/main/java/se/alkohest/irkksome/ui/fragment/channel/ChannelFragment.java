package se.alkohest.irkksome.ui.fragment.channel;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.alkohest.irkksome.R;
import se.alkohest.irkksome.model.entity.IrcChannel;
import se.alkohest.irkksome.model.entity.IrcMessage;
import se.alkohest.irkksome.ui.view.ChatListView;

public class ChannelFragment extends Fragment {
    private static ArrayAdapter<ChannelChatItem> arrayAdapter;
    private static List<ChannelChatItem> messageList;
    private static IrcChannel ircChannel;
    private static ChatListView messageListView;
    private static OnMessageSendListener activity;

    public static ChannelFragment newInstance(IrcChannel ircChannel) {
        ChannelFragment fragment = new ChannelFragment();
        ChannelFragment.ircChannel = ircChannel;
        return fragment;
    }

    public ChannelFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(ircChannel.getName());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        messageList = new ArrayList<>(ircChannel.getMessages().size());
        for (IrcMessage message : ircChannel.getMessages()) {
            addMessage(message);
        }
        ChannelFragment.activity = (OnMessageSendListener) activity;
        ChannelFragment.arrayAdapter = new ChannelArrayAdapter(activity.getApplicationContext(), messageList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View inflatedView = inflater.inflate(R.layout.fragment_channel, container, false);
        final EditText messageEditText = ((EditText) inflatedView.findViewById(R.id.input_field));
        messageEditText.setHint("Send message to " + ircChannel.getName());
        messageEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int keyCode, KeyEvent keyEvent) {
                if (keyCode == EditorInfo.IME_ACTION_SEND) {
                    activity.sendMessage(null);
                    return true;
                }
                return false;
            }
        });

        messageListView = (ChatListView) inflatedView.findViewById(R.id.listView);
        messageListView.setAdapter(arrayAdapter);
        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final ChannelChatItem chatItem = messageList.get(i);
                if (chatItem.getViewType() == ChannelChatItem.ChatItemTypeEnum.MESSAGE) {
                    messageEditText.append(((MessageItem) chatItem).getMessage().getAuthor().getName() + ": ");
                }
            }
        });
        messageListView.setOnSizeChangedListener(new ChatListView.OnSizeChangedListener() {
            @Override
            public void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
                if (oldHeight > newHeight) {
                    scrollToBottom();
                }
            }
        });

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(messageEditText, 0);
        messageEditText.requestFocus();
        scrollToBottom();
        return inflatedView;
    }

    private static boolean hasBacklog() {
        return messageListView.getLastVisiblePosition() < arrayAdapter.getCount()-1;
    }

    public static void scrollToBottom() {
        if (arrayAdapter != null) {
            messageListView.setSelection(arrayAdapter.getCount() - 1);
        }
    }

    public static void receiveMessage(IrcMessage message) {
        if (arrayAdapter != null) {
            addMessage(message);
            arrayAdapter.notifyDataSetChanged();
            scrollToBottom();
        }
    }

    private static void addMessage(IrcMessage message) {
        final IrcMessage.MessageTypeEnum messageType = message.getMessageType();
        if (messageType == IrcMessage.MessageTypeEnum.RECEIVED || messageType == IrcMessage.MessageTypeEnum.SENT) {
            messageList.add(new MessageItem(message));
        }
        else {
            messageList.add(new InfoItem(message));
        }
    }

    public interface OnMessageSendListener {
        public void sendMessage(View view);
    }
}
