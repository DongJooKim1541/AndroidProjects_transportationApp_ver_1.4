package com.example.busappver14.ui.subway;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.busappver14.R;

import java.util.ArrayList;

public class Subway_findTimeTableActivity extends AppCompatActivity {

    private ListView mySubwayListView;
    private EditText dailyTypeCode,upDownTypeCode;
    private Button searchTimeTableBtn;
    private Subway_findTimeTableParser parser;
    private ArrayList<SubwayItems> list;
    private Subway_findTimeTableViewModelAdapter adapter;
    private String subwayStationId,dailyTypeCodeString,upDownTypeCodeString;

    int pageNo=1;   //검색을 시작할 page 번호
    ProgressDialog dialog_progress; //로딩을 위한 다이얼로그

    //스크롤링을 통한 추가 로드를 위해 필요한 변수
    LayoutInflater mInflater;
    boolean mLockListView=true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_findtimetable);

        dailyTypeCode=(EditText)findViewById(R.id.dailyTypeCode);
        upDownTypeCode=(EditText)findViewById(R.id.upDownTypeCode);
        searchTimeTableBtn=(Button)findViewById(R.id.searchTimeTableBtn);

        dialog_progress = new ProgressDialog(this);
        dialog_progress.setMessage("Loading.....");

        Intent i=getIntent();
        subwayStationId=i.getExtras().getString("subwayStationId");

        parser=new Subway_findTimeTableParser();

        searchTimeTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subwayStationId!=null && dailyTypeCode.getText().toString().trim().length()>0 && upDownTypeCode.getText().toString().trim().length()>0){
                    pageNo=1;

                    mySubwayListView=(ListView)findViewById(R.id.mySubwayListView);

                    mySubwayListView.setOverScrollMode(View.OVER_SCROLL_NEVER);

                    list=new ArrayList<SubwayItems>();

                    adapter=null;

                    dailyTypeCodeString=convertEditText(dailyTypeCode);
                    upDownTypeCodeString=convertEditText(upDownTypeCode);

                    new SubwayAsync().execute();
                }
            }
        });
    }

    public String convertEditText(EditText editText){
        String input=editText.getText().toString().trim();
        String result;
        if(input.equalsIgnoreCase("평일")){
            result="01";
        }
        else if(input.equalsIgnoreCase("토요일")){
            result="02";
        }
        else if(input.equalsIgnoreCase("일요일")){
            result="03";
        }
        else if(input.equalsIgnoreCase("상행")){
            result="U";
        }
        else if(input.equalsIgnoreCase("하행")){
            result="D";
        }
        else{
            result=null;
        }
        return result;
    }

    class SubwayAsync extends AsyncTask<String, String, ArrayList<SubwayItems>>{
        @Override
        protected ArrayList<SubwayItems> doInBackground(String... strings) {
            return parser.Subway_findTimeTableApi(subwayStationId,dailyTypeCodeString,upDownTypeCodeString,list,pageNo);
        }

        @Override
        protected void onPostExecute(ArrayList<SubwayItems> SubwayItems) {
            if(adapter==null){

                adapter=new Subway_findTimeTableViewModelAdapter(Subway_findTimeTableActivity.this,R.layout.subway_find_timetable_item,SubwayItems,mySubwayListView);

                mySubwayListView.setOnScrollListener(scrollListener);

                mySubwayListView.setAdapter(adapter);
            }
            if (SubwayItems.size() == 0) {
                Toast.makeText(getApplicationContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                dialog_progress.dismiss();
                return;
            }

            //리스트뷰의 변경사항 갱신
            adapter.notifyDataSetChanged();
            mLockListView = false;
            dialog_progress.dismiss();
        }
    }

    AbsListView.OnScrollListener scrollListener=new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            int count=totalItemCount-visibleItemCount;

            if(firstVisibleItem>=count && totalItemCount!=0 && mLockListView==false){
                mLockListView=true;
                if(pageNo*10 < Integer.parseInt(parser.getSi().getTotalCount()) && list.size()>=10){

                    pageNo+=1;
                    new SubwayAsync().execute();
                }
            }
        }
    };
}
