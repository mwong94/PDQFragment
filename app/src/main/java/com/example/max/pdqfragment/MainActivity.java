package com.example.max.pdqfragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    private DistributedRNG<Character> dRNG = new DistributedRNG<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dRNG.addPair('A',0.0533333);
        dRNG.addPair('B',0.0933333);
        dRNG.addPair('C',0.1200000);
        dRNG.addPair('D',0.1600000);
        dRNG.addPair('E',0.2133330);
        dRNG.addPair('F',0.2533330);
        dRNG.addPair('G',0.3066670);
        dRNG.addPair('H',0.3466670);
        dRNG.addPair('I',0.4000000);
        dRNG.addPair('J',0.4133330);
        dRNG.addPair('K',0.4266670);
        dRNG.addPair('L',0.4800000);
        dRNG.addPair('M',0.5333330);
        dRNG.addPair('N',0.5866670);
        dRNG.addPair('O',0.6400000);
        dRNG.addPair('P',0.6933330);
        dRNG.addPair('Q',0.7066670);
        dRNG.addPair('R',0.7600000);
        dRNG.addPair('S',0.8133330);
        dRNG.addPair('T',0.8666670);
        dRNG.addPair('U',0.9200000);
        dRNG.addPair('V',0.9333330);
        dRNG.addPair('W',0.9466670);
        dRNG.addPair('X',0.9733330);
        dRNG.addPair('Y',0.9866670);
        dRNG.addPair('Z',1.0000000);

        if (findViewById(R.id.fragmentContainer) != null) {
            if (savedInstanceState != null) { return; }
            LettersFragment letters = new LettersFragment();
            letters.setArguments(getIntent().getExtras());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragmentContainer, letters);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public void openSuggestions(View view) {
        // open suggestions only if suggestions are not open already
        if (findViewById(R.id.fragmentContainer) != null) {
            findViewById(R.id.suggestionsButton).setVisibility(View.INVISIBLE);
            SuggestionsFragment suggestions = new SuggestionsFragment();

            String pdq = "";
            TextView letter1 = (TextView) findViewById(R.id.letter1);
            TextView letter2 = (TextView) findViewById(R.id.letter2);
            TextView letter3 = (TextView) findViewById(R.id.letter3);
            pdq += letter1.getText().charAt(0);
            pdq += letter2.getText().charAt(0);
            pdq += letter3.getText().charAt(0);

            Bundle bundle = new Bundle();
            bundle.putString("pdq", pdq);
            suggestions.setArguments(bundle);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.replace(R.id.fragmentContainer, suggestions);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public void updateLetters(View view) {
        // close previous suggestions if open
        SuggestionsFragment suggestions = (SuggestionsFragment) getSupportFragmentManager().findFragmentByTag("SUGGESTIONS");
        if(suggestions != null) getSupportFragmentManager().beginTransaction().remove(suggestions).commit();

        // get new letters
        TextView l1 = (TextView) findViewById(R.id.letter1);
        TextView l2 = (TextView) findViewById(R.id.letter2);
        TextView l3 = (TextView) findViewById(R.id.letter3);
        l1.setText("");
        l2.setText("");
        l3.setText("");

        Character letter1 = dRNG.getRandom();
        Character letter2 = dRNG.getRandom();
        Character letter3 = dRNG.getRandom();

        l1.setText(letter1.toString());
        l2.setText(letter2.toString());
        l3.setText(letter3.toString());

        System.out.println("MAX: letter1... " + l1.getText());
        System.out.println("MAX: letter2... " + l2.getText());
        System.out.println("MAX: letter3... " + l3.getText());
    }

    private static class DistributedRNG<K> {
        private TreeMap<K, Double> elements;

        private DistributedRNG() {
            elements = new TreeMap<K, Double>();
        }
        private void addPair(K k, Double d) {
            elements.put(k,d);
        }
        private K getRandom() {
            Random r = new Random();
            Double rand = r.nextDouble();
            for(Map.Entry entry : elements.entrySet()) {
                if(((Double) entry.getValue()) > rand) return (K) entry.getKey();
            }
            return (K) "";
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
