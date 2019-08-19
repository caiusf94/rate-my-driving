package com.caiusf.ratemydriving.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.caiusf.ratemydriving.R;
import com.caiusf.ratemydriving.activities.HistoryActivity;
import com.caiusf.ratemydriving.data.JourneyDO;

import java.util.ArrayList;

/**
 * An adapter allowing the communication between HistoryActivity and the data retrieved from the SQLite database
 *
 * @author Caius Florea, 2017
 */
public class HistoryListAdapter extends BaseAdapter{

    /**
     * The JourneyDO list, representing the adapter's data set
     */
    private ArrayList<JourneyDO> journeyList;
    /**
     * The context
     */
    private Context context;
    /**
     * The layout inflater
     */
    private static LayoutInflater inflater = null;

    /**
     * Constructor
     * @param historyActivity
     *                  The HistoryActivity object
     * @param journeyList
     *                  The adapter's data set
     *
     */
    public HistoryListAdapter(HistoryActivity historyActivity, ArrayList<JourneyDO> journeyList){
        this.journeyList = new ArrayList<>();
        this.journeyList.addAll(journeyList);
        this.context = historyActivity;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Gets the size of the JourneyDO list
     *
     * @return the size of the list which contains the JourneyDO objects
     */
    @Override
    public int getCount() {

        return journeyList.size();
    }

    /**
     * Gets the object at a given position in the list
     *
     * @param position
     *              the object's position within the list
     *
     * @return the objecet at the given position
     */
    @Override
    public Object getItem(int position) {

        return position;
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position
     *              the position of the item within the adapter's data set whose row id we want
     *
     * @return the id of the item at the specified position
     */
    @Override
    public long getItemId(int position) {

        return position;
    }

    /**
     * An inner class for holding for accessing the views inside HistoryActivity
     *
     * @see HistoryActivity
     */
    public class Holder{
        /**
         * The TextView for the journey's name
         */
        TextView journeyName;
        /**
         * The TextView for the journey's starting timestamp
         */
        TextView timestamp;
        /**
         * The TextView for the journey's global score text
         */
        TextView globalScore;
        /**
         * The TextView for the journey's global score value
         */
        TextView globalScoreValue;
        /**
         * The ImageView for the car icon
         */
        ImageView carIcon;
    }

    /**
     * Gets a View that displays the data at the specified position in the data set
     *
     * @param position
     *              get a View that displays the data at the specified position in the data set
     * @param convertView
     *              the old view to reuse, if possible
     * @param parent
     *              the parent that this view will eventually be attached to
     *
     * @return a View corresponding to the data at the specified position
     */
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final Holder holder = new Holder();
        final View rowView;
        rowView = inflater.inflate(R.layout.history_item, null);

        /**
         * Set up the views
         */
        holder.journeyName = (TextView) rowView.findViewById(R.id.historyItemFilename);
        holder.timestamp = (TextView) rowView.findViewById(R.id.historyItemTimestamp);
        holder.globalScore = (TextView) rowView.findViewById(R.id.historyItemGlobalScore);
        holder.globalScoreValue = (TextView) rowView.findViewById(R.id.historyItemGlobalScoreValue);
        holder.carIcon = (ImageView) rowView.findViewById(R.id.historyItemCarIcon);

        /**
         * Set actual values for each of the views
         */
        holder.journeyName.setText(journeyList.get(position).getJourneyId());
        holder.timestamp.setText(journeyList.get(position).getStartTimestamp());
        holder.globalScoreValue.setText(String.format("%.2f", journeyList.get(position).getGlobalScore()));
        holder.carIcon.setImageResource(R.drawable.rsz_2carratemydrivingmedium);

        return rowView;
    }

    /**
     * Remove a JourneyDO from the list
     *
     * @param journey
     *              the JourneyDO to be removed
     */
    public void remove(JourneyDO journey){
        journeyList.remove(journey);
    }


}
