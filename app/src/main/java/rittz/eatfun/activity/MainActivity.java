package rittz.eatfun.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import rittz.eatfun.R;
import rittz.eatfun.core.CheckStatus;
import rittz.eatfun.service.WorkerService;
import rittz.eatfun.storage.Config;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.tv_status) TextView statusView;
    @Bind(R.id.tv_last_sync) TextView lastSyncview;
    @Bind(R.id.cb_eatfun_ordering) CheckBox eatfunSyncView;
    @Bind(R.id.cb_shuffle_items) CheckBox shuffleItemsView;
    @Bind(R.id.tv_preferred_items) TextView itemsView;

    private CheckStatus checkStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (checkStatus  != null) {
            checkStatus.setListener(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        if (Config.get().getLastSync() != 0) {
            lastSyncview.setText(DateUtils.formatDateTime(this, Config.get().getLastSync(),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME));
        }
        else {
            lastSyncview.setText("Never synced");
        }

        if (Config.get().getAuthToken() == null) {
            eatfunSyncView.setEnabled(false);
            eatfunSyncView.setChecked(false);
        }
        else {
            eatfunSyncView.setEnabled(true);
            eatfunSyncView.setChecked(Config.get().isAutoOrder());
        }

        shuffleItemsView.setChecked(Config.get().isShuffleMode());
        final List<String> preferredItems = Config.get().getPreferredItems();
        if (preferredItems.size() > 0) {
            itemsView.setText(TextUtils.join(", ", Config.get().getPreferredItems()));
        }
        else {
            itemsView.setText("You have no items selected");
        }

        checkStatus();
    }

    private void checkStatus() {
        statusView.setText("Checking status...");
        checkStatus = new CheckStatus();
        checkStatus.setListener(new CheckStatus.Listener() {
            @Override
            public void onComplete(String status) {
                statusView.setText(status);
            }
        });
        checkStatus.execute();
    }

    @OnCheckedChanged(R.id.cb_eatfun_ordering)
    void onAutoSyncChecked(boolean checked) {
        Config.get().setAutoOrder(checked);

        if (checked) {
            WorkerService.scheduleOrdering(this);
        }
        else {
            WorkerService.unscheduleOrdering(this);
        }
    }

    @OnCheckedChanged(R.id.cb_shuffle_items)
    void onShuffleItemsChecked(boolean checked) {
        Config.get().setShuffleMode(checked);
    }

    @OnClick(R.id.btn_set_preferred_items)
    void onSetPreferredItemsClick() {
        startActivity(new Intent(this, SetPreferredItemsActivity.class));
    }

    @OnClick(R.id.btn_order_now)
    void onOrderNowClick() {
        // Are we logged in?
        if (Config.get().getAuthToken() == null) {
            Toast.makeText(this, "You must login to order", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Ordering...", Toast.LENGTH_SHORT).show();
        WorkerService.startOrdering(this);
    }

    @OnClick(R.id.btn_login)
    void onLoginClick() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
