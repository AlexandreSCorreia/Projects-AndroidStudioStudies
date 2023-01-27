package com.easyway.calendario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

public class MainActivity extends AppCompatActivity {

   // private CalendarView calendarView;

    private MaterialCalendarView materialCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        materialCalendarView = findViewById(R.id.calendarView);

        //Configurar o calendario
        //Limitando maxima e minima para navegar
      /*  materialCalendarView.state().edit()
                .setMinimumDate(CalendarDay.from(2019,11,1))
                .setMaximumDate(CalendarDay.from(2019,12,1))
                .commit();*/
        //Customizar nome dos meses
        CharSequence meses[] = {"Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho",
                "Agosto","Setembro","Outubro","Novembro","Dezembro"};
        materialCalendarView.setTitleMonths(meses);

        //Customizar nome da semana
        CharSequence semana[] = {"Dom","Seg","Ter","Qua","Qui","Sex","Sab"};
        materialCalendarView.setWeekDayLabels(semana);




        //Evento click nos dias do mes no calendario
        materialCalendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
               // Toast.makeText(MainActivity.this,"Data: "+date,Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this,"Data: "+(date.getMonth()+1)+"/"+date.getYear(),Toast.LENGTH_SHORT).show();
            }
        });
      /*  materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                Toast.makeText(MainActivity.this,"Data: "+date,Toast.LENGTH_SHORT).show();
            }
        });*/
       /* calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(MainActivity.this,"Data: "+dayOfMonth+"/"+month+"/"+year,Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}
