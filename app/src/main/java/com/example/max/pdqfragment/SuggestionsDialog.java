package com.example.max.pdqfragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SuggestionsDialog extends DialogFragment {
    private ArrayList<String> dictionaries = new ArrayList<>();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        // ArrayList<String> suggestions = searchDictionaries(pdq);
        DictionaryDatabase dd = new DictionaryDatabase(getActivity());
        ArrayList<String> suggestions = dd.getSuggestions(pdq);

        // display suggestions
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, suggestions);
        builder.setTitle(R.string.suggestions)
                .setAdapter(adapter, null);

        return builder.create();
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
            System.out.println("MAX: searchDictionaries() " + d);
            file = d;
            file += Character.toLowerCase(pdq.charAt(0));
            file += ".txt";
            temp = searchDictionary(pdq, file);
            file = d;
            file += Character.toLowerCase(pdq.charAt(2));
            file += ".txt";
            temp.addAll(searchDictionary(revString(pdq), file));
            System.out.println("MAX: temp.size(): " + Integer.toString(temp.size()));
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
            InputStream is = getActivity().getAssets().open(dictionary);
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
}
