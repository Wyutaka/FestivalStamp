package com.example.nakatsuka.newgit.mainAction

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.KeyEvent
import com.example.nakatsuka.newgit.R
import com.example.nakatsuka.newgit.navigationAction.*
import kotlinx.android.synthetic.main.activity_main.*
import org.altbeacon.beacon.BeaconConsumer
import android.bluetooth.BluetoothAdapter



private val RESULT_SUBACTIVITY: Int = 1000
//位置情報取得のために必要な定数（ここで設定しさえすれば、どんな値でも良いです）
private val PERMISSION_REQUEST_COARSE_LOCATION = 1
//Bluetoothをオンにするために必要な定数（ここで設定しさえすれば、どんな値でも良いです）
private val REQUEST_ENABLE_BT = 1

/*Todo fragmentの処理　
  Todo APITestの部分の差し替え
  Todo Goal後のアラートの実装*/
class MainActivity : AppCompatActivity(), BeaconConsumer {
    private val TAG = this.javaClass.simpleName

    private lateinit var prefer: SharedPreferences
    private lateinit var stampFragment: StampFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


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
            val transaction = supportFragmentManager.beginTransaction()

            //追記:beaconでuuidを用いるので、bundleを使ってMainActivity->StampFragment間の値渡しをします
            val bnd = Bundle()
            Log.d(TAG,prefer.getString("UUID",""))
            bnd.putString("UUID", prefer.getString("UUID", ""))

            stampFragment = StampFragment()
            stampFragment.arguments = bnd

            transaction.replace(R.id.container, stampFragment)
            transaction.commit()
        }

        var nowFragment = 0
        val normal: Int = R.drawable.normalorengebutton
        val inverted: Int = R.drawable.invertedorangebutton
        //Todo アニメーションの追加
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
                transaction.addToBackStack(null)

                //パラメータを設定
                transaction.replace(R.id.container, stampFragment)

                nowFragment = 0
                transaction.commit()
            }
        }
        map.setOnClickListener {

            stamp_rally.setBackgroundResource(normal)
            map.setBackgroundResource(inverted)
            board.setBackgroundResource(normal)
            time_table.setBackgroundResource(normal)
            others.setBackgroundResource(normal)
            Log.d("fragment", "called")
            if (savedInstanceState == null) {
                nowFragment = 1
                val transaction = supportFragmentManager.beginTransaction()
                //パラメータを設定
                transaction.replace(R.id.container, MapFragment())
                //バックスタックを設定
                transaction.addToBackStack(null)
                transaction.commit()
            }
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

                val buttonResult = mutableListOf(false, false, false, false, false, false)
                val answerNumber: Int? = intent!!.getIntExtra("answerNumber", 6)
                buttonResult[answerNumber!!] = true

                val mStampFragment = StampFragment()
                when (answerNumber) {
                    0 -> if (buttonResult[0]) {
                        mStampFragment.setState(buttonResult[0], 0)
                    }
                    1 -> if (buttonResult[1]) {
                        mStampFragment.setState(buttonResult[1], 1)
                    }
                    2 -> if (buttonResult[2]) {
                        mStampFragment.setState(buttonResult[2], 2)
                    }
                    3 -> if (buttonResult[3]) {
                        mStampFragment.setState(buttonResult[3], 3)
                    }
                    4 -> if (buttonResult[4]) {
                        mStampFragment.setState(buttonResult[4], 4)
                    }
                    5 -> if (buttonResult[5]) {
                        mStampFragment.setState(buttonResult[5], 5)
                    }
                }
            }
        }
    }

    override fun onBeaconServiceConnect() {
        stampFragment.onBeaconServiceConnect()
    }

    override fun onResume() {
        super.onResume()
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }
}

