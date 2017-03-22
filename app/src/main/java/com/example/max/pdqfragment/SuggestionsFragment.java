package com.example.max.pdqfragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SuggestionsFragment extends Fragment {
    private DictionaryDatabase dd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.suggestions_fragment, container, false);
        Context context = getActivity().getApplicationContext();

        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        String pdq = "";
        Bundle bundle = getArguments();
        if(bundle != null) { pdq = bundle.getString("pdq"); }

        // get suggestions
        dd = new DictionaryDatabase(getActivity());
        ArrayList<String> suggestions = dd.getSuggestions(pdq);

        // display suggestions
        ListView lv = (ListView) fragmentView.findViewById(R.id.suggestionsList);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, suggestions);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String word = adapter.getItem(position);
                String definition = dd.getDefinition(word);

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View v = inflater.inflate(R.layout.definition_dialog, null);

                TextView tv = (TextView) v.findViewById(R.id.definition);
                if(tv != null) {
                    tv.setText(definition);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(word);
                builder.setView(v);
                builder.create();
                builder.show();
            }
        });
        lv.setAdapter(adapter);

        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().findViewById(R.id.suggestionsButton).setVisibility(View.VISIBLE);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
    }
}
