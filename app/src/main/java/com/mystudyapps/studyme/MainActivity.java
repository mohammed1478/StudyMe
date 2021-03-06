package com.mystudyapps.studyme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView questionTV, answerTV, option1TV, option2TV, option3TV;
    private FloatingActionButton addCardBTN;
    boolean isShowingOptions = true, isQuestionShowing = false, isAnswerShowing = false;
    int currentCardIndex = 0;

    // instance of the FlashcardDatabase database so that we can read / write to it
    FlashcardDatabase flashcardDatabase;
    //list to hold all of the flash cards
    List<Flashcard> allFlashcards;

    Animation leftOutAnim , rightInAnim;


    //----------------------------------------------------------------------//
    //                           HELPER METHODS                            //
    // ---------------------------------------------------------------------//

    public void showAnswer() {
        questionTV.setVisibility(View.INVISIBLE);
        questionTV.setEnabled(false);

        answerTV.setEnabled(true);
        answerTV.setVisibility(View.VISIBLE);
        isQuestionShowing = true;
        isAnswerShowing = false;
    }

    public void showQuestion() {
        answerTV.setVisibility(View.INVISIBLE);
        answerTV.setEnabled(false);

        questionTV.setEnabled(true);
        questionTV.setVisibility(View.VISIBLE);
        isQuestionShowing = false;
        isAnswerShowing = true;
    }


    //----------------------------------------------------------------------//
    //                   BUTTON ON CLICK METHODS                           //
    // ---------------------------------------------------------------------//

    //when question is tapped, the answer is shown
    public void questionOnClick(View view) {

        questionTV.animate().rotationY(90).setDuration(200).withEndAction(
                new Runnable() {
                    @Override
                    public void run() {
                        showAnswer();
                        // second quarter turn
                        answerTV.setRotationY(-90);
                        answerTV.animate().rotationY(0).setDuration(200).start();
                    }
                }
        ).start();
        questionTV.setCameraDistance(25000);
        answerTV.setCameraDistance(25000);
    }

    //when the answer is tapped, the question is shown
    public void answerOnClick(View view) {

        answerTV.animate().rotationY(90).setDuration(200).withEndAction(
                new Runnable() {
                    @Override
                    public void run() {
                        showQuestion();
                        // second quarter turn
                        questionTV.setRotationY(-90);
                       questionTV.animate().rotationY(0).setDuration(200).start();
                    }
                }
        ).start();
        answerTV.setCameraDistance(25000);
        questionTV.setCameraDistance(25000);
    }

    //users answer options
    public void optionOnClick(View view) {

        TextView text = (TextView) view;

        if (text.getText().equals("option One")) {
            text.setBackground(ContextCompat.getDrawable(this, R.drawable.correct));
        } else {
            text.setBackground(ContextCompat.getDrawable(this, R.drawable.incorrect));
        }

    }

    //onclick method for the show / hide answer toggle
    public void showToggleOnclick(View view) {
        if (isShowingOptions) {
            option1TV.setVisibility(View.INVISIBLE);
            option2TV.setVisibility(View.INVISIBLE);
            option3TV.setVisibility(View.INVISIBLE);
            isShowingOptions = false;
        } else {
            option1TV.setVisibility(View.VISIBLE);
            option2TV.setVisibility(View.VISIBLE);
            option3TV.setVisibility(View.VISIBLE);
            isShowingOptions = true;
        }
    }

    //onclick method for adding a new card.
    public void addCardOnClick(View view) {
        Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
        startActivityForResult(intent, 100);
        overridePendingTransition(R.anim.right_in, R.anim.left_out); /*For overridePendingTransition the first parameter is the "enter" animation
                                                                     for the new launched activity and the second is the "exit"
                                                                     animation for the current activity we're leaving. */
    }

    //shows user the next card
    public void nextCardOnClick(View view) {
        questionTV.setRotationY(0);
        showQuestion();
        if (allFlashcards.isEmpty()) {
            Toast.makeText(MainActivity.this, "MUST ADD A CARD FIRST", Toast.LENGTH_LONG).show();
        } else {
            //if we pass the last card, go back to the beginning
            ++currentCardIndex;

            if (currentCardIndex > allFlashcards.size() - 1) {
                currentCardIndex = 0;
            }


            //animate sliding action when next button is pressed

            final Animation leftOutAnim = AnimationUtils.loadAnimation(this, R.anim.left_out);
            final Animation  rightInAnim = AnimationUtils.loadAnimation(this, R.anim.right_in);
            questionTV.startAnimation(leftOutAnim);
            leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // this method is called when the animation first starts
                    showQuestion();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // this method is called when the animation is finished playing
                    questionTV.startAnimation(rightInAnim);
                    //show the card
                    questionTV.setText(allFlashcards.get(currentCardIndex).getQuestion());
                    answerTV.setText(allFlashcards.get(currentCardIndex).getAnswer());
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // we don't need to worry about this method
                }
            });



        }


    }

    public void previousCardOnClick(View view) {
        questionTV.setRotationY(0);
        showQuestion();
        if (allFlashcards.isEmpty()) {
            Toast.makeText(MainActivity.this, "MUST ADD A CARD FIRST", Toast.LENGTH_LONG).show();
        } else {
            --currentCardIndex;

            //if we pass the first card, loop around  to the end
            if (currentCardIndex < 0) {
                currentCardIndex = allFlashcards.size() - 1;
            }


            //do a sliding animation
            final Animation leftOutAnim = AnimationUtils.loadAnimation(this, R.anim.left_out);
            final Animation  rightInAnim = AnimationUtils.loadAnimation(this, R.anim.right_in);
            questionTV.startAnimation(leftOutAnim);
            leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    // this method is called when the animation first starts
                    showQuestion();
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // this method is called when the animation is finished playing
                    questionTV.startAnimation(rightInAnim);
                    //show the card
                    questionTV.setText(allFlashcards.get(currentCardIndex).getQuestion());
                    answerTV.setText(allFlashcards.get(currentCardIndex).getAnswer());
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // we don't need to worry about this method
                }
            });

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();





        questionTV = findViewById(R.id.questionTV);
        answerTV = findViewById(R.id.answerTV);
        option1TV = findViewById(R.id.option1TV);
        option2TV = findViewById(R.id.option2TV);
        option3TV = findViewById(R.id.option3TV);
        addCardBTN = findViewById(R.id.addCardBTN);
        answerTV.setVisibility(View.INVISIBLE);
        answerTV.setEnabled(false);


        /*
        initialize our local flashcardDatabase variable. We have to do this inside onCreate()
        because getApplicationContext() will return null if the app hasn't initialized yet.
        And in onCreate() we can guarantee that the app has been initialized
        */
        flashcardDatabase = new FlashcardDatabase(getApplicationContext());

        //access the available flashcards once the app opens up
        allFlashcards = flashcardDatabase.getAllCards();


        //if the flashcard list is not empty, display the very first card once the app opens up
        if (allFlashcards != null && allFlashcards.size() > 0) {

            questionTV.setText(allFlashcards.get(0).getQuestion());
            answerTV.setText(allFlashcards.get(0).getAnswer());
        }

    }


    //onActivityResult method checks for the request code that was sent to AddCardActivity.class, checks for the
    //result code sent back from AddCardActivity.class as well as the data. Then it sets the value of questionTV and answerTV
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            String question = data.getExtras().getString("QUESTION");
            String answer = data.getExtras().getString("ANSWER");
            questionTV.setText(question);
            answerTV.setText(answer);

            //insertCard method allows us to save our card data
            flashcardDatabase.insertCard(new Flashcard(question, answer));

            //update the local variable holding the list of flashcards
            allFlashcards = flashcardDatabase.getAllCards();


        }
    }


}
