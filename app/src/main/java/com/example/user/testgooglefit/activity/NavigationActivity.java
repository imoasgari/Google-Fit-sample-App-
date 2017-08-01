package com.example.user.testgooglefit.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.user.testgooglefit.BuildFitnessClient;
import com.example.user.testgooglefit.R;
import com.example.user.testgooglefit.activity.clientprovider.BuildFitnessClientProvider;


/**
 * Created by user on 18/07/2017.
 */

public class NavigationActivity extends AppCompatActivity implements BuildFitnessClientProvider {

	private BottomNavigationView bottomNavigationView;
	private BuildFitnessClient mBuildFitnessClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);

		Toolbar toolBar = (Toolbar) findViewById(R.id.custom_toolbar);
		setSupportActionBar(toolBar);
		toolBar.setTitleTextColor(getResources().getColor(R.color.textColor));

		mBuildFitnessClient = new BuildFitnessClient(this);

		bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

		bottomNavigationView.setOnNavigationItemSelectedListener(
			new BottomNavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(@NonNull MenuItem item) {
					switch (item.getItemId()) {
						case R.id.action_dashboard:
							getSupportActionBar().setTitle(R.string.text_toolbar_dashboard);
							replaceFragment(new DashboardFragment());
							break;
						case R.id.action_history:
							getSupportActionBar().setTitle(R.string.text_toolbar_history);
							replaceFragment(new HistoryFragment());
							break;
						case R.id.action_stats:
							getSupportActionBar().setTitle(R.string.text_toolbar_stats);
							//replaceFragment();
							break;
						case R.id.action_setting:
							getSupportActionBar().setTitle(R.string.text_toolbar_setting);
							replaceFragment(new DashboardFragment());
							break;
					}

					return true;
				}
			});

		bottomNavigationView.setOnNavigationItemReselectedListener(
			new BottomNavigationView.OnNavigationItemReselectedListener() {
				@Override
				public void onNavigationItemReselected(@NonNull MenuItem item) {

				}
			}

		);

		if(savedInstanceState == null) {
			android.app.FragmentManager manager = getFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.replace(R.id.fragment_container, new DashboardFragment());
			transaction.commit();
		}
	}

	protected void replaceFragment(Fragment fragment) {
		getFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_container, fragment)
			.commit();
	}

	@Override
	public BuildFitnessClient getFitnessClient() {
		return mBuildFitnessClient;
	}

	@Override
	public void onStop() {
		super.onStop();
		mBuildFitnessClient.manageClients();
	}

}
