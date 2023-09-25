package com.techmania.numberguessinggame

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.techmania.numberguessinggame.databinding.ActivityGameBinding
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    lateinit var gameBinding : ActivityGameBinding
    var randomNumber : Int = -1
    var remainingRight = 8

    var steps = 0
    var guesses = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameBinding = ActivityGameBinding.inflate(layoutInflater)
        val view = gameBinding.root
        setContentView(view)

        gameBinding.toolbarGame.setNavigationOnClickListener {

            finish()

        }

        gameBinding.textViewHint.visibility = View.INVISIBLE
        gameBinding.textViewLastGuess.visibility = View.INVISIBLE
        gameBinding.textViewRight.visibility = View.INVISIBLE

        when(intent.getStringExtra("number").toString()){

            "one" -> {
                gameBinding.textViewInfo.text = "Guess a number between 0-9"
                randomNumber = Random.nextInt(0,10)
                remainingRight = 3
            }
            "two" -> {
                gameBinding.textViewInfo.text = "Guess a number between 10-99"
                randomNumber = Random.nextInt(10,100)
                remainingRight = 8
            }
            "three" -> {
                gameBinding.textViewInfo.text = "Guess a number between 100-999"
                randomNumber = Random.nextInt(100,1000)
                remainingRight = 13
            }
            else -> Toast.makeText(applicationContext,"There is a problem",Toast.LENGTH_SHORT).show()

        }

        gameBinding.buttonConfirm.setOnClickListener {

            gameLogic(randomNumber)

        }

    }

    fun gameLogic(randomNumber : Int){

        val guess : String = gameBinding.editTextNumber.text.toString()

        if(guess == ""){
            Toast.makeText(applicationContext,"Please enter a guess",Toast.LENGTH_LONG).show()
        }else{

            try {

                val userGuess = guess.toInt()
                if(randomNumber == -1){
                    Toast.makeText(applicationContext,"The random number cannot be -1, please try again",Toast.LENGTH_LONG).show()
                }else{

                    steps++
                    guesses.add(userGuess)

                    if (randomNumber == userGuess){
                        //dialog message
                        createDialogMessage()

                    }else{

                        remainingRight--

                        if (remainingRight == 0){

                            //dialog message
                            createDialogMessage()

                        }else{

                            if (userGuess < randomNumber){
                                gameBinding.textViewHint.text = "Increase your guess"
                            }else{
                                gameBinding.textViewHint.text = "Decrease your guess"
                            }

                        }

                    }

                    gameBinding.textViewRight.text = "Your remaining right is $remainingRight"
                    gameBinding.textViewLastGuess.text = "Your last is $userGuess"

                    gameBinding.textViewHint.visibility = View.VISIBLE
                    gameBinding.textViewLastGuess.visibility = View.VISIBLE
                    gameBinding.textViewRight.visibility = View.VISIBLE

                    gameBinding.editTextNumber.setText("")

                }

            }catch (e : Exception){
                Toast.makeText(applicationContext,"Please enter a valid guess",Toast.LENGTH_LONG).show()
            }

        }

    }

    fun createDialogMessage(){
        val dialogBox = AlertDialog.Builder(this@GameActivity)
        dialogBox.setTitle("Number Guessing Game")
        if (remainingRight > 0){

            dialogBox.setMessage("\t Congratulations, The number in my mind was $randomNumber." +
                    "\n\n\t You know my number in $steps steps." +
                    "\n\n\t Your guesses: $guesses" +
                    "\n\n\t Do you want to play again?")

        }else{
            dialogBox.setMessage("\t Sorry, your right to guess is over." +
                    "\n\n\t The number in my mind was $randomNumber" +
                    "\n\n\t Your guesses: $guesses" +
                    "\n\n\t Do you want to play again?")
        }
        dialogBox.setCancelable(false)
        dialogBox.setNegativeButton("NO", DialogInterface.OnClickListener { dialog, which ->

            finishAffinity()

        })
        dialogBox.setPositiveButton("YES", DialogInterface.OnClickListener { dialog, which ->

            val intent = Intent(this@GameActivity,MainActivity::class.java)
            startActivity(intent)
            finish()

        })

        dialogBox.create().show()

    }
}