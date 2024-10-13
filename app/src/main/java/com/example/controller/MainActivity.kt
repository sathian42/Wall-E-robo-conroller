package com.example.controller

import android.os.Bundle
import android.os.Vibrator
import android.content.Context
import android.media.MediaPlayer
import android.os.VibrationEffect
import android.speech.tts.TextToSpeech
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private val ipAddress = "192.168.4.1"  // Default IP for ESP8266 in AP mode
    private val client = OkHttpClient()
    private var speed = 50  // Initial speed
    private lateinit var vibrator: Vibrator
    private lateinit var tts: TextToSpeech
    private lateinit var textInput: EditText
    private lateinit var speakButton: Button
    private lateinit var mediaPlayer: MediaPlayer

    private var isGlowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        tts = TextToSpeech(this, this)

        val ipAddressTextView = findViewById<TextView>(R.id.ipAddressTextView)
        ipAddressTextView.text = "IP Address: $ipAddress"

        val showPopupButton = findViewById<ImageButton>(R.id.emoji)
        showPopupButton.setOnClickListener { view ->
            showPopupMenu(view)
        }
        val rightButton = findViewById<ImageButton>(R.id.buttonRight)
        val forwardButton = findViewById<ImageButton>(R.id.buttonForward)
        val backwardButton = findViewById<ImageButton>(R.id.buttonBackward)
        val leftButton = findViewById<ImageButton>(R.id.buttonLeft)

        val forwardLeftButton = findViewById<ImageButton>(R.id.buttonForwardLeft)
        val forwardRightButton = findViewById<ImageButton>(R.id.buttonForwardRight)
        val stopButton = findViewById<ImageButton>(R.id.buttonStop)
        val backwardLeftButton = findViewById<ImageButton>(R.id.buttonBackwardLeft)
        val backwardRightButton = findViewById<ImageButton>(R.id.buttonBackwardRight)

        val RightHandUp = findViewById<ImageButton>(R.id.RightHandUp)
        val RightHandMid = findViewById<ImageButton>(R.id.RightHandMid)
        val RightHandDown = findViewById<ImageButton>(R.id.RightHandDown)

        val LeftHandUp = findViewById<ImageButton>(R.id.LeftHandUp)
        val LeftHandMid =findViewById<ImageButton>(R.id.LeftHandMid)
        val LeftHandDown = findViewById<ImageButton>(R.id.LeftHandDown)

        val HeadLeft = findViewById<ImageButton>(R.id.HeadLeft)
        val HeadHoriMid = findViewById<ImageButton>(R.id.HeadHMid)
        val HeadRight = findViewById<ImageButton>(R.id.HeadRight)

        val HeadUP = findViewById<ImageButton>(R.id.HeadUp)
        val HeadVertMid = findViewById<ImageButton>(R.id.HeadVMid)
        val HeadDown = findViewById<ImageButton>(R.id.HeadDown)

        val Glow = findViewById<ImageButton>(R.id.gloweyes)

        val speedSeekBar = findViewById<SeekBar>(R.id.slider)
        val speakButton = findViewById<ImageButton>(R.id.speakButton)
        textInput = findViewById(R.id.textInput)


        Glow.setOnClickListener {
            vibrate()


                // Toggle the state
                isGlowing = !isGlowing

                // Update the button image based on the new state
                if (isGlowing) {
                    Glow.setImageResource(R.drawable.bulb_on)
                    // Send command to turn on the LED
                    sendCommand("GLOW")
                } else {
                    Glow.setImageResource(R.drawable.bulb)
                    // Optionally send a command to turn off the LED
                    sendCommand("GLOW_OFF")
                }
        }

        HeadLeft.setOnClickListener{
            vibrate()
            sendCommand("HL")
        }

        HeadHoriMid.setOnClickListener {
            vibrate()
            sendCommand("HHM")
        }

        HeadRight.setOnClickListener{
            vibrate()
            sendCommand("HR ")
        }

        HeadUP.setOnClickListener{
            vibrate()
            sendCommand("HU")
        }

        HeadVertMid.setOnClickListener {
            vibrate()
            sendCommand("HVM")
        }

        HeadDown.setOnClickListener{
            vibrate()
            sendCommand("HD")
        }

        speakButton.setOnClickListener {
            vibrate()
            speakOut()
        }

        forwardLeftButton.setOnClickListener {
            vibrate()
            sendCommand("FL")
        }

        forwardRightButton.setOnClickListener {
            vibrate()
            sendCommand("FR")
        }

        stopButton.setOnClickListener {
            vibrate()
            sendCommand("S")
        }

        backwardLeftButton.setOnClickListener {
            vibrate()
            sendCommand("BL")
        }

        backwardRightButton.setOnClickListener {
            vibrate()
            sendCommand("BR")
        }
        RightHandUp.setOnClickListener {
            vibrate()
            sendCommand("RHU")
        }

        RightHandMid.setOnClickListener {
            vibrate()
            sendCommand("RHM")
        }

        RightHandDown.setOnClickListener {
            vibrate()
            sendCommand("RHD")
        }
        LeftHandUp.setOnClickListener {
            vibrate()
            sendCommand("LHU")
        }

        LeftHandMid.setOnClickListener {
            vibrate()
            sendCommand("LHM")
        }

        LeftHandDown.setOnClickListener {
            vibrate()  
            sendCommand("LHD")
        }

        rightButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Send "R" command
                    sendCommand("R")
                }
                MotionEvent.ACTION_UP -> {
                    // Send "S" command
                    sendCommand("S")
                    v.performClick() // Ensure accessibility and proper click handling
                }
            }
            true
        }

        forwardButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Send "F" command
                    sendCommand("F")
                }
                MotionEvent.ACTION_UP -> {
                    // Send "S" command
                    sendCommand("S")
                    v.performClick() // Ensure accessibility and proper click handling
                }
            }
            true
        }

        backwardButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Send "B" command
                    sendCommand("B")
                }
                MotionEvent.ACTION_UP -> {
                    // Send "S" command
                    sendCommand("S")
                    v.performClick() // Ensure accessibility and proper click handling
                }
            }
            true
        }

        leftButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Send "L" command
                    sendCommand("L")
                }
                MotionEvent.ACTION_UP -> {
                    // Send "S" command
                    sendCommand("S")
                    v.performClick() // Ensure accessibility and proper click handling
                }
            }
            true
        }



        speedSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                // Map the progress to the range 0-9
                val commandValue = when (progress) {
                    in 1..9 -> "0"  // Send stop command for 0% to 9%
                    in 10..19 -> "1"
                    in 20..29 -> "2"
                    in 30..39 -> "3"
                    in 40..49 -> "4"
                    in 50..59 -> "5"
                    in 60..69 -> "6"
                    in 70..79 -> "7"
                    in 80..89 -> "8"
                    in 90..100 -> "9"
                    else -> "S"  // Default to stop if out of range
                }

                sendCommand(commandValue)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show()
            }

            // Set pitch and speed for Wall-E like voice
            tts.setPitch(2.3f)  // High pitch for a robotic sound
            tts.setSpeechRate(0.9f)  // Slow speech rate for deliberate, mechanical delivery

            // Optionally list available voices and select one
            val voices = tts.voices
            if (voices != null) {
                for (voice in voices) {
                    if (voice.name.contains("en-us-x-sfg#male")) {
                        tts.voice = voice
                        break
                    }
                }
            }
        } else {
            Toast.makeText(this, "Initialization failed", Toast.LENGTH_SHORT).show()
        }
    }


    private fun vibrate() {
        if (vibrator.hasVibrator()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(200)
            }
        }
    }

    private fun speakOut() {
        val text = textInput.text.toString()
        val pitch = 4.5f // Set the pitch value to modify the voice (1.0 is default)
        tts.setPitch(pitch) // Set the pitch for helium-like voice
        tts.setSpeechRate(0.6f)
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        tts.setPitch(1.0f) // Reset the pitch back to normal after speaking
    }


    private fun sendCommand(command: String) {
        val url = "http://$ipAddress/?State=$command"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Failed to send command: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Command sent successfully", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onDestroy() {
        if (tts != null) {
            tts.stop()
            tts.shutdown()
        }

        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        super.onDestroy()
    }

    private fun sendSpeed(speed: Int) {
        val url = "http://$ipAddress/?Speed=$speed"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Failed to send speed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Speed set successfully", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    private fun showPopupMenu(view: View) {
        val popup = PopupMenu(this, view)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.emotes, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            handleMenuItemClick(menuItem)
        }
        popup.show()
    }

    private fun handleMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.speech1 -> {
                Toast.makeText(this, "speech 1", Toast.LENGTH_SHORT).show()
                sendCommand("SAYHI")
                mediaPlayer = MediaPlayer.create(this, R.raw.speech1)
                mediaPlayer.start()
                true

            }

            R.id.speech2 -> {
                Toast.makeText(this, "speech 2", Toast.LENGTH_SHORT).show()
                sendCommand("SAYHI")
                mediaPlayer = MediaPlayer.create(this, R.raw.speech2)
                mediaPlayer.start()
                true

            }

            R.id.hello -> {
                Toast.makeText(this, "Action One clicked", Toast.LENGTH_SHORT).show()
                sendCommand("SAYHI")
                mediaPlayer = MediaPlayer.create(this, R.raw.walle)
                mediaPlayer.start()
                true

            }
            R.id.no -> {
                Toast.makeText(this, "Action Two clicked", Toast.LENGTH_SHORT).show()
                sendCommand("SAYNO")
                mediaPlayer = MediaPlayer.create(this, R.raw.no)
                mediaPlayer.start()
                true
            }

            R.id.Yes -> {
                Toast.makeText(this, "Action Two clicked", Toast.LENGTH_SHORT).show()
                sendCommand("SAYYES")
                mediaPlayer = MediaPlayer.create(this, R.raw.yes)
                mediaPlayer.start()
                true
            }

            R.id.eva -> {
                Toast.makeText(this, "Action Two clicked", Toast.LENGTH_SHORT).show()
                sendCommand("EVA")
                mediaPlayer = MediaPlayer.create(this, R.raw.eve)
                mediaPlayer.start()
                true
            }

            R.id.welcome -> {
                Toast.makeText(this, "Action Two clicked", Toast.LENGTH_SHORT).show()
                sendCommand("WEL")
                mediaPlayer = MediaPlayer.create(this, R.raw.welcome)
                mediaPlayer.start()
                true
            }

            R.id.gm -> {
                Toast.makeText(this, "Action Two clicked", Toast.LENGTH_SHORT).show()
                sendCommand("GM")
                mediaPlayer = MediaPlayer.create(this, R.raw.gm)
                mediaPlayer.start()
                true
            }

            R.id.ga -> {
                Toast.makeText(this, "Action Two clicked", Toast.LENGTH_SHORT).show()
                sendCommand("GA")
                mediaPlayer = MediaPlayer.create(this, R.raw.ga)
                mediaPlayer.start()
                true
            }

            R.id.ge -> {
                Toast.makeText(this, "Action Two clicked", Toast.LENGTH_SHORT).show()
                sendCommand("GE")
                mediaPlayer = MediaPlayer.create(this, R.raw.ge)
                mediaPlayer.start()
                true
            }

            R.id.exm -> {
                Toast.makeText(this, "Action Two clicked", Toast.LENGTH_SHORT).show()
                sendCommand("EXM")
                mediaPlayer = MediaPlayer.create(this, R.raw.excuseme)
                mediaPlayer.start()
                true
            }

            R.id.help -> {
                Toast.makeText(this, "Action Two clicked", Toast.LENGTH_SHORT).show()
                sendCommand("EXM")
                mediaPlayer = MediaPlayer.create(this, R.raw.excuseme)
                mediaPlayer.start()
                true
            }
            else -> false
        }
    }



}
