package corp.tuter.developerscompanionapp.CustomView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import corp.tuter.developerscompanionapp.Model.Project;
import corp.tuter.developerscompanionapp.MyProjectMainActivity;
import corp.tuter.developerscompanionapp.R;

public class CreateNewProjectView extends FrameLayout implements View.OnClickListener{

    private TextView nameHeader;
    private EditText nameField, descriptionField;
    private Button submitButton;
    private CreateNewProjectListener myListener;
    private AlertDialog dialog;

    public CreateNewProjectView(@NonNull Context context) {
        super(context);
        setUp();
    }


    public CreateNewProjectView(@NonNull Context context, AlertDialog dialog) {
        super(context);
        setUp();
        this.dialog = dialog;
    }

    public CreateNewProjectView(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setUp();
    }

    public CreateNewProjectView(@NonNull Context context, AttributeSet attributeSet, AlertDialog dialog) {
        super(context, attributeSet);
        this.dialog = dialog;
        setUp();
    }

    private void setUp() {
        inflate(getContext(), R.layout.create_new_project, this);
        bindView();
    }

    private void bindView() {
        nameHeader = findViewById(R.id.name_header);
        nameField = findViewById(R.id.new_project_name_field);
        descriptionField = findViewById(R.id.new_project_description_field);
        submitButton = findViewById(R.id.submit_new_project_button);
        submitButton.setOnClickListener(this);
    }

    public void setEventListerner(CreateNewProjectListener listener) {
        myListener = listener;
    }


    @Override
    public void onClick(View v) {
        if (v == submitButton) {
            Log.d("ZZz","SSA");
            String name = nameField.getText().toString();
            if (!name.equals("")) {
                String desc = descriptionField.getText().toString();
                Project project = new Project(name, desc);
                dialog.cancel();
                myListener.onCreateProject(project);
            } else {
                nameHeader.setTextColor(getResources().getColor(R.color.red));
            }
        }
    }
}
