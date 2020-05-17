package ie.droghedatabletop.psychiccasters;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ie.droghedatabletop.psychiccasters.adapter.ExpandableListAdapter;
import ie.droghedatabletop.psychiccasters.entities.Caster;
import ie.droghedatabletop.psychiccasters.view.CasterViewModel;

public class MainActivity extends AppCompatActivity {
    private CasterViewModel casterViewModel;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareListData();

        final Button startTurnButton = findViewById(R.id.button_load);
        startTurnButton.setOnClickListener(v -> {
            expListView = findViewById(R.id.lvExp);
            listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);
        });

        final Button endTurnButton = findViewById(R.id.button_end);
        endTurnButton.setOnClickListener(v -> {
            for (Switch childswitch : listAdapter.getSwitch()) {
                childswitch.setChecked(false);
            }
        });
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        casterViewModel = new ViewModelProvider(this).get(CasterViewModel.class);
        casterViewModel.getCasters().observe(this, casters -> {
            LiveData<List<Caster>> listOfCasters = casterViewModel.getCasters();
            List<Caster> casterList = listOfCasters.getValue();
            ArrayList<String> powers;
            // Adding child data
            assert casterList != null;
            for (Caster caster : casterList) {
                powers = new ArrayList<>(Arrays.asList(caster.getPowers().split(",")));
                listDataHeader.add(caster.getCasterName());
                listDataChild.put(caster.getCasterName(), powers);
            }
        });
    }
}
