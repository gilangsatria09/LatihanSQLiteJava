package com.example.myapplication;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMain extends Fragment implements View.OnClickListener, RecyclerviewAdapter.OnUserClickListener {

    RecyclerView recyclerView;
    EditText edtName, edtAge;
    Button submit;
    RecyclerView.LayoutManager layoutManager;
    Context context;
    List<Person> listPersonInfo;

    public FragmentMain() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        recyclerView = view.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        edtName = view.findViewById(R.id.edtname);
        edtAge = view.findViewById(R.id.edtage);
        submit = view.findViewById(R.id.btnsubmit);
        submit.setOnClickListener(this);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        DatabaseHelper db = new DatabaseHelper(context);
        listPersonInfo = db.selectUserData();
        RecyclerviewAdapter adapter = new RecyclerviewAdapter (context,listPersonInfo,this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnsubmit) {
            DatabaseHelper db = new DatabaseHelper(context);
            Person currentPerson = new Person();
            String btnStatus = submit.getText().toString();
            if (btnStatus.equals("Submit")) {
//                if (edtName.getText().equals(null) && edtAge.getText().equals(null) ){
//                    Toast.makeText(getActivity(),"Mohon Isi Data Dengan Benar",Toast.LENGTH_LONG).show();
//                }
//                else if (edtName.getText().equals(null)){
//                    Toast.makeText(getActivity(),"Mohon isi nama!",Toast.LENGTH_SHORT).show();
//                }
//                else if (edtAge.getText().equals("")){
//                    Toast.makeText(getActivity(),"Mohon Isi Usia",Toast.LENGTH_SHORT).show();
//                }
                    currentPerson.setName(edtName.getText().toString());
                    currentPerson.setAge(Integer.parseInt(edtAge.getText().toString()));
                    db.insert(currentPerson);
                    Toast.makeText(getActivity(),"Data Berhasil Dimasukkan",Toast.LENGTH_SHORT).show();
            }
            if (btnStatus.equals("Update")) {
                currentPerson.setName(edtName.getText().toString());
                currentPerson.setAge(Integer.parseInt(edtAge.getText().toString()));
                db.update(currentPerson);
            }
            setupRecyclerView();
            edtName.setText("");
            edtAge.setText("");
            edtName.setFocusable(true);
            submit.setText("Submit");

        }
    }
    @Override
    public void onUserClick(Person currentPerson, String action) {
        if(action.equals("Edit")){

            edtName.setText(currentPerson.getName());
            edtName.setFocusable(false);
            edtName.setEnabled(false);
            edtAge.setText(currentPerson.getAge()+"");
            submit.setText("Update");
        }
        if(action.equals("Delete")){
            DatabaseHelper db=new DatabaseHelper(context);
            db.delete(currentPerson.getName());
            setupRecyclerView();
        }
    }
}

