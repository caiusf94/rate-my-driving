package com.caiusf.ratemydriving.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.caiusf.ratemydriving.R;
import com.caiusf.ratemydriving.activities.adapters.HistoryListAdapter;
import com.caiusf.ratemydriving.controllers.DatabaseHandler;
import com.caiusf.ratemydriving.controllers.DrivingActivityController;
import com.caiusf.ratemydriving.data.JourneyDO;
import com.caiusf.ratemydriving.utils.toast.ToastDisplayer;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The activity which represents the <b>History</b> menu of the app
 *
 * @author Caius Florea, 2017
 */
public class HistoryActivity extends AppCompatActivity {

    /**
     * The data set
     */
    private ArrayList<JourneyDO> journeyList;
    /**
     * The ListView for displaying the JourneyDO items
     */
    private ListView historyListview;
    /**
     * The TextView for displaying a message if the data set is empty
     */
    private TextView historyEmptyList;
    /**
     * The adapter used for displaying items retrieved from the database
     *
     * @see HistoryListAdapter
     */
    private HistoryListAdapter adapter;
    /**
     * The database handler used for CRUD operations
     *
     * @see DatabaseHandler
     */
    DatabaseHandler db;
    /**
     * The SharedPreferences object
     */
    private SharedPreferences preferences;


    /**
     * Set up the layout and the data for this activity
     *
     * @param savedInstanceState
     *                      not being used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        db = new DatabaseHandler(this);   //set up a connection to the database

        journeyList = new ArrayList<>();


        journeyList.addAll(db.getAllJourneys());   //populate the data set
        Collections.reverse(journeyList);   //reverse list items to sort them from oldest to latest

        historyListview = (ListView) findViewById(R.id.historyListview);
        historyEmptyList = (TextView) findViewById(R.id.historyEmptyList);

        adapter = new HistoryListAdapter(this, journeyList);


        historyListview.setAdapter(adapter);   //set the adapter for  the ListView

        /**
         * Check if there are any saved journeys
         */
        if (adapter.getCount() == 0) {
            historyEmptyList.setVisibility(View.VISIBLE);
        } else {
            historyEmptyList.setVisibility(View.GONE);
        }

        registerForContextMenu(historyListview);

        /**
         * Open a specific journey upon tapping its position within the data set
         */
        historyListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                DrivingActivityController.prepareJourneyDetails(HistoryActivity.this, journeyList.get(position));
            }
        });
    }

    /**
     * Called when the context menu for this view is being built
     *
     * @param menu
     *          the context menu that is being built
     * @param v
     *          the view for which the context menu is being built
     * @param menuInfo
     *          extra information about the item for which the context menu should be shown
     */
    @Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_history, menu);
    }

    /**
     * Get extra information set by the View that added this menu item
     *
     * @param item
     *            the context menu item that was selected
     *
     * @return false to allow normal context menu processing to proceed, true to consume it here
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_delete:
                final JourneyDO journeyToDelete = journeyList.get(info.position);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle(getResources().getString(R.string.historyActivity_deleteJourneyTitle));        //ask user for confirmation
                builder1.setMessage(getResources().getString(R.string.historyActivity_deleteJourneyMessage) + " "
                        + journeyToDelete.getJourneyId() + "?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                db.deleteJourney(journeyToDelete);   //remove item from data set
                                adapter.remove(journeyToDelete);
                                journeyList.remove(journeyToDelete);
                                if (journeyList.isEmpty()) {     //if no items are left, inform the user
                                    historyEmptyList.setVisibility(View.VISIBLE);
                                    preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                    SharedPreferences.Editor edit = preferences.edit();
                                    edit.putInt("fileIdNumber", 1).commit();
                                }
                                ToastDisplayer.displayLongToast(getBaseContext(),
                                        getResources().getString(R.string.historyActivity_deleteToastPartOne) + " "
                                                + journeyToDelete.getJourneyId() + " "
                                                + getResources().getString(R.string.historyActivity_deleteToastPartTwo));
                                adapter.notifyDataSetChanged();
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

                return true;
            case R.id.action_rename:
                final JourneyDO journeyToRename = journeyList.get(info.position);
                final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(HistoryActivity.this);
                builder.setTitle(getResources().getString(R.string.historyActivity_renameJourneyTitle));
                builder.setMessage(getResources().getString(R.string.historyActivity_renameJourneyMessage));



                final EditText input = new EditText(HistoryActivity.this);   // set up the input



                input.setInputType(InputType.TYPE_CLASS_TEXT);   // specify the type of input expected;
                input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
                input.setHint(getResources().getString(R.string.historyActivity_renameJourneyHint));

                LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(500, ViewGroup.LayoutParams.WRAP_CONTENT);
                inputParams.setMargins(40, 0, 0, 0);

                final LinearLayout layout = new LinearLayout(HistoryActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                layout.addView(input, inputParams);


                builder.setView(layout);


                builder.setPositiveButton(R.string.historyActivity_renameJourneyOk, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().trim().length() < 3) {   //check if new name is at least 3 characters long
                            ToastDisplayer.displayLongToast(getBaseContext(),
                                    getResources().getString(R.string.historyActivity_renameJourneyNameTooShort));
                            dialog.cancel();
                        } else if (db.journeyExistsInDatabase(input.getText().toString())) {
                            ToastDisplayer.displayLongToast(getBaseContext(),
                                    getResources().getString(R.string.historyActivity_renameJourneyNameExists));
                        } else {
                            db.updateJourney(journeyToRename, input.getText().toString());   //rename item
                            adapter.notifyDataSetChanged();
                            ToastDisplayer.displayLongToast(getBaseContext(),
                                    getResources().getString(R.string.historyActivity_renameJourneyToast));
                            recreate();
                        }
                    }
                });
                builder.setNegativeButton(R.string.historyActivity_renameJourneyCancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


}
