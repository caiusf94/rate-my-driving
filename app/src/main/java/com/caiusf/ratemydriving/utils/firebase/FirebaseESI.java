package com.caiusf.ratemydriving.utils.firebase;


import androidx.annotation.NonNull;

import com.caiusf.ratemydriving.data.driving.events.types.ScoreType;
import com.caiusf.ratemydriving.utils.timestamp.TimestampGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseESI {

    final private FirebaseAuth firebaseAuth;

    final private DatabaseReference databaseReference;
    private DatabaseReference ratingsReference;

    private String USER_UID;


    private double currentRoadAverage;
    private long currentRoadNumberOfReviews;
    private double currentUserAverage;
    private long totalNumberOfUsersOnCurrentLocation;
    private int drivingBetterThanCount;

    public FirebaseESI() {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        ratingsReference = databaseReference.child("ratings");

        try {
            USER_UID = firebaseAuth.getCurrentUser().getUid();
        } catch (NullPointerException e) {
            USER_UID = "anonymous_user";
        }


    }

    public void storeRatingForLocationToFirebase(final String currentLocation, final ScoreType scoreType) {

        ratingsReference.child(currentLocation)
                .child("userValues")
                .child(USER_UID)
                .child("userRatings")
                .child(TimestampGenerator.getTimestamp())
                .setValue(ScoreType.getPointsFromScoreType(scoreType));

        this.onFirebaseRatingsUpdate(currentLocation);

    }

    public void onFirebaseRatingsUpdate(final String currentLocation) {

        final DatabaseReference userLocationAverageReference = ratingsReference
                .child(currentLocation)
                .child("userValues")
                .child(USER_UID)
                .child("userAverage");

        final DatabaseReference userRatingsReference = ratingsReference
                .child(currentLocation)
                .child("userValues")
                .child(USER_UID)
                .child("userRatings");

        final DatabaseReference currentLocationReference = ratingsReference
                .child(currentLocation);

        userRatingsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userLocationAverageReference.setValue(calculateUserAverageForCurrentLocation(dataSnapshot));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        currentLocationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    currentLocationReference.child("roadAverage").setValue(calculateRoadAverageRating(dataSnapshot));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private double calculateUserAverageForCurrentLocation(final DataSnapshot dataSnapshot) {
        double sum = 0;

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            sum += Double.parseDouble(ds.getValue().toString());
        }

        double average = sum / dataSnapshot.getChildrenCount();

        currentUserAverage = average;

        return average;
    }

    private double calculateRoadAverageRating(final DataSnapshot dataSnapshot) throws NullPointerException {

        DataSnapshot values = dataSnapshot.child("userValues");
        DataSnapshot numberOfReviews = dataSnapshot.child("numberOfReviews");

        drivingBetterThanCount = 0;


        double sum = 0;
        long reviewsCount = 0;
        totalNumberOfUsersOnCurrentLocation = values.getChildrenCount();


        for (DataSnapshot ds : values.getChildren()) {
            double nextUserAverage = Double.parseDouble(ds.child("userAverage").getValue().toString());
            sum += nextUserAverage;

            if (currentUserAverage > nextUserAverage) {
                drivingBetterThanCount++;
            }

            reviewsCount += ds.child("userRatings").getChildrenCount();

        }

        if (numberOfReviews.getValue() == null) {
            numberOfReviews.getRef().setValue(0);
        } else {
            numberOfReviews.getRef().setValue(reviewsCount);
            currentRoadNumberOfReviews = reviewsCount;
        }

        double average = sum / values.getChildrenCount();

        currentRoadAverage = average;

        return average;
    }


    public double getCurrentRoadAverage() {
        return currentRoadAverage;
    }

    public long getCurrentRoadNumberOfReviews() {
        return currentRoadNumberOfReviews;
    }


    public long getTotalNumberOfUsersOnCurrentLocation() {
        return totalNumberOfUsersOnCurrentLocation;
    }

    public int getDrivingBetterThanCount() {
        return drivingBetterThanCount;
    }

    public double calculateDrivingBetterThanPercentage() {

        if (getTotalNumberOfUsersOnCurrentLocation() > 1) {
            return (double) getDrivingBetterThanCount() / (double) (getTotalNumberOfUsersOnCurrentLocation() - 1) * 100.0;
        } else {
            return -1;
        }

    }
}
