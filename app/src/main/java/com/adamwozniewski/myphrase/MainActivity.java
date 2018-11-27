package com.adamwozniewski.myphrase;

import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button buttonCopyText, buttonFindPhrase, buttonReset;
    private TextView textViewFoundPhrase, textViewCopiedText;

    private String initialText, phrase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.buttonCopyText = (Button) findViewById(R.id.buttonCopyText);
        this.buttonFindPhrase = (Button) findViewById(R.id.buttonFindPhrase);
        this.buttonReset = (Button) findViewById(R.id.buttonResetPhrase);

        this.textViewFoundPhrase = (TextView) findViewById(R.id.textViewFoundPhrase);
        this.textViewCopiedText = (TextView) findViewById(R.id.textViewCopiedPhrase);

        this.buttonCopyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String clip = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
                if (clip != null) {
                    MainActivity.this.textViewCopiedText.setText(clip);
                    MainActivity.this.initialText = MainActivity.this.textViewCopiedText.getText().toString();
                } else {
                    MainActivity.this.textViewFoundPhrase.setText("Schowek jest pusty.");
                }
            }
        });

        this.buttonFindPhrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.this.textViewCopiedText.getText().toString().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Znajdź frazę");
                    final EditText editTextPhrase = new EditText(MainActivity.this);
                    editTextPhrase.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(editTextPhrase);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.this.phrase = editTextPhrase.getText().toString();
                            if (MainActivity.this.phrase.length() > 0) {
                                MainActivity.this.textViewFoundPhrase.setText(findPhrase(MainActivity.this.phrase));
                            } else {
                                MainActivity.this.textViewCopiedText.setText(MainActivity.this.initialText);
                                MainActivity.this.textViewFoundPhrase.setText("Jak mam znaleźć NIC ?");
                            }
                        }
                    });
                    builder.setNegativeButton("Zamknij", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    MainActivity.this.textViewFoundPhrase.setText("Skopiuj coś do schowka !");

                }
            }
        });

        this.buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.textViewCopiedText.setText("");
                MainActivity.this.textViewFoundPhrase.setText("");
            }
        });
    }

    /**
     * findPhrase wyszukuje fraze tekstu ze schowka i podkreśla je na żółto
     * @param phrase
     * @return String z zmodyfikowanym tekstem
     */
    private String findPhrase(String phrase) {
        SpannableString spannableString = new SpannableString(MainActivity.this.initialText); // zmaienia tekst ze schowka na SpannableString
        int phraseLength = MainActivity.this.phrase.length(); // pobierz długośc frazy
        int index = MainActivity.this.initialText.indexOf(MainActivity.this.phrase); // Znajdz pierwsze wywołąnie frazy
        int phraseCounter = 0;

        while (index >= 0) {
            phraseCounter++;
            spannableString.setSpan(new BackgroundColorSpan(Color.YELLOW), index, index + phraseLength, 0);// Zmien kolor wyszukanej frazy

            index = MainActivity.this.initialText.indexOf(phrase, index + phraseCounter);
        }
        MainActivity.this.textViewCopiedText.setText(spannableString);
        return String.format("Znaleziono %d wystąpień szukanej frazy", phraseCounter);
    }
}
