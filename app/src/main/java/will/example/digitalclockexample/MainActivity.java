package will.example.digitalclockexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView timeDisplay;
    private Button startButton;
    private Button stopButton;
    private Button resetButton;

    private WatchTime watchTime;
    private long timeInMilliseconds = 0L;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeDisplay = findViewById(R.id.clockText);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        resetButton = findViewById(R.id.resetButton);

        stopButton.setEnabled(false);
        resetButton.setEnabled(false);

        watchTime =  new WatchTime();

        mHandler = new Handler();
    }

    public void startTimer(View view) {
        stopButton.setEnabled(true);
        startButton.setEnabled(false);
        resetButton.setEnabled(false);

        watchTime.setStartTime(SystemClock.uptimeMillis());
        mHandler.postDelayed(updateTimerRunnable, 20);
    }

    private Runnable updateTimerRunnable = new Runnable() {
        @Override
        public void run() {

            //Computing the time difference
            timeInMilliseconds = SystemClock.uptimeMillis() -
                    watchTime.getStartTime();
            watchTime.setTimeUpdate(watchTime.getStoredTime() +
                    timeInMilliseconds);
            int time = (int) (watchTime.getTimeUpdate() / 100);

            //Computing minutes, seconds and milliseconds
            int minutes = time / 60;
            int seconds = time % 60;
            int milliseconds = (int) (watchTime.getTimeUpdate() / 1000);

            //Displaying time in the clockText Text View
            timeDisplay.setText(String.format("%02d", minutes) + ":"
            + String.format("%02d", seconds) + ":"
            + String.format("%02d", milliseconds));

            //Specifying a no time lapse between posting
            mHandler.postDelayed(this, 0);
        }
    };

    public void stopTimer(View view) {
        //Disable the stop button and enable the start button
        stopButton.setEnabled(false);
        startButton.setEnabled(true);
        resetButton.setEnabled(true);

        //Updating the stored time values
        watchTime.addStoredTime(timeInMilliseconds);

        //The handler clears the message queue
        mHandler.removeCallbacks(updateTimerRunnable);
    }

    public void resetTimer(View view) {
        watchTime.resetWatchTime();
        timeInMilliseconds = 0L;

        int minutes = 0;
        int seconds = 0;
        int milliseconds = 0;

        timeDisplay.setText(String.format("%02d", minutes) + ":"
                + String.format("%02d", seconds) + ":"
                + String.format("%02d", milliseconds));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Handle action bar item clicks here. The action bar
        will automatically handle clicks on the Home/Up button,
        so long as a parent activity is specified in the Manifest

        int id = item.getItemId();
        if(id == R.id.action_settings)

        return super.onOptionsItemSelected(item);
    } */
}
