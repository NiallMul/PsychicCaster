package ie.droghedatabletop.psychiccasters.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import ie.droghedatabletop.psychiccasters.R;
import ie.droghedatabletop.psychiccasters.entities.Caster;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<String>> _listDataChild;
    private List<Switch> switchList;
    private Switch childSwitch;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        switchList = new ArrayList<>();
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return Objects.requireNonNull(this._listDataChild.get(this._listDataHeader.get(groupPosition)))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert infalInflater != null;
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        childSwitch = convertView
                .findViewById(R.id.lblListItem);
        childSwitch.setText(childText);

        childSwitch.setOnLongClickListener(listener -> {
            showChildItemDialog(childText);
            return true;
        });
        switchList.add(childSwitch);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Objects.requireNonNull(this._listDataChild.get(this._listDataHeader.get(groupPosition)))
                .size();
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

    @SuppressLint("InflateParams")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert infalInflater != null;
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

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

    public List<Switch> getSwitch() {
        return switchList;
    }

    private void showChildItemDialog(String switchText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setTitle(switchText);
        View view = LayoutInflater.from(_context).inflate(R.layout.dialog_view, null);
        final EditText edit_text = view.findViewById(R.id.edit_dialog);
        edit_text.setText(switchText);
        builder.setView(view);
        builder.setNegativeButton("cancel", null);
        builder.setPositiveButton("confirm", (dialog, which) -> childSwitch.setText(edit_text.getText().toString()));
        builder.show();
    }

    public void addNewGroup(Caster newCaster) {
        _listDataHeader.add(newCaster.getCasterName());
        _listDataChild.put(newCaster.getCasterName(), Arrays.asList(newCaster.getPowers().split(",")));
    }

    public List<String> getGroups() {
        return _listDataHeader;
    }

    public HashMap<String, List<String>> getChildren() {
        return _listDataChild;
    }


}
