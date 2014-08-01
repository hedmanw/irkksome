package se.alkohest.irkksome.ui.fragment;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.alkohest.irkksome.R;

public class ConnectionController {
    public static List<ConnectionItem> ITEMS = new ArrayList<>();

    public enum ConnectionTypeEnum {
        NEW_CONNECTION, OLD_CONNECTION
    }

    static {
        addItem(new ConnectionMethod("Regular irkk", R.drawable.connection_icon_blue));
        addItem(new ConnectionMethod("Irssi proxy", R.drawable.connection_icon_purple));
    }

    private static void addItem(ConnectionItem item) {
        ITEMS.add(item);
    }

    public static abstract class ConnectionItem {
        private String representation;
        private ConnectionTypeEnum viewType;

        public ConnectionItem(ConnectionTypeEnum viewType, String representation) {
            this.representation = representation;
            this.viewType = viewType;
        }

        public abstract View getView(LayoutInflater inflater, View convertView);
        public abstract AbstractConnectionFragment getConnectionFragment();

        @Override
        public String toString() {
            return representation;
        }

        public ConnectionTypeEnum getViewType() {
            return viewType;
        }
    }

    public static class ConnectionMethod extends ConnectionItem {
        private int icon;

        public ConnectionMethod(String representation, int color) {
            super(ConnectionTypeEnum.NEW_CONNECTION, representation);
            this.icon = color;
        }

        @Override
        public View getView(LayoutInflater inflater, View convertView) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.server_connect_list_new_item, null);
            }
            Drawable shape = inflater.getContext().getResources().getDrawable(icon);
            View view = convertView.findViewById(R.id.server_connect_icon);
            view.setBackground(shape);
            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            tv.setText(toString());
            return convertView;
        }

        @Override
        public AbstractConnectionFragment getConnectionFragment() {
            return AbstractConnectionFragment.newInstance(icon);
        }
    }
}
