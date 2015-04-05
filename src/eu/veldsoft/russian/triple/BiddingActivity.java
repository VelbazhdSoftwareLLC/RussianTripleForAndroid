package eu.veldsoft.russian.triple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class BiddingActivity extends Activity {

	static final String EXTRA_CURRENT_BID_KEY = "eu.veldsoft.russian.triple.currentBidKey";

	static final String EXTRA_MAX_BID_KEY = "eu.veldsoft.russian.triple.maxBidKey";

	static final String EXTRA_RESULT_BID_KEY = "eu.veldsoft.russian.triple.resultBidKey";

	static final String EXTRA_PASS_BID_KEY = "eu.veldsoft.russian.triple.passBidKey";

	static final int PASS_VALUE = 0;

	private int last = 0;

	private int current = 0;

	private int maximum = 0;

	private void setCurrentBid(int current) {
		if (current <= 0) {
			return;
		}

		/*
		 * Current bid can not be more than maximum possible.
		 */
		if (current > maximum) {
			return;
		}

		this.current = current;

		((TextView) findViewById(R.id.bidValue)).setText("" + last);

		Spinner spinner = (Spinner) findViewById(R.id.bidding);
		for (int i = 0; i < spinner.getCount(); i++) {
			Object value = spinner.getItemAtPosition(i);
			if (("" + current).equals(value)) {
				spinner.setSelection(i);
				break;
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		last = current = getIntent().getIntExtra(EXTRA_CURRENT_BID_KEY, 0);
		maximum = getIntent().getIntExtra(EXTRA_MAX_BID_KEY, 200);

		setCurrentBid(current);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bidding);

		((Button) findViewById(R.id.pass))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						setResult(Activity.RESULT_OK, (new Intent().putExtra(
								EXTRA_RESULT_BID_KEY, PASS_VALUE).putExtra(
								EXTRA_PASS_BID_KEY, true)));
						finish();
					}
				});

		((Button) findViewById(R.id.plus_one))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						setCurrentBid(current + 1);
					}
				});

		((Button) findViewById(R.id.plus_ten))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						setCurrentBid(current + 10);
					}
				});

		((Button) findViewById(R.id.done))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						setResult(Activity.RESULT_OK, (new Intent().putExtra(
								EXTRA_RESULT_BID_KEY, current).putExtra(
								EXTRA_PASS_BID_KEY, false)));
						finish();
					}
				});

		((Spinner) findViewById(R.id.bidding))
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long arg3) {
						/*
						 * First value is not a number.
						 */
						if (position == 0) {
							return;
						}

						int value = new Integer(parent.getItemAtPosition(
								position).toString()).intValue();
						if (value <= current || value > maximum) {
							setCurrentBid(current);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
	}
}
