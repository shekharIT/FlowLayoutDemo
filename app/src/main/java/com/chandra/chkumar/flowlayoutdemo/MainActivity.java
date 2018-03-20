package com.chandra.chkumar.flowlayoutdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TagContainerLayout mTagContainerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTagContainerLayout = (TagContainerLayout) findViewById(R.id.tagcontainerLayout1);
        List<String> list1 = new ArrayList<String>();
        list1.add("Java");
        list1.add("C++");
        list1.add("Python");
        list1.add("Swift");
        list1.add("你好，这是一个TAG。你好，这是一个TAG。你好，这是一个TAG。你好，这是一个TAG。");
        list1.add("PHP");
        list1.add("Welcome to use HhHH!");
        list1.add("JavaScript");
        list1.add("Html");
        list1.add("Welcome to use AndroidTagView!");
        // After you set your own attributes for TagView, then set tag(s) or add tag(s)
        mTagContainerLayout.setTags(list1);
//        mTagContainerLayout.addTag(text.getText().toString());
    }
}
