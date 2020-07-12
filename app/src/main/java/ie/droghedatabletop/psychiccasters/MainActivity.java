package ie.droghedatabletop.psychiccasters;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;
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
        addCasterButton.setOnClickListener(addCaster -> addNewCaster());

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Caster");

        // Set up the input
        final EditText casterNameInput = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        casterNameInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(casterNameInput);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String casterName = casterNameInput.getText().toString();
            AlertDialog.Builder subAlert = new AlertDialog.Builder(builder.getContext());
            subAlert.setTitle("Add Powers (Separate with ,)");

            // Set up the input
            final EditText casterPowersInput = new EditText(builder.getContext());
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            casterPowersInput.setInputType(InputType.TYPE_CLASS_TEXT);
            subAlert.setView(casterPowersInput);

            // Set up the buttons
            subAlert.setPositiveButton("OK", (dialog1, which1) -> {
                String casterPowers = casterPowersInput.getText().toString();
                Caster newCaster = new Caster(casterName, casterPowers);
                if (listAdapter != null) {
                    casterViewModel.insert(newCaster);
                    listAdapter.addNewGroup(newCaster);
                    listDataHeader = listAdapter.getGroups();
                    listDataChild = listAdapter.getChildren();
                    loadList();
                }
            });
            subAlert.setNegativeButton("Cancel", (dialog12, which12) -> dialog12.cancel());

            subAlert.show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();

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
        listAdapter = new ExpandableListAdapter(MainActivity.this.getApplicationContext(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        expListView.refreshDrawableState();
        listAdapter.notifyDataSetInvalidated();
        listAdapter.notifyDataSetChanged();
    }
}
