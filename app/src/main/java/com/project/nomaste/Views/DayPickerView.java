package com.project.nomaste.Views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.project.nomaste.R;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

public class DayPickerView extends LinearLayout {
    View rootView;
    ToggleButton weekdays[];
    boolean checkedButton[];
    public DayPickerView(Context context) {
        super(context);
        init(context);
    }

    public DayPickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DayPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DayPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    //Function to Set Up everything
    public void init(final Context context){
        rootView = inflate(context, R.layout.view_daypicker, this);
        weekdays = new ToggleButton[7];
        weekdays[0] = (ToggleButton) this.findViewById(R.id.tL);
        weekdays[1] = (ToggleButton) this.findViewById(R.id.tM);
        weekdays[2] = (ToggleButton) this.findViewById(R.id.tMi);
        weekdays[3] = (ToggleButton) this.findViewById(R.id.tJ);
        weekdays[4] = (ToggleButton) this.findViewById(R.id.tV);
        weekdays[5] = (ToggleButton) this.findViewById(R.id.tS);
        weekdays[6] = (ToggleButton) this.findViewById(R.id.tD);

        checkedButton = new boolean[7];

        //Check individual items.
        weekdays[0].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               checkedButton[0] = isChecked;
            }
        });
        weekdays[1].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedButton[0] = isChecked;
            }
        });
        weekdays[2].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedButton[0] = isChecked;
            }
        });
        weekdays[3].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedButton[0] = isChecked;
            }
        });
        weekdays[4].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedButton[0] = isChecked;
            }
        });
        weekdays[5].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedButton[0] = isChecked;
            }
        });
        weekdays[6].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkedButton[0] = isChecked;
                Toast.makeText(context, "Sunday is "+isChecked, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public boolean[] getMarkedDays(){
        return checkedButton;
    }


}
