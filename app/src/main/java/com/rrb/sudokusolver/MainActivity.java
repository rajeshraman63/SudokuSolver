package com.rrb.sudokusolver;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private LinearLayout buttons_layout;
    private LinearLayout field_layout;
    private LinearLayout main_layout;
    private TextView solution_status_tv;
    private TextView temp_text_value;
    private int prev_selected_tv = -1;
    private int curr_selected_tv = -1;

    private TextView[] textViewArray = new TextView[81];

    private Map<Integer,Integer> textId_to_intId_mapping = new HashMap<>();

    private int[][] grid = {

            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solution_status_tv = findViewById(R.id.solution_status_tv);
        buttons_layout = findViewById(R.id.buttons_layout);

        // number key buttons on the bottom
        for(int i=1;i<10;i++) {
            Button btnView = new Button(this);
            btnView.setLayoutParams(new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
            ));
            btnView.setText(i+"");
            btnView.setId(i);
            btnView.setHeight(50);
//            btnView.setPadding(5,5,5,5);
            final String btnValue = i+"";
            btnView.setBackgroundResource(R.drawable.custom_key_buttons);
            btnView.setTextColor(getColor(R.color.white));

            btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    solution_status_tv.setText(btnValue);
                    if(curr_selected_tv == -1) return;

                    TextView curr_selected_key = (TextView)findViewById(curr_selected_tv);
                    curr_selected_key.setText(btnValue);
                }
            });
            if (buttons_layout != null)
                buttons_layout.addView(btnView);
        }
//        main_layout.removeView(buttons_layout);
//        LinearLayout li = buttons_layout;
//        field_layout.addView(buttons_layout);

        try {
            loadTextIdToIntId();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void loadTextIdToIntId() throws IllegalAccessException {
        Field[] ids = R.id.class.getFields();
        Log.i(" ids length : ", ids.length + "");
        for (Field id : ids) {
            Object val = id.get(null);
            if(val instanceof Integer){
                String cell_id_value = id.getName();   // user given id to the text_views
                if(cell_id_value.contains("tv_")){
                    String[] splits =  (cell_id_value.trim()).split("_");
                    if(splits.length > 1){
                        int x = Integer.parseInt(splits[1].charAt(0)+"") - 1;
                        int y  = Integer.parseInt(splits[1].charAt(1) + "") - 1;
                        int key = x*10 + y;
                        textId_to_intId_mapping.put(key, (Integer) val);
                    }
                }
            }
        }

        // System.out.println("Map Size : " + ""+ textId_to_intId_mapping.size());
    }


    public  void solveSudoku(View v) throws IllegalAccessException {
        for (Map.Entry<Integer,Integer> entry : textId_to_intId_mapping.entrySet()) {
            // System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            int key = entry.getKey();
            int y = key%10;
            key /= 10;
            int x = key;

            TextView tv_key = (TextView)findViewById(entry.getValue());
            String myTextViewString = (String) tv_key.getText();
            if(!myTextViewString.equals("") && myTextViewString.length() != 0){
                grid[x][y] = Integer.parseInt(myTextViewString);
            }
        }

        SudokuSolver sudokuSolver = new SudokuSolver();
//        boolean status = sudokuSolver.solveSudoku(grid,9);
        String status = sudokuSolver.solveSudoku(grid,9);

//        solution_status_tv.setText(status);

        if(status.equals("Invalid Input")){
            Toast.makeText(getApplicationContext(),"Invalid Input",Toast.LENGTH_SHORT).show();
            solution_status_tv.setText(status);
            solution_status_tv.setTextColor(getColor(R.color.noSolution));
            return;
        }else if(status.equals("Doesn't Exist")){
            solution_status_tv.setText(status);
            solution_status_tv.setTextColor(getColor(R.color.noSolution));
            Toast.makeText(getApplicationContext(),"No Solution Exists",Toast.LENGTH_SHORT).show();
            return;
        }
       /* else{
             System.out.println("Solution Exists");
        }*/

//        solution_status_tv.setTextColor(getColor(R.color.solution));
        displayTheSolution(grid,9);
    }

    private void displayTheSolution(int[][] grid, int n){
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                TextView myTextView = (TextView)findViewById(textId_to_intId_mapping.get(i*10+j));
                myTextView.setText(grid[i][j]+"");
            }
        }
    }

    public void clearSudoku(View view){
        solution_status_tv.setText("");
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                TextView myTextView = (TextView)findViewById(textId_to_intId_mapping.get(i*10+j));
                myTextView.setText("");
                myTextView.setBackgroundResource(0);
                myTextView.setBackgroundResource(R.drawable.borderforkeys);
            }
        }
//        try {
//            loadTextIdToIntId();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                grid[i][j]=0;
            }
        }
//        displayGrid(grid);
    }

    // work on this method
    public void  changeValueInClickedCell(View v) {
        TextView tv = (TextView) v;
        curr_selected_tv = v.getId();
        tv.setBackgroundResource(R.drawable.colored_background);
    }

    // erase the selected cell
    public void eraseCell(View v){
        if(curr_selected_tv == -1) return;

        TextView curr_selected_key = (TextView)findViewById(curr_selected_tv);
        curr_selected_key.setText("");
    }

    public static String getIDName(View view, Class<?> clazz) throws Exception {
        Integer id = view.getId();
        Field[] ids = clazz.getFields();
        Log.i(" ids length : ", ids.length + "");
        for (int i = 0; i < ids.length; i++) {
            Object val = ids[i].get(null);
            if (val != null && val instanceof Integer
                    && ((Integer) val).intValue() == id.intValue()) {
                return ids[i].getName();
            }
        }
        return "";
    }

    private void displayGrid(int[][] grid) {
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                System.out.print(grid[i][j] + " ");
            }
             System.out.println("");
        }
    }

}
