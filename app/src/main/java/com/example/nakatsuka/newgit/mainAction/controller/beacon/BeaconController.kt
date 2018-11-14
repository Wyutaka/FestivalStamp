package com.example.nakatsuka.newgit.mainAction.controller.beacon

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.example.nakatsuka.newgit.BuildConfig
import com.example.nakatsuka.newgit.mainAction.model.beacon.MyBeaconData
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.Identifier
import org.altbeacon.beacon.Region
import org.altbeacon.beacon.BeaconParser
import java.util.*

const val IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
private val mRegion = Region("APIAndBeaconModuleRegion", Identifier.parse("0f0ba8e1-f98a-4f59-af70-91308c6620b5"), null, null)
private var startTime = Date().time
private val handler: Handler = Handler()





class BeaconController(val parentContext: Context) : BeaconConsumer {
    //一定時間単位でBeacon位置を収集するリスト

    private val TAG = this.javaClass.simpleName

    private lateinit var mBeaconManager: BeaconManager

    /*クラス生成時に、IActivityLifeCycleによって実行される*/
    fun onCreated() {
        mBeaconManager = BeaconManager.getInstanceForApplication(parentContext)
        // iBeaconの受信設定：iBeaconのフォーマットを登録する
        mBeaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(IBEACON_FORMAT))

    }


    override fun onBeaconServiceConnect() {
        val myBeaconDataList: MutableList<MyBeaconData> = mutableListOf()
        Log.i(TAG, "onBeaconServiceConnect called")
        // レンジングのイベント。設定
        mBeaconManager.addRangeNotifier { beaconList, _ ->
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "beaconList.size = ${beaconList.size}")
            }
           if (Date().time - startTime < 3000) {
                myBeaconDataList.addAll(beaconList.asSequence().map { MyBeaconData(it.id2.toInt(), it.rssi) })
            } else {
                Log.d(TAG, "time passed: ${Date().time - startTime}, count: ${myBeaconDataList.size}")
                mBeaconManager.stopRangingBeaconsInRegion(mRegion)
                Toast.makeText(parentContext, "BeaconSize:${myBeaconDataList.size}", Toast.LENGTH_SHORT).show()
                //全ループ終了後に非同期処理を行う
                handler.post {
                    onBeaconDataIsUpdated?.invoke(myBeaconDataList)
                    myBeaconDataList.clear()
                }
            }
        }
    }

    //3秒間のビーコン取得後に実行したいコールバックメソッド。thisを使う側のクラスから書き換える。
    var onBeaconDataIsUpdated: ((beaconListModel: MutableCollection<MyBeaconData>) -> Unit)? = null

    fun rangeBeacon(overWriteFun: (MutableCollection<MyBeaconData>) -> Unit) {
        Log.i(TAG, "rangeBeacon Called")
        mBeaconManager.startRangingBeaconsInRegion(mRegion)
        startTime = Date().time
        onBeaconDataIsUpdated = overWriteFun
    }

    fun bind(bc: BeaconConsumer) = mBeaconManager.bind(bc)
    fun unbind(bc: BeaconConsumer) = mBeaconManager.unbind(bc)

    override fun getApplicationContext(): Context = parentContext.applicationContext
    override fun unbindService(p0: ServiceConnection?)
    {
        unbind(parentContext as BeaconConsumer)
        parentContext.unbindService(p0)
    }
    override fun bindService(p0: Intent?, p1: ServiceConnection?, p2: Int): Boolean
    {
        bind(parentContext as BeaconConsumer)
        return parentContext.bindService(p0, p1, p2)
    }

}
