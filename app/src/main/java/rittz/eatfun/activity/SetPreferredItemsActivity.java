package rittz.eatfun.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rittz.eatfun.R;
import rittz.eatfun.storage.Config;

public class SetPreferredItemsActivity extends AppCompatActivity {

    @Bind(R.id.item_container) LinearLayout linearLayout;

    private List<String> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_preferred_items);
        ButterKnife.bind(this);

        for (String item : Config.get().getPreferredItems()) {
            addItem(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Config.get().setPreferredItems(items);
    }

    private void addItem(final String item) {
        items.add(item);

        final TextView textView = (TextView) LayoutInflater.from(SetPreferredItemsActivity.this).inflate(R.layout.item_preferred, linearLayout, false);
        textView.setText(item);

        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                linearLayout.removeView(v);
                items.remove(item);
                return true;
            }
        });

        linearLayout.addView(textView, linearLayout.getChildCount() - 1);
    }

    @OnClick(R.id.tv_add_item)
    void onAddItemClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Specify Item");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String newItem = input.getText().toString();
                addItem(newItem);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
