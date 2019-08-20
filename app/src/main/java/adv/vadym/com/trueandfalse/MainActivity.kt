package adv.vadym.com.trueandfalse

import android.animation.ObjectAnimator
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.RadioButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var isYes = false
    private var isVolume = false
    private var isRandomSelect = true
    private var coinType = CoinType.OREL
    private var coinMap = HashMap<String, Drawable>()
    private lateinit var preference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        preference = PreferenceManager.getDefaultSharedPreferences(this)


        iv_volume.setOnClickListener {
            isVolume = !isVolume
            if (isVolume) {
                iv_volume.setImageDrawable(resources.getDrawable(R.drawable.ic_volume_up))
                Toast.makeText(this, "звук включен", Toast.LENGTH_SHORT).show()
            } else {
                iv_volume.setImageDrawable(resources.getDrawable(R.drawable.ic_volume_off))
                Toast.makeText(this, "звук выключен", Toast.LENGTH_SHORT).show()
            }
        }

        iv_settings.setOnClickListener {
            AlertDialog.Builder(this).apply {
                val subView = LayoutInflater.from(this@MainActivity).inflate(R.layout.settings_dialog_activity, null)
                val randomRB = subView.findViewById<RadioButton>(R.id.rb_random)
                val handleRB = subView.findViewById<RadioButton>(R.id.rb_handle)
                randomRB.isChecked = preference.getBoolean("isRandomSaved", isRandomSelect)
                handleRB.isChecked = preference.getBoolean("isHandleSaved", !isRandomSelect)

                setTitle(resources.getString(R.string.dialog_settings_title))
                setView(subView)

                setPositiveButton("OK") {_, _ ->
                    if (randomRB.isChecked) {
                        isRandomSelect = true
                        Toast.makeText(this@MainActivity , "рандомный способ", Toast.LENGTH_SHORT).show()
                    } else {
                        isRandomSelect = false
                        Toast.makeText(this@MainActivity, "ручной способ", Toast.LENGTH_SHORT).show()
                    }
                    preference.edit().putBoolean("isRandomSaved", isRandomSelect).apply()
                    preference.edit().putBoolean("isHandleSaved", !isRandomSelect).apply()
                }
                create()
            }.show()
        }

        im_coin.setOnClickListener {
            if (isVolume) {
                val mp = MediaPlayer.create(this, R.raw.moneta)
                mp.start()
            }

            isYes = Math.random() < 0.5
            animation(it)

            if (isYes) {
                im_coin.setImageDrawable(resources.getDrawable(R.drawable.orel))
                when (coinType) {
                    CoinType.YES -> im_coin.setImageDrawable(coinMap["yes"])
                    CoinType.OREL -> im_coin.setImageDrawable(coinMap["orel"])
                    CoinType.LOVE -> im_coin.setImageDrawable(coinMap["love"])
                    CoinType.VERNYAK -> im_coin.setImageDrawable(coinMap["vernyak"])
                    else -> im_coin.setImageDrawable(coinMap["orel"])
                }
            } else {
                im_coin.setImageDrawable(resources.getDrawable(R.drawable.reshka))
                when (coinType) {
                    CoinType.YES -> im_coin.setImageDrawable(coinMap["no"])
                    CoinType.OREL -> im_coin.setImageDrawable(coinMap["reshka"])
                    CoinType.LOVE -> im_coin.setImageDrawable(coinMap["hates"])
                    CoinType.VERNYAK -> im_coin.setImageDrawable(coinMap["oblom"])
                    else -> im_coin.setImageDrawable(coinMap["reshka"])
                }
            }

        }


    }

    private fun animation(view: View) {
        val animation = ObjectAnimator.ofFloat(view, View.ROTATION_X, 0.0f, 360f)
        if (isRandomSelect) {
            animation.cancel()
            animation.apply {
                duration = 100
                repeatCount = ObjectAnimator.RESTART
                interpolator = AccelerateDecelerateInterpolator()
            }.start()
        } else {
            animation.apply {
                duration = 80
                repeatCount = ObjectAnimator.INFINITE
                interpolator = AccelerateDecelerateInterpolator()
            }.start()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.type_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.orel -> {
                coinType = CoinType.OREL
                im_coin.setImageDrawable(resources.getDrawable(R.drawable.orel_reshka2))
                coinMap["orel"] = resources.getDrawable(R.drawable.orel)
                coinMap["reshka"] = resources.getDrawable(R.drawable.reshka)
            }
            R.id.yes -> {
                coinType = CoinType.YES
                coinMap["yes"] = resources.getDrawable(R.drawable.yes)
                coinMap["no"] = resources.getDrawable(R.drawable.no)
            }
            R.id.love -> {
                coinType = CoinType.LOVE
                im_coin.setImageDrawable(resources.getDrawable(R.drawable.lubit_nelubit))
                coinMap["love"] = resources.getDrawable(R.drawable.lubit)
                coinMap["hates"] = resources.getDrawable(R.drawable.nelubit)
            }
            R.id.luck -> { Toast.makeText(this, "click удача - неудача", Toast.LENGTH_SHORT).show() }
            R.id.vernyak -> {
                coinType = CoinType.VERNYAK
                im_coin.setImageDrawable(resources.getDrawable(R.drawable.vernyak_oblom))
                coinMap["vernyak"] = resources.getDrawable(R.drawable.vernyak)
                coinMap["oblom"] = resources.getDrawable(R.drawable.oblom)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    enum class CoinType {
        OREL, YES, LOVE, LUCK, VERNYAK
    }
}
