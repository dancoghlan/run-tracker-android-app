package com.dancoghlan.androidapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.dancoghlan.androidapp.R;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import static com.dancoghlan.androidapp.util.ProjectConstants.DATE;
import static com.dancoghlan.androidapp.util.ProjectConstants.DISTANCE;
import static com.dancoghlan.androidapp.util.ProjectConstants.PACE;

public class ViewRunsCursorAdaptor extends SimpleCursorAdapter {
    private int[] mFrom;
    private int[] mTo;
    private String[] mOriginalFrom;

    public ViewRunsCursorAdaptor(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        findColumns(c, from);
        this.mTo = to;
        mOriginalFrom = from;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final ViewBinder binder = getViewBinder();
        final int[] from = mFrom;
        final int[] to = mTo;

        for (int i = 0; i < mTo.length; i++) {
            final View v = view.findViewById(to[i]);
            if (v != null) {
                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, cursor, from[i]);
                }

                if (!bound) {
                    String text = cursor.getString(from[i]);
                    text = appendTextToValue(mOriginalFrom[i], text);
                    if (text == null) {
                        text = "";
                    }

                    if (v instanceof TextView) {
                        setViewText((TextView) v, text);
                    } else if (v instanceof ImageView) {
                        setViewImage((ImageView) v, text);
                    } else {
                        throw new IllegalStateException(v.getClass().getName() + " is not a " +
                                " view that can be bounds by this SimpleCursorAdapter");
                    }
                }
            }
        }
    }

    private void findColumns(Cursor c, String[] from) {
        if (c != null) {
            int i;
            int count = from.length;
            if (mFrom == null || mFrom.length != count) {
                mFrom = new int[count];
            }
            for (i = 0; i < count; i++) {
                mFrom[i] = c.getColumnIndexOrThrow(from[i]);
            }
        } else {
            mFrom = null;
        }
    }

    private String appendTextToValue(String from, String to) {
        if (StringUtils.isEmpty(to)) {
            return to;
        }
        switch (from) {
            case DISTANCE:
                return to.concat("km");
            case PACE:
                return to.concat("/km");
            case DATE:
                LocalDate date = LocalDate.parse(to);
                return date.toString("dd MMMM, yyyy");
            default:
                return to;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        ImageView imageView = view.findViewById(R.id.list_icon);
        if (position % 2 == 1) {
            imageView.setImageResource(R.drawable.heart);
        } else {
            imageView.setImageResource(R.drawable.heart2);
        }
        return view;
    }

}
