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
    private LiveData<List<Caster>> listOfCasters;
    private List<Caster> casterList;
    private ArrayList<String> powers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        casterViewModel = new ViewModelProvider(this).get(CasterViewModel.class);
        prepareListData();

        final Button addCasterButton = findViewById(R.id.button_add_caster);
        addCasterButton.setOnClickListener(addCaster -> {
            addNewCaster();
        });

        final Button loadDataButton = findViewById(R.id.button_load);
        loadDataButton.setOnClickListener(loadCasters -> {
            loadList();
            loadDataButton.setEnabled(false);
        });

        final Button endTurnButton = findViewById(R.id.button_end);
        endTurnButton.setOnClickListener(resetSpells -> {
            for (Switch childswitch : listAdapter.getSwitch()) {
                childswitch.setChecked(false);
            }
        });
    }

    private void addNewCaster() {
        Caster newCaster = new Caster("new caster",
                "sample power,sample power");
        if (listAdapter != null) {
            casterViewModel.insert(newCaster);
            listAdapter.addNewGroup(newCaster);
            listDataHeader = listAdapter.getGroups();
            listDataChild = listAdapter.getChildren();
            loadList();
        }
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        casterViewModel.getCasters().observe(this, casters -> {
            listOfCasters = casterViewModel.getCasters();
            casterList = listOfCasters.getValue();
            // Adding child data
            assert casterList != null;
            for (Caster caster : casterList) {
                powers = new ArrayList<>(Arrays.asList(caster.getPowers().split(",")));
                listDataHeader.add(caster.getCasterName());
                listDataChild.put(caster.getCasterName(), powers);
            }
        });
    }

    synchronized private void loadList() {
        expListView = findViewById(R.id.lvExp);
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        //notifydatasetchanged does not seem to work with arrays
        //notifyDatasetinvalidated seems to tolerate this adapter
        listAdapter.notifyDataSetChanged();
    }
}
