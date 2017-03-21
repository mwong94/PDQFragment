package com.example.max.pdqfragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SuggestionsFragment extends Fragment {
    private View fragmentView;
    private Context context;
    private ArrayList<String> dictionaries = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.suggestions_fragment, container, false);
        context = getActivity().getApplicationContext();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        String pdq = "";
        Bundle bundle = getArguments();
        if(bundle != null) {
            pdq = bundle.getString("pdq");
        }

        // set dictionary paths
        dictionaries.add("small/");
        dictionaries.add("medium/");
        dictionaries.add("large/");

        // get suggestions
        ArrayList<String> suggestions = searchDictionaries(pdq);

        // display suggestions
        ListView lv = (ListView) fragmentView.findViewById(R.id.suggestionsList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, suggestions);
        lv.setAdapter(adapter);

        return fragmentView;
    }

    private class SuggestionComp implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            return s1.compareTo(s2);
        }
    }

    private String revString(String s) {
        StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb = sb.reverse();
        return sb.toString();
    }

    private ArrayList<String> searchDictionaries(String pdq) {
        ArrayList<String> temp;
        ArrayList<String> toReturn = null;
        String file;
        for(String d : dictionaries) {
            file = d;
            file += Character.toLowerCase(pdq.charAt(0));
            file += ".txt";
            temp = searchDictionary(pdq, file);
            file = d;
            file += Character.toLowerCase(pdq.charAt(2));
            file += ".txt";
            temp.addAll(searchDictionary(revString(pdq), file));
            if(toReturn == null || (temp.size() > toReturn.size() && temp.size() < 50) || (toReturn.size() < 15 && temp.size() < 100 )) {
                toReturn = temp;
            }
        }
        Collections.sort(toReturn, new SuggestionComp());
        return toReturn;
    }

    private ArrayList<String> searchDictionary(String pdq, String dictionary) {
        ArrayList<String> suggestions = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open(dictionary);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String input;
            while((input = reader.readLine()) != null) {
                if(isPDQ(input, pdq)) {
                    suggestions.add(input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return suggestions;
    }

    private boolean isPDQ(String s, String pdq) {
        int j = 1;
        for(int i=1; i<s.length() && j<=2; i++) {
            if(s.charAt(i) == Character.toLowerCase(pdq.charAt(j))) j++;
        }
        return j == 3;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().findViewById(R.id.suggestionsButton).setVisibility(View.VISIBLE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
    }
}
