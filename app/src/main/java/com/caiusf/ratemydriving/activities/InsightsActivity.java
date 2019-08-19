package com.caiusf.ratemydriving.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.caiusf.ratemydriving.R;
import com.caiusf.ratemydriving.data.JourneyStatsDO;
import com.caiusf.ratemydriving.utils.converter.DurationFromSeconds;

/**
 * The activity which displays insights about a journey
 *
 * @author Caius Florea, 2017
 */
public class InsightsActivity extends Activity {

    /**
     * The statistics of the journey
     *
     * @see JourneyStatsDO
     */
    private JourneyStatsDO stats;

    /**
     * TextViews for displaying the rating and number of occurrences for each type of driving event
     */
    private TextView nbGoodLeftTurns;
    private TextView nbMediumLeftTurns;
    private TextView nbBadLeftTurns;
    private TextView nbTotalLeftTurns;

    private TextView nbGoodRightTurns;
    private TextView nbMediumRightTurns;
    private TextView nbBadRightTurns;
    private TextView nbTotalRightTurns;

    private TextView nbGoodAccelerations;
    private TextView nbMediumAccelerations;
    private TextView nbBadAccelerations;
    private TextView nbTotalAccelerations;

    private TextView nbGoodBrakes;
    private TextView nbMediumBrakes;
    private TextView nbBadBrakes;
    private TextView nbTotalBrakes;

    private TextView nbOverspeedings;
    private TextView durationOverspeedings;

    private TextView goodPercentage;
    private TextView mediumPercentage;
    private TextView badPercentage;

    /**
     * Set up the layout for this activity and get the stats object
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insights);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            stats = (JourneyStatsDO) bundle.getSerializable("stats");
        }

        initTextViews();
        setupStats();
    }

    /**
     * Initialize the TextViews
     */
    private void initTextViews(){

        nbGoodLeftTurns = (TextView) findViewById(R.id.insightsNbGoodLeftTurns);
        nbMediumLeftTurns = (TextView) findViewById(R.id.insightsNbMediumLeftTurns);
        nbBadLeftTurns = (TextView) findViewById(R.id.insightsNbBadLeftTurns);
        nbTotalLeftTurns = (TextView) findViewById(R.id.insightsNbTotalLeftTurns);

        nbGoodRightTurns = (TextView) findViewById(R.id.insightsNbGoodRightTurns);
        nbMediumRightTurns = (TextView) findViewById(R.id.insightsNbMediumRightTurns);
        nbBadRightTurns = (TextView) findViewById(R.id.insightsNbBadRightTurns);
        nbTotalRightTurns = (TextView) findViewById(R.id.insightsNbTotalRightTurns);

        nbGoodAccelerations = (TextView) findViewById(R.id.insightsNbGoodAccelerations);
        nbMediumAccelerations = (TextView) findViewById(R.id.insightsNbMediumAccelerations);
        nbBadAccelerations = (TextView) findViewById(R.id.insightsNbBadAccelerations);
        nbTotalAccelerations = (TextView) findViewById(R.id.insightsNbTotalAccelerations);

        nbGoodBrakes = (TextView) findViewById(R.id.insightsNbGoodBrakes);
        nbMediumBrakes = (TextView) findViewById(R.id.insightsNbMediumBrakes);
        nbBadBrakes = (TextView) findViewById(R.id.insightsNbBadBrakes);
        nbTotalBrakes = (TextView) findViewById(R.id.insightsNbTotalBrakes);

        nbOverspeedings = (TextView) findViewById(R.id.journeyInsightsOverspeedingsValue);
        durationOverspeedings = (TextView) findViewById(R.id.journeyInsightsOverspeedingsDurationValue);

        goodPercentage = (TextView) findViewById(R.id.journeyInsigtsGoodPercentageValue);
        mediumPercentage = (TextView) findViewById(R.id.journeyInsigtsMediumPercentageValue);
        badPercentage = (TextView) findViewById(R.id.journeyInsigtsBadPercentageValue);
    }

    /**
     * Populate the TextViews
     */
    private void setupStats(){
        nbGoodLeftTurns.setText(String.valueOf(stats.getNbGoodLeftTurns()));
        nbMediumLeftTurns.setText(String.valueOf(stats.getNbMediumLeftTurns()));
        nbBadLeftTurns.setText(String.valueOf(stats.getNbBadLeftTurns()));
        nbTotalLeftTurns.setText(String.valueOf(stats.getNbTotalLeftTurns()));

        nbGoodRightTurns.setText(String.valueOf(stats.getNbGoodRightTurns()));
        nbMediumRightTurns.setText(String.valueOf(stats.getNbMediumRightTurns()));
        nbBadRightTurns.setText(String.valueOf(stats.getNbBadRightTurns()));
        nbTotalRightTurns.setText(String.valueOf(stats.getNbTotalRightTurns()));

        nbGoodAccelerations.setText(String.valueOf(stats.getNbGoodAccelerations()));
        nbMediumAccelerations.setText(String.valueOf(stats.getNbMediumAccelerations()));
        nbBadAccelerations.setText(String.valueOf(stats.getNbBadAccelerations()));
        nbTotalAccelerations.setText(String.valueOf(stats.getNbTotalAccelerations()));

        nbGoodBrakes.setText(String.valueOf(stats.getNbGoodBrakes()));
        nbMediumBrakes.setText(String.valueOf(stats.getNbMediumBrakes()));
        nbBadBrakes.setText(String.valueOf(stats.getNbBadBrakes()));
        nbTotalBrakes.setText(String.valueOf(stats.getNbTotalBrakes()));

        nbOverspeedings.setText(String.valueOf(stats.getNbOverspeedings()));
        durationOverspeedings.setText(DurationFromSeconds.getDurationString(stats.getDurationOverspeedings()));

        goodPercentage.setText(String.format("%.2f", stats.getGoodPercentage()));
        mediumPercentage.setText(String.format("%.2f", stats.getMediumPercentage()));
        badPercentage.setText(String.format("%.2f", stats.getBadPercentage()));
    }

}
