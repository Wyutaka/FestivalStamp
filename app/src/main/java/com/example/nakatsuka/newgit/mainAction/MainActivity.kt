package com.example.nakatsuka.newgit.mainAction

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.widget.Toast
import com.example.nakatsuka.newgit.R
import com.example.nakatsuka.newgit.mainAction.model.beacon.MyBeaconData
import com.example.nakatsuka.newgit.navigationAction.*
import kotlinx.android.synthetic.main.activity_main.*
import org.altbeacon.beacon.*
import java.util.*


private val RESULT_SUBACTIVITY: Int = 1000
//位置情報取得のために必要な定数（ここで設定しさえすれば、どんな値でも良いです）
private val PERMISSION_REQUEST_COARSE_LOCATION = 1
//Bluetoothをオンにするために必要な定数（ここで設定しさえすれば、どんな値でも良いです）
private val REQUEST_ENABLE_BT = 1

private const val IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"

/*Todo fragmentの処理　
  Todo APITestの部分の差し替え
  Todo Goal後のアラートの実装*/
class MainActivity : AppCompatActivity(),  StampFragment.fragmentListner,BeaconConsumer {
    private val TAG = this.javaClass.simpleName




    private val mRegion = Region("APIAndBeaconModuleRegion", Identifier.parse("0f0ba8e1-f98a-4f59-af70-91308c6620b5"), null, null)
    private lateinit var mBeaconManager: BeaconManager
    var onBeaconDataIsUpdated: ((beaconListModel: MutableCollection<MyBeaconData>) -> Unit)? = null
    private var mBeaconScanStartTime = Date().time
    private var mBeaconScanIsFinished = false





    private lateinit var prefer: SharedPreferences
    private lateinit var stampFragment: StampFragment
    val buttonResult: IntArray = intArrayOf(0, 0, 0, 0, 0, 0, 0)
    private var imageURL = arrayListOf<String>("","","","","","","")
    var uuid : String =  ""
    var goalApiIsCalled = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        mBeaconManager = BeaconManager.getInstanceForApplication(this)
        // iBeaconの受信設定：iBeaconのフォーマットを登録する
        mBeaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(IBEACON_FORMAT))

        mBeaconManager.bind(this)


        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        pref.apply {
            buttonResult[0] = pref.getInt("buttonResult[0]", 0)
            buttonResult[1] = pref.getInt("buttonResult[1]", 0)
            buttonResult[2] = pref.getInt("buttonResult[2]", 0)
            buttonResult[3] = pref.getInt("buttonResult[3]", 0)
            buttonResult[4] = pref.getInt("buttonResult[4]", 0)
            buttonResult[5] = pref.getInt("buttonResult[5]", 0)
            buttonResult[6] = pref.getInt("buttonResult[6]", 0)
            goalApiIsCalled = pref.getBoolean("goalApiIsCalled",false)
            imageURL[1] = pref.getString("imageURL[1]","URL")
            imageURL[2] = pref.getString("imageURL[2]","URL")
            imageURL[3] = pref.getString("imageURL[3]","URL")
            imageURL[4] = pref.getString("imageURL[4]","URL")
            imageURL[5] = pref.getString("imageURL[5]","URL")
            imageURL[6] = pref.getString("imageURL[6]","URL")
            uuid = pref.getString("uuid","uuid")
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
            val editor = pref.edit()
            editor.putString("uuid",prefer.getString("UUID",""))
            uuid = prefer.getString("UUID","")
            editor.apply()

            Log.d("UUID",uuid)
            bnd.putString("UUID", uuid)
            bnd.putString("USERNAME", prefer.getString("USERNAME", ""))
            //bundleを用いてbuttonResultをfragmentに提供
            bnd.putIntArray("buttonResult", buttonResult)
            bnd.putBoolean("goalApiIsCalled",goalApiIsCalled)
            bnd.putStringArrayList("imageURL",imageURL)


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

    override fun onDestroy() {
        super.onDestroy()
        mBeaconManager.unbind(this)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(this)
                    .setTitle("確認")
                    .setMessage("アプリを終了しますか？")
                    .setPositiveButton("OK") { _, _ ->
                        this.moveTaskToBack(true)
                    }
                    .setNegativeButton("キャンセル") { _, _ ->
                    }
                    .show()
        }
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
                editor.putInt("buttonResult["+"${answerNumber}"+"]", 2)
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
                    bnd.putStringArrayList("imageURL",imageURL)
                    bnd.putString("UUID", uuid)
                    Log.d("UUID",uuid)
                    bnd.putString("USERNAME", prefer.getString("USERNAME", ""))

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

    override fun goActivity(answerNumber: Int, isSend: Boolean) {
        val intent = Intent(this, SecondActivity::class.java)
        if (buttonResult[answerNumber] == 1) {

            intent.putExtra("AnswerNumber", answerNumber)
            if (isSend) {
                startActivityForResult(intent, RESULT_SUBACTIVITY)
            }
        }
    }


    override fun take1(answerNumber: Int) {
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()
        editor.putInt("buttonResult["+"${answerNumber}"+"]", 1)
        editor.apply()
        buttonResult[answerNumber!!] = 1
    }

    override fun takeGoal(){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()
        editor.putBoolean("goalIsApiCalled",true)
        editor.apply()
        goalApiIsCalled = true
    }

    override fun saveURL(quizCode: Int,imageURL:String){
        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = pref.edit()
        editor.putString("imageURL["+"${quizCode}"+"]", imageURL)
        editor.apply()

    }

    private fun makeToast(message: String, x: Int, y: Int) {
        val toast: Toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, x, y / 4)
        toast.show()
    }

    override fun goActivity(answerNumber: Int ,isSend: Boolean,imageUrl:String) {
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("AnswerNumber", answerNumber)
        if (buttonResult[answerNumber] == 1) {
            if(isSend)
                startActivityForResult(intent, RESULT_SUBACTIVITY)

        }
    }











    override fun onBeaconServiceConnect() {
        Log.d(TAG,"onBeaconServiceConnect called")
        var callBackCalled = false
        val myBeaconDataList: MutableList<MyBeaconData> = mutableListOf()
        //レンジングのイベント
        mBeaconManager.addRangeNotifier { beaconList, _ ->
            if (Date().time - mBeaconScanStartTime < 3000) {
                callBackCalled = false
                //Android端末によってはbeaconが全然取れないので、（とりあえずの処理として）3倍する
                //TODO:もっと「いい」方法に…
                Log.d(TAG,"Ranging. . . ")
                for (i in 1..3) {
                    myBeaconDataList.addAll(beaconList.asSequence().map { MyBeaconData(it.id2.toInt(), it.rssi) })
                }
            } else {
                mBeaconManager.stopRangingBeaconsInRegion(mRegion)
                //全ループ終了後に非同期処理を行う
                onBeaaconDataUpdate(myBeaconDataList)
            }
        }
    }

    @Synchronized
    fun rangeBeacon(overWriteFun: (MutableCollection<MyBeaconData>) -> Unit) {
        mBeaconManager.startRangingBeaconsInRegion(mRegion)
        mBeaconScanStartTime = Date().time
        onBeaconDataIsUpdated = overWriteFun
        mBeaconScanIsFinished = false
        Log.d("DEBAG","Rangebeacon called from origin")
    }

    @Synchronized
    fun onBeaaconDataUpdate(myBeaconDataList: MutableList<MyBeaconData>) {
        if (!mBeaconScanIsFinished) {
            mBeaconScanIsFinished = true
            Handler().post {
                onBeaconDataIsUpdated?.invoke(myBeaconDataList)
                myBeaconDataList.clear()
            }
        }
    }









    override fun onResume() {
        super.onResume()
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }
}



