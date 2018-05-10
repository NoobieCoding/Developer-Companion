package corp.tuter.developerscompanionapp.CustomView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import corp.tuter.developerscompanionapp.Model.Project;
import corp.tuter.developerscompanionapp.MyProjectMainActivity;
import corp.tuter.developerscompanionapp.R;

public class AddComponentView extends FrameLayout implements View.OnClickListener{

    private TextView nameHeader;
    private Button thisSubmitButton;
    private Spinner spinner;
    private AddComponentListener myListener;
    private AlertDialog dialog;
    private List<String> options;

    public AddComponentView(@NonNull Context context) {
        super(context);
        setUp();
    }


    public AddComponentView(@NonNull Context context, AlertDialog dialog) {
        super(context);
        setUp();
        this.dialog = dialog;
    }

    public AddComponentView(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setUp();
    }

    public AddComponentView(@NonNull Context context, AttributeSet attributeSet, AlertDialog dialog) {
        super(context, attributeSet);
        this.dialog = dialog;
        setUp();
    }

    private void setUp() {
        options = new ArrayList<>();
        inflate(getContext(), R.layout.add_detail, this);
        bindView();
    }

    private void bindView() {
        nameHeader = findViewById(R.id.add_framework_dialog_header);
        thisSubmitButton = findViewById(R.id.add_framework_dialog_button);
        spinner = findViewById(R.id.framework_spinner);
        thisSubmitButton.setOnClickListener(this);
    }

    public void setOptions(List<String> options) {
        this.options = options;
        Log.d("CAN", options.size()+"ZZZ");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                R.layout.spinner_layout, options);
        spinner.setAdapter(adapter);
    }

    public void setEventListerner(AddComponentListener listener) {
        myListener = listener;
    }


    @Override
    public void onClick(View v) {
        Log.d("MUSIC","CAll2");
        if (v == thisSubmitButton) {
            String selected = "";
            selected = (String)spinner.getSelectedItem();
            myListener.onAdded(selected);
            dialog.dismiss();

        }
    }
}
