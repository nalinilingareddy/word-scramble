package com.example.word_scramble

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var scoreTextView: TextView
    private lateinit var scrambledWordTextView: TextView
    private lateinit var guessEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var newWordButton: Button
    private lateinit var skipButton: Button
    private lateinit var resultTextView: TextView
    private lateinit var difficultySpinner: Spinner

    private var currentWord = ""
    private var score = 0
    private var attempts = 0

    private val easyWords = listOf(
        "cat", "dog", "sun", "moon", "tree", "book", "fish", "bird", "apple", "car",
        "hat", "ball", "star", "hand", "play", "milk", "rose", "cake", "door", "shoe",
        "frog", "coat", "fork", "sock", "cake", "kite", "ring", "leaf", "pool", "coin",
        "hill", "toy", "desk", "lamp", "road", "wave", "kite", "duck", "sock", "sail",
        "pen", "map", "star", "sand", "bell", "fire", "cake", "ring", "nest", "pool",
        "wind", "fish", "shoe", "moon", "cat", "dog", "egg", "ball", "hand", "hat",
        "rose", "bird", "leaf", "book", "car", "toy", "sky", "tree", "drum", "worm",
        "nose", "tail", "ring", "hand", "race", "clay", "seat", "step", "hand", "sky",
        "cloud", "park", "root", "bell", "flame", "well", "ship", "star", "gate", "web",
        "horn", "cake", "lamp", "nest", "frog", "leaf", "spot", "line", "bark", "park",
        "bike", "bowl", "beet", "bump", "cap", "drum", "farm", "frog", "game", "hill",
        "kite", "lawn", "mole", "nest", "peach", "quilt", "sock", "tent", "van", "wind",
        "bell", "cage", "dust", "face", "glow", "horn", "ice", "joke", "knee", "lamp",
        "moon", "nose", "owl", "pole", "quack", "rose", "soap", "tree", "urn", "vest",
        "wave", "yard", "zoo", "dust", "palm", "web", "band", "coal", "crew", "deck"
    )

    private val mediumWords = listOf(
        "bicycle", "candle", "dolphin", "elephant", "forest", "garden", "hiking", "igloo", "jigsaw", "kangaroo",
        "lighthouse", "mountain", "notebook", "octopus", "penguin", "quicksand", "rainbow", "scissors", "tornado", "unicorn",
        "vampire", "whistle", "xylophone", "yacht", "zebra", "adventure", "balloon", "chocolate", "dinosaur", "exercise",
        "firefighter", "giraffe", "hamburger", "internet", "jungle", "koala", "labyrinth", "monopoly", "notebook", "octagon",
        "parachute", "quokka", "rainforest", "sapphire", "telescope", "universe", "volcano", "watermelon", "xenophobia", "yesterday",
        "algebra", "butterfly", "caterpillar", "dolphin", "exploration", "furniture", "geography", "hydrangea", "insect", "jigsaw",
        "keyboard", "labrador", "mushroom", "nutcracker", "ostrich", "pomegranate", "quilt", "raccoon", "sundial", "tortoise",
        "ukulele", "vulture", "weather", "x-ray", "yarn", "zucchini", "basketball", "calculator", "dramatics", "electricity",
        "fountain", "grapefruit", "houseplant", "imagination", "jamboree", "kettle", "landscape", "molecule", "narrative", "opulent",
        "plankton", "quasar", "research", "scavenger", "turbulence", "underwater", "vegetable", "waterfall", "xylophonist", "youthful",
        "archaeology", "biography", "conductor", "diplomat", "ecology", "flamingo", "glacier", "harpoon", "incubator", "juggernaut",
        "kaleidoscope", "laboratory", "mathematics", "nebulous", "ornithology", "peacock", "quarantine", "revolution", "silhouette", "telescope"
    )

    private val hardWords = listOf(
        "aberration", "benevolent", "cacophony", "dichotomy", "effervescent", "facetious", "garrulous", "heuristic", "incomprehensible", "juxtaposition",
        "kaleidoscope", "loquacious", "metamorphosis", "nebulous", "obfuscate", "perpendicular", "quintessential", "rhetoric", "serendipity", "transcend",
        "ubiquitous", "vicarious", "warranted", "xenophobic", "yarn", "zephyr", "archetype", "bombastic", "cacophony", "debilitate",
        "ephemeral", "fastidious", "grandiloquent", "haphazard", "iconoclast", "juxtaposition", "kismet", "languid", "machiavellian", "nihilism",
        "obsequious", "pseudonym", "quagmire", "recalcitrant", "sophistry", "tenacious", "unilateral", "vicissitude", "whimsical", "xylophone",
        "yesterday", "zealous", "anachronism", "bamboozle", "camaraderie", "dichotomy", "epiphany", "fervent", "gargantuan", "histrionic",
        "idiom", "juxtapose", "knapsack", "lucrative", "misanthrope", "neologism", "opulent", "paradigm", "quixotic", "resilient",
        "sanctimonious", "tenebrous", "unfathomable", "venerable", "xenophile", "yawning", "zephyr", "antithesis", "bravado", "capricious",
        "despondent", "euphoria", "flabbergasted", "grandiose", "hubris", "ineffable", "juxtaposition", "kaleidoscopic", "litigious", "meticulous",
        "nonchalant", "obstinate", "precarious", "quizzical", "resplendent", "subterfuge", "terrestrial", "ubiquitous", "vicarious", "whimsy",
        "xenogenesis", "yesteryear", "zealousness", "abomination", "banishment", "cognition", "delineate", "efficacious", "fortuitous", "germinate"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        scoreTextView = findViewById(R.id.scoreTextView)
        scrambledWordTextView = findViewById(R.id.scrambledWordTextView)
        guessEditText = findViewById(R.id.guessEditText)
        submitButton = findViewById(R.id.submitButton)
        newWordButton = findViewById(R.id.newWordButton)
        skipButton = findViewById(R.id.skipButton)
        resultTextView = findViewById(R.id.resultTextView)
        difficultySpinner = findViewById(R.id.difficultySpinner)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.difficulty_levels,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        }

        difficultySpinner.adapter = adapter

        difficultySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                newWord()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        submitButton.setOnClickListener { checkGuess() }
        newWordButton.setOnClickListener { newWord() }
        skipButton.setOnClickListener { skipWord() }

        newWord()
    }

    private fun newWord() {
        attempts = 0
        newWordButton.visibility = View.GONE
        skipButton.visibility = View.VISIBLE
        guessEditText.isEnabled = true
        submitButton.isEnabled = true

        val wordList = when (difficultySpinner.selectedItem.toString()) {
            "Easy" -> easyWords
            "Medium" -> mediumWords
            "Hard" -> hardWords
            else -> mediumWords
        }

        currentWord = wordList.random()
        val scrambledWord = currentWord.toList().shuffled().joinToString("")
        scrambledWordTextView.text = scrambledWord
        guessEditText.text.clear()
        resultTextView.text = ""
        Log.d("WordScramble", "New word generated: $currentWord")
    }

    private fun checkGuess() {
        val guess = guessEditText.text.toString().trim().lowercase()
        Log.d("WordScramble", "Guess: $guess, Current Word: ${currentWord.lowercase()}")
        Log.d("WordScramble", "Are they equal? ${guess.equals(currentWord, ignoreCase = true)}")

        if (guess.equals(currentWord, ignoreCase = true)) {
            score++
            scoreTextView.text = "Score: $score"
            resultTextView.text = "Correct!"
            resultTextView.setTextColor(ContextCompat.getColor(this, R.color.correctGreen))

            Handler(Looper.getMainLooper()).postDelayed({
                newWord()
            }, 1000)
        } else {
            attempts++
            if (attempts < 3) {
                resultTextView.text = "Incorrect. Try again!"
                resultTextView.setTextColor(ContextCompat.getColor(this, R.color.incorrectRed))
            } else {
                resultTextView.text = "Three attempts over! The correct answer is: $currentWord"
                resultTextView.setTextColor(Color.RED)
                guessEditText.isEnabled = false
                submitButton.isEnabled = false
                newWordButton.visibility = View.VISIBLE
                skipButton.visibility = View.GONE
            }
        }
    }

    private fun skipWord() {
        newWord()
        resultTextView.text = "Word skipped"
        resultTextView.setTextColor(Color.BLUE)
    }
}