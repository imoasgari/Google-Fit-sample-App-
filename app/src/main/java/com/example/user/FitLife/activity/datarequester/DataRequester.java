package com.example.user.FitLife.activity.datarequester;

import com.example.user.FitLife.BuildFitnessClient;

/**
 * Created by user on 08/08/2017.
 */

public interface DataRequester {

	DataRequester setClient(BuildFitnessClient client);

	void requestDataFor(int range);
}
