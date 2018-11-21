package com.example.nakatsuka.newgit.mainAction

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.widget.Toast
import com.example.nakatsuka.newgit.R
import com.example.nakatsuka.newgit.navigationAction.*
import kotlinx.android.synthetic.main.activity_main.*
import org.altbeacon.beacon.BeaconConsumer


private val RESULT_SUBACTIVITY: Int = 1000
//位置情報取得のために必要な定数（ここで設定しさえすれば、どんな値でも良いです）
private val PERMISSION_REQUEST_COARSE_LOCATION = 1
//Bluetoothをオンにするために必要な定数（ここで設定しさえすれば、どんな値でも良いです）
private val REQUEST_ENABLE_BT = 1

/*Todo fragmentの処理　
  Todo APITestの部分の差し替え
  Todo Goal後のアラートの実装*/
class MainActivity : AppCompatActivity(), BeaconConsumer, StampFragment.fragmentListner {
    private val TAG = this.javaClass.simpleName

    private lateinit var prefer: SharedPreferences
    private lateinit var stampFragment: StampFragment
    val buttonResult: IntArray = intArrayOf(0, 0, 0, 0, 0, 0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.apply {
            buttonResult[0] = pref.getInt("buttonResult[0]", 0)
            buttonResult[1] = pref.getInt("buttonResult[1]", 0)
            buttonResult[2] = pref.getInt("buttonResult[2]", 0)
            buttonResult[3] = pref.getInt("buttonResult[3]", 0)
            buttonResult[4] = pref.getInt("buttonResult[4]", 0)
            buttonResult[5] = pref.getInt("buttonResult[5]", 0)
            buttonResult[6] = pref.getInt("buttonResult[6]", 0)
        }


        //位置情報パーミッションの確認
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_COARSE_LOCATION)
            }
        }


        //前のActivityからuuidをもらうためのsharedPreferences
        prefer = getSharedPreferences("prefer", Context.MODE_PRIVATE)

        //fragmentの初期設定
        if (savedInstanceState == null) {
            stamp_rally.setBackgroundResource(R.drawable.invertedorangebutton)
            val transaction = supportFragmentManager.beginTransaction()

            //追記:beaconでuuidを用いるので、bundleを使ってMainActivity->StampFragment間の値渡しをします
            val bnd = Bundle()
            Log.d(TAG, prefer.getString("UUID", ""))
            bnd.putString("UUID", prefer.getString("UUID", ""))
            bnd.putString("USERNAME", prefer.getString("USERNAME", ""))
            //bundleを用いてbuttonResultをfragmentに提供
            bnd.putIntArray("buttonResult", buttonResult)


            stampFragment = StampFragment()
            stampFragment.arguments = bnd

            transaction.replace(R.id.container, stampFragment)
            transaction.commit()


        }

        var nowFragment = 0
        val normal: Int = R.drawable.normalorengebutton
        val inverted: Int = R.drawable.invertedorangebutton
        stamp_rally.setOnClickListener {
            stamp_rally.setBackgroundResource(inverted)
            map.setBackgroundResource(normal)
            board.setBackgroundResource(normal)
            time_table.setBackgroundResource(normal)
            others.setBackgroundResource(normal)
            // コードからフラグメントを追加
            Log.d("fragment", "called!!")
            if (savedInstanceState == null) {
                val transaction = supportFragmentManager.beginTransaction()
                //バックスタックを設定
                //transaction.addToBackStack(null)

                //パラメータを設定
                transaction.add(R.id.container, stampFragment!!)


                nowFragment = 0
                transaction.commit()
            }
            fragment_title.text = "スタンプラリー"
        }
        map.setOnClickListener {

            stamp_rally.setBackgroundResource(normal)
            map.setBackgroundResource(inverted)
            board.setBackgroundResource(normal)
            time_table.setBackgroundResource(normal)
            others.setBackgroundResource(normal)
            if (savedInstanceState == null) {
                nowFragment = 1
                val transaction = supportFragmentManager.beginTransaction()
                //パラメータを設定
                transaction.replace(R.id.container, MapFragment())
                //バックスタックを設定
                transaction.addToBackStack(null)
                transaction.commit()
            }
            fragment_title.text = "地図"
        }
        board.setOnClickListener {

            stamp_rally.setBackgroundResource(normal)
            map.setBackgroundResource(normal)
            board.setBackgroundResource(inverted)
            time_table.setBackgroundResource(normal)
            others.setBackgroundResource(normal)
            Log.d("fragment", "called")
            if (savedInstanceState == null) {
                nowFragment = 2
                val transaction = supportFragmentManager.beginTransaction()
                //パラメータを設定
                transaction.replace(R.id.container, BoardFragment())
                //バックスタックを設定
                //transaction.addToBackStack(null)
                transaction.commit()
            }
            fragment_title.text = "掲示板"
        }
        time_table.setOnClickListener {

            stamp_rally.setBackgroundResource(normal)
            map.setBackgroundResource(normal)
            board.setBackgroundResource(normal)
            time_table.setBackgroundResource(inverted)
            others.setBackgroundResource(normal)
            Log.d("fragment", "called")
            if (savedInstanceState == null) {
                nowFragment = 3
                val transaction = supportFragmentManager.beginTransaction()
                //パラメータを設定
                transaction.replace(R.id.container, ScheduleFragment())
                //バックスタックを設定
                //transaction.addToBackStack(null)
                transaction.commit()
            }
            fragment_title.text = "予定表"
        }
        others.setOnClickListener {

            stamp_rally.setBackgroundResource(normal)
            map.setBackgroundResource(normal)
            board.setBackgroundResource(normal)
            time_table.setBackgroundResource(normal)
            others.setBackgroundResource(inverted)
            Log.d("fragment", "called")
            if (savedInstanceState == null) {
                nowFragment = 4
                val transaction = supportFragmentManager.beginTransaction()
                //パラメータを設定
                transaction.replace(R.id.container, OthersFragment())
                //バックスタックを設定
                //transaction.addToBackStack(null)
                transaction.commit()
            }
            fragment_title.text = "その他"
        }

    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        AlertDialog.Builder(this)
                .setTitle("確認")
                .setMessage("アプリを終了しますか？")
                .setPositiveButton("OK") { _, _ ->
                    this.moveTaskToBack(true)
                }
                .setNegativeButton("キャンセル") { _, _ ->
                }
                .show()
        return super.onKeyDown(keyCode, event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == RESULT_SUBACTIVITY) {
            if (resultCode == result_canceled) {
            } else if (resultCode == AppCompatActivity.RESULT_OK) {

                val answerNumber: Int? = intent!!.getIntExtra("answerNumber", 0)
                val pref = PreferenceManager.getDefaultSharedPreferences(this)
                val editor = pref.edit()
                editor.putInt("buttonResult[answerNumber]", 2)
                editor.apply()
                buttonResult[answerNumber!!] = 2

                if (buttonResult[1] == 2 && buttonResult[2] == 2 && buttonResult[3] == 2 && buttonResult[4] == 2 && buttonResult[4] == 2 && buttonResult[5] == 2 && buttonResult[6] == 2) {
                    for (i in 1..6) {
                        buttonResult[i] = 3
                    }}



                val transaction = supportFragmentManager.beginTransaction()
                    //追記:beaconでuuidを用いるので、bundleを使ってMainActivity->StampFragment間の値渡しをします
                    val bnd = Bundle()
                    //Log.d(TAG,prefer.getString("UUID",""))
                    //bnd.putString("UUID", prefer.getString("UUID", ""))
                    //bundleを用いてbuttonResultをfragmentに提供
                    bnd.putIntArray("buttonResult",buttonResult)

                stampFragment = StampFragment()
                stampFragment.arguments = bnd

                transaction.replace(R.id.container, stampFragment)
                transaction.commit()



                //val mStampFragment = StampFragment()
                //現在使用していないため、削除しました。
                /*
                val buttonResult = mutableListOf(false, false, false, false, false, false)
                val answerNumber: Int? = intent!!.getIntExtra("answerNumber", 6)

                buttonResult[answerNumber!!] = true

                val mStampFragment = StampFragment()

                when (answerNumber) {
                    1 -> if (buttonResult[0]) {
                        mStampFragment.setState(buttonResult[1], 1)
                    }
                    2 -> if (buttonResult[1]) {
                        mStampFragment.setState(buttonResult[2], 2)
                    }
                    3 -> if (buttonResult[2]) {
                        mStampFragment.setState(buttonResult[3], 3)
                    }
                    4 -> if (buttonResult[3]) {
                        mStampFragment.setState(buttonResult[4], 4)
                    }
                    5 -> if (buttonResult[4]) {
                        mStampFragment.setState(buttonResult[5], 5)
                    }
                    6 -> if (buttonResult[5]) {
                        mStampFragment.setState(buttonResult[6], 6)
                    }
                }
                */
            }
        }
    }

    override fun goActivity(answerNumber: Int, isSend: Boolean, imageUrl: String) {
        val intent = Intent(this, SecondActivity::class.java)
        if (buttonResult[answerNumber] == 2) {
            val completed = "すでにスタンプは押されています"
            makeToast(completed, 0, this.for_scale.height)
        } else {
            intent.putExtra("AnswerNumber", answerNumber)
            intent.putExtra("ImageUrl", imageUrl[answerNumber])
            if (isSend) {
                startActivityForResult(intent, RESULT_SUBACTIVITY)
            }
        }
    }

    private fun makeToast(message: String, x: Int, y: Int) {
        val toast: Toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, x, y / 4)
        toast.show()
    }


    override fun onBeaconServiceConnect() {
        stampFragment!!.onBeaconServiceConnect()
    }

    override fun onResume() {
        super.onResume()
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }
}



