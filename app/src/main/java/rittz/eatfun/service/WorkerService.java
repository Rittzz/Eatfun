package rittz.eatfun.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import rittz.eatfun.R;
import rittz.eatfun.activity.MainActivity;
import rittz.eatfun.core.OrderStatus;
import rittz.eatfun.core.OrderTask;
import rittz.eatfun.core.OrderTaskResult;
import rittz.eatfun.storage.Config;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WorkerService extends IntentService {

    private static final String TAG = "WorkerService";

    private static final int NOTIFICATION_ID = 1;

    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_ORDER = "rittz.eatfun.service.action.ORDER";

    public static void scheduleOrdering(Context context) {
        final PendingIntent pIntent = PendingIntent.getService(context, 0, makeOrderingIntent(context), PendingIntent.FLAG_UPDATE_CURRENT);

        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, AlarmManager.INTERVAL_HALF_DAY, AlarmManager.INTERVAL_HALF_DAY, pIntent);
    }

    public static void unscheduleOrdering(Context context) {
        final PendingIntent pIntent = PendingIntent.getService(context, 0, makeOrderingIntent(context), PendingIntent.FLAG_CANCEL_CURRENT);

        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pIntent);
    }

    private static Intent makeOrderingIntent(Context context) {
        final Intent intent = new Intent(context, WorkerService.class);
        intent.setAction(ACTION_ORDER);
        return intent;
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startOrdering(Context context) {
        context.startService(makeOrderingIntent(context));
    }

    public WorkerService() {
        super("WorkerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_ORDER.equals(action)) {
                handleOrdering();
            }
        }
    }

    private void showErrorNotification(final CharSequence message) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        final Intent mainIntent = new Intent(this, MainActivity.class);
        final PendingIntent pIntent = PendingIntent.getActivity(this, 0, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle("There was an error ordering your food")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification_error)
                .setContentIntent(pIntent)
                .setAutoCancel(true);

        final NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifyManager.notify(NOTIFICATION_ID, builder.build());
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleOrdering() {
        if (Config.get().getPreferredItems().size() == 0) {
            // No preferred items to order
            Log.d(TAG, "Nothing to order!");
            return;
        }

        Log.d(TAG, "Ordering Eatclub...");

        final OrderTask task = new OrderTask();
        final OrderTaskResult result = task.doWork();

        Log.d(TAG, "Done ordering");

        List<String> errorMessages = new ArrayList<>();

        if (result.getErrorType() == null) {
            Log.d(TAG, "No general errors, checking the results of each order");

            // No general errors, lets check the status of each order
            for (OrderStatus status : result.getResults()) {
                if (!status.isSuccess()) {
                    if (status.getErrorType() != OrderStatus.ErrorType.NETWORK) {
                        switch (status.getErrorType()) {
                            case AUTH_ERROR:
                                errorMessages.add("User credential error, please login again.");
                                break;
                            case NONE_AVAILABLE:
                                errorMessages.add("Could not order lunch as there was no food available.");
                                break;
                            default:
                                errorMessages.add("Something bad happened");
                                break;
                        }
                    }

                    Log.w(TAG, "Error ordering food", result.getException());
                }
                else {
                    Log.d(TAG, "Order success!");
                }
            }
        }
        else {
            switch (result.getErrorType()) {
                case AUTH_ERROR:
                    errorMessages.add("User credential error, please login again.");
                    break;
                case UNKNOWN:
                    errorMessages.add("Something bad happened");
                    break;
            }

            Log.w(TAG, "Error ordering food", result.getException());
        }

        if (errorMessages.size() > 0) {
            showErrorNotification(TextUtils.join("\n", errorMessages));
        }

        Config.get().setLastSync(System.currentTimeMillis());
    }
}
