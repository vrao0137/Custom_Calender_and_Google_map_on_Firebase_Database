package com.example.firebasedatabaseproject.user.model;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.admin.adapter.ListUsersDataAdapter;

import java.util.HashMap;
import java.util.List;

public class ExpandableListMonthAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<NotesDataModel>> _listDataChild;
    ListUsersDataAdapter _listUsersDataAdapter;

    public ExpandableListMonthAdapter(Context context, List<String> listUserDateHeader, HashMap<String, List<NotesDataModel>> listUserDataChild) {
        this._context = context;
        this._listDataHeader = listUserDateHeader;
        this._listDataChild = listUserDataChild;
        this._listUsersDataAdapter = _listUsersDataAdapter;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final NotesDataModel childText = (NotesDataModel) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         //   convertView = infalInflater.inflate(R.layout.child_list, null);
            convertView = infalInflater.inflate(R.layout.admin_user_design, null);
        }

        TextView txtProjectName = (TextView) convertView.findViewById(R.id.txvProjectName);
        TextView txtDays = (TextView) convertView.findViewById(R.id.txvDay);
        TextView txtDates = (TextView) convertView.findViewById(R.id.txvDate);

        /*RecyclerView rcvListChild = (RecyclerView) convertView.findViewById(R.id.rcvUsersList);
        rcvListChild.setLayoutManager(new LinearLayoutManager(_context,  RecyclerView.VERTICAL, false));
        _listUsersDataAdapter = new ListUsersDataAdapter(_context,childText,this);
        rcvListChild.setAdapter(_listUsersDataAdapter);*/

        txtProjectName.setText(childText.getProjectName());
        txtDays.setText(childText.getWorkedHours());
        txtDates.setText(childText.getDate());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        final String childCount = String.valueOf(getChildrenCount(groupPosition));
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_header_month_name, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        TextView txtListChildCount = (TextView) convertView.findViewById(R.id.txvCountUsers);

        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        txtListChildCount.setTypeface(null, Typeface.BOLD);
        txtListChildCount.setText(childCount);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
